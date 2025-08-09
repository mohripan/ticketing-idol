package com.external.ticketingidoluserservice.application.dto.request;

public record OrganizerRegistrationRequest(
        String username,
        String email,
        String password,
        String organizationName
) {}
