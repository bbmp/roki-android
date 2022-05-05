package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/17.
 */
public class BlackPromptConfirmAndCancleDialog extends Dialog {
    protected int layout_res;//布局文件
    protected View contentView;
    protected Context cx;
    public static BlackPromptConfirmAndCancleDialog dlg;

    @InjectView(R.id.dialog_confirmcancle_confirm_container)
    RelativeLayout dialog_confirmcancle_confirm_container;


    public BlackPromptConfirmAndCancleDialog(Context context) {
        super(context);
        init();
    }

    public BlackPromptConfirmAndCancleDialog(Context context, PickListener listener, int res) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.layout_res = res;
        this.listener = listener;
        this.cx = context;
        init();
    }

    protected void init() {
        if (layout_res == 0) return;
        LayoutInflater layoutInflater = LayoutInflater.from(cx);
        contentView = layoutInflater
                .inflate(R.layout.dialog_prompt_confirmandcancel, null, false);
        ViewGroup viewGroup = contentView.findViewById(R.id.dialog_confirmcancle_confirm_container);
        viewGroup.addView(layoutInflater.inflate(layout_res, null));
        setContentView(contentView);
        ButterKnife.inject(this, contentView);
    }

    @OnClick(R.id.dialog_confirmcancle_confirm_btn)
    public void OnConfirmClick() {
        if (listener != null) {
            listener.onConfirm(0, null);
        }
    }

    @OnClick(R.id.dialog_confirmcancle_cancle_btn)
    public void OnCancelClick() {
        if (this != null && this.isShowing()) {
            this.dismiss();
        }
    }

    public static BlackPromptConfirmAndCancleDialog show(Context cx, PickListener listener, int res) {
        dlg = new BlackPromptConfirmAndCancleDialog(cx, listener, res);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.3);
        window.setAttributes(layoutParams);
        return dlg;
    }


    public interface PickListener {
        void onCancel();

        void onConfirm(int what, Object m);
    }

    protected PickListener listener;

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }
}
