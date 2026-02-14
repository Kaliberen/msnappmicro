package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Generate a simple logger
public class UserService {

    private final UserRepository userRepository;

    // Create user and log
    public User createUser(User user) {
        log.info("Creating user with username={}",  user.getUsername());
        return userRepository.save(user);
    }

    // Get user by ID
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", id);
                    return new RuntimeException("User not found");
                });
    }

    // Get all users in system
    public List<User> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll();
    }
}
