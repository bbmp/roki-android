package com.legent.ui.ext.popoups;

import android.content.Context;
import android.view.View;

/**
 * 用于微波炉 参数调节
 * Created by Rent on 2016/6/13.
 */
public class BasePickerPopupWindow5 extends AbsPopupWindow {
    public interface PickListener {

        void onCancel();

        void onConfirm();
    }

    Context cx;

    public BasePickerPopupWindow5(Context cx, View customView) {
        super(cx);
        this.cx=cx;
       // init();
    }

    /*private void init() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_view_picker_popoup2, null);
        divMain = (FrameLayout) view.findViewById(R.id.divMain);
        warning = (ImageView) view.findViewById(R.id.icon);
        txtConfirm = (TextView) view.findViewById(R.id.txtConfirm);
        txtConfirm.setOnClickListener(this);

        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.bottom_window_style);

        this.customView = customView;
        divMain.addView(customView);
    }*/
}
