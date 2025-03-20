package com.example.speech.aiservice.vn.service.filehandler;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LatestFileFinderService {
    public String getLatestFile(String dirPath, String extension) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }

        File latest = null;
        long lastModified = 0;

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isFile() && file.getName().endsWith(extension)) {
                long modifiedTime = file.lastModified();
                if (latest == null || modifiedTime > lastModified) {
                    latest = file;
                    lastModified = modifiedTime;
                }
            }
        }

        if (latest == null) {
            return null;
        } else {
            return latest.getAbsolutePath();
        }
    }
}
