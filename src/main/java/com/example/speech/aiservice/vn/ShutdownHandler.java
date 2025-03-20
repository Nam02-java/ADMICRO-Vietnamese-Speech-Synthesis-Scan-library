package com.example.speech.aiservice.vn;

import org.springframework.stereotype.Component;

@Component
public class ShutdownHandler extends Thread {

    @Override
    public void run() {
        System.out.println("⚠\uFE0F Application is shutting down... Force stopping NOW ⚠\uFE0F ");
        Runtime.getRuntime().halt(0);  // Force JVM to stop immediately

    }
}
