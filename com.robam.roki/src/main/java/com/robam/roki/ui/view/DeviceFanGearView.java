package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeviceFanGearView extends FrameLayout {

    @InjectView(R.id.txtGear)
    TextView txtGear;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    @InjectView(R.id.layout)
    RelativeLayout layout;
    @InjectView(R.id.iv)
    public ImageView img;

    public DeviceFanGearView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceFanGearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceFanGearView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_fan_gear,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);

            if (attrs != null) {
                TypedArray ta = cx.obtainStyledAttributes(attrs,
                        R.styleable.DeviceFanGear);
                String title = ta.getString(R.styleable.DeviceFanGear_title);
                String desc = ta.getString(R.styleable.DeviceFanGear_description);
                ta.recycle();

                txtGear.setText(title);
                txtDesc.setText(desc);
            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        txtGear.setSelected(selected);
        txtDesc.setSelected(selected);
    }

    public void setTitle(String title, String description) {
        txtGear.setText(title);
        txtDesc.setText(description);
    }

    public ImageView getImg() {
        return img;
    }
}
