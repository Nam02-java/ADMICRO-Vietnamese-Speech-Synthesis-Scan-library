package com.example.speech.aiservice.vn.service.repositoryService;

import com.example.speech.aiservice.vn.model.TrackUpload;
import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.model.repository.TrackUploadRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrackUploadService {

    private final TrackUploadRepository trackUploadRepository;

    @Autowired
    public TrackUploadService(TrackUploadRepository trackUploadRepository) {
        this.trackUploadRepository = trackUploadRepository;
    }

    public void saveTrack(long novelId, long chapterId) {
        TrackUpload track = new TrackUpload();
        Novel novel = new Novel();
        novel.setId(novelId);

        Chapter chapter = new Chapter();
        chapter.setId(chapterId);

        track.setNovel(novel);
        track.setChapter(chapter);

        trackUploadRepository.save(track);
    }

    public TrackUpload findByNovelAndChapter(long novelId, long chapterId) {
        return trackUploadRepository.findByNovelAndChapter(novelId, chapterId);
    }

    public List<TrackUpload> findAll() {
        return trackUploadRepository.findAll();
    }

    @Transactional
    public void deleteById(long id) {
        trackUploadRepository.deleteById(id);
    }

    @PostConstruct
    public void clearTrackingOnStartup() {
        System.out.println("Application started. Clearing tracked upload...");
        trackUploadRepository.deleteAll();
    }
}
