package com.assetly.core.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.assetly.core.domain.Asset;
import com.assetly.core.exception.AssetNotFoundException;
import com.assetly.core.exception.NotAssetOwnerException;
import com.assetly.core.repository.AssetRepository;
import com.assetly.core.repository.InMemoryAssetRepository;

class AssetServiceTest {
    private AssetRepository repository;
    private AssetService assetService;

    private static final UUID OWNER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID OTHER_OWNER_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final String NAME = "assetName.png";
    private static final String MIME_TYPE = "image/png";
    private static final long SIZE_BYTES = 2024L;
    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        repository = new InMemoryAssetRepository();

        assetService = new AssetService(
                repository,
                Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void uploadReturnsAnAssetBuildFromTheGivenValues() {
        Asset uploaded = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);

        assertAll(
                () -> assertEquals(NAME, uploaded.name()),
                () -> assertEquals(MIME_TYPE, uploaded.mimeType()),
                () -> assertEquals(SIZE_BYTES, uploaded.sizeBytes()),
                () -> assertEquals(OWNER_ID, uploaded.ownerId()),
                () -> assertEquals(NOW, uploaded.createdAt()),
                () -> assertEquals(Optional.of(uploaded), repository.findById(uploaded.id()))
            );
    }

    @Test
    void getSavedAssetById() {
        Asset uploaded = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);
        Asset retrievedUploadedAsset = assetService.getById(uploaded.id());

        assertEquals(uploaded, retrievedUploadedAsset);
    }

    @Test
    void getByUnknownIdThrowsAssetNotFound() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var e = assertThrows(AssetNotFoundException.class,
                () -> assetService.getById(id));
        assertEquals("asset not found: " + id, e.getMessage());
    }

    @Test
    void theOwnerCanRenameItsAssetById() {
        String newName = "newName.png";
        Asset uploaded = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);
        Asset renamed = assetService.rename(uploaded.id(), OWNER_ID, newName);

        assertAll(
                () -> assertEquals(uploaded.id(), renamed.id()),
                () -> assertEquals(newName, renamed.name()),
                () -> assertEquals(uploaded.mimeType(), renamed.mimeType()),
                () -> assertEquals(uploaded.sizeBytes(), renamed.sizeBytes()),
                () -> assertEquals(uploaded.createdAt(), renamed.createdAt()));
    }

    @Test
    void assetCannotBeRenamesByOtherOwner() {
        String newName = "newName.png";
        Asset uploaded = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);

        var e = assertThrows(NotAssetOwnerException.class,
                () -> assetService.rename(uploaded.id(), OTHER_OWNER_ID, newName));
        assertEquals("user " + OTHER_OWNER_ID + " is not the owner of asset " + uploaded.id(), e.getMessage());
        assertEquals(assetService.getById(uploaded.id()).name(), uploaded.name());
    }

    @Test
    void assetCannotBeRenamedToNull() {
        Asset uploaded = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);

        var e = assertThrows(NullPointerException.class,
                () -> assetService.rename(uploaded.id(), OWNER_ID, null));
        assertEquals("name cannot be null", e.getMessage());
    }

    @Test
    void unknownAssetIdCannotBeRenamed() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var e = assertThrows(AssetNotFoundException.class,
                () -> assetService.rename(id, OWNER_ID, NAME));
        assertEquals("asset not found: " + id, e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "\t" })
    void assetCannotBeRenamedToBlank(String newName) {
        Asset uploaded = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);

        var e = assertThrows(IllegalArgumentException.class,
                () -> assetService.rename(uploaded.id(), OWNER_ID, newName));
        assertEquals("name cannot be blank", e.getMessage());
    }

    @Test
    void getListOfAssetsWithOwnerId() {
        Asset firstAsset = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);
        Asset secondAsset = assetService.upload(OTHER_OWNER_ID, "otherName.png", MIME_TYPE, SIZE_BYTES);

        assertTrue(repository.findAll().containsAll(List.of(firstAsset, secondAsset)));
        assertEquals(List.of(firstAsset), assetService.listForOwner(OWNER_ID));
    }

    @Test
    void getListOfAssetsWithUnknownOwnerIdReturnsEmpty() {
        UUID unknownOwnerId = UUID.fromString("99999999-9999-9999-9999-999999999999");
        Asset firstAsset = assetService.upload(OWNER_ID, NAME, MIME_TYPE, SIZE_BYTES);
        Asset secondAsset = assetService.upload(OTHER_OWNER_ID, "otherName.png", MIME_TYPE, SIZE_BYTES);

        assertTrue(repository.findAll().containsAll(List.of(firstAsset, secondAsset)));
        assertTrue(assetService.listForOwner(unknownOwnerId).isEmpty());
    }
}
