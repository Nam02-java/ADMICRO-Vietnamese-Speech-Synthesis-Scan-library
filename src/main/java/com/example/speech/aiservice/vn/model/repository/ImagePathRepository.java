package com.example.speech.aiservice.vn.model.repository;

import com.example.speech.aiservice.vn.model.entity.ImagePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagePathRepository extends JpaRepository<ImagePath, Long> {
}
