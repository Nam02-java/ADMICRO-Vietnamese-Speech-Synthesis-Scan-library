package com.example.speech.aiservice.vn.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "novel", uniqueConstraints = {@UniqueConstraint(columnNames = "title"), @UniqueConstraint(columnNames = "link")})
public class Novel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true, length = 500)
    private String link;

    public Novel() {
    }

    public Novel(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

