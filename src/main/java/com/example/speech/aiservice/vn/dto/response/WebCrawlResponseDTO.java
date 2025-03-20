package com.example.speech.aiservice.vn.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class WebCrawlResponseDTO {

    @JsonProperty("message")
    private String message;

    @JsonProperty("url_website")
    private String urlWebsite;

    @JsonProperty("content_file_path")
    private String contentFilePath;

    public WebCrawlResponseDTO(String message, String urlWebsite, String contentFilePath) {
        this.message = message;
        this.urlWebsite = urlWebsite;
        this.contentFilePath = contentFilePath;
    }


    public String getContentFilePath() {
        return contentFilePath;
    }

    public void setContentFilePath(String contentFilePath) {
        this.contentFilePath = contentFilePath;
    }
}
