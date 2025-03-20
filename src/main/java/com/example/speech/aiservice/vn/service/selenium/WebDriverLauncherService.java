package com.example.speech.aiservice.vn.service.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class WebDriverLauncherService {


    public WebDriver initWebDriver(String localhost) {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:" + localhost);

        return new ChromeDriver(options);
    }

    public void shutDown(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}
