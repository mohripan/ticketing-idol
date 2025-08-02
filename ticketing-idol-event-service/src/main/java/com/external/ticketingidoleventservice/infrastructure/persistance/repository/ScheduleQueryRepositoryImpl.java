package com.external.ticketingidoleventservice.infrastructure.persistance.repository;

import com.external.ticketingidoleventservice.domain.model.Schedule;
import com.external.ticketingidoleventservice.domain.repository.ScheduleQueryRepository;
import com.external.ticketingidoleventservice.infrastructure.persistance.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepositoryImpl implements ScheduleQueryRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public Optional<Schedule> findById(UUID id) {
        return scheduleJpaRepository.findById(id).map(ScheduleMapper::toDomain);
    }

    @Override
    public List<Schedule> findByEventId(UUID eventId) {
        return scheduleJpaRepository.findByEventId(eventId).stream()
                .map(ScheduleMapper::toDomain)
                .toList();
    }
}
