package com.robam.roki.ui.page.device.steamovenone;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerPosition;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter3.Rv610ModeAdapter;
import com.robam.roki.ui.dialog.type.DialogType_Time;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.wheelview.LoopView;
import com.robam.roki.ui.widget.layout.SettingBar;
import com.robam.roki.ui.widget.view.SwitchButton;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.ToolUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/6.
 */
public class SteamOven610ModelSelectedPage extends MyBasePage<MainActivity> {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    //    private DeviceModelAdapter mDeviceModelAdapter;
    private Rv610ModeAdapter mDeviceModelAdapter;
    List<String> stringTimeList = new ArrayList<String>();
    List<String> stringTempList = new ArrayList<String>();
    String mGuid;
    String mTitle;
    String functionName;
    String functionCode;
    private IRokiDialog mRokiDialog;
    private List<DeviceConfigurationFunctions> mDeviceSelectModelList;
    private int mSteamModel;
    AbsSteameOvenOne mSteamOvenOne;
    /**
     * ??????
     */
    private LoopView wheelFront;
    /**
     * exp????????????
     */
    private TextView tvTopTemp;
    /**
     * exp????????????
     */
    private LinearLayout llFront2;
    private LinearLayout llRear;
    /**
     * ??????2
     */
    private LoopView wheelFront2;
    /**
     * ??????
     */
    private LoopView wheelRear;
    /**
     * ??????
     */
    private AppCompatButton btnStart;
    /**
     * ?????????mode??????
     */
    private TextView tvModeMessage;
    /**
     * ??????
     */
    private List<Integer> setTempList;
    /**
     * ??????
     */
    private List<Integer> setTimeList;
    /**
     * ??????checkBox
     */
    private SwitchButton sbOrderSwitch;
    /**
     * ??????????????????
     */
    private SettingBar stbOrder;
    /**
     * ??????????????????dialog
     */
    private IRokiDialog iRokiOrderDialog;
    /**
     * ????????????
     */
    private String orderDate;
    /**
     * ??????????????????dialog
     */
    private DialogType_Time dialogType_time;
    /**
     * ??????
     */
    private LinearLayout llOrder;


    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID()))
            return;

        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause
                || mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Order
                || mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            if (mRokiDialog != null && mRokiDialog.isShow()) {
                mRokiDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }

    }

    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID()))
            return;
        mSteamOvenOne = (AbsSteameOvenOne) event.pojo;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_device_model_selected_610;
    }

    @Override
    protected void initView() {
        setStateBarFixer();
        wheelFront = (LoopView) findViewById(R.id.wheel_view_front);
        llFront2 = (LinearLayout) findViewById(R.id.ll_front_2);
        llRear = (LinearLayout) findViewById(R.id.ll_rear);
        wheelFront2 = (LoopView) findViewById(R.id.wheel_view_front_2);
        wheelRear = (LoopView) findViewById(R.id.wheel_view_rear);
        btnStart = (AppCompatButton) findViewById(R.id.btn_start);
        tvModeMessage = (TextView) findViewById(R.id.tv_mode_message);
        tvTopTemp = (TextView) findViewById(R.id.tv_top_temp);
        sbOrderSwitch = (SwitchButton) findViewById(R.id.sb_order_switch);
        stbOrder = (SettingBar) findViewById(R.id.stb_order);
        llOrder = (LinearLayout) findViewById(R.id.ll_order);
        setOnClickListener(btnStart, stbOrder);
        sbOrderSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton switchButton, boolean b) {
                if (b) {
                    stbOrder.setVisibility(View.VISIBLE);
                    try {
                        orderDate = DateUtil.getCurrentDate1Hours();
                        stbOrder.setLeftText("??????" + orderDate + "???????????????");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    stbOrder.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void initData() {
        Bundle bd = getArguments();
        mDeviceSelectModelList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        mSteamOvenOne = Plat.deviceService.lookupChild(mGuid);
        mTvDeviceModelName.setText(mTitle);

        if ("?????????".equals(mTitle)) {
            modelSelectItem(0);
        } else {
            if ("????????????".equals(mTitle)) {
                llOrder.setVisibility(View.GONE);
            }
            mRecyclerView.setLayoutManager(new GridLayoutManager(cx, 4, RecyclerView.VERTICAL, false));
            mDeviceModelAdapter = new Rv610ModeAdapter(cx);
            mRecyclerView.setAdapter(mDeviceModelAdapter);
            mDeviceModelAdapter.addData(mDeviceSelectModelList);
            mDeviceModelAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    DeviceConfigurationFunctions item = mDeviceModelAdapter.getItem(position);
                    if ("EXP".equals(item.functionName)) {
                        expMode(position);
                    } else {
                        modelSelectItem(position);
                    }
//                    modelSelectItemEvent(view);
                    mDeviceModelAdapter.setSelectPosition(position);
                }
            });
            modelSelectItem(0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSteamOvenOne == null) {
            return;
        }
        switch (mTitle) {
            case "?????????":

                break;
            case "?????????":

                break;
            case "?????????":

                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == btnStart) {
            if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                ToastUtils.showShort(R.string.device_alarm_close_content);
                return;
            }
            stratWork();
        } else if (view == stbOrder) {
            setDeviceOrderRunData();
        }
    }

    /**
     * ????????????
     */
    private void modelSelectItem(int position) {
        llFront2.setVisibility(View.GONE);
        tvTopTemp.setText("??????");
        try {
            orderDate = DateUtil.getCurrentDate1Hours();
            stbOrder.setLeftText("??????" + orderDate + "???????????????");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
            ToastUtils.showShort(R.string.device_alarm_close_content);
            return;
        }
        try {
            if (mDeviceSelectModelList != null && mDeviceSelectModelList.size() > 0) {
                DeviceConfigurationFunctions deviceFunction = mDeviceSelectModelList.get(position);
                functionName = deviceFunction.functionName;
                functionCode = deviceFunction.functionCode;
                ArrayList<DeviceConfigurationFunctions> descList = Lists.newArrayList();
                descList.clear();
                descList.add(deviceFunction);
                String freshSteamedParams = deviceFunction.functionParams;
                SteamOvenModelFunctionParams functionParams = JsonUtils.json2Pojo(freshSteamedParams, SteamOvenModelFunctionParams.class);
                mSteamModel = Short.parseShort(functionParams.getParam().getModel().getValue());
                String tempDefaultValue = functionParams.getParam().getDefaultSetTemp().getValue();
                String timeDefaultValue = functionParams.getParam().getDefaultSetTime().getValue();
                short newDefaultTemp = (Short.parseShort(tempDefaultValue));
                short newDefaultTime = (Short.parseShort(timeDefaultValue));
                setTempList = functionParams.getParam().getSetTemp().getValue();
                setTimeList = functionParams.getParam().getSetTime().getValue();
                //??????????????????????????????
                int indexTemp = newDefaultTemp - setTempList.get(0);

                //??????
                wheelFront.setItems(HelperRikaData.getTempData3(setTempList));
                wheelFront.setInitPosition(indexTemp);
                wheelFront.setCurrentPosition(indexTemp);
                wheelFront.setDividerColor(Color.parseColor("#e1e1e1"));
                //??????
                if (!"??????".equals(functionName)) {
                    int indexTime = newDefaultTime - setTimeList.get(0);
                    wheelRear.setItems(HelperRikaData.getTimeData4(setTimeList));
//                            wheelRear.setInitPosition(indexTime);
                    wheelRear.setCurrentPosition(indexTime);
                    wheelRear.setDividerColor(Color.parseColor("#e1e1e1"));
                    llRear.setVisibility(View.VISIBLE);
                } else {
                    llRear.setVisibility(View.GONE);
                }
                tvModeMessage.setText(deviceFunction.msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????
     *
     * @param postion
     */
    private void expMode(int postion) {
        llFront2.setVisibility(View.VISIBLE);
        tvTopTemp.setText("????????????");
        OvenExpParamBean ovenParams = null;
        try {
            DeviceConfigurationFunctions deviceConfigurationFunctions = mDeviceSelectModelList.get(postion);
            functionName = deviceConfigurationFunctions.functionName;
            functionCode = deviceConfigurationFunctions.functionCode;
            ovenParams = JsonUtils.json2Pojo(mDeviceSelectModelList.get(postion).functionParams, OvenExpParamBean.class);
            if (ovenParams != null) {
                List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
                List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
                List<Integer> time = ovenParams.getParam().getMinute().getValue();
                String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();
                String timeDefault = ovenParams.getParam().getMinuteDefault().getValue();
                mSteamModel = Short.parseShort(ovenParams.getParam().getModel().getValue());
                String tempDiff = ovenParams.getParam().getTempDiff().getValue();
                String tempStart = ovenParams.getParam().getTempStart().getValue();
                String downMin = ovenParams.getParam().getTempMin().getValue();
                String descTips = ovenParams.getParam().getDesc().getValue();

                int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
                int deDiff = Integer.parseInt(tempDiff);
                int deNum2 = deNum1 - deDiff;
                int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
                int deStart = Integer.parseInt(tempStart);
                int min = Integer.parseInt(downMin);

                //??????
                wheelFront.setItems(HelperRikaData.getTempData3(tempUp));
                wheelFront.setInitPosition(deNum1);
                wheelFront.setCurrentPosition(deNum1);
                wheelFront.setDividerColor(Color.parseColor("#e1e1e1"));
                wheelFront.setListenerPosition(new OnItemSelectedListenerPosition() {
                    @Override
                    public void onItemSelectedRear(int position) {
                        LogUtils.i("position", "-----" + position);


                        List<String> tempDataEXPCentener = HelperRikaData.getTempDataEXPCentener(tempDown, HelperRikaData.getTempData3(tempUp).get(position));
                        wheelFront2.setItems(tempDataEXPCentener);
                        wheelFront2.setCurrentPosition(0);
                        if (position >= 20) {
//                            wheelFront2.setItems(HelperRikaData.getTempData3(tempDown));
//                            wheelFront2.setCurrentPosition(position-20);
                        } else {
//                            wheelFront2.setItems(HelperRikaData.getTempData3(tempDown));
//                            wheelFront2.setCurrentPosition(0);
                        }
                    }
                });

                List<String> tempDataEXPCentener = HelperRikaData.getTempDataEXPCentener(tempDown, HelperRikaData.getTempData3(tempUp).get(deNum1));

//                wheelFront2.setItems(HelperRikaData.getTempData3(tempDown));
                wheelFront2.setItems(tempDataEXPCentener);
//                wheelFront2.setInitPosition(deNum2);
//                wheelFront2.setCurrentPosition(deNum2);
                wheelFront2.setDividerColor(Color.parseColor("#e1e1e1"));
//                wheelFront2.setListenerPosition(new OnItemSelectedListenerPosition() {
//                    @Override
//                    public void onItemSelectedRear(int position) {
//                        LogUtils.i("position"  , "-----" + position ) ;
//                        if (position <= HelperRikaData.getTempData3(tempUp).size() - 20){
//                            wheelFront.setItems(HelperRikaData.getTempData3(tempUp));
//                            wheelFront.setCurrentPosition(position + 20);
//                        }else {
//                            wheelFront.setItems(HelperRikaData.getTempData3(tempUp));
//                            wheelFront.setCurrentPosition(HelperRikaData.getTempData3(tempUp).size()-1);
//                        }
//                    }
//                });
                //??????
                wheelRear.setItems(HelperRikaData.getTimeData4(time));
                wheelRear.setInitPosition(deNum3);
                wheelRear.setCurrentPosition(deNum3);
                wheelRear.setDividerColor(Color.parseColor("#e1e1e1"));
                tvModeMessage.setText(descTips);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ??????????????????
     */
    private void setDeviceOrderRunData() {
        dialogType_time = new DialogType_Time(cx);
        try {
            String currentDate1Hours = DateUtil.getCurrentDate1Hours();
            int housPosition = Integer.parseInt(currentDate1Hours.substring(0, 2));
            int minPosition = Integer.parseInt(currentDate1Hours.substring(currentDate1Hours.length() - 2, currentDate1Hours.length()));
            dialogType_time.getmLoopViewFront().setItems(HelperRikaData.getHous());
            dialogType_time.getmLoopViewRear().setItems(HelperRikaData.getMin());
            dialogType_time.show();
            LoopView loopView1 = dialogType_time.getmLoopViewFront();
            LoopView loopView2 = dialogType_time.getmLoopViewRear();

//        loopView1.setInitPosition(housPosition == 23 ? 0 : (housPosition));
            loopView1.setInitPosition(housPosition);
            loopView2.setInitPosition(minPosition);

            loopView1.setListenerRear(new OnItemSelectedListenerRear() {
                @Override
                public void onItemSelectedRear(String contentRear) {
                    String date = contentRear + ":" + loopView2.getItemsContent(loopView2.getSelectedItem());
                    setOrderDate(date);
//                dialogType_time.setDest("??????" + orderDate + "???????????????");
                }
            });
            loopView2.setListenerRear(new OnItemSelectedListenerRear() {
                @Override
                public void onItemSelectedRear(String contentRear) {
                    String date = loopView1.getItemsContent(loopView1.getSelectedItem()) + ":"
                            + contentRear;
                    setOrderDate(date);
//                dialogType_time.setDest("??????" + orderDate + "???????????????");
                }
            });

            dialogType_time.setOnOkClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDate = loopView1.getItemsContent(loopView1.getSelectedItem()) + ":" + loopView2.getItemsContent(loopView2.getSelectedItem());
                    if (DateUtil.compareTime(DateUtil.getCurrentTime(DateUtil.PATTERN), orderDate, DateUtil.PATTERN) == 1) {
                        stbOrder.setLeftText("??????" + orderDate + "???????????????");
                    } else {
                        stbOrder.setLeftText("??????" + orderDate + "???????????????");
                    }
                    dialogType_time.dismiss();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setOrderDate(String orderDate) {
        if (DateUtil.compareTime(DateUtil.getCurrentTime(DateUtil.PATTERN), orderDate, DateUtil.PATTERN) == 1) {
            dialogType_time.setDest("??????" + orderDate + "???????????????");
        } else {
            dialogType_time.setDest("??????" + orderDate + "???????????????");
        }

    }

    /**
     * ????????????
     */
    private void stratWork() {
        final short newTemp2;
        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
            ToastUtils.showShort(R.string.device_alarm_close_content);
            return;
        }
        //??????????????????????????????
        if (mSteamOvenOne.doorStatusValue == 1 && (mSteamModel != 21 && mSteamModel != 19)) {
            ToastUtils.show("??????????????????????????????????????????", Toast.LENGTH_SHORT);
            return;
        }
        if (mSteamModel == 13
                || mSteamModel == 14
                || mSteamModel == 15
                || mSteamModel == 16
                || mSteamModel == 20
                || mSteamModel == 21
                || mSteamModel == 22
                || mSteamModel == 17
                || mSteamModel == 18
        ) {
            if (mSteamOvenOne.WaterStatus == 1) {
                ToastUtils.showShort(R.string.device_alarm_water_out);
                return;
            }
            if (mSteamOvenOne.alarm == 16) {
                ToastUtils.show("????????????????????????", Toast.LENGTH_SHORT);
                return;
            }
        }
        if (mSteamModel == 10) {
            String temp2 = wheelFront2.getItemsContent(wheelFront2.getSelectedItem());
//            String temp2 = itemsFront2.substring(0 , itemsFront2.length()-1);
            newTemp2 = Short.parseShort(temp2);
        } else {
            newTemp2 = 0;
        }
        String temp = wheelFront.getItemsContent(wheelFront.getSelectedItem());
        String time = "";
        if (llRear.getVisibility() == View.VISIBLE) {
            time = wheelRear.getItemsContent(wheelRear.getSelectedItem());
        } else {
            time = "0";
        }
//        String temp = itemsContent.substring(0 , itemsContent.length()-1);
//        String time = itemsContent1.substring(0 , itemsContent1.length()-2);
        LogUtils.i("20180731", " temp:" + temp + " time:" + time);
        final short newTemp = Short.parseShort(temp);
        final short newTime = Short.parseShort(time);
        final int ordertime_min;
        final int ordertime_hour;
        //????????????
        if (sbOrderSwitch.isChecked()) {
            ordertime_min = HelperRikaData.getMinGap(orderDate);
            ordertime_hour = HelperRikaData.getHousGap(orderDate);
        } else {
            ordertime_min = 255;
            ordertime_hour = 255;
        }
        if (mSteamOvenOne != null) {
            ToolUtils.logEvent(mSteamOvenOne.getDt(), "???????????????" + mTitle + "??????????????????:" + functionName + ":" + newTemp + ":" + newTime, "roki_??????");
        }
        assert mSteamOvenOne != null;
        if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Off) {
            mSteamOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    mSteamOvenOne.setSteameOvenOneRunMode((short) mSteamModel, newTime, newTemp, (short) 0, newTemp2, (short) ordertime_min, (short) ordertime_hour, new VoidCallback() {
                        @Override
                        public void onSuccess() {
//                                    UIService.getInstance().popBack();
                            sendMul(functionCode);
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            mSteamOvenOne.setSteameOvenOneRunMode((short) mSteamModel, newTime, newTemp, (short) 0, newTemp2, (short) ordertime_min, (short) ordertime_hour, new VoidCallback() {
                @Override
                public void onSuccess() {
//                            UIService.getInstance().popBack();
                    sendMul(functionCode);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    //????????????
    private void sendMul(String code) {
        if (mSteamModel == 23
                || mSteamModel == 19
                || mSteamModel == 21
                || mSteamModel == 18) {
            return;
        }
        CloudHelper.getReportCode(Plat.accountService.getCurrentUserId(), mSteamOvenOne.getID(), code, mSteamOvenOne.getDc(), new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
                LogUtils.i("202012041055", "sendMul:::");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("202012041055", "sendMul:::" + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialogType_time != null){
            dialogType_time.dismiss();
        }
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }


}
