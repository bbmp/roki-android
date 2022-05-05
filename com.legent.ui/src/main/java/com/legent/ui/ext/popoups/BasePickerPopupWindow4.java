package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.ui.R;

/**
 * Created by linxiaobin on 2015/12/27.
 */
public class BasePickerPopupWindow4 extends AbsPopupWindow implements View.OnClickListener {

    public interface PickListener {

        void onCancel();

        void onConfirm();
    }


    protected TextView txtConfirm;
    protected FrameLayout divMain;
    protected View customView;
    protected PickListener listener;

    public BasePickerPopupWindow4(Context cx, View customView) {
        super(cx);
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_view_picker_popoup_oven, null);
        divMain = view.findViewById(R.id.divMain);
        txtConfirm = view.findViewById(R.id.txtConfirm);

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
//        if (v == txtCancel) {
//            onCancel();
//        } else
        if (v == txtConfirm) {
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