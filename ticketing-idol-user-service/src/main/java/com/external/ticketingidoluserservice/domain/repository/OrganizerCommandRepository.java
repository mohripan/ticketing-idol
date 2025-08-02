package com.external.ticketingidoluserservice.domain.repository;

import com.external.ticketingidoluserservice.domain.model.Organizer;

import java.util.concurrent.CompletionStage;

public interface OrganizerCommandRepository {
    CompletionStage<Void> save(Organizer organizer);
}
