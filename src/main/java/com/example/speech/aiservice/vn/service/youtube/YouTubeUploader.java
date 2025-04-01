package com.example.speech.aiservice.vn.service.youtube;

import com.example.speech.aiservice.vn.model.entity.TrackUpload;
import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.service.repositoryService.TrackUploadService;
import com.example.speech.aiservice.vn.service.wait.WaitService;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class YouTubeUploader {

    private final OAuthHelper oAuthHelper;
    private final TrackUploadService trackUploadService;
    private final WaitService waitService;

    @Autowired
    public YouTubeUploader(OAuthHelper oAuthHelper, TrackUploadService trackUploadService, WaitService waitService) {
        this.oAuthHelper = oAuthHelper;
        this.trackUploadService = trackUploadService;
        this.waitService = waitService;
    }

    // Upload video to YouTube
    public String uploadVideo(String videoFilePath, Novel novel, Chapter chapter, String title, String description, String tags, String privacyStatus) throws Exception {
        YouTube youtubeService = oAuthHelper.getService();

        // Configure video metadata
        Video video = new Video();
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus(privacyStatus);
        video.setStatus(status);

        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription(description);
        snippet.setTags(Collections.singletonList(tags));
        video.setSnippet(snippet);

        File mediaFile = new File(videoFilePath);
        FileContent mediaContent = new FileContent("video/*", mediaFile);

        while (true) {
            List<TrackUpload> trackUploadList = trackUploadService.findAll();
            // Arrange from smallest to largest based on chapter number
            for (int i = 0; i < trackUploadList.size() - 1; i++) {
                for (int j = i + 1; j < trackUploadList.size(); j++) {
                    if (trackUploadList.get(i).getChapter().getChapterNumber() > trackUploadList.get(j).getChapter().getChapterNumber()) {
                        TrackUpload temp = trackUploadList.get(i);
                        trackUploadList.set(i, trackUploadList.get(j));
                        trackUploadList.set(j, temp);
                    }
                }
            }
            if (trackUploadList.isEmpty()) {
                break;
            }
            if (trackUploadList.get(0).getChapter().getChapterNumber() == chapter.getChapterNumber()) {

                TrackUpload firstTrack = trackUploadService.findByNovelAndChapter(novel.getId(), chapter.getId());

                if (firstTrack != null) {
                    trackUploadService.deleteById(firstTrack.getId());
                }
                // Send upload request
                while (true) {
                    try {
                        YouTube.Videos.Insert request = youtubeService.videos().insert("snippet,status", video, mediaContent);
                        Video response = request.execute();
                        String videoId = response.getId();
                        String uploadedVideoURL = "https://www.youtube.com/watch?v=" + videoId;
                        System.out.printf("%s - uploaded at: %s%n", title, uploadedVideoURL);
                        return uploadedVideoURL;
                    } catch (GoogleJsonResponseException e) {
                        if (e.getDetails() != null && e.getDetails().getErrors() != null) {
                            boolean retry = false;
                            for (GoogleJsonError.ErrorInfo error : e.getDetails().getErrors()) {
                                if ("uploadLimitExceeded".equals(error.getReason())) {
                                    e.printStackTrace();
                                    System.out.println("Upload limit exceeded. Retrying in 10 minutes...");
                                    waitService.waitForSeconds(10); // default wait 10 minutes
                                    retry = true;
                                    break;
                                }
                            }

                            /**
                             * Continue looping if upload error Limit Exceeded
                             * Continue until video upload is successful
                             */
                            if (retry) {
                                continue;
                            }
                        }
                        throw e; // If other error, exit immediately
                    }
                }

            } else {
                waitService.waitForSeconds(5);
            }
        }
        return null;
    }
}
