package com.assetly.core;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssetTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String NAME = "logo.png";
    private static final String MIME_TYPE = "image/png";
    private static final long SIZE_BYTES = 2048L;
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void exposesTheValuesItWasCreatedWith() {
        Asset asset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, CREATED_AT);

        assertAll(
                () -> assertEquals(ID, asset.id()),
                () -> assertEquals(NAME, asset.name()),
                () -> assertEquals(MIME_TYPE, asset.mimeType()),
                () -> assertEquals(SIZE_BYTES, asset.sizeBytes()),
                () -> assertEquals(CREATED_AT, asset.createdAt()));
    }

    @Test
    void nullIdIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(null, NAME, MIME_TYPE, SIZE_BYTES, CREATED_AT));
        assertEquals("id cannot be null", e.getMessage());
    }

    @Test
    void nullNameIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(ID, null, MIME_TYPE, SIZE_BYTES, CREATED_AT));
        assertEquals("name cannot be null", e.getMessage());
    }

    @Test
    void nullMimeTypeIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(ID, NAME, null, SIZE_BYTES, CREATED_AT));
        assertEquals("mimeType cannot be null", e.getMessage());
    }

    @Test
    void nullCreatedAtIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, null));
        assertEquals("createdAt cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    void blankNameIsNotAllowed(String name) {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new Asset(ID, name, MIME_TYPE, SIZE_BYTES, CREATED_AT));
        assertEquals("name cannot be blank", e.getMessage());
    }

    @Test
    void negativeSizeBytesIsNotAllowed() {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new Asset(ID, NAME, MIME_TYPE, -1L, CREATED_AT));
        assertEquals("sizeBytes cannot be negative", e.getMessage());
    }

    @Test
    void zeroSizeBytesIsAllowed() {
        Asset asset = new Asset(ID, NAME, MIME_TYPE, 0, CREATED_AT);
        assertEquals(0, asset.sizeBytes());
    }

    @Test
    void twoAssetsBuiltFromIdenticalValuesAreEqual() {
        Asset firstAsset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, CREATED_AT);
        Asset secondAsset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, CREATED_AT);

        assertEquals(firstAsset, secondAsset);
        assertEquals(firstAsset.hashCode(), secondAsset.hashCode());
    }

    @Test
    void assetsWithDifferentIdsAreNotEqual() {
        Asset firstAsset = new Asset(UUID.randomUUID(), NAME, MIME_TYPE, SIZE_BYTES, CREATED_AT);
        Asset secondAsset = new Asset(UUID.randomUUID(), NAME, MIME_TYPE, SIZE_BYTES, CREATED_AT);

        assertNotEquals(firstAsset, secondAsset);
    }
}
