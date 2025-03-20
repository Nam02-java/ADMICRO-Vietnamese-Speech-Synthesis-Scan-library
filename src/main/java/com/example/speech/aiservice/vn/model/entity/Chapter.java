package com.example.speech.aiservice.vn.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chapter", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"novel_id", "chapter_number"}),
        @UniqueConstraint(columnNames = {"novel_id", "title"})
})
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @Column(nullable = false)
    private int chapterNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String link;


    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isScanned = false;


    public Chapter() {
    }

    public Chapter(Novel novel, int chapterNumber, String title, String link) {
        this.novel = novel;
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.link = link;
        this.isScanned = isScanned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Novel getNovel() {
        return novel;
    }

    public void setNovel(Novel novel) {
        this.novel = novel;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isScanned() {
        return isScanned;
    }

    public void setScanned(boolean scanned) {
        isScanned = scanned;
    }
}
