package com.external.ticketingidoluserservice.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "userId")
@ToString
@AllArgsConstructor
@Builder
public class Attendee {
    private final UUID userId;
    private final boolean onboardingCompleted;
    private final String fullName;
    private final String identityCardNumber;
    private final LocalDate birthDate;
}
