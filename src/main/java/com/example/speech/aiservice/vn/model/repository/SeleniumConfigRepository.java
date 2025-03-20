package com.example.speech.aiservice.vn.model.repository;

import com.example.speech.aiservice.vn.model.entity.SeleniumConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeleniumConfigRepository extends JpaRepository<SeleniumConfig, Long> {
    SeleniumConfig findByPort(String port);
}
