package com.robam.roki.ui.page.device.fan;

import android.app.Dialog;
import android.view.MotionEvent;
import android.view.View;

import com.google.common.eventbus.Subscribe;
import com.robam.common.events.FanCleanLockEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.Fan5610;
import com.robam.common.util.FanLock5610Utils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;


/**
 * Created by Administrator on 2016/9/28.
 */

public class DeviceFan5610Page extends DeviceFanRevisionPage<Fan5610> implements View.OnTouchListener {

    private Dialog fanPlateRemoveDg;
    private IRokiDialog mCloseDialog;

    @Subscribe
    public void onEvent(FanCleanLockEvent event) {
        if (IRokiFamily.R5610.equals(event.fan.getDt())){
            FanLock5610Utils.setLock(event.flag_CleanLock);
            onFanCleanLockEvent(event.fan, event.flag_CleanLock);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mIvLockBg.setOnTouchListener(this);
        onFanCleanLockEvent(fan, FanLock5610Utils.getLock());
    }

    private void onFanCleanLockEvent(AbsFan fan, short flag) {
        if (flag == 1) {
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
