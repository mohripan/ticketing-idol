package com.external.ticketingidoluserservice.application.service;

import com.external.ticketingidoluserservice.application.usecase.UserCommandUseCase;
import com.external.ticketingidoluserservice.domain.model.Email;
import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.UserRepository;
import com.external.ticketingidoluserservice.domain.security.PasswordEncoder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserCommandService implements UserCommandUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCommandService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CompletionStage<User> register(String username, Email email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);

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

    @Override
    public CompletionStage<Boolean> deleteById(UUID id) {
        return CompletableFuture.completedFuture(userRepository.deleteById(id));
    }

    @Override
    public CompletionStage<Boolean> update(UUID id, String newUsername, Email newEmail) {
        return CompletableFuture.supplyAsync(() -> {
            return userRepository.findById(id)
                    .map(existing -> {
                        User updated = existing.toBuilder()
                                .username(newUsername)
                                .email(newEmail)
                                .updatedAt(Instant.now())
                                .build();
                        userRepository.update(updated);
                        return true;
                    })
                    .orElse(false);
        });
    }
}
