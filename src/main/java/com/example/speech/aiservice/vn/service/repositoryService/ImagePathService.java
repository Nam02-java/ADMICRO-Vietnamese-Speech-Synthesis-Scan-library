package com.example.speech.aiservice.vn.service.repositoryService;

import com.example.speech.aiservice.vn.model.entity.ImagePath;
import com.example.speech.aiservice.vn.model.repository.ImagePathRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImagePathService {

    private final ImagePathRepository imagePathRepository;

    @Autowired
    public ImagePathService(ImagePathRepository imagePathRepository) {
        this.imagePathRepository = imagePathRepository;
    }

    public void saveImagePath(String path) {
        imagePathRepository.save(new ImagePath(path));
    }


    public Optional<ImagePath> getImagePath() {
        List<ImagePath> imagePath = imagePathRepository.findAll();
        if (!imagePath.isEmpty()) {
            return Optional.of(imagePath.get(0));
        }
        return Optional.empty();
    }

    public void clearAll() {
        imagePathRepository.deleteAll();
    }

    @PostConstruct
    public void clearTrackingOnStartup() {
        System.out.println("Application started. Clearing tracked image path...");
        imagePathRepository.deleteAll();
    }
}
