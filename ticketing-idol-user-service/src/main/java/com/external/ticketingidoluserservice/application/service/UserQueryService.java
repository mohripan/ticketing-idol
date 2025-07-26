package com.external.ticketingidoluserservice.application.service;

import com.external.ticketingidoluserservice.application.usecase.UserQueryUseCase;
import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.UserQueryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserQueryService implements UserQueryUseCase {
    private final UserQueryRepository userQueryRepository;

    public UserQueryService(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    @Override
    public CompletionStage<Optional<User>> findByEmail(String email) {
        return CompletableFuture.completedFuture(userQueryRepository.findByEmail(email));
    }

    @Override
    public CompletionStage<Optional<User>> findById(UUID id) {
        return CompletableFuture.completedFuture(userQueryRepository.findById(id));
    }

    @Override
    public CompletionStage<List<User>> findAll() {
        return CompletableFuture.completedFuture(userQueryRepository.findAll());
    }
}
