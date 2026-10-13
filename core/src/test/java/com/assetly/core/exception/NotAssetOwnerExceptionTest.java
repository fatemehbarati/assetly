package com.assetly.core.exception;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class NotAssetOwnerExceptionTest {
    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ASSET_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void messageNamesTheUserIdAndTheAssetId() {
        var e = new NotAssetOwnerException(USER_ID, ASSET_ID);

        assertEquals("user " + USER_ID + " is not the owner of asset " + ASSET_ID, e.getMessage());
    }

    @Test
    void exposesTheUserIdAndAssetId() {
        var e = new NotAssetOwnerException(USER_ID, ASSET_ID);

        assertAll(
            () -> assertEquals(USER_ID, e.userId()),
            () -> assertEquals(ASSET_ID, e.assetId())
        );
    }
}
