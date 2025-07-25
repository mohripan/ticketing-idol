package com.external.ticketingidoluserservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString
@AllArgsConstructor
@Builder
public class Role {
    private final UUID id;
    private final String name;
    private final Set<Permission> permissions;
    private final Instant createdAt;

    public boolean hasPermission(String code) {
        return permissions.stream().anyMatch(p -> p.getCode().equals(code));
    }
}
