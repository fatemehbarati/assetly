package com.assetly.core.domain;

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
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String NAME = "logo.png";
    private static final String MIME_TYPE = "image/png";
    private static final long SIZE_BYTES = 2048L;
    private static final UUID OWNER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void exposesTheValuesItWasCreatedWith() {
        Asset asset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);

        assertAll(
                () -> assertEquals(ID, asset.id()),
                () -> assertEquals(NAME, asset.name()),
                () -> assertEquals(MIME_TYPE, asset.mimeType()),
                () -> assertEquals(SIZE_BYTES, asset.sizeBytes()),
                () -> assertEquals(OWNER_ID, asset.ownerId()),
                () -> assertEquals(CREATED_AT, asset.createdAt()));
    }

    @Test
    void nullIdIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(null, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT));
        assertEquals("id cannot be null", e.getMessage());
    }

    @Test
    void nullNameIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(ID, null, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT));
        assertEquals("name cannot be null", e.getMessage());
    }

    @Test
    void nullMimeTypeIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(ID, NAME, null, SIZE_BYTES, OWNER_ID, CREATED_AT));
        assertEquals("mimeType cannot be null", e.getMessage());
    }

    @Test
    void nullOwnerIdIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, null, CREATED_AT));
        assertEquals("ownerId cannot be null", e.getMessage());
    }

    @Test
    void nullCreatedAtIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, null));
        assertEquals("createdAt cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "\t" })
    void blankNameIsNotAllowed(String name) {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new Asset(ID, name, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT));
        assertEquals("name cannot be blank", e.getMessage());
    }

    @Test
    void negativeSizeBytesIsNotAllowed() {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new Asset(ID, NAME, MIME_TYPE, -1L, OWNER_ID, CREATED_AT));
        assertEquals("sizeBytes cannot be negative", e.getMessage());
    }

    @Test
    void zeroSizeBytesIsAllowed() {
        Asset asset = new Asset(ID, NAME, MIME_TYPE, 0, OWNER_ID, CREATED_AT);
        assertEquals(0, asset.sizeBytes());
    }

    @Test
    void twoAssetsBuiltFromIdenticalValuesAreEqual() {
        Asset firstAsset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);
        Asset secondAsset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);

        assertEquals(firstAsset, secondAsset);
        assertEquals(firstAsset.hashCode(), secondAsset.hashCode());
    }

    @Test
    void assetsWithDifferentIdsAreNotEqual() {
        Asset firstAsset = new Asset(UUID.randomUUID(), NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);
        Asset secondAsset = new Asset(UUID.randomUUID(), NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);

        assertNotEquals(firstAsset, secondAsset);
    }

    @Test
    void assetCannotBeRenamedToNull() {
        Asset asset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);

        var e = assertThrows(NullPointerException.class,
            () -> asset.withName(null)
        );
        assertEquals("name cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource (strings = {"", " "})
    void assetCannotBeRenamedToBlank(String newName) {
        Asset asset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);

        var e = assertThrows(IllegalArgumentException.class,
            () -> asset.withName(newName)
        );
        assertEquals("name cannot be blank", e.getMessage());
    }

    @Test
    void assetCanBeRenamed() {
        Asset asset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);
        Asset renamed = asset.withName("new name");

        assertAll(
                () -> assertEquals(ID, renamed.id()),
                () -> assertEquals("new name", renamed.name()),
                () -> assertEquals(MIME_TYPE, renamed.mimeType()),
                () -> assertEquals(SIZE_BYTES, renamed.sizeBytes()),
                () -> assertEquals(OWNER_ID, renamed.ownerId()),
                () -> assertEquals(CREATED_AT, renamed.createdAt()));
    }
}
