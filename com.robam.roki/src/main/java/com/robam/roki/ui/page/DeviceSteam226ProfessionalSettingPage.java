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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.DeviceOven026ModeWheel;
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
 * Created by Administrator on 2016/11/3.
 */

public class DeviceSteam226ProfessionalSettingPage extends HeadPage {

    AbsSteamoven steamoven226;
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
    DeviceOven026ModeWheel modeWheel;
    @InjectView(R.id.wv2)
    DeviceOvenTemWheel temWheel;
    @InjectView(R.id.wv3)
    DeviceOvenTimeWheel timeWheel;
    @InjectView(R.id.txt1)
    TextView txtMode;
    @InjectView(R.id.txt2)
    TextView txtContext;
    @InjectView(R.id.btnorder)
    TextView btnorder;

    @InjectView(R.id.steam226_main_waternotice)
    LinearLayout steam226_main_waternotice;
    private IRokiDialog mOrderTimeDialog;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ORDERHOURTIME:
                    setDeviceOrderTime((String) msg.obj);
                    break;
                case ORDERMINTIME:
                    setDeviceOrderTime((String) msg.obj);
                    break;
            }

        }
    };

    private IRokiDialog mRokiToastDialog;

    //设置设备预约时间
    private void setDeviceOrderTime(String content) {


        if (content.contains(StringConstantsUtil.STR_HOUR)) {
            String hour = RemoveManOrsymbolUtil.getRemoveString(content);
            LogUtils.i("20170919", "hour:" + hour);
            mHourList.add(hour);
        }

        if (content.contains(StringConstantsUtil.STRING_MINUTES)) {
            String min = RemoveManOrsymbolUtil.getRemoveString(content);
            LogUtils.i("20170919", "min:" + min);
            mMinutesList.add(min);
        }

        mOrderTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderTimeDialog != null && mOrderTimeDialog.isShow()) {
                    mOrderTimeDialog.dismiss();
                    hanToEnglish(modeWheel);
                    if (!steamoven226.isConnected()) {
                        ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
                        return;
                    }
                    if (steamoven226.waterboxstate == 0) {
                        ToastUtils.show(R.string.device_water_good, Toast.LENGTH_SHORT);
                        return;
                    }
                    int model = getRunModel();
                    short n = (short) 0;
                    time = Short.valueOf(timeWheel.getSelectedText());
                    temp = Short.valueOf(temWheel.getSelectedText());
                    steamoven226.setSteamCookMode((short) model, temp, time,
                            (short) 1, n, n, (short) 1, (short) 1, (short) 2,
                            Short.parseShort(String.valueOf(mMinutesList.get(mMinutesList.size() - 1))),
                            Short.parseShort(String.valueOf(mHourList.get(mHourList.size() - 1))), new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    UIService.getInstance().popBack();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(PageArgumentKey.Guid, steamoven226.getID());
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
        LogUtils.i("20180108", "modeWheel::" + modeWheel.getSelectedText());
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
        } else if ("速蒸".equals(modeWheel.getSelectedText())) {
            model = 13;
        } else if ("解冻".equals(modeWheel.getSelectedText())) {
            model = 9;
        } else if ("除垢".equals(modeWheel.getSelectedText())) {
            model = 20;
        }

        return model;
    }


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steamoven226 = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven026_professional_setting,
                container, false);
        ButterKnife.inject(this, contentView);
        steam226_main_waternotice.setVisibility(View.VISIBLE);
        if ("RS275".equals(steamoven226.getDt())) {
            initDatawith275();
            btnorder.setVisibility(View.GONE);
        } else {
            initData();
        }
        txtMode.setText(modeWheel.getSelectedText());
        mRokiToastDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        initview();
        return contentView;
    }

    private void initview() {
        txtMode.setVisibility(View.GONE);
        txtContext.setVisibility(View.GONE);
    }

    private void initDatawith275() {
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
        map.put(0, R.mipmap.ic_steam226_fish_white);//鱼类
        map.put(1, R.mipmap.ic_steam226_pastry_white);//面食
        map.put(2, R.mipmap.ic_steam226_egg_white);//蛋类
        map.put(3, R.mipmap.ic_steam226_vegetable_white);//蔬菜
        map.put(4, R.mipmap.ic_steam226_vegetable_white);//速蒸
        map.put(5, R.mipmap.ic_steam226_vegetable_white);//解冻
        map.put(6, R.mipmap.ic_steam226_vegetable_white);//除垢
        modeWheel.setMapImg(map);
    }

    private void initData() {
        List<String> list = Lists.newArrayList();
        list.add(cx.getString(R.string.device_steam_model_yulei));
        list.add(cx.getString(R.string.device_steam_model_danlei));
        list.add(cx.getString(R.string.device_steam_model_tijin));
        list.add(cx.getString(R.string.device_steam_model_shucai));
        list.add(cx.getString(R.string.device_steam_model_roulei));
        list.add(cx.getString(R.string.device_steam_model_mianshi));
        list.add(cx.getString(R.string.device_steam_model_mifan));
        list.add(cx.getString(R.string.device_steam_model_qianglizheng));
        modeWheel.initData(list);
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        Map<Integer, Integer> map = Maps.newHashMap();
        map.put(0, R.mipmap.ic_steam226_fish_white);
        map.put(1, R.mipmap.ic_steam226_egg_white);
        map.put(2, R.mipmap.ic_steam226_tendon_white);
        map.put(3, R.mipmap.ic_steam226_vegetable_white);
        map.put(4, R.mipmap.ic_steam226_meat_white);
        map.put(5, R.mipmap.ic_steam226_pastry_white);
        map.put(6, R.mipmap.ic_steam226_rice_white);
        map.put(7, R.mipmap.ic_steam226_strongsteam_white);
        modeWheel.setMapImg(map);
    }

    DeviceOven026ModeWheel.OnSelectListener modeWheelLitener = new DeviceOven026ModeWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            if ("RS275".equals(steamoven226.getDt())) {
                setInitDate275(index, item);
            } else {
                setInitDate(index, item);
            }
        }

        @Override
        public void selecting(int index, Object item) {

        }
    };


    private void setInitDate275(int index, Object item) {
        List<?> list1 = getList4(item);
        List<?> list2 = getList5(item);
        temWheel.setData(list1);
        timeWheel.setData(list2);
        int def1 = 0, def2 = 0;
        if (index == 0) {//鱼
            def1 = 10;
            def2 = 7;
        } else if (index == 1) {//面食
            def1 = 65;
            def2 = 20;
        } else if (index == 2) {//蛋类
            def1 = 15;
            def2 = 10;
        } else if (index == 3) {//蔬菜
            def1 = 15;
            def2 = 5;
        } else if (index == 4) {//速蒸
            def1 = 0;
            def2 = 5;
        } else if (index == 5) {//解冻
            def1 = 0;
            def2 = 15;
        } else if (index == 6) {//除垢
            def1 = 10;
            def2 = 5;
        }
        temWheel.setDefault(def1);
        timeWheel.setDefault(def2);
    }

    private void setInitDate(int index, Object item) {
        List<?> list1 = getList2(item);
        List<?> list2 = getList3(item);
        temWheel.setData(list1);
        timeWheel.setData(list2);
        int def1 = 0, def2 = 0;
        if (index == 0) {//鱼
            def1 = 10;
            def2 = 7;
        } else if (index == 1) {//蛋
            def1 = 15;
            def2 = 10;
        } else if (index == 2) {//蹄筋
            def1 = 10;
            def2 = 40;
        } else if (index == 3) {//蔬菜
            def1 = 15;
            def2 = 5;
        } else if (index == 4) {//肉
            def1 = 10;
            def2 = 15;
        } else if (index == 5) {//面食
            def1 = 65;
            def2 = 20;
        } else if (index == 6) {//米饭
            def1 = 10;
            def2 = 25;
        } else if (index == 7) {//强力蒸
            def1 = 0;
            def2 = 30;
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
                || model.equals(cx.getString(R.string.device_steam_model_shucai))) {
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
        } else if (model.equals(cx.getString(R.string.clean))) {
            for (int i = 90; i <= 100; i++) {
                list.add(i);
            }
        }
        return list;
    }


    /*275设置各种模式时间*/
    protected List<?> getList5(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;
        if (model.equals(cx.getString(R.string.device_steam_model_yulei))
                || model.equals(cx.getString(R.string.device_steam_model_danlei))
                || model.equals(cx.getString(R.string.device_steam_model_shucai))
                || model.equals(cx.getString(R.string.device_steam_model_mianshi))
                ) {
            for (int i = 5; i <= 60; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.unfreeze))
                || model.equals(cx.getString(R.string.clean))) {
            for (int i = 15; i <= 60; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steam_model_suzheng))) {
            for (int i = 5; i <= 90; i++) {
                list.add(i);
            }
        }
        return list;
    }


    /*226设置各种模式温度范围*/
    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;

        if (model.equals(cx.getString(R.string.device_steam_model_yulei))
                || model.equals(cx.getString(R.string.device_steam_model_danlei))
                || model.equals(cx.getString(R.string.device_steam_model_shucai))) {
            for (int i = 85; i <= 100; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steam_model_tijin))
                || model.equals(cx.getString(R.string.device_steam_model_roulei))
                || model.equals(cx.getString(R.string.device_steam_model_mifan))) {
            for (int i = 90; i <= 100; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steam_model_mianshi))) {
            for (int i = 35; i <= 100; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steam_model_qianglizheng))) {
            list.add(105);
        }
        return list;
    }


    /*226设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String model = (String) item;
        if (model.equals(cx.getString(R.string.device_steam_model_yulei))
                || model.equals(cx.getString(R.string.device_steam_model_danlei))
                || model.equals(cx.getString(R.string.device_steam_model_shucai))
                || model.equals(cx.getString(R.string.device_steam_model_mianshi))
                || model.equals(cx.getString(R.string.device_steam_model_qianglizheng))
                || model.equals(cx.getString(R.string.device_steam_model_roulei))
                || model.equals(cx.getString(R.string.device_steam_model_mifan))) {
            for (int i = 5; i <= 90; i++) {
                list.add(i);
            }
        } else if (model.equals(cx.getString(R.string.device_steam_model_tijin))) {
            for (int i = 5; i <= 180; i++) {
                list.add(i);
            }
        }
        return list;
    }

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!steamoven226.isConnected()) {
            mRokiToastDialog.setContentText(R.string.device_connected);
            mRokiToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiToastDialog.show();
            return;
        }
        if (steamoven226.waterboxstate == 0) {
            mRokiToastDialog.setContentText(R.string.device_water_good);
            mRokiToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiToastDialog.show();
            return;
        }
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
        short n = (short) 0;
        int model = getRunModel();
        LogUtils.i("20180108", "model::" + model);
        steamoven226.setSteamCookMode((short) model, temp, time, n, n, n, n, n, n, n, n, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, steamoven226.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceSteam226Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private String tempPar;

    private void hanToEnglish(DeviceOven026ModeWheel modeWheel) {
        if (cx.getString(R.string.device_steam_model_yulei).equals(modeWheel.getSelectedText())) {
            tempPar = "Fish";
        } else if (cx.getString(R.string.device_steam_model_danlei).equals(modeWheel.getSelectedText())) {
            tempPar = "Eggs";
        } else if (cx.getString(R.string.device_steam_model_tijin).equals(modeWheel.getSelectedText())) {
            tempPar = "Sinew";
        } else if (cx.getString(R.string.device_steam_model_shucai).equals(modeWheel.getSelectedText())) {
            tempPar = "Vegetables";
        } else if (cx.getString(R.string.device_steam_model_roulei).equals(modeWheel.getSelectedText())) {
            tempPar = "Meat";
        } else if (cx.getString(R.string.device_steam_model_mianshi).equals(modeWheel.getSelectedText())) {
            tempPar = "Pasta";
        } else if (cx.getString(R.string.device_steam_model_mifan).equals(modeWheel.getSelectedText())) {
            tempPar = "Rice";
        } else if (cx.getString(R.string.device_steam_model_qianglizheng).equals(modeWheel.getSelectedText())) {
            tempPar = "Steam";
        }
    }

    @OnClick(R.id.btnorder)
    public void OnOrderClick() {
        if (!steamoven226.isConnected()) {
            mRokiToastDialog.setContentText(R.string.device_connected);
            mRokiToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiToastDialog.show();
            return;
        }
        if (steamoven226.waterboxstate == 0) {
            mRokiToastDialog.setContentText(R.string.device_water_good);
            mRokiToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mRokiToastDialog.show();
            return;
        }
        mOrderTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        mOrderTimeDialog.setWheelViewData(getOrderHourData(), null, getOrderMinutesData(), false, 12, 0, 30, new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {

                Message message = mHandler.obtainMessage();
                message.what = ORDERHOURTIME;
                message.obj = contentFront;
                mHandler.sendMessage(message);

            }
        }, null, new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                Message message = mHandler.obtainMessage();
                message.what = ORDERMINTIME;
                message.obj = contentRear;
                mHandler.sendMessage(message);
            }
        });

        mOrderTimeDialog.setCanceledOnTouchOutside(true);
        mOrderTimeDialog.show();
    }

    //预约小时
    private List<String> getOrderHourData() {
        List<String> hourList = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            hourList.add(i + StringConstantsUtil.STR_HOUR);
        }
        return hourList;
    }

    //预约分钟
    private List<String> getOrderMinutesData() {
        List<String> minutesList = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minutesList.add(i + StringConstantsUtil.STRING_MINUTES);
        }
        return minutesList;
    }


}
