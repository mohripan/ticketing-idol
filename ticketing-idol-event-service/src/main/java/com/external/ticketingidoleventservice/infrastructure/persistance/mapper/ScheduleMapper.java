package com.external.ticketingidoleventservice.infrastructure.persistance.mapper;

import com.external.ticketingidoleventservice.domain.model.Schedule;
import com.external.ticketingidoleventservice.infrastructure.persistance.entity.ScheduleEntity;

public class ScheduleMapper {
    public static Schedule toDomain(ScheduleEntity entity) {
        return Schedule.builder()
                .id(entity.getId())
                .eventId(entity.getEventId())
                .showDateTime(entity.getShowDateTime())
                .quota(entity.getQuota())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ScheduleEntity toEntity(Schedule domain) {
        return ScheduleEntity.builder()
                .id(domain.getId())
                .eventId(domain.getEventId())
                .showDateTime(domain.getShowDateTime())
                .quota(domain.getQuota())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
