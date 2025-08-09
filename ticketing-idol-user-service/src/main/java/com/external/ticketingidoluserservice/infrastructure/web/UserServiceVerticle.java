package com.external.ticketingidoluserservice.infrastructure.web;

import com.external.ticketingidoluserservice.infrastructure.config.UserServiceFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.flywaydb.core.Flyway;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {

        String jdbcUrl = "jdbc:postgresql://localhost:5433/ticketing_user";
        String dbUser = "user_service";
        String dbPass = "secret";

        Flyway flyway = Flyway.configure()
                .dataSource(jdbcUrl, dbUser, dbPass)
                .locations("classpath:db/migration")
                .schemas("public")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8000")
                .realm("master")
                .clientId("admin-cli")
                .grantType(OAuth2Constants.PASSWORD)
                .username("admin")
                .password("admin")
                .build();

        String realm = "ticketing-idol";

        String jwksUrl = "http://localhost:8000/realms/" + realm + "/protocol/openid-connect/certs";
        HttpClient client = HttpClient.newHttpClient();
        List<JsonObject> jwkList;
        try {
            String resp = client.send(
                    HttpRequest.newBuilder(URI.create(jwksUrl)).GET().build(),
                    HttpResponse.BodyHandlers.ofString()
            ).body();

            JsonObject jwks = new JsonObject(resp);
            jwkList = jwks.getJsonArray("keys")
                    .stream()
                    .map(obj -> (JsonObject) obj)
                    .filter(jwk -> "sig".equals(jwk.getString("use")))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load JWKS", e);
        }

        JWTAuthOptions jwtOpts = new JWTAuthOptions()
                .setJwks(jwkList)
                .setJWTOptions(new JWTOptions().setIssuer("http://localhost:8000/realms/" + realm)
                        .setLeeway(3600));

        JWTAuth jwtAuth = JWTAuth.create(vertx, jwtOpts);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route("/api/users/*").handler(JWTAuthHandler.create(jwtAuth));

        UserServiceFactory.registerRoutes(vertx, router, keycloak, realm,
                "localhost",
                5433,
                "ticketing_user",
                "user_service",
                "secret");

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
