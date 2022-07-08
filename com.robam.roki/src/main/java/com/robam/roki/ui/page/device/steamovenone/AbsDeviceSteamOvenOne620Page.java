package com.robam.roki.ui.page.device.steamovenone;


import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.BEIKAO;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.EXP;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.FENGBEIKAO;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.FENGSHANKAO;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.JIANKAO;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.JIASHIBEIKAO;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.KONGQIZHA;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.QIANGSHAOKAO;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.SHAOKAO;
import static com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum.WEIBOKAO;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.events.PageBackEvent;
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
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.RobamApp;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.FunctionsTop4;
import com.robam.common.pojos.device.Oven.OvenModeName;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatusNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatusNew;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.bean.SteamOvenExpParam;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.AbsOvenExpDialog;
import com.robam.roki.ui.dialog.SteamOvenExpDialog;
import com.robam.roki.ui.dialog.type.DialogType_05;
import com.robam.roki.ui.form.SteamFinish;
import com.robam.roki.ui.form.SteamOvenCookCurveActivity;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.LocalCookbook620Activity;
import com.robam.roki.ui.widget.view.SteamFrameLayout;
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

public class AbsDeviceSteamOvenOne620Page <SteamOvenOne extends AbsSteameOvenOneNew> extends DeviceCatchFilePage{

    SteamOvenOne mSteamOvenOneNew;
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
    SteamFrameLayout contain;
    List<DeviceConfigurationFunctions> hideFunList = new ArrayList<>();
    List<DeviceConfigurationFunctions> mainList = new ArrayList<>();
    List<DeviceConfigurationFunctions> otherList = new ArrayList<>();
    List<DeviceConfigurationFunctions> moreList = new ArrayList<>();
    List<FunctionTop3> top3s = new ArrayList<>();
    List<FunctionMore> mores = new ArrayList<>();
    AbsSteamOvenFirstView steamOvenFirstView;
//    AbsSteamOvenWorkingView steamOvenWorkingView;

    AbsSteamOvenWorking620View steamOvenWorking620View;
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


    private List<DeviceConfigurationFunctions> getRecipeList(){
        List<DeviceConfigurationFunctions> localCookbookList = null;

        for (int i = 0; i < otherList.size(); i++) {
            if (otherList.get(i).functionCode.equals(SteamOvenModeName.localCookbook)) {
                localCookbookList = otherList.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;

            }
        }
        return localCookbookList;
    }

    private List<FunctionsTop4> top4s;
    boolean isSpecial = false;
    private String localRecipeParams = "";

    private String selectTemp;
    private String selectTime;
    private String selectSteam;
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

                case 3:
                    String steam = (String) msg.obj;

                     selectSteam = steam;
                    break;

                case 0x1234:
                    UIService.getInstance().popBack();
                    break;
            }
        }
    };
    private MainFunc mainFunc;
    public IRokiDialog mRokiDialog;
    public IRokiDialog dryDialog;
    private IRokiDialog descalingDialog;
    public String needDescalingParams;


    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        if (mGuid.equals(event.device.getGuid().getGuid())){
            String name = event.device.getName();
            mTvDeviceModelName.setText(name);
        }
    }

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
        if (mSteamOvenOneNew == null) {
//            mSteamOvenOne = Plat.deviceService.lookupChild(mGuid);
            return;
        }
        MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), mSteamOvenOneNew.getDt() + ":工作页", null);

    }


    private boolean WorkBeforeCheck(int mSteamModel){
        SteamOvenFaultEnum faultEnum = SteamOvenFaultEnum.match(mSteamOvenOneNew.faultCode);
        if (SteamOvenFaultEnum.NO_FAULT != faultEnum&&mSteamOvenOneNew.faultCode!=11) {
            if (faultEnum != null) {

                com.hjq.toast.ToastUtils.show(SteamOvenFaultEnum.match(mSteamOvenOneNew.faultCode).getValue());
            }else {
                com.hjq.toast.ToastUtils.show("设备端故障未处理，请及时处理");
            }
            return false;
        }
        if (SteamOvenHelper.isWork2(mSteamOvenOneNew.workState)) {
            com.hjq.toast.ToastUtils.show("设备已占用");
            return false;
        }
        //门已打开 而且不能开门工作
        if (!SteamOvenHelper.isDoorState(mSteamOvenOneNew.doorState) && !SteamOvenHelper.isOpenDoorWork(SteamOvenModeEnum.match(mSteamModel)) ){
            com.hjq.toast.ToastUtils.show("门未关好，请检查并确认关好门");
            return false;
        }

        /**
         * 判断是否需要水
         */
        if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(mSteamModel))) {
            if (SteamOvenHelper.isDescale(mSteamOvenOneNew.descaleFlag)) {
                com.hjq.toast.ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");
                return false;
            }
            if (!SteamOvenHelper.isWaterBoxState(mSteamOvenOneNew.waterBoxState)) {
                com.hjq.toast.ToastUtils.show("水箱已弹出，请检查水箱状态");
                return false;
            }
            if (!SteamOvenHelper.isWaterLevelState(mSteamOvenOneNew.waterLevelState)) {
                com.hjq.toast.ToastUtils.show("水箱缺水，请加水");
                return  false;
            }
        }
        return true;
    }

    private void startWork(int mSteamModel, List<Integer> tempList, List<Integer> timeList,
                           String defaultTemp, String defaultTime, String desc,
                           final String code, int count) {
        if (!WorkBeforeCheck(mSteamModel)){
            return;
        }

//        List<Integer> tempTure = TestDatas.createModeDataTemp(tempList);
//        List<Integer> timeTemp = TestDatas.createModeDataTime(timeList);

        setDialogParam(tempList, timeList, defaultTemp, defaultTime, desc, code, count);

    }


    private void startWork(int mSteamModel, List<Integer> tempList, List<Integer> timeList,
                           String defaultTemp, String defaultTime, String desc,
                           final String code, int count,List<Integer> steamList, List<String> titleList,String defaultSteam) {


       if (!WorkBeforeCheck(mSteamModel)){
           return;
       }

//        List<Integer> tempTure = TestDatas.createModeDataTemp(tempList);
//        List<Integer> timeTemp = TestDatas.createModeDataTime(timeList);

        setDialogParam(tempList, timeList, defaultTemp, defaultTime, desc, code, count,steamList,titleList,defaultSteam);

    }



//
    @Subscribe
    public  void finishCov(PageBackEvent pageBackEvent){
        if (pageBackEvent.getPageName().equals("SteamOvenCookCurveActivityBack1")) {
            Message msg = mHandler.obtainMessage();
            msg.what = 0x1234;
            mHandler.sendMessageDelayed(msg,300);
           UIService.getInstance().returnHome();
        }
    }



    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
      Log.e("型号",mSteamOvenOneNew.getID()+"----"+event.pojo.getID());
      if (mSteamOvenOneNew == null || !Objects.equal(mSteamOvenOneNew.getID(), event.pojo.getID())) {
          return;
      }

        Log.e("状态",mSteamOvenOneNew.workState+"-----");
        mSteamOvenOneNew = (SteamOvenOne) event.pojo;

        if (mSteamOvenOneNew.workState == SteamOvenOneWorkStatus.PreHeat){
            isWorking=true;
        }

            if (mSteamOvenOneNew.workState == SteamOvenOneWorkStatus.Working ||
                    mSteamOvenOneNew.workState == SteamOvenOneWorkStatus.HeatFish ||
                    mSteamOvenOneNew.workState == SteamOvenOneWorkStatus.WaterRecovery
                    || mSteamOvenOneNew.workState == SteamOvenOneWorkStatusNew.Working ||
                    mSteamOvenOneNew.workState == SteamOvenOneWorkStatusNew.Pause) {
                closePage();
            }

            if (mSteamOvenOneNew.powerState == SteamOvenOnePowerOnStatus.Pause
                    || mSteamOvenOneNew.powerState == SteamOvenOnePowerOnStatus.Order
                    || mSteamOvenOneNew.powerState == SteamOvenOnePowerOnStatus.WorkingStatus) {
                mCompleteSign = false;
                LogUtils.i("202010201813", "666" + mCompleteSign);
            }


            if (mSteamOvenOneNew.getID().equals(event.pojo.getID())) {
                if ((mSteamOvenOneNew.workState == IntegStoveStatus.workState_free)) {
                    contain.getChildAt(0).setVisibility(View.VISIBLE);
                    contain.getChildAt(1).setVisibility(View.INVISIBLE);//工作页面

                    return;
                }
            }
            if (SteamOvenOnePowerStatus.Off == event.pojo.powerState
                    && !mCompleteSign//关机 且 未工作sign
                    || SteamOvenOnePowerStatus.Wait == event.pojo.powerState
                    && !mCompleteSign //待机 且 未工作sign
                    || SteamOvenOnePowerStatus.On == event.pojo.powerState
                    && SteamOvenOnePowerOnStatus.OperatingState == event.pojo.powerState
                    && !mCompleteSign //开机 且 操作状态 且 未工作sign
            ) {
                LogUtils.i("202010201813", "222" + event.pojo.powerState);
                if (mCloseDialog != null && mCloseDialog.isShow()) {
                    mCloseDialog.dismiss();
                }
                steamOvenWorking620View.closeAllDialog();
                if (mSteamOvenOneNew.getDt().contains("920")&&
                        (SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.JIEDONG
                    ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.SHAJUN
                    ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.FAJIAO
                    ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.GANZAO
                    ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.BAOWEN
                    ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.FURE
                    ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.QINGJIE
                    ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.CHUGOU)) {
                    contain.getChildAt(0).setVisibility(View.VISIBLE);
                    contain.getChildAt(1).setVisibility(View.INVISIBLE);
                    EventUtils.postEvent(new SteamFinish());
                }else {
                    contain.getChildAt(0).setVisibility(View.VISIBLE);//待机页面
                    contain.getChildAt(1).setVisibility(View.INVISIBLE);
                }
                if (steamOvenWorking620View != null) {
                    steamOvenWorking620View.inVisible();
                }
            } else {
                LogUtils.i("202010201813", "777" + mCompleteSign);
                LogUtils.i("202010201813", "powerStatus" + event.pojo.powerState);
                LogUtils.i("202010201813", "powerOnStatus" + event.pojo.powerState);
                if (SteamOvenOnePowerStatus.Off == event.pojo.powerState) {
                    LogUtils.i("202010201813", "444" + event.pojo.powerState);
                    if (mSteamOvenOneNew.getDt().contains("920")
                            &&(SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.JIEDONG
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.SHAJUN
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.FAJIAO
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.GANZAO
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.FURE
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.QINGJIE
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.BAOWEN
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)!=SteamOvenModeEnum.CHUGOU
                    ||mSteamOvenOneNew.recipeId!=0)) {
                        contain.getChildAt(0).setVisibility(View.VISIBLE);
                        contain.getChildAt(1).setVisibility(View.INVISIBLE);

                        EventUtils.postEvent(new SteamFinish());
                    }else{

                        contain.getChildAt(0).setVisibility(View.VISIBLE);
                        contain.getChildAt(1).setVisibility(View.INVISIBLE);
                    }
                    mCompleteSign = false;
                    steamOvenWorking620View.closeAllDialog();
                    if (mCloseDialog != null && mCloseDialog.isShow()) {
                        mCloseDialog.dismiss();
                    }
                }
                LogUtils.i("202010201813", "555" + event.pojo.powerState);
                closeAllDialog2();
//
//                contain.getChildAt(0).setVisibility(View.VISIBLE);
                if (mSteamOvenOneNew.getDt().contains("920")) {

                    if ((SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.JIEDONG
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.SHAJUN
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.FAJIAO
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.GANZAO
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.FURE
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.QINGJIE
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.BAOWEN
                            ||SteamOvenModeEnum.match(mSteamOvenOneNew.mode)==SteamOvenModeEnum.CHUGOU
                            ||mSteamOvenOneNew.recipeId!=0)
                    ||mSteamOvenOneNew.workState == SteamOvenOneWorkStatusNew.Order){
                        contain.getChildAt(0).setVisibility(View.INVISIBLE);
                        contain.getChildAt(1).setVisibility(View.VISIBLE);//工作页面
                    }else {
                        contain.getChildAt(1).setVisibility(View.INVISIBLE);
                        contain.getChildAt(0).setVisibility(View.VISIBLE);
                        startWork();
                    }
                }else {
                    contain.getChildAt(0).setVisibility(View.INVISIBLE);
                    contain.getChildAt(1).setVisibility(View.VISIBLE);//工作页面
                }
                LogUtils.i("202010231509", "mCompleteSign:::" + mCompleteSign);

            }




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
        if (mSteamOvenOneNew == null || !Objects.equal(mSteamOvenOneNew.getID(), event.device.getID()))
            return;
        LogUtils.i("20180829", "event::" + event.isConnected);
        setDeviceSatuse(!event.isConnected);
    }

    private void setDeviceSatuse(boolean isStatus){
        if (isStatus) {
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
        View view = inflater.inflate(R.layout.page_device_steamovenone, container, false);
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

                Log.e(TAG,"onSuccess");
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
        Log.e(TAG,"进来");
        if (mDevice instanceof AbsSteameOvenOne) {
            mSteamOvenOneNew = (SteamOvenOne) mDevice;
        }
        try {
            if (deviceResponse != null) {
                Log.e(TAG,"deviceResponse");
                String backgroundImg = deviceResponse.viewBackgroundImg;
                if (mIvBg != null) {
                    Glide.with(cx).load(backgroundImg).into(mIvBg);
                }
                if (mTvDeviceModelName != null) {
                    mTvDeviceModelName.setText(mSteamOvenOneNew.getName() == null ||  mSteamOvenOneNew.getName().equals(mSteamOvenOneNew.getCategoryName()) ? mSteamOvenOneNew.getDispalyType() : mSteamOvenOneNew.getName());
//                    if (deviceResponse.title.contains("610D")){
//                        mTvDeviceModelName.setText("DB610D");
//                    }
                }
                mainFunc = deviceResponse.modelMap.mainFunc;
                mainList.addAll(mainFunc.deviceConfigurationFunctions) ;
                for (int i=mainList.size()-1;i>=0;--i){
                    if (mainList.get(i).functionName.equals("更多")){
                        mainList.remove(i);
                    }
                }
                Log.e(TAG,"mainList");
                BackgroundFunc backgroundFunc = deviceResponse.modelMap.backgroundFunc;
                List<DeviceConfigurationFunctions> backDeviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions;
                for (int i = 0; i < backDeviceConfigurationFunctions.size(); i++) {
                    if ("isSpecial".equals(backDeviceConfigurationFunctions.get(i).functionCode)) {
                        isSpecial = true;
                    } else if ("needDescaling".equals(backDeviceConfigurationFunctions.get(i).functionCode)) {
                        needDescalingParams = backDeviceConfigurationFunctions.get(i).functionParams;
                    }
                }
                Log.e(TAG,"DeviceConfigurationFunctions");

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
                    Log.e(TAG,"setParamMapSpecial");
                }

                OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
                otherList = otherFunc.deviceConfigurationFunctions;
                HideFunc hideFunc = deviceResponse.modelMap.hideFunc;
                hideFunList = hideFunc.deviceConfigurationFunctions;

                setParamMap();
                Log.e(TAG,"setParamMap");
                if (isSpecial) {

                    getTop4Data();
                    Log.e(TAG,"getTop4Data");
                } else {
                    getTop3Data();
                    Log.e(TAG,"getTop3Data");
                }

                startWork();

            }

        } catch (Exception e) {
            LogUtils.i("20200318", e.getMessage());
            e.printStackTrace();


        }

        Log.e(TAG,"结束");
    }

    private void  startWork(){
        if (mSteamOvenOneNew.workState == SteamOvenOneWorkStatusNew.Working
                ||mSteamOvenOneNew.workState == SteamOvenOneWorkStatusNew.Pause
                ||mSteamOvenOneNew.workState==SteamOvenOneWorkStatusNew.PreHeat
                ||mSteamOvenOneNew.workState==SteamOvenOneWorkStatusNew.preHeatPause
                &&mSteamOvenOneNew.getGuid().getGuid().contains("920")
                &&mSteamOvenOneNew.recipeId==0
        ) {  //预热或者工作中 跳转到

            if (isWorking)
                goCurvePage();
//                                query();

        } else if (mSteamOvenOneNew.workState == SteamOvenOneWorkStatusNew.FINISH) {
            isWorking = true;
        }
    }

    private void initView() {


        Log.e(TAG,"initView  DeviceConfigurationFunctions");
        List<DeviceConfigurationFunctions> functions = null;
        for (int i = 0; i < otherList.size(); i++) {
            if (otherList!=null&&"localCookbook".equals(otherList.get(i).functionCode)) {
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

        Log.e(TAG,"initView  mainList");



            if (mainList != null && mainList.size() > 0 && otherList != null && otherList.size() > 0) {

                steamOvenFirstView = new AbsSteamOvenFirstView(cx, mainList, otherList);

                if (contain!=null)
                contain.addView(steamOvenFirstView);
            } else {
                mainList = mainFunc.deviceConfigurationFunctions;
                if (mainList != null && mainList.size() > 0 && otherList != null && otherList.size() > 0) {

                    steamOvenFirstView = new AbsSteamOvenFirstView(cx, mainList, otherList);

                    contain.addView(steamOvenFirstView);
                } else {
                    ToastUtils.showShort("获取数据失败");
                    UIService.getInstance().popBack();
                    return;
                }
            }

        if (hideFunList != null && hideFunList.size() > 0) {
                steamOvenWorking620View = new AbsSteamOvenWorking620View(cx, hideFunList, mSteamOvenOneNew, localRecipeParams, isSpecial);
                steamOvenWorking620View.setCookbookList(getRecipeList());
            for (DeviceConfigurationFunctions deviceConfigurationFunctions : hideFunList) {
                if (deviceConfigurationFunctions.functionCode.equals("runTimeUpView")) {
                    if (deviceConfigurationFunctions.subView.subViewModelMap.subViewModelMapSubView!=null) {
                        for (DeviceConfigurationFunctions deviceConfigurationFunction :
                                deviceConfigurationFunctions.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions) {
                            if (deviceConfigurationFunction.functionCode.equals("model")) {
                                try {
                                    JSONObject jsonObject
                                            = new JSONObject(deviceConfigurationFunction.functionParams);
                                    JSONObject jsonArray = jsonObject.getJSONObject("param");

                                    for (int i = 0; i < 39; i++) {
                                        if (jsonArray.has(i + "")) {
                                            JSONObject jsn = jsonArray.getJSONObject(i + "");

                                            for (SteamOvenModeEnum steamOvenModeEnum : SteamOvenModeEnum.values()) {
                                                if (steamOvenModeEnum.getCode() == i) {
                                                    steamOvenModeEnum.setValue(jsn.get("value").toString());
                                                }
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (contain == null){
            return;
        }



        if (steamOvenWorking620View != null) {
            contain.addView(steamOvenWorking620View);
//            steamOvenWorking620View.inVisible();
            if (contain.getChildCount()>1) {
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
            }else{
                contain.getChildAt(0).setVisibility(View.INVISIBLE);
            }
        }
        if (contain != null) {
            View view;
            if (from == 1) {
                //待机
                view = contain.getChildAt(1);
            } else {
                //工作中
                view = contain.getChildAt(0);
            }
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }


        if (!mSteamOvenOneNew.isConnected()) {
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

            //上面四个按钮
            steamOvenFirstView.setOnclickMainLister(new AbsSteamOvenFirstView.OnClickMian() {
                @Override
                public void onclickMain(String str) {
                if (!mSteamOvenOneNew.isConnected()) {
                        ToastUtils.showLong(R.string.device_connected);
                    } else {
                        clickMain(str);
                    }

                }

                @Override
                public void onclickOther(String str) {
                  if (!mSteamOvenOneNew.isConnected()) {
                        ToastUtils.showLong(R.string.device_connected);
                    } else {
                        clickOther(str);
                    }
                }
            });
        }
        contain.getChildAt(0).setVisibility(View.VISIBLE);
    }
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        for (ActivityManager.RunningTaskInfo taskInfo : list) {
            if (taskInfo.topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
                return true;
            }
        }
        return false;

    }

    private void setParamMapSpecial() {
        for (int i = 0; i < mainList.size(); i++) {
            paramMapMore.put(mainList.get(i).functionCode, mainList.get(i));
        }
    }


    public void clickMain(String code) {
        LogUtils.i("20180831", " code:" + code);

        if (OvenModeName.more==code) {

                if (falg) {
                    steamOvenFirstView.setUpData(moreList);
                    falg = false;
                } else {
                    steamOvenFirstView.removeMoreView();
                    falg = true;
                }

                if (mSteamOvenOneNew != null) {
                    ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "点击一体机场景功能:" + "更多", "roki_设备");
                }
        }else {
            goRecipe(code);
        }


    }

    private void closePage() {

//        //专业模式
//        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenProMode)) {
//            UIService.getInstance().popBack();
//        }
//        //更多模式页面
        if (UIService.getInstance().isCurrentPage(PageKey.AbsSteamOvenMore)
                &&mSteamOvenOneNew.recipeId==0
                &&mSteamOvenOneNew.mode!=32
                &&mSteamOvenOneNew.mode!=33
                &&mSteamOvenOneNew.mode!=34
                &&mSteamOvenOneNew.mode!=35
                &&mSteamOvenOneNew.mode!=36
                &&mSteamOvenOneNew.mode!=38
                &&mSteamOvenOneNew.mode!=37) {
            UIService.getInstance().popBack();
        }
        if (UIService.getInstance().isCurrentPage(PageKey.MultiListRecipePage)) {
            UIService.getInstance().popBack();
        }

//        //本地自动菜谱
//        if (UIService.getInstance().isCurrentPage(PageKey.LocalCookbook)) {
//            UIService.getInstance().popBack();
//        }
//        //本地自动菜谱
//        if (UIService.getInstance().isCurrentPage(PageKey.Local610CookbookDetailPage)) {
//            UIService.getInstance().popBack().popBack();
//        }
//        //Diy列表页
//        if (UIService.getInstance().isCurrentPage(PageKey.DiyCookbookList)) {
//            UIService.getInstance().popBack();
//        }
//        //Diy 详情页
//        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenDiyDetail)) {
//            UIService.getInstance().popBack().popBack();
//        }
//        //diy编辑页
//        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenEdit)) {
//            UIService.getInstance().popBack().popBack();
//        }
//        //多段页面
//        if (UIService.getInstance().isCurrentPage(PageKey.Multi610RecipePage)) {
//            UIService.getInstance().popBack();
//        }
//        //多段详情页
//        if (UIService.getInstance().isCurrentPage(PageKey.Multi610EditDetailPage)) {
//            UIService.getInstance().popBack().popBack();
//        }
//        //926页面
//        if (UIService.getInstance().isCurrentPage(PageKey.SteamOven926ProMode)) {
//            UIService.getInstance().popBack();
//        }
//        if (UIService.getInstance().isCurrentPage(PageKey.MultiStepC908Mode)) {
//            UIService.getInstance().popBack();
//        }


        //
        if (UIService.getInstance().isCurrentPage(PageKey.SteamOven620ModelSelected)){
            UIService.getInstance().popBack();
        }


//        if (UIService.getInstance().isCurrentPage(PageKey.RecipeCategoryList)){
//            UIService.getInstance().popBack();
//        }


        if (UIService.getInstance().isCurrentPage(PageKey.RecipeSearch2)){
            UIService.getInstance().popBack();
        }




        if (UIService.getInstance().isCurrentPage(PageKey.RecipeSearch)){
            UIService.getInstance().popBack();
        }

        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenProMode)){
            UIService.getInstance().popBack();
        }


        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenProMode)){
            UIService.getInstance().popBack();
        }

        if (UIService.getInstance().isCurrentPage(PageKey.Multi620RecipePage)){
            UIService.getInstance().popBack();
        }

        if (UIService.getInstance().isCurrentPage(PageKey.Multi620EditDetailPage)){
            UIService.getInstance().popBack();
        }


        if (UIService.getInstance().isCurrentPage(PageKey.Multi610EditDetailPage)){
            UIService.getInstance().popBack();
        }



        if (UIService.getInstance().isCurrentPage(PageKey.LocalCookbook620Page)){
            UIService.getInstance().popBack();
        }


        if (UIService.getInstance().isCurrentPage(PageKey.Local620CookbookDetailPage)){
            UIService.getInstance().popBack();
        }




        if (UIService.getInstance().isCurrentPage(PageKey.DiyCookbookList)){
            UIService.getInstance().popBack();
        }
        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenDiyDetail)){
            UIService.getInstance().popBack();
        }
        if (UIService.getInstance().isCurrentPage(PageKey.SteamOvenEdit)){
            UIService.getInstance().popBack();
        }


        ///

        if (UIService.getInstance().isCurrentPage(PageKey.MicroWave620Page)){
            UIService.getInstance().popBack();
        }

    }

    public void clickOther(String code) {
        LogUtils.i("20180809", " code:" + code);
        switch (code) {
            //蒸模式
            case SteamOvenModeName.steamingMode:
//                if (mSteamOvenOneNew.descaleFlag != 0 && !allTimeUseTag) {
//                    descalingDialog();
//                    return;
//                }

                if (mSteamOvenOneNew.waterBoxState == 1) {
                    ToastUtils.showShort(R.string.device_alarm_water_out);
                    return;
                }
                if (mSteamOvenOneNew.doorState == 1 && !TextUtils.equals(mode, "19")) {
                    ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                    return;
                }
                if (mSteamOvenOneNew.WaterStatus == 1 ) {
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
                bdSteam.putString(PageArgumentKey.descaling, needDescalingParams);
                UIService.getInstance().postPage(PageKey.SteamOven620ModelSelected, bdSteam);
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
                bdOven.putString(PageArgumentKey.descaling, needDescalingParams);
                UIService.getInstance().postPage(PageKey.SteamOven620ModelSelected, bdOven);
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
                bdfry.putString(PageArgumentKey.descaling, needDescalingParams);
                UIService.getInstance().postPage(PageKey.SteamOven620ModelSelected, bdfry);
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
                if (mSteamOvenOneNew != null) {
                    ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "点击:蒸箱自动烹饪菜谱", "roki_设备");
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
                if (mSteamOvenOneNew != null) {
                    ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "点击:电烤箱自动烹饪菜谱", "roki_设备");
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
                if (mSteamOvenOneNew != null) {
                    ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "点击:一体机自动烹饪菜谱", "roki_设备");
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
                UIService.getInstance().postPage(PageKey.SteamOvenProMode, bdPro);

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
                UIService.getInstance().postPage(PageKey.MultiListRecipePage, bdMultiStepModel);

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
                bdLocalCookbook.putString(PageArgumentKey.dt, mSteamOvenOneNew.getDt());
//                if (TextUtils.equals(mSteamOvenOne.getDt(), "DB610")){
//                    UIService.getInstance().postPage(PageKey.Local610Cookbook, bdLocalCookbook);
//                }else {

                if (mSteamOvenOneNew.getGuid().getGuid().contains("920")){
                    Intent intent=new Intent(getContext(), LocalCookbook620Activity.class);
                    intent.putExtra(LocalCookbook620Activity.getBdLocalCookbook(),bdLocalCookbook);
                    getContext().startActivity(intent);
                }else {
                    UIService.getInstance().postPage(PageKey.LocalCookbook620Page, bdLocalCookbook);
                }
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
                if (mSteamOvenOneNew != null) {
                    ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "点击:蒸烤一体机自动烹饪菜谱", "roki_设备");
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
                bdFz.putString(PageArgumentKey.descaling, needDescalingParams);
                UIService.getInstance().postPage(PageKey.SteamOven620ModelSelected, bdFz);

                break;
            case SteamOvenModeName.microwaveModel:
                List<DeviceConfigurationFunctions> microwaveFunctions = null;
                String titleMi = null;
                Bundle bdFz1 = new Bundle();
                bdFz1.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < otherList.size(); i++) {
                    if (SteamOvenModeName.microwaveModel.equals(otherList.get(i).functionCode)) {
                        microwaveFunctions = otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        titleMi = otherList.get(i).subView.title;
                    }
                }
                bdFz1.putSerializable(PageArgumentKey.List, (Serializable) microwaveFunctions);
                bdFz1.putString(PageArgumentKey.title, titleMi);
                UIService.getInstance().postPage(PageKey.MicroWave620Page, bdFz1);
                break;

            case SteamOvenModeName.addSteamRoastModel:

                List<DeviceConfigurationFunctions> addFunctions = null;
                String titleOvenAddWater = null;
                Bundle bdOven1AddWater = new Bundle();
                bdOven1AddWater.putString(PageArgumentKey.Guid, mGuid);
                for (int i = 0; i < otherList.size(); i++) {
                    if ("addSteamRoastModel".equals(otherList.get(i).functionCode)) {
                        addFunctions = otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                        titleOvenAddWater = otherList.get(i).subView.title;
                    }
                }
                bdOven1AddWater.putSerializable(PageArgumentKey.List, (Serializable) addFunctions);
                bdOven1AddWater.putString(PageArgumentKey.title, titleOvenAddWater);
                bdOven1AddWater.putString(PageArgumentKey.descaling, needDescalingParams);
                UIService.getInstance().postPage(PageKey.SteamOven620ModelSelected, bdOven1AddWater);
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

        Log.e(TAG,"initView");
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
                if (mSteamOvenOneNew.doorState == 1 && !TextUtils.equals(model, "19")) {
                    ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                    return;
                }
                if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(Integer.parseInt(model)))){
                    if (!SteamOvenHelper.isWaterBoxState(mSteamOvenOneNew.waterBoxState)) {
                        ToastUtils.showShort("水箱已弹出，请检查水箱状态");
                        return;
                    }
                    if (!SteamOvenHelper.isWaterLevelState(mSteamOvenOneNew.waterLevelState)) {
                        ToastUtils.showShort("水箱缺水，请加水");
                        return;
                    }
                }

                //干燥
                if ("33".equals(model)) {
                    String title = param.getJSONObject("defaultTips").getString("title");
                    String content = param.getJSONObject("defaultTips").getString("content");
                    String positiveButton = param.getJSONObject("defaultTips").getString("positiveButton");
                    String negativeButton = param.getJSONObject("defaultTips").getString("negativeButton");
                    dryTipsDialog(title, content, positiveButton, negativeButton, code);
                    //除垢
                } else if ("35".equals(model)) {
                    String title = param.getJSONObject("defaultTips").getString("title");
                    String content = param.getJSONObject("defaultTips").getString("content");
                    String positiveButton = param.getJSONObject("defaultTips").getString("positiveButton");
                    String negativeButton = param.getJSONObject("defaultTips").getString("negativeButton");

                    descalingTipsDialog(title, content, positiveButton, negativeButton, code);
                    //EXP
                } else if ("14".equals(model)) {
                    OvenExpParamBean ovenParams = null;
                    try {
                        ovenParams = JsonUtils.json2Pojo(jsonObject.toString(), OvenExpParamBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (ovenParams != null) {
                        List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
                        List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
                        List<Integer> time = ovenParams.getParam().getSetTime().getValue();


                        String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();

                        String timeDefault = ovenParams.getParam().getDefaultSetTime().getValue();

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
                        if (mSteamOvenOneNew.descaleFlag != 0) {
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

                    JSONArray setTemp = param.getJSONObject("setTemp").getJSONArray("value");
                    String defaultSetTemp = param.getJSONObject("defaultSetTemp").getString("value");
                    JSONArray setTime = param.getJSONObject("setTime").getJSONArray("value");
                    String defaultSetTime = param.getJSONObject("defaultSetTime").getString("value");
                    List<Integer> tempList = new ArrayList<>();

                    List<Integer> steamList = new ArrayList<>();

                    List<String> steamTitle = new ArrayList<>();

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


                    if (param!=null&&param.has("setSteam")){
                        JSONArray setSteam = param.getJSONObject("setSteam").getJSONArray("value");
                        for (int i = 0; i < setSteam.length(); i++) {
                            Integer temp = Integer.parseInt(setSteam.get(i).toString());
                            steamList.add(temp);
                        }


                        JSONArray setTitle = param.getJSONObject("setSteam").getJSONArray("title");

                        for (int i = 0; i < setTitle.length(); i++) {
                            String temp = (String) setTitle.get(i);
                            steamTitle.add(temp);
                        }
                        String defaultSetSteam = param.getJSONObject("defaultSetSteam").getString("value");


                        if (mSteamOvenOneNew.descaleFlag != 0) {
                            if (allTimeUseTag) {
                                startWork(Short.parseShort(model),tempTure, timeTemp, defaultSetTemp, defaultSetTime, msg, code, timeList.get(2),
                                        steamList,  steamTitle, defaultSetSteam);
                            } else {
                                descalingDialog();
                            }
                        } else {
                            startWork(Short.parseShort(model),tempTure, timeTemp, defaultSetTemp, defaultSetTime, msg, code, timeList.get(2)
                            , steamList,  steamTitle, defaultSetSteam);

                        }
                        return;
                    }
//                    if (mSteamOvenOneNew.doorStatusValue == 1) {
//                        ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
//                        return;
//                    }
                    //0 不需要 1 需要



                    if (mSteamOvenOneNew.descaleFlag != 0) {
                        if (allTimeUseTag) {
                            startWork(Short.parseShort(model),tempTure, timeTemp, defaultSetTemp, defaultSetTime, msg, code, timeList.get(2));
                        } else {
                            descalingDialog();
                        }
                    } else {
                        startWork(Short.parseShort(model),tempTure, timeTemp, defaultSetTemp, defaultSetTime, msg, code, timeList.get(2));

                    }


                }

            } else {
//                if (mSteamOvenOneNew.getGuid().getGuid().contains("920")) {
//                    query();
//                }
                sendMul(code);
                long cookId = jsonObject.getLong("cookbookId");
                RecipeDetailPage.show(cookId, RecipeDetailPage.DeviceRecipePage,
                        RecipeRequestIdentification.RECIPE_SORTED, mGuid);
                if (mSteamOvenOneNew != null) {
                    ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "点击一体机场景功能:" + functionName, "roki_设备");
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
        if (mSteamOvenOneNew == null){
            return;
        }
        if (!mSteamOvenOneNew.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        if (mSteamOvenOneNew.powerState == SteamOvenOnePowerOnStatusNew.Off) {
            ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
            return;
        }
//        ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "关机", "roki_设备");


        //待机的状态下直接下发
        if (mSteamOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.working||
        mSteamOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeatPause
                ||mSteamOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.preWorkHeat
                ||mSteamOvenOneNew.workState == SteamOvenOnePowerOnStatusNew.waitForWork){
            IRokiDialog mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
            mCloseDialog.setTitleText(R.string.off_device);
            mCloseDialog.setContentText(R.string.device_off_content);
            mCloseDialog.show();
            mCloseDialog.setOkBtn(R.string.ok_btn, v -> {
                        if (mCloseDialog != null && mCloseDialog.isShow()) {
                            mCloseDialog.dismiss();
                        }

                        mSteamOvenOneNew.setSteamWorkStatus(IntegStoveStatus.workCtrl_stop , (short) 2, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                isWorking=true;
                                ToastUtils.show(RobamApp.getInstance().getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
//                                setDeviceSatuse(true);
                                mCompleteSign = false;
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });

                    }
            );
            mCloseDialog.setCancelBtn(R.string.can_btn, v -> {
            }
            );
        }else{
            mSteamOvenOneNew.setSteamWorkStatus(IntegStoveStatus.workCtrl_stop, (short) 2, new VoidCallback() {
                @Override
                public void onSuccess() {
                    isWorking=true;

                    ToastUtils.show(RobamApp.getInstance().getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
//                    setDeviceSatuse(true);
                    mCompleteSign = false;
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }


    }


    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, mGuid);
        UIService.getInstance().postPage(PageKey.AbsSteamOvenMore, bd);
        if (mSteamOvenOneNew == null) {
            return;
        }
        ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "点击:更多", "roki_设备");
    }

    private static final String TAG = "AbsDeviceSteamOvenOne62";


    //蒸、烤、辅助模式
    private void sendSteamOvenFZModel(final String code) {


             short steam=0;
            if (steamList!=null&&titleList!=null){
                for (int i = 0; i < titleList.size(); i++) {
                    if (selectSteam!=null&&titleList.get(i).equals(selectSteam)){
                        steam=steamList.get(i).byteValue();
                    }
                }
            }
        Log.e(TAG,steam+"---"+"===");
            mSteamOvenOneNew.setSteameOvenOneRunMode(Short.parseShort(model),Integer.parseInt(selectTime), Short.parseShort(selectTemp),
                    (short) 0,steam, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("sendNew", "成功");
//                    if (mSteamOvenOneNew.getGuid().getGuid().contains("920")) {
//                        query();
//                    }
                    isWorking=true;
                    sendMul(code);
                    steamList=null;
                    titleList=null;

                }

                @Override
                public void onFailure(Throwable t) {
                    steamList=null;
                    titleList=null;
                }
            });

    }

    /**
     * 干燥模式
     */
    private void dryMode(final String code) {
       if (!WorkBeforeCheck(19)){
           return;
       };

        mSteamOvenOneNew.setSteameOvenOneRunMode(Short.parseShort("19"),
                (short) 10, (short) 160, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
//                        if (mSteamOvenOneNew.getGuid().getGuid().contains("920")) {
//                            query();
//                        }
                        isWorking=true;
                        sendMul(code);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    public   boolean isWorking=true;
    private  void goCurvePage() {
        if (!isForeground(getContext(), "SteamOvenCookCurveActivity")
                && !isForeground(getContext(), "RecipePreviewActivity")
                && !isForeground(getContext(), "RecipeActivity")
                && !isForeground(getContext(), "RecipeSuccessActivity")
                && !isForeground(getContext(), "CustomizeRecipeOrPreviewActivity")
                ) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, mSteamOvenOneNew.getGuid().getGuid());
            Intent intent = new Intent(getContext(), SteamOvenCookCurveActivity.class);
            intent.putExtras(bd);
            getActivity().startActivity(intent);
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

    private List<Integer> steamList;
    private List<String> titleList;
    private void setDialogParam(List<Integer> tempList, List<Integer> timeList, String defaultTemp, String defaultTime, String desc, final String code, int count,
                                List<Integer> steamList, List<String> titleList,String defaultSteam) {
        short newDefaultTemp = (Short.parseShort(defaultTemp));
        short newDefaultTime = (Short.parseShort(defaultTime));

        this.steamList=steamList;
        this.titleList=titleList;
        int defaultSteamPos=0;
        for (int i = 0; i < timeList.size(); i++) {
            if (defaultSteam.equals(titleList.get(i))){
                defaultSteamPos=i;
                break;
            }
        }



        this.selectSteam=defaultSteam;
        //拿到时间温度的索引值
        int indexTemp=0;
        for (int i = 0; i < tempList.size(); i++) {
            if (newDefaultTemp==tempList.get(i)){
                indexTemp=i;
                break;
            }
        }
        int indexTime=0;
        for (int i = 0; i < tempList.size(); i++) {
            if (newDefaultTime==timeList.get(i)){
                indexTime=i;
                break;
            }
        }



//        int indexTime = newDefaultTime - timeList.get(0);
//        if (timeList.size() > 1) {
//            indexTime = indexTime / (timeList.get(1) - timeList.get(0));
//        }
        DialogType_05 mRokiDialog = (DialogType_05) RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_05);
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
        mRokiDialog.setContentText(desc);
        mRokiDialog.setWheelViewData(
                titleList,
                HelperRikaData.getTempData2(tempList),

                HelperRikaData.getTimeData3(timeList, count),

                false,
                defaultSteamPos,
                indexTemp,

                indexTime,
                contentFront -> {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = contentFront;
                    msg.what = 3;
                    mHandler.sendMessage(msg);
                }, contentCenter->{
                    Message msg = mHandler.obtainMessage();
                    msg.obj = contentCenter;
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }, contentRear -> {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = contentRear;
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                });

        mRokiDialog.show();
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
                contentFront -> {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = contentFront;
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }, null, contentRear -> {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = contentRear;
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                });

        mRokiDialog.show();


    }




    private boolean needShowDialog(String mode) {
        if (allTimeUseTag) {
            return false;
        }
        if (TextUtils.equals(mode,RobamApp.getInstance().getString(R.string.device_steamOvenOne_name_fengpeikao))) {
            return false;
        }
        if ((TextUtils.equals(mode,"干燥"))) {
            return false;
        }
        if (mSteamOvenOneNew.descaleFlag == 0) {
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
//
//                    mSteamOvenOneNew.setSteameOvenOneRunMode(mode, (short) setTime,
//                            (short) setTempUp, (short) 0, (short) setTempDown, (short) 0, (short) 0, new VoidCallback() {
//                                @Override
//                                public void onSuccess() {
//                                    if (mSteamOvenOneNew != null) {
//                                        ToolUtils.logEvent(mSteamOvenOneNew.getDt(), "开始专业烘烤:" + setTempUp + ":" + setTempDown + ":" + setTime, "roki_设备");
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Throwable t) {
//                                }
//                            });

//            mSteamOvenOneNew.setSteameOvenOneRunMode(expMode, (short) setTime,
//                    (short) setTempUp, (short) 0, (short) setTempDown, (short) 0, (short) 0, new VoidCallback() {
//                        @Override
//                        public void onSuccess() {
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//                        }
//                    });

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
                Log.e(TAG,"onCancel");
            }
            @Override
            public void onConfirm(int index1, int index2, int index3) {
                Log.e(TAG,"onConfirm");
                int tempU=index1;
                int tempD=index2;
                int timeP=index3;
                mSteamOvenOneNew.setSteameOvenOneRunModeExp((short) 14, timeP, (short) tempU, (short)tempD, 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul("14");
                    }
                    @Override
                    public void onFailure(Throwable t) {}
                });
            }
        });
    }



}
