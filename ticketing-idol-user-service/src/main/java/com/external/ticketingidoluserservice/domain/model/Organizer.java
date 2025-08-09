package com.external.ticketingidoluserservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "userId")
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class Organizer {
    private final UUID userId;
    private final String organizationName;
    private final boolean verified;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Organizer verify(Instant now) {
        if (verified) return this; // idempotent
        return this.toBuilder().verified(true).updatedAt(now).build();
    }

    public Organizer renameOrganization(String newName, Instant now) {
        return this.toBuilder().organizationName(newName).updatedAt(now).build();
    }

    public static Organizer create(UUID userId, String orgName, Instant now) {
        return Organizer.builder()
                .userId(userId)
                .organizationName(orgName)
                .verified(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}