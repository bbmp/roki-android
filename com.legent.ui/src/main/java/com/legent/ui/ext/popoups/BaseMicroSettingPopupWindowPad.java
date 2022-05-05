package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.ui.R;

/**
 * Created by Rosicky on 16/2/27.
 */
public class BaseMicroSettingPopupWindowPad extends AbsPopupWindow implements View.OnClickListener {

    public interface PickListener {

        void onCancel();

        void onConfirm();
    }

    FrameLayout divMain;
    TextView txtConfirm, txtCancel, txtSetting;
    View customView;
    protected PickListener listener;

    public BaseMicroSettingPopupWindowPad(Context cx, String setTitle, String setConfirm, String setCancel, View customView) {
        super(cx);
        View view = LayoutInflater.from(cx).inflate(R.layout.view_icro_setting_popoup_pad, null);
        divMain = view.findViewById(R.id.divMain);
        txtSetting = view.findViewById(R.id.txtSetting);
        txtCancel = view.findViewById(R.id.txtCancel);
        txtConfirm = view.findViewById(R.id.txtConfirm);
        txtSetting.setText(setTitle);
        txtConfirm.setText(setConfirm);
        txtCancel.setText(setCancel);

        txtConfirm.setOnClickListener(this);
        txtCancel.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth((int)cx.getResources().getDimension(R.dimen.common_popWindown_width));
        this.setHeight((int)cx.getResources().getDimension(R.dimen.common_microwava_popWindown_height));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.bottom_window_style);

        this.customView = customView;
        divMain.addView(customView);
    }

    @Override
    public void onClick(View view) {
        if (view == txtConfirm) {
            dismiss();
            if (listener != null) {
                listener.onConfirm();
            }
        } else if (view == txtCancel) {
            dismiss();
        }
    }

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        txtSetting.setText(title);
    }

    public void setTxtConfirm(String title) {
        txtConfirm.setText(title);
    }

    public void setTxtCancel(String title) {
        txtCancel.setText(title);
    }

    public void setTxtCancelColor(int color) {
        if (color != 0) {
            txtCancel.setTextColor(color);
        }
    }

    public void setTxtCancelSize(int size) {
        if (size != 0) {
            txtCancel.setTextSize(size);
        }
    }

}
