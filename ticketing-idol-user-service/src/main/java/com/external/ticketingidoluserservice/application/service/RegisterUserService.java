package com.external.ticketingidoluserservice.application.service;

import com.external.ticketingidoluserservice.application.usecase.RegisterUserUseCase;
import com.external.ticketingidoluserservice.domain.model.Email;
import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.UserRepository;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class RegisterUserService implements RegisterUserUseCase {
    private final UserRepository userRepository;

    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletionStage<User> register(String username, Email email, String rawPassword) {
        String hashedPassword = "hashed_" + rawPassword;

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .hashedPassword(hashedPassword)
                .enabled(true)
                .roles(Set.of())
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);
        return CompletableFuture.completedFuture(user);
    }
}
