package com.assetly.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.assetly.core.domain.Folder;

class InMemoryFolderRepositoryTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID OWNER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final String NAME = "FolderName";
    private static final List<UUID> ASSET_IDS = List.of(UUID.fromString("33333333-3333-3333-3333-333333333333"));

    private static final UUID OTHER_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final UUID OTHER_ASSET_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");
    private static final UUID OTHER_OWNER_ID = UUID.fromString("66666666-6666-6666-6666-666666666666");

    private final FolderRepository repository = new InMemoryFolderRepository();

    @Test
    void nullFolderCannotBeSaved() {
        var e = assertThrows(NullPointerException.class,
                () -> repository.save(null));

        assertEquals("folder cannot be null", e.getMessage());
    }

    @Test
    void folderIsSavedSuccessfully() {
        Folder folder = folderOf(ID, OWNER_ID, NAME);
        repository.save(folder);

        assertEquals(List.of(folder), repository.findAll());
    }

    @Test
    void folderCanBeFoundById() {
        Folder folder = folderOf(ID, OWNER_ID, NAME);
        repository.save(folder);

        assertEquals(folder, repository.findById(ID));
    }

    @Test
    void findByUnknownIdReturnsNull() {
        repository.save(folderOf(ID, OWNER_ID, NAME));

        assertNull(repository.findById(OTHER_ID));
    }

    @Test
    void folderCanBeFoundByOwnerId() {
        Folder firstFolder = folderOf(ID, OWNER_ID, NAME);
        Folder secondFolder = folderOf(OTHER_ID, OTHER_OWNER_ID, "secondFolder");

        repository.save(firstFolder);
        repository.save(secondFolder);

        assertEquals(List.of(firstFolder), repository.findByOwnerId(OWNER_ID));
    }

    @Test
    void findByUnknownOwnerIdReturnsEmptyList() {
        repository.save(folderOf(ID, OWNER_ID, NAME));

        assertEquals(List.of(), repository.findByOwnerId(OTHER_OWNER_ID));
    }

    @Test
    void emptyFoldersReturnsEmptyList() {
        assertEquals(List.of(), repository.findAll());
    }

    @Test
    void findAllFoldersSuccessfully() {
        Folder firstFolder = folderOf(ID, OWNER_ID, NAME);
        Folder secondFolder = folderOf(OTHER_ID, OWNER_ID, "second folder");

        repository.save(firstFolder);
        repository.save(secondFolder);

        assertEquals(2, repository.findAll().size());
        assertTrue(repository.findAll().containsAll(List.of(firstFolder, secondFolder)));
    }

    @Test
    void savingWithAnExistingIdReplacesTheFolder() {
        Folder folder = folderOf(ID, OWNER_ID, NAME);
        Folder replacement = folderOf(ID, OWNER_ID, "replacement");

        repository.save(folder);
        repository.save(replacement);

        assertEquals(List.of(replacement), repository.findAll());
    }

    @Test
    void savingAFolderWithANewAssetUpdatesTheStoredFolder() {
        Folder folder = folderOf(ID, OWNER_ID, NAME);
        repository.save(folder);
        Folder updated = folder.withAsset(OTHER_ASSET_ID);
        repository.save(updated);

        assertEquals(updated.assetIds(), repository.findById(ID).assetIds());
    }

    @Test
    void findAllResultCannotBeModified() {
        assertThrows(UnsupportedOperationException.class,
            () -> repository.findAll().add(folderOf(ID, OWNER_ID, NAME))
        );
    }

    private static Folder folderOf(UUID id, UUID ownerId, String name) {
        return new Folder(id, ownerId, name, ASSET_IDS);
    }
}
