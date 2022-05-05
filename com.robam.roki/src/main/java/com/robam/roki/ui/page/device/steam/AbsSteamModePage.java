package com.robam.roki.ui.page.device.steam;

import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.oven.AbsOvenGridBasePage;
import com.robam.roki.utils.ToolUtils;

/**
 * 蒸箱 蒸模式
 */

public class AbsSteamModePage extends AbsOvenGridBasePage {

    public AbsSteamoven steam;
    String dt, dc;

    @Override
    public void initView() {
        super.initView();
        steam = Plat.deviceService.lookupChild(guid);
        if (steam != null) {
            dt = steam.getDt();
            dc = steam.getDc();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (steam == null) {
            return;
        }

    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.AbsSteamMode)) {
            return;
        }
        if (steam == null || !Objects.equal(steam.getID(), event.pojo.getID()))
            return;
        this.steam = event.pojo;
        if (steam.status == SteamStatus.Working || steam.status == SteamStatus.Pause
                || steam.status == SteamStatus.Order || steam.status == SteamStatus.PreHeat) {
            if (absOvenModeSettingDialog != null && absOvenModeSettingDialog.isShowing()) {
                absOvenModeSettingDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }
    }

    @Override
    public void send(final int cmd, final String mode, final int setTime, final int setTemp) {
        if (steam != null) {
            ToolUtils.logEvent(steam.getDt(), "开始蒸箱模式温度时间工作:" + mode + ":" + setTemp + ":" + setTime, "roki_设备");
        }
        if (steam.doorState == 0) {
            ToastUtils.show("门未关好，请先关好箱门", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.waterboxstate == 0) {
            ToastUtils.show("水箱已弹出，请确保水箱已放好", Toast.LENGTH_SHORT);
            return;
        }

        if (steam.status == SteamStatus.AlarmStatus) {
            ToastUtils.show("请先解除报警", Toast.LENGTH_SHORT);
            return;
        }

        steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
            @Override
            public void onSuccess() {

                sendCom(cmd, mode, setTime, setTemp);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void sendCom(int cmd, String mode, int setTime, int setTemp) {
        steam.setSteamCookModule((short) cmd,
                Short.decode(mode), (short) setTemp, (short) setTime,
                (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul(clickPos);
                        UIService.getInstance().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }


    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, guid, code, dc, new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
                LogUtils.i("20200702", "getReportResponse::" + getReportResponse.msg);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
