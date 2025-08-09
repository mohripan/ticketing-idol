package com.external.ticketingidoluserservice.infrastructure.persistance;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class HelperUtil {
    public static OffsetDateTime odt(Instant i) {
        return i.atOffset(ZoneOffset.UTC);
    }
}
