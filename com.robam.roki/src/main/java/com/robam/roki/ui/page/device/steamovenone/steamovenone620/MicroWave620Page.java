package com.robam.roki.ui.page.device.steamovenone.steamovenone620;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.collect.Lists;
import com.robam.widget.layout.SettingBar;
import com.robam.widget.view.SwitchButton;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.robam.common.pojos.Tag;
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.listener.OnItemSelectedListenerPosition;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.SteamOvenModelFunction620Params;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter3.Rv610ModeAdapter;
import com.robam.roki.ui.dialog.type.DialogType_Time;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.wheelview.LoopView;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.TestDatas;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MicroWave620Page extends MyBasePage<MainActivity> {


    @InjectView(R.id.iv_micro_back)
    ImageView mIvBack;

    @InjectView(R.id.recyclerView_micro)
    RecyclerView mRVMicro;
    /**
     * 功率
     */
    private LoopView wheel1;
    /**
     * 功率
     */
    private LoopView wheel2;
    /**
     * 设置状态栏占位
     */

    private LoopView wheel3;


   private String mTitle;

    private TextView mTvDeviceModelName;
   private AbsSteameOvenOneNew mSteamOvenOne;
    /**
     * 功率
     */
    private List<Integer> mDataList1;
    /**
     * 时间
     */
    private List<Integer> mDataList2;
    /**
     * 时间
     */

    private LinearLayout llMin;

    private TextView tvModeMessage;

    private List<Integer> mDataList3;
    /**
     * 选择预约时间dialog
     */
    private DialogType_Time dialogType_time;

    private List<DeviceConfigurationFunctions> mDeviceSelectModelList;
    private int mSteamModel;
    private String mGuid;
    String functionName;
    String functionCode;
    private Rv610ModeAdapter mDeviceModelAdapter;
    protected void setStateBarFixer(){
        View mStateBarFixer = findViewById(R.id.view_status_bar_fix_mirco);
        if (mStateBarFixer != null){
            mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(getActivity())));
            mStateBarFixer.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
    /**
     * 预约
     */
    private LinearLayout llOrderWeight;

    private TextView txtPower;

    private TextView txtWeight;

    private TextView txtWeightUnit;


    private TextView txtPowerUnit;

    private TextView textTime;
    /**
     * 预约checkBox
     */
    private SwitchButton sbOrderSwitch;

    /**
     * 预约选择时间
     */
    private SettingBar stbOrder;

    private String orderDate;

    @Override
    protected int getLayoutId() {
        return R.layout.micro_wave_page;
    }

    @Override
    protected void initView() {
        setStateBarFixer();
        wheel1=findViewById(R.id.wheel_view_1);

        wheel2=findViewById(R.id.wheel_view_2);

        wheel3=findViewById(R.id.wheel_view_3);
        llOrderWeight=findViewById(R.id.ll_order_micro);

        mRVMicro=findViewById(R.id.recyclerView_micro);
        mTvDeviceModelName=findViewById(R.id.tv_device_model_name);
        txtPower=findViewById(R.id.tv_top_power_micro);
        textTime=findViewById(R.id.tv_top_power_time);
        txtWeight=findViewById(R.id.tv_top_weight);

        txtWeightUnit=findViewById(R.id.tv_top_weight_unit);
        tvModeMessage=findViewById(R.id.tv_mode_message);


        txtPowerUnit=findViewById(R.id.tv_top_power_micro_unit);
        llMin=findViewById(R.id.ll_min);

        sbOrderSwitch=findViewById(R.id.sb_order_switch_micro);


        stbOrder=findViewById(R.id.stb_order_micro);
        stbOrder.setOnClickListener(v -> setDeviceOrderRunData());
        mDataList1=new ArrayList<>();
        mDataList2=new ArrayList<>();

        mDataList3=new ArrayList<>();


        sbOrderSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton switchButton, boolean b) {
                if (b) {
                    stbOrder.setVisibility(View.VISIBLE);
                    try {
                        orderDate = DateUtil.getCurrentDate1Hours();
                        stbOrder.setLeftText("今天" + orderDate + "分开始烹饪");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    stbOrder.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(v -> {
//                startWork(mSteamModel);
            startWork();
        });

        findViewById(R.id.iv_micro_back).setOnClickListener(v -> UIService.getInstance().popBack());

    }
    private void setDeviceOrderRunData() {
        dialogType_time = new DialogType_Time(cx);
        try {
            String currentDate1Hours = DateUtil.getCurrentDate1Hours();
            int housPosition = Integer.parseInt(currentDate1Hours.substring(0, 2));
            int minPosition = Integer.parseInt(currentDate1Hours.substring(currentDate1Hours.length() - 2));
            dialogType_time.getmLoopViewFront().setItems(HelperRikaData.getTimeData4(functionParams.appoint.setHour.value));
            dialogType_time.getmLoopViewRear().setItems(HelperRikaData.getTimeData4(functionParams.appoint.setMin.value));
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
//                dialogType_time.setDest("今天" + orderDate + "分开始烹饪");
                }
            });
            loopView2.setListenerRear(new OnItemSelectedListenerRear() {
                @Override
                public void onItemSelectedRear(String contentRear) {
                    String date = loopView1.getItemsContent(loopView1.getSelectedItem()) + ":"
                            + contentRear;
                    setOrderDate(date);
//                   dialogType_time.setDest("今天" + orderDate + "分开始烹饪");
                }
            });

            dialogType_time.setOnOkClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDate = loopView1.getItemsContent(loopView1.getSelectedItem()) + ":" + loopView2.getItemsContent(loopView2.getSelectedItem());
                    if (DateUtil.compareTime(DateUtil.getCurrentTime(DateUtil.PATTERN), orderDate, DateUtil.PATTERN) == 1) {
                        stbOrder.setLeftText("明天" + orderDate + "分开始烹饪");
                    } else {
                        stbOrder.setLeftText("今天" + orderDate + "分开始烹饪");
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
            dialogType_time.setDest("明天" + orderDate + "分开始烹饪");
        } else {
            dialogType_time.setDest("今天" + orderDate + "分开始烹饪");
        }

    }
     int ordertime_min;
     int ordertime_hour;

    private void startWork() {


        SteamOvenFaultEnum faultEnum = SteamOvenFaultEnum.match(mSteamOvenOne.faultCode);
        if (SteamOvenFaultEnum.NO_FAULT != faultEnum&&mSteamOvenOne.faultCode!=11) {
            if (faultEnum != null) {
                com.hjq.toast.ToastUtils.show(SteamOvenFaultEnum.match(mSteamOvenOne.faultCode).getValue());
            }else {
                com.hjq.toast.ToastUtils.show("设备端故障未处理，请及时处理");
            }
//            return;
        }
        if (SteamOvenHelper.isWork(mSteamOvenOne.workState)) {
            com.hjq.toast.ToastUtils.show("设备已占用");
            return;
        }
        //门已打开 而且不能开门工作
        if (!SteamOvenHelper.isDoorState(mSteamOvenOne.doorState) && !SteamOvenHelper.isOpenDoorWork(SteamOvenModeEnum.match(mSteamModel)) ){
            com.hjq.toast.ToastUtils.show("门未关好，请检查并确认关好门");
            return;
        }

        /**
         * 判断是否需要水
         */
        if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(mSteamModel))) {
            if (SteamOvenHelper.isDescale(mSteamOvenOne.descaleFlag)) {
                com.hjq.toast.ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");
                return;
            }
            if (!SteamOvenHelper.isWaterBoxState(mSteamOvenOne.waterBoxState)) {
                com.hjq.toast.ToastUtils.show("水箱已弹出，请检查水箱状态");
                return;
            }
//            if (!SteamOvenHelper.isWaterLevelState(mSteamOvenOneNew.waterLevelState)) {
//                com.hjq.toast.ToastUtils.show("水箱缺水，请加水");
//                return;
//            }
        }


        if (sbOrderSwitch.isChecked()) {
            ordertime_min = HelperRikaData.getMinGap620(orderDate+":00");
            ordertime_hour = HelperRikaData.getHousGap(orderDate);
        } else {
            ordertime_min = 0;
            ordertime_hour = 0;
        }
        Log.e("时间差",(ordertime_hour*60+ordertime_min)*60+"----");




        if (position==0){
            mSteamOvenOne.setMicroRunModel((short) mSteamModel, defaultmin * 60 + defaultSec, newDefaultPower
                    ,(ordertime_hour*60+ordertime_min)*60,new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            UIService.getInstance().popBack();
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
        }else{
            mSteamOvenOne.setReMicroRunModel((short) mSteamModel, newDefaultPower, defaultWeight, (ordertime_hour * 60 + ordertime_min) * 60, new VoidCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });


        }


//        List<Integer> tempTure = TestDatas.createModeDataTemp(tempList);
//        List<Integer> timeTemp = TestDatas.createModeDataTime(timeList);

//        setDialogParam(tempTure, timeTemp, defaultTemp, defaultTime, desc, code, timeList.get(2));

    }

    @Override
    protected void initData() {

        Bundle bd = getArguments();
        mDeviceSelectModelList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        mSteamOvenOne = Plat.deviceService.lookupChild(mGuid);
        mTvDeviceModelName.setText(mTitle);


        mRVMicro.setLayoutManager(new GridLayoutManager(cx, 4, RecyclerView.VERTICAL, false));
        mDeviceModelAdapter = new Rv610ModeAdapter();
        mRVMicro.setAdapter(mDeviceModelAdapter);
        mDeviceModelAdapter.addData(mDeviceSelectModelList);
        modelSelectItem(0);
        mDeviceModelAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int position) {
                DeviceConfigurationFunctions item = mDeviceModelAdapter.getItem(position);
                modelSelectItem(position);

                mDeviceModelAdapter.setSelectPosition(position);
            }
        });
    }
    short newDefaultPower;

    short defaultmin;
    short defaultSec;

    short defaultWeight;
    private int position=0;
    private static final String TAG = "MicroWave620Page";

    private  SteamOvenModelFunction620Params functionParams;
    private void modelSelectItem(int position) {
        txtPower.setText("功率");
        txtPowerUnit.setText("w");

        this.position=position;

//        DeviceConfigurationFunctions mDeviceConfigurationFunctions=mDeviceSelectModelList.get(position);
        try {

            if (mDeviceSelectModelList != null && mDeviceSelectModelList.size() > 0) {
                DeviceConfigurationFunctions deviceFunction = mDeviceSelectModelList.get(position);
                functionName = deviceFunction.functionName;
                functionCode = deviceFunction.functionCode;
                ArrayList<DeviceConfigurationFunctions> descList = Lists.newArrayList();
                descList.clear();
                descList.add(deviceFunction);
                String freshSteamedParams = deviceFunction.functionParams;
                Log.e(TAG, freshSteamedParams + "----");
                functionParams = JsonUtils.json2Pojo(freshSteamedParams, SteamOvenModelFunction620Params.class);
                mSteamModel = Short.parseShort(functionParams.param.model.value);
                mDataList1 = functionParams.param.setPowerOrLevel.value;
                wheel1.setItems(HelperRikaData.getTimeData4(mDataList1));


                 String defaultPower = functionParams.param.defaultSetPowerOrLevel.value;
                 newDefaultPower = (Short.parseShort(defaultPower));
                int indexTemp = newDefaultPower - mDataList1.get(0);
                wheel1.setInitPosition(indexTemp);
                wheel1.setCurrentPosition(indexTemp);
                wheel1.setDividerColor(Color.parseColor("#e1e1e1"));
                wheel1.setListenerPosition(position1 -> {

                    Log.e(TAG,position1+"----");
                });
                if ("单微".equals(functionName)) {

                    mDataList2 = functionParams.param.setTime.value;
                    mDataList3 = functionParams.param.setMinTime.value;
                    defaultPower = functionParams.param.defaultSetTime.value;
                    defaultmin = (Short.parseShort(defaultPower));
                    indexTemp = defaultmin - mDataList2.get(0);
                    wheel2.setInitPosition(indexTemp);
                    wheel2.setCurrentPosition(indexTemp);
                    wheel2.setItems(HelperRikaData.getTimeData4(mDataList2));
                    wheel2.setDividerColor(Color.parseColor("#e1e1e1"));
                    wheel2.setListenerPosition(position1 -> {
                        Log.e(TAG,position1+"----mini");
                    });



                    defaultPower = functionParams.param.defaultSetMinTime.value;
                    defaultSec = (Short.parseShort(defaultPower));
                    indexTemp = defaultSec - mDataList3.get(0);
                    wheel3.setInitPosition(indexTemp);
                    wheel3.setCurrentPosition(indexTemp);

                    wheel3.setItems(HelperRikaData.getTimeData4(mDataList3));
                    wheel3.setDividerColor(Color.parseColor("#e1e1e1"));
                    wheel3.setListenerPosition(position1 -> {
                        Log.e(TAG,position1+"---- sec");
                    });
                    txtWeight.setText("时间");
                    txtWeightUnit.setText("s");
                    llMin.setVisibility(View.VISIBLE);
                } else {

                    txtWeight.setText("重量");
                    txtWeightUnit.setText("g");
                    mDataList3 = functionParams.param.setTime.value;
                    defaultPower = functionParams.param.defaultSetTime.value;
                    defaultWeight = (Short.parseShort(defaultPower));
                    indexTemp = defaultWeight - mDataList2.get(0);
                    wheel2.setInitPosition(indexTemp);
                    wheel2.setCurrentPosition(indexTemp);

                    wheel2.setItems(HelperRikaData.getTimeData4(mDataList3));
                    wheel2.setDividerColor(Color.parseColor("#e1e1e1"));
                    llMin.setVisibility(View.GONE);
                    wheel2.setListenerPosition(position1 -> {
                        Log.e(TAG,position1+"---- 重量");
                    });
                }

                tvModeMessage.setText(deviceFunction.msg);


            }

        }catch(Exception exception){
        exception.printStackTrace();
        }
    }
}
