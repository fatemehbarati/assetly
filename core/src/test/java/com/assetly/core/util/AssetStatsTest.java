package com.assetly.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.assetly.core.domain.Asset;

class AssetStatsTest {
    private static final String NAME = "logo.png";
    private static final String MIME_TYPE = "image/png";
    private static final long SIZE_BYTES = 2048L;
    private static final UUID OWNER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID OTHER_OWNER_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void nullAssetsIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
            () -> AssetStats.countByOwner(null)
        );

        assertEquals("assets cannot be null", e.getMessage());
    }

    @Test
    void anEmptyListOfAssetsReturnsEmptyMap() {
        Map<UUID, Integer> assetCountPerOwner = AssetStats.countByOwner(List.of());

        assertEquals(Map.of(), assetCountPerOwner);
    }

    @Test
    void oneAssetReturnsOneResult() {
        List<Asset> assets = List.of(assetOf(OWNER_ID));
        Map<UUID, Integer> assetCountPerOwner = AssetStats.countByOwner(assets);

        assertEquals(Map.of(OWNER_ID, 1), assetCountPerOwner);
    }

    @Test
    void countsSeveralAssetsOfTheSameOwner() {
        List<Asset> assets = List.of(assetOf(OWNER_ID), assetOf(OWNER_ID), assetOf(OWNER_ID));
        Map<UUID, Integer> assetCountPerOwner = AssetStats.countByOwner(assets);

        assertEquals(Map.of(OWNER_ID, 3), assetCountPerOwner);
    }

    @Test
    void countsEachOwnerSeparately() {
        List<Asset> assets = List.of(assetOf(OWNER_ID), assetOf(OTHER_OWNER_ID), assetOf(OTHER_OWNER_ID));
        Map<UUID, Integer> assetCountPerOwner = AssetStats.countByOwner(assets);

        assertEquals(Map.of(OWNER_ID, 1, OTHER_OWNER_ID, 2), assetCountPerOwner);
    }

    @Test
    void resultIsNotModifiable() {
        List<Asset> assets = List.of(assetOf(OWNER_ID));
        Map<UUID, Integer> assetCountPerOwner = AssetStats.countByOwner(assets);

        assertThrows(UnsupportedOperationException.class,
            () -> assetCountPerOwner.put(OTHER_OWNER_ID, 1)
        );
    }

    @Test
    void changingTheInputListAfterwardsDoesNotChangeTheResult() {
        List<Asset> assets = new ArrayList<>(List.of(assetOf(OWNER_ID)));
        Map<UUID, Integer> assetCountPerOwner = AssetStats.countByOwner(assets);
        assets.add(assetOf(OTHER_OWNER_ID));

        assertEquals(Map.of(OWNER_ID, 1), assetCountPerOwner);
    }

    private static Asset assetOf(UUID ownerId) {
        return new Asset(UUID.randomUUID(), NAME, MIME_TYPE, SIZE_BYTES, ownerId, CREATED_AT);
    }
}
