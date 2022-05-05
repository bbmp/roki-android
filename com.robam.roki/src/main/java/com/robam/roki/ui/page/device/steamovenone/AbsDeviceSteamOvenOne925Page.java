package com.robam.roki.ui.page.device.steamovenone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.FunctionMore;
import com.legent.plat.pojos.device.FunctionTop3;
import com.legent.plat.pojos.device.HideFunc;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.NewSteamOvenOneAlarm2Event;
import com.robam.common.events.OvenOtherEvent;
import com.robam.common.events.SteamOvenOneDescalingEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenOneWorkFinishEvent;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.FunctionsTop4;
import com.robam.common.pojos.device.Oven.OvenModeName;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.SteamOvenExpParam;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.AbsOvenExpDialog;
import com.robam.roki.ui.dialog.SteamOvenExpDialog;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/7/23.
 */
public class AbsDeviceSteamOvenOne925Page<SteamOvenOne extends AbsSteameOvenOne>
        extends DeviceCatchFilePage {

    SteamOvenOne mSteamOvenOne;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.iv_device_switch)
    ImageView mIvDeviceSwitch;
    @InjectView(R.id.iv_device_more)
    ImageView mIvDeviceMore;
    @InjectView(R.id.contain)
    FrameLayout contain;
    List<DeviceConfigurationFunctions> hideFunList = new ArrayList<>();
    List<DeviceConfigurationFunctions> mainList = new ArrayList<>();
    List<DeviceConfigurationFunctions> otherList = new ArrayList<>();
    List<DeviceConfigurationFunctions> moreList = new ArrayList<>();
    List<FunctionTop3> top3s = new ArrayList<>();
    List<FunctionMore> mores = new ArrayList<>();
    AbsSteamOvenFirstView steamOvenFirstView;
    AbsSteamOvenWorkingView steamOvenWorkingView;
    AbsSteamOvenWorking906View steamOvenWorking906View;
    AbsSteamOvenWorking610View steamOvenWorking610View;
    public HashMap<String, String> paramMap = new HashMap<>();
    public HashMap<String, DeviceConfigurationFunctions> paramMapMore = new HashMap<>();
    short from;
    private boolean mCompleteSign = false;
    private IRokiDialog mCloseDialog;
    IRokiDialog iRokiDialogAlarmType_02;
    IRokiDialog iRokiDialogAlarmType_03;
    IRokiDialog dialogByType;
    boolean falg = true;

    int cmd;
    String mode;
    String desc;

    private List<FunctionsTop4> top4s;
    boolean isSpecial = false;
    private String localRecipeParams = "";

    private String selectTemp;
    private String selectTime;
    private String model;
    private boolean isPause;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String temp = (String) msg.obj;
                    selectTemp = getNumData(temp);
                    break;
                case 2:
                    String time = (String) msg.obj;
                    selectTime = getNumData(time);
                    break;
            }
        }
    };
    private MainFunc mainFunc;
    public IRokiDialog mRokiDialog;
    public IRokiDialog dryDialog;
    private IRokiDialog descalingDialog;
    public String needDescalingParams;


    //从字符中提取数字
    private String getNumData(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
        if (mSteamOvenOne == null) {
//            mSteamOvenOne = Plat.deviceService.lookupChild(mGuid);
            return;
        }

    }

    @Subscribe
    public void onEvent(SteamOvenOneWorkFinishEvent event) {
        LogUtils.i("202010191634", "工作完成上报:" + event.steameOvenOne.powerOnStatus);
        if (mSteamOvenOne.powerOnStatus != SteamOvenOnePowerOnStatus.OperatingState
                && mSteamOvenOne.powerOnStatus != SteamOvenOnePowerOnStatus.AlarmStatus) {
            LogUtils.i("202010191634", "工作完成上报11111:" + event.steameOvenOne.SteameOvenWorkComplete);
            if (event.steameOvenOne.SteameOvenWorkComplete == 0) {
                mCompleteSign = true;
                if (steamOvenWorking906View != null) {
                    steamOvenWorking906View.completeWork();
                } else if (steamOvenWorking610View != null){
                    steamOvenWorking610View.completeWork();
                }else {
                    steamOvenWorkingView.completeWork();
                }
            }
        }
        if (event.steameOvenOne.SteameOvenWorkComplete == 1) {
            mCompleteSign = false;
        }
        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.OperatingState){
            mCompleteSign = false;
        }
        if (steamOvenWorking906View != null) {
            steamOvenWorking906View.closeAllDialog();
        } else if (steamOvenWorking610View != null){
            steamOvenWorking610View.closeAllDialog();
        }else {
            steamOvenWorkingView.closeAllDialog();
        }
        if (mCloseDialog != null && mCloseDialog.isShow()) {
            mCloseDialog.dismiss();
        }
    }

    @Subscribe
    public void onEvent(OvenOtherEvent event) {
        if (isPause) {
            return;
        }
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID())) {
            return;
        }
        if (event.pojo.eventId == 22) {
            if (iRokiDialogAlarmType_02 == null) {
                iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(getActivity(), DialogUtil.DIALOG_TYPE_02);
            }
                iRokiDialogAlarmType_02.setTitleText("蒸汽提醒");
                iRokiDialogAlarmType_02.setTitleAralmCodeText("");
                iRokiDialogAlarmType_02.setContentText("无法开始工作，请移至产品端进行操作。");
                centerOneBtnListener();
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);//工作页面
            return;
        }
        if (event.pojo.eventId == 23) {
            if (iRokiDialogAlarmType_03 == null) {
                iRokiDialogAlarmType_03 = RokiDialogFactory.createDialogByType(getActivity(), DialogUtil.DIALOG_TYPE_02);
            }
                iRokiDialogAlarmType_03.setTitleText("温度过高");
                iRokiDialogAlarmType_03.setTitleAralmCodeText("");
                iRokiDialogAlarmType_03.setContentText("温度过高，请打开门冷却后再开始工作。");
                centerOneBtnListener1();
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);//工作页面
            return;
        }
    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID())) {
            return;
        }
        mSteamOvenOne = (SteamOvenOne) event.pojo;
        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause
                || mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Order
                || mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            mCompleteSign = false;
            LogUtils.i("202010201813", "666" + mCompleteSign);
        }
        if (SteamOvenOnePowerStatus.Off == event.pojo.powerStatus
                && !mCompleteSign//关机 且 未工作sign
                || SteamOvenOnePowerStatus.Wait == event.pojo.powerStatus
                //&& !mCompleteSign //待机 且 未工作sign
                || SteamOvenOnePowerStatus.On == event.pojo.powerStatus
                && SteamOvenOnePowerOnStatus.OperatingState == event.pojo.powerOnStatus
                && !mCompleteSign //开机 且 操作状态 且 未工作sign
                ) {
            LogUtils.i("202010201813", "222" + event.pojo.powerStatus);
            if (mCloseDialog != null && mCloseDialog.isShow()) {
                mCloseDialog.dismiss();
            }
            if (steamOvenWorking906View != null) {
                steamOvenWorking906View.closeAllDialog();
            } else if (steamOvenWorking610View != null){
                steamOvenWorking610View.closeAllDialog();
            }else {
                steamOvenWorkingView.closeAllDialog();
            }

            contain.getChildAt(0).setVisibility(View.VISIBLE);//待机页面
            contain.getChildAt(1).setVisibility(View.INVISIBLE);
        } else {
            LogUtils.i("202010201813", "777" + mCompleteSign);
            LogUtils.i("202010201813", "powerStatus" + event.pojo.powerStatus);
            LogUtils.i("202010201813", "powerOnStatus" + event.pojo.powerOnStatus);
            if (SteamOvenOnePowerStatus.Off == event.pojo.powerStatus) {
                LogUtils.i("202010201813", "444" + event.pojo.powerStatus);
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                mCompleteSign = false;
                if (steamOvenWorking906View != null) {
                    steamOvenWorking906View.closeAllDialog();
                } else  if (steamOvenWorking610View != null){
                    steamOvenWorking610View.closeAllDialog();
                }{
                    steamOvenWorkingView.closeAllDialog();
                }
                if (mCloseDialog != null && mCloseDialog.isShow()) {
                    mCloseDialog.dismiss();
                }
            }
            if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);//工作页面
                return;
            }
            if (mSteamOvenOne.WaterStatus == 1
                    && mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus
                    && mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.On
                    || SteamOvenOnePowerStatus.On == event.pojo.powerStatus
                    && SteamOvenOnePowerOnStatus.AlarmStatus == event.pojo.powerOnStatus) {
                return;
            }
            LogUtils.i("202010201813", "555" + event.pojo.powerStatus);
            closeAllDialog2();
            closePage();
            contain.getChildAt(0).setVisibility(View.INVISIBLE);
            contain.getChildAt(1).setVisibility(View.VISIBLE);//工作页面

            LogUtils.i("202010231509", "mCompleteSign:::" + mCompleteSign);
            if (steamOvenWorking906View != null) {
                steamOvenWorking906View.updateStatus(event.pojo, mCompleteSign);
            }  else if (steamOvenWorking610View != null){
                steamOvenWorking610View.updateStatus(event.pojo, mCompleteSign);
            }else {
                steamOvenWorkingView.updateStatus(event.pojo, mCompleteSign);
            }
        }

    }

    //开门事件
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID()))
            return;
        mSteamOvenOne = (SteamOvenOne) event.pojo;
    }

    //除垢提醒事件
    @Subscribe
    public void onEvent(SteamOvenOneDescalingEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID()))
            return;
        descalingDialog();
    }

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


    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.device.getID()))
            return;
        LogUtils.i("20180829", "event::" + event.isConnected);
        if (!event.isConnected) {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_LONG);
            contain.getChildAt(1).setVisibility(View.INVISIBLE);
            contain.getChildAt(0).setVisibility(View.VISIBLE);
            steamOvenFirstView.disConnect(true);
        } else {
            steamOvenFirstView.disConnect(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        from = bd.getShort(PageArgumentKey.from);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_steamovenone925, container, false);
        ScreenAdapterTools.getInstance().loadView(view);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }


    private void getTop3Data() {
        CloudHelper.getLookUpCode(mUserId, mGuid, mDc, new Callback<Reponses.GetLookUpResponse>() {
            @Override
            public void onSuccess(Reponses.GetLookUpResponse getLookUpResponse) {
                top3s = getLookUpResponse.functionTop3s;
                mores = getLookUpResponse.functionMores;
                setSortList();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void getTop4Data() {
        RokiRestHelper.getRecipeTop4(mUserId, mGuid, mDc, mDt, new Callback<com.robam.common.io.cloud.Reponses.GetRecipeTop4Response>() {
            @Override
            public void onSuccess(com.robam.common.io.cloud.Reponses.GetRecipeTop4Response getRecipeTop4Response) {
                top4s = getRecipeTop4Response.mFunctionCode;
                LogUtils.i("202010201731", "top4s::" + top4s.toString());
                setSortListForTop4();

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    @Override
    public void onPause() {
        isPause = true;
        super.onPause();
    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse deviceResponse) {

        if (mDevice instanceof AbsSteameOvenOne) {
            mSteamOvenOne = (SteamOvenOne) mDevice;
        }
        try {
            if (deviceResponse != null) {
                String backgroundImg = deviceResponse.viewBackgroundImg;
                if (mIvBg != null) {
                    Glide.with(cx).load(backgroundImg).into(mIvBg);
                }
                if (mTvDeviceModelName != null) {
                    mTvDeviceModelName.setText(deviceResponse.title);
                }
                mainFunc = deviceResponse.modelMap.mainFunc;
                mainList = mainFunc.deviceConfigurationFunctions;


                BackgroundFunc backgroundFunc = deviceResponse.modelMap.backgroundFunc;
                List<DeviceConfigurationFunctions> backDeviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions;
                for (int i = 0; i < backDeviceConfigurationFunctions.size(); i++) {
                    if ("isSpecial".equals(backDeviceConfigurationFunctions.get(i).functionCode)) {
                        isSpecial = true;
                    } else if ("needDescaling".equals(backDeviceConfigurationFunctions.get(i).functionCode)) {
                        needDescalingParams = backDeviceConfigurationFunctions.get(i).functionParams;
                    }
                }

                if (!isSpecial) {
                    if (mainList != null && mainList.size() > 0) {
                        moreList = mainList.get(mainList.size() - 1)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                    }
                    setParamMapMore();
                } else {
                    setParamMapSpecial();
                }

                OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
                otherList = otherFunc.deviceConfigurationFunctions;
                HideFunc hideFunc = deviceResponse.modelMap.hideFunc;
                hideFunList = hideFunc.deviceConfigurationFunctions;

                setParamMap();
                if (isSpecial) {
                    getTop4Data();
                } else {
                    getTop3Data();

                }

            }

        } catch (Exception e) {
            LogUtils.i("20200318", e.getMessage());
            e.printStackTrace();


        }
    }

    private void initView() {
        List<DeviceConfigurationFunctions> functions = null;
        for (int i = 0; i < otherList.size(); i++) {
            if ("localCookbook".equals(otherList.get(i).functionCode)) {
                functions = otherList.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            }
        }
        if (functions != null) {
            for (int j = 0; j < functions.size(); j++) {
                if (functions.get(j).functionCode.equals("ckno")) {
                    localRecipeParams = functions.get(j).functionParams;
                }
            }
        }


        if (mainList != null && mainList.size() > 0 && otherList != null && otherList.size() > 0) {
            steamOvenFirstView = new AbsSteamOvenFirstView(cx, mainList, otherList);
        } else {
            mainList = mainFunc.deviceConfigurationFunctions;

            if (mainList != null && mainList.size() > 0 && otherList != null && otherList.size() > 0) {
                steamOvenFirstView = new AbsSteamOvenFirstView(cx, mainList, otherList);
            } else {
                ToastUtils.showShort("获取数据失败");
                UIService.getInstance().popBack();
            }
        }

        if (hideFunList != null && hideFunList.size() > 0) {
            if (TextUtils.equals(mSteamOvenOne.getDt(), "RC906")) {
                steamOvenWorking906View = new AbsSteamOvenWorking906View(cx, hideFunList, mSteamOvenOne);
            } else if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
                steamOvenWorking610View = new AbsSteamOvenWorking610View(cx, hideFunList, mSteamOvenOne, localRecipeParams, isSpecial);
            }else {
                steamOvenWorkingView = new AbsSteamOvenWorkingView(cx, hideFunList, mSteamOvenOne, localRecipeParams, isSpecial);
            }
        }
        if (contain == null){
            return;
        }
        if (steamOvenFirstView != null) {
            contain.addView(steamOvenFirstView);
        }
        if (steamOvenWorkingView != null) {
            contain.addView(steamOvenWorkingView);
        }

        if (steamOvenWorking906View != null) {
            contain.addView(steamOvenWorking906View);
        }
        if (steamOvenWorking610View != null) {
            contain.addView(steamOvenWorking610View);
        }
        if (contain != null) {
            if (from == 1) {
                //待机
                View view = contain.getChildAt(1);
                if (view != null) {
                    view.setVisibility(View.INVISIBLE);
                }
            } else {
                //工作中
                View view = contain.getChildAt(0);
                if (view != null) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }


        if (!mSteamOvenOne.isConnected()) {
            ToastUtils.showLong(R.string.device_new_connected);
            if (steamOvenFirstView != null) {
                steamOvenFirstView.disConnect(true);
            }
        } else {
            if (steamOvenFirstView != null) {
                steamOvenFirstView.disConnect(false);
            }
        }
        if (steamOvenFirstView != null) {
            steamOvenFirstView.setOnclickMainLister(new AbsSteamOvenFirstView.OnClickMian() {
                @Override
                public void onclickMain(String str) {
                    if (!mSteamOvenOne.isConnected()) {
                        ToastUtils.showLong(R.string.device_connected);
                    } else {
                        clickMain(str);
                    }

                }

                @Override
                public void onclickOther(String str) {
                    if (!mSteamOvenOne.isConnected()) {
                        ToastUtils.showLong(R.string.device_connected);
                    } else {
                        clickOther(str);
                    }

                }
            });
        }
    }


    private void setParamMapSpecial() {
        for (int i = 0; i < mainList.size(); i++) {
            paramMapMore.put(mainList.get(i).functionCode, mainList.get(i));
        }
    }


    public void clickMain(String code) {
        LogUtils.i("20180831", " code:" + code);
        switch (code) {
            case OvenModeName.more:
                if (falg) {
                    steamOvenFirstView.setUpData(moreList);
                    falg = false;
                } else {
                    steamOvenFirstView.removeMoreView();
                    falg = true;
                }

                if (mSteamOvenOne != null) {
                    ToolUtils.logEvent(mSteamOvenOne.getDt(), "点击一体机场景功能:" + "更多", "roki_设备");
                }
                break;
            default:

                goRecipe(code);

                break;
        }
    }

    public void clickOther(String code) {
        LogUtils.i("20180809", " code:" + code);
        switch (code) {
            //蒸模式
            case SteamOvenModeName.steamingMode:
                if (mSteamOvenOne.WaterStatus == 1) {
                    ToastUtils.showShort(R.string.device_alarm_water_out);
                    return;
                }
                if (mSteamOvenOne.doorStatusValue == 1 && !TextUtils.equals(mode, "19")) {
                    ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                    return;
                }
                if (mSteamOvenOne.alarm == 16 ) {
                    ToastUtils.show("水箱缺水，请补充水份", Toast.LENGTH_SHORT);
                    return;
                }
                List<DeviceConfigurationFunctions> steamFunctions = null;
                Bundle bdSteam = new Bundle();
                bdSteam.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < otherList.size(); i++) {
                    if ("steamingMode".equals(otherList.get(i).functionCode)) {
                        steamFunctions = otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        title = otherList.get(i).subView.title;
                    }
                }

                bdSteam.putSerializable(PageArgumentKey.List, (Serializable) steamFunctions);
                bdSteam.putString(PageArgumentKey.title, title);
                if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
                    UIService.getInstance().postPage(PageKey.SteamOven610ModelSelected, bdSteam);
                }else {
                    UIService.getInstance().postPage(PageKey.SteamOvenModelSelected, bdSteam);
                }

                break;
            // 烤模式
            case SteamOvenModeName.roastModel:
                List<DeviceConfigurationFunctions> ovenFunctions = null;
                String titleOven = null;
                Bundle bdOven = new Bundle();
                bdOven.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < otherList.size(); i++) {
                    if ("roastModel".equals(otherList.get(i).functionCode)) {
                        ovenFunctions = otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        titleOven = otherList.get(i).subView.title;
                    }
                }
                bdOven.putSerializable(PageArgumentKey.List, (Serializable) ovenFunctions);
                bdOven.putString(PageArgumentKey.title, titleOven);
                if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
                    UIService.getInstance().postPage(PageKey.SteamOven610ModelSelected, bdOven);
                }else {
                    UIService.getInstance().postPage(PageKey.SteamOvenModelSelected, bdOven);
                }

                break;
            case SteamOvenModeName.airfryFunction:
                List<DeviceConfigurationFunctions> airfryFunction = null;
                String titleFry = null;
                Bundle bdfry = new Bundle();
                bdfry.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < otherList.size(); i++) {
                    if ("airfryFunction".equals(otherList.get(i).functionCode)) {
                        airfryFunction = otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        titleFry = otherList.get(i).subView.title;
                    }
                }
                bdfry.putSerializable(PageArgumentKey.List, (Serializable) airfryFunction);
                bdfry.putString(PageArgumentKey.title, titleFry);
                if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
                    UIService.getInstance().postPage(PageKey.SteamOven610ModelSelected, bdfry);
                }else {
                    UIService.getInstance().postPage(PageKey.SteamOvenModelSelected, bdfry);
                }

                break;
            //蒸箱自动菜谱
            case SteamOvenModeName.steamAutoRecipes:
                Bundle bd = new Bundle();
                String functionParams = null;
                String deviceType = "";
                String cookTitle1 = "";
                String platformCode = "";
                for (int i = 0; i < otherList.size(); i++) {
                    if ((SteamOvenModeName.steamAutoRecipes).equals(otherList.get(i).functionCode)) {
                        functionParams = otherList.get(i).functionParams;
                    }
                }
                try {
                    if (functionParams != null && !"".equals(functionParams)) {
                        JSONObject jsonObject = new JSONObject(functionParams);
                        deviceType = jsonObject.getString("deviceType");
                        cookTitle1 = jsonObject.getString("cookTitle");
                        platformCode = jsonObject.optString("platformCode");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.i("20200102", "deviceType:::" + deviceType);
                bd.putString(PageArgumentKey.RecipeId, deviceType);
                bd.putString(PageArgumentKey.title, cookTitle1);
                bd.putString(PageArgumentKey.platformCode, platformCode);
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, bd);
                if (mSteamOvenOne != null) {
                    ToolUtils.logEvent(mSteamOvenOne.getDt(), "点击:蒸箱自动烹饪菜谱", "roki_设备");
                }

                break;
            //烤箱自动菜谱
            case SteamOvenModeName.ovenAutoRecipes:
                Bundle ovenBd = new Bundle();
                String functionParams2 = null;
                String deviceType2 = "";
                String cookTitle2 = "";
                String platformCode2 = "";
                for (int i = 0; i < otherList.size(); i++) {
                    if ((SteamOvenModeName.ovenAutoRecipes).equals(otherList.get(i).functionCode)) {
                        functionParams2 = otherList.get(i).functionParams;
                    }
                }
                try {
                    if (functionParams2 != null && !"".equals(functionParams2)) {
                        JSONObject jsonObject = new JSONObject(functionParams2);
                        deviceType2 = jsonObject.getString("deviceType");
                        cookTitle2 = jsonObject.getString("cookTitle");
                        platformCode2 = jsonObject.optString("platformCode");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ovenBd.putString(PageArgumentKey.RecipeId, deviceType2);
                ovenBd.putString(PageArgumentKey.title, cookTitle2);
                ovenBd.putString(PageArgumentKey.platformCode, platformCode2);
                ovenBd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, ovenBd);
                if (mSteamOvenOne != null) {
                    ToolUtils.logEvent(mSteamOvenOne.getDt(), "点击:电烤箱自动烹饪菜谱", "roki_设备");
                }

                break;
            //蒸烤一体机
            case SteamOvenModeName.zkyAutoRecipes:
                Bundle ovenBd3 = new Bundle();
                String functionParams3 = null;
                String deviceType3 = "";
                String cookTitle = "";
                String platformCode3 = "";
                for (int i = 0; i < otherList.size(); i++) {
                    if ((SteamOvenModeName.zkyAutoRecipes).equals(otherList.get(i).functionCode)) {
                        functionParams3 = otherList.get(i).functionParams;
                    }
                }
                try {
                    if (functionParams3 != null && !"".equals(functionParams3)) {
                        JSONObject jsonObject = new JSONObject(functionParams3);
                        deviceType3 = jsonObject.getString("deviceType");
                        cookTitle = jsonObject.getString("cookTitle");
                        platformCode3 = jsonObject.optString("platformCode");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ovenBd3.putString(PageArgumentKey.RecipeId, deviceType3);
                ovenBd3.putString(PageArgumentKey.title, cookTitle);
                ovenBd3.putString(PageArgumentKey.platformCode, platformCode3);
                ovenBd3.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, ovenBd3);
                if (mSteamOvenOne != null) {
                    ToolUtils.logEvent(mSteamOvenOne.getDt(), "点击:一体机自动烹饪菜谱", "roki_设备");
                }
                break;
            //915新增
            //专业模式
            case SteamOvenModeName.professionalModel:
                List<DeviceConfigurationFunctions> professionalModelList = null;
                String professionalModelTitle = null;
                String imgType = "";
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(SteamOvenModeName.professionalModel)) {
                        imgType = otherList.get(i).functionParams;
                        professionalModelList = otherList.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        professionalModelTitle = otherList.get(i).functionName;

                    }
                }
                Bundle bdPro = new Bundle();
                bdPro.putString(PageArgumentKey.Guid, mGuid);
                bdPro.putSerializable(PageArgumentKey.List, (Serializable) professionalModelList);
                bdPro.putString(PageArgumentKey.title, professionalModelTitle);
                bdPro.putString(PageArgumentKey.descaling, needDescalingParams);
                if (TextUtils.equals(mSteamOvenOne.getDt(), "CQ926")) {
                    bdPro.putString(PageArgumentKey.imgType, imgType);
                    UIService.getInstance().postPage(PageKey.SteamOven926ProMode, bdPro);
                } else {
                    UIService.getInstance().postPage(PageKey.SteamOvenProMode, bdPro);
                }
                break;
            //多段模式
            case SteamOvenModeName.multiStepModel:
                List<DeviceConfigurationFunctions> multiStepModelList = null;
                String multiStepModelTitle = null;
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(SteamOvenModeName.multiStepModel)) {
                        multiStepModelList = otherList.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        multiStepModelTitle = otherList.get(i).functionName;
                    }
                }
                Bundle bdMultiStepModel = new Bundle();
                bdMultiStepModel.putString(PageArgumentKey.Guid, mGuid);
                bdMultiStepModel.putSerializable(PageArgumentKey.List, (Serializable) multiStepModelList);
                bdMultiStepModel.putString(PageArgumentKey.title, multiStepModelTitle);
                bdMultiStepModel.putString(PageArgumentKey.descaling, needDescalingParams);
                bdMultiStepModel.putBoolean(PageArgumentKey.isRunning, isSpecial);
                if (TextUtils.equals(mSteamOvenOne.getDt(), "CQ926")) {
                    UIService.getInstance().postPage(PageKey.MultiStepC908Mode, bdMultiStepModel);
                } else if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
                    List<DeviceConfigurationFunctions> multModes = new ArrayList<>();
                    for (int i = 0; i < otherList.size(); i++) {
                        if ("steamingMode".equals(otherList.get(i).functionCode) || "roastModel".equals(otherList.get(i).functionCode)  || "airfryFunction".equals(otherList.get(i).functionCode)) {
                            multModes.addAll(otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions);
                        }
                    }
                    bdMultiStepModel.putSerializable(PageArgumentKey.List,(Serializable)  multModes);
                    UIService.getInstance().postPage(PageKey.Multi610RecipePage, bdMultiStepModel);
                }else {
                    UIService.getInstance().postPage(PageKey.MultiStepMode, bdMultiStepModel);
                }
                break;
            //本地菜谱
            case SteamOvenModeName.localCookbook:
                List<DeviceConfigurationFunctions> localCookbookList = null;
                String localCookbookTitle = null;
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(SteamOvenModeName.localCookbook)) {
                        localCookbookList = otherList.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        localCookbookTitle = otherList.get(i).subView.title;
                    }
                }
                Bundle bdLocalCookbook = new Bundle();
                bdLocalCookbook.putString(PageArgumentKey.Guid, mGuid);
                bdLocalCookbook.putSerializable(PageArgumentKey.List, (Serializable) localCookbookList);
                bdLocalCookbook.putString(PageArgumentKey.title, localCookbookTitle);
                bdLocalCookbook.putString(PageArgumentKey.descaling, needDescalingParams);
                bdLocalCookbook.putString(PageArgumentKey.descaling, needDescalingParams);
                bdLocalCookbook.putString(PageArgumentKey.dt, mSteamOvenOne.getDt());
//                if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
//                    UIService.getInstance().postPage(PageKey.Local610Cookbook, bdLocalCookbook);
//                }else {
                    UIService.getInstance().postPage(PageKey.LocalCookbook, bdLocalCookbook);
//                }

                break;
            //diy菜谱
            case SteamOvenModeName.diyCookbook:
                DeviceConfigurationFunctions diyBean = null;
                String diyCookbookTitle = null;
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(SteamOvenModeName.diyCookbook)) {
                        diyCookbookTitle = otherList.get(i).functionName;
                        diyBean = otherList.get(i);
                    }

                }
                Bundle bdDiyCookbook = new Bundle();
                bdDiyCookbook.putString(PageArgumentKey.Guid, mGuid);
                bdDiyCookbook.putSerializable(PageArgumentKey.Bean, diyBean);
                bdDiyCookbook.putString(PageArgumentKey.title, diyCookbookTitle);
                bdDiyCookbook.putString(PageArgumentKey.descaling, needDescalingParams);
                UIService.getInstance().postPage(PageKey.DiyCookbookList, bdDiyCookbook);
                break;
            //在线自动烹饪菜谱
            case SteamOvenModeName.easyCook:
                Bundle bdEasyCook = new Bundle();
                String functionParams4 = null;
                String deviceType4 = "";
                String cookTitle4 = "";
                String platformCode4 = "";
                for (int i = 0; i < otherList.size(); i++) {
                    if (SteamOvenModeName.easyCook.equals(otherList.get(i).functionCode)) {
                        functionParams4 = otherList.get(i).functionParams;
                    }
                }
                try {
                    JSONObject jsonObject = new JSONObject(functionParams4);
                    deviceType4 = jsonObject.getString("deviceType");
                    cookTitle4 = jsonObject.getString("cookTitle");
                    platformCode4 = jsonObject.getString("platformCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bdEasyCook.putString(PageArgumentKey.Guid, mGuid);
                bdEasyCook.putString(PageArgumentKey.RecipeId, deviceType4);
                bdEasyCook.putString(PageArgumentKey.title, cookTitle4);
                bdEasyCook.putString(PageArgumentKey.platformCode, platformCode4);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, bdEasyCook);
                if (mSteamOvenOne != null) {
                    ToolUtils.logEvent(mSteamOvenOne.getDt(), "点击:蒸烤一体机自动烹饪菜谱", "roki_设备");
                }

                break;
            //烟机联动
            case SteamOvenModeName.linkage:
                List<DeviceConfigurationFunctions> linkageList = null;
                String linkageTitle = null;
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(SteamOvenModeName.linkage)) {
                        linkageList = otherList.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                        linkageTitle = otherList.get(i).functionName;
                    }
                }
                Bundle bdLinkage = new Bundle();
                bdLinkage.putString(PageArgumentKey.Guid, mGuid);
                bdLinkage.putSerializable(PageArgumentKey.List, (Serializable) linkageList);
                bdLinkage.putString(PageArgumentKey.title, linkageTitle);
                UIService.getInstance().postPage(PageKey.SteamOvenFanLinkage, bdLinkage);
                break;
            case SteamOvenModeName.professionalBaking:
                paramShow(code);
                break;
            case SteamOvenModeName.fzMode:
                List<DeviceConfigurationFunctions> fzModeFunctions = null;
                String titleFz = null;
                Bundle bdFz = new Bundle();
                bdFz.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < otherList.size(); i++) {
                    if ("fzMode".equals(otherList.get(i).functionCode)) {
                        fzModeFunctions = otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        titleFz = otherList.get(i).subView.title;
                    }
                }
                bdFz.putSerializable(PageArgumentKey.List, (Serializable) fzModeFunctions);
                bdFz.putString(PageArgumentKey.title, titleFz);
                if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
                    UIService.getInstance().postPage(PageKey.SteamOven610ModelSelected, bdFz);
                }else {
                    UIService.getInstance().postPage(PageKey.SteamOvenModelSelected, bdFz);
                }
                break;
            default:
                ToastUtils.show("即将开放，敬请期待", Toast.LENGTH_SHORT);
                break;
        }

    }


    private void setSortList() {
        mainList.clear();
        moreList.clear();
        if (top3s != null && top3s.size() > 0) {
            for (int i = 0; i < top3s.size(); i++) {
                for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                    if (top3s.get(i).functionCode.equals(en.getKey())) {
                        mainList.add(en.getValue());
                    }
                }
            }
        }

        if (mores != null && mores.size() > 0) {
            for (int i = 0; i < mores.size(); i++) {
                for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                    if (mores.get(i).functionCode.equals(en.getKey())) {
                        moreList.add(en.getValue());
                    }
                }
            }
        }
        initView();

    }


    private void setSortListForTop4() {
        mainList.clear();
        if (top4s != null && top4s.size() > 0) {
            for (int i = 0; i < top4s.size(); i++) {
                for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                    if (top4s.get(i).functionCode.equals(en.getKey())) {
                        mainList.add(en.getValue());
                    }
                }
            }
        }
        initView();

    }

    private void setParamMapMore() {

        for (int i = 0; i < mainList.size(); i++) {
            paramMapMore.put(mainList.get(i).functionCode, mainList.get(i));
        }

        for (int i = 0; i < moreList.size(); i++) {
            paramMapMore.put(moreList.get(i).functionCode, moreList.get(i));
        }

    }

    private void setParamMap() {
        for (int i = 0; i < otherList.size(); i++) {
            paramMap.put(otherList.get(i).functionCode, otherList.get(i).functionParams);
        }
    }

    boolean allTimeUseTag;

    //跳转到菜谱
    private void goRecipe(String code) {
        try {
            String funParam = paramMapMore.get(code).functionParams;
            String functionName = paramMapMore.get(code).functionName;
            String msg = paramMapMore.get(code).msg;
            JSONObject jsonObject = new JSONObject(funParam);
            if (isSpecial) {

                JSONObject param = jsonObject.getJSONObject("param");
                if ("".equals(param.optString("allTimeUse"))) {
                    allTimeUseTag = false;
                } else {
                    allTimeUseTag = true;
                }
                if (needShowDialog(functionName)) {
                    descalingDialog();
                    return;
                }
                model = param.getJSONObject("model").getString("value");
                if (mSteamOvenOne.doorStatusValue == 1 && !TextUtils.equals(model, "19")) {
                    ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                    return;
                }

                if (model.equals("13")
                        ||model.equals("14")
                        ||model.equals("15")
                        ||model.equals("16")
                        ||model.equals("17") //解冻
                        ||model.equals("18") //解冻
                        ||model.equals("20")
                        ||model.equals("21")
                        ||model.equals("22")
                ){
                    if (mSteamOvenOne.WaterStatus == 1){
                        ToastUtils.showShort(R.string.device_alarm_water_out);
                        return;
                    }
                    if (mSteamOvenOne.alarm == 16 ) {
                        ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_SHORT);
                        return;
                    }
                }
                //干燥
                if ("19".equals(model)) {
                    String title = param.getJSONObject("defaultTips").getString("title");
                    String content = param.getJSONObject("defaultTips").getString("content");
                    String positiveButton = param.getJSONObject("defaultTips").getString("positiveButton");
                    String negativeButton = param.getJSONObject("defaultTips").getString("negativeButton");
                    dryTipsDialog(title, content, positiveButton, negativeButton, code);
                    //除垢
                } else if ("21".equals(model)) {
                    String title = param.getJSONObject("defaultTips").getString("title");
                    String content = param.getJSONObject("defaultTips").getString("content");
                    String positiveButton = param.getJSONObject("defaultTips").getString("positiveButton");
                    String negativeButton = param.getJSONObject("defaultTips").getString("negativeButton");

                    descalingTipsDialog(title, content, positiveButton, negativeButton, code);
                    //EXP
                } else if ("10".equals(model)) {
                    OvenExpParamBean ovenParams = null;
                    try {
                        ovenParams = JsonUtils.json2Pojo(jsonObject.toString(), OvenExpParamBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (ovenParams != null) {
                        List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
                        List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
                        List<Integer> time = ovenParams.getParam().getMinute().getValue();


                        String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();

                        String timeDefault = ovenParams.getParam().getMinuteDefault().getValue();

                        String tempDiff = ovenParams.getParam().getTempDiff().getValue();
                        String tempStart = ovenParams.getParam().getTempStart().getValue();
                        String downMin = ovenParams.getParam().getTempMin().getValue();
                        String descTips = ovenParams.getParam().getDesc().getValue();

                        List<Integer> tempUpList = TestDatas.createTempertureDatas(tempUp);
                        List<Integer> timeList = TestDatas.createModeDataTime(time);

                        int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
                        int deDiff = Integer.parseInt(tempDiff);
                        int deNum2 = deNum1 - deDiff;
                        int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
                        int deStart = Integer.parseInt(tempStart);
                        int min = Integer.parseInt(downMin);

                        //0 不需要 1 需要
                        if (mSteamOvenOne.weatherDescalingValue != 0) {
                            if (allTimeUseTag) {
                                setEXPDialogParam(tempUpList, tempDown, timeList, "℃", "℃", "分钟", descTips, deNum1, deNum2, deNum3, deDiff, deStart, min, code);
                            } else {
                                descalingDialog();
                            }
                        } else {
                            setEXPDialogParam(tempUpList, tempDown, timeList, "℃", "℃", "分钟", descTips, deNum1, deNum2, deNum3, deDiff, deStart, min, code);
                        }

                    }
                } else {
                    if (model.equals("13")
                            ||model.equals("14")
                            ||model.equals("15")
                            ||model.equals("16")
                            ||model.equals("20")
                            ||model.equals("21")
                            ||model.equals("22")
                    ){
                        if (mSteamOvenOne.alarm == 16 ) {
                            ToastUtils.show("水箱缺水，请加水", Toast.LENGTH_SHORT);
                            return;
                        }
                    }
                    JSONArray setTemp = param.getJSONObject("setTemp").getJSONArray("value");
                    String defaultSetTemp = param.getJSONObject("defaultSetTemp").getString("value");
                    JSONArray setTime = param.getJSONObject("setTime").getJSONArray("value");
                    String defaultSetTime = param.getJSONObject("defaultSetTime").getString("value");
                    List<Integer> tempList = new ArrayList<>();
                    for (int i = 0; i < setTemp.length(); i++) {
                        Integer temp = (Integer) setTemp.get(i);
                        tempList.add(temp);
                    }
                    List<Integer> timeList = new ArrayList<>();
                    for (int i = 0; i < setTime.length(); i++) {
                        Integer time = (Integer) setTime.get(i);
                        timeList.add(time);
                    }
                    List<Integer> tempTure = TestDatas.createModeDataTemp(tempList);
                    List<Integer> timeTemp = TestDatas.createModeDataTime(timeList);

                    if (mSteamOvenOne.doorStatusValue == 1) {
                        ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                        return;
                    }
                    //0 不需要 1 需要
                    if (mSteamOvenOne.weatherDescalingValue != 0) {
                        if (allTimeUseTag) {
                            setDialogParam(tempTure, timeTemp, defaultSetTemp, defaultSetTime, msg, code, timeList.get(2));
                        } else {
                            descalingDialog();
                        }
                    } else {
                        setDialogParam(tempTure, timeTemp, defaultSetTemp, defaultSetTime, msg, code, timeList.get(2));
                    }


                }

            } else {

                sendMul(code);
                long cookId = jsonObject.getLong("cookbookId");
                RecipeDetailPage.show(cookId, RecipeDetailPage.DeviceRecipePage,
                        RecipeRequestIdentification.RECIPE_SORTED, mGuid);
                if (mSteamOvenOne != null) {
                    ToolUtils.logEvent(mSteamOvenOne.getDt(), "点击一体机场景功能:" + functionName, "roki_设备");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发送统计
    private void sendMul(String code) {
        LogUtils.i("202010201715", "mUserId::" + mUserId);
        LogUtils.i("202010201715", "mGuid::" + mGuid);
        LogUtils.i("202010201715", "code::" + code);
        LogUtils.i("202010201715", "mDc::" + mDc);
        CloudHelper.getReportCode(mUserId, mGuid, code, mDc, new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
                LogUtils.i("202010201715", getReportResponse.msg);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("202010201715", t.getMessage());
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
        mCompleteSign = false;
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_device_switch)
    public void onMIvDeviceSwitchClicked() {
        if (mSteamOvenOne == null){
            return;
        }
        if (!mSteamOvenOne.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Off) {
            ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
            return;
        }
        ToolUtils.logEvent(mSteamOvenOne.getDt(), "关机", "roki_设备");


        if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.On && mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.OperatingState) {
            mSteamOvenOne.setSteameOvenStatus_Off(new VoidCallback() {
                @Override
                public void onSuccess() {
                    ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait && mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.NoStatus) {
                mSteamOvenOne.setSteameOvenStatus_Off(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

            } else {
                mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
                mCloseDialog.setTitleText(R.string.off_device);
                mCloseDialog.setContentText(R.string.device_off_content);
                mCloseDialog.show();
                mCloseDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mCloseDialog != null && mCloseDialog.isShow()) {
                                    mCloseDialog.dismiss();
                                }
                                mSteamOvenOne.setSteameOvenStatus_Off(new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
                                        mCompleteSign = false;
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });

                            }

                        }
                );
                mCloseDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }

                );
            }

        }


    }


    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, mGuid);
        UIService.getInstance().postPage(PageKey.AbsSteamOvenMore, bd);
        if (mSteamOvenOne == null) {
            return;
        }
        ToolUtils.logEvent(mSteamOvenOne.getDt(), "点击:更多", "roki_设备");
    }


    //蒸、烤、辅助模式
    private void sendSteamOvenFZModel(final String code) {
        if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Off ||
                mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait) {
            mSteamOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    mSteamOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), Short.parseShort(selectTime), Short.parseShort(selectTemp), (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtils.i("202010201715", "success:下发模式成功1");
                            sendMul(code);
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
            mSteamOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), Short.parseShort(selectTime), Short.parseShort(selectTemp), (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("202010201715", "success:下发模式成功2");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendMul(code);
                        }
                    },100);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }

    /**
     * 干燥模式
     */
    private void dryMode(final String code) {
        if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Off ||
                mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait) {
            mSteamOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSteamOvenOne.setSteameOvenOneRunMode(Short.parseShort("19"),
                                    (short) 10, (short) 160, (short) 0, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            sendMul(code);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });


                        }
                    }, 500);

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            mSteamOvenOne.setSteameOvenOneRunMode(Short.parseShort("19"), (short) 10, (short) 160, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendMul(code);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });

        }
    }


    private void closeAllDialog2() {
        if (dryDialog != null && dryDialog.isShow()) {
            dryDialog.dismiss();
        }
        if (descalingDialog != null && descalingDialog.isShow()) {
            descalingDialog.dismiss();
        }
        if (mRokiDialog != null && mRokiDialog.isShow()) {
            mRokiDialog.dismiss();
        }

    }


    //下发干燥模式前的提示框
    private void dryTipsDialog(String title, String content, String positiveButton, String negativeButton, final String code) {
        dryDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        dryDialog.setTitleText(title);
        dryDialog.setContentText(content);
        dryDialog.setCancelBtn(negativeButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dryDialog.dismiss();
            }
        });
        dryDialog.setOkBtn(positiveButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dryMode(code);
                dryDialog.dismiss();
            }
        });
        dryDialog.show();

    }

    //下发除垢模式前的提示框
    private void descalingTipsDialog(String title, String content, String positiveButton, String negativeButton, final String code) {
        descalingDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        descalingDialog.setTitleText(title);
        descalingDialog.setContentText(content);
        descalingDialog.setCancelBtn(negativeButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descalingDialog.dismiss();
            }
        });
        descalingDialog.setOkBtn(positiveButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dryMode(code);
                descalingDialog.dismiss();
            }
        });
        descalingDialog.show();
    }


    private void setDialogParam(List<Integer> tempList, List<Integer> timeList, String defaultTemp, String defaultTime, String desc, final String code, int count) {
        short newDefaultTemp = (Short.parseShort(defaultTemp));
        short newDefaultTime = (Short.parseShort(defaultTime));
        //拿到时间温度的索引值
        int indexTemp = newDefaultTemp - tempList.get(0);
        int indexTime = newDefaultTime - timeList.get(0);
        if (timeList.size() > 1) {
            indexTime = indexTime / (timeList.get(1) - timeList.get(0));
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_17);
        mRokiDialog.setOkBtn("开始工作", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("202010201715", "success:开始工作");
                sendSteamOvenFZModel(code);
                mRokiDialog.dismiss();
            }
        });
        mRokiDialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });
        mRokiDialog.setWheelViewData(
                HelperRikaData.getTempData2(tempList),
                null,
                HelperRikaData.getTimeData3(timeList, count),
                desc,
                false,
                indexTemp,
                0,
                indexTime,
                new OnItemSelectedListenerFrone() {

                    @Override
                    public void onItemSelectedFront(String contentFront) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = contentFront;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }, null, new OnItemSelectedListenerRear() {

                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = contentRear;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }
                });

        mRokiDialog.show();


    }


    private void closePage() {
        //专业模式
        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenProMode)) {
            UIService.getInstance().popBack();
        }
        //多段模式页面
        if (UIService.getInstance().isCurrentPage(PageKey.MultiStepMode)) {
            UIService.getInstance().popBack();
        }
        //本地自动菜谱
        if (UIService.getInstance().isCurrentPage(PageKey.LocalCookbook)) {
            UIService.getInstance().popBack();
        }
        //Diy列表页
        if (UIService.getInstance().isCurrentPage(PageKey.DiyCookbookList)) {
            UIService.getInstance().popBack();
        }
        //Diy 详情页
        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenDiyDetail)) {
            UIService.getInstance().popBack().popBack();
        }
        //diy编辑页
        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenEdit)) {
            UIService.getInstance().popBack().popBack();
        }

        //926页面
        if (UIService.getInstance().isCurrentPage(PageKey.SteamOven926ProMode)) {
            UIService.getInstance().popBack();
        }
        if (UIService.getInstance().isCurrentPage(PageKey.MultiStepC908Mode)) {
            UIService.getInstance().popBack();
        }
    }

    private boolean needShowDialog(String mode) {
        if (allTimeUseTag) {
            return false;
        }
        if (TextUtils.equals(mode,cx.getString(R.string.device_steamOvenOne_name_fengpeikao))) {
            return false;
        }
        if ((TextUtils.equals(mode,"干燥"))) {
            return false;
        }
        if (mSteamOvenOne.weatherDescalingValue == 0) {
            return false;
        }
        return true;
    }

    private void paramShow(String code) {
        SteamOvenExpParam steamOvenExpParams = null;
        LogUtils.i("20180730", "paramShow code:" + code);
        try {
            steamOvenExpParams = JsonUtils.json2Pojo(paramMap.get(code), SteamOvenExpParam.class);
            cmd = steamOvenExpParams.getParam().getCmd().getValue();
            if (steamOvenExpParams != null) {
                mode = steamOvenExpParams.getParam().getModel().getValue();
                desc = steamOvenExpParams.getParam().getDesc().getValue();
                List<Integer> tempUp = steamOvenExpParams.getParam().getUpTemp().getValue();
                String tempUpDefault = steamOvenExpParams.getParam().getUpTempDefault().getValue();
                String tempDownDefault = steamOvenExpParams.getParam().getDownTempDefault().getValue();
                List<Integer> time = steamOvenExpParams.getParam().getMinute().getValue();
                List<Integer> tempDown = steamOvenExpParams.getParam().getDownTemp().getValue();
                String timeDefault = steamOvenExpParams.getParam().getMinuteDefault().getValue();
                String tempDiff = steamOvenExpParams.getParam().getTempDiff().getValue();
                String tempStart = steamOvenExpParams.getParam().getTempStart().getValue();
                String downMin = steamOvenExpParams.getParam().getTempMin().getValue();
                String desc = steamOvenExpParams.getParam().getDesc().getValue();
                List<Integer> tempT = TestDatas.createTempertureDatas(tempUp);
                List<Integer> timeT = TestDatas.createModeDataTime(time);
                int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
                int deDiff = Integer.parseInt(tempDiff);
                int deNum2 = deNum1 - deDiff;
                int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
                int deStart = Integer.parseInt(tempStart);
                int min = Integer.parseInt(downMin);
                setDialogParam(tempT, tempDown, timeT, "℃", "℃", "分", desc, deNum1, deNum2, deNum3, deDiff, deStart, min);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDialogParam(List<Integer> tempUp, List<Integer> tempDown, List<Integer> time, String str1, String str2, String str3, String str4, int defNum1, int defNum2, int defNum3, int stepC, int defaultValue, int min) {
        List<String> listButton = TestDatas.createExpDialogText(str1, str2, str3, str4);
        SteamOvenExpDialog steamOvenExpDialog = new SteamOvenExpDialog(cx, tempUp, tempDown, time, listButton, defNum1, defNum2, defNum3, stepC, defaultValue, min);
        steamOvenExpDialog.show(steamOvenExpDialog);
        steamOvenExpDialog.setListener(new SteamOvenExpDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int index1, int index2, int index3) {
                send(cmd, mode, index1, index2, index3);
            }
        });
    }

    private void send(int cmd, String mode, final int setTempUp, final int setTempDown, final int setTime) {
        LogUtils.i("20180721", "code:" + cmd + " mode:" + mode + " setTime:" + setTime + " setTempUp:" +
                setTempUp + " setTempDown::" + setTempDown);
        final short expMode = Short.parseShort(mode);
        if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Off) {
            mSteamOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    mSteamOvenOne.setSteameOvenOneRunMode(expMode, (short) setTime,
                            (short) setTempUp, (short) 0, (short) setTempDown, (short) 0, (short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    if (mSteamOvenOne != null) {
                                        ToolUtils.logEvent(mSteamOvenOne.getDt(), "开始专业烘烤:" + setTempUp + ":" + setTempDown + ":" + setTime, "roki_设备");
                                    }
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
            mSteamOvenOne.setSteameOvenOneRunMode(expMode, (short) setTime,
                    (short) setTempUp, (short) 0, (short) setTempDown, (short) 0, (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
        }
    }


    protected AbsOvenExpDialog absOvenExpDialog;
    private void setEXPDialogParam(List<Integer> tempUp, List<Integer> tempDown, List<Integer> time,
                                   String str1, String str2, String str3, String str4, int defNum1,
                                   int defNum2, int defNum3, int stepC, int defaultValue, int min, final String code) {
        List<String> listButton = TestDatas.createExpDialogText(str1, str2, str3, str4);
        absOvenExpDialog = new AbsOvenExpDialog(cx, tempUp, tempDown, time, listButton, defNum1, defNum2, defNum3, stepC, defaultValue, min);
        absOvenExpDialog.showDialog(absOvenExpDialog);
        absOvenExpDialog.setListener(new AbsOvenExpDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int index1, int index2, int index3) {
                sendExpCommand(model, index3, index1, index2, code);
            }
        });
    }

    //CQ908专业模式
    private void sendExpCommand(final String model, final int time, final int tempUp, final int tempDown, final String code) {

        if (mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Off ||
                mSteamOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait
        ) {

            mSteamOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSteamOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), (short) time, (short) tempUp, (short) 0, (short) tempDown, (short) 255, (short) 255, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("20200602", "onSuccess:::");
                                    sendMul(code);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20200602", "error:::" + t.getMessage());
                                }
                            });

                        }
                    }, 500);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200602", "error:::" + t.getMessage());
                }
            });

        } else {
            mSteamOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), (short) time, (short) tempUp, (short) 0, (short) tempDown, (short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("20200602", "onSuccess:::");
                    sendMul(code);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200602", "error:::" + t.getMessage());
                }
            });

        }
    }

    public void centerOneBtnListener() {
        iRokiDialogAlarmType_02.setOkBtn(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_02.dismiss();
            }
        });
        iRokiDialogAlarmType_02.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_02.show();
    }

    public void centerOneBtnListener1() {
        iRokiDialogAlarmType_03.setOkBtn(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_03.dismiss();
            }
        });
        iRokiDialogAlarmType_03.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_03.show();
    }

    @Subscribe
    public void onEvent(NewSteamOvenOneAlarm2Event event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.steameOvenOne.getID())) {
            return;
        }
        mSteamOvenOne = (SteamOvenOne)event.steameOvenOne;
//        short alarms = event.alarmId;
//        LogUtils.i("202012071057","alarms::"+alarms);
//        AlarmDataUtils.steamOvenOneAlarmStatus(steameOvenOne, alarms);
    }
}
