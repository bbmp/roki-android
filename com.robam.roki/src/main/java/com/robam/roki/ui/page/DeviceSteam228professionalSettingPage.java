package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.events.SteamWaterBoxEvent;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.view.DeviceAssistC906ModeWheel;
import com.robam.roki.ui.view.DeviceOven026ModeWheel;
import com.robam.roki.ui.view.DeviceOven028ModelWheel;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;
import com.robam.roki.ui.view.DeviceSteameC906ModeWheel;
import com.robam.roki.utils.DialogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.R.string.strongsteam;

/**
 * Created by yinwei on 2017/9/5.
 */

public class DeviceSteam228professionalSettingPage extends HeadPage {
    AbsSteamoven steam228;

    public View view;
    private short time;
    private short temp;
    private short modeKind;
    private Map<String, Short> modeKingMap = new HashMap<String, Short>();

    @InjectView(R.id.wheelView)
    LinearLayout wheelView;
    @InjectView(R.id.btnConfirm)
    TextView btnConfirm;
    @InjectView(R.id.wv028)
    DeviceOven028ModelWheel modeWheel;
    @InjectView(R.id.wv2)
    DeviceOvenTemWheel temWheel;
    @InjectView(R.id.wv3)
    DeviceOvenTimeWheel timeWheel;
    @InjectView(R.id.txt1)
    TextView txtMode;
    @InjectView(R.id.txt2)
    TextView txtContext;

    @InjectView(R.id.steam226_main_waternotice)
    LinearLayout steam226_main_waternotice;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x02) {
                txtMode.setText(modeWheel.getSelectedText());
                if (getStringName(R.string.freshsteam).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.freshsteam_tip));
                }else if (getStringName(R.string.nutritive).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.nutritive_tip));
                }else if (getStringName(R.string.strongsteam).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.strongsteam_tip));
                }else if (getStringName(R.string.fast_steam_slow_steam).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.faststeam_tip));
                }
            }
        }
    };

    IRokiDialog iRokiDialogAlarmType_01 = null;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steam228 = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven028_professional_setting,
                container, false);
        ButterKnife.inject(this, contentView);
        steam226_main_waternotice.setVisibility(View.VISIBLE);
        initData();
        txtMode.setText(modeWheel.getSelectedText());
        initview();
        return contentView;
    }

    private void initview() {
        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
       /* txtMode.setVisibility(View.GONE);
        txtContext.setVisibility(View.GONE);*/
     //   btn_order.setVisibility(View.GONE);

    }

    private void initData() {
        List<String> list = Lists.newArrayList();
        list.add(getStringName(R.string.freshsteam));
        list.add(getStringName(R.string.nutritive));
        list.add(getStringName(strongsteam));
        list.add(getStringName(R.string.fast_steam_slow_steam));
        modeWheel.initData(list);
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        Map<Integer, Integer> map = Maps.newHashMap();
        map.put(0, R.mipmap.ic_275_xianneizheng);
        map.put(1, R.mipmap.ic_275_yingyangzheng);
        map.put(2, R.mipmap.ic_275_qianglizheng);
        map.put(3, R.mipmap.ic_275_kuaizhengmanduan);
        modeWheel.setMapImg(map);
    }

    DeviceOven028ModelWheel.OnSelectListener modeWheelLitener = new DeviceOven028ModelWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            List<?> list1 = getList2(item);
            List<?> list2 = getList3(item);
            temWheel.setData(list1);
            timeWheel.setData(list2);
            int def1 = 0, def2 = 0;
            if (index == 0) {//鲜嫩蒸
                def1 = 15;
                def2 = 19;
            } else if (index == 1) {//营养蒸
                def1 = 30;
                def2 = 19;
            } else if (index == 2) {//强力蒸
                def1 = 0;
                def2 = 34;
            } else if (index == 3) {//快蒸慢炖
                def1 = 0;
                def2 = 59;
            }
            handler.sendEmptyMessage(0x02);
            temWheel.setDefault(def1);
            timeWheel.setDefault(def2);

        }

        @Override
        public void selecting(int index, Object item) {

        }
    };

    /*设置各种模式温度范围*/
    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;

        if (getStringName(R.string.freshsteam).equals(s)||
                getStringName(R.string.nutritive).equals(s)) {
            for (int i = 70; i <= 100; i++) {
                list.add(i);
            }
        } else if (getStringName(strongsteam).equals(s)) {
                list.add(105);
        } else if (getStringName(R.string.fast_steam_slow_steam).equals(s)) {
                list.add(100);
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (getStringName(R.string.freshsteam).equals(s)||
                getStringName(R.string.nutritive).equals(s)||
                getStringName(strongsteam).equals(s)) {
            for (int i = 1; i <= 90; i++) {
                list.add(i);
            }
        }
        if (getStringName(R.string.fast_steam_slow_steam).equals(s)){
            for (int i = 1; i <= 180; i++) {
                list.add(i);
            }
        }
        return list;
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceSteam228ProfessionalSetting)) {
            return;
        }
        if (steam228 == null || !Objects.equal(steam228.getID(), event.pojo.getID()))
            return;
        if (steam228.status == OvenStatus.Wait || steam228.status == OvenStatus.Off || steam228.status == OvenStatus.Working || steam228.status == OvenStatus.Pause
                || steam228.status == OvenStatus.Order || steam228.status == OvenStatus.PreHeat) {
          /*  if (orderTimeDialog != null && orderTimeDialog.isShowing())
                orderTimeDialog.dismiss();
            orderTimeDialog = null;*/
            UIService.getInstance().popBack();
        }
    }

    @Subscribe
    public void onEvent(SteamWaterBoxEvent event){
        ToastUtils.show("水箱已弹出",Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!steam228.isConnected()) {
            ToastUtils.show(getStringName(R.string.networkwrong), Toast.LENGTH_SHORT);
            return;
        }
        if (steam228.waterboxstate == 0) {
            ToastUtils.show(getStringName(R.string.installtank), Toast.LENGTH_SHORT);
            return;
        }
        short model = 0;
        if (getStringName(R.string.freshsteam).equals(modeWheel.getSelectedText())) {
            model = 17;
        } else if (getStringName(R.string.nutritive).equals(modeWheel.getSelectedText())) {
            model = 16;
        } else if (getStringName(strongsteam).equals(modeWheel.getSelectedText())) {
            model = 13;
        } else if (getStringName(R.string.fast_steam_slow_steam).equals(modeWheel.getSelectedText())) {
            model = 15;
        }
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
        short n = (short) 0;
        if (steam228.doorState==0){//0是开1是关
            iRokiDialogAlarmType_01.setTitleText("门未关好");
            iRokiDialogAlarmType_01.setContentText("门未关好，请检查并确认关好门");
            iRokiDialogAlarmType_01.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRokiDialogAlarmType_01.dismiss();
                }
            });
            iRokiDialogAlarmType_01.setCanceledOnTouchOutside(false);
            iRokiDialogAlarmType_01.show();
            return;
        }

        steam228.setSteamCookMode(model, temp, time, n, n, n, n, n, n, n, n, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20171121","pro---success");
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, steam228.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceSteam228Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20171121","pro---onFailure");
            }
        });
    }
    
    private String getStringName(int s){
        return cx.getResources().getString(s);
    }


}
