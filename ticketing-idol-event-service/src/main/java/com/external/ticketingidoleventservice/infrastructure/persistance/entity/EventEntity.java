package com.external.ticketingidoleventservice.infrastructure.persistance.entity;

import com.external.ticketingidoleventservice.domain.model.EventStatus;
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
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity {
    @Id
    private UUID id;

    private String title;
    private String description;
    private String venueName;
    private String venueAddress;
    private String city;

    private Instant startDateTime;
    private Instant endDateTime;
    private String posterUrl;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private Instant createdAt;
    private Instant updatedAt;
}
