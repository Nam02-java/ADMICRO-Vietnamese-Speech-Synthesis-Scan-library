package com.example.speech.aiservice.vn.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class CreateVideoResponseDTO {

    @JsonProperty("message")
    private String message;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("audio_path")
    private String audioPath;

    @JsonProperty("created_video_file_path")
    private String createdVideoFilePath;

    public CreateVideoResponseDTO(String message, String imagePath, String audioPath, String createdVideoFilePath) {
        this.message = message;
        this.imagePath = imagePath;
        this.audioPath = audioPath;
        this.createdVideoFilePath = createdVideoFilePath;
    }

    public String getCreatedVideoFilePath() {
        return createdVideoFilePath;
    }
}
