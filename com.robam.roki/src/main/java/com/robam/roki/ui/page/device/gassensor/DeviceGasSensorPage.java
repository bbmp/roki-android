package com.robam.roki.ui.page.device.gassensor;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.GasStatusChangedEvent;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

/**
 * Created by Dell on 2018/5/11.
 */

public class DeviceGasSensorPage extends AbsDeviceSensorView {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (DeviceGasSensorPage.this.isAdded()){
                        upDateBgImg(getResources().getDrawable(R.mipmap.ic_gas_run_up),getResources().getDrawable(R.mipmap.ic_gas_run_down));
                        upDateStatus("正常",String.valueOf(gasSensor.gasCon));
                        gasCon.setVisibility(View.VISIBLE);
                        gasAlarm.setVisibility(View.GONE);
                        gasItemDisplayView.setVisibility(View.VISIBLE);
                    }
                    break;
                case 1:
                    if (DeviceGasSensorPage.this.isAdded()){
                        upDateBgImg(getResources().getDrawable(R.mipmap.ic_gas_run_alarm_up),getResources().getDrawable(R.mipmap.ic_gas_run_alarm_down));
                        upDateStatus("报警",String.valueOf(gasSensor.gasCon));
                        gasCon.setVisibility(View.GONE);
                        gasItemDisplayView.setVisibility(View.GONE);
                        upDateBottomView("智能烟机已自动开启，\n降低泄漏煤气浓度。","如何处理？");
                    }

                    break;
                case 3:
                    if (DeviceGasSensorPage.this.isAdded()){
                        upDateBgImg(getResources().getDrawable(R.mipmap.ic_gas_fault_up),getResources().getDrawable(R.mipmap.ic_gas_fault_down));
                        upDateStatus("故障","--");
                        gasCon.setVisibility(View.GONE);
                        gasItemDisplayView.setVisibility(View.GONE);
                        upDateBottomView("可申请售后服务","拨打售后");
                    }

                    break;
                default:
                    break;
            }
        }
    };


    private void upDateBottomView(String str1,String str2){
        gasAlarm.setVisibility(View.VISIBLE);
        gasAlarm.upDate(str1, str2);
    }


    private void upDateBgImg(Drawable imgup, Drawable impDown){
        if (isStartAnimation){
            stopAnimation();
        }
        ivRunUp.setImageDrawable(imgup);
        ivRunDown.setImageDrawable(impDown);
        startAnimation();
    }

    private void upDateStatus(String str,String ppm){
        tvWorkStateName.setText(str);
        tvWorkDec.setText("PPM值："+ppm);
    }

    @Override
    protected void initView() {
        super.initView();
        ivBg.setImageResource(R.mipmap.ic_gassensor_bg);
        tvDeviceModelName.setText(deviceName);
        if (!gasSensor.isConnected()){
            txtDisConnect.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onEvent(GasStatusChangedEvent event){
        LogUtils.i("20180531","ev-status::"+event.pojo.status);
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceGasSensor)) {
            return;
        }
        if (gasSensor == null || !Objects.equal(gasSensor.getID(), event.pojo.getID()))
            return;
        switch (event.pojo.status){
            case 0://正常
                updateUi(0);
                break;
            case 1://泄漏报警
                updateUi(1);
                break;
            case 3://燃气传感器故障
                updateUi(3);
                break;
            default:
                break;
        }
    }

    private void updateUi(int num){
        Message msg =Message.obtain();
        msg.what = num;
        handler.sendMessage(msg);
    }


}
