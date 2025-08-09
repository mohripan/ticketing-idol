package com.external.ticketingidoluserservice.infrastructure.config;

import com.external.ticketingidoluserservice.application.usecase.OrganizerCommandUseCase;
import com.external.ticketingidoluserservice.application.usecase.OrganizerCommandUserCaseImpl;
import com.external.ticketingidoluserservice.domain.repository.OrganizerCommandRepository;
import com.external.ticketingidoluserservice.domain.repository.OrganizerQueryRepository;
import com.external.ticketingidoluserservice.domain.repository.UserCommandRepository;
import com.external.ticketingidoluserservice.domain.repository.UserQueryRepository;
import com.external.ticketingidoluserservice.infrastructure.persistance.OrganizerCommandRepositorySql;
import com.external.ticketingidoluserservice.infrastructure.persistance.OrganizerQueryRepositorySql;
import com.external.ticketingidoluserservice.infrastructure.persistance.UserCommandRepositorySql;
import com.external.ticketingidoluserservice.infrastructure.persistance.UserQueryRepositorySql;
import com.external.ticketingidoluserservice.infrastructure.web.OrganizerHttpHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgPool;
import org.keycloak.admin.client.Keycloak;

public class UserServiceFactory {
    public static void registerRoutes(Vertx vertx,
                                      Router router,
                                      Keycloak keycloak,
                                      String realm,
                                      String dbHost, int dbPort, String dbName, String dbUser, String dbPass) {
        PgPool pg = PgClientFactory.create(vertx, dbHost, dbPort, dbName, dbUser, dbPass);

        UserCommandRepository userCommandRepository = new UserCommandRepositorySql(pg);
        UserQueryRepository userQueryRepository = new UserQueryRepositorySql(pg);
        OrganizerCommandRepository organizerCommandRepository = new OrganizerCommandRepositorySql(pg);
        OrganizerQueryRepository organizerQueryRepository = new OrganizerQueryRepositorySql(pg);

        OrganizerCommandUseCase organizerCommandUseCase =
                new OrganizerCommandUserCaseImpl(organizerCommandRepository, userCommandRepository, keycloak, realm);

        new OrganizerHttpHandler(vertx, organizerCommandUseCase).registerOrganizer(router);
    }
}
