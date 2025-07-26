package com.external.ticketingidoluserservice.application.usecase;

import com.external.ticketingidoluserservice.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface UserQueryUseCase {
    CompletionStage<Optional<User>> findByEmail(String email);
    CompletionStage<Optional<User>> findById(UUID id);
    CompletionStage<List<User>> findAll();
}
