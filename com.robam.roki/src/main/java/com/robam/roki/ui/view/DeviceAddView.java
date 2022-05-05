package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.legent.ui.UIService;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceAddView extends FrameLayout {

    public DeviceAddView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceAddView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_add,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    @OnClick(R.id.layout)
    public void onClick() {
        onAddDevice();
    }


    void onAddDevice() {
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            UIService.getInstance().postPage(PageKey.DeviceAddByEasylink);
        }
    }

}
