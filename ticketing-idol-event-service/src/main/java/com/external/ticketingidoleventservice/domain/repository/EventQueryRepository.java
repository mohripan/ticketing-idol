package com.external.ticketingidoleventservice.domain.repository;

import com.external.ticketingidoleventservice.domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventQueryRepository {
    Optional<Event> findById(UUID id);      // <-- seharusnya ini ada di Query
    List<Event> findAll();
}
