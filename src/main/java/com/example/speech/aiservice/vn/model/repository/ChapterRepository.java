package com.example.speech.aiservice.vn.model.repository;

import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Chapter findByTitle(String title);

    List<Chapter> findAll();

    // Kiểm tra chapterNumber có tồn tại trong Novel hay không
    boolean existsByNovelAndChapterNumber(Novel novel, int chapterNumber);

    // Kiểm tra title có tồn tại trong Novel hay không
    boolean existsByNovelAndTitle(Novel novel, String title);

    List<Chapter> findTop3ByIsScannedFalseOrderByChapterNumberAsc();

    List<Chapter> findTop3ByNovelIdAndIsScannedFalseOrderByChapterNumberAsc(Long novelId);
}

