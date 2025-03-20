package com.example.speech.aiservice.vn.service.repositoryService;

import com.example.speech.aiservice.vn.model.entity.User;
import com.example.speech.aiservice.vn.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User does not exist in the database");
        }
        return user;
    }
}
