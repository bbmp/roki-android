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

    private final int HOUR_SELE = 0;//??????
    private final int MIN_SELE = 1;//??????
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
                    if (modeWheel.getSelectedText().equals("??????"))
                        txtContext.setText("???????????????????????????????????????????????????");
                    else if (modeWheel.getSelectedText().equals("?????????"))
                        txtContext.setText("?????????????????????????????????????????????????????????????????????");
                    else if (modeWheel.getSelectedText().equals("??????"))
                        txtContext.setText("????????????????????????????????????????????????????????????????????????");
                    else if (modeWheel.getSelectedText().equals("?????????"))
                        txtContext.setText("??????????????????????????????????????????????????????????????????????????????????????????????????????");
                    else if (modeWheel.getSelectedText().equals("??????"))
                        txtContext.setText("?????????????????????????????????????????????????????????");
                    else if (modeWheel.getSelectedText().equals("?????????"))
                        txtContext.setText("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                    else if (modeWheel.getSelectedText().equals("?????????"))
                        txtContext.setText("?????????????????????????????????????????????????????????????????????????????????");
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
        modeKingMap.put("??????", (short) 0);
        modeKingMap.put("?????????", (short) 1);
        modeKingMap.put("??????", (short) 2);
        modeKingMap.put("?????????", (short) 3);
        modeKingMap.put("?????????", (short) 4);
        modeKingMap.put("?????????", (short) 5);
        modeKingMap.put("??????", (short) 6);
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
            } else if (index == 3) {//?????????
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

    /*??????????????????????????????*/
    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;

        if (s.equals("?????????")) {
            for (int i = 50; i <= 180; i++) {
                list.add(i);
            }
        } else if (s.equals("??????") || s.equals("??????")) {
            for (int i = 50; i <= 250; i++) {
                list.add(i);
            }
        } else if (s.equals("?????????") || s.equals("??????") || s.equals("?????????")) {
            for (int i = 60; i <= 250; i++) {
                list.add(i);
            }
        } else if (s.equals("?????????")) {
            for (int i = 40; i <= 250; i++) {
                list.add(i);
            }
        }
        return list;
    }


    /*????????????????????????*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (s.equals("??????") || s.equals("?????????") || s.equals("?????????")
                || s.equals("??????") || s.equals("?????????") || s.equals("??????") || s.equals("?????????")) {
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
            ToastUtils.show("??????????????????", Toast.LENGTH_SHORT);
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

    //??????????????????
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

    //??????????????????
    private short getModel() {
        short model = 0;
        if ("??????".equals(modeWheel.getSelectedText())) {
            model = 1;
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
            model = 2;
        } else if ("??????".equals(modeWheel.getSelectedText())) {
            model = 3;
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
            model = 4;
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
            model = 6;
        } else if ("??????".equals(modeWheel.getSelectedText())) {
            model = 7;
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
            model = 8;
        }
        return model;
    }


    private String tempPar = null;

    private void hanToEnglish(DeviceOven026ModeWheel modeWheel) {
        if ("??????".equals(modeWheel.getSelectedText())) {
            tempPar = "QuickHeat";
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
            tempPar = "WindBaking";
        } else if ("??????".equals(modeWheel.getSelectedText())) {
            tempPar = "Baking";
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
            tempPar = "BottomHeat";
        } else if ("??????".equals(modeWheel.getSelectedText())) {
            tempPar = "Unfreeze";
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
            tempPar = "FanBaking";
        } else if ("??????".equals(modeWheel.getSelectedText())) {
            tempPar = "Barbecue";
        } else if ("?????????".equals(modeWheel.getSelectedText())) {
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
