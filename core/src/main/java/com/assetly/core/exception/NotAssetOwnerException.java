package com.assetly.core.exception;

import java.util.UUID;

public class NotAssetOwnerException extends RuntimeException {
    private final UUID userId;
    private final UUID assetId;

    public NotAssetOwnerException(UUID userId, UUID assetId) {
        super("user " + userId + " is not the owner of asset " + assetId);
        this.userId = userId;
        this.assetId = assetId;
    }

    public UUID userId() {
        return this.userId;
    }

    public UUID assetId() {
        return this.assetId;
    }
}
