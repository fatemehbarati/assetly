package com.assetly.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.assetly.core.domain.Asset;

public final class AssetStats {
    private AssetStats() {}

    public static Map<UUID, Integer> countByOwner(List<Asset> assets) {
        Objects.requireNonNull(assets, "assets cannot be null");

        Map<UUID, Integer> counter = new HashMap<>();

        for (Asset asset : assets) {
            counter.merge(asset.ownerId(), 1, Integer::sum);
        }

        return Map.copyOf(counter);
    }
}
