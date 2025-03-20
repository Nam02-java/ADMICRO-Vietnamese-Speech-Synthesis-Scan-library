package com.example.speech.aiservice.vn.dto.request;

public class FullProcessRequestDTO {
    private String crawlUrl;
    private String textToSpeechUrl;

    public String getCrawlUrl() {
        return crawlUrl;
    }

    public void setCrawlUrl(String crawlUrl) {
        this.crawlUrl = crawlUrl;
    }

    public String getTextToSpeechUrl() {
        return textToSpeechUrl;
    }

    public void setTextToSpeechUrl(String textToSpeechUrl) {
        this.textToSpeechUrl = textToSpeechUrl;
    }
}
