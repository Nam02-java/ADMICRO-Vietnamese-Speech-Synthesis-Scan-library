package com.example.speech.aiservice.vn.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class FullProcessResponseDTO {

    public FullProcessResponseDTO( WebCrawlResponseDTO webCrawlResponseDTO, TextToSpeechResponseDTO textToSpeechResponseDTO, CreateVideoResponseDTO createVideoResponseDTO, YoutubeUploadResponseDTO youtubeUploadResponseDTO) {
        this.webCrawlResponseDTO = webCrawlResponseDTO;
        this.textToSpeechResponseDTO = textToSpeechResponseDTO;
        this.createVideoResponseDTO = createVideoResponseDTO;
        this.youtubeUploadResponseDTO = youtubeUploadResponseDTO;
    }

    public FullProcessResponseDTO(LoginResponseDTO loginResponseDTO, WebCrawlResponseDTO webCrawlResponseDTO, TextToSpeechResponseDTO textToSpeechResponseDTO, CreateVideoResponseDTO createVideoResponseDTO, YoutubeUploadResponseDTO youtubeUploadResponseDTO) {
        this.loginResponseDTO = loginResponseDTO;
        this.webCrawlResponseDTO = webCrawlResponseDTO;
        this.textToSpeechResponseDTO = textToSpeechResponseDTO;
        this.createVideoResponseDTO = createVideoResponseDTO;
        this.youtubeUploadResponseDTO = youtubeUploadResponseDTO;
    }

    @JsonProperty("login_response")
    private LoginResponseDTO loginResponseDTO;

    @JsonProperty("web_crawl_response")
    private WebCrawlResponseDTO webCrawlResponseDTO;

    @JsonProperty("text_to_speech_response")
    private TextToSpeechResponseDTO textToSpeechResponseDTO;

    @JsonProperty("create_video_response")
    private CreateVideoResponseDTO createVideoResponseDTO;

    @JsonProperty("youtube_upload_response")
    private YoutubeUploadResponseDTO youtubeUploadResponseDTO;


}

