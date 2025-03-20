package com.example.speech.aiservice.vn.service.repositoryService;

import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.model.repository.NovelRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class NovelService {
    @Autowired
    private NovelRepository novelRepository;

    public Novel saveNovel(Novel novel) {
        return novelRepository.save(novel);
    }

    public boolean isNovelExists(String title) {
        return novelRepository.existsByTitle(title);
    }

    public Novel findByTitle(String title) {
        return novelRepository.findByTitle(title);
    }
}
