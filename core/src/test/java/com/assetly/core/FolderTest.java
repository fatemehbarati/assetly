package com.assetly.core;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FolderTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID OWNER_ID = UUID.fromString("22222222-1111-1111-1111-111111111111");
    private static final String NAME = "FolderName";
    private static final List<UUID> ASSET_IDS = List.of(UUID.fromString("33333333-1111-1111-1111-111111111111"));

    @Test
    void exposesTheValuesItWasCreatedWith() {
        Folder folder = new Folder(ID, OWNER_ID, NAME, ASSET_IDS);

        assertAll(
                () -> assertEquals(ID, folder.id()),
                () -> assertEquals(OWNER_ID, folder.ownerId()),
                () -> assertEquals(NAME, folder.name()),
                () -> assertEquals(ASSET_IDS, folder.assetIds()));
    }

    @Test
    void nullIdIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Folder(null, OWNER_ID, NAME, ASSET_IDS));
        assertEquals("id cannot be null", e.getMessage());
    }

    @Test
    void nullNameIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Folder(ID, OWNER_ID, null, ASSET_IDS));
        assertEquals("name cannot be null", e.getMessage());
    }

    @Test
    void nullAssetIdsIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Folder(ID, OWNER_ID, NAME, null));
        assertEquals("assetIds cannot be null", e.getMessage());
    }

    @Test
    void nullOwnerIdIsNotAllowed() {
        var e = assertThrows(NullPointerException.class,
                () -> new Folder(ID, null, NAME, ASSET_IDS));
        assertEquals("ownerId cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "\t" })
    void blankNameIsNotAllowed(String name) {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new Folder(ID, OWNER_ID, name, ASSET_IDS));

        assertEquals("name cannot be blank", e.getMessage());
    }

    @Test
    void changingTheOriginalListDoesNotChangeTheFolder() {
        List<UUID> ids = new ArrayList<>(ASSET_IDS);
        Folder folder = new Folder(ID, OWNER_ID, NAME, ids);
        ids.add(UUID.randomUUID());
        assertEquals(ASSET_IDS, folder.assetIds());
    }

    @Test
    void assetIdsCannotBeModified() {
        Folder folder = new Folder(ID, OWNER_ID, NAME, ASSET_IDS);
        assertThrows(UnsupportedOperationException.class,
                () -> folder.assetIds().add(UUID.randomUUID()));
    }

    @Test
    void duplicateAssetIdsAreNotAllowed() {
        List<UUID> ids = new ArrayList<>(ASSET_IDS);
        ids.add(ids.get(0));

        var e = assertThrows(IllegalArgumentException.class,
                () -> new Folder(ID, OWNER_ID, NAME, ids));
        assertEquals("assetIds cannot contain duplicates", e.getMessage());
    }

    @Test
    void withAssetReturnsAFolderContainingTheNewAsset() {
        Folder folder = new Folder(ID, OWNER_ID, NAME, ASSET_IDS);
        UUID newAssetId = UUID.fromString("44444444-1111-1111-1111-111111111111");

        Folder updated = folder.withAsset(newAssetId);

        assertEquals(List.of(ASSET_IDS.get(0), newAssetId), updated.assetIds());
    }

    @Test
    void withAssetAddsNewAssetIdToTheOriginalFolder() {
        Folder folder = new Folder(ID, OWNER_ID, NAME, ASSET_IDS);
        folder.withAsset(UUID.fromString("44444444-1111-1111-1111-111111111111"));

        assertEquals(ASSET_IDS, folder.assetIds());
    }

    @Test
    void withAssetDoesNotChangeTheOriginalFolder() {
        Folder folder = new Folder(ID, OWNER_ID, NAME, ASSET_IDS);
        Folder updated = folder.withAsset(UUID.fromString("44444444-1111-1111-1111-111111111111"));

        assertAll(
                () -> assertEquals(ID, updated.id()),
                () -> assertEquals(OWNER_ID, updated.ownerId()),
                () -> assertEquals(NAME, updated.name()));
    }

    @Test
    void sameAssetCannotBeAddedTwice() {
        Folder folder = new Folder(ID, OWNER_ID, NAME, ASSET_IDS);

        var e = assertThrows(IllegalArgumentException.class,
                () -> folder.withAsset(ASSET_IDS.get(0)));
        assertEquals("assetIds cannot contain duplicates", e.getMessage());
    }

    @Test
    void nullAssetIdCannotBeAdded() {
        Folder folder = new Folder(ID, OWNER_ID, NAME, ASSET_IDS);
        var e = assertThrows(NullPointerException.class,
                () -> folder.withAsset(null));
        assertEquals("assetId cannot be null", e.getMessage());
    }

    @Test
    void assetIdsCannotContainNull() {
        List<UUID> ids = Arrays.asList(ASSET_IDS.get(0), null);
        var e = assertThrows(NullPointerException.class,
                () -> new Folder(ID, OWNER_ID, NAME, ids));
        assertEquals("assetIds cannot contain null", e.getMessage());
    }
}
