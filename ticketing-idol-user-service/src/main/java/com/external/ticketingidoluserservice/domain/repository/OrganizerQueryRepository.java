package com.external.ticketingidoluserservice.domain.repository;

import com.external.ticketingidoluserservice.domain.model.Organizer;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface OrganizerQueryRepository {
    CompletionStage<Optional<Organizer>> findByUserId(UUID userId);
}
