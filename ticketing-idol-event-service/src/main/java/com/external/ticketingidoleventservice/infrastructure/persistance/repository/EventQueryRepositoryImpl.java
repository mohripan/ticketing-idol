package com.external.ticketingidoleventservice.infrastructure.persistance.repository;

import com.external.ticketingidoleventservice.domain.model.Event;
import com.external.ticketingidoleventservice.domain.repository.EventQueryRepository;
import com.external.ticketingidoleventservice.infrastructure.persistance.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventQueryRepositoryImpl implements EventQueryRepository {

    private final EventJpaRepository eventJpaRepository;

    @Override
    public Optional<Event> findById(UUID id) {
        return eventJpaRepository.findById(id).map(EventMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventJpaRepository.findAll().stream()
                .map(EventMapper::toDomain)
                .toList();
    }
}