package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/17.
 */
public class BlackPromptConfirmDialog extends Dialog {
    private int layout_res;//布局文件
    private View contentView;
    private Context cx;
    public static BlackPromptConfirmDialog dlg;
    @InjectView(R.id.dialog_mic_confirm_btn)
    Button dialog_mic_confirm_btn;

    public BlackPromptConfirmDialog(Context context) {
        super(context);
        init();
    }

    public BlackPromptConfirmDialog(Context context, int res) {
        super(context, R.style.Dialog_Microwave_professtion);
        this.layout_res = res;
        this.cx = context;
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(cx);
        contentView = layoutInflater.inflate(R.layout.dialog_microwave_confirm, null, false);
        if (layout_res != 0) {
            ViewGroup viewGroup = contentView.findViewById(R.id.dialog_mic_confirm_container);
            viewGroup.addView(layoutInflater.inflate(layout_res, null));
        }
        setContentView(contentView);
        ButterKnife.inject(this, contentView);
    }

    @OnClick(R.id.dialog_mic_confirm_btn)
    public void OnClickConfirm() {
        if (listener != null && dlg != null) {
            listener.onConfirm("confirm");
            dlg.dismiss();
        }
    }

    /**
     * 设置按钮文字
     */
    public void setButtonText(String text) {
        dialog_mic_confirm_btn.setText(text);
    }

    public static BlackPromptConfirmDialog show(Context cx, int res) {
        dlg = new BlackPromptConfirmDialog(cx, res);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Window window = dlg.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = displayMetrics.widthPixels;
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.4);
        window.setAttributes(layoutParams);
        return dlg;
    }


    public interface PickListener {
        void onCancel();

        void onConfirm(String confirm);
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }
}
