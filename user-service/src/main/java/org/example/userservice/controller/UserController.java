package org.example.userservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // Endpoint for create new user
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        log.debug("POST /users called with username {}", request.username());
        return UserMapper.toResponse(userService.createUser(UserMapper.toEntity(request)));
    }

    // Endpoint for get user by ID
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return UserMapper.toResponse(userService.getUser(id));
    }

    // Endpoint for get all users
    @GetMapping
    public List<UserResponse> getAllUsers() {
        log.debug("GET /users called");
        return userService.getAllUsers().stream()
                .map(UserMapper::toResponse)
                .toList();
    }
}
