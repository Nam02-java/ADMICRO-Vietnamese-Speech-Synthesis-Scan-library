package com.example.speech.aiservice.vn.service.repositoryService;

import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import com.example.speech.aiservice.vn.model.repository.ChapterRepository;
import com.example.speech.aiservice.vn.model.repository.NovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterService(NovelRepository novelRepository, ChapterRepository chapterRepository) {
        this.novelRepository = novelRepository;
        this.chapterRepository = chapterRepository;
    }

    public Chapter findByTitle(String title) {
        return chapterRepository.findByTitle(title);
    }

    public Chapter addChapter(Novel novel, Integer chapterNumber, String title, String link) {
        // Check for duplicate chapterNumber
        if (chapterRepository.existsByNovelAndChapterNumber(novel, chapterNumber)) {
            throw new RuntimeException("Chapter number " + chapterNumber + " existed in the Novel " + novel.getTitle());
        }

        // Check for duplicate titles
        if (chapterRepository.existsByNovelAndTitle(novel, title)) {
            throw new RuntimeException("Chapter title '" + title + " existed in the Novel " + novel.getTitle());
        }

        Chapter chapter = new Chapter(novel, chapterNumber, title, link);
        return chapterRepository.save(chapter);
    }


    public Chapter saveChapter(Long novelId, Chapter chapter) {
        Optional<Novel> novelOptional = novelRepository.findById(novelId);

        if (!novelOptional.isPresent()) {
            throw new RuntimeException("Novel does not exist!");
        }

        Novel novel = novelOptional.get();

        if (chapterRepository.existsByNovelAndChapterNumber(novel, chapter.getChapterNumber()) ||
                chapterRepository.existsByNovelAndTitle(novel, chapter.getTitle())) {
            throw new RuntimeException("Number of Chapters or Titles that already exist in the Novel!");
        }

        chapter.setNovel(novel);
        return chapterRepository.save(chapter);
    }

//    public List<Chapter> getUnscannedChapters() {
//        return chapterRepository.findTop3ByIsScannedFalseOrderByChapterNumberAsc();
//    }

    public List<Chapter> getUnscannedChapters(Long novelId) {
        return chapterRepository.findTop3ByNovelIdAndIsScannedFalseOrderByChapterNumberAsc(novelId);
    }

    public void markChapterAsScanned(Chapter chapter) {
        chapter.setScanned(true);
        chapterRepository.save(chapter);
    }
}

