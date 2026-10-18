package com.assetly.core.service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.assetly.core.domain.Asset;
import com.assetly.core.repository.AssetRepository;
import com.assetly.core.exception.AssetNotFoundException;
import com.assetly.core.exception.NotAssetOwnerException;

public class AssetService {
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
        return repository.findByOwnerId(ownerId);
    }
}
