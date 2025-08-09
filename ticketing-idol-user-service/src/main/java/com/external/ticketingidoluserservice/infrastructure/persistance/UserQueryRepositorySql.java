package com.external.ticketingidoluserservice.infrastructure.persistance;

import com.external.ticketingidoluserservice.domain.model.User;
import com.external.ticketingidoluserservice.domain.repository.UserQueryRepository;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserQueryRepositorySql implements UserQueryRepository {
    private final PgPool pg;

    public UserQueryRepositorySql(PgPool pg){ this.pg = pg; }

    @Override
    public CompletionStage<Optional<User>> findByUserId(UUID userId) {
        final String sql = """
                select id, keycloak_id, username, profile_picture_id, created_at, updated_at
                from users where id = $1
                """;
        var fut = new CompletableFuture<Optional<User>>();
        pg.preparedQuery(sql).execute(Tuple.of(userId), ar -> {
           if (ar.failed()) { fut.completeExceptionally(ar.cause()); return; }
           var rows = ar.result();
           if (rows == null || rows.size() == 0) { fut.complete(Optional.empty()); return; }
           Row r = rows.iterator().next();
           User u = User.builder()
                   .id(r.get(UUID.class, 0))
                   .keycloakId(r.get(UUID.class, 1))
                   .username(r.getString(2))
                   .profilePictureId(r.get(UUID.class, 3))
                   .createdAt(r.get(OffsetDateTime.class, 4).toInstant())
                   .updatedAt(r.get(OffsetDateTime.class, 5).toInstant())
                   .build();
           fut.complete(Optional.of(u));
        });
        return fut;
    }
}
