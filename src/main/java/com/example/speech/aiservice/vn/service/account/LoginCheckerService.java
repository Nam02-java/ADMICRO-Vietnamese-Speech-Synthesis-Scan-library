package com.example.speech.aiservice.vn.service.account;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;


@Service
public class LoginCheckerService {

    public boolean isLoggedIn(WebDriver driver) {
        return !driver.findElements(By.xpath("//*[@id=\"svelte\"]/div[1]/header/div[2]/div/div[1]/button")).isEmpty();
    }
}

