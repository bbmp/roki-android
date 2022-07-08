package com.robam.roki.ui.push;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.legent.utils.EventUtils;
import com.robam.common.events.WebUrlEvent;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.WelcomeActivity;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

public class MfrMessageActivity extends UmengNotifyClickActivity {
    private static final String TAG = "MfrMessageActivity";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.push_layout);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.d(TAG, "bundle: " + bundle);
        }
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.d(TAG, "body: " + body);

        if (!TextUtils.isEmpty(body)) {
            Extra extra = new Gson().fromJson(body, Extra.class);
//            JSONObject jsonObject = JSONObject.parseObject(body);
            Log.d(TAG, "body: 22222");
            if (extra.extra != null) {
                Log.d(TAG, "body: 11111111");

                if (extra.extra.type != null && extra.extra.type.equals("theme")) {
                    if (TextUtils.isEmpty(extra.extra.id)){
                        MobApp.id=null;
                    }else {
                        MobApp.id = extra.extra.id;
                    }
                    WelcomeActivity.start(this);
                    Log.d(TAG, "body: 1111");
                    finish();
                    return;
                }else if (extra.extra.type != null && extra.extra.type.equals("game")){
                    MobApp.Game="game";
                }else if (extra.extra.type != null && extra.extra.type.equals("cook")){
                    if (TextUtils.isEmpty(extra.extra.id)){
                        MobApp.cookId=null;
                    }else {
                        MobApp.cookId = extra.extra.id;
                    }
                    WelcomeActivity.start(this);
                    Log.d(TAG, "body: 1111");
                    finish();
                    return;
                }else if (extra.extra.type != null && extra.extra.type.equals("h5")){
                    if (TextUtils.isEmpty(extra.extra.url)){
                        MobApp.h5Url=null;
                    }else {
                        MobApp.h5Url = extra.extra.url;
                        MobApp.secondTitle = extra.extra.secondTitle;
                        MobApp.img = extra.body.img;
                        MobApp.title = extra.body.title;
                    }
                    WelcomeActivity.start(this);
//                    EventUtils.postEvent(new WebUrlEvent(extra.extra.url,extra.extra.secondTitle,extra.extra.img, extra.extra.title));
                    Log.d(TAG, "body: 1111");
                    finish();
                    return;
                }
            }
        }
        Log.d(TAG, "body: 1111");
        WelcomeActivity.start(this);
        finish();
    }


}
