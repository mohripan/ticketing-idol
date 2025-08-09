package com.external.ticketingidoluserservice.application.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RegisterOrganizerResult(
        UUID userId,
        String username,
        OrganizerInfo organizer,
        Instant createdAt,
        Instant updatedAt
) {
    public static RegisterOrganizerResult of (
            UUID userId,
            String username,
            String orgName,
            boolean verified,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new RegisterOrganizerResult(
                userId,
                username,
                new OrganizerInfo(orgName, verified),
                createdAt,
                updatedAt
        );
    }
    public record OrganizerInfo(String organizationName, boolean verified) {}
}
