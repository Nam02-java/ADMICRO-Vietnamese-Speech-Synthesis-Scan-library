package com.example.speech.aiservice.vn.model.repository;

import com.example.speech.aiservice.vn.model.entity.TrackedNovel;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TrackedNovelRepository extends JpaRepository<TrackedNovel, Long> {
    boolean existsByNovelId(Long novelId);
}
