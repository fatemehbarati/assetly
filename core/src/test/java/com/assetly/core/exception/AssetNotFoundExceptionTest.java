package com.assetly.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class AssetNotFoundExceptionTest {
    private static final UUID ASSET_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Test
    void messageNamesTheAssetId() {
        var e = new AssetNotFoundException(ASSET_ID);

        assertEquals("asset not found: " + ASSET_ID, e.getMessage());
    }

    @Test
    void exposesTheAssetId() {
        var e = new AssetNotFoundException(ASSET_ID);

        assertEquals(ASSET_ID, e.assetId());
    }
}
