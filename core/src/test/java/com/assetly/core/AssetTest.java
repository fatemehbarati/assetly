package com.assetly.core;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssetTest {
    final String NAME = "logo.png";
    final String MIME_TYPE = "image/png";
    final long SIZE_BYTES = 2048L;
    final Instant CREATED_AT = Instant.now();

    @Test
    void exposesTheValuesItWasCreatedWith() {
        UUID id = UUID.randomUUID();

        Asset asset = new Asset(id, NAME, MIME_TYPE, SIZE_BYTES, CREATED_AT);

        assertAll(
                () -> assertEquals(id, asset.id()),
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
                () -> new Asset(UUID.randomUUID(), null, MIME_TYPE, SIZE_BYTES, CREATED_AT));
        assertEquals("name cannot be null", e.getMessage());
    }

    @Test
    void nullMimeTypeIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(UUID.randomUUID(), NAME, null, SIZE_BYTES, CREATED_AT));
        assertEquals("mimeType cannot be null", e.getMessage());
    }

    @Test
    void nullCreatedAtIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(UUID.randomUUID(), NAME, MIME_TYPE, SIZE_BYTES, null));
        assertEquals("createdAt cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " " })
    void blankNameIsNotAllowed(String name) {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new Asset(UUID.randomUUID(), name, MIME_TYPE, SIZE_BYTES, CREATED_AT));
        assertEquals("name cannot be blank", e.getMessage());
    }

    @Test
    void negativeSizeBytesIsNotAllowed() {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new Asset(UUID.randomUUID(), NAME, MIME_TYPE, -1L, CREATED_AT));
        assertEquals("sizeBytes cannot be negative", e.getMessage());
    }

    @Test
    void zeroSizeBytesIsAllowed() {
        Asset asset = new Asset(UUID.randomUUID(), NAME, MIME_TYPE, 0, CREATED_AT);
        assertEquals(0, asset.sizeBytes());
    }

    @Test 
    void twoAssetsBuiltFromIdenticalValuesAreEqual() {
        UUID id = UUID.randomUUID();
        Instant createdAt = CREATED_AT;
        Asset firstAsset = new Asset(id, "asset.png", MIME_TYPE, SIZE_BYTES, createdAt);
        Asset secondAsset = new Asset(id, "asset.png", MIME_TYPE, SIZE_BYTES, createdAt);

        assertEquals(firstAsset, secondAsset);
        assertEquals(firstAsset.hashCode(), secondAsset.hashCode());
    }
}
