package com.example.speech.aiservice.vn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Application.class, args);

        // Get `ShutdownHandler` from Spring Context and register it in JVM
        ShutdownHandler shutdownHandler = context.getBean(ShutdownHandler.class);
        Runtime.getRuntime().addShutdownHook(shutdownHandler);
    }
}

/**
 * https://googlechromelabs.github.io/chrome-for-testing/
 * taskkill /F /IM chrome.exe
 * E:\CongViecHocTap\Picture\20220211142754-margherita-9920_5a73220e-4a1a-4d33-b38f-26e98e3cd986.jpg
 * https://chivi.app/wn/books/254cuyB25ea
 * UPDATE chapter SET is_scanned = 1;
 */
