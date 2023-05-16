package com.earthlyz9.stepin.entities;

import com.earthlyz9.stepin.exceptions.PermissionDeniedException;

public interface NeedsPermission {

    void checkPermission(int requestUserId) throws PermissionDeniedException;

}
