package com.external.ticketingidoluserservice.application.service;

import com.external.ticketingidoluserservice.application.dto.request.OrganizerRegistrationRequest;
import com.external.ticketingidoluserservice.application.usecase.OrganizerCommandUseCase;
import com.external.ticketingidoluserservice.domain.model.Organizer;
import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.OrganizerCommandRepository;
import com.external.ticketingidoluserservice.domain.repository.UserCommandRepository;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class OrganizerCommandUserCaseImpl implements OrganizerCommandUseCase {
    private final OrganizerCommandRepository organizerCommandRepository;
    private final UserCommandRepository userCommandRepository;
    private final Keycloak keycloak;
    private final String realm;

    public OrganizerCommandUserCaseImpl(OrganizerCommandRepository organizerCommandRepository,
                                        UserCommandRepository userCommandRepository,
                                        Keycloak keycloak,
                                        String realm) {
        this.organizerCommandRepository = organizerCommandRepository;
        this.userCommandRepository = userCommandRepository;
        this.keycloak = keycloak;
        this.realm = realm;
    }

    @Override
    public CompletionStage<Void> registerOrganizer(OrganizerRegistrationRequest request) {
        return CompletableFuture.runAsync(() -> {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setEnabled(true);

            Response response = keycloak.realm(realm).users().create(user);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create Keycloak user");
            }

            String keycloakUserIdStr = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            UUID keycloakUserId = UUID.fromString(keycloakUserIdStr);

            CredentialRepresentation password = new CredentialRepresentation();
            password.setTemporary(false);
            password.setType(CredentialRepresentation.PASSWORD);
            password.setValue(request.getPassword());

            keycloak.realm(realm).users().get(keycloakUserIdStr).resetPassword(password);

            RoleRepresentation role = keycloak.realm(realm).roles().get("organizer").toRepresentation();
            keycloak.realm(realm).users().get(keycloakUserIdStr).roles().realmLevel().add(List.of(role));

            UUID appUserId = UUID.randomUUID();
            Instant now = Instant.now();
            userCommandRepository.save(User.builder()
                    .id(appUserId)
                    .keycloakId(keycloakUserId)
                    .username(request.getUsername())
                    .profilePictureId(null)
                    .createdAt(now)
                    .updatedAt(now)
                    .build()
            );

            organizerCommandRepository.save(Organizer.builder()
                    .userId(appUserId)
                    .organizationName(request.getOrganizationName())
                    .verified(false)
                    .createdAt(now)
                    .updatedAt(now)
                    .build()
            );
        });
    }
}
