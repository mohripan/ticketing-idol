package com.external.ticketingidoluserservice.application.usecase;

import com.external.ticketingidoluserservice.application.dto.request.OrganizerRegistrationRequest;
import com.external.ticketingidoluserservice.application.dto.response.RegisterOrganizerResult;
import com.external.ticketingidoluserservice.domain.model.Organizer;
import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.OrganizerCommandRepository;
import com.external.ticketingidoluserservice.domain.repository.UserCommandRepository;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizerCommandUserCaseImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Keycloak keycloak;

    @Mock UserCommandRepository userCommandRepository;
    @Mock OrganizerCommandRepository organizerCommandRepository;

    // Keycloak resource graph
    @Mock RealmResource realmResource;
    @Mock UsersResource usersResource;
    @Mock UserResource userResource;
    @Mock RolesResource rolesResource;
    @Mock RoleResource roleResource;
    @Mock RoleMappingResource roleMappingResource;
    @Mock RoleScopeResource roleScopeResource;

    private Clock clock;
    private Executor sameThread;
    private OrganizerCommandUserCaseImpl useCase;

    private final String realm = "ticketing-idol";
    private final UUID kcUserId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");
    private final Instant now = Instant.parse("2025-08-09T04:00:00Z");

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(now, ZoneOffset.UTC);
        sameThread = Runnable::run;

        useCase = new OrganizerCommandUserCaseImpl(
                organizerCommandRepository, userCommandRepository, keycloak, realm, sameThread, clock
        );

        // only the minimum wiring that EVERY test uses
        when(keycloak.realm(realm)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
    }

    private OrganizerRegistrationRequest req() {
        return new OrganizerRegistrationRequest(
                "johnny", "johnny@example.com", "Secret123!", "CoolOrg"
        );
    }

    private void mockCreateUser201() {
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(URI.create("/admin/realms/" + realm + "/users/" + kcUserId));
        doNothing().when(response).close();

        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);

        // only tests that proceed past creation need a user handle
        when(usersResource.get(kcUserId.toString())).thenReturn(userResource);
    }

    private void mockCreateUser409() {
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(409);
        doNothing().when(response).close();
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        // NOTE: no usersResource.get(..) here on purpose (avoids unnecessary stubbing)
    }

    private void mockPasswordOk() {
        when(usersResource.get(kcUserId.toString())).thenReturn(userResource);
        doNothing().when(userResource).resetPassword(any(CredentialRepresentation.class));
    }

    private void mockPasswordBadPolicy() {
        when(usersResource.get(kcUserId.toString())).thenReturn(userResource);
        doThrow(new BadRequestException("password policy"))
                .when(userResource).resetPassword(any(CredentialRepresentation.class));
    }

    private void mockRoleExistsAndAssigns() {
        when(realmResource.roles()).thenReturn(rolesResource);
        when(rolesResource.get("organizer")).thenReturn(roleResource);
        RoleRepresentation rep = new RoleRepresentation();
        rep.setName("organizer");
        when(roleResource.toRepresentation()).thenReturn(rep);

        when(usersResource.get(kcUserId.toString())).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        doNothing().when(roleScopeResource).add(anyList());
    }

    private void mockRoleMissing() {
        when(realmResource.roles()).thenReturn(rolesResource);
        when(rolesResource.get("organizer")).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenThrow(new NotFoundException("role missing"));

        when(usersResource.get(kcUserId.toString())).thenReturn(userResource);
    }

    private void mockRepoSavesOk() {
        when(userCommandRepository.save(any(User.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(organizerCommandRepository.save(any(Organizer.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
    }

    // ---------- Tests ----------

    @Test
    void registerOrganizer_success_happyPath() {
        mockCreateUser201();
        mockPasswordOk();
        mockRoleExistsAndAssigns();
        mockRepoSavesOk();

        RegisterOrganizerResult result = useCase.registerOrganizer(req())
                .toCompletableFuture().join();

        assertNotNull(result);
        assertEquals("johnny", result.username());
        assertEquals("CoolOrg", result.organizer().organizationName());
        assertFalse(result.organizer().verified());
        assertEquals(now, result.createdAt());
        assertEquals(now, result.updatedAt());

        verify(usersResource).create(any(UserRepresentation.class));
        verify(userResource).resetPassword(any(CredentialRepresentation.class));
        verify(roleScopeResource).add(anyList());
        // no deletion on success
        verify(userResource, never()).remove();

        verify(userCommandRepository).save(any(User.class));
        verify(organizerCommandRepository).save(any(Organizer.class));
    }

    @Test
    void registerOrganizer_passwordPolicyFails_cleansUpKC() {
        mockCreateUser201();
        mockPasswordBadPolicy();

        var ex = assertThrows(CompletionException.class,
                () -> useCase.registerOrganizer(req()).toCompletableFuture().join());

        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
        assertTrue(ex.getCause().getMessage().contains("Password does not meet realm policy"));

        verify(userResource).remove();      // cleanup happened
        verifyNoInteractions(userCommandRepository, organizerCommandRepository);
    }

    @Test
    void registerOrganizer_roleMissing_cleansUpKC() {
        mockCreateUser201();
        mockPasswordOk();
        mockRoleMissing();

        var ex = assertThrows(CompletionException.class,
                () -> useCase.registerOrganizer(req()).toCompletableFuture().join());

        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
        assertTrue(ex.getCause().getMessage().contains("Realm role 'organizer' does not exist"));

        verify(userResource).remove();      // cleanup happened
        verifyNoInteractions(userCommandRepository, organizerCommandRepository);
    }

    @Test
    void registerOrganizer_userRepoFails_cleansUpKC() {
        mockCreateUser201();
        mockPasswordOk();
        mockRoleExistsAndAssigns();

        when(userCommandRepository.save(any(User.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("db down")));

        var ex = assertThrows(CompletionException.class,
                () -> useCase.registerOrganizer(req()).toCompletableFuture().join());

        assertInstanceOf(RuntimeException.class, ex.getCause());
        assertEquals("db down", ex.getCause().getMessage());

        verify(userResource).remove();      // cleanup happened
        verify(organizerCommandRepository, never()).save(any());
    }

    @Test
    void registerOrganizer_organizerRepoFails_cleansUpKC() {
        mockCreateUser201();
        mockPasswordOk();
        mockRoleExistsAndAssigns();

        when(userCommandRepository.save(any(User.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(organizerCommandRepository.save(any(Organizer.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("org insert failed")));

        var ex = assertThrows(CompletionException.class,
                () -> useCase.registerOrganizer(req()).toCompletableFuture().join());

        assertInstanceOf(RuntimeException.class, ex.getCause());
        assertEquals("org insert failed", ex.getCause().getMessage());

        verify(userResource).remove();      // cleanup happened
    }

    @Test
    void registerOrganizer_conflictInKeycloak_returnsIllegalArgument() {
        mockCreateUser409();

        var ex = assertThrows(CompletionException.class,
                () -> useCase.registerOrganizer(req()).toCompletableFuture().join());

        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
        assertEquals("User already exists", ex.getCause().getMessage());

        verify(usersResource).create(any(UserRepresentation.class));
        verifyNoInteractions(userCommandRepository, organizerCommandRepository);
        // no KC deletion since user wasn’t created
        verify(userResource, never()).remove();
    }

    @Test
    void registerOrganizer_unexpectedKCStatus_throwsRuntime() {
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(500);
        doNothing().when(response).close();
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);

        var ex = assertThrows(CompletionException.class,
                () -> useCase.registerOrganizer(req()).toCompletableFuture().join());

        assertInstanceOf(RuntimeException.class, ex.getCause());
        assertTrue(ex.getCause().getMessage().contains("Failed to create Keycloak user (status 500)"));

        verify(usersResource).create(any(UserRepresentation.class));
        verifyNoInteractions(userCommandRepository, organizerCommandRepository);
        verify(userResource, never()).remove();
    }
}