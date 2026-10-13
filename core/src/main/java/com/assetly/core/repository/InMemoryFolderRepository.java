package com.assetly.core.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.assetly.core.domain.Folder;

public class InMemoryFolderRepository implements FolderRepository {
    private final Map<UUID, Folder> folders = new HashMap<>();

    @Override
    public void save(Folder folder) {
        Objects.requireNonNull(folder, "folder cannot be null");

        folders.put(folder.id(), folder);
    }

    @Override
    public List<Folder> findAll() {
        return List.copyOf(folders.values());
    }

    @Override
    public Optional<Folder> findById(UUID id) {
        return Optional.ofNullable(folders.get(id));
    }

    @Override
    public List<Folder> findByOwnerId(UUID ownerId) {
        return folders.values().stream().filter(
                folder -> folder.ownerId().equals(ownerId))
                .toList();
    }
}
