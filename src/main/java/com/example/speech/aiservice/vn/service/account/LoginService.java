package com.example.speech.aiservice.vn.service.account;

import com.example.speech.aiservice.vn.dto.response.LoginResponseDTO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.example.speech.aiservice.vn.model.entity.User;
import com.example.speech.aiservice.vn.service.propertie.PropertiesService;
import com.example.speech.aiservice.vn.service.repositoryService.UserService;
import com.example.speech.aiservice.vn.service.wait.WaitService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginService {

    private final WaitService waitService;
    private final UserService userService;
    private final PropertiesService propertiesService;

    @Autowired
    public LoginService(WaitService waitService, UserService userService, PropertiesService propertiesService) {
        this.waitService = waitService;
        this.userService = userService;
        this.propertiesService = propertiesService;
    }

    public LoginResponseDTO loginResponseDTO(WebDriver driver) {

        /**
         * Wait few seconds for cloudflare's success icon to appear
         * If it's too fast, cloudflare will consider it a bot and fail to log in.
         */
        waitService.waitForSeconds(7);

        String loginUrl = "https://chivi.app/_u/login";
        driver.get(loginUrl);


        /**
         * Wait few seconds for cloudflare's success icon to appear
         * If it's too fast, cloudflare will consider it a bot and fail to log in.
         */
        waitService.waitForSeconds(7);

        // clear email input
        Actions actions = new Actions(driver);
        WebElement emailInput = driver.findElement(By.xpath("//*[@id=\"email\"]"));
        actions.moveToElement(emailInput).click().perform();
        emailInput.clear();

        // clear password input
        driver.findElement(By.xpath("//*[@id=\"upass\"]")).clear();

        User user = userService.getUserByUsername(propertiesService.getUserEmail());
        String email = user.getUsername();
        String hashedPassword = user.getPasswordHash();
        String password = propertiesService.getUserPassword();


        if (BCrypt.checkpw(password, hashedPassword)) {
            driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys(email);
            driver.findElement(By.xpath("//*[@id=\"upass\"]")).sendKeys(password);
            driver.findElement(By.xpath("//*[@id=\"svelte\"]/div[1]/main/article/form/footer/button")).click();
            waitService.waitForSeconds(7);
        }

        return new LoginResponseDTO("Log in successfully", email, LocalDateTime.now());
    }
}
