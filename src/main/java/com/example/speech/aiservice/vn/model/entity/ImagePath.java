package com.example.speech.aiservice.vn.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "image_path")
public class ImagePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path", nullable = false, length = 500)
    private String path;

    public ImagePath() {
    }

    public ImagePath(String path) {
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
