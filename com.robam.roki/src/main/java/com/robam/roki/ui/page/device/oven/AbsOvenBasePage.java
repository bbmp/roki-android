package com.robam.roki.ui.page.device.oven;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.FunctionMore;
import com.legent.plat.pojos.device.FunctionTop3;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.AbsOvenCookStepNameEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.events.OvenWorkBottomFinishEvent;
import com.robam.common.events.OvenWorkFinishEvent;
import com.robam.common.events.OvenWorkTopFinishEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.FunctionsTop4;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.AbsOvenExpDialog;
import com.robam.roki.ui.dialog.AbsOvenModeSettingDialog;
import com.robam.roki.ui.dialog.AbsOvenModeSettingHasDbDialog;
import com.robam.roki.utils.DataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsOvenBasePage extends BasePage {

    protected String mGuid;
    protected AbsOven oven;
    @InjectView(R.id.iv_oven_bg)
    ImageView ivOvenBg;
    @InjectView(R.id.iv_oven_back)
    ImageView ivOvenBack;
    @InjectView(R.id.oven_name)
    TextView ovenName;
    @InjectView(R.id.oven_switch)
    ImageView ovenSwitch;
    @InjectView(R.id.oven_more)
    ImageView ovenMore;
    //??????
    @InjectView(R.id.contain)
    FrameLayout contain;
    long userId = Plat.accountService.getCurrentUserId();
    String dt;
    String dc;

    List<DeviceConfigurationFunctions> bgFunList;
    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;
    List<DeviceConfigurationFunctions> moreList;


    AbsOvenFirstView ovenFirstView;

    //????????????
    AbsOvenWorkingView ovenWorkingView;
    //035????????????
    AbsOvenTopAndBottomWorkingView rq035WorkingView;
    public HashMap<String, String> paramMap = new HashMap<>();
    public HashMap<String, DeviceConfigurationFunctions> paramMapMore = new HashMap<>();
    int from;
    boolean isComplete = false;
    boolean isCompleteWhole = false;
    static boolean isCompleteTop = false;
    static boolean isCompleteBottom = false;


    List<FunctionTop3> top3s = new ArrayList<>();
    List<FunctionMore> mores = new ArrayList<>();
    String version = null;

    IRokiDialog closedialog = null;

    //??????????????????
    public boolean isSpecial = false;

    private List<FunctionsTop4> top4s;

    public String modeParams = "";
    public String cookBookParams = "";
    protected static AbsOvenExpDialog absOvenExpDialog;
    protected static AbsOvenModeSettingHasDbDialog absOvenModeSettingHasDbDialog;
    protected static AbsOvenModeSettingDialog absOvenModeSettingDialog;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setDataToView((Reponses.DeviceResponse) msg.obj);
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd.getInt(PageArgumentKey.from);
        oven = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.abs_oven_main, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        if (oven != null) {
            dt = oven.getDt();
            dc = oven.getDc();
        }
        version = DataUtils.readJson(cx, "version" + dt);
        if (version == null) {
            getDataMethod();
        } else {
            getVersionMethod();
        }


    }


    private void getTop3() {
        //?????????????????????Top3???????????????
        //api.myroki.com:80/rest/ops/api/functionAcount/get
        LogUtils.i("2020100901","userId:::"+userId);
        LogUtils.i("2020100901","mGuid:::"+mGuid);
        LogUtils.i("2020100901","dc:::"+dc);
        CloudHelper.getLookUpCode(userId, mGuid, dc, new Callback<Reponses.GetLookUpResponse>() {
            @Override
            public void onSuccess(Reponses.GetLookUpResponse getLookUpResponse) {
                top3s = getLookUpResponse.functionTop3s;
                mores = getLookUpResponse.functionMores;
                LogUtils.i("2020100901","top3s:::"+top3s);
                LogUtils.i("2020100901","mores:::"+mores);
                setSortList();
                showFirstAndWorkingView();

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void getVersionMethod() {
        CloudHelper.getCheck(dt, version, new Callback<Reponses.GetCheckResponse>() {
            @Override
            public void onSuccess(Reponses.GetCheckResponse getCheckResponse) {
                if (getCheckResponse == null) return;

                if (!getCheckResponse.isLast) {//false?????????
                    getDataMethod();
                } else {//????????????????????????????????????????????????????????????
                    String ovenData = DataUtils.readJson(cx, "oven" + dt);
                    Reponses.DeviceResponse deviceOven = null;
                    try {
                        deviceOven = JsonUtils.json2Pojo(ovenData, Reponses.DeviceResponse.class);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = deviceOven;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void getDataMethod() {
        LogUtils.i("20200825","userId:"+userId);
        LogUtils.i("20200825","dt:"+dt);
        LogUtils.i("20200825","dc:"+dc);
        CloudHelper.getDeviceByParams(userId, dt, dc, new Callback<Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;

                DataUtils.writeJson(cx, deviceResponse.version, "version" + dt, false);
                DataUtils.writeJson(cx, deviceResponse.toString(), "oven" + dt, false);
                try {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = deviceResponse;
                    handler.sendMessage(msg);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        });
    }


    private void setDataToView(Reponses.DeviceResponse deviceResponse) {
        try {
            //???????????????
            String backgroundImg = deviceResponse.viewBackgroundImg;
            Glide.with(cx).load(backgroundImg).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivOvenBg);
            //??????
            ovenName.setText(deviceResponse.title);

            BackgroundFunc bgFunc = deviceResponse.modelMap.backgroundFunc;
            bgFunList = bgFunc.deviceConfigurationFunctions;
            if (bgFunList.size() != 0) {
                DeviceConfigurationFunctions deviceConfigurationFunction = bgFunList.get(bgFunList.size() - 1);
                if ("isSpecial".equals(deviceConfigurationFunction.functionCode) && "1".equals(deviceConfigurationFunction.functionParams)) {
                    isSpecial = true;
                }
                List<DeviceConfigurationFunctions> runTimeUpFunctioins = null;
                for (int i = 0; i < bgFunList.size(); i++) {
                    if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                        runTimeUpFunctioins = bgFunList.get(i)
                                .subView
                                .subViewModelMap
                                .subViewModelMapSubView
                                .deviceConfigurationFunctions;
                    }
                }
                for (int i = 0; i < runTimeUpFunctioins.size(); i++) {
                    if ("model".equals(runTimeUpFunctioins.get(i).functionCode)) {
                        modeParams = runTimeUpFunctioins.get(i).functionParams;
                    }
                }
            }
            OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
            otherList = otherFunc.deviceConfigurationFunctions;

            List<DeviceConfigurationFunctions> otherFunctions = null;
            for (int i = 0; i < otherList.size(); i++) {
                if ("localCookbook".equals(otherList.get(i).functionCode)) {
                    otherFunctions = otherList.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                }
            }
            if (otherFunctions != null) {
                for (int i = 0; i < otherFunctions.size(); i++) {
                    if ("cookBookTop".equals(otherFunctions.get(i).functionCode)) {
                        cookBookParams = otherFunctions.get(i).functionParams;
                    }
                }
            }

            setParamMap();
            MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
            //?????????????????????
            mainList = mainFunc.deviceConfigurationFunctions;
            if (!isSpecial) {
                if (mainList.size() > 1) {
                    moreList = mainList.get(mainList.size() - 1).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                }
            }
            //???mainList??????????????????paramMapMore??????
            setParamMapMore();
            LogUtils.i("20201009","isSpecial:::"+isSpecial);
            if (!isSpecial) {
                getTop3();
            } else {
                getTop4();
            }
        } catch (NullPointerException e) {
            LogUtils.i("20200401000", e.getMessage());
            e.printStackTrace();

        }
    }

    private void showFirstAndWorkingView() {
        ovenFirstView = new AbsOvenFirstView(cx, mainList, otherList,oven);
        ovenWorkingView = new AbsOvenWorkingView(cx, bgFunList, otherList, oven, modeParams, cookBookParams,mGuid);
        rq035WorkingView = new AbsOvenTopAndBottomWorkingView(cx, bgFunList, otherList, oven, modeParams, cookBookParams);

        contain.removeAllViews();
        contain.addView(ovenFirstView);//0
        contain.addView(ovenWorkingView);//1
        contain.addView(rq035WorkingView);//2
        LogUtils.i("20201009","from:::"+from);
        //?????????????????????
        if (from == 1) {
            contain.getChildAt(0).setVisibility(View.VISIBLE);
            contain.getChildAt(1).setVisibility(View.INVISIBLE);
            contain.getChildAt(2).setVisibility(View.INVISIBLE);
        } else {
            //?????????
            try {
                JSONObject modeParamsObject = new JSONObject(modeParams);
                JSONObject modeParam = modeParamsObject.getJSONObject("param");
                if (dt.equals("Q082A")) {
                    contain.getChildAt(0).setVisibility(View.INVISIBLE);
                    contain.getChildAt(1).setVisibility(View.VISIBLE);
                    contain.getChildAt(2).setVisibility(View.INVISIBLE);
                }else{
                    //????????????
                    if (oven.runP != 0) {
                        JSONObject jsonObject = modeParam.getJSONObject(String.valueOf(oven.runP));
                        String isCombinations = jsonObject.optString("isCombination");
                        String isCombination = isCombinations.equals("") ? "false" : isCombinations;
                        //???????????? ????????????
                        if ("true".equals(isCombination)) {
                            //??????
                            contain.getChildAt(0).setVisibility(View.INVISIBLE);
                            contain.getChildAt(1).setVisibility(View.INVISIBLE);
                            contain.getChildAt(2).setVisibility(View.VISIBLE);

                            //??????
                        } else {
                            contain.getChildAt(0).setVisibility(View.INVISIBLE);
                            contain.getChildAt(1).setVisibility(View.VISIBLE);
                            contain.getChildAt(2).setVisibility(View.INVISIBLE);
                        }
                        //??????????????????
                    } else {
                        JSONObject cookBookParamsObject = new JSONObject(cookBookParams);
                        JSONObject autoObj = cookBookParamsObject.getJSONObject(String.valueOf(oven.autoMode));
                        String isCombination = autoObj.getString("isCombination");
                        if ("true".equals(isCombination)) {
                            //??????
                            contain.getChildAt(0).setVisibility(View.INVISIBLE);
                            contain.getChildAt(1).setVisibility(View.INVISIBLE);
                            contain.getChildAt(2).setVisibility(View.VISIBLE);
                        } else {
                            //??????
                            contain.getChildAt(0).setVisibility(View.INVISIBLE);
                            contain.getChildAt(1).setVisibility(View.VISIBLE);
                            contain.getChildAt(2).setVisibility(View.INVISIBLE);
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        if (!oven.isConnected()) {
            disConStatus();
        }

        ovenFirstView.setOnclickMainLister(new AbsOvenFirstView.OnClickMian() {
            @Override
            public void onclickMain(String str) {
                if (getIsCon()) {
                    ToastUtils.show("?????????", Toast.LENGTH_SHORT);
                    return;
                }
                clickMain(str);
            }

            @Override
            public void onclickOther(String str) {
                if (getIsCon()) {
                    ToastUtils.show("?????????", Toast.LENGTH_SHORT);
                    return;
                }
                clickOther(str);
            }
        });
    }

    public void clickMain(String str) {
    }

    public void clickOther(String str) {
    }


    private void setSortList() {
        mainList.clear();
        moreList.clear();
        for (int i = 0; i < top3s.size(); i++) {
            for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                if (top3s.get(i).functionCode.equals(en.getKey())) {
                    mainList.add(en.getValue());
                }
            }
        }

        for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
            if ("more".equals(en.getKey())) {
                mainList.add(en.getValue());
            }
        }

        for (int i = 0; i < mores.size(); i++) {
            for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                if (mores.get(i).functionCode.equals(en.getKey())) {
                    moreList.add(en.getValue());
                }
            }
        }
    }

    private void setSortListForTop4() {
        if (mainList != null) {
            mainList.clear();
        }
        for (int i = 0; i < top4s.size(); i++) {
            for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                if (top4s.get(i).functionCode.equals(en.getKey())) {
                    mainList.add(en.getValue());
                }
            }
        }
    }


    private void setParamMapMore() {
        for (int i = 0; i < mainList.size(); i++) {
            paramMapMore.put(mainList.get(i).functionCode, mainList.get(i));
        }

        if (!isSpecial) {
            for (int i = 0; i < moreList.size(); i++) {
                paramMapMore.put(moreList.get(i).functionCode, moreList.get(i));
            }
        }

    }

    private void setParamMap() {
        for (int i = 0; i < otherList.size(); i++) {
            paramMap.put(otherList.get(i).functionCode, otherList.get(i).functionParams);
        }
    }

    @Subscribe
    public void onEvent(AbsOvenCookStepNameEvent event) {
        PreferenceUtils.setString("codeName", event.codeName);
    }

    //??????????????????
    @Subscribe
    public void onEvent(OvenWorkFinishEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.oven.getID()))
            return;
        if (event.finish != 0) return;
        ovenWorkingView.completeWork();
        isComplete = true;
        isCompleteWhole = true;
        LogUtils.i("20201009999","?????????????????? isCompleteWhole::"+isCompleteWhole);
    }

    //??????????????????
    @Subscribe
    public void onEvent(OvenWorkTopFinishEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.oven.getID())) {
            return;
        }
        if (event.finish != 0) {
            return;
        }
        isCompleteTop = true;
        rq035WorkingView.completeTopWork();
        LogUtils.i("20201009999","?????????????????? isCompleteTop::"+isCompleteTop);
    }

    //??????????????????
    @Subscribe
    public void onEvent(OvenWorkBottomFinishEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.oven.getID()))
            return;
        if (event.finish != 0) return;
        isCompleteBottom = true;
        rq035WorkingView.completeBottomWork();
        LogUtils.i("20201009999","?????????????????? isCompleteBottom::"+isCompleteBottom);
    }


    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (!event.isConnected) {
            isConnect = false;
            disConStatus();
        } else {
            isConnect = true;
        }
    }


    boolean isConnect = true;

    private void disConStatus() {
        ToastUtils.show("??????????????????", Toast.LENGTH_LONG);
        contain.getChildAt(1).setVisibility(View.INVISIBLE);
        contain.getChildAt(2).setVisibility(View.INVISIBLE);
        contain.getChildAt(0).setVisibility(View.VISIBLE);
        ovenFirstView.disConnect(true);
    }

    boolean flag = true;

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        short status = event.pojo.status;
        short status2Values = event.pojo.status2Values;
        oven = (AbsOven) event.pojo;
        //?????????????????????
        AbsOvenBasePage.this.oven = event.pojo;
        if (IRokiFamily.RR039.equals(oven.getDt())) {
            if (event.pojo.time <= 4 && flag) {
                ovenWorkingView.completeWork();
                isComplete = true;
                flag = false;
            }
            if (isComplete) {
                if (status != OvenStatus.On && status != OvenStatus.Working) {
                    isComplete = false;
                }
                return;
            }
        } else {

            if (isComplete) {
                if (status != OvenStatus.On) {
                    isComplete = false;
                }
                return;
            }
        }
        flag = true;

        if ("RQ035".equals(event.pojo.getDt())) {
            statusShow(status, status2Values, event.pojo);
        } else {
            statusShow(status, event.pojo);

        }


    }


    private void statusShow(short status, AbsOven oven) {
        switch (status) {
            case OvenStatus.Off:
            case OvenStatus.On:
            case OvenStatus.Wait:
                ovenWorkingView.closeAllDialog();
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                ovenFirstView.disConnect(false);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                break;
            case OvenStatus.AlarmStatus:
                break;
            default:
                if (absOvenExpDialog != null && absOvenExpDialog.isShowing()) {
                    absOvenExpDialog.dismiss();
                }
                if (absOvenModeSettingHasDbDialog != null && absOvenModeSettingHasDbDialog.isShowing()) {
                    absOvenModeSettingHasDbDialog.dismiss();
                }
                if (absOvenModeSettingDialog != null && absOvenModeSettingDialog.isShowing()) {
                    absOvenModeSettingDialog.dismiss();
                }

                contain.getChildAt(1).setVisibility(View.VISIBLE);
                contain.getChildAt(0).setVisibility(View.INVISIBLE);
                ovenWorkingView.updateStatus(oven);
                break;
        }
    }


    private void statusShow(short status, short status2Values, AbsOven oven) {

        try {
            JSONObject modeParamsObject = new JSONObject(modeParams);
            JSONObject cookBookParamsObject = new JSONObject(cookBookParams);
            JSONObject modeParam = modeParamsObject.getJSONObject("param");

            //????????????
            if (oven.runP != 0 && oven.autoMode == 0) {
                JSONObject jsonObject = modeParam.getJSONObject(String.valueOf(oven.runP));
                String isCombination = jsonObject.getString("isCombination");
                //???????????? ????????????
                if ("true".equals(isCombination)) {
                    isCombination(status, status2Values, oven);
                    //??????
                } else {
                    isCompleteWhole(status, oven);
                }

                //??????????????????
            } else if (oven.runP == 0 && oven.autoMode != 0) {
                JSONObject autoObj = cookBookParamsObject.getJSONObject(String.valueOf(oven.autoMode));
                String isCombination = autoObj.getString("isCombination");
                //???????????? ????????????
                if ("true".equals(isCombination)) {
                    isCombination(status, status2Values, oven);
                } else {
                    //??????
                    isCompleteWhole(status, oven);
                }

            } else {


                if (oven.status == OvenStatus.Off || oven.status == OvenStatus.On) {
                    isCompleteTop = false;
                }

                if (oven.status2Values == OvenStatus.Off || oven.status2Values == OvenStatus.On) {
                    isCompleteBottom = false;
                }


                //???????????????
                ovenWorkingView.closeAllDialog();
                contain.getChildAt(0).setVisibility(View.VISIBLE);//ovenFirstView
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                contain.getChildAt(2).setVisibility(View.INVISIBLE);
                ovenFirstView.disConnect(false);

            }


        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.i("2020060201", "error::" + e.getMessage());
        }


    }

    private void isCompleteWhole(short status, AbsOven oven) {
        switch (status) {
            case OvenStatus.Working:
            case OvenStatus.Order:
            case OvenStatus.PreHeat:
            case OvenStatus.Pause:
                if (absOvenExpDialog != null && absOvenExpDialog.isShowing()) {
                    absOvenExpDialog.dismiss();
                }
                closePage();

                contain.getChildAt(0).setVisibility(View.INVISIBLE);
                contain.getChildAt(1).setVisibility(View.VISIBLE);
                contain.getChildAt(2).setVisibility(View.INVISIBLE);
                ovenWorkingView.updateStatus(oven);
                break;
            //??????
            case OvenStatus.AlarmStatus:
                break;
            default:

                ovenWorkingView.closeAllDialog();
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                contain.getChildAt(2).setVisibility(View.INVISIBLE);
                ovenFirstView.disConnect(false);
                break;
        }
    }

    private void isCombination(short status, short status2Values, AbsOven oven) {


        //??????
        if (status == OvenStatus.Off || status == OvenStatus.On || status == OvenStatus.Wait) {
            //??????
            if (status2Values == OvenStatus.Off || status2Values == OvenStatus.On || status2Values == OvenStatus.Wait) {


                if (isCompleteTop || isCompleteBottom) {
                    closePage();
                    contain.getChildAt(0).setVisibility(View.INVISIBLE);//ovenFirstView
                    contain.getChildAt(1).setVisibility(View.INVISIBLE);//working
                    contain.getChildAt(2).setVisibility(View.VISIBLE);//035Working
                    rq035WorkingView.updateStatus(oven, modeParams, cookBookParams);
                } else {

                    if (oven.status == OvenStatus.Off || oven.status == OvenStatus.On) {
                        isCompleteTop = false;
                    }

                    if (oven.status2Values == OvenStatus.Off || oven.status2Values == OvenStatus.On) {
                        isCompleteBottom = false;
                    }
                    ovenWorkingView.closeAllDialog();
                    contain.getChildAt(0).setVisibility(View.VISIBLE);//ovenFirstView
                    contain.getChildAt(1).setVisibility(View.INVISIBLE);
                    contain.getChildAt(2).setVisibility(View.INVISIBLE);
                    ovenFirstView.disConnect(false);
                }


            } else if (status2Values == OvenStatus.AlarmStatus) {
                //???????????? ??????????????????
            } else {

                closePage();
                contain.getChildAt(0).setVisibility(View.INVISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                contain.getChildAt(2).setVisibility(View.VISIBLE);
                rq035WorkingView.updateStatus(oven, modeParams, cookBookParams);
            }


        } else if (status == OvenStatus.AlarmStatus) {
            //???????????? ??????????????????

        } else {

            closePage();
            contain.getChildAt(0).setVisibility(View.INVISIBLE);
            contain.getChildAt(1).setVisibility(View.INVISIBLE);
            contain.getChildAt(2).setVisibility(View.VISIBLE);//035Working
            rq035WorkingView.updateStatus(oven, modeParams, cookBookParams);
        }


    }

    private void closePage() {
        //????????????
        if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenZoning)) {
            UIService.getInstance().popBack();
        }
        //??????????????????
        if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenZoneSet)) {
            UIService.getInstance().popBack();
        }
        //??????????????????
        if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenLocalRecipe)) {
            UIService.getInstance().popBack();
        }
        //??????diy?????????
        if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenBakeDiyList)) {
            UIService.getInstance().popBack();
        }
        //??????diy?????????
        if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenBakeDiyDetail)) {
            UIService.getInstance().popBack().popBack();
        }
        //??????diy?????????
        if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenBakeEdit)) {
            UIService.getInstance().popBack();

            if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenBakeDiyList)) {
                UIService.getInstance().popBack();
            }

            if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenBakeDiyDetail)) {
                UIService.getInstance().popBack();
            }
        }

        //?????????????????????
        if (UIService.getInstance().isCurrentPage(PageKey.AbsOvenFanLinkage)) {
            UIService.getInstance().popBack();
        }

        if (absOvenExpDialog != null && absOvenExpDialog.isShowing()) {
            absOvenExpDialog.dismiss();
        }
        if (absOvenModeSettingHasDbDialog != null && absOvenModeSettingHasDbDialog.isShowing()) {
            absOvenModeSettingHasDbDialog.dismiss();
        }
        if (absOvenModeSettingDialog != null && absOvenModeSettingDialog.isShowing()) {
            absOvenModeSettingDialog.dismiss();
        }

    }


    @OnClick({R.id.iv_oven_back, R.id.oven_switch, R.id.oven_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_oven_back:
                UIService.getInstance().popBack();
                break;
            case R.id.oven_switch:
                if (getIsCon()) {
                    ToastUtils.show("?????????", Toast.LENGTH_SHORT);
                    return;
                }
                if (isSpecial) {
                    try {
                        if (!dt.equals("RQ035")) {
                            DeviceOff(oven.status);
                        }else{
                            JSONObject modeParamsObject = new JSONObject(modeParams);
                            JSONObject cookBookParamsObject = new JSONObject(cookBookParams);
                            JSONObject modeParam = modeParamsObject.getJSONObject("param");
                            //????????????
                            if (oven.runP != 0) {
                                JSONObject jsonObject = modeParam.getJSONObject(String.valueOf(oven.runP));
                                String isCombination = jsonObject.getString("isCombination");
                                String isOnlyTop = jsonObject.getString("isOnlyTop");
                                String isOnlyBottem = jsonObject.getString("isOnlyBottem");
                                //???????????? ????????????
                                if ("true".equals(isCombination)) {
                                    //??????
                                    //??????
                                    if ("true".equals(isOnlyTop) && "false".equals(isOnlyBottem)) {
                                        DeviceOff(oven.status);
                                    }

                                    //??????
                                    if ("false".equals(isOnlyTop) && "true".equals(isOnlyBottem)) {
                                        DeviceOff(oven.status2Values);
                                    }
                                    //??????
                                    if ("false".equals(isOnlyTop) && "false".equals(isOnlyBottem)) {
                                        DeviceOffTop();
                                    }
                                    //??????
                                } else {
                                    DeviceOff(oven.status);

                                }
                                //??????????????????
                            } else {
                                JSONObject autoObj = cookBookParamsObject.getJSONObject(String.valueOf(oven.autoMode));
                                String isCombination = autoObj.getString("isCombination");
                                //?????? ??????
                                if (isCombination.equals("true")) {
                                    String isOnlyTop = autoObj.getString("isOnlyTop");
                                    String isOnlyBottem = autoObj.getString("isOnlyBottem");
                                    //??????
                                    if ("true".equals(isOnlyTop) && "false".equals(isOnlyBottem)) {
                                        DeviceOff(oven.status);
                                    }
                                    //??????
                                    if ("false".equals(isOnlyTop) && "true".equals(isOnlyBottem)) {
                                        DeviceOff(oven.status2Values);
                                    }
                                    //??????
                                } else {
                                    DeviceOff(oven.status);
                                }

                            }
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    DeviceOff(oven.status);
                }
                break;
            case R.id.oven_more:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "????????????", "roki_??????");
                }

                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.AbsOvenMore, bd);
                break;
        }
    }

    private void DeviceOff(short status) {
        if (status == OvenStatus.Off) {
            ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
        } else if (status == OvenStatus.On
                || status == OvenStatus.AlarmStatus
                || status == OvenStatus.Wait) {
            sendOffCommand(OvenStatus.Off);
        } else {
            isOff();
        }
    }

    private void DeviceOffTop() {
        DeviceOff(oven.status);
    }

    private void isOff() {
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(getString(R.string.close_new));
        closedialog.setContentText(getString(R.string.oven_off_tip));
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    sendOffCommand(OvenStatus.Off);
                }
            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });

    }

    //??????????????????
    protected boolean getIsCon() {

        return !oven.isConnected();
    }

    //??????????????????
    private void sendOffCommand(short state) {
        oven.setOvenStatus(state, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (oven != null) {
                    ToolUtils.logEvent(oven.getDt(), "??????", "roki_?????? ");
                }
                ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("??????????????????????????????", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        PreferenceUtils.setString("codeName", null);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (oven == null) {
            return;
        }
    }


    private void getTop4() {
        RokiRestHelper.getRecipeTop4(userId, mGuid, dc, dt, new Callback<com.robam.common.io.cloud.Reponses.GetRecipeTop4Response>() {
            @Override
            public void onSuccess(com.robam.common.io.cloud.Reponses.GetRecipeTop4Response getRecipeTop4Response) {
                top4s = getRecipeTop4Response.mFunctionCode;
                setSortListForTop4();
                showFirstAndWorkingView();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
