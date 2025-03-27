package com.example.speech.aiservice.vn.service.speech;

import com.example.speech.aiservice.vn.dto.response.TextToSpeechResponseDTO;
import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.service.filehandler.FileNameService;
import com.example.speech.aiservice.vn.service.filehandler.FileReaderService;
import com.example.speech.aiservice.vn.service.google.GoogleAudioDownloaderService;
import com.example.speech.aiservice.vn.service.propertie.PropertiesService;
import com.example.speech.aiservice.vn.service.wait.WaitService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SpeechService {
    private final FileNameService fileNameService;
    private final FileReaderService fileReaderService;
    private final WaitService waitService;
    private final GoogleAudioDownloaderService googleAudioDownloaderService;
    private final PropertiesService propertiesService;


    @Autowired
    public SpeechService(FileNameService fileNameService, FileReaderService fileReaderService, WaitService waitService, GoogleAudioDownloaderService googleAudioDownloaderService, PropertiesService propertiesService) {
        this.fileNameService = fileNameService;
        this.fileReaderService = fileReaderService;
        this.waitService = waitService;
        this.googleAudioDownloaderService = googleAudioDownloaderService;
        this.propertiesService = propertiesService;
    }

    public TextToSpeechResponseDTO textToSpeechResponseDTO(WebDriver driver, String textToSpeechUrl, String contentfilePath, Novel novel, Chapter chapter) throws IOException {

        String voiceDirectoryPath = propertiesService.getVoiceDirectory();
        String voiceFileExtension = propertiesService.getVoiceFileExtension();

        driver.get(textToSpeechUrl);

        waitService.waitForSeconds(10);

        String content = fileReaderService.readFileContent(contentfilePath);
        System.out.println(content);

        WebElement textArea = driver.findElement(By.cssSelector("#edit-content"));
        textArea.sendKeys(content);

        waitService.waitForSeconds(2);

        if (textArea.isDisplayed() && textArea.isEnabled()) {
            String contentValue = textArea.getAttribute("value");
            if (contentValue == null || contentValue.trim().isEmpty()) {
                System.out.println("TextArea is empty.");
            } else {
                System.out.println("TextArea contains: " + contentValue);
            }
        } else {
            System.out.println("TextArea is not available.");
        }

        waitService.waitForSeconds(10);
        driver.findElement(By.id("submit_btn")).click();
        waitService.waitForSeconds(15);
        driver.findElement(By.id("submit_btn")).click();
        waitService.waitForSeconds(15);
        driver.findElement(By.id("submit_btn")).click();
        waitService.waitForSeconds(15);

        String audioUrl = driver.findElement(By.id("audio")).getAttribute("src");
        System.out.println("Audio URL: " + audioUrl);

        // Tạo thư mục cho bộ truyện nếu chưa tồn tại
        String safeNovelTitle = fileNameService.sanitizeFileName(novel.getTitle());
        String novelDirectory = voiceDirectoryPath + File.separator + safeNovelTitle;
        fileNameService.ensureDirectoryExists(novelDirectory);

        // Xử lý tên file chương hợp lệ
        String safeChapterTitle = fileNameService.sanitizeFileName(chapter.getTitle()) ;
        //String audioFilePath = novelDirectory + File.separator + safeChapterTitle;
        String audioFilePath = fileNameService.getAvailableFileName(novelDirectory, safeChapterTitle, voiceFileExtension);

        googleAudioDownloaderService.download(audioUrl, audioFilePath);

        waitService.waitForSeconds(10);

        return new TextToSpeechResponseDTO("Successful conversion", textToSpeechUrl, audioUrl, audioFilePath);
    }
}
