package com.example.speech.aiservice.vn.service.google;

import com.example.speech.aiservice.vn.service.filehandler.AudioFileWriterService;
import com.example.speech.aiservice.vn.service.filehandler.FileNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GoogleAudioDownloaderService {

    private final AudioFileWriterService audioFileWriterService;

    @Autowired
    public GoogleAudioDownloaderService(AudioFileWriterService audioFileWriterService) {
        this.audioFileWriterService = audioFileWriterService;
    }

    public void download(String audioUrl, String audioFilePath) throws IOException {
        // Generate URL from audio path
        URL url = new URL(audioUrl);

        // Open an HTTP connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get audio data
            InputStream inputStream = connection.getInputStream();

            audioFileWriterService.writeToFile(inputStream, audioFilePath);

        } else {
            System.out.println("Unable to load audio. Status code : " + connection.getResponseCode());
        }
        connection.disconnect();
    }
}
