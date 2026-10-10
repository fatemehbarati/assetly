package com.assetly.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.assetly.core.domain.Asset;

class InMemoryAssetRepositoryTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String NAME = "logo.png";
    private static final String MIME_TYPE = "image/png";
    private static final long SIZE_BYTES = 2048L;
    private static final UUID OWNER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    private final AssetRepository repository = new InMemoryAssetRepository();
    private final Asset asset = new Asset(ID, NAME, MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);
    private final Asset otherOwnersAsset = new Asset(
            UUID.fromString("33333333-3333-3333-3333-333333333333"),
            NAME,
            MIME_TYPE,
            SIZE_BYTES,
            UUID.fromString("44444444-4444-4444-4444-444444444444"),
            CREATED_AT);

    @Test
    void newAssetCanBeAdded() {
        repository.save(asset);
        repository.save(otherOwnersAsset);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void existingAssetCanBeFoundById() {
        repository.save(asset);
        repository.save(otherOwnersAsset);

        assertEquals(asset, repository.findById(ID));
    }

    @Test
    void findByUnknownIdReturnsNull() {
        repository.save(asset);

        assertNull(repository.findById(UUID.randomUUID()));
    }

    @Test
    void nullAssetCannotBeSaved() {
        var e = assertThrows(NullPointerException.class,
            () -> repository.save(null)
        );
        assertEquals("asset cannot be null", e.getMessage());
    }

    @Test
    void assetCanBeFoundByOwnerId() {
        repository.save(asset);
        repository.save(otherOwnersAsset);

        assertEquals(List.of(otherOwnersAsset), repository.findByOwnerId(otherOwnersAsset.ownerId()));
    }

    @Test
    void deleteExistingAssetWithIdReturnsTrue() {
        repository.save(asset);

        assertTrue(repository.deleteById(ID));
    }

    @Test
    void deleteNonExistingAssetWithIdReturnsFalse() {
        repository.save(asset);

        assertFalse(repository.deleteById(UUID.randomUUID()));
    }

    @Test
    void deleteExistingAssetWithIdRemovesAssetFromList() {
        repository.save(asset);
        repository.deleteById(ID);

        assertEquals(List.of(), repository.findAll());
    }

    @Test
    void savingWithAnExistingIdReplacesTheAsset() {
        Asset replacement = new Asset(ID, "renamed.png", MIME_TYPE, SIZE_BYTES, OWNER_ID, CREATED_AT);

        repository.save(asset);
        repository.save(replacement);

        assertEquals(List.of(replacement), repository.findAll());
    }

    @Test
    void findAllResultCannotBeModified() {
        assertThrows(UnsupportedOperationException.class,
            () -> repository.findAll().add(asset)
        );
    }
}
