package com.external.ticketingidoluserservice.infrastructure.web;

import com.external.ticketingidoluserservice.application.dto.request.OrganizerRegistrationRequest;
import com.external.ticketingidoluserservice.application.usecase.OrganizerCommandUseCase;
import com.external.ticketingidoluserservice.infrastructure.web.error.HttpErrorHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class OrganizerHttpHandler {
    private final Vertx vertx;
    private final OrganizerCommandUseCase organizerCommandUseCase;

    public OrganizerHttpHandler(Vertx vertx, OrganizerCommandUseCase organizerCommandUseCase) {
        this.vertx = vertx;
        this.organizerCommandUseCase = organizerCommandUseCase;
    }

    public void registerOrganizer(Router router) {
        router.post("/api/users/organizers").handler(this::handleRegisterOrganizer);
    }

    private void handleRegisterOrganizer(RoutingContext ctx) {
        OrganizerRegistrationRequest request;
        try {
            request = ctx.body().asPojo(OrganizerRegistrationRequest.class);
        } catch (Exception e) {
            ctx.response().setStatusCode(400).end("{\"error\": \"Invalid request body\"}");
            return;
        }

        organizerCommandUseCase.registerOrganizer(request)
                .thenAccept(v -> ctx.response()
                        .setStatusCode(201)
                        .putHeader("Content-Type", "application/json")
                        .end("{\"status\": \"organizer created\"}"))
                .exceptionally(ex -> {
                    HttpErrorHandler.handleError(ctx, ex);
                    return null;
                });
    }
}
