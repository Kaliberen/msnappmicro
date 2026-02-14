package org.example.userservice.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String displayName,
        String email,
        List<Long> friendIds
) {
}
