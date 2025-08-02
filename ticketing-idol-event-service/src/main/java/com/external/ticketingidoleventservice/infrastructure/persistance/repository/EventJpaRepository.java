package com.external.ticketingidoleventservice.infrastructure.persistance.repository;

import com.external.ticketingidoleventservice.infrastructure.persistance.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventJpaRepository extends JpaRepository<EventEntity, UUID> {

}
