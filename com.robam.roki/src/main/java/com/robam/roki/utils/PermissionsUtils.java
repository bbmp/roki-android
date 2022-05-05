package com.robam.roki.utils;

import android.app.Activity;
import androidx.core.app.ActivityCompat;
import android.content.Context;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/12.
 * PS: 适配android 6.0及以上运行时权限.
 */
public class PermissionsUtils {


    public static final int CODE_RECIPE_DETAIL_SHARE = 1;
    public static final int CODE_HOME_DEVICE_CAMERA = 2;
    public static final int CODE_THEME_DETAIL_SHARE = 3;
    public static final int CODE_USER_INFO_SHARE = 4;
    public static final int CODE_RECIPE_DETAIL_CAMERA = 5;
    public static final int CODE_DEVICE_WATER_SHARE = 6;
    public static final int CODE_KITCHEN_SHARE_ACTIVE = 7;
    public static final int CODE_KITCHEN_SHARE_VIDEO = 8;
    public static final int CODE_WIFI_SSID = 9;
    public static final int CODE_BLUE_TOOTH = 10;

    public static void checkPermission(Context context, String manifest, int code) {
        String[] permissions = {manifest};
        ActivityCompat.requestPermissions((Activity) context, permissions, code);
    }

}
