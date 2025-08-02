package com.external.ticketingidoluserservice.infrastructure.persistance;

import com.external.ticketingidoluserservice.domain.model.Organizer;
import com.external.ticketingidoluserservice.domain.repository.OrganizerQueryRepository;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public class OrganizerQueryRepositoryImpl implements OrganizerQueryRepository {
    private final Mutiny.SessionFactory sessionFactory;

    public OrganizerQueryRepositoryImpl() {
        this.sessionFactory = HibernateReactiveUtil.getSessionFactory();
    }

    @Override
    public CompletionStage<Optional<Organizer>> findByUserId(UUID userId) {
        return sessionFactory.withSession(session ->
                session.find(Organizer.class, userId)
                        .map(Optional::ofNullable)
        ).subscribeAsCompletionStage();
    }
}
