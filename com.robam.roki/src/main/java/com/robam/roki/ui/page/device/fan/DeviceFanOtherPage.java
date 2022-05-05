package com.robam.roki.ui.page.device.fan;

import android.view.MotionEvent;
import android.view.View;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.utils.LogUtils;
import com.robam.common.events.FanCleanLockEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.device.fan.OtherFan;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;

/**
 * Created by Rent on 2016/6/28.
 */
public class DeviceFanOtherPage extends DeviceFanRevisionPage<OtherFan> implements View.OnTouchListener {

    private IRokiDialog mCloseDialog;



    @Override
    protected void initData() {
        super.initData();
        mIvLockBg.setOnTouchListener(this);
    }



    @Override
    public void onMOilcleanLockBgClicked() {
        super.onMOilcleanLockBgClicked();
        mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_16);
        mCloseDialog.setTitleText(R.string.device_oil_clean_lock_text);
        mCloseDialog.setContentText(R.string.device_oil_clean_lock_desc);
        mCloseDialog.show();
        mCloseDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}