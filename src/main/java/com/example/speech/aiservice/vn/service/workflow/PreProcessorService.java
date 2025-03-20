package com.example.speech.aiservice.vn.service.workflow;

import com.example.speech.aiservice.vn.dto.response.NovelInfoResponseDTO;
import com.example.speech.aiservice.vn.model.entity.*;
import com.example.speech.aiservice.vn.model.repository.ChapterRepository;
import com.example.speech.aiservice.vn.model.repository.NovelRepository;
import com.example.speech.aiservice.vn.service.repositoryService.*;
import com.example.speech.aiservice.vn.service.executor.MyRunnableService;
import com.example.speech.aiservice.vn.service.google.GoogleChromeLauncherService;
import com.example.speech.aiservice.vn.service.schedule.TimeDelay;
import com.example.speech.aiservice.vn.service.selenium.WebDriverLauncherService;
import com.example.speech.aiservice.vn.service.wait.WaitService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

@Service
public class PreProcessorService {

    private final GoogleChromeLauncherService googleChromeLauncherService;
    private final WebDriverLauncherService webDriverLauncherService;
    private final WaitService waitService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final TrackedNovelService trackedNovelService;
    private final ExecutorService executorService;
    private final ApplicationContext applicationContext;
    private final SeleniumConfigService seleniumConfigService;
    private volatile boolean stop = false; // Volatile variable to track STOP command - true = stopping
    private volatile String imagePath = null;
    private final TaskScheduler taskScheduler;
    private volatile ScheduledFuture<?> scheduledTask;
    private final TimeDelay timeDelay;

    @Autowired
    public PreProcessorService(GoogleChromeLauncherService googleChromeLauncherService, WebDriverLauncherService webDriverLauncherService, WaitService waitService, NovelRepository novelRepository, ChapterRepository chapterRepository, NovelService novelService, ChapterService chapterService, TrackedNovelService trackedNovelService,  ApplicationContext applicationContext, SeleniumConfigService seleniumConfigService, TaskScheduler taskScheduler, TimeDelay timeDelay) {
        this.googleChromeLauncherService = googleChromeLauncherService;
        this.webDriverLauncherService = webDriverLauncherService;
        this.waitService = waitService;
        this.novelService = novelService;
        this.chapterService = chapterService;
        this.trackedNovelService = trackedNovelService;
        this.applicationContext = applicationContext;
        this.seleniumConfigService = seleniumConfigService;
        this.taskScheduler = taskScheduler;
        this.timeDelay = timeDelay;
        this.executorService = Executors.newFixedThreadPool(3);
    }


    public void startWorkflow(long delay) {
        System.out.println("delay - " + timeDelay.getSecond() + "ms");
        timeDelay.setSecond(0);
        if (scheduledTask != null && !scheduledTask.isDone()) {
            return;
        }
        scheduledTask = taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                executeWorkflow();
                stop = false;
            }
        }, new Date(System.currentTimeMillis() + delay));
    }


    public void executeWorkflow() {
        NovelInfoResponseDTO novelInfo = scanNovelTitle();
        String imagePath = getValidImagePath();
        fetchFullPageContent(novelInfo);

        List<SeleniumConfig> threadConfigs = seleniumConfigService.getAllConfigs();
        List<Chapter> unscannedChapters;
        int maxThreads = 3;

        if (!stop) {
            System.out.println("stop is false");
            while (true) {
                Novel novel = novelService.findByTitle(novelInfo.getTitle());
                unscannedChapters = chapterService.getUnscannedChapters(novel.getId());

                if (unscannedChapters.isEmpty()) {
                    System.out.println("⚡ All chapters have been scanned, scheduling next check...");
                    if (novel != null) {
                        trackedNovelService.trackNovel(novel);
                    }
                    timeDelay.setSecond(10000);
                    return;
                }

                int taskCount = Math.min(maxThreads, unscannedChapters.size());
                CountDownLatch latch = new CountDownLatch(taskCount);

                for (int i = 0; i < taskCount; i++) {
                    if (stop) {
                        System.out.println("STOP command received! No new tasks will be started.");
                        timeDelay.setSecond(5000);
                        return;
                    }

                    Chapter chapter = unscannedChapters.get(i);
                    SeleniumConfig config = threadConfigs.get(i);

                    FullWorkFlow fullWorkFlow = applicationContext.getBean(FullWorkFlow.class);
                    MyRunnableService myRunnableService = new MyRunnableService(fullWorkFlow, config.getPort(), config.getSeleniumFileName(), chapter.getNovel(), chapter, imagePath);
                    executorService.execute(() -> {
                        try {
                            myRunnableService.run();
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    System.err.println("Error completing task : " + e.getMessage());
                }
                System.out.println("Complete threads, continue scanning...");
            }
        } else {
            System.out.println("stop is true");
            stopConditions();
            timeDelay.setSecond(5000);
        }
    }

    private NovelInfoResponseDTO scanNovelTitle() {
        Scanner scanner = new Scanner(System.in);
        String inputLink;

        Optional<TrackedNovel> trackedNovelOptional = trackedNovelService.getTrackedNovel();

        if (!trackedNovelOptional.isPresent()) {
            while (true) {

                System.out.println("Enter the novel link to scan: ");
                inputLink = scanner.nextLine().trim();

                if (!inputLink.isEmpty()) {
                    try {
                        Document doc = Jsoup.connect(inputLink).get();
                        String title = doc.title();
                        String safeTitle = title.split(" - ", 2)[0].trim();
                        System.out.println("Title: " + safeTitle);

                        return new NovelInfoResponseDTO(safeTitle, inputLink);
                    } catch (Exception e) {
                        //System.err.println("Error fetching novel title: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            String novelTitle = trackedNovelOptional.get().getNovel().getTitle();
            String novelLink = trackedNovelOptional.get().getNovel().getLink();
            return new NovelInfoResponseDTO(novelTitle, novelLink);
        }
    }

    private String getValidImagePath() {
        Scanner scanner = new Scanner(System.in);
        String input;
        if (imagePath == null) {
            while (true) {
                System.out.println("Enter image path: ");
                input = scanner.nextLine().trim();
                imagePath = input;
                return imagePath;
            }
        }
        return imagePath;
    }

    private void fetchFullPageContent(NovelInfoResponseDTO novelInfo) {

        WebDriver driver = null;
        String defaultPort = "9222";

        if (!novelService.isNovelExists(novelInfo.getTitle()) || trackedNovelService.isTrackNovellExists(novelInfo.getTitle())) {

            try {
                SeleniumConfig seleniumConfig = seleniumConfigService.getConfigByPort(defaultPort);
                if (seleniumConfig == null) {
                    System.out.println("Could not find configuration with port " + defaultPort);
                    System.exit(1);
                }

                googleChromeLauncherService.openGoogleChrome(seleniumConfig.getPort(), seleniumConfig.getSeleniumFileName());
                driver = webDriverLauncherService.initWebDriver(seleniumConfig.getPort());

                driver.get(novelInfo.getLink());

                waitService.waitForSeconds(1);
                driver.findElement(By.xpath("//*[@id=\"svelte\"]/div[1]/main/article[1]/div[2]/div[1]/svelte-css-wrapper/div/div[1]/button")).click();
                waitService.waitForSeconds(1);
                driver.findElement(By.xpath("//*[@id=\"svelte\"]/div[1]/main/article[1]/div[2]/div[1]/svelte-css-wrapper/div/div[2]/div/a[1]/span[1]")).click();

                waitService.waitForSeconds(3);
                driver.navigate().refresh();
                waitService.waitForSeconds(3);

//            WebElement novelElement = driver.findElement(By.cssSelector("nav.bread a[href*='/wn/books']:nth-last-of-type(2) span"));
//            String novelTitle = novelElement.getText();
//            String novelLink = novelElement.findElement(By.xpath("./parent::a")).getAttribute("href");

                Novel novel = new Novel(novelInfo.getTitle(), novelInfo.getLink());
                if (!novelService.isNovelExists(novel.getTitle())) {
                    novelService.saveNovel(novel);
                }

                novel = novelService.findByTitle(novel.getTitle()); // Retrieve saved object from database
                if (novel != null) {
                    trackedNovelService.trackNovel(novel); // Always follow novel
                }

                // Get a list of pressable buttons
                List<WebElement> buttons = driver.findElements(By.cssSelector("section.article._padend.island button.cpage.svelte-ssn7if"));

                while (true) {
                    boolean hasClicked = false;
                    for (WebElement button : buttons) {

                        try {
                            WebElement svgIcon = button.findElement(By.cssSelector("svg.m-icon use"));
                            String iconHref = svgIcon.getAttribute("xlink:href");
                            if (iconHref.equals("/icons/tabler.svg#chevron-down")) {
                                button.click();
                                waitService.waitForSeconds(1);
                                hasClicked = true;
                            }
                        } catch (NoSuchElementException e) {
                            System.out.println(e);
                        }

                        /**
                         * only stop when crawling chapters on the website and not saving anything to the database
                         */
                        if (stop) {
                            System.out.println("stop at fetchFullPageContent method");
                            return;
                        }
                    }

                    if (!hasClicked) {
                        break; // Exit the loop if there are no buttons to click
                    }
                    buttons = driver.findElements(By.cssSelector("section.article._padend.island button.cpage.svelte-ssn7if"));
                }

                waitService.waitForSeconds(2);

                // Get the list of chapters
                List<WebElement> chapters = driver.findElements(By.cssSelector("div.chaps a.cinfo"));

                // Browse each chapter to get information
                for (WebElement chapter : chapters) {
                    try {

                        String chapterTitle = chapter.findElement(By.cssSelector("span.title")).getText();
                        String chapterNumberText = chapter.findElement(By.cssSelector("span.ch_no")).getText();
                        String chapterLink = chapter.getAttribute("href");

                        String chapterNumber = chapterNumberText.replaceAll("[^0-9]", "");

                        System.out.println("Chapter " + chapterNumberText + ": " + chapterTitle);
                        System.out.println("Link: " + chapterLink);
                        System.out.println("-------------------------");

                        chapterService.addChapter(novel, Integer.valueOf(chapterNumber), chapterTitle, chapterLink);


                    } catch (RuntimeException e) {
                        System.out.println("Skip chapter due to error : " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error outside the protocol : " + e.getMessage());
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                googleChromeLauncherService.shutdown();
                webDriverLauncherService.shutDown(driver);
            }
        } else {
            System.out.println(String.format("%s with link: %s already exists in the database system, stop crawling from the website! ", novelInfo.getTitle(), novelInfo.getLink()));
        }
    }

    public void stopConditions() {
        trackedNovelService.clearTracking();
        imagePath = null;
        stop = true;
    }
}
