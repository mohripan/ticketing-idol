package com.external.ticketingidoleventservice.infrastructure.persistance.repository;

import com.external.ticketingidoleventservice.domain.model.Schedule;
import com.external.ticketingidoleventservice.domain.repository.ScheduleCommandRepository;
import com.external.ticketingidoleventservice.infrastructure.persistance.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ScheduleCommandRepositoryImpl implements ScheduleCommandRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public Schedule save(Schedule schedule) {
        var entity = ScheduleMapper.toEntity(schedule);
        var saved = scheduleJpaRepository.save(entity);
        return ScheduleMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        scheduleJpaRepository.deleteById(id);
    }
}
