package com.robam.roki.ui.page.device.dishWasher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.HideFunc;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DishWasherWorkCompleteResetEvent;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.dishWasher.DishWasherStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.SlideLockView;
import com.robam.roki.utils.DataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsDishWasherBasePage extends BasePage {
    @InjectView(R.id.iv_abs_dish_washer_bg)
    ImageView ivAbsDishWasherBg;
    @InjectView(R.id.iv_dish_washer_back)
    ImageView ivDishWasherBack;
    @InjectView(R.id.dish_washer_name)
    TextView dishWasherName;
    @InjectView(R.id.dish_washer_switch)
    ImageView dishWasherSwitch;
    @InjectView(R.id.dish_washer_more)
    ImageView dishWasherMore;
    @InjectView(R.id.contain)
    FrameLayout contain;
    @InjectView(R.id.main_lock_show)
    LinearLayout mainLockShow;
    @InjectView(R.id.rl_lock)
    RelativeLayout rlLock;
    @InjectView(R.id.main_lock)
    SlideLockView mainLock;
    @InjectView(R.id.tv_child_lock)
    TextView tvChildLock;

    String mGuid;
    int from;
    AbsDishWasher absDishWasher;
    String dt;
    String dc;
    long userId = Plat.accountService.getCurrentUserId();
    String version = null;

    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> moreList;
    List<DeviceConfigurationFunctions> otherList;
    List<DeviceConfigurationFunctions> bgFunList;
    public AbsDishWasherWorkingView absDishWasherWorkingView;
    public AbsDishWasherFirstView absDishWasherFirstView;
    public HashMap<String, DeviceConfigurationFunctions> paramMapMore = new HashMap<>();
    public HashMap<String, String> paramMap = new HashMap<>();
    private List<DeviceConfigurationFunctions> hideList;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setDataToView((Reponses.DeviceResponse) msg.obj);
                    break;
                case 2:
                    showDataView((Reponses.GetHistoryDataResponse) msg.obj);
                    break;
            }
        }
    };
    private String modeInfoParams;
    private String guid;
    private String wxappletParams;
    private String guideUrl;
    private String userExplainTitle;

    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        if (mGuid.equals(event.device.getGuid().getGuid())){
            String name = event.device.getName();
            dishWasherName.setText(name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd.getInt(PageArgumentKey.From);
        absDishWasher = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.abs_dish_washer_main, container, false);
        ButterKnife.inject(this, view);
        initData();
        setLockListener();
        return view;
    }

    private void setLockListener() {
        // 设置滑动解锁-解锁的监听
        mainLock.setLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                //下发解锁童锁指令
                absDishWasher.setDishWasherLock((short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mainLockShow.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }
        });
    }

    @OnClick({R.id.iv_dish_washer_back, R.id.dish_washer_switch, R.id.dish_washer_more})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_dish_washer_back:
                UIService.getInstance().popBack();
                break;
            case R.id.dish_washer_switch:
                if (childLock()) {
                    sendOffCommand();
                }
                break;
            case R.id.dish_washer_more:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击更多", "roki_设备");
                }

                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                bd.putString(PageArgumentKey.Url, guideUrl);
                bd.putString(PageArgumentKey.title, userExplainTitle);
                UIService.getInstance().postPage(PageKey.DishWasherMore, bd);

                break;
        }

    }

    private void sendOffCommand() {
        if (getIsCon()) {
            ToastUtils.show("已离线", Toast.LENGTH_SHORT);
            return;
        }
        if (absDishWasher.powerStatus == DishWasherStatus.off) {
            ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
        } else {

            if (absDishWasher.powerStatus == DishWasherStatus.working || absDishWasher.powerStatus == DishWasherStatus.pause) {
                final IRokiDialog closeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
                closeDialog.setTitleText(R.string.device_off);
                closeDialog.setContentText(R.string.device_off_desc);
                closeDialog.show();
                closeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeDialog.dismiss();
                        off();

                    }
                });
                closeDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (closeDialog.isShow()) {
                            closeDialog.dismiss();
                        }
                    }
                });
            } else {
                off();
            }


        }
    }

    private void off() {
        absDishWasher.setDishWasherStatusControl(DishWasherStatus.off, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (absDishWasher != null) {
                    ToolUtils.logEvent(absDishWasher.getDt(), "关机", "roki_设备 ");
                }
                ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (absDishWasher == null || !Objects.equal(absDishWasher.getID(), event.device.getID()))
            return;
        boolean isConnect = event.isConnected;
        LogUtils.i("2020061104", "isConnect:::" + isConnect);
        if (isConnect) {
            absDishWasherFirstView.disConnect(false);
        } else {
            disConStatus();

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initData() {
        if (absDishWasher != null) {
            dt = absDishWasher.getDt();
            dc = absDishWasher.getDc();
            guid = absDishWasher.getGuid().getGuid();
        }

        version = DataUtils.readJson(cx, "version" + dt);
        if (version == null) {
            getDataMethod();
        } else {
            getVersionMethod();
        }


    }

    private void getDataMethod() {
        Plat.deviceService.getDeviceByParams(userId, dt, dc, new Callback<Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;
                DataUtils.writeJson(cx, deviceResponse.version, "version" + dt, false);
                DataUtils.writeJson(cx, deviceResponse.toString(), "dishWasher" + dt, false);
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

    private void getVersionMethod() {
        CloudHelper.getCheck(dt, version, new Callback<Reponses.GetCheckResponse>() {
            @Override
            public void onSuccess(Reponses.GetCheckResponse getCheckResponse) {
                if (getCheckResponse == null) return;

                if (!getCheckResponse.isLast) {//false是最新
                    getDataMethod();
                } else {//不是最新的不需要去服务器请求加载本地文件
                    String dishWasherData = DataUtils.readJson(cx, "dishWasher" + dt);
                    Reponses.DeviceResponse deviceDishWasher = null;
                    try {
                        deviceDishWasher = JsonUtils.json2Pojo(dishWasherData, Reponses.DeviceResponse.class);
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = deviceDishWasher;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            if (deviceResponse.viewBackgroundImg != null) {
                String viewBackgroundImg = deviceResponse.viewBackgroundImg;
                Glide.with(cx).load(viewBackgroundImg).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivAbsDishWasherBg);
            }
//            dishWasherName.setText(deviceResponse.title);
            dishWasherName.setText(absDishWasher.getName() == null || absDishWasher.getName().equals(absDishWasher.getCategoryName()) ?
                    absDishWasher.getDispalyType() : absDishWasher.getName());
            MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
            mainList = mainFunc.deviceConfigurationFunctions;
            if (mainList.size() > 1) {
                moreList = mainList.get(mainList.size() - 1)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            }
            setParamMapMore();
            OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
            otherList = otherFunc.deviceConfigurationFunctions;
            BackgroundFunc bgFunc = deviceResponse.modelMap.backgroundFunc;
            bgFunList = bgFunc.deviceConfigurationFunctions;
            setParamMap();
            HideFunc hideFunc = deviceResponse.modelMap.hideFunc;
            hideList = hideFunc.deviceConfigurationFunctions;
            for (int i = 0; i < hideList.size(); i++) {
                if ("modelInfo".equals(hideList.get(i).functionCode)) {
                    modeInfoParams = hideList.get(i).functionParams;
                }
                if ("wxapplet".equals(hideList.get(i).functionCode)) {
                    wxappletParams = hideList.get(i).functionParams;
                }

                if ("userExplain".equals(hideList.get(i).functionCode)) {
                    guideUrl = hideList.get(i).functionParams;
                    userExplainTitle = hideList.get(i).functionName;
                    LogUtils.i("20200617", "userExplainTitle:::" + userExplainTitle);
                }
            }
            getHistoryData(guid);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void onEvent(DisherWasherStatusChangeEvent event) {
        if (absDishWasher == null || !Objects.equal(absDishWasher.getID(), event.pojo.getID())) {
            return;
        }
        this.absDishWasher = event.pojo;
        if (absDishWasher.StoveLock == 1) {
        mainLockShow.setVisibility(View.VISIBLE);
        } else {
        mainLockShow.setVisibility(View.GONE);
        }
        short status = event.pojo.powerStatus;
        if (status == DishWasherStatus.working) {
            closePage();
        }
        statusShow(status, absDishWasher);
        if (!absDishWasher.isConnected()) {
            disConStatus();
        }


    }


    private boolean childLock() {
        if (absDishWasher.StoveLock == 1) {
            ToastUtils.show("请先到机器端解除童锁", Toast.LENGTH_LONG);
            return false;
        } else {
            return true;
        }
    }

    private void closePage() {
        if (UIService.getInstance().isCurrentPage(PageKey.DishWash)) {
            UIService.getInstance().popBack();
        }

    }

    @Subscribe
    public void onEvent(DishWasherWorkCompleteResetEvent event) {
        if (absDishWasher == null || !Objects.equal(absDishWasher.getID(), event.pojo.getID())) {
            return;
        }
        absDishWasher = event.pojo;
        short powerConsumption = event.getPowerConsumption();
        short waterConsumption = event.getWaterConsumption();
        absDishWasherWorkingView.updateCompleteState(powerConsumption, waterConsumption);
    }


    private void statusShow(short status, AbsDishWasher washer) {
        //报警不更新状态
        if (washer.AbnormalAlarmStatus != 0) {
            LogUtils.i("2020061502", "报警::" + washer.AbnormalAlarmStatus);
            return;
        }
        LogUtils.i("2020061502", "没报警::" + washer.AbnormalAlarmStatus);
        switch (status) {
            case DishWasherStatus.off:
            case DishWasherStatus.wait:
                absDishWasherWorkingView.closeAllDialog();
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                absDishWasherFirstView.disConnect(false);

                break;
            case DishWasherStatus.working:
            case DishWasherStatus.pause:
            case DishWasherStatus.end:
                contain.getChildAt(0).setVisibility(View.INVISIBLE);
                contain.getChildAt(1).setVisibility(View.VISIBLE);
                absDishWasherWorkingView.updateStatus(washer);
                break;

        }

    }


    private void ga(String str) {
        switch (str) {
            case DishWasherName.strongWash:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：强力洗", "roki_设备");
                }
                break;
            case DishWasherName.intelligentWash:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：智能洗", "roki_设备");
                }
                break;
            case DishWasherName.quickWash:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：快速洗", "roki_设备");
                }
                break;
            case DishWasherName.more:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击洗碗机场景功能：更多", "roki_设备");
                }

                break;
            case DishWasherName.crystalBrightWash:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：晶亮洗", "roki_设备");
                }
                break;
            case DishWasherName.dailyWash:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：日常洗", "roki_设备");
                }
                break;
            case DishWasherName.energySavingWashing:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：节能洗", "roki_设备");
                }
            case DishWasherName.consumables:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：专属耗材购买", "roki_设备");
                }
                break;
        }
    }

    private void disConStatus() {
        ToastUtils.show("洗碗机已离线", Toast.LENGTH_LONG);
        contain.getChildAt(1).setVisibility(View.INVISIBLE);
        contain.getChildAt(0).setVisibility(View.VISIBLE);
        absDishWasherFirstView.disConnect(true);

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


    boolean flag = true;

    public void clickMain(String str) {
        if (getIsCon()) {
            ToastUtils.show(getString(R.string.oven_dis_con), Toast.LENGTH_SHORT);
            return;
        }
        switch (str) {
            //强力洗
            case DishWasherName.strongWash:

                sendMul(str);
                String strongWashTitle = null;
                DeviceConfigurationFunctions strongWashBean = null;

                for (int i = 0; i < mainList.size(); i++) {
                    if (DishWasherName.strongWash.equals(mainList.get(i).functionCode)) {
                        strongWashTitle = mainList.get(i).functionName;
                        strongWashBean = mainList.get(i);
                    }
                }


                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, mGuid);
                bundle.putSerializable(PageArgumentKey.Bean, strongWashBean);
                bundle.putString(PageArgumentKey.title, strongWashTitle);
                bundle.putString(PageArgumentKey.modeInfo, modeInfoParams);
                UIService.getInstance().postPage(PageKey.DishWash, bundle);

                break;
            //智能洗
            case DishWasherName.intelligentWash:
                sendMul(str);
                String intelligentWashTitle = null;
                DeviceConfigurationFunctions intelligentWashWashBean = null;
                for (int i = 0; i < mainList.size(); i++) {
                    if (DishWasherName.intelligentWash.equals(mainList.get(i).functionCode)) {
                        intelligentWashTitle = mainList.get(i).functionName;
                        intelligentWashWashBean = mainList.get(i);
                    }
                }
                Bundle intelligentWashBundle = new Bundle();
                intelligentWashBundle.putString(PageArgumentKey.Guid, mGuid);
                intelligentWashBundle.putSerializable(PageArgumentKey.Bean, intelligentWashWashBean);
                intelligentWashBundle.putString(PageArgumentKey.title, intelligentWashTitle);
                intelligentWashBundle.putString(PageArgumentKey.modeInfo, modeInfoParams);
                UIService.getInstance().postPage(PageKey.DishWash, intelligentWashBundle);
                break;
            //快速洗
            case DishWasherName.quickWash:
                sendMul(str);
                String quickWashTitle = null;
                DeviceConfigurationFunctions quickWashBean = null;
                for (int i = 0; i < mainList.size(); i++) {
                    if (DishWasherName.quickWash.equals(mainList.get(i).functionCode)) {
                        quickWashTitle = mainList.get(i).functionName;
                        quickWashBean = mainList.get(i);
                    }
                }
                Bundle quickWashBundle = new Bundle();
                quickWashBundle.putString(PageArgumentKey.Guid, mGuid);
                quickWashBundle.putSerializable(PageArgumentKey.Bean, quickWashBean);
                quickWashBundle.putString(PageArgumentKey.title, quickWashTitle);
                quickWashBundle.putString(PageArgumentKey.modeInfo, modeInfoParams);
                UIService.getInstance().postPage(PageKey.DishWash, quickWashBundle);
                break;
            case DishWasherName.more:
                if (flag) {
                    absDishWasherFirstView.setUpData(moreList);
                    flag = false;
                } else {
                    absDishWasherFirstView.removeMoreView();
                    flag = true;
                }

                break;
            //晶亮洗
            case DishWasherName.crystalBrightWash:
                sendMul(str);
                String crystalBrightWashTitle = null;
                DeviceConfigurationFunctions crystalBrightWashBean = null;
                for (int i = 0; i < moreList.size(); i++) {
                    if (DishWasherName.crystalBrightWash.equals(moreList.get(i).functionCode)) {
                        crystalBrightWashTitle = moreList.get(i).functionName;
                        crystalBrightWashBean = moreList.get(i);
                    }
                }
                Bundle crystalBrightWashBundle = new Bundle();
                crystalBrightWashBundle.putString(PageArgumentKey.Guid, mGuid);
                crystalBrightWashBundle.putSerializable(PageArgumentKey.Bean, crystalBrightWashBean);
                crystalBrightWashBundle.putString(PageArgumentKey.title, crystalBrightWashTitle);
                crystalBrightWashBundle.putString(PageArgumentKey.modeInfo, modeInfoParams);
                UIService.getInstance().postPage(PageKey.DishWash, crystalBrightWashBundle);
                break;
            //日常洗
            case DishWasherName.dailyWash:
                sendMul(str);
                String dailyWashTitle = null;
                DeviceConfigurationFunctions dailyWashBean = null;
                for (int i = 0; i < moreList.size(); i++) {
                    if (DishWasherName.dailyWash.equals(moreList.get(i).functionCode)) {
                        dailyWashTitle = moreList.get(i).functionName;
                        dailyWashBean = moreList.get(i);
                    }
                }
                Bundle dailyWash = new Bundle();
                dailyWash.putString(PageArgumentKey.Guid, mGuid);
                dailyWash.putSerializable(PageArgumentKey.Bean, dailyWashBean);
                dailyWash.putString(PageArgumentKey.title, dailyWashTitle);
                dailyWash.putString(PageArgumentKey.modeInfo, modeInfoParams);
                UIService.getInstance().postPage(PageKey.DishWash, dailyWash);

                break;
            //节能洗
            case DishWasherName.energySavingWashing:
                sendMul(str);
                String energySavingWashingTitle = null;
                DeviceConfigurationFunctions energySavingWashingBean = null;
                for (int i = 0; i < moreList.size(); i++) {
                    if (DishWasherName.energySavingWashing.equals(moreList.get(i).functionCode)) {
                        energySavingWashingTitle = moreList.get(i).functionName;
                        energySavingWashingBean = moreList.get(i);
                    }
                }
                Bundle energySavingWashing = new Bundle();
                energySavingWashing.putString(PageArgumentKey.Guid, mGuid);
                energySavingWashing.putSerializable(PageArgumentKey.Bean, energySavingWashingBean);
                energySavingWashing.putString(PageArgumentKey.title, energySavingWashingTitle);
                energySavingWashing.putString(PageArgumentKey.modeInfo, modeInfoParams);
                UIService.getInstance().postPage(PageKey.DishWash, energySavingWashing);
                break;


        }
    }


    public void clickOther(String str) {
        if (getIsCon()) {
            ToastUtils.show(getString(R.string.oven_dis_con), Toast.LENGTH_SHORT);
            return;
        }
        switch (str) {
            case DishWasherName.consumables:
                if (dt != null) {
                    ToolUtils.logEvent(dt, "点击：专属耗材购买", "roki_设备");
                }
                String titleName = null;
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(DishWasherName.consumables)) {
                        titleName = otherList.get(i).functionName;
                    }
                }
                Bundle consumablesBundle = new Bundle();
                consumablesBundle.putString(PageArgumentKey.Guid, mGuid);
                consumablesBundle.putString(PageArgumentKey.title, titleName);
                consumablesBundle.putString(PageArgumentKey.wxparams, wxappletParams);
                UIService.getInstance().postPage(PageKey.ConsumablesBuy, consumablesBundle);
                break;
        }
    }


    //获取联网状态
    protected boolean getIsCon() {
        if (!absDishWasher.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, mGuid, code, dc, new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void getHistoryData(String guid) {
        CloudHelper.getHistroyData(guid, new Callback<Reponses.GetHistoryDataResponse>() {
            @Override
            public void onSuccess(Reponses.GetHistoryDataResponse getHistoryDataResponse) {

                try {
                    Message msg = Message.obtain();
                    msg.what = 2;
                    msg.obj = getHistoryDataResponse;
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

    private void showDataView(Reponses.GetHistoryDataResponse historyDataResponse) {
        absDishWasherWorkingView = new AbsDishWasherWorkingView(cx, hideList, absDishWasher);
        absDishWasherFirstView = new AbsDishWasherFirstView(cx, mainList, otherList, bgFunList, absDishWasher, historyDataResponse, wxappletParams);
        contain.addView(absDishWasherFirstView);
        contain.addView(absDishWasherWorkingView);
        LogUtils.i("2020061502", "from：：" + from);
        if (absDishWasher.AbnormalAlarmStatus != 0) {
            contain.getChildAt(1).setVisibility(View.INVISIBLE);
        } else {
            //待机页面
            if (from == 1) {
                LogUtils.i("2020061502", "待机页面");
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
            } else {
                //工作页面
                contain.getChildAt(0).setVisibility(View.INVISIBLE);
            }

        }
        if (!absDishWasher.isConnected()) {
            disConStatus();
        }


        absDishWasherFirstView.setOnClickMainListener(new AbsDishWasherFirstView.OnClickMain() {
            @Override
            public void onClickMain(String str) {
                ga(str);
                clickMain(str);
            }

            @Override
            public void onClickOther(String str) {
                if (childLock()) {
                    ga(str);
                    clickOther(str);
                }

            }
        });
    }


}
