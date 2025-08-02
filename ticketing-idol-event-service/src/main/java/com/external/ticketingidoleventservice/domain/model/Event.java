package com.external.ticketingidoleventservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class Event {
    private final UUID id;
    private final String title;
    private final String description;
    private final String venueName;
    private final String venueAddress;
    private final String city;
    private final Instant startDateTime;
    private final Instant endDateTime;
    private final String posterUrl;
    private final EventStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;
}
