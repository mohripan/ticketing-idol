package com.external.ticketingidoluserservice.infrastructure.persistance;

import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.UserQueryRepository;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public class UserQueryRepositoryImpl implements UserQueryRepository {
    private final Mutiny.SessionFactory sessionFactory;

    public UserQueryRepositoryImpl() {
        this.sessionFactory = HibernateReactiveUtil.getSessionFactory();
    }

    @Override
    public CompletionStage<Optional<User>> findByUserId(UUID userId) {
        return sessionFactory
                .withSession(session -> session.find(User.class, userId)
                        .map(Optional::ofNullable))
                .subscribeAsCompletionStage();
    }
}
