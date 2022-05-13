package com.robam.roki.ui.page.device.steam;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
import com.robam.common.events.RecipeCookFinishEven;
import com.robam.common.events.SteamCleanResetEvent;
import com.robam.common.events.SteamFinishEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.FunctionsTop4;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.AbsOvenModeSettingDialog;
import com.robam.roki.ui.page.device.oven.AbsOvenFirstView;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.util.FanLockUtils.mGuid;

public class AbsSteamBasePage extends BasePage {

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


    long userId = Plat.accountService.getCurrentUserId();
    String dt;
    String dc;

    List<DeviceConfigurationFunctions> bgFunList = new ArrayList<>();
    List<DeviceConfigurationFunctions> mainList = new ArrayList<>();
    List<DeviceConfigurationFunctions> otherList = new ArrayList<>();
    List<DeviceConfigurationFunctions> moreList = new ArrayList<>();

    public HashMap<String, String> paramMap = new HashMap<>();
    public HashMap<String, DeviceConfigurationFunctions> paramMapMore = new HashMap<>();
    int from;
    AbsSteamoven steam;
    AbsOvenFirstView ovenFirstView;
    SteamWorkingView steamWorkingView;
    @InjectView(R.id.contain)
    FrameLayout contain;

    List<FunctionTop3> top3s = new ArrayList<>();
    List<FunctionMore> mores = new ArrayList<>();
    String version = null;


    //是否是特殊蒸箱
    public boolean isSpecial = false;
    public String needDescalingParams;
    private List<FunctionsTop4> top4s;


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
    protected static AbsOvenModeSettingDialog absOvenModeSettingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd.getInt(PageArgumentKey.from);
        steam = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.abs_oven_main, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        if (steam != null) {
            dt = steam.getDt();
            dc = steam.getDc();
        }

        version = DataUtils.readJson(cx, "version" + dt);
        if (version == null) {
            getDataMethod();
        } else {
            getVersionMethod();
        }


    }

    private void showFirstAndShowingView() {

        ovenFirstView = new AbsOvenFirstView(cx, mainList, otherList);
        steamWorkingView = new SteamWorkingView(cx, bgFunList, otherList, steam);
        contain.addView(ovenFirstView);
        contain.addView(steamWorkingView);
        if (from == 1) {
            contain.getChildAt(1).setVisibility(View.INVISIBLE);
        } else {
            contain.getChildAt(0).setVisibility(View.INVISIBLE);
        }

        if (!steam.isConnected()) {
            disConStatus();
        }

        ovenFirstView.setOnclickMainLister(new AbsOvenFirstView.OnClickMian() {
            @Override
            public void onclickMain(String str) {
                LogUtils.i("20200722", "str:::" + str);
                if (!steam.isConnected()) {
                    disConStatus();
                    return;
                }
                if (alarmDialog()) {
                    return;
                }
                if (steam.waterboxstate == 0) {
                    ToastUtils.show("水箱已弹出", Toast.LENGTH_SHORT);
                    return;
                }
                if (steam.waterboxstate == 0) {
                    ToastUtils.show(R.string.device_alarm_water_out, Toast.LENGTH_SHORT);
                    return;
                }
                clickMain(str);
            }

            @Override
            public void onclickOther(String str) {
                LogUtils.i("20200722", "str:::" + str);
                if (!steam.isConnected()) {
                    disConStatus();
                    return;
                }
                if (alarmDialog()) {
                    return;
                }
                if (steam.waterboxstate == 0) {
                    ToastUtils.show("水箱已弹出", Toast.LENGTH_SHORT);
                    return;
                }
                clickOther(str);
            }
        });
    }

    private void getTop3() {
        CloudHelper.getLookUpCode(userId, mGuid, dc, new Callback<Reponses.GetLookUpResponse>() {
            @Override
            public void onSuccess(Reponses.GetLookUpResponse getLookUpResponse) {
                top3s = getLookUpResponse.functionTop3s;
                mores = getLookUpResponse.functionMores;
                setSortList();
                showFirstAndShowingView();

            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20180727", "getLookUpResponse:");
            }
        });
    }


    private void getTop4() {
        RokiRestHelper.getRecipeTop4(userId, mGuid, dc, dt, new Callback<com.robam.common.io.cloud.Reponses.GetRecipeTop4Response>() {
            @Override
            public void onSuccess(com.robam.common.io.cloud.Reponses.GetRecipeTop4Response getRecipeTop4Response) {
                top4s = getRecipeTop4Response.mFunctionCode;
                setSortListForTop4();
                showFirstAndShowingView();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void getVersionMethod() {
        CloudHelper.getCheck(dt, version, new Callback<Reponses.GetCheckResponse>() {
            @Override
            public void onSuccess(Reponses.GetCheckResponse getCheckResponse) {
                if (getCheckResponse == null) return;

                if (!getCheckResponse.isLast) {//false是最新
                    LogUtils.i("20180815", "有更新了");
                    getDataMethod();
                } else {//不是最新的不需要去服务器请求加载本地文件
                    String ovenData = DataUtils.readJson(cx, "steam" + dt);
                    Reponses.DeviceResponse deviceOven = null;
                    try {
                        deviceOven = JsonUtils.json2Pojo(ovenData, Reponses.DeviceResponse.class);
                        setDataToView(deviceOven);
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
        CloudHelper.getDeviceByParams(userId, dt, dc, new Callback<Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;
                DataUtils.writeJson(cx, deviceResponse.version, "version" + dt, false);
                DataUtils.writeJson(cx, deviceResponse.toString(), "steam" + dt, false);
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

            }
        });
    }


    private void setDataToView(Reponses.DeviceResponse deviceResponse) {
        try {
            String backgroundImg = deviceResponse.viewBackgroundImg;
            Glide.with(cx).load(backgroundImg).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivOvenBg);
            ovenName.setText(deviceResponse.title);
            BackgroundFunc bgFunc = deviceResponse.modelMap.backgroundFunc;
            bgFunList = bgFunc.deviceConfigurationFunctions;
            if (bgFunList.size() != 0) {
                for (int i = 0; i < bgFunList.size(); i++) {
                    if (TextUtils.equals("isSpecial", bgFunList.get(i).functionCode)) {
                        if (TextUtils.equals("1", bgFunList.get(i).functionParams)) {
                            isSpecial = true;
                        }
                    } else if (TextUtils.equals("needDescaling", bgFunList.get(i).functionCode)) {
                        needDescalingParams = bgFunList.get(i).functionParams;
                    }
                }
            }
            OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
            otherList = otherFunc.deviceConfigurationFunctions;
            MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
            mainList = mainFunc.deviceConfigurationFunctions;
            if (!isSpecial) {
                if (mainList.size() > 1) {
                    if (mainList.get(mainList.size() - 1).subView != null) {
                        if (mainList.get(mainList.size() - 1).subView.subViewModelMap != null) {
                            moreList = mainList.get(mainList.size() - 1)
                                    .subView
                                    .subViewModelMap
                                    .subViewModelMapSubView
                                    .deviceConfigurationFunctions;
                        }
                    }
                }
            }


            setParamMapMore();
            setParamMap();
            if (!isSpecial) {
                getTop3();
            } else {
                getTop4();
            }


        } catch (NullPointerException e) {
            LogUtils.i("20180725", "error::" + e.getMessage());
            e.printStackTrace();
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


    private void disConStatus() {
        ToastUtils.show("电蒸箱已离线", Toast.LENGTH_LONG);
        contain.getChildAt(1).setVisibility(View.INVISIBLE);
        contain.getChildAt(0).setVisibility(View.VISIBLE);
        ovenFirstView.disConnect(true);
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

    boolean isComplete = false;

    @Subscribe
    public void onEvent(SteamFinishEvent event) {
        if (steam == null || !Objects.equal(steam.getID(), event.steam.getID()))
            return;
        //if (event.alarmId == 0 || event.alarmId == 1) {
        //    steamWorkingView.completeWork();
        //    isComplete = true;
        //}
        /**
         * event.alarmId==0 自然结束
         * event.alarmId==1 中途手动结束
         */
        LogUtils.i("20200730", event.alarmId + "");
        if (event.alarmId == 0) {
            LogUtils.i("202010271147", event.alarmId + "");
            steamWorkingView.completeWork();
            isComplete = true;
        }

    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (!event.isConnected) {
            if (closedialog != null && closedialog.isShow()) {
                closedialog.dismiss();
            }
            disConStatus();
        } else {
            ovenFirstView.disConnect(false);
        }
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (steam == null || !Objects.equal(steam.getID(), event.pojo.getID()))
            return;

        this.steam = event.pojo;

        //short status = event.pojo.status;
        //if (isComplete) {
        //    if (status != OvenStatus.On) {
        //        isComplete = false;
        //    }
        //    return;
        //}
        if (IRokiFamily.RS209.equals(event.pojo.getDt())) {
            if (steam.alarm == 3) {
                ToastUtils.show("门未关好，请检查并确认关好门。", Toast.LENGTH_SHORT);
                return;
            }
        }


        if (isSpecial) {
            if (isComplete) {
                if (steam.status == SteamStatus.Wait) {
                    isComplete = false;
                }
            }
        } else {
            if (isComplete) {
                if (SteamStatus.Wait != steam.status) {
                    isComplete = false;
                }
                return;
            }
        }
        statusShow(event.pojo);
        if (steam.status == OvenStatus.Off) {
            AlarmDataUtils.closeDialog();
        }
    }

    private boolean alarmDialog() {
        AbsSteamoven device = Plat.deviceService.lookupChild(mGuid);
        if (device.status == SteamStatus.AlarmStatus) {
            AlarmDataUtils.SteamAlarmStatus( steam,steam.alarm);
            return true;
        }
        return false;
    }


    private void statusShow(AbsSteamoven steam) {
        LogUtils.i("20200723000", "status:::" + steam.status);
        switch (steam.status) {
            case SteamStatus.Off:
                if (closedialog != null && closedialog.isShow()) {
                    closedialog.dismiss();
                }
                steamWorkingView.closeAllDialog();
                contain.getChildAt(0).setVisibility(View.VISIBLE);//待机页
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                ovenFirstView.disConnect(false);
                break;
            case SteamStatus.On:
            case SteamStatus.Wait:
                LogUtils.i("202007230001", "isComplete:::" + !isComplete);
                if (!isComplete) {
                    LogUtils.i("202007230001", "进来");
                    if (closedialog != null && closedialog.isShow()) {
                        closedialog.dismiss();
                    }
                    steamWorkingView.closeAllDialog();
                    contain.getChildAt(0).setVisibility(View.VISIBLE);//待机页
                    contain.getChildAt(1).setVisibility(View.INVISIBLE);
                    ovenFirstView.disConnect(false);
                }

                break;
            case SteamStatus.AlarmStatus:
                break;
            default:
                closePage();
                contain.getChildAt(1).setVisibility(View.VISIBLE);//工作页
                contain.getChildAt(0).setVisibility(View.INVISIBLE);
                steamWorkingView.updateStatus(steam);
                break;
        }
    }


    IRokiDialog closedialog = null;

    @OnClick({R.id.iv_oven_back, R.id.oven_switch, R.id.oven_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_oven_back:
                UIService.getInstance().popBack();
                break;
            case R.id.oven_switch:
                if (getIsCon()) {
                    ToastUtils.show("已离线", Toast.LENGTH_SHORT);
                    return;
                }

                if (steam.status == OvenStatus.Off) {
                    ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
                } else if (steam.status == SteamStatus.On && isComplete == false || steam.status == SteamStatus.AlarmStatus || steam.status == SteamStatus.Wait) {
                    sendOffCommand(SteamStatus.Off);
                } else {
                    isOff();
                }

                break;
            case R.id.oven_more:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击:更多", "roki_设备");
                }
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.AbsOvenMore, bd);
                break;
        }
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

    //发送关机指令
    private void sendOffCommand(short state) {
        steam.setSteamStatus(state, new VoidCallback() {
            @Override
            public void onSuccess() {
                steam.status = SteamStatus.Off;
                ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
                if (dt != null) {
                    ToolUtils.logEvent(dt, "关机", "roki_设备");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令下发失败，请重试", Toast.LENGTH_SHORT);
            }
        });
    }

    //获取联网状态
    protected boolean getIsCon() {
        return !steam.isConnected();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        PreferenceUtils.setString("codeName", null);
    }

//    @Subscribe
//    public void onEvent(RecipeCookFinishEven event) {
////        closePage();
//        //在线烹饪
//        if (UIService.getInstance().isCurrentPage(PageKey.RecipeDetail)) {
//            UIService.getInstance().popBack();
//        }
//        if (UIService.getInstance().isCurrentPage(PageKey.RecipeCategoryList)) {
//            UIService.getInstance().popBack();
//        }
//    }

    private void closePage() {
        //专业模式
        if (UIService.getInstance().isCurrentPage(PageKey.SteamProMode)) {
            UIService.getInstance().popBack();
        }
        //本地自动菜谱
        if (UIService.getInstance().isCurrentPage(PageKey.SteamLocalRecipe)) {
            UIService.getInstance().popBack();
        }
        //烹饪DIY列表页
        if (UIService.getInstance().isCurrentPage(PageKey.SteamDiyList)) {
            UIService.getInstance().popBack();
        }
        //烹饪diy详情页
        if (UIService.getInstance().isCurrentPage(PageKey.SteamDiyDetail)) {
            UIService.getInstance().popBack().popBack();
        }
        //烹饪diy编辑页
        if (UIService.getInstance().isCurrentPage(PageKey.SteamDiyEdit)) {
            UIService.getInstance().popBack();

            if (UIService.getInstance().isCurrentPage(PageKey.SteamDiyList)) {
                UIService.getInstance().popBack();
            }
            if (UIService.getInstance().isCurrentPage(PageKey.SteamDiyDetail)) {
                UIService.getInstance().popBack().popBack();
            }

        }
        //在线烹饪
//        if (UIService.getInstance().isCurrentPage(PageKey.RecipeCook)) {
//            UIService.getInstance().popBack().popBack().popBack();
//        }
//        if (UIService.getInstance().isCurrentPage(PageKey.RecipeDetail)) {
//            UIService.getInstance().popBack().popBack();
//        }
//        if (UIService.getInstance().isCurrentPage(PageKey.RecipeCategoryList)) {
//            UIService.getInstance().popBack();
//        }


        if (absOvenModeSettingDialog != null && absOvenModeSettingDialog.isShowing()) {
            absOvenModeSettingDialog.dismiss();
        }


    }
}
