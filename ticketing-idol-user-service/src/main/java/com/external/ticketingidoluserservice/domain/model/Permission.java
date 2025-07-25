package com.external.ticketingidoluserservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
@AllArgsConstructor
@Builder
public class Permission {
    private final UUID id;
    private final String code;
    private final String description;
    private final Instant createdAt;
}
