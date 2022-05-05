package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.R;

/**
 * Created by linxiaobin on 2016/1/18.
 */
public class BasePickerPopupWindowMicroTwoPickerPad extends AbsPopupWindow implements View.OnClickListener {

    public interface PickListener {

        void onCancel();

        void onConfirm();
    }

    FrameLayout divMain;
    ImageView warning;
    TextView txtConfirm, txtCancel,txtProMode,txtDesc;
    View customView;
    protected PickListener listener;

    public BasePickerPopupWindowMicroTwoPickerPad(Context cx, View customView) {
        super(cx);

        View view = LayoutInflater.from(cx).inflate(R.layout.abs_view_micro_picker_popoup_pad, null);
        divMain = view.findViewById(R.id.divMain);
        txtConfirm = view.findViewById(R.id.txtConfirm);
        txtProMode = view.findViewById(R.id.txtProMode);
        txtCancel = view.findViewById(R.id.txtCancel);
        txtDesc = view.findViewById(R.id.txtDesc);
        txtConfirm.setOnClickListener(this);
        txtCancel.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth((int)cx.getResources().getDimension(R.dimen.common_popWindown_width));
        this.setHeight((int)cx.getResources().getDimension(R.dimen.common_popWindown_height));
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
            if (listener != null) {
                listener.onCancel();
            }
        }
    }

    public void setPickListener(PickListener listener) {
        this.listener = listener;
    }

    public void setTxtDesc(String desc) {
        txtDesc.setText(desc);
    }
    public void setTxtProMode(String title) {
        txtProMode.setText(title);
    }
}

