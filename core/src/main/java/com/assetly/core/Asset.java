package com.assetly.core;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Asset(
        UUID id,
        String name,
        String mimeType,
        long sizeBytes,
        Instant createdAt) {
    public Asset {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(mimeType, "mimeType cannot be null");
        Objects.requireNonNull(createdAt, "createdAt cannot be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }

        if (sizeBytes < 0) {
            throw new IllegalArgumentException("sizeBytes cannot be negative");
        }
    }
}
