package com.assetly.core.service;

import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import com.assetly.core.domain.Asset;
import com.assetly.core.repository.AssetRepository;
import com.assetly.core.exception.AssetNotFoundException;
import com.assetly.core.exception.NotAssetOwnerException;

public class AssetService {
    private static final Comparator<Asset> NEWEST_FIRST = Comparator.comparing(Asset::createdAt).reversed()
            .thenComparing(Asset::name);

    private final AssetRepository repository;
    private final Clock clock;

    public AssetService(AssetRepository repository, Clock clock) {
        this.repository = Objects.requireNonNull(repository, "repository cannot be null");
        this.clock = Objects.requireNonNull(clock, "clock cannot be null");
    }

    public Asset upload(UUID ownerId, String name, String mimeType, long sizeBytes) {
        Asset asset = new Asset(UUID.randomUUID(), name, mimeType, sizeBytes, ownerId, Instant.now(clock));
        repository.save(asset);
        return asset;
    }

    public Asset getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new AssetNotFoundException(id));
    }

    public Asset rename(UUID assetId, UUID requesterId, String newName) {
        Asset asset = getById(assetId);

        if (!asset.ownerId().equals(requesterId)) {
            throw new NotAssetOwnerException(requesterId, assetId);
        }
        Asset renamedAsset = asset.withName(newName);

        repository.save(renamedAsset);
        return renamedAsset;
    }

    public List<Asset> listForOwner(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream().sorted(NEWEST_FIRST).toList();
    }

    public void delete(UUID assetId, UUID requesterId) {
        Asset asset = getById(assetId);
        if (!asset.ownerId().equals(requesterId)) {
            throw new NotAssetOwnerException(requesterId, assetId);
        }
        repository.deleteById(assetId);
    }

    public List<Asset> searchByName(UUID ownerId, String query) {
        Objects.requireNonNull(query, "query cannot be null");
        if (query.isBlank()) {
            throw new IllegalArgumentException("query cannot be blank");
        }

        String needle = query.toLowerCase(Locale.ROOT);
        return listForOwner(ownerId).stream()
                .filter(asset -> asset.name().toLowerCase(Locale.ROOT).contains(needle))
                .toList();
    }
}
