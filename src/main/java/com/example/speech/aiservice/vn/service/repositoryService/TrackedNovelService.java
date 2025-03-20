package com.example.speech.aiservice.vn.service.repositoryService;

import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.model.entity.TrackedNovel;
import com.example.speech.aiservice.vn.model.repository.TrackedNovelRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrackedNovelService {

    private final TrackedNovelRepository trackedNovelRepository;
    private final NovelService novelService;

    @Autowired
    public TrackedNovelService(TrackedNovelRepository trackedNovelRepository, NovelService novelService) {
        this.trackedNovelRepository = trackedNovelRepository;
        this.novelService = novelService;
    }

    public void trackNovel(Novel novel) {
        trackedNovelRepository.deleteAll();
        trackedNovelRepository.save(new TrackedNovel(novel));
    }

    public boolean isTrackNovellExists(String title) {
        Novel novel = novelService.findByTitle(title);
        if (novel != null) {
            return trackedNovelRepository.existsByNovelId(novel.getId());
        }
        return false;
    }

    public Optional<TrackedNovel> getTrackedNovel() {
        List<TrackedNovel> trackedNovels = trackedNovelRepository.findAll();
        if (!trackedNovels.isEmpty()) {
            return Optional.of(trackedNovels.get(0));
        }
        return Optional.empty();
    }


    public void clearTracking() {
        trackedNovelRepository.deleteAll();
    }

    @PreDestroy
    public void clearTrackingOnShutdown() {
        System.out.println("Application is shutting down. Clearing tracked novel table...");
        trackedNovelRepository.deleteAll();
    }

    @PostConstruct
    public void clearTrackingOnStartup() {
        System.out.println("Application started. Clearing tracked novel table...");
        trackedNovelRepository.deleteAll();
    }
}


