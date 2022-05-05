package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.Oven026OrderTimeDialog;
import com.robam.roki.ui.view.DeviceOven026ModeWheel;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.OvenOrderTimeDataUtil;
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
 * Created by linxiaobin on 2015/12/22.
 */
public class DeviceOven026ProfessionalSettingPage extends HeadPage {


    AbsOven oven;

    public View view;

    private short time;
    private short temp;
    private short modeKind;
    private Map<String, Short> modeKingMap = new HashMap<String, Short>();

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

    private final int HOUR_SELE = 0;//小时
    private final int MIN_SELE = 1;//分钟
    List<String> stringHourList = new ArrayList<>();
    List<String> stringMinList = new ArrayList<>();
    private IRokiDialog rokiOrderTimeDialog = null;


    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HOUR_SELE:
                    setDeviceOrderTime((String) msg.obj);
                    break;
                case MIN_SELE:
                    setDeviceOrderTime((String) msg.obj);
                    break;
                case 0x03:
                    txtMode.setText(modeWheel.getSelectedText());
                    if (modeWheel.getSelectedText().equals("快热"))
                        txtContext.setText("快热模式适合烤蛋挞、玉米、葱油饼。");
                    else if (modeWheel.getSelectedText().equals("风焙烤"))
                        txtContext.setText("风焙烤模式适合烤一些成品食物，如：肉串、薯条。");
                    else if (modeWheel.getSelectedText().equals("焙烤"))
                        txtContext.setText("焙烤模式特别适合烘焙，主要烤蛋糕、饼干、奶黄包。");
                    else if (modeWheel.getSelectedText().equals("风扇烤"))
                        txtContext.setText("风扇烤适合烤鸭，烤鸡，也适合烤牛排、猪排、五花肉、培根、翅中、鸡腿。");
                    else if (modeWheel.getSelectedText().equals("烤烧"))
                        txtContext.setText("烤烧模式适合烤猪排、肉串、香肠、翅根。");
                    else if (modeWheel.getSelectedText().equals("强烤烧"))
                        txtContext.setText("强烤烧功能强大，适合烤：牛排、香肠、培根、鸡肉、翅中、翅根、鸡腿、肉串、烤鱼。");
                    else if (modeWheel.getSelectedText().equals("底加热"))
                        txtContext.setText("适合加热餐点、燉干汤汁、制作果酱，也适合烘烤浅色糕点。");
                    break;
            }

        }
    };

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven026_professional_setting,
                container, false);
        ButterKnife.inject(this, contentView);
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        initData();
        txtMode.setText(modeWheel.getSelectedText());
        return contentView;
    }

    private void initData() {

        btnorder.setVisibility(View.VISIBLE);
        modeKingMap.put("快热", (short) 0);
        modeKingMap.put("风焙烤", (short) 1);
        modeKingMap.put("焙烤", (short) 2);
        modeKingMap.put("底加热", (short) 3);
        modeKingMap.put("风扇烤", (short) 4);
        modeKingMap.put("强烤烧", (short) 5);
        modeKingMap.put("烤烧", (short) 6);
    }

    DeviceOven026ModeWheel.OnSelectListener modeWheelLitener = new DeviceOven026ModeWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            Log.i("index+Item:endSelect", index + "+" + item);
            List<?> list1 = getList2(item);
            List<?> list2 = getList3(item);
            temWheel.setData(list1);
            timeWheel.setData(list2);
            int def1 = 0, def2 = 0;
            if (index == 0) {
                def1 = 150;
                def2 = 45;
            } else if (index == 1) {
                def1 = 140;
                def2 = 55;
            } else if (index == 2) {
                def1 = 100;
                def2 = 55;
            } else if (index == 3) {//底加热
                def1 = 110;
                def2 = 45;
            } else if (index == 4) {
                def1 = 160;
                def2 = 55;
            } else if (index == 5) {
                def1 = 130;
                def2 = 45;
            } else if (index == 6) {
                def1 = 140;
                def2 = 35;
            }
            mHandle.sendEmptyMessage(0x03);
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

        if (s.equals("底加热")) {
            for (int i = 50; i <= 180; i++) {
                list.add(i);
            }
        } else if (s.equals("快热") || s.equals("烤烧")) {
            for (int i = 50; i <= 250; i++) {
                list.add(i);
            }
        } else if (s.equals("风焙烤") || s.equals("焙烤") || s.equals("风扇烤")) {
            for (int i = 60; i <= 250; i++) {
                list.add(i);
            }
        } else if (s.equals("强烤烧")) {
            for (int i = 40; i <= 250; i++) {
                list.add(i);
            }
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (s.equals("快热") || s.equals("风焙烤") || s.equals("风扇烤")
                || s.equals("烤烧") || s.equals("强烤烧") || s.equals("焙烤") || s.equals("底加热")) {
            for (int i = 5; i <= 90; i++) {
                list.add(i);
            }
        }
        return list;
    }

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        hanToEnglish(modeWheel);
        if (!oven.isConnected()) {
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return;
        }
        if (oven.status == OvenStatus.AlarmStatus) {
            ToastUtils.show(getString(R.string.mac_error), Toast.LENGTH_SHORT);
            return;
        }
        short model = getModel();
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
        oven.setOvenRunMode(model, time, temp, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, oven.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceOven026Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    Oven026OrderTimeDialog orderTimeDialog;

    @OnClick(R.id.btnorder)
    public void OnOrderClick() {

        rokiOrderTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        rokiOrderTimeDialog.setWheelViewData(OvenOrderTimeDataUtil.getListOrderTimeHourData(), null,
                OvenOrderTimeDataUtil.getListOrderTimeMinData(), false, 12, 0, 30, new OnItemSelectedListenerFrone() {
                    @Override
                    public void onItemSelectedFront(String contentFront) {
                        Message message = mHandle.obtainMessage();
                        message.obj = contentFront;
                        message.what = HOUR_SELE;
                        mHandle.sendMessage(message);
                    }
                }, null, new OnItemSelectedListenerRear() {
                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        Message message = mHandle.obtainMessage();
                        message.obj = contentRear;
                        message.what = MIN_SELE;
                        mHandle.sendMessage(message);
                    }
                });
        rokiOrderTimeDialog.show();
    }

    //设置预约时间
    private void setDeviceOrderTime(String data) {
        if (data.contains(StringConstantsUtil.STR_HOUR)) {
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringHourList.add(removetTempString);
        }
        if (data.contains(StringConstantsUtil.STRING_MINUTES)) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringMinList.add(removeTimeString);
        }

        rokiOrderTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiOrderTimeDialog.dismiss();
                hanToEnglish(modeWheel);
                short model = getModel();
                time = Short.valueOf(timeWheel.getSelectedText());
                temp = Short.valueOf(temWheel.getSelectedText());
                oven.setOvenRunMode(model, time, temp, (short) 1, (short) 0, (short) 0, (short) 1, (short) 0,
                        Short.parseShort(String.valueOf(stringMinList.get(stringMinList.size() - 1))),
                        Short.parseShort(String.valueOf(stringHourList.get(stringHourList.size() - 1))), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                if (orderTimeDialog != null && orderTimeDialog.isShowing()) {
                                    orderTimeDialog.dismiss();
                                    orderTimeDialog = null;
                                }
                                UIService.getInstance().popBack();
                                Bundle bundle = new Bundle();
                                bundle.putString(PageArgumentKey.Guid, oven.getID());
                                bundle.putShort("from", (short) 1);
                                UIService.getInstance().postPage(PageKey.DeviceOven026Working, bundle);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
            }
        });


    }

    //获取运行模式
    private short getModel() {
        short model = 0;
        if ("快热".equals(modeWheel.getSelectedText())) {
            model = 1;
        } else if ("风焙烤".equals(modeWheel.getSelectedText())) {
            model = 2;
        } else if ("焙烤".equals(modeWheel.getSelectedText())) {
            model = 3;
        } else if ("底加热".equals(modeWheel.getSelectedText())) {
            model = 4;
        } else if ("风扇烤".equals(modeWheel.getSelectedText())) {
            model = 6;
        } else if ("烤烧".equals(modeWheel.getSelectedText())) {
            model = 7;
        } else if ("强烤烧".equals(modeWheel.getSelectedText())) {
            model = 8;
        }
        return model;
    }


    private String tempPar = null;

    private void hanToEnglish(DeviceOven026ModeWheel modeWheel) {
        if ("快热".equals(modeWheel.getSelectedText())) {
            tempPar = "QuickHeat";
        } else if ("风焙烤".equals(modeWheel.getSelectedText())) {
            tempPar = "WindBaking";
        } else if ("焙烤".equals(modeWheel.getSelectedText())) {
            tempPar = "Baking";
        } else if ("底加热".equals(modeWheel.getSelectedText())) {
            tempPar = "BottomHeat";
        } else if ("解冻".equals(modeWheel.getSelectedText())) {
            tempPar = "Unfreeze";
        } else if ("风扇烤".equals(modeWheel.getSelectedText())) {
            tempPar = "FanBaking";
        } else if ("烧烤".equals(modeWheel.getSelectedText())) {
            tempPar = "Barbecue";
        } else if ("强烧烤".equals(modeWheel.getSelectedText())) {
            tempPar = "StrongBarbecue";
        }
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceOven026ProfessionalSetting)) {
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
