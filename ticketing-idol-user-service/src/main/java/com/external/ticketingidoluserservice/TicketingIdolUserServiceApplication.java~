package com.external.ticketingidoluserservice;

import com.external.ticketingidoluserservice.infrastructure.web.UserServiceVerticle;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.Vertx;

public class TicketingIdolUserServiceApplication {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new UserServiceVerticle());
    }

}
