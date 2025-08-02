package com.external.ticketingidoleventservice.domain.repository;

import com.external.ticketingidoleventservice.domain.model.Schedule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleQueryRepository {
    Optional<Schedule> findById(UUID id);
    List<Schedule> findByEventId(UUID eventId);
}
