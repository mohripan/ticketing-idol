package com.external.ticketingidoleventservice.domain.repository;

import com.external.ticketingidoleventservice.domain.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventCommandRepository {
    Event save(Event event);
    void deleteById(UUID id);
}
