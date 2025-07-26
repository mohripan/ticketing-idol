package com.external.ticketingidoluserservice.application.mapper;

import com.external.ticketingidoluserservice.application.dto.response.UserResponse;
import com.external.ticketingidoluserservice.domain.model.User;

import java.util.stream.Collectors;

public class UserResponseMapper {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail().toString(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet())
        );
    }
}
