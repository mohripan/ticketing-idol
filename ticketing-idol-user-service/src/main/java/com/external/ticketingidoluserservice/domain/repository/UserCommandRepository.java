package com.external.ticketingidoluserservice.domain.repository;

import com.external.ticketingidoluserservice.domain.model.User;

import java.util.concurrent.CompletionStage;

public interface UserCommandRepository {
    CompletionStage<Void> save(User user);
}
