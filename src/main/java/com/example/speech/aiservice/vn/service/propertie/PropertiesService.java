package com.example.speech.aiservice.vn.service.propertie;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Service
public class PropertiesService {

    private final Environment environment;

    @Autowired
    public PropertiesService(Environment environment) {
        this.environment = environment;
    }

    public String getUserEmail() {
        return environment.getProperty("app.user.email");
    }

    public String getUserPassword() {
        return environment.getProperty("app.user.password");
    }

    public String getImageDirectory() {
        return environment.getProperty("app.image.directory");
    }

    public String getBaseLibraryURL() {
        return environment.getProperty("app.base.library.url");
    }

    public String getDefaultPort() {
        return environment.getProperty("app.default.port");
    }

    public String getImageExtension() {
        return environment.getProperty("app.image.extension");
    }

    public String getHomePageUrl() {
        return environment.getProperty("app.url.homepage");
    }

    public String getTextToSpeechUrl() {
        return environment.getProperty("app.url.text-to-speech");
    }

    public String getChromePath() {
        return environment.getProperty("app.chrome.path");
    }

    public String getUserDataDir() {
        return environment.getProperty("app.chrome.user-data-dir");
    }

    public String getContentDirectory() {
        return environment.getProperty("app.content.directory");
    }

    public String getContentFileExtension() {
        return environment.getProperty("app.content.extension");
    }

    public String getFfmpegPath() {
        return environment.getProperty("app.ffmpeg.path");
    }

    public String getVoiceDirectory() {
        return environment.getProperty("app.voice.directory");
    }

    public String getVoiceFileExtension() {
        return environment.getProperty("app.voice.file-extension");
    }

    public String getUploadDirectory() {
        return environment.getProperty("app.upload.directory");
    }

    public String getUploadFileExtension() {
        return environment.getProperty("app.upload.file-extension");
    }

    public String getOAuthClientSecretFile() {
        return environment.getProperty("app.oauth.client-secret-file");
    }

    public String getOAuthTokensDirectory() {
        return environment.getProperty("app.oauth.tokens-directory");
    }
}
