package com.external.ticketingidoluserservice.infrastructure.web;

import com.external.ticketingidoluserservice.application.dto.request.RegisterUserRequest;
import com.external.ticketingidoluserservice.application.usecase.RegisterUserUseCase;
import com.external.ticketingidoluserservice.domain.model.Email;
import com.external.ticketingidoluserservice.domain.model.User;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class UserHttpHandler {
    private final Vertx vertx;
    private final RegisterUserUseCase registerUserUseCase;

    public UserHttpHandler(Vertx vertx, RegisterUserUseCase registerUserUseCase) {
        this.vertx = vertx;
        this.registerUserUseCase = registerUserUseCase;
    }

    public void registerRoutes(Router router) {
        router.get("/api/users/health").handler(this::handleHealthCheck);
        router.post("/api/users").handler(this::handleRegisterUser);
    }

    private void handleRegisterUser(RoutingContext ctx) {
        RegisterUserRequest request = ctx.body().asPojo(RegisterUserRequest.class);

        registerUserUseCase.register(request.getUsername(), new Email(request.getEmail()), request.getPassword())
                .thenAccept(user -> {
                   ctx.response()
                           .putHeader("Content-Type", "application/json")
                           .setStatusCode(201)
                           .end("{\"id\": \"" + user.getId() + "\"}");
                })
                .exceptionally(ex -> {
                    ctx.response().setStatusCode(500).end(ex.getMessage());
                    return null;
                });
    }

    private void handleHealthCheck(RoutingContext ctx) {
        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end("{\"status\": \"up\"}");
    }
}
