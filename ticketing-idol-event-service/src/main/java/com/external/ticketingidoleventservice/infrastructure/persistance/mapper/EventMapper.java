package com.external.ticketingidoleventservice.infrastructure.persistance.mapper;

import com.external.ticketingidoleventservice.domain.model.Event;
import com.external.ticketingidoleventservice.infrastructure.persistance.entity.EventEntity;

public class EventMapper {
    public static Event toDomain(EventEntity entity) {
        return Event.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .venueName(entity.getVenueName())
                .venueAddress(entity.getVenueAddress())
                .city(entity.getCity())
                .startDateTime(entity.getStartDateTime())
                .endDateTime(entity.getEndDateTime())
                .posterUrl(entity.getPosterUrl())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static EventEntity toEntity(Event domain) {
        return EventEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .venueName(domain.getVenueName())
                .venueAddress(domain.getVenueAddress())
                .city(domain.getCity())
                .startDateTime(domain.getStartDateTime())
                .endDateTime(domain.getEndDateTime())
                .posterUrl(domain.getPosterUrl())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
