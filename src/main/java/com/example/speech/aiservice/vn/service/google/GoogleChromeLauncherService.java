package com.example.speech.aiservice.vn.service.google;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleChromeLauncherService {

    private Process process;
    private String chromePath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    private String userDataDir = "C:\\ChromeProfiles\\";

    public void openGoogleChrome(String port, String seleniumFileName) throws IOException {
        String command = chromePath + " --remote-debugging-port=" + port + " --user-data-dir=" + userDataDir + seleniumFileName + " -headless";
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
    }


    public void shutdown() {
        process.destroy();
    }
}
