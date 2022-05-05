package com.robam.roki.ui.page.device.oven;

import android.os.Handler;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.ui.PageKey;

/**
 * Created by Administrator on 2019/8/26.
 */

public class AbsZoningModePage extends AbsOvenZoningBasePage {
    public AbsOven oven;

    @Override
    public void initView() {
        super.initView();
        oven = Plat.deviceService.lookupChild(guid);
    }


    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.AbsOvenMode)) {
            return;
        }
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        if (oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause
                || oven.status == OvenStatus.Order || oven.status == OvenStatus.PreHeat) {
            if (absOvenModeSettingDialog != null && absOvenModeSettingDialog.isShowing()) {
                absOvenModeSettingDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }
    }


    public void send(final int cmd, final String mode, final int setTime, final int setTemp) {
        LogUtils.i("20180710", "cmd:" + cmd + "mode:" + mode + "setTime::" + setTime + " setTemp::" + setTemp);

        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom(cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            sendCom(cmd, mode, setTime, setTemp);
        }
    }

    private void sendCom(final int cmd, final String mode, final int setTime, final int setTemp) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task

                oven.setOvenModelRunMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("指令下发失败了哟,请重新下发", Toast.LENGTH_SHORT);
                    }
                });
            }
        }, 500);
    }
}
