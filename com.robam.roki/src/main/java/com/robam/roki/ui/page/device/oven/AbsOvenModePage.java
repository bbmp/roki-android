package com.robam.roki.ui.page.device.oven;

import android.os.Handler;
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
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.MobApp;
import com.robam.roki.ui.PageKey;

/**
 * Created by Dell on 2018/7/6.
 * 烤箱 烤模式&& 035
 */

public class AbsOvenModePage extends AbsOvenGridBasePage {


    public AbsOven oven;

    @Override
    public void initView() {
        super.initView();
        oven = Plat.deviceService.lookupChild(guid);
    }



    @Override
    public void onResume() {
        super.onResume();
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
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom(cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.show("指令下发失败了哟,请重新下发", Toast.LENGTH_SHORT);
                }
            });
        } else {
            sendCom(cmd, mode, setTime, setTemp);
        }
    }

    @Override
    public void send1(final int cmd, final String mode, final int setTime, final int setTemp) {

        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom1(cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.show("指令下发失败了哟,请重新下发", Toast.LENGTH_SHORT);
                }
            });
        } else {
            sendCom1(cmd, mode, setTime, setTemp);
        }
    }


    @Override
    public void sendCom1(final int cmd, final String mode, final int setTime, final int setTemp) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                oven.setOvenBakeDIYMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, barbecue, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul(clickPos);
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

    private void sendCom(final int cmd, final String mode, final int setTime, final int setTemp) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                oven.setOvenModelRunMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul(clickPos);
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
