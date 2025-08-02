package com.external.ticketingidoluserservice.application.usecase;

import com.external.ticketingidoluserservice.application.dto.request.OrganizerRegistrationRequest;

import java.util.concurrent.CompletionStage;

public interface OrganizerCommandUseCase {
    CompletionStage<Void> registerOrganizer(OrganizerRegistrationRequest request);
}
