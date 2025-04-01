package com.example.speech.aiservice.vn.service.youtube;


import com.example.speech.aiservice.vn.dto.response.YoutubeUploadResponseDTO;
import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.service.wait.WaitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YoutubeUploadService {
    private final OAuthHelper oAuthHelper;
    private final YouTubeUploader youTubeUploader;
    private final WaitService waitService;

    @Autowired
    public YoutubeUploadService(OAuthHelper oAuthHelper, YouTubeUploader youTubeUploader, WaitService waitService) {
        this.oAuthHelper = oAuthHelper;
        this.youTubeUploader = youTubeUploader;
        this.waitService = waitService;
    }

    public YoutubeUploadResponseDTO upload(String videoFilePath, Novel novel, Chapter chapter) {
        try {
            String title = chapter.getTitle();
            String description = novel.getTitle();
            String tags = "api, java, upload";
            String privacyStatus = "public"; // "public", "private", "unlisted"

            String uploadVideoURL = youTubeUploader.uploadVideo(videoFilePath, novel, chapter, title, description, tags, privacyStatus);

            return new YoutubeUploadResponseDTO("Video uploaded successfully", uploadVideoURL, videoFilePath);
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }
}



