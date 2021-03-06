package com.robam.roki.ui.push;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
                }
            }
        }
        Log.d(TAG, "body: 1111");
        WelcomeActivity.start(this);
        finish();
    }


    private Intent toIntent(String id, String theme, Class<?> cls) {
        //??????????????????
        Intent intentClick = null;
        intentClick = new Intent(this, cls);
        intentClick.putExtra("id", id);
        intentClick.putExtra("theme", theme);
        intentClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentClick.addCategory("android.intent.category.LAUNCHER");
        intentClick.setAction("android.intent.action.MAIN");
        return intentClick;
    }
}
