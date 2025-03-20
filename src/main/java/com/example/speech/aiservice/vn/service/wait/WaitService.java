package com.example.speech.aiservice.vn.service.wait;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class WaitService {
    public void waitForSeconds(int seconds) {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(seconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
