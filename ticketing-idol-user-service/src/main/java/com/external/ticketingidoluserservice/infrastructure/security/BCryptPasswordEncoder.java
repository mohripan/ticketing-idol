package com.external.ticketingidoluserservice.infrastructure.security;

import com.external.ticketingidoluserservice.domain.security.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {
    private final int logRounds;

    public BCryptPasswordEncoder(int logRounds) {
        this.logRounds = logRounds;
    }

    @Override
    public String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(logRounds));
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
