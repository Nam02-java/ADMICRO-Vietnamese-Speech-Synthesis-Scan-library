package com.example.speech.aiservice.vn.service.google;

import com.example.speech.aiservice.vn.service.propertie.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleChromeLauncherService {

    private Process process;
    private PropertiesService propertiesService;

    @Autowired
    public GoogleChromeLauncherService(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    public void openGoogleChrome(String port, String seleniumFileName) throws IOException {
        String chromePath = propertiesService.getChromePath();
        String userDataDir = propertiesService.getUserDataDir();

        String command = chromePath + " --remote-debugging-port=" + port + " --user-data-dir=" + userDataDir + seleniumFileName + " -headless";
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
    }

    public void shutdown() {
        process.destroy();
    }
}
