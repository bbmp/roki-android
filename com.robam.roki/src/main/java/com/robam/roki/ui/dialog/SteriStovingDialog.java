package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Gu on 2016/3/1.
 */
public class SteriStovingDialog extends Dialog {

    UIListeners.SteriStoveCallback callback;
    @InjectView(R.id.commit)
    TextView commit;

    @InjectView(R.id.rl_eighty)
    RelativeLayout rl_eighty;
    @InjectView(R.id.rl_sixty)
    RelativeLayout rl_sixty;
    @InjectView(R.id.rl_ten)
    RelativeLayout rl_ten;
    @InjectView(R.id.rl_fifteen)
    RelativeLayout rl_fifteen;
    @InjectView(R.id.rl_twenty)
    RelativeLayout rl_twenty;
    private int miniutes;


    public static void show(Context context, UIListeners.SteriStoveCallback callback) {
        SteriStovingDialog dialog = new SteriStovingDialog(context, callback);
        dialog.show();
    }

    public SteriStovingDialog(Context context, UIListeners.SteriStoveCallback callback) {
        super(context);
        this.callback = callback;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadView(context);
    }

    private void loadView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_steri_stoving, null);
        initView(view, context);
    }

    private void initView(View view, Context context) {
        setContentView(view);
        ButterKnife.inject(this, view);
        initDialog(context);
        initData();
    }

    private void initData() {
        miniutes=10;
    }

    private void initDialog(Context context) {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //将对话框的大小按屏幕大小的百分比设置
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.4
        p.width = d.getWidth(); // 宽度设置为屏幕的宽度
        dialogWindow.setAttributes(p);
    }

    @OnClick(R.id.rl_eighty)
    public void OnClickEighty() {
        ChangeColor(rl_eighty, true);
        ChangeColor(rl_twenty, false);
        ChangeColor(rl_fifteen, false);
        ChangeColor(rl_ten, false);
        ChangeColor(rl_sixty, false);
        miniutes=80;
    }

    @OnClick(R.id.rl_sixty)
    public void OnClickSixty() {
        ChangeColor(rl_sixty, true);
        ChangeColor(rl_twenty, false);
        ChangeColor(rl_fifteen, false);
        ChangeColor(rl_ten, false);
        ChangeColor(rl_eighty, false);
        miniutes=60;
    }

    @OnClick(R.id.rl_ten)
    public void OnClickTen() {
        ChangeColor(rl_ten, true);
        ChangeColor(rl_twenty, false);
        ChangeColor(rl_fifteen, false);
        ChangeColor(rl_sixty, false);
        ChangeColor(rl_eighty, false);
        miniutes=10;

    }

    @OnClick(R.id.rl_fifteen)
    public void OnClickFifteen() {
        ChangeColor(rl_fifteen, true);
        ChangeColor(rl_twenty, false);
        ChangeColor(rl_ten, false);
        ChangeColor(rl_sixty, false);
        ChangeColor(rl_eighty, false);
        miniutes=15;
    }

    @OnClick(R.id.rl_twenty)
    public void OnClickTwenty() {
        ChangeColor(rl_twenty, true);
        ChangeColor(rl_fifteen, false);
        ChangeColor(rl_ten, false);
        ChangeColor(rl_sixty, false);
        ChangeColor(rl_eighty, false);
        miniutes=20;
    }

    @OnClick(R.id.commit)
    public void onClickCommit() {
        /*if (!rlSix.isSelected() && !rlEight.isSelected())
            return;
        if (rlSix.isSelected()) {
            callback.callBack(60);
        }
        if (rlEight.isSelected()) {
            callback.callBack(80);
        }*/
        if(miniutes!=0)
            callback.callBack(miniutes);
        this.dismiss();
    }

    private void ChangeColor(ViewGroup vg, boolean black) {
        if (vg.getChildCount() <= 0) return;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View view = vg.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (black) {
                    textView.setTextColor(Color.parseColor("#1f1f1f"));
                } else {
                    textView.setTextColor(Color.parseColor("#969696"));
                }
            } else if (view instanceof ImageView) {
                ImageView img = (ImageView) view;
                if (black) {
                    img.setVisibility(View.VISIBLE);
                } else {
                    img.setVisibility(View.GONE);
                }
            }
        }
    }
}
