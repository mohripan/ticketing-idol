package com.external.ticketingidoluserservice.infrastructure.config;

import com.external.ticketingidoluserservice.application.service.OrganizerCommandUserCaseImpl;
import com.external.ticketingidoluserservice.application.usecase.OrganizerCommandUseCase;
import com.external.ticketingidoluserservice.domain.repository.OrganizerCommandRepository;
import com.external.ticketingidoluserservice.domain.repository.UserCommandRepository;
import com.external.ticketingidoluserservice.infrastructure.persistance.OrganizerCommandRepositoryImpl;
import com.external.ticketingidoluserservice.infrastructure.persistance.UserCommandRepositoryImpl;
import com.external.ticketingidoluserservice.infrastructure.web.OrganizerHttpHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.keycloak.admin.client.Keycloak;

public class UserServiceFactory {
    public static void registerRoutes(Vertx vertx, Router router, Keycloak keycloak, String realm) {
        // Repositories
        OrganizerCommandRepository organizerCommandRepository = new OrganizerCommandRepositoryImpl();
        UserCommandRepository userCommandRepository = new UserCommandRepositoryImpl();

        // Use Cases
        OrganizerCommandUseCase organizerCommandUseCase =
                new OrganizerCommandUserCaseImpl(organizerCommandRepository, userCommandRepository, keycloak, realm);

        // HTTP Handlers
        OrganizerHttpHandler organizerHttpHandler = new OrganizerHttpHandler(vertx, organizerCommandUseCase);

        // Route Registration
        organizerHttpHandler.registerOrganizer(router);
    }
}
