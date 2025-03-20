package com.example.speech.aiservice.vn.service.schedule;

import org.springframework.stereotype.Component;

@Component
public class TimeDelay {

    private int second = 100;

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
