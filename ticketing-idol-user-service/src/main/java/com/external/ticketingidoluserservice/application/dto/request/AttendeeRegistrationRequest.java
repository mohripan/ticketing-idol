package com.external.ticketingidoluserservice.application.dto.request;

import java.time.LocalDate;

public record AttendeeRegistrationRequest(
        String fullName,
        String identityCardNumber,
        LocalDate birthDate
) {}
