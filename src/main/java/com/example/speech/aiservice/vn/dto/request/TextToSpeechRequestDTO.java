package com.example.speech.aiservice.vn.dto.request;

public class TextToSpeechRequestDTO {
    private String textToSpeechUrl;
    private String contentPath;


    public String getTextToSpeechUrl() {
        return textToSpeechUrl;
    }

    public void setTextToSpeechUrl(String textToSpeechUrl) {
        this.textToSpeechUrl = textToSpeechUrl;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }
}
