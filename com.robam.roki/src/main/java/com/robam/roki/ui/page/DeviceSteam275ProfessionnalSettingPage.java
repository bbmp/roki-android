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
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.DeviceOven026ModeWheel;
import com.robam.roki.ui.view.DeviceOven028ModelWheel;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/1/17.
 */

public class DeviceSteam275ProfessionnalSettingPage extends HeadPage {

    AbsSteamoven steamoven275;
    private final int ORDERHOURTIME = 1;
    private final int ORDERMINTIME = 2;
    public View view;

    private short time;
    private short temp;
    private short modeKind;
    private Map<String, Short> modeKingMap = new HashMap<String, Short>();
    List<String> mHourList = new ArrayList<>();
    List<String> mMinutesList = new ArrayList<>();
    @InjectView(R.id.wheelView)
    LinearLayout wheelView;
    @InjectView(R.id.btnConfirm)
    TextView btnConfirm;
    @InjectView(R.id.wv1)
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
    private IRokiDialog mOrderTimeDialog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ORDERHOURTIME:
                    setDeviceOrderTime((String)msg.obj);
                    break;
                case ORDERMINTIME:
                    setDeviceOrderTime((String)msg.obj);
                    break;
            }

        }
    };

    private IRokiDialog mRokiToastDialog;

    //设置设备预约时间
    private void setDeviceOrderTime(String content) {


        if (content.contains(StringConstantsUtil.STR_HOUR)){
            String hour = RemoveManOrsymbolUtil.getRemoveString(content);
            LogUtils.i("20170919","hour:"+hour);
            mHourList.add(hour);
        }

        if (content.contains(StringConstantsUtil.STRING_MINUTES)){
            String min = RemoveManOrsymbolUtil.getRemoveString(content);
            LogUtils.i("20170919","min:"+min);
            mMinutesList.add(min);
        }

        mOrderTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderTimeDialog != null && mOrderTimeDialog.isShow()){
                    mOrderTimeDialog.dismiss();
                    if (!steamoven275.isConnected()) {
                        ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
                        return;
                    }
                    if (steamoven275.waterboxstate == 0) {
                        ToastUtils.show(R.string.device_water_good, Toast.LENGTH_SHORT);
                        return;
                    }
                    int model = getRunModel();
                    short n = (short) 0;
                    time = Short.valueOf(timeWheel.getSelectedText());
                    temp = Short.valueOf(temWheel.getSelectedText());
                    steamoven275.setSteamCookMode((short) model,temp ,time ,
                            (short) 1 , n, n, (short) 1, (short) 1, (short) 2,
                            Short.parseShort(String.valueOf(mMinutesList.get(mMinutesList.size()-1))),
                            Short.parseShort(String.valueOf(mHourList.get(mHourList.size()-1))), new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    UIService.getInstance().popBack();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(PageArgumentKey.Guid, steamoven275.getID());
                                    bundle.putShort("from", (short) 1);
                                    UIService.getInstance().postPage(PageKey.DeviceSteam226Working, bundle);
                                }

                                @Override
                                public void onFailure(Throwable t) {

                                }
                            });
                }
            }
        });




    }
    //获取运行
    private int getRunModel() {
        LogUtils.i("20180108","modeWheel::"+modeWheel.getSelectedText());
        short model = 0;
        if (cx.getString(R.string.device_steam_model_yulei).equals(modeWheel.getSelectedText())) {
            model = 3;
        } else if (cx.getString(R.string.device_steam_model_danlei).equals(modeWheel.getSelectedText())) {
            model = 4;
        } else if (cx.getString(R.string.device_steam_model_tijin).equals(modeWheel.getSelectedText())) {
            model = 6;
        } else if (cx.getString(R.string.device_steam_model_shucai).equals(modeWheel.getSelectedText())) {
            model = 7;
        } else if (cx.getString(R.string.device_steam_model_roulei).equals(modeWheel.getSelectedText())) {
            model = 10;
        } else if (cx.getString(R.string.device_steam_model_mianshi).equals(modeWheel.getSelectedText())) {
            model = 11;
        } else if (cx.getString(R.string.device_steam_model_mifan).equals(modeWheel.getSelectedText())) {
            model = 12;
        } else if (cx.getString(R.string.device_steam_model_qianglizheng).equals(modeWheel.getSelectedText())) {
            model = 13;
        }else if ("速蒸".equals(modeWheel.getSelectedText())){
            model = 13;
        }else if ("解冻".equals(modeWheel.getSelectedText())){
            model = 9;
        }else if ("除垢".equals(modeWheel.getSelectedText())){
            model = 20;
        }

        return model;
    }


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steamoven275 = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven275_professional_setting,
                container, false);
        ButterKnife.inject(this, contentView);
        steam226_main_waternotice.setVisibility(View.VISIBLE);
        initDatawith275();
        txtMode.setText(modeWheel.getSelectedText());
        mRokiToastDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        initview();
        return contentView;
    }

    private void initview() {
        txtMode.setVisibility(View.GONE);
        txtContext.setVisibility(View.GONE);
    }

    private void initDatawith275(){
        List<String> list = Lists.newArrayList();
        list.add(cx.getString(R.string.device_steam_model_yulei));//鱼类
        list.add(cx.getString(R.string.device_steam_model_mianshi));//面食
        list.add(cx.getString(R.string.device_steam_model_danlei));//蛋类
        list.add(cx.getString(R.string.device_steam_model_shucai));//蔬菜
        list.add(cx.getString(R.string.device_steam_model_suzheng));//速蒸
        list.add(cx.getString(R.string.unfreeze));//解冻
        list.add(cx.getString(R.string.clean));//除垢
        modeWheel.initData(list);
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        Map<Integer, Integer> map = Maps.newHashMap();
        map.put(0, R.mipmap.ic_275_yu);//鱼类
        map.put(1, R.mipmap.ic_275_mianshi);//面食
        map.put(2, R.mipmap.ic_275_dan);//蛋类
        map.put(3, R.mipmap.ic_275_shucai);//蔬菜
        map.put(4,R.mipmap.ic_275_suzheng);//速蒸
        map.put(5,R.mipmap.jiedong_icon);//解冻
        map.put(6,R.mipmap.ic_275_chugou);//除垢
        modeWheel.setMapImg(map);
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceSteam275ProfessionalSetting)) {
            return;
        }
        LogUtils.i("20180306","ev::::"+event.pojo.status);
        if (steamoven275 == null || !Objects.equal(steamoven275.getID(), event.pojo.getID()))
            return;
        if (steamoven275.status == SteamStatus.Wait || steamoven275.status == SteamStatus.Off || steamoven275.status==SteamStatus.Working||
                steamoven275.status == SteamStatus.PreHeat ) {
            LogUtils.i("20180306","back----");
           UIService.getInstance().popBack();
        } else if (steamoven275.status == SteamStatus.AlarmStatus) {

        }

    }


    DeviceOven028ModelWheel.OnSelectListener modeWheelLitener = new DeviceOven028ModelWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            setInitDate275(index,item);
        }

        @Override
        public void selecting(int index, Object item) {

        }
    };


    private void setInitDate275(int index, Object item){
        List<?> list1 = getList4(item);
        List<?> list2 = getList5(item);
        temWheel.setData(list1);
        timeWheel.setData(list2);
        int def1 = 0, def2 = 0;
        if (index == 0) {//鱼
            def1 = 10;
            def2 = 11;
        } else if (index == 1) {//面食
            def1 = 65;
            def2 = 24;
        } else if (index == 2) {//蛋类
            def1 = 15;
            def2 = 14;
        } else if (index == 3) {//蔬菜
            def1 = 15;
            def2 = 9;
        } else if (index == 4) {//速蒸
            def1 = 0;
            def2 = 9;
        } else if (index == 5) {//解冻
            def1 = 0;
            def2 = 29;
        } else if (index == 6) {//除垢
            def1 = 10;
            def2 = 19;
        }
        temWheel.setDefault(def1);
        timeWheel.setDefault(def2);
    }



    //275温度参数范围
    protected List<?> getList4(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;

        if (model.equals(cx.getString(R.string.device_steam_model_yulei))
                || model.equals(cx.getString(R.string.device_steam_model_danlei))
                || model.equals(cx.getString(R.string.device_steam_model_shucai)) ) {
            for (int i = 85; i <= 100; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.unfreeze))) {
            for (int i = 55; i <= 65; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steam_model_mianshi))) {
            for (int i = 35; i <= 100; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steam_model_suzheng))) {
            list.add(105);
        } else if (model.equals(cx.getString(R.string.clean))){
            for (int i = 90; i <= 100 ; i++) {
                list.add(i);
            }
        }
        return list;
    }


    /*275设置各种模式时间*/
    protected List<?> getList5(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;
        if (model.equals(cx.getString(R.string.device_steam_model_yulei))||
                model.equals(cx.getString(R.string.device_steam_model_danlei))||
                model.equals(cx.getString(R.string.device_steam_model_shucai))||
                model.equals(cx.getString(R.string.device_steam_model_mianshi))||
                model.equals(cx.getString(R.string.unfreeze))||
                model.equals(cx.getString(R.string.clean))||
                model.equals(cx.getString(R.string.device_steam_model_suzheng))
                ){
            for (int i = 1; i <=90 ; i++) {
                list.add(i);
            }
        }
        /*if (model.equals(cx.getString(R.string.device_steam_model_yulei))
                || model.equals(cx.getString(R.string.device_steam_model_danlei))
                || model.equals(cx.getString(R.string.device_steam_model_shucai))
                || model.equals(cx.getString(R.string.device_steam_model_mianshi))
                ) {
            for (int i = 5; i <= 60; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.unfreeze))
                ||model.equals(cx.getString(R.string.clean))) {
            for (int i = 15; i <= 60; i++) {
                list.add(i);
            }
        }else if (model.equals(cx.getString(R.string.device_steam_model_suzheng))){
            for (int i = 5; i <=90 ; i++) {
                list.add(i);
            }
        }*/
        return list;
    }



    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!steamoven275.isConnected()) {
            mRokiToastDialog.setContentText(R.string.device_connected);
            mRokiToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiToastDialog.show();
            return;
        }
        if (steamoven275.waterboxstate == 0) {
            mRokiToastDialog.setContentText(R.string.device_water_good);
            mRokiToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiToastDialog.show();
            return;
        }
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
        short n = (short) 0;
        int model = getRunModel();
        LogUtils.i("20180108","model::"+model);
        steamoven275.setSteamCookMode((short) model, temp, time, n, n, n, n, n, n, n, n, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20180108","success::");
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, steamoven275.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceSteam226Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
