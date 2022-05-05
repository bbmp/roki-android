package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Gu on 2016/2/29.
 */
public class SteriWarnDialog extends Dialog {
    UIListeners.StopworkCallback callBack;

    @InjectView(R.id.img)
    ImageView img;
    @InjectView(R.id.tv_title)
    TextView txtTitle;
    @InjectView(R.id.tv_content)
    TextView txtContent;
    @InjectView(R.id.tv_sale)
    TextView txtSaleService;

    @OnClick(R.id.tv_sale)
    public void OnClickSaleService() {
        callBack.callBack();
        this.dismiss();
    }

    public static void show(Context context, String title, String content, int resId, UIListeners.StopworkCallback callBack) {
        SteriWarnDialog dialog = new SteriWarnDialog(context, title, content, resId, callBack);
        dialog.show();
    }

    public static void show(Context context, String title, int resId) {
        SteriWarnDialog dialog = new SteriWarnDialog(context, title, resId);
        dialog.show();
    }

    public SteriWarnDialog(Context context, String title, String content, int resId, UIListeners.StopworkCallback callBack) {
        super(context);
        this.callBack = callBack;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadView(context, title, content, resId);
    }

    public SteriWarnDialog(Context context, String title, int resId) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadView(context, title, null, resId);
    }

    private void loadView(Context context, String title, String content, int resId) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_steri_warn, null);
        initView(view, context, title, content, resId);
    }

    private void initView(View view, Context context, String title, String content, int resId) {
        setContentView(view);
        ButterKnife.inject(this, view);
        initData(context, title, content, resId);
    }

    private void initData(Context context, String title, String content, int resId) {
        txtTitle.setText(title);
        if (content == null) {
            txtContent.setVisibility(View.GONE);
            txtSaleService.setVisibility(View.GONE);
        } else {
            txtContent.setVisibility(View.VISIBLE);
            txtSaleService.setVisibility(View.VISIBLE);
            txtContent.setText(content);
        }
        img.setImageResource(resId);

        Window dialogWindow = getWindow();
        //将对话框的大小按屏幕大小的百分比设置
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.4
        p.width = d.getWidth(); // 宽度设置为屏幕的宽度
        p.alpha=0.7f;
        dialogWindow.setAttributes(p);
    }
}
