package com.external.ticketingidoluserservice.domain.repository;

import com.external.ticketingidoluserservice.domain.model.Attendee;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface AttendeeQueryRepository {
    CompletionStage<Optional<Attendee>> findByUserId(UUID userId);
}
