package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Gu on 2016/2/26.
 */
public class SteriStopWorkingDialog extends Dialog {
    String str;
    UIListeners.StopworkCallback callback;
    @InjectView(R.id.tv_stop_work)
    TextView tvStop;

    @OnClick(R.id.btn_stop_work)
    public void onClickStop(){
        callback.callBack();
        this.dismiss();
    }

    public static void show(Context context, String str, UIListeners.StopworkCallback callback) {
        SteriStopWorkingDialog dialog = new SteriStopWorkingDialog(context, str, callback);
        dialog.show();
    }

    public SteriStopWorkingDialog(Context context, String str, UIListeners.StopworkCallback callback) {
        super(context);
        this.str = str;
        this.callback = callback;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadView(context);
    }

    private void loadView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_steri_stop_work, null, false);
        initView(view, context);
    }

    private void initView(View view, Context context) {
        setContentView(view);
        Window dialogWindow = getWindow();
        //将对话框的大小按屏幕大小的百分比设置
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = d.getWidth(); // 宽度设置为屏幕的0.65
        p.alpha=0.7f;
        dialogWindow.setAttributes(p);
        ButterKnife.inject(this, view);
        tvStop.setText(str);
    }
}
