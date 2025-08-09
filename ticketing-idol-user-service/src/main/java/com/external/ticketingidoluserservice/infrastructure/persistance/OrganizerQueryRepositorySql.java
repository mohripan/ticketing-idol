package com.external.ticketingidoluserservice.infrastructure.persistance;

import com.external.ticketingidoluserservice.domain.model.Organizer;
import com.external.ticketingidoluserservice.domain.repository.OrganizerQueryRepository;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class OrganizerQueryRepositorySql implements OrganizerQueryRepository {
    private final PgPool pg;

    public OrganizerQueryRepositorySql(PgPool pg) {
        this.pg = pg;
    }


    @Override
    public CompletionStage<Optional<Organizer>> findByUserId(UUID userId) {
        final String sql = """
                select user_id, organization_name, verified, created_at, updated_at
                from organizers where user_id = $1
                """;

        var fut = new CompletableFuture<Optional<Organizer>>();
        pg.preparedQuery(sql).execute(Tuple.of(userId), ar -> {
           if (ar.failed()) {
               fut.completeExceptionally(ar.cause()); return;
           }
           var rows = ar.result();
           if (rows == null || rows.size() == 0) {
               fut.complete(Optional.empty()); return;
           }
           Row r = rows.iterator().next();
            Organizer o = Organizer.builder()
                    .userId(r.get(UUID.class, 0))
                    .organizationName(r.getString(1))
                    .verified(Boolean.TRUE.equals(r.getBoolean(2)))
                    .createdAt(r.get(OffsetDateTime.class, 3).toInstant())
                    .updatedAt(r.get(OffsetDateTime.class, 4).toInstant())
                    .build();
            fut.complete(Optional.of(o));
        });
        return fut;
    }
}
