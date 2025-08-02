package com.external.ticketingidoleventservice.infrastructure.persistance.repository;

import com.external.ticketingidoleventservice.infrastructure.persistance.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, UUID> {
    List<ScheduleEntity> findByEventId(UUID eventId);
}
