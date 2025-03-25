package com.example.speech.aiservice.vn.service.workflow;

import com.example.speech.aiservice.vn.dto.response.*;
import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.service.account.LoginCheckerService;
import com.example.speech.aiservice.vn.service.account.LoginService;
import com.example.speech.aiservice.vn.service.propertie.PropertiesService;
import com.example.speech.aiservice.vn.service.repositoryService.ChapterService;
import com.example.speech.aiservice.vn.service.crawl.WebCrawlerService;
import com.example.speech.aiservice.vn.service.google.GoogleChromeLauncherService;
import com.example.speech.aiservice.vn.service.repositoryService.TrackUploadService;
import com.example.speech.aiservice.vn.service.selenium.WebDriverLauncherService;
import com.example.speech.aiservice.vn.service.speech.SpeechService;
import com.example.speech.aiservice.vn.service.video.VideoCreationService;
import com.example.speech.aiservice.vn.service.youtube.YoutubeUploadService;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype") // Create a new instance every time you call
public class FullWorkFlow {
    private final GoogleChromeLauncherService googleChromeLauncherService;
    private final WebDriverLauncherService webDriverLauncherService;
    private final LoginCheckerService loginCheckerService;
    private final LoginService loginService;
    private final WebCrawlerService webCrawlerService;
    private final SpeechService speechService;
    private final VideoCreationService videoCreationService;
    private final YoutubeUploadService youtubeUploadService;
    private final ChapterService chapterService;
    private final TrackUploadService trackUploadService;
    private final PropertiesService propertiesService;

    // Constructor Injection
    @Autowired
    public FullWorkFlow(GoogleChromeLauncherService googleChromeLauncherService, WebDriverLauncherService webDriverLauncherService, LoginCheckerService loginCheckerService, LoginService loginService, WebCrawlerService webCrawlerService, SpeechService speechService, VideoCreationService videoCreationService, YoutubeUploadService youtubeUploadService, ChapterService chapterService, TrackUploadService trackUploadService, PropertiesService propertiesService) {
        this.googleChromeLauncherService = googleChromeLauncherService;
        this.webDriverLauncherService = webDriverLauncherService;
        this.loginCheckerService = loginCheckerService;
        this.loginService = loginService;
        this.webCrawlerService = webCrawlerService;
        this.speechService = speechService;
        this.videoCreationService = videoCreationService;
        this.youtubeUploadService = youtubeUploadService;
        this.chapterService = chapterService;
        this.trackUploadService = trackUploadService;
        this.propertiesService = propertiesService;
    }

    public void runProcess(String port, String seleniumFileName, Novel novel, Chapter chapter, String imagePath) {
        FullProcessResponseDTO fullProcessResponseDTO = fullProcessResponseDTO(port, seleniumFileName, novel, chapter, imagePath);
    }

    private FullProcessResponseDTO fullProcessResponseDTO(String port, String seleniumFileName, Novel novel, Chapter chapter, String imagePath) {
        WebDriver chromeDriver = null;
        String homepageUrl = propertiesService.getHomePageUrl();
        String textToSpeechUrl = propertiesService.getTextToSpeechUrl();

        trackUploadService.saveTrack(novel.getId(), chapter.getId());

//        TrackUpload firstTrack = trackUploadService.findByNovelAndChapter(novel.getId(), chapter.getId());
//        if (firstTrack != null) {
//            trackUploadService.deleteById(firstTrack.getId());
//        }

        try {

            googleChromeLauncherService.openGoogleChrome(port, seleniumFileName);
            chromeDriver = webDriverLauncherService.initWebDriver(port);

            chromeDriver.get(homepageUrl);

            // Check to see if you are logged in or not
            boolean loggedIn = loginCheckerService.isLoggedIn(chromeDriver);

            //If not, then log in
            LoginResponseDTO loginResponseDTO = null;
            if (!loggedIn) {
                loginResponseDTO = loginService.loginResponseDTO(chromeDriver);
                System.out.println(Thread.currentThread().getId() + " - " + loginResponseDTO.getMessage());
            } else {
                loginResponseDTO = new LoginResponseDTO("Logged in before", null, null);
                System.out.println(Thread.currentThread().getId() + " - " + loginResponseDTO.getMessage());
            }

            // Crawl data on Chivi.App website
            WebCrawlResponseDTO webCrawlResponseDTO = webCrawlerService.webCrawlResponseDTO(chromeDriver, novel, chapter);

            // Convert text to speech with ADMICRO | Vietnamese Speech Synthesis
            TextToSpeechResponseDTO textToSpeechResponseDTO = speechService.textToSpeechResponseDTO(chromeDriver, textToSpeechUrl, webCrawlResponseDTO.getContentFilePath(), novel, chapter);

            // Create videos using mp4 files combined with photos
            CreateVideoResponseDTO createVideoResponseDTO = videoCreationService.createVideoResponseDTO(textToSpeechResponseDTO.getFilePath(), imagePath, novel, chapter);

            //Upload video to youtube with youtube data API
            YoutubeUploadResponseDTO youtubeUploadResponseDTO = youtubeUploadService.upload(createVideoResponseDTO.getCreatedVideoFilePath(), novel, chapter);

            //Aggregated DTO response
            FullProcessResponseDTO fullProcessResponse = new FullProcessResponseDTO(loginResponseDTO, webCrawlResponseDTO, textToSpeechResponseDTO, createVideoResponseDTO, youtubeUploadResponseDTO);

            chapterService.markChapterAsScanned(chapter);

            return fullProcessResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if (chromeDriver != null) {
                webDriverLauncherService.shutDown(chromeDriver);
            }
            googleChromeLauncherService.shutdown();
        }
    }
}
