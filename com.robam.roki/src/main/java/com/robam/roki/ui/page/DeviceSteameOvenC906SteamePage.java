package com.robam.roki.ui.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.robam.common.events.SteamOvenOneAlarmEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;
import com.robam.roki.ui.view.DeviceSteameC906ModeWheel;
import com.robam.roki.ui.view.TemlWheelView;
import com.robam.roki.ui.view.TempC906WheelView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19.
 */

public class DeviceSteameOvenC906SteamePage extends BasePage {

    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.iv_water)
    ImageView ivWater;
    @InjectView(R.id.ll_wheelView)
    LinearLayout llWheelView;
    @InjectView(R.id.tv_mode_name)
    TextView tvModeName;
    @InjectView(R.id.tv_mode_dec)
    TextView tvModeDec;
    @InjectView(R.id.btn_start)
    Button btnStart;
    @InjectView(R.id.wv1_pattern_steame)
    DeviceSteameC906ModeWheel wv1PatternSteame;
    @InjectView(R.id.wv2_steame_temp)
    TempC906WheelView wv2SteameTemp;
    @InjectView(R.id.wv3_steame_time)
    TempC906WheelView wv3SteameTime;
    private AbsSteameOvenOne steameOvenC906;
    private short time;
    private short temp;


    private Map<String, Short> modeKingMap = new HashMap<String, Short>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                tvModeName.setText(wv1PatternSteame.getSelectedText());
                if (wv1PatternSteame.getSelectedText().equals(cx.getString
                        (R.string.device_steamOvenOne_name_faXiao)))
                    tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_faXiao));
                else if (wv1PatternSteame.getSelectedText().equals(cx.getString
                        (R.string.device_steamOvenOne_name_xianNenZheng)))
                    tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_xianNenZheng));
                else if (wv1PatternSteame.getSelectedText().equals(cx.getString
                        (R.string.device_steamOvenOne_name_yingYangZheng)))
                    tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_yingYangZheng));
                else if (wv1PatternSteame.getSelectedText().equals(cx.getString
                        (R.string.device_steamOvenOne_name_gaoWenZhen)))
                    tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_gaoWenZhen));
            }
        }
    };

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {

        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.pojo.getID()))
            return;
        if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus ||
                steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause || steameOvenC906.powerOnStatus
                == SteamOvenOnePowerOnStatus.Order || steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off){
            UIService.getInstance().popBack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_device_steame_oven_c906_steame, null, false);
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
        ButterKnife.inject(this, view);
        wv1PatternSteame.setOnSelectListener(modeLitener);
        wv1PatternSteame.setDefault(1);
        initData();
        tvModeName.setText(wv1PatternSteame.getSelectedText());
        return view;
    }


    private void initData() {
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_faXiao), (short) 0);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_xianNenZheng), (short) 1);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_yingYangZheng), (short) 2);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_gaoWenZhen), (short) 3);

    }

    DeviceSteameC906ModeWheel.OnSelectListener modeLitener = new DeviceSteameC906ModeWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {

            List<?> listModeTemperature = getListModeTemperature(item);
            List<?> listModeTime = getListModeTime(item);
            wv2SteameTemp.setData(listModeTemperature);
            wv3SteameTime.setData(listModeTime);
            int defTemp = 0, defTime = 0;
            if (index == 0) {//发酵
                defTemp = 3;
                defTime = 59;
            } else if (index == 1) {//鲜嫩蒸
                defTemp = 0;
                defTime = 19;
            } else if (index == 2) {//营养蒸
                defTemp = 4;
                defTime = 19;
            } else if (index == 3) {//高温蒸
                defTemp = 14;
                defTime = 19;

            }
            handler.sendEmptyMessage(3);
            wv2SteameTemp.setDefault(defTemp);
            wv3SteameTime.setDefault(defTime);
        }

        @Override
        public void selecting(int index, Object item) {

        }
    };


    /*设置各种模式温度范围*/
    protected List<?> getListModeTemperature(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;
        if (model.equals(cx.getString(R.string.device_steamOvenOne_name_yingYangZheng))) {
            for (int i = 96; i <= 105; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_xianNenZheng))) {
            for (int i = 90; i <= 95; i++) {
                list.add(i);
            }
        }else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_gaoWenZhen))){
            for (int i = 106; i <= 150; i++) {
                list.add(i);
            }
        }else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_faXiao))){
            for (int i = 35; i <= 60; i++) {
                list.add(i);
            }
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getListModeTime(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;
        if (cx.getString(R.string.device_steamOvenOne_name_faXiao).equals(model) ||
                cx.getString(R.string.device_steamOvenOne_name_gaoWenZhen).equals(model)
                || cx.getString(R.string.device_steamOvenOne_name_xianNenZheng).equals(model)
                || cx.getString(R.string.device_steamOvenOne_name_yingYangZheng).equals(model)) {
            for (int i = 1; i <= 120; i++) {
                list.add(i);
            }
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.img_back, R.id.btn_start,R.id.iv_water})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                UIService.getInstance().popBack();
                break;
            case R.id.btn_start:
                UIService.getInstance().popBack();
                startRunModel();
                break;
            case R.id.iv_water:
                if(steameOvenC906.WaterStatus == 0){
                    steameOvenC906.setSteameOvenOneWaterPop((short) 1,new VoidCallback() {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onFailure(Throwable t) {}
                    });
                }
                break;
        }
    }

    private void startRunModel() {
        if (steameOvenC906.WaterStatus == 1) {
            ToastUtils.show(R.string.device_alarm_water_out, Toast.LENGTH_LONG);
            return;
        }
        short model = 0;
        if (cx.getString(R.string.device_steamOvenOne_name_faXiao).equals(wv1PatternSteame.getSelectedText())) {
            model = SteamOvenOneModel.FAXIAO;
        } else if (cx.getString(R.string.device_steamOvenOne_name_gaoWenZhen).equals(wv1PatternSteame.getSelectedText())) {
            model = SteamOvenOneModel.GAOWENZHENG;
        } else if (cx.getString(R.string.device_steamOvenOne_name_yingYangZheng).equals(wv1PatternSteame.getSelectedText())) {
            model = SteamOvenOneModel.YINGYANGZHENG;
        } else if (cx.getString(R.string.device_steamOvenOne_name_xianNenZheng).equals(wv1PatternSteame.getSelectedText())) {
            model = SteamOvenOneModel.XIANNENZHENG;
        }
        time = Short.valueOf(wv3SteameTime.getSelectedText());
        temp = Short.valueOf(wv2SteameTemp.getSelectedText());
        LogUtils.i("20171012","model:"+model);
        if (steameOvenC906 != null){
            steameOvenC906.setSteameOvenOneRunMode(model, time, temp,
                    (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                         /*   if (steameOvenC906.alarm == 0){
                                Bundle bundle = new Bundle();
                                bundle.putString(PageArgumentKey.Guid, steameOvenC906.getID());
                                bundle.putShort("from", (short) 1);
                                UIService.getInstance().postPage(PageKey.DeviceOvenC906Working, bundle);
                            }*/
                        }
                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
        }
    }
}
