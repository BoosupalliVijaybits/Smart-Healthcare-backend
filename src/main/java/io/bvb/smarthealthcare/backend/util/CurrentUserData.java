package io.bvb.smarthealthcare.backend.util;

import io.bvb.smarthealthcare.backend.model.UserResponse;

public class CurrentUserData {
    private static final ThreadLocal<UserResponse> CURRENT_USER = new ThreadLocal<>();

    public static UserResponse getUser() {
        return CURRENT_USER.get();
    }

    public static void setUser(UserResponse userResponse) {
        CURRENT_USER.set(userResponse);
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}

