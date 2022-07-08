package com.robam.roki.ui.dialog;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.BaseActivity;
import com.robam.roki.ui.FormKey;


public class CountDownDialog extends BaseActivity {

    static public void start(Activity atv) {
        Intent intent=new Intent(atv, CountDownDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //atv.startActivity(new Intent(atv, CountDownDialog.class));
        atv.startActivity(intent);
    }

    @Override
    protected String createFormKey() {
        return FormKey.CountDownForm;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedState);
        //设置弹框居中显示
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        //获取屏幕宽、高
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        //设置对话框当前的参数值
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = (int) (d.getHeight() * 0.4);   //高度设置为屏幕的1.0
        lp.width = (int) (d.getWidth() * 1.0);    //宽度设置为屏幕的0.8
        window.setAttributes(lp);     //设置生效
    }
}
