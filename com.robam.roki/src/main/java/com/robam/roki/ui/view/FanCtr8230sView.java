package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.ui.UIListeners;

/**
 * Created by Administrator on 2017/1/9.
 */

public class FanCtr8230sView extends FrameLayout implements UIListeners.IFanCtrView {
    public FanCtr8230sView(Context context) {
        super(context);
    }

    public FanCtr8230sView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FanCtr8230sView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public void attachFan(IFan fan) {

    }

    @Override
    public void onRefresh() {

    }
}
