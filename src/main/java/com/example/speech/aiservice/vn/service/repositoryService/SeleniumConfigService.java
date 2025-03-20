package com.example.speech.aiservice.vn.service.repositoryService;
import com.example.speech.aiservice.vn.model.entity.SeleniumConfig;
import com.example.speech.aiservice.vn.model.repository.SeleniumConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeleniumConfigService {

    private final SeleniumConfigRepository seleniumConfigRepository;

    @Autowired
    public SeleniumConfigService(SeleniumConfigRepository seleniumConfigRepository) {
        this.seleniumConfigRepository = seleniumConfigRepository;
    }

    public List<SeleniumConfig> getAllConfigs() {
        return seleniumConfigRepository.findAll();
    }

    public SeleniumConfig saveConfig(SeleniumConfig config) {
        return seleniumConfigRepository.save(config);
    }

    public SeleniumConfig getConfigByPort(String port) {
        return seleniumConfigRepository.findByPort(port);
    }
}
