package com.example.speech.aiservice.vn.model.repository;

import com.example.speech.aiservice.vn.model.entity.TrackUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackUploadRepository extends JpaRepository<TrackUpload, Integer> {

    @Query("SELECT t FROM TrackUpload t WHERE t.novel.id = :novelId ORDER BY t.chapter.id ASC")
    List<TrackUpload> findByNovel(@Param("novelId") int novelId);

    @Query("SELECT t FROM TrackUpload t WHERE t.novel.id = :novelId AND t.chapter.id = :chapterId")
    TrackUpload findByNovelAndChapter(@Param("novelId") long novelId, @Param("chapterId") long chapterId);

    @Query("SELECT t FROM TrackUpload t ORDER BY t.chapter.id ASC")
    List<TrackUpload> findAll();

    void deleteById(Long id);


}
