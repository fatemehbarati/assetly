package com.assetly.core.exception;

import java.util.UUID;

public class AssetNotFoundException extends RuntimeException {
    private final UUID assetId;

    public AssetNotFoundException(UUID assetId) {
        super("asset not found: " + assetId);
        this.assetId = assetId;
    }

    public UUID assetId() {
        return this.assetId;
    }
}
