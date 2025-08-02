package com.external.ticketingidoleventservice.infrastructure.persistance.entity;

import com.external.ticketingidoleventservice.domain.model.ScheduleStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleEntity {
    @Id
    private UUID id;

    private UUID eventId;

    private Instant showDateTime;
    private int quota;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    private Instant createdAt;
    private Instant updatedAt;
}
