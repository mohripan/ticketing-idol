package com.external.ticketingidoluserservice.application.usecase;

import com.external.ticketingidoluserservice.application.dto.request.OrganizerRegistrationRequest;
import com.external.ticketingidoluserservice.application.dto.response.RegisterOrganizerResult;
import com.external.ticketingidoluserservice.domain.model.Organizer;
import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.OrganizerCommandRepository;
import com.external.ticketingidoluserservice.domain.repository.UserCommandRepository;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class OrganizerCommandUserCaseImpl implements OrganizerCommandUseCase {
    private final OrganizerCommandRepository organizerCommandRepository;
    private final UserCommandRepository userCommandRepository;
    private final Keycloak keycloak;
    private final String realm;
    private final Executor blockingExecutor;
    private final Clock clock;

    public OrganizerCommandUserCaseImpl(OrganizerCommandRepository organizerCommandRepository,
                                        UserCommandRepository userCommandRepository,
                                        Keycloak keycloak,
                                        String realm
                                        ) {
        this(organizerCommandRepository, userCommandRepository, keycloak, realm,
                Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2)),
                Clock.systemUTC());
    }

    public OrganizerCommandUserCaseImpl(OrganizerCommandRepository organizerCommandRepository,
                                        UserCommandRepository userCommandRepository,
                                        Keycloak keycloak,
                                        String realm,
                                        Executor blockingExecutor,
                                        Clock clock) {
        this.organizerCommandRepository = organizerCommandRepository;
        this.userCommandRepository = userCommandRepository;
        this.keycloak = keycloak;
        this.realm = realm;
        this.blockingExecutor = blockingExecutor;
        this.clock = clock;
    }

    @Override
    public CompletionStage<RegisterOrganizerResult> registerOrganizer(OrganizerRegistrationRequest request) {
        validate(request);

        UUID appUserId = UUID.randomUUID();
        Instant now = Instant.now(clock);

        return CompletableFuture
                .supplyAsync(() -> createKeycloakUser(request), blockingExecutor)
                .thenComposeAsync(kcUserId -> setPasswordAndRole(kcUserId, request.getPassword()).thenApply(v -> kcUserId),
                        blockingExecutor)
                .thenCompose(kcUserId -> {
                    User user = User.create(appUserId, UUID.fromString(kcUserId), request.getUsername(), now);
                    return userCommandRepository.save(user)
                            .exceptionally(ex -> {
                                safeDeleteKeycloakUser(kcUserId);
                                throw new CompletionException(ex);
                            })
                            .thenApply(v -> user);
                })
                .thenCompose(user -> {
                    Organizer organizer = Organizer.create(user.getId(), request.getOrganizationName(), now);
                    return organizerCommandRepository.save(organizer)
                            .thenApply(v -> RegisterOrganizerResult.of(
                                    user.getId(),
                                    user.getUsername(),
                                    organizer.getOrganizationName(),
                                    organizer.isVerified(),
                                    user.getCreatedAt(),
                                    user.getUpdatedAt()
                            ))
                            .exceptionallyCompose(ex -> {
                                CompletableFuture.runAsync(() ->
                                        safeDeleteKeycloakUser(user.getKeycloakId().toString()), blockingExecutor);
                                CompletableFuture<RegisterOrganizerResult> failed = new CompletableFuture<>();
                                failed.completeExceptionally(ex);
                                return failed;
                            });
                });
    }

    private void validate(OrganizerRegistrationRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank())
            throw new IllegalArgumentException("username is required");
        if (req.getEmail() == null || req.getEmail().isBlank())
            throw new IllegalArgumentException("email is required");
        if (req.getPassword() == null || req.getPassword().isBlank())
            throw new IllegalArgumentException("password is required");
        if (req.getOrganizationName() == null || req.getOrganizationName().isBlank())
            throw new IllegalArgumentException("organizationName is required");
    }

    private String createKeycloakUser(OrganizerRegistrationRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(true);

        Response response = keycloak.realm(realm).users().create(user);
        try (response) {
            int status = response.getStatus();
            if (status == 201) {
                return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            } else if (status == 409) {
                throw new IllegalArgumentException("User already exists");
            } else {
                throw new RuntimeException("Failed to create Keycloak user (status " + status + ")");
            }
        }
    }

    private CompletionStage<Void> setPasswordAndRole(String kcUserId, String rawPassword) {
        return CompletableFuture.runAsync(() -> {
            try {
                CredentialRepresentation password = new CredentialRepresentation();
                password.setTemporary(false);
                password.setType(CredentialRepresentation.PASSWORD);
                password.setValue(rawPassword);

                keycloak.realm(realm).users().get(kcUserId).resetPassword(password);
            } catch (jakarta.ws.rs.BadRequestException e) {
                safeDeleteKeycloakUser(kcUserId);
                throw new IllegalArgumentException("Password does not meet realm policy.", e);
            }

            try {
                RoleRepresentation role = keycloak.realm(realm).roles().get("organizer").toRepresentation();
                keycloak.realm(realm).users().get(kcUserId).roles().realmLevel().add(List.of(role));
            } catch (jakarta.ws.rs.NotFoundException e) {
                safeDeleteKeycloakUser(kcUserId);
                throw new IllegalArgumentException("Realm role 'organizer' does not exist.", e);
            }
        }, blockingExecutor);
    }

    private void safeDeleteKeycloakUser(String kcUserId) {
        try { keycloak.realm(realm).users().get(kcUserId).remove(); } catch (Exception ignore) { }
    }
}
