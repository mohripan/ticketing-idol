package com.external.ticketingidoluserservice.infrastructure.persistance;

import com.external.ticketingidoluserservice.domain.model.Organizer;
import com.external.ticketingidoluserservice.domain.repository.OrganizerCommandRepository;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.concurrent.CompletionStage;

public class OrganizerCommandRepositoryImpl implements OrganizerCommandRepository {
    private final Mutiny.SessionFactory sessionFactory;

    public OrganizerCommandRepositoryImpl() {
        this.sessionFactory = HibernateReactiveUtil.getSessionFactory();
    }

    @Override
    public CompletionStage<Void> save(Organizer organizer) {
        return sessionFactory.withTransaction((session, tx) -> session.persist(organizer)).subscribeAsCompletionStage();
    }
}
