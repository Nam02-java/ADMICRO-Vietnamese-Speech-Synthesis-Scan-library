package com.example.speech.aiservice.vn.model;

import com.example.speech.aiservice.vn.model.entity.Chapter;
import com.example.speech.aiservice.vn.model.entity.Novel;
import jakarta.persistence.*;

@Entity
@Table(name = "track_upload")
public class TrackUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    @OrderBy("id ASC")
    private Chapter chapter;


    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Novel getNovel() { return novel; }
    public void setNovel(Novel novel) { this.novel = novel; }

    public Chapter getChapter() { return chapter; }
    public void setChapter(Chapter chapter) { this.chapter = chapter; }
}
