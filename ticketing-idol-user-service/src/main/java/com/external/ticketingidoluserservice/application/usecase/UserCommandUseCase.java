package com.external.ticketingidoluserservice.application.usecase;

import com.external.ticketingidoluserservice.domain.model.Email;
import com.external.ticketingidoluserservice.domain.model.User;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface UserCommandUseCase {
    CompletionStage<User> register(String username, Email email, String rawPassword);
    CompletionStage<Boolean> deleteById(UUID id);
    CompletionStage<Boolean> update(UUID id, String newUsername, Email newEmail);
}
