package com.assetly.core.domain;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public record User(
        UUID id,
        String email,
        String displayName,
        Instant createdAt) {
    public User {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(displayName, "displayName cannot be null");
        Objects.requireNonNull(createdAt, "createdAt cannot be null");

        if (displayName.isBlank()) {
            throw new IllegalArgumentException("displayName cannot be blank");
        }

        if (email.isBlank()) {
            throw new IllegalArgumentException("email cannot be blank");
        }

        email = email.toLowerCase(Locale.ROOT).strip();
        int at = email.indexOf('@');
        if (at <= 0 || at != email.lastIndexOf("@") || at == email.length() - 1) {
            throw new IllegalArgumentException("email must contain a single @ between a name and a domain");
        }
    }
}
