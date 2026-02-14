package org.example.userservice.dto;

import java.util.List;

public record UserRequest (
        String username,
        String displayName,
        String email,
        List<Long> friendIds
) {
}
