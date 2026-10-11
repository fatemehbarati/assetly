package com.assetly.core.repository;

import java.util.List;
import java.util.UUID;

import com.assetly.core.domain.Folder;

public interface FolderRepository {
    void save(Folder folder);
    Folder findById(UUID id);
    List<Folder> findAll();
    List<Folder> findByOwnerId(UUID ownerId);
}
