package com.external.ticketingidoluserservice.domain.repository;

import com.external.ticketingidoluserservice.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserQueryRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    List<User> findAll();
}