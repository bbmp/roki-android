package com.robam.roki.ui.page.device.integratedStove;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.IForm;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.roki.R;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerPosition;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.PengpaiZhengParamBean;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
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
public class IntegratedStoveModelPage extends MyBasePage<MainActivity> {

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
    AbsIntegratedStove mIntegratedStove;
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
    private TextView tv_temp2;
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
    private PengpaiZhengParamBean ovenParams;


    /**
     * @param event ?????????????????????
     */
    @Subscribe
    public void onEvent(IntegStoveStatusChangedEvent event) {
        if (mIntegratedStove == null) {
            return;
        }
        mIntegratedStove = event.pojo;
        if (mIntegratedStove.getID().equals(event.pojo.getID())) {
            if (mIntegratedStove.workState != IntegStoveStatus.workState_free && mIntegratedStove.workState != IntegStoveStatus.workState_complete) {
                UIService.getInstance().popBack();
                UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, getArguments());
            }
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_device_model_selected_integrastove;
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
        tv_temp2 = (TextView) findViewById(R.id.tv_temp2);


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
        mIntegratedStove = Plat.deviceService.lookupChild(mGuid);
        mTvDeviceModelName.setText(mTitle);


        mRecyclerView.setLayoutManager(new GridLayoutManager(cx, 4, RecyclerView.VERTICAL, false));
        mDeviceModelAdapter = new Rv610ModeAdapter(cx);
        mRecyclerView.setAdapter(mDeviceModelAdapter);
        mDeviceModelAdapter.addData(mDeviceSelectModelList);
        mDeviceModelAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                DeviceConfigurationFunctions item = mDeviceModelAdapter.getItem(position);
                if (SteamOvenModeEnum.catchMessage(item.functionName) == SteamOvenModeEnum.ZHIKONGZHENG) {
                    expMode(position);
                } else if ("EXP".equals(item.functionName)) {
                    expMode(position);
                } else {
                    modelSelectItem(position);
                }
//                    modelSelectItemEvent(view);
                mDeviceModelAdapter.setSelectPosition(position);
            }
        });
        if (SteamOvenModeEnum.catchMessage(mDeviceModelAdapter.getItem(0).functionName) == SteamOvenModeEnum.ZHIKONGZHENG) {
            expMode(0);
        } else {
            modelSelectItem(0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIntegratedStove == null) {
            return;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == btnStart) {
//            if (mIntegratedStove.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
//                ToastUtils.showShort(R.string.device_alarm_close_content);
//                return;
//            }
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
//        if (mIntegratedStove.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
//            ToastUtils.showShort(R.string.device_alarm_close_content);
//            return;
//        }
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
        tvTopTemp.setText("?????????");
        ovenParams = null;
        try {
            DeviceConfigurationFunctions deviceConfigurationFunctions = mDeviceSelectModelList.get(postion);
            functionName = deviceConfigurationFunctions.functionName;
            functionCode = deviceConfigurationFunctions.functionCode;
            ovenParams = JsonUtils.json2Pojo(mDeviceSelectModelList.get(postion).functionParams, PengpaiZhengParamBean.class);
            if (ovenParams != null) {
                List<Integer> tempUp = ovenParams.param.setTemp.value;
                List<String> tempDown = ovenParams.param.setSteam.title;
                List<Integer> time = ovenParams.param.setTime.value;


                String stam = ovenParams.param.defaultSetSteam.value;
                String tempUpDefault = ovenParams.param.defaultSetTemp.value;
                String timeDefault = ovenParams.param.defaultSetTime.value;
                mSteamModel = Short.parseShort(ovenParams.param.model.value);

//                String tempDiff = ovenParams.getParam().getTempDiff().getValue();
//                String tempStart = ovenParams.getParam().getTempStart().getValue();
//                String downMin = ovenParams.getParam().getTempMin().getValue();
//                String descTips = ovenParams.getParam().getDesc().getValue();
//
                int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
//                int deDiff = Integer.parseInt(tempDiff);1
                int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
//                int deStart = Integer.parseInt(tempStart);
//                int min = Integer.parseInt(downMin);

                //??????
                wheelFront2.setItems(HelperRikaData.getTempData3(tempUp));
                wheelFront2.setInitPosition(deNum1);
                wheelFront2.setCurrentPosition(deNum1);
                wheelFront2.setDividerColor(Color.parseColor("#e1e1e1"));
                wheelFront2.setListenerPosition(new OnItemSelectedListenerPosition() {
                    @Override
                    public void onItemSelectedRear(int position) {

                    }
                });

                wheelFront.setItems(tempDown);
                wheelFront.setInitPosition(1);
                wheelFront.setDividerColor(Color.parseColor("#e1e1e1"));

                //??????
                wheelRear.setItems(HelperRikaData.getTimeData4(time));
                wheelRear.setInitPosition(deNum3);
//                wheelRear.setCurrentPosition(deNum3);
                wheelRear.setDividerColor(Color.parseColor("#e1e1e1"));
//                tvModeMessage.setText(descTips);
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

            loopView1.setInitPosition(housPosition);
            loopView2.setInitPosition(minPosition);

            loopView1.setListenerRear(new OnItemSelectedListenerRear() {
                @Override
                public void onItemSelectedRear(String contentRear) {
                    String date = contentRear + ":" + loopView2.getItemsContent(loopView2.getSelectedItem());
                    setOrderDate(date);
                }
            });
            loopView2.setListenerRear(new OnItemSelectedListenerRear() {
                @Override
                public void onItemSelectedRear(String contentRear) {
                    String date = loopView1.getItemsContent(loopView1.getSelectedItem()) + ":"
                            + contentRear;
                    setOrderDate(date);
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
        SteamOvenFaultEnum faultEnum = SteamOvenFaultEnum.match(mIntegratedStove.faultCode);
        if (SteamOvenFaultEnum.NO_FAULT != faultEnum) {
            if (faultEnum != null) {
                ToastUtils.showShort(SteamOvenFaultEnum.match(mIntegratedStove.faultCode).getValue());
            }else {
                ToastUtils.showShort("??????????????????????????????????????????");
            }
            return;
        }
        if (SteamOvenHelper.isWork(mIntegratedStove.workState)) {
            ToastUtils.showShort("???????????????");
            return;
        }
        //???????????? ????????????????????????
        if (!SteamOvenHelper.isDoorState(mIntegratedStove.doorState) && !SteamOvenHelper.isOpenDoorWork(SteamOvenModeEnum.match(mSteamModel)) ){
            ToastUtils.showShort("??????????????????????????????????????????");
            return;
        }

        /**
         * ?????????????????????
         */
        if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(mSteamModel))) {
            if (SteamOvenHelper.isDescale(mIntegratedStove.descaleFlag)) {
                ToastUtils.showShort("??????????????????????????????????????????????????????");
                return;
            }
            if (!SteamOvenHelper.isWaterBoxState(mIntegratedStove.waterBoxState)) {
                ToastUtils.showShort("???????????????????????????????????????");
                return;
            }
            if (!SteamOvenHelper.isWaterLevelState(mIntegratedStove.waterLevelState)) {
                ToastUtils.showShort("????????????????????????");
                return;
            }
        }
        //= wheelFront.getItemsContent(wheelFront.getSelectedItem());
        String temp;
        short steam = 0;
        SteamOvenModeEnum match = SteamOvenModeEnum.match(mSteamModel);
        if (match == SteamOvenModeEnum.ZHIKONGZHENG) {
            temp = wheelFront2.getItemsContent(wheelFront2.getSelectedItem());
            String s = ovenParams.param.setSteam.value.get(wheelFront.getSelectedItem());
            steam = Short.parseShort(s);
        } else {
            temp = wheelFront.getItemsContent(wheelFront.getSelectedItem());
        }


        String time = "";
        if (llRear.getVisibility() == View.VISIBLE) {
            time = wheelRear.getItemsContent(wheelRear.getSelectedItem());
        } else {
            time = "0";
        }
        LogUtils.i("20180731", " temp:" + temp + " time:" + time);
        final short newTemp = Short.parseShort(temp);
        final short newTime = Short.parseShort(time);
        final int ordertime_min;
        final int ordertime_hour;
        //????????????
//        if (sbOrderSwitch.isChecked()) {
//            ordertime_min = HelperRikaData.getMinGap(orderDate);
//            ordertime_hour = HelperRikaData.getHousGap(orderDate);
//        } else {
//            ordertime_min = 255;
//            ordertime_hour = 255;
//        }
        assert mIntegratedStove != null;
        mIntegratedStove.setSteameOvenOneRunMode((short) mSteamModel, newTime, newTemp, (short) 0, (short) steam, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("sendNew", "??????");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }


}
