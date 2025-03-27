package com.example.speech.aiservice.vn.service.filehandler;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileNameService {
    public synchronized String getAvailableFileName(String directoryPath, String baseFileName, String extension) {
        if (directoryPath == null || baseFileName == null || extension == null) {
            throw new IllegalArgumentException("directoryPath, baseFileName, and extension cannot be null");
        }

        baseFileName = sanitizeFileName(baseFileName); // Thay thế ký tự không hợp lệ bằng "_"

        int fileNumber = 1;
        Path filePath = Paths.get(directoryPath, baseFileName + "(" + fileNumber + ")" + extension);

        while (Files.exists(filePath)) {
            fileNumber++;
            filePath = Paths.get(directoryPath, baseFileName + "(" + fileNumber + ")" + extension);
        }

        return filePath.toString();
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

