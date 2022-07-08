package com.robam.roki.ui.page;

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
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.view.DeviceAssistC906ModeWheel;
import com.robam.roki.ui.view.TemlWheelView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19.
 */

public class DeviceSteameOvenC906AssistPage extends BasePage {


    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.iv_water)
    ImageView ivWater;
    @InjectView(R.id.ll_c906_sj)
    LinearLayout llC906Sj;
    @InjectView(R.id.wv1_pattern_assist)
    DeviceAssistC906ModeWheel wv1PatternAssist;
    @InjectView(R.id.wv2_assist_temp)
    TemlWheelView wv2AssistTemp;
    @InjectView(R.id.wv3_assist_time)
    TemlWheelView wv3AssistTime;
    @InjectView(R.id.ll_wheelView)
    LinearLayout llWheelView;
    @InjectView(R.id.tv_mode_name)
    TextView tvModeName;
    @InjectView(R.id.tv_mode_dec)
    TextView tvModeDec;
    @InjectView(R.id.btn_assist_start)
    Button btnAssistStart;

    private Map<String, Short> modeKingMap = new HashMap<String, Short>();
    private AbsSteameOvenOne steameOvenC906;
    private short mTime;
    private short mTemp;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 4) {
                    tvModeName.setText(wv1PatternAssist.getSelectedText());
                if (wv1PatternAssist.getSelectedText().equals
                        (cx.getString(R.string.device_steamOvenOne_name_jiedong))) {
                    tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_jiedong));
                } else if (wv1PatternAssist.getSelectedText().equals
                        (cx.getString(R.string.device_steamOvenOne_name_shajun))) {
                    tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_shajun));
                }
            }
        }
    };

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.pojo.getID()))
            return;
        if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus ||
                steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause || steameOvenC906.powerOnStatus
                == SteamOvenOnePowerOnStatus.Order || steameOvenC906.powerState == SteamOvenOnePowerStatus.Off){
            UIService.getInstance().popBack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_decive_c906_assist, null, false);
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
        ButterKnife.inject(this, view);
        wv1PatternAssist.setOnSelectListener(modeLitener);
        wv1PatternAssist.setDefault(1);
        initData();
        tvModeName.setText(wv1PatternAssist.getSelectedText());
        return view;
    }

    private void initData() {
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_jiedong), (short) 0);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_shajun), (short) 1);
    }

    DeviceAssistC906ModeWheel.OnSelectListener modeLitener = new DeviceAssistC906ModeWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {

            List<?> listModeTemperature = getListModeTemperature(item);
            List<?> listModeTime = getListModeTime(item);

            LogUtils.i("20171017","listModeTime:"+listModeTime.size()+"item:"+item);
            wv2AssistTemp.setData(listModeTemperature);
            wv3AssistTime.setData(listModeTime);
            int defTemp = 0, defTime = 0;
            if (index == 0) {//解冻
                defTemp = 20;
                defTime = 19;
            } else if (index == 1) {//杀菌
                defTemp = 0;
                defTime = 0;
            }
            handler.sendEmptyMessage(4);
            wv2AssistTemp.setDefault(defTemp);
            wv3AssistTime.setDefault(defTime);

        }

        @Override
        public void selecting(int index, Object item) {

        }
    };

    /*设置各种模式温度范围*/
    protected List<?> getListModeTemperature(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;

        if (model.equals(cx.getString(R.string.device_steamOvenOne_name_jiedong))) {
            for (int i = 40; i <= 80; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_shajun))) {
            int i = 100;
            list.add(i);
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getListModeTime(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;
        if (model.equals(cx.getString(R.string.device_steamOvenOne_name_jiedong))) {
            for (int i = 1; i <= 120; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_shajun))) {
            int i = 20;
            list.add(i);
        }

        return list;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
    @OnClick({R.id.img_back, R.id.iv_water, R.id.btn_assist_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                UIService.getInstance().popBack();
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
            case R.id.btn_assist_start:
                UIService.getInstance().popBack();
                startRunModel();
                break;
        }
    }

    private void startRunModel() {
        if (steameOvenC906.WaterStatus == 1) {
            ToastUtils.show(R.string.device_alarm_water_out, Toast.LENGTH_LONG);
            return;
        }
        short model = 0;
        if (cx.getString(R.string.device_steamOvenOne_name_jiedong).equals(wv1PatternAssist.getSelectedText())) {
            model = SteamOvenOneModel.JIEDONG;
        } else if (cx.getString(R.string.device_steamOvenOne_name_shajun).equals(wv1PatternAssist.getSelectedText())) {
            model = SteamOvenOneModel.ZHENGQISHAJUN;
        }

        mTemp = Short.valueOf(wv2AssistTemp.getSelectedText());
        mTime = Short.valueOf(wv3AssistTime.getSelectedText());
        LogUtils.i("20171013","model:"+model+" mTime:"+mTime+" mTemp:"+mTemp);

        if (steameOvenC906 != null) {
            steameOvenC906.setSteameOvenOneRunMode(model, mTime, mTemp,
                    (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {

                          /*  Bundle bundle = new Bundle();
                            bundle.putString(PageArgumentKey.Guid, steameOvenC906.getID());
                            bundle.putShort("from", (short) 1);
                            UIService.getInstance().postPage(PageKey.DeviceOvenC906Working, bundle);*/
                        }
                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
        }
    }
}
