package com.external.ticketingidoleventservice.domain.repository;

import com.external.ticketingidoleventservice.domain.model.Schedule;

import java.util.UUID;

public interface ScheduleCommandRepository {
    Schedule save(Schedule schedule);
    void deleteById(UUID id);
}
