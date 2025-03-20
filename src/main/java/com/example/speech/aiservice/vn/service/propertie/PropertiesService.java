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
}
