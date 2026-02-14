package org.example.userservice.mapper;

import org.example.userservice.dto.UserRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.model.User;

import java.util.ArrayList;

public class UserMapper {

    public static User toEntity(UserRequest req) {
        User user = new User();
        user.setUsername(req.username());
        user.setDisplayName(req.displayName());
        user.setEmail(req.email());
        user.setFriendsIds(req.friendIds() == null ? new ArrayList<>()
                : new ArrayList<>(req.friendIds()));
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getFriendsIds()
        );
    }
}
