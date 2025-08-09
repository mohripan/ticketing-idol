package com.external.ticketingidoluserservice.infrastructure.config;

import io.vertx.core.Vertx;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;

public final class PgClientFactory {
    private PgClientFactory() {}

    public static PgPool create(Vertx vertx, String host, int port, String database, String user, String password) {
        PgConnectOptions connect = new PgConnectOptions()
                .setHost(host)
                .setPort(port)
                .setDatabase(database)
                .setUser(user)
                .setPassword(password)
                .setCachePreparedStatements(true);
        PoolOptions pool = new PoolOptions().setMaxSize(10);
        return PgPool.pool(vertx, connect, pool);
    }
}
