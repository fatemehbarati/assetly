package com.assetly.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Folder(
        UUID id,
        UUID ownerId,
        String name,
        List<UUID> assetIds) {
    public Folder {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(ownerId, "ownerId cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(assetIds, "assetIds cannot be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }

        if (assetIds.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("assetIds cannot contain null");
        }

        assetIds = List.copyOf(assetIds);

        if (new HashSet<>(assetIds).size() != assetIds.size()) {
            throw new IllegalArgumentException("assetIds cannot contain duplicates");
        }
    }

    public Folder withAsset(UUID assetId) {
        Objects.requireNonNull(assetId, "assetId cannot be null");

        List<UUID> newAssetIds = new ArrayList<>(assetIds);
        newAssetIds.add(assetId);
        return new Folder(id, ownerId, name, newAssetIds);
    }
}
