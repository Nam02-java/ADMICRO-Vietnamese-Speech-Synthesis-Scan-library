package com.example.speech.aiservice.vn.dto.request;

public class CreateVideoRequestDTO {
    private String audioPath;
    private String imagePath;

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
