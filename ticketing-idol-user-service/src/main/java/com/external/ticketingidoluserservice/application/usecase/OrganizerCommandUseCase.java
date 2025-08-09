package com.external.ticketingidoluserservice.application.usecase;

import com.external.ticketingidoluserservice.application.dto.request.OrganizerRegistrationRequest;
import com.external.ticketingidoluserservice.application.dto.response.RegisterOrganizerResult;

import java.util.concurrent.CompletionStage;

public interface OrganizerCommandUseCase {
    CompletionStage<RegisterOrganizerResult> registerOrganizer(OrganizerRegistrationRequest request);
}
