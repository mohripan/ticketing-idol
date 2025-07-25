package com.external.ticketingidoluserservice.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class Email {
    private final String value;

    public Email(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value;
    }

    private boolean isValid(String value) {
        return Pattern.matches("^(.+)@(.+)$", value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Email other && this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
