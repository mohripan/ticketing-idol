package com.external.ticketingidoleventservice.infrastructure.persistance.repository;

import com.external.ticketingidoleventservice.domain.model.Event;
import com.external.ticketingidoleventservice.domain.repository.EventCommandRepository;
import com.external.ticketingidoleventservice.infrastructure.persistance.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventCommandRepositoryImpl implements EventCommandRepository {

    private final EventJpaRepository eventJpaRepository;

    @Override
    public Event save(Event event) {
        var entity = EventMapper.toEntity(event);
        var saved = eventJpaRepository.save(entity);
        return EventMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        eventJpaRepository.deleteById(id);
    }
}
