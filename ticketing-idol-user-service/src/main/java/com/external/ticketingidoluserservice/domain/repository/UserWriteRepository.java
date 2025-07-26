package com.external.ticketingidoluserservice.domain.repository;

import com.external.ticketingidoluserservice.domain.model.User;

import java.util.UUID;

public interface UserWriteRepository {
    void save(User user);
    boolean deleteById(UUID id);
    void update(User user);
}
