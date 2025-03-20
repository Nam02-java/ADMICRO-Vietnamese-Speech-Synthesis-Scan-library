package com.example.speech.aiservice.vn.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Ignore null fields when converting to JSON
public class LoginResponseDTO {

    @JsonProperty("message")
    private String message;
    @JsonProperty("user_name")
    private String username;
    @JsonProperty("login_time")
    private LocalDateTime loginTime;


    public LoginResponseDTO(String message, String username, LocalDateTime loginTime) {
        this.message = message;
        this.username = username;
        this.loginTime = loginTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}
