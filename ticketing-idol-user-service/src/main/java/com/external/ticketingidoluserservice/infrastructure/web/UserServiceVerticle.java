package com.external.ticketingidoluserservice.infrastructure.web;

import com.external.ticketingidoluserservice.infrastructure.config.UserServiceFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class UserServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        UserServiceFactory.create(vertx).registerRoutes(router);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081, http -> {
                    if (http.succeeded()) {
                        System.out.println("User service started on http://localhost:8081");
                        startPromise.complete();
                    } else {
                        System.out.println("Failed to start server: " + http.cause());
                        startPromise.fail(http.cause());
                    }
                });
    }
}
