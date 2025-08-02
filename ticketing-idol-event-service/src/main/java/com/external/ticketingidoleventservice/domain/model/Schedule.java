package com.external.ticketingidoleventservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class Schedule {
    private final UUID id;
    private final UUID eventId;
    private final Instant showDateTime;
    private final int quota;
    private final ScheduleStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;
}
