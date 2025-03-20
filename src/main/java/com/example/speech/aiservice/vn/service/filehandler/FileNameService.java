package com.example.speech.aiservice.vn.service.filehandler;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileNameService {
    public synchronized String getAvailableFileName(String directoryPath, String baseFileName, String extension) {
        baseFileName = sanitizeFileName(baseFileName); // Replace invalid characters with "_"

        int fileNumber = 1;
        String fileName = baseFileName + "(" + fileNumber + ")" + extension;

        while (Files.exists(Paths.get(directoryPath + fileName))) {
            fileNumber++;
            fileName = baseFileName + "(" + fileNumber + ")" + extension;
        }
        return directoryPath + fileName;
    }

    public synchronized void ensureDirectoryExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created directory: " + path);
            } else {
                System.err.println("Failed to create directory: " + path);
            }
        }
    }

    public synchronized String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[<>:\"/\\\\|?*]", "_");
    }
}

