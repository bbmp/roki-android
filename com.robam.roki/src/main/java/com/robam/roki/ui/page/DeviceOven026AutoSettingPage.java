package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.Oven026OrderTimeDialog;
import com.robam.roki.ui.dialog.OvenBroken026Dialog;
import com.robam.roki.ui.view.DeviceNumWheel;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.OvenOrderTimeDataUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/13.
 */

public class DeviceOven026AutoSettingPage extends BasePage {
    AbsOven oven;
    LayoutInflater inflater;
    View contentView;
    String guid;
    short model;
    private final int HOUR_SELE = 0;//小时
    private final int MIN_SELE = 1;//分钟

    @InjectView(R.id.oven026_title)
    TextView oven026_title;
    @InjectView(R.id.oven026_autosetting_time)//时间Wheel
            DeviceNumWheel oven026_autosetting_time;
    @InjectView(R.id.oven026_order)//开启预约
            Button oven026_order;
    @InjectView(R.id.oven026_start)//开始运行
            Button oven026_start;
    List<String> stringHourList = new ArrayList<>();
    List<String> stringMinList = new ArrayList<>();
    private IRokiDialog rokiOrderTimeDialog = null;

    Handler mHandel = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HOUR_SELE:
                    setDeviceOrderTime((String) msg.obj);
                    break;
                case MIN_SELE:
                    setDeviceOrderTime((String) msg.obj);
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        model = bd == null ? null : bd.getShort("model");
        oven = Plat.deviceService.lookupChild(guid);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_oven026_autosetting,
                container, false);
        ButterKnife.inject(this, contentView);
        init();
        return contentView;
    }

    void init() {
        if ("RR075".equals(oven.getDt())){
            oven026_order.setVisibility(View.GONE);
        }else{
            oven026_order.setVisibility(View.VISIBLE);
        }
        switch (model) {
            case OvenMode.BEEF:
                init_BEEFData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_niupai));
                break;
            case OvenMode.BREAD:
                init_BREADData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_mianbao));
                break;
            case OvenMode.BISCUITS:
                init_BISCUITSData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_binggan));
                break;
            case OvenMode.CHICKENWINGS:
                init_CHICKENWINGSData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_jichi));
                break;
            case OvenMode.CAKE:
                init_CAKEData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_dangao));
                break;
            case OvenMode.PIZZA:
                init_PIZZAData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_pisa));
                break;
            case OvenMode.GRILLEDSHRIMP:
                init_ROASTFISHData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_xia));
                break;
            case OvenMode.ROASTFISH:
                init_GRILLEDSHRIMPData();
                oven026_title.setText(cx.getString(R.string.device_steam075_name));
                break;
            case OvenMode.SWEETPOTATO:
                init_SWEETPOTATOData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_hongshu));
                break;
            case OvenMode.CORN:
                init_CORNData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_yumi));
                break;
            case OvenMode.STREAKYPORK:
                init_STREAKYPORKData();
                oven026_title.setText(cx.getString(R.string.device_oven_model_wuhuarou));
                break;
            case OvenMode.VEGETABLES:
                init_VEGETABLESData();
                oven026_title.setText(cx.getString(R.string.device_steam_model_shucai));
                break;
            default:
                break;
        }
        oven026_autosetting_time.setUnit(StringConstantsUtil.STRING_MIN);
        oven026_autosetting_time.startx_offset = 50;
    }

    void init_BEEFData() {
        ArrayList<Integer> list = Lists.newArrayList();
        if (IRokiFamily.RR075.equals(oven.getDt())){
            for (int i = 9; i <= 14; i++) {
                list.add(i);
            }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(0);
        }else{
            for (int i = 7; i <= 12; i++) {
                list.add(i);
            }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(0);
        }
    }

    void init_BREADData() {
        ArrayList<Integer> list = Lists.newArrayList();
        if (IRokiFamily.RR075.equals(oven.getDt())){
            for (int i=18; i <=23;i++){
                list.add(i);
            }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(2);
        }else{
            if (IRokiFamily.RR016.equals(oven.getDt())||IRokiFamily.HK906.equals(oven.getDt()))
                for (int i = 13; i <= 20; i++) {
                    list.add(i);
                }
            else
                for (int i = 15; i <= 20; i++) {
                    list.add(i);
                }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(0);
        }

    }

    void init_BISCUITSData() {
        ArrayList<Integer> list = Lists.newArrayList();
        if (IRokiFamily.RR075.equals(oven.getDt())){
            for (int i = 18; i <= 23; i++) {
                list.add(i);
            }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(2);
        }else{
            for (int i = 17; i <= 25; i++) {
                list.add(i);
            }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(0);
        }


    }

    void init_CHICKENWINGSData() {
        ArrayList<Integer> list = Lists.newArrayList();

            if (IRokiFamily.RR016.equals(oven.getDt())||IRokiFamily.HK906.equals(oven.getDt()))
                for (int i = 14; i <= 20; i++) {
                    list.add(i);
                }
            else
                for (int i = 12; i <= 18; i++) {
                    list.add(i);
                }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(0);


    }

    void init_CAKEData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 55; i <= 65; i++) {
            list.add(i);
        }
        oven026_autosetting_time.setData(list);
        oven026_autosetting_time.setDefault(0);
    }

    void init_PIZZAData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 15; i <= 20; i++) {
            list.add(i);
        }
        oven026_autosetting_time.setData(list);
        oven026_autosetting_time.setDefault(0);
    }

    void init_ROASTFISHData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 10; i <= 15; i++) {
            list.add(i);
        }
        oven026_autosetting_time.setData(list);
        oven026_autosetting_time.setDefault(2);
    }

    void init_GRILLEDSHRIMPData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 23; i <= 30; i++) {
            list.add(i);
        }
        oven026_autosetting_time.setData(list);
        oven026_autosetting_time.setDefault(0);
    }

    void init_SWEETPOTATOData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 45; i <= 60; i++) {
            list.add(i);
        }
        oven026_autosetting_time.setData(list);
        oven026_autosetting_time.setDefault(0);
    }

    void init_CORNData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 35; i <= 45; i++) {
            list.add(i);
        }
        oven026_autosetting_time.setData(list);
        oven026_autosetting_time.setDefault(0);
    }

    void init_STREAKYPORKData() {
        ArrayList<Integer> list = Lists.newArrayList();
        if (IRokiFamily.RR075.equals(oven.getDt())){
            for (int i = 20; i <= 23; i++) {
                list.add(i);
            }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(3);
        }else{
            for (int i = 25; i <= 30; i++) {
                list.add(i);
            }
            oven026_autosetting_time.setData(list);
            oven026_autosetting_time.setDefault(0);
        }

    }

    void init_VEGETABLESData() {
        ArrayList<Integer> list = Lists.newArrayList();
        for (int i = 8; i <= 15; i++) {
            list.add(i);
        }
        oven026_autosetting_time.setData(list);
        oven026_autosetting_time.setDefault(0);
    }


    @OnClick(R.id.oven026_return)
    public void OnReturnClick() {
        UIService.getInstance().popBack();
    }

    Oven026OrderTimeDialog orderTimeDialog;

    @OnClick(R.id.oven026_order)
    public void OnOrderClick() {

        rokiOrderTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        rokiOrderTimeDialog.setWheelViewData(OvenOrderTimeDataUtil.getListOrderTimeHourData(), null,
                OvenOrderTimeDataUtil.getListOrderTimeMinData(), false, 12, 0, 30, new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {
                Message message = mHandel.obtainMessage();
                message.obj = contentFront;
                message.what = HOUR_SELE;
                mHandel.sendMessage(message);
            }
        }, null, new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                Message message = mHandel.obtainMessage();
                message.obj = contentRear;
                message.what = MIN_SELE;
                mHandel.sendMessage(message);
            }
        });
        rokiOrderTimeDialog.show();
    }



    //设置预约时间
    private void setDeviceOrderTime(String data) {
        if (data.contains(StringConstantsUtil.STR_HOUR)){
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringHourList.add(removetTempString);
        }
        if (data.contains(StringConstantsUtil.STRING_MINUTES)){
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringMinList.add(removeTimeString);
        }
        rokiOrderTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiOrderTimeDialog.dismiss();
                oven.setOvenAutoRunMode(model, Short.parseShort(String.valueOf(oven026_autosetting_time.getSelectedTag())),
                        (short) 1, Short.parseShort(String.valueOf(stringMinList.get(stringMinList.size()-1))),
                        Short.parseShort(String.valueOf(stringHourList.get(stringHourList.size()-1))), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack();
                        UIService.getInstance().popBack();
                        Bundle bd = new Bundle();
                        bd.putString(PageArgumentKey.Guid, oven.getID());
                        bd.putShort("model", model);
                        bd.putShort("from", (short) 0);
                        UIService.getInstance().postPage(PageKey.DeviceOven026Working, bd);
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show(R.string.device_Throwable_error, Toast.LENGTH_SHORT);
                    }
                });
            }
        });


    }

    @OnClick(R.id.oven026_start)
    public void OnOvenStart() {
        if (!oven.isConnected()) {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
            return;
        }
        if(isInAlarmStatus()){
            ToastUtils.show(getString(R.string.mac_error),Toast.LENGTH_SHORT);
            return;
        }

        oven.setOvenAutoRunMode(model, Short.parseShort(String.valueOf(oven026_autosetting_time.getSelectedTag())), new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, oven.getID());
                bd.putShort("model", model);
                bd.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceOven026Working, bd);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(R.string.device_Throwable_error, Toast.LENGTH_SHORT);
            }
        });
    }

    private boolean isInAlarmStatus() {
        return oven.status == OvenStatus.AlarmStatus;
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceOven026AutoSetting)) {
            return;
        }
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        if (oven.status == OvenStatus.Wait || oven.status == OvenStatus.Off || oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause
                || oven.status == OvenStatus.Order || oven.status == OvenStatus.PreHeat) {
            if (orderTimeDialog != null && orderTimeDialog.isShowing())
                orderTimeDialog.dismiss();
            orderTimeDialog = null;
            UIService.getInstance().popBack();
        }
    }

}
