package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaiyuanyi on 15/12/28.
 */
public class StoveSwitchView extends FrameLayout {
    @InjectView(R.id.switchView)
    View switchView;

    @InjectView(R.id.imgSwitch)
    ImageView imgSwitch;

    @InjectView(R.id.txtSwitch)
    TextView txtSwitch;

    Context cx;

    public StoveSwitchView(Context context) {
        super(context);
        init(context, null);
    }

    public StoveSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StoveSwitchView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        this.cx = cx;
        View view = LayoutInflater.from(cx).inflate(
                R.layout.view_device_switch, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void setStatus(boolean powerStatus) {

        imgSwitch.setSelected(powerStatus);
        switchView.setSelected(powerStatus);
        txtSwitch.setSelected(powerStatus);

        txtSwitch.setText("辅助关火");//(powerStatus ? "已开启" : "已关闭");
    }
}
