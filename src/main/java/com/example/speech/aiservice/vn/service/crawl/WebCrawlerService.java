package com.example.speech.aiservice.vn.service.crawl;

import com.example.speech.aiservice.vn.dto.response.WebCrawlResponseDTO;
import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.service.filehandler.FileNameService;
import com.example.speech.aiservice.vn.service.filehandler.FileWriterService;
import com.example.speech.aiservice.vn.service.propertie.PropertiesService;
import com.example.speech.aiservice.vn.service.wait.WaitService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class WebCrawlerService {
    private final FileNameService fileNameService;
    private final FileWriterService fileWriterService;
    private final WaitService waitService;
    private final PropertiesService propertiesService;

    @Autowired
    public WebCrawlerService(FileNameService fileNameService, FileWriterService fileWriterService, WaitService waitService, PropertiesService propertiesService) {
        this.fileNameService = fileNameService;
        this.fileWriterService = fileWriterService;
        this.waitService = waitService;
        this.propertiesService = propertiesService;
    }

    public WebCrawlResponseDTO webCrawlResponseDTO(WebDriver driver, Novel novel, Chapter chapter) throws InterruptedException {

        String contentDirectoryPath = propertiesService.getContentDirectory();
        String contentFileExtension = propertiesService.getContentFileExtension();

        driver.get(chapter.getLink());

        waitService.waitForSeconds(15); // Wait for the translation to be complete

        String pageSource = driver.getPageSource();

        Document doc = Jsoup.parse(pageSource);

        String content = doc.select("#svelte > div.tm-light.rd-ff-0.rd-fs-3.svelte-19vhflx > main > article:nth-child(6)").text();

        if (content.isEmpty()) {
            content = doc.select("#svelte > div.tm-light.rd-ff-0.rd-fs-3.svelte-19vhflx > main > article:nth-child(5)").text();
        }

//        String safeNovelTitle = fileNameService.sanitizeFileName(novel.getTitle());
//        String novelDirectory = directoryPath + File.separator + safeNovelTitle;
//        fileNameService.ensureDirectoryExists(novelDirectory);
//
//        String safeChapterTitle = fileNameService.sanitizeFileName(chapter.getTitle());
//
//        String contentFilePath = fileNameService.getAvailableFileName(novelDirectory, safeChapterTitle, fileExtension);
//
//        fileWriterService.writeToFile(contentFilePath, content);

        // Create a folder for the collection if it does not exist.
        String safeNovelTitle = fileNameService.sanitizeFileName(novel.getTitle());
        String novelDirectory = contentDirectoryPath + File.separator + safeNovelTitle;
        fileNameService.ensureDirectoryExists(novelDirectory);

        // Handling valid chapter file names
        String safeChapterTitle = fileNameService.sanitizeFileName(chapter.getTitle()) + contentFileExtension;
        //String contentFilePath = novelDirectory + File.separator + safeChapterTitle;
        String contentFilePath = fileNameService.getAvailableFileName(novelDirectory, safeChapterTitle, contentFileExtension);

        // Write content to file
        fileWriterService.writeToFile(contentFilePath, content);

        return new WebCrawlResponseDTO("Crawling completed", chapter.getLink(), contentFilePath);
    }
}


/**
 * researching
 */
//WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        wait.until(new Function<WebDriver, Boolean>() {
//            @Override
//            public Boolean apply(WebDriver d) {
//                return ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete");
//            }
//        });