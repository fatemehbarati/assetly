package com.assetly.core.repository;

import java.util.List;
import java.util.UUID;

import com.assetly.core.domain.Asset;

public interface AssetRepository {
    void save(Asset asset);
    Asset findById(UUID id);
    List<Asset> findAll();
    List<Asset> findByOwnerId(UUID ownerId);
    boolean deleteById(UUID id);
}
