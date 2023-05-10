package com.earthlyz9.stepin.entities;

import com.earthlyz9.stepin.exceptions.PermissionDeniedException;

public abstract class NeedsPermission {
    protected int ownerId;

    public void checkPermission(int requestUserId) throws PermissionDeniedException {
        if (requestUserId != ownerId) {
            throw new PermissionDeniedException("access denied");
        }
    }

}
