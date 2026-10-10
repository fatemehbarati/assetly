package com.assetly.core.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.assetly.core.domain.Asset;

public class InMemoryAssetRepository implements AssetRepository {
    private final Map<UUID, Asset> assets = new HashMap<>();

    @Override
    public void save(Asset asset) {
        Objects.requireNonNull(asset, "asset cannot be null");

        assets.put(asset.id(), asset);
    }

    @Override
    public Asset findById(UUID id) {
        return assets.get(id);
    }

    @Override
    public List<Asset> findAll() {
        return List.copyOf(assets.values());
    }

    @Override
    public List<Asset> findByOwnerId(UUID ownerId) {
        return assets.values().stream().filter(
                asset -> asset.ownerId().equals(ownerId))
                .toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        return assets.remove(id) != null;
    }
}
