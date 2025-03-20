package com.example.speech.aiservice.vn.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class TextToSpeechResponseDTO {

    @JsonProperty("message")
    private String message;

    @JsonProperty("url_website")
    private String urlWebsite;

    @JsonProperty("url_download")
    private String urlDownload;

    @JsonProperty("file_path")
    private String filePath;

    public TextToSpeechResponseDTO(String message, String urlWebsite, String urlDownload, String filePath) {
        this.message = message;
        this.urlDownload = urlDownload;
        this.urlWebsite = urlWebsite;
        this.filePath = filePath;
    }


    public String getMessage() {
        return message;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public String getFilePath() {
        return filePath;
    }
}
