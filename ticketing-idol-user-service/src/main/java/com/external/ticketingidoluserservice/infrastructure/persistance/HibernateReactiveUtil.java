package com.external.ticketingidoluserservice.infrastructure.persistance;

import jakarta.persistence.Persistence;
import org.hibernate.reactive.mutiny.Mutiny;

public class HibernateReactiveUtil {
    private static Mutiny.SessionFactory sessionFactory;

    public static Mutiny.SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            var emf = Persistence.createEntityManagerFactory("user-service");
            sessionFactory = emf.unwrap(Mutiny.SessionFactory.class);
        }
        return sessionFactory;
    }
}
