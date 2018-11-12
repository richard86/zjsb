package com.kcmap.frame.utils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by lizhiwei on 2018/11/12.
 */
public class PermissionUtils {
    private static final int REQUEST_CODE = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.CAMERA};

    public static void verifyStoragePermissions(Activity activity) {
        // We don't have permission so prompt the user
        ActivityCompat.requestPermissions(activity, PERMISSIONS,
                REQUEST_CODE);
    }

}
