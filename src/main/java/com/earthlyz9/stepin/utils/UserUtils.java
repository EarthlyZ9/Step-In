package com.earthlyz9.stepin.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UserUtils {

    public static String generateGuestTempEmail() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX) + "@guest.email";
    }
}
