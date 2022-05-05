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
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.DeviceOven026ModeWheel;
import com.robam.roki.ui.view.DeviceOven028ModelWheel;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;
import com.robam.roki.utils.DialogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/9/5.
 */

public class DeviceSteam228assistSettingPage extends HeadPage {
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
            if (msg.what == 0x03) {
                txtMode.setText(modeWheel.getSelectedText());
                if (getStringName(R.string.keeptempture).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.keeptemture_tip));
                }else if (getStringName(R.string.unfreeze).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.unfreeze_tip));
                }else if (getStringName(R.string.ferment).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.fermentsteam_tip));
                }else if (getStringName(R.string.sterilization).equals(modeWheel.getSelectedText())){
                    txtContext.setText(getStringName(R.string.sterilization_tip));
                }
            }
        }
    };


    IRokiDialog rokiDialog=null;

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
        rokiDialog= RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        return contentView;
    }

    private void initview() {
       /* txtMode.setVisibility(View.GONE);
        txtContext.setVisibility(View.GONE);*/
       // btn_order.setVisibility(View.GONE);
    }

    @Subscribe
    public void onEvent(SteamWaterBoxEvent event){
        ToastUtils.show("水箱已弹出",Toast.LENGTH_SHORT);
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceSteam228assistSetting)) {
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

    private void initData() {
        List<String> list = Lists.newArrayList();
        list.add(cx.getResources().getString(R.string.keeptempture));
        list.add(cx.getResources().getString(R.string.unfreeze));
        list.add(cx.getResources().getString(R.string.ferment));
        list.add(cx.getResources().getString(R.string.sterilization));
        modeWheel.initData(list);
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(1);
        Map<Integer, Integer> map = Maps.newHashMap();
        map.put(0, R.mipmap.img_oven028_baowen_mode);
        map.put(1, R.mipmap.jiedong_icon);
        map.put(2, R.mipmap.faxiao_icon);
        map.put(3, R.mipmap.shajun_icon);
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
            if (index == 0) {//保温
                def1 = 0;
                def2 = 59;
            } else if (index == 1) {//解冻
                def1 = 0;
                def2 = 29;
            } else if (index == 2) {//发酵
                def1 = 5;
                def2 = 59;
            } else if (index == 3) {//杀菌
                def1 = 0;
                def2 = 29;
            }
            handler.sendEmptyMessage(0x03);
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

        if (cx.getResources().getString(R.string.keeptempture).equals(s)) {
            for (int i = 60; i <= 70; i++) {
                list.add(i);
            }
        } else if (cx.getResources().getString(R.string.unfreeze).equals(s)){
            for (int i = 55; i <= 65; i++) {
                list.add(i);
            }
        } else if (cx.getResources().getString(R.string.ferment).equals(s)) {
            for (int i = 35; i <= 45; i++) {
                list.add(i);
            }
        } else if (cx.getResources().getString(R.string.sterilization).equals(s)) {
            list.add(100);
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (cx.getResources().getString(R.string.keeptempture).equals(s)) {
            for (int i = 1; i <= 99; i++) {
                list.add(i);
            }
        } else if (cx.getResources().getString(R.string.unfreeze).equals(s)){
            for (int i = 1; i <= 90; i++) {
                list.add(i);
            }
        } else if (cx.getResources().getString(R.string.ferment).equals(s)) {
            for (int i = 1; i <= 90; i++) {
                list.add(i);
            }
        } else if (cx.getResources().getString(R.string.sterilization).equals(s)) {
            for (int i = 1; i <= 60; i++) {
                list.add(i);
            }
        }
        return list;
    }
    short model = 0;
    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!steam228.isConnected()) {
            ToastUtils.show(cx.getResources().getString(R.string.networkwrong), Toast.LENGTH_SHORT);
            return;
        }
        if (steam228.waterboxstate == 0) {
            ToastUtils.show(cx.getResources().getString(R.string.installtank), Toast.LENGTH_SHORT);
            return;
        }

        if (steam228.doorState==0){
           rokiDialog.setTitleText("门未关好");
            rokiDialog.setContentText("门未关好，请检查并确认关好门");
            rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rokiDialog.dismiss();
                }
            });
            rokiDialog.setCanceledOnTouchOutside(false);
            rokiDialog.show();
            return;
        }

        if (cx.getResources().getString(R.string.keeptempture).equals(modeWheel.getSelectedText())) {
            model = 19;
        } else if ( cx.getResources().getString(R.string.unfreeze).equals(modeWheel.getSelectedText())) {
            model = 9;
        } else if (cx.getResources().getString(R.string.ferment).equals(modeWheel.getSelectedText())) {
            model = 18;
        } else if (cx.getResources().getString(R.string.sterilization).equals(modeWheel.getSelectedText())) {
            model = 14;
        }
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
       final short n = (short) 0;
        if (model==18){
            rokiDialog.setTitleText("发酵提示");
            rokiDialog.setContentText("请把发酵的食物放入有孔蒸盘，面团发酵时请盖一层保鲜膜");
            rokiDialog.setOkBtn("已放入蒸盘", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMsg(model,temp,time,n);
                    rokiDialog.dismiss();
                }
            });
            rokiDialog.show();
        }else{
            sendMsg(model,temp,time,n);
        }

    }

    private void sendMsg(short model,short temp,short time,short n){
        LogUtils.i("20171207","model:"+model);
        steam228.setSteamCookMode(model, temp, time, n, n, n, n, n, n, n, n, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, steam228.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceSteam228Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20171207","onFailure");
            }
        });
    }

    private String getStringName(int s){
        return cx.getResources().getString(s);
    }
}
