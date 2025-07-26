package com.external.ticketingidoluserservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private Set<String> roles;
}
