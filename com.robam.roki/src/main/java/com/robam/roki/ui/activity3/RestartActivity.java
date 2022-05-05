package com.robam.roki.ui.activity3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.WelcomeActivity;


/**

 *    time   : 2020/11/29
 *    desc   : 重启应用
 */
public final class RestartActivity extends AppActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, RestartActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        restart(this);
        finish();
        ToastUtils.show("应用出了点小意外，正在重新启动");
    }

    public static void restart(Context context) {
        Intent intent;
//        if (true) {
//            // 如果是未登录的情况下跳转到闪屏页
            intent = new Intent(context, WelcomeActivity.class);
//        } else {
//            // 如果是已登录的情况下跳转到首页
//            intent = new Intent(context, MainActivity.class);
//        }

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}