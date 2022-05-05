package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.ui.R;


/**
 * Created by sylar on 15/7/21.
 */
public class BasePickerPopupWindow extends AbsPopupWindow implements View.OnClickListener {

    public interface PickListener {

        void onCancel();

        void onConfirm();
    }


    protected TextView txtConfirm, txtCancel;
    protected FrameLayout divMain;
    protected View customView;
    protected PickListener listener;

    public BasePickerPopupWindow(Context cx, View customView) {
        super(cx);

        View view = LayoutInflater.from(cx).inflate(R.layout.abs_view_picker_popoup, null);
        divMain = view.findViewById(R.id.divMain);
        txtConfirm = view.findViewById(R.id.txtConfirm);
        txtCancel = view.findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(this);
        txtConfirm.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.bottom_window_style);

        this.customView = customView;
        divMain.addView(customView);
    }

    @Override
    public void onClick(View v) {
        if (v == txtCancel) {
            onCancel();
        } else if (v == txtConfirm) {
            onConfirm();
        }
    }

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }

    public View getCustomView() {
        return customView;
    }

    protected void onCancel() {
        dismiss();
        if (listener != null) {
            listener.onCancel();
        }
    }

    protected void onConfirm() {
        dismiss();
        if (listener != null) {
            listener.onConfirm();
        }
    }


}
