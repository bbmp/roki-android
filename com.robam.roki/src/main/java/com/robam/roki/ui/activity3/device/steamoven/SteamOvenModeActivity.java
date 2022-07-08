package com.robam.roki.ui.activity3.device.steamoven;

import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModelNew;
import com.robam.roki.ui.activity3.device.base.DeviceModeSelectActivity;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/28
 *     desc   : 一体机功能选择
 *     version: 1.0
 * </pre>
 */
public class SteamOvenModeActivity extends DeviceModeSelectActivity {
    @Override
    protected boolean isOrder() {

        return  deviceConfigurationFunction.functionCode!= FuncCodeKey.FZMODE;
    }

    @Override
    public void startWork(int code, int steam, int temp, int downUp, int time, long orderTime) {
        toast(code + " : " + steam + ":"+ temp + ":"+ downUp + ":"+ time + ":"+ orderTime + ":");

        if (mDevice instanceof AbsSteameOvenOneNew){
            AbsSteameOvenOneNew mAbsSteameOvenOneNew=(AbsSteameOvenOneNew)mDevice;

            if (code==14){
                mAbsSteameOvenOneNew.setSteameOvenOneRunModeExp((short) code, time, (short) temp,(short) downUp,(int) orderTime, (short) steam, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                     finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }else {
                mAbsSteameOvenOneNew.setSteameOvenOneRunMode((short) code, time, (short) temp, (int) orderTime, (short) steam, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        }
    }
}
