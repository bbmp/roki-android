package com.robam.roki.ui.page.device.fan;

import android.view.MotionEvent;
import android.view.View;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.fan.Fan5910;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;

/**
 * Created by rent on 2016/10/7.
 */
public class DeviceFan5910Page extends DeviceFanRevisionPage<Fan5910> implements View.OnTouchListener {

    private IRokiDialog mCloseDialog;

    /**
     * 烟机状态轮训事件
     */
    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        super.onEvent(event);
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID()))
            return;
        inspectCleanLock();
    }

    @Override
    protected void initData() {
        super.initData();
        mIvLockBg.setOnTouchListener(this);
    }

    @Override
    public void onMIvBackClicked() {
        super.onMIvBackClicked();
    }

    void inspectCleanLock() {
        if (fan.status == FanStatus.CleanLock) {
            mIvLockBg.setVisibility(View.VISIBLE);
            mOilcleanLockBg.setVisibility(View.VISIBLE);
        } else {
            mIvLockBg.setVisibility(View.GONE);
            mOilcleanLockBg.setVisibility(View.GONE);
        }

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
