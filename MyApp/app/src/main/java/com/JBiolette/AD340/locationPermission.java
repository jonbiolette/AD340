package com.JBiolette.AD340;

import android.app.Application;

public class locationPermission extends Application {

    private static Boolean permission;

    public static Boolean getPermission() {
        return permission;
    }

    public static void setPermission(Boolean permission) {
        locationPermission.permission = permission;
    }
}
