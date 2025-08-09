package com.external.ticketingidoluserservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private final UUID id;
    private final UUID keycloakId;
    private final String username;
    private final UUID profilePictureId;
    private final Instant createdAt;
    private final Instant updatedAt;

    public User withUpdatedAt(Instant now) {
        return this.toBuilder().updatedAt(now).build();
    }

    public User withProfilePicture(UUID pictureId, Instant now) {
        return this.toBuilder().profilePictureId(pictureId).updatedAt(now).build();
    }

    public static User create(UUID id, UUID keycloakId, String username, Instant now) {
        return User.builder()
                .id(id)
                .keycloakId(keycloakId)
                .username(username)
                .profilePictureId(null)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
