package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.plat.pojos.dictionary.DeviceType;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeviceUnlistedItemNoView extends FrameLayout {

    @InjectView(R.id.imgDevice)
    ImageView imgDevice;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;

    public DeviceUnlistedItemNoView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceUnlistedItemNoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceUnlistedItemNoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_unlisted_item_before,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);

            if (attrs != null) {
                TypedArray ta = cx.obtainStyledAttributes(attrs,
                        R.styleable.HomeUnlistedDeviceItem);
                String title = ta.getString(R.styleable.HomeUnlistedDeviceItem_title);
                String desc = ta.getString(R.styleable.HomeUnlistedDeviceItem_description);
                int imgResid = ta.getResourceId(R.styleable.HomeUnlistedDeviceItem_imgSource, 0);
                ta.recycle();

                txtTitle.setText(title);
                txtDesc.setText(desc);
                imgDevice.setImageResource(imgResid);
            }
        }
    }

    public void loadData(DeviceType devType) {
        if (devType == null) return;
        txtTitle.setText(devType.getName());
        if (devType.tag != null) {
            txtDesc.setText(devType.tag);
        }
    }
}
