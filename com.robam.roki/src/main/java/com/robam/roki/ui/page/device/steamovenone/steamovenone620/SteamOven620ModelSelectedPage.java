package com.robam.roki.ui.page.device.steamovenone.steamovenone620;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.robam.widget.layout.SettingBar;
import com.robam.widget.view.SwitchButton;
import com.j256.ormlite.stmt.query.In;
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
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
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
import com.robam.roki.ui.form.SteamOvenCookCurveActivity;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.wheelview.LoopView;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/6.
 */
public class SteamOven620ModelSelectedPage extends MyBasePage<MainActivity> {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    String needDescalingParams;
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
    AbsSteameOvenOneNew mSteamOvenOne;
    /**
     * 温度
     */
    private LoopView wheelFront;
    /**
     * exp温度标题
     */
    private TextView tvTopTemp;
    /**
     * exp下管布局
     */
    private LinearLayout llFront2;
    private LinearLayout llRear;
    /**
     * 温度2
     */
    private LoopView wheelFront2;
    /**
     * 时间
     */
    private LoopView wheelRear;
    /**
     * 开始
     */
    private AppCompatButton btnStart;
    /**
     * 选择的mode描述
     */
    private TextView tvModeMessage;
    /**
     * 温度
     */
    private List<Integer> setTempList;
    /**
     * 时间
     */
    private List<Integer> setTimeList;

    private List<String> setSteamList;

    private List<Integer> setSteamValueList;
    /**
     * 预约checkBox
     */
    private SwitchButton sbOrderSwitch;
    /**
     * 预约选择时间
     */
    private SettingBar stbOrder;
    /**
     * 设置预约时间dialog
     */
    private IRokiDialog iRokiOrderDialog;
    /**
     * 预约时间
     */
    private String orderDate;
    /**
     * 选择预约时间dialog
     */
    private DialogType_Time dialogType_time;
    /**
     * 预约
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
//
//    @Subscribe
//    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
//        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID()))
//            return;
//        mSteamOvenOne = (AbsSteameOvenOneNew) event.pojo;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_device_model_selected_610;
    }

    @Override
    protected void initView() {
        setStateBarFixer();
        wheelFront = findViewById(R.id.wheel_view_front);
        llFront2 = findViewById(R.id.ll_front_2);
        llRear = findViewById(R.id.ll_rear);
        wheelFront2 = findViewById(R.id.wheel_view_front_2);
        wheelRear = findViewById(R.id.wheel_view_rear);
        btnStart = findViewById(R.id.btn_start);
        tvModeMessage = findViewById(R.id.tv_mode_message);
        tvTopTemp = findViewById(R.id.tv_top_temp);
        sbOrderSwitch = findViewById(R.id.sb_order_switch);
        stbOrder = findViewById(R.id.stb_order);
        llOrder = findViewById(R.id.ll_order);
        setOnClickListener(btnStart, stbOrder);
        sbOrderSwitch.setOnCheckedChangeListener((switchButton, b) -> {
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

        needDescalingParams=bd.getString(PageArgumentKey.descaling);

        if (!"空气炸".equals(mTitle)) {
            if ("辅助模式".equals(mTitle)) {
                llOrder.setVisibility(View.GONE);
            }
            mRecyclerView.setLayoutManager(new GridLayoutManager(cx, 4, RecyclerView.VERTICAL, false));
            mDeviceModelAdapter = new Rv610ModeAdapter();
            mRecyclerView.setAdapter(mDeviceModelAdapter);
            mDeviceModelAdapter.addData(mDeviceSelectModelList);
            mDeviceModelAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    DeviceConfigurationFunctions item = mDeviceModelAdapter.getItem(position);
                    if ("EXPFunction".equals(item.functionCode)) {
                        expMode(position);
                    } else {
                        modelSelectItem(position);
                    }
                    mDeviceModelAdapter.setSelectPosition(position);
                }
            });

        } modelSelectItem(0);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mSteamOvenOne == null) {
            return;
        }
        switch (mTitle) {
            case "蒸模式":
                MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), mSteamOvenOne.getDt() + ":蒸模式页", null);
                break;
            case "烤模式":
                MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), mSteamOvenOne.getDt() + ":烤模式页", null);
                break;
            case "空气炸":
                MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), mSteamOvenOne.getDt() + ":模式页", null);
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == btnStart) {
            startWork();
        } else if (view == stbOrder) {
            setDeviceOrderRunData();
        }
    }

    /**
     * 模式选择
     */
    private void modelSelectItem(int position) {
        llFront2.setVisibility(View.GONE);
        tvTopTemp.setText("温度");
        try {
            orderDate = DateUtil.getCurrentDate1Hours();
            stbOrder.setLeftText("今天" + orderDate + "分开始烹饪");
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
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
                findViewById(R.id.tv_steam_unit).setVisibility(View.VISIBLE);
                SteamOvenModelFunctionParams functionParams = JsonUtils.json2Pojo(freshSteamedParams, SteamOvenModelFunctionParams.class);
                mSteamModel = Short.parseShort(functionParams.getParam().getModel().getValue());
                if (functionParams.getParam().getDefaultSetSteam()!=null&&functionParams.getParam().getDefaultSetTemp()!=null) {
                    String tempDefaultValue = functionParams.getParam().getDefaultSetTemp().getValue();
                    String timeDefaultValue = functionParams.getParam().getDefaultSetTime().getValue();
                    short newDefaultTemp = (Short.parseShort(tempDefaultValue));
                    short newDefaultTime = (Short.parseShort(timeDefaultValue));
                    setTempList = functionParams.getParam().getSetTemp().getValue();
                    setTimeList = functionParams.getParam().getSetTime().getValue();
                    String steamDefaultValue=functionParams.getParam().getDefaultSetSteam().getValue();
                    setSteamList=functionParams.getParam().getSetSteam().getTitle();
                    setSteamValueList=functionParams.getParam().getSetSteam().getValue();
                    tvTopTemp.setText("蒸汽量");
                    llFront2.setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.ll_add)).setText("温度");
                    findViewById(R.id.tv_steam_unit).setVisibility(View.INVISIBLE);
                    int indexSteam=0;
                    for (int i = 0; i < setSteamList.size(); i++) {
                        if (steamDefaultValue.equals(setSteamList.get(i))){
                            indexSteam=i;
                        }
                    }
                    wheelFront.setItems(setSteamList);
                    wheelFront.setInitPosition(indexSteam);
                    wheelFront.setCurrentPosition(indexSteam);
                    wheelFront.setDividerColor(Color.parseColor("#e1e1e1"));


                    //拿到时间温度的索引值
                    int indexTemp = newDefaultTemp - setTempList.get(0);
                    wheelFront2.setItems(HelperRikaData.getTempData3(setTempList));
                    wheelFront2.setInitPosition(indexTemp);
                    wheelFront2.setCurrentPosition(indexTemp);
                    wheelFront2.setDividerColor(Color.parseColor("#e1e1e1"));



                    int indexTime = newDefaultTime - setTimeList.get(0);
                    wheelRear.setItems(HelperRikaData.getTimeData4(setTimeList));
                    wheelRear.setInitPosition(indexTime);
                    wheelRear.setCurrentPosition(indexTime);
                    wheelRear.setDividerColor(Color.parseColor("#e1e1e1"));
                    llRear.setVisibility(View.VISIBLE);
                } else {
                    String tempDefaultValue = functionParams.getParam().getDefaultSetTemp().getValue();
                    String timeDefaultValue = functionParams.getParam().getDefaultSetTime().getValue();
                    short newDefaultTemp = (Short.parseShort(tempDefaultValue));
                    short newDefaultTime = (Short.parseShort(timeDefaultValue));
                    setTempList = functionParams.getParam().getSetTemp().getValue();
                    setTimeList = functionParams.getParam().getSetTime().getValue();
                    //拿到时间温度的索引值
                    int indexTemp = newDefaultTemp - setTempList.get(0);

                    //温度
                    wheelFront.setItems(HelperRikaData.getTempData3(setTempList));
                    wheelFront.setInitPosition(indexTemp);
                    wheelFront.setCurrentPosition(indexTemp);
                    wheelFront.setDividerColor(Color.parseColor("#e1e1e1"));


                    //时间
                    if (!"除垢".equals(functionName)) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 专业模式
     *
     * @param postion
     */
    private void expMode(int postion) {
        llFront2.setVisibility(View.VISIBLE);
        tvTopTemp.setText("上温度");
        OvenExpParamBean ovenParams = null;
        try {
            DeviceConfigurationFunctions deviceConfigurationFunctions = mDeviceSelectModelList.get(postion);
            functionName = deviceConfigurationFunctions.functionName;
            functionCode = deviceConfigurationFunctions.functionCode;
            ovenParams = JsonUtils.json2Pojo(mDeviceSelectModelList.get(postion).functionParams, OvenExpParamBean.class);
            if (ovenParams != null) {
                List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
                List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
                List<Integer> time = ovenParams.getParam().getSetTime().getValue();
                String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();
                String timeDefault = ovenParams.getParam().getDefaultSetTime().getValue();
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

                //温度
                wheelFront.setItems(HelperRikaData.getTempData3(tempUp));
                wheelFront.setInitPosition(deNum1);
                wheelFront.setCurrentPosition(deNum1);
                wheelFront.setDividerColor(Color.parseColor("#e1e1e1"));
                wheelFront.setListenerPosition(new OnItemSelectedListenerPosition() {
                    @Override
                    public void onItemSelectedRear(int position) {
                        LogUtils.i("position"  , "-----" + position+"xia"+wheelFront.getItemsContent(position) ) ;

                        if (HelperRikaData.getTempData3(tempUp).size()>position) {
                            List<String> tempDataEXPCentener =
                                    HelperRikaData.getTempDataEXPCentener(tempDown, HelperRikaData.getTempData3(tempUp).get(position));
                            wheelFront2.setItems(tempDataEXPCentener);
                        }
                        wheelFront2.setCurrentPosition(0);
                        if (position >= 20) {
//                            wheelFront2.setItems(HelperRikaData.getTempData3(tempDown));
//                            wheelFront2.setCurrentPosition(position-20);
                        } else {
//                            wheelFront2.setItems(HelperRikaData.getTempData3(tempDown));
//                            wheelFront2.setCurrentPosition(0);
                        }
                    }
                });//Attempt to invoke virtual method 'java.util.List com.robam.roki.model.bean.OvenExpParamBean$ParamBean$MinuteBean.getValue()' on a null object reference

                List<String> tempDataEXPCentener = HelperRikaData.getTempDataEXPCentener(tempDown, HelperRikaData.getTempData3(tempUp).get(deNum1));

                wheelFront2.setItems(HelperRikaData.getTempData3(tempDown));
                wheelFront2.setItems(tempDataEXPCentener);
                wheelFront2.setInitPosition(deNum2);
                wheelFront2.setCurrentPosition(deNum2);
                wheelFront2.setDividerColor(Color.parseColor("#e1e1e1"));
                wheelFront2.setListenerPosition(new OnItemSelectedListenerPosition() {
                    @Override
                    public void onItemSelectedRear(int position) {
                        LogUtils.i("position"  , "-----" + position+"shang"+wheelFront2.getItemsContent(position) ) ;
                        if (wheelFront.getSelectedItem()+20>wheelFront2.getSelectedItem()||wheelFront.getSelectedItem()-20<wheelFront2.getSelectedItem()){

                        }else {
                            wheelFront2.setItems(tempDataEXPCentener);
                            wheelFront2.setDividerColor(Color.parseColor("#e1e1e1"));
                            if (position <= HelperRikaData.getTempData3(tempUp).size() - 20) {
                                wheelFront.setItems(HelperRikaData.getTempData3(tempUp));
                                wheelFront.setCurrentPosition(position + 40);
                            } else {
                                wheelFront.setItems(HelperRikaData.getTempData3(tempUp));
                                wheelFront.setCurrentPosition(HelperRikaData.getTempData3(tempUp).size() - 1);
                            }
                        }
                    }
                });
                //时间
                wheelRear.setItems(HelperRikaData.getTimeData4(time));
                wheelRear.setInitPosition(deNum3);
                wheelRear.setCurrentPosition(deNum3);
                wheelRear.setDividerColor(Color.parseColor("#e1e1e1"));
                wheelRear.setListenerPosition(new OnItemSelectedListenerPosition() {
                    @Override
                    public void onItemSelectedRear(int position) {
                        LogUtils.i("position"  , "-----" + position+"时间"+wheelRear.getItemsContent(position) ) ;
                    }
                });
                tvModeMessage.setText(descTips);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置预约时间
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
//                dialogType_time.setDest("今天" + orderDate + "分开始烹饪");
                }
            });
            loopView2.setListenerRear(new OnItemSelectedListenerRear() {
                @Override
                public void onItemSelectedRear(String contentRear) {
                    String date = loopView1.getItemsContent(loopView1.getSelectedItem()) + ":"
                            + contentRear;
                    setOrderDate(date);
//                dialogType_time.setDest("今天" + orderDate + "分开始烹饪");
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

    IRokiDialog dialogByType;
    private void descalingDialog() {
        if (dialogByType == null) {
            dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        }
        if (dialogByType.isShow()) {
            return;
        }
        String descalingTitle = null;
        String descalingContent = null;
        String descalingButton = null;
        try {
            if (!"".equals(needDescalingParams)) {
                JSONObject jsonObject = new JSONObject(needDescalingParams);
                JSONObject needDescaling = jsonObject.getJSONObject("needDescaling");
                descalingTitle = needDescaling.getString("title");
                descalingContent = needDescaling.getString("content");
                descalingButton = needDescaling.getString("positiveButton");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialogByType.setTitleText(descalingTitle);
        dialogByType.setContentText(descalingContent);
        final IRokiDialog finalDialogByType = dialogByType;
        dialogByType.setOkBtn(descalingButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalDialogByType != null) {
                    finalDialogByType.dismiss();
                }
            }
        });
        dialogByType.show();
    }
    private void setOrderDate(String orderDate) {
        if (DateUtil.compareTime(DateUtil.getCurrentTime(DateUtil.PATTERN_TIME), orderDate+":00", DateUtil.PATTERN_TIME) == 1) {
            dialogType_time.setDest("明天" + orderDate + "分开始烹饪");
        } else {
            dialogType_time.setDest("今天" + orderDate + "分开始烹饪");
        }

    }
    private boolean WorkBeforeCheck(int mSteamModel){
        SteamOvenFaultEnum faultEnum = SteamOvenFaultEnum.match(mSteamOvenOne.faultCode);


        if (SteamOvenHelper.isMicro(SteamOvenModeEnum.match(mSteamModel))){
            if (mSteamOvenOne.faultCode==16||mSteamOvenOne.faultCode==17){
                ToastUtils.showShort("设备端故障未处理，请及时处理");
                return false;
            }
        }

//        if (SteamOvenFaultEnum.NO_FAULT != faultEnum) {
//            if (faultEnum != null) {
//                ToastUtils.showShort(SteamOvenFaultEnum.match(mSteamOvenOne.faultCode).getValue());
//            }else {
//                ToastUtils.showShort("设备端故障未处理，请及时处理");
//            }
//            return false;
//        }
        if (SteamOvenHelper.isWork(mSteamOvenOne.workState)) {
            ToastUtils.showShort("设备已占用");
            return false;
        }
        //门已打开 而且不能开门工作
        if (!SteamOvenHelper.isDoorState(mSteamOvenOne.doorState) && !SteamOvenHelper.isOpenDoorWork(SteamOvenModeEnum.match(mSteamModel)) ){
            com.hjq.toast.ToastUtils.show("门未关好，请检查并确认关好门");
            return false;
        }

        /**
         * 判断是否需要水
         */
        if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(mSteamModel))) {
            if (SteamOvenHelper.isDescale(mSteamOvenOne.descaleFlag)) {
//                com.hjq.toast.ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");

                descalingDialog();
                return false;
            }
            if (!SteamOvenHelper.isWaterBoxState(mSteamOvenOne.waterBoxState)) {
                com.hjq.toast.ToastUtils.show("水箱已弹出，请检查水箱状态");
                return false;
            }
            if (!SteamOvenHelper.isWaterLevelState(mSteamOvenOne.waterLevelState)) {
                com.hjq.toast.ToastUtils.show("水箱缺水，请加水");
                return false;
            }
        }
        return true;
    }

    short steam=0;

    /**
     * 开始工作
     */
    private void startWork() {

        if (!WorkBeforeCheck(mSteamModel)) {
            return;
        }

        //加湿蒸
        if (setSteamList!=null) {
            for (int i = 0; i < setSteamList.size(); i++) {
                String steam1=wheelFront.getItemsContent(wheelFront.getSelectedItem());
                if (setSteamList.get(i).equals(steam1)) {
                    steam = setSteamValueList.get(i).byteValue();
                }
            }
            String temp = wheelFront2.getItemsContent(wheelFront2.getSelectedItem());
            String time = "";
            if (llRear.getVisibility() == View.VISIBLE) {
                time = wheelRear.getItemsContent(wheelRear.getSelectedItem());
            } else {
                time = "0";
            }
            LogUtils.i("20180731", " temp:" + temp + " time:" + time);
            final short newTemp = Short.parseShort(temp);
            final int newTime = Integer.parseInt(time);
            final int ordertime_min;
            //是否预约
            if (sbOrderSwitch.isChecked()) {
                ordertime_min = HelperRikaData.getMinGap620(orderDate+":00");
            } else {
                ordertime_min = 0;
            }
            Log.e("预约时间",ordertime_min+"---");

//        if (ordertime_min<0){
//            ToastUtils.show("预约时间不能早于当前时间",Toast.LENGTH_LONG);
//            return;
//        }
            if (mSteamOvenOne != null) {
                ToolUtils.logEvent(mSteamOvenOne.getDt(), "开始一体机" + mTitle + "温度时间工作:" + functionName + ":" + newTemp + ":" + newTime, "roki_设备");
            }


            mSteamOvenOne.setSteameOvenOneRunMode((short) mSteamModel, newTime, newTemp,  ordertime_min, (byte)steam, new VoidCallback() {

                @Override
                public void onSuccess() {

//                    if (mSteamOvenOne.getGuid().getGuid().contains("920")) {
//                        query();
//                    }
//                    AbsDeviceSteamOvenOne620Page.isWorking=true;
                    UIService.getInstance().popBack();
                    sendMul(functionCode);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });


        }else {

            String temp = wheelFront.getItemsContent(wheelFront.getSelectedItem());


            String time = "";
            if (llRear.getVisibility() == View.VISIBLE) {
                time = wheelRear.getItemsContent(wheelRear.getSelectedItem());
            } else {
                time = "0";
            }
            LogUtils.i("20180731", " temp:" + temp + " time:" + time);
            final short newTemp = Short.parseShort(temp);
            final int newTime = Integer.parseInt(time);
            final int ordertime_min;

            //是否预约
            if (sbOrderSwitch.isChecked()) {
                ordertime_min = HelperRikaData.getMinGap620(orderDate+":00");
            } else {
                ordertime_min = 0;
            }

//        if (ordertime_min<0){
//            ToastUtils.show("预约时间不能早于当前时间",Toast.LENGTH_LONG);
//            return;
//        }
            if (mSteamOvenOne != null) {
                ToolUtils.logEvent(mSteamOvenOne.getDt(), "开始一体机" + mTitle + "温度时间工作:" + functionName + ":" + newTemp + ":" + newTime, "roki_设备");
            }


            if (mSteamModel==14){
                mSteamOvenOne.setSteameOvenOneRunModeExp((short) mSteamModel, newTime, newTemp,Short.parseShort(wheelFront2.getItemsContent(wheelFront2.getSelectedItem())), ordertime_min, (byte) 0, new VoidCallback() {

                    @Override
                    public void onSuccess() {
//                        if (mSteamOvenOne.getGuid().getGuid().contains("920")) {
//                            query();
//                        }
                        UIService.getInstance().popBack();
                        sendMul(functionCode);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }else {

                mSteamOvenOne.setSteameOvenOneRunMode((short) mSteamModel, newTime, newTemp, ordertime_min, (byte) 0, new VoidCallback() {

                    @Override
                    public void onSuccess() {
//                        if (mSteamOvenOne.getGuid().getGuid().contains("920")) {
//                            query();
//                        }
                        UIService.getInstance().popBack();
                        sendMul(functionCode);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        }
        Log.e("---", "0----On");

    }


    private void cookingCurveSave() {
        String deviceGuid = mSteamOvenOne.getGuid().getGuid();// stove.getGuid().getGuid();
        int id = 0;
        String model =  "";
        String setTemp =  "";
        String setTime = "";
        RokiRestHelper.cookingCurveSave(deviceGuid, id, model, setTemp, setTime,0,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveSaveRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveSaveRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        if (rcReponse.payload != 0) {
                            long curveId = rcReponse.payload;
                            Bundle bd = new Bundle();
                            bd.putString(PageArgumentKey.Guid,deviceGuid);
//                            bd.putInt(PageArgumentKey.HeadId,headId);
                            bd.putLong(PageArgumentKey.curveId, curveId);
//                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
//                            CookCurveActivity.start(activity,bd);
                            SteamOvenCookCurveActivity.start(activity,bd);


                        }

                        onMIvBackClicked();
                    }

                    @Override
                    public void onFailure(Throwable t) {


                        ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);

                        onMIvBackClicked();
                    }
                });

    }


    //发送统计
    private void sendMul(String code) {
        if (mSteamModel == 34||
                mSteamModel == 18||mSteamModel == 37
                || mSteamModel == 32
                || mSteamModel == 33
                || mSteamModel == 35
                || mSteamModel == 36) {
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
