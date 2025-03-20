package com.example.speech.aiservice.vn.service.filehandler;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileWriterService {

    public void writeToFile(String contentFilePath, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(contentFilePath))) {
            writer.write(content);
            System.out.println("The content has been downloaded and saved as :" + contentFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
