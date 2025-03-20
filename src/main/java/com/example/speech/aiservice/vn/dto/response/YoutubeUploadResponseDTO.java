package com.example.speech.aiservice.vn.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class YoutubeUploadResponseDTO {

    @JsonProperty("message")
    private String message;

    @JsonProperty("url_website")
    private String urlWebsite;

    @JsonProperty("video_path")
    private String videoPath;

    public YoutubeUploadResponseDTO(String message, String urlWebsite, String videoPath) {
        this.message = message;
        this.urlWebsite = urlWebsite;
        this.videoPath = videoPath;
    }


    public String getMessage() {
        return message;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }

    public String getVideoPath() {
        return videoPath;
    }
}
