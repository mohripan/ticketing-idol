package com.external.ticketingidoluserservice.domain.repository;

import com.external.ticketingidoluserservice.domain.model.Attendee;

import java.util.concurrent.CompletionStage;

public interface AttendeeCommandRepository {
    CompletionStage<Void> save(Attendee attendee);
}
