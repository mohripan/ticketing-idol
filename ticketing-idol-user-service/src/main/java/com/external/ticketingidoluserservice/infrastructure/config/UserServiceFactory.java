package com.external.ticketingidoluserservice.infrastructure.config;

import com.external.ticketingidoluserservice.application.service.UserCommandService;
import com.external.ticketingidoluserservice.application.service.UserQueryService;
import com.external.ticketingidoluserservice.application.usecase.UserCommandUseCase;
import com.external.ticketingidoluserservice.application.usecase.UserQueryUseCase;
import com.external.ticketingidoluserservice.domain.repository.UserRepository;
import com.external.ticketingidoluserservice.domain.security.PasswordEncoder;
import com.external.ticketingidoluserservice.infrastructure.persistance.InMemoryUserRepository;
import com.external.ticketingidoluserservice.infrastructure.security.BCryptPasswordEncoder;
import com.external.ticketingidoluserservice.infrastructure.web.UserHttpHandler;
import io.vertx.core.Vertx;

public class UserServiceFactory {
    public static UserHttpHandler create(Vertx vertx) {
        UserRepository repo = new InMemoryUserRepository();
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        UserCommandUseCase service = new UserCommandService(repo, encoder);
        UserQueryUseCase userQueryUseCase = new UserQueryService(repo);

        return new UserHttpHandler(vertx, service, userQueryUseCase);
    }
}
