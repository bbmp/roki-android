package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robam.common.Utils;
import com.robam.roki.R;
import com.robam.roki.model.bean.RikaFunctionParams;
import com.robam.roki.ui.page.device.oven.MyGridView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeviceRikaModeSelectLayout extends LinearLayout {
    @InjectView(R.id.tv_mode)
    public TextView mTvMode;

    @InjectView(R.id.mgv_mode)
    public MyGridView myGridView;

    private Context cx;
    private RikaFunctionParams params;

    public DeviceRikaModeSelectLayout(Context context, RikaFunctionParams params) {
        super(context);
        this.params = params;
        this.cx = context;
        init(cx);
    }

    void init(Context cx) {
        View view = LayoutInflater.from(cx).inflate(R.layout.item_mode_layout,
                this, true);

        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }
}
