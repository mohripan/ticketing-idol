package com.external.ticketingidoluserservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private final UUID id;
    private final String username;
    private final Email email;
    private final String hashedPassword;
    private final boolean enabled;
    private final Set<Role> roles;
    private final Instant createdAt;
    private Instant updatedAt;

    public void updatedTimestamp() {
        this.updatedAt = Instant.now();
    }

    public boolean hasPermission(String code) {
        return roles.stream().anyMatch(r -> r.hasPermission(code));
    }
}
