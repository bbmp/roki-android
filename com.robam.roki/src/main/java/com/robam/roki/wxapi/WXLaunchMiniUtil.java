package com.robam.roki.wxapi;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXLaunchMiniUtil {
    private final Activity mActivity;

    public static final String MINIPTOGRAM_TYPE_RELEASE = "0";

    public String appId;//微信开发者平台APPP的id
    public String userName;// 小程序原始id
    public String path;//拉起小程序页面的可带参路径，不填默认拉起小程序首页
    public String miniprogramType;//0正式版 1开发版 2体验版

    public WXLaunchMiniUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //跳转
    public void sendReq() {
        ContentResolver resolver = mActivity.getContentResolver();
        Uri uri = Uri.parse("content://com.tencent.mm.sdk.comm.provider/launchWXMiniprogram");
        String[] path = new String[]{this.appId, this.userName, this.path, this.miniprogramType, ""};
        Cursor cursor;
        if ((cursor = resolver.query(uri, null, null, path, null)) != null) {
            cursor.close();
        }


    }

}
