package com.example.speech.aiservice.vn.model.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "tracked_novel")
public class TrackedNovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "novel_id", referencedColumnName = "id", nullable = false)
    private Novel novel;

    public TrackedNovel() {}

    public TrackedNovel(Novel novel) {
        this.novel = novel;
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
}

