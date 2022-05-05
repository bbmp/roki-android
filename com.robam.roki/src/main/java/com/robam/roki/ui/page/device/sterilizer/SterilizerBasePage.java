package com.robam.roki.ui.page.device.sterilizer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SterFinishEvent;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.SteriStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.SerilizerDialog;
import com.robam.roki.ui.view.SlideLockView;
import com.robam.roki.utils.DataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/10/16.
 */

public class SterilizerBasePage extends BasePage {


    long userId = Plat.accountService.getCurrentUserId();
    String dt;
    String dc;
    List<DeviceConfigurationFunctions> bgFunList = new ArrayList<>();
    List<DeviceConfigurationFunctions> mainList = new ArrayList<>();
    List<DeviceConfigurationFunctions> otherList = new ArrayList<>();
    List<DeviceConfigurationFunctions> moreList = new ArrayList<>();
    public HashMap<String, String> paramMap = new HashMap<>();
    protected SerilizerDialog serilizerDialog;
    public HashMap<String, DeviceConfigurationFunctions> paramMapMore = new HashMap<>();
    public HashMap<String, DeviceConfigurationFunctions> paramMapOther = new HashMap<>();
    SterilizerFirstView ovenFirstView;
    SterilizerWorkingView sterilizerWorkingView;
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
    AbsSterilizer absSterilizer;
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
    @InjectView(R.id.contain)
    FrameLayout contain;
    int from;
    public String mGuid;
    protected Steri826 steri826 = null;
    protected Steri829 steri829 = null;

    @InjectView(R.id.main_lock)
    SlideLockView mainLock;

    @InjectView(R.id.main_lock_show)
    LinearLayout mainLockShow;
    private List<DeviceConfigurationFunctions> hideFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd.getInt(PageArgumentKey.from);

        View view = inflater.inflate(R.layout.abs_sterilizer_module_main, container, false);
        ButterKnife.inject(this, view);
        absSterilizer = Plat.deviceService.lookupChild(mGuid);

        if (absSterilizer != null && (IRokiFamily.RR829.equals(absSterilizer.getDt()))) {
            steri829 = (Steri829) absSterilizer;
        } else {
            LogUtils.i("20181018", "absSterilizer:" + absSterilizer.getGuid().getGuid());
            if (absSterilizer instanceof Steri826) {
                steri826 = (Steri826) absSterilizer;
            }
        }
        initData();
        setLockListener();
        return view;
    }

    private void setLockListener() {
        // 设置滑动解锁-解锁的监听
        mainLock.setLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                steri826.setSteriLock((short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (mainLockShow != null) {
                            mainLockShow.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (absSterilizer == null) {
            return;
        }
    }

    String version = null;

    private void initData() {
        if (absSterilizer != null) {
            dt = absSterilizer.getDt();
            dc = absSterilizer.getDc();
        }

        version = DataUtils.readJson(cx, "version" + dt);
        if (version == null) {
            getDataMethod();
        } else {
            getVersionMethod();
        }

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
                    String sterilizer = DataUtils.readJson(cx, "sterilizer" + dt);
                    Reponses.DeviceResponse deviceOven = null;
                    try {
                        deviceOven = JsonUtils.json2Pojo(sterilizer, Reponses.DeviceResponse.class);
                       /*Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = deviceOven;
                        handler.sendMessage(msg);*/
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
        Plat.deviceService.getDeviceByParams(userId, dt, dc, new Callback<Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;
                DataUtils.writeJson(cx, deviceResponse.version, "version" + dt, false);
                DataUtils.writeJson(cx, deviceResponse.toString(), "sterilizer" + dt, false);
                try {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = deviceResponse;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    LogUtils.i("20180725", "error::" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    List<DeviceConfigurationFunctions> settingParam = new ArrayList<>();

    private void setDataToView(Reponses.DeviceResponse deviceResponse) {
        try {
            String backgroundImg = deviceResponse.viewBackgroundImg;
            String deviceType = deviceResponse.deviceType;
            Glide.with(cx).load(backgroundImg).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivOvenBg);
            ovenName.setText(deviceResponse.title);
            hideFunctions = deviceResponse.modelMap.hideFunc.deviceConfigurationFunctions;

            MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
            mainList = mainFunc.deviceConfigurationFunctions;

            if (mainList.size() > 1) {
                if (mainList.get(mainList.size() - 1).subView != null) {
                    moreList = mainList.get(mainList.size() - 1).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                }
            }

            setParamMapMore();
            OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
            otherList = otherFunc.deviceConfigurationFunctions;
            for (int i = 0; i < otherList.size(); i++) {
                if ("peakValleyElectricDisinfection".equals(otherList.get(i).functionCode)) {
                    settingParam = otherList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                }
            }
            BackgroundFunc bgFunc = deviceResponse.modelMap.backgroundFunc;
            bgFunList = bgFunc.deviceConfigurationFunctions;
            setParamMap();
            sterilizerWorkingView = new SterilizerWorkingView(cx, bgFunList, absSterilizer);
            ovenFirstView = new SterilizerFirstView(cx, mainList, otherList, bgFunList, deviceType);
            contain.addView(ovenFirstView);
            contain.addView(sterilizerWorkingView);
            if (from == 0) {
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
            }

            if (!absSterilizer.isConnected()) {
                disConStatus();
            }

            ovenFirstView.setOnclickMainLister(new SterilizerFirstView.OnClickMian() {
                @Override
                public void onclickMain(String str) {
                    clickMain(str);
                }

                @Override
                public void onclickOther(String str) {
                    clickOther(str);
                }
            });
        } catch (Exception e) {
            LogUtils.i("20180725", "error::" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void clickMain(String str) {

    }

    public void clickOther(String str) {

    }

    @OnClick({R.id.iv_oven_back, R.id.oven_switch, R.id.oven_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_oven_back:
                UIService.getInstance().popBack();
                break;
            //关机
            case R.id.oven_switch:
                if (getIsCon()) {
                    ToastUtils.show("已离线", Toast.LENGTH_SHORT);
                    return;
                }
                setStatus();
                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "关机", "roki_设备");
                    }
                }
                break;
            //更多
            case R.id.oven_more:

                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                if (hideFunctions.size() != 0) {
                    bd.putSerializable(PageArgumentKey.List, (Serializable) hideFunctions);
                }
                UIService.getInstance().postPage(PageKey.AbsSterilizerMore, bd);
                break;
        }
    }


    //下发关机开机的指令,后面的设备都以826未标准，故把829单独处理
    public void setStatus() {
        if (steri829 != null) {
            if (steri829.status == 0) {
                ToastUtils.show("已关机", Toast.LENGTH_SHORT);
            } else {
                if (steri829.status == 1) {
                    sendOff829Com((short) 0);
                } else {
                    isOff();
                }
            }

        } else {
            if (steri826.status == 0) {
                ToastUtils.show("已关机", Toast.LENGTH_SHORT);
            } else {
                if (steri826.status == 1) {
                    sendOffCom((short) 0);
                } else {
                    isOff();
                }
            }

        }
    }

    IRokiDialog closedialog = null;

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
                    if (steri829 != null) {
                        sendOff829Com((short) 0);
                    } else {
                        sendOffCom((short) 0);
                    }

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


    private void remindDialog() {
        final IRokiDialog remindDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        remindDialog.setTitleText("消毒提示");
        remindDialog.setContentText("你的餐具长时间未消毒，请及时消毒");
        remindDialog.show();
        remindDialog.setOkBtn("开启消毒", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remindDialog.isShow()) {
                    remindDialog.dismiss();
                }

                short isChildLock = steri826.isChildLock;
                if (isChildLock == 0) {
                    send();
                } else {
                    ToastUtils.show("请先解除童锁,再开启消毒", Toast.LENGTH_SHORT);
                }

            }
        });
        remindDialog.setCancelBtn("关闭提示", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remindDialog.isShow()) {
                    remindDialog.dismiss();
                }
            }
        });
    }


    private void send() {
        steri826.setSteriPower((short) 1, (short) 0, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        steri826.setSteriPower2((short) 2, (short) 90, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }, 500);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    private void sendOff829Com(short status) {
        steri829.setSteriPower(status, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
            }
        });
    }


    private void sendOffCom(short status) {
        steri826.setSteriPower(status, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
            }
        });
    }

    //获取联网状态
    protected boolean getIsCon() {

        return !absSterilizer.isConnected();
    }

    private void disConStatus() {
        contain.getChildAt(1).setVisibility(View.INVISIBLE);
        contain.getChildAt(0).setVisibility(View.VISIBLE);
        ovenFirstView.disConnect(true);
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
            paramMapOther.put(otherList.get(i).functionCode, otherList.get(i));
        }
    }

    boolean finish = false;

    @Subscribe
    public void onEvent(SterFinishEvent event) {
        if (!Objects.equal(absSterilizer.getDt(), event.pojo.getDt())) {
            return;
        }
        if (!Objects.equal(absSterilizer.getGuid(), event.pojo.getGuid())) {
            return;
        }
        if (!absSterilizer.getGuid().getGuid().equals(event.pojo.getGuid().getGuid())) {
            return;
        }
        //消毒柜工作结束事件
        if (event.eventId == 12 && event.eventParam == 0) {
            finish = true;
            sterilizerWorkingView.completeWork();
        }
        //新增 2020年1月8日  消毒柜消毒提醒事件
        if (event.eventId == 13 && event.eventParam == 1) {
            remindDialog();
        }

    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        LogUtils.i("20180711", "event:" + event.isConnected);
        if (!event.isConnected) {
            if (closedialog != null && closedialog.isShow()) {
                closedialog.dismiss();
            }
            disConStatus();
        }
    }

    @Subscribe
    public void onEvent(SteriAlarmEvent event) {
        if (absSterilizer == null || !Objects.equal(absSterilizer.getID(), event.absSterilizer.getID()))
            return;
        switch (event.alarm) {
            case 0://门控报警
                //IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
                //dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
                //dialogByType.setContentText(R.string.device_alarm_gating_content);
                //dialogByType.show();
                ToastUtils.show(R.string.device_alarm_gating_content, Toast.LENGTH_LONG);
                break;

            default:
                break;
        }
    }

    //设备轮训处理方法
    @Subscribe
    public void onEvent(SteriStatusChangedEvent event) {
        if (absSterilizer == null || !Objects.equal(absSterilizer.getID(), event.pojo.getID()))
            return;
        if (steri826 != null) {
            steri826 = (Steri826) event.pojo;
            if (steri826.isChildLock == 1) {
                mainLockShow.setVisibility(View.VISIBLE);
            } else {
                mainLockShow.setVisibility(View.GONE);
            }
        }

        if (finish) {
            if (steri826.status == 0 || steri826.status == 1 || steri826.status == 8) {
                finish = false;
            }
            return;
        }

        LogUtils.i("2020060204","steri826.status:::"+event.pojo.status);
        switch (event.pojo.status) {
            case 0://关机
            case 1://开机

                if (closedialog != null && closedialog.isShow()) {
                    closedialog.dismiss();
                }
                sterilizerWorkingView.closeAllDialog();
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                ovenFirstView.disConnect(false);
                ovenFirstView.upDataView(event.pojo);


                break;
            //报警状态
            case 6:
                //不做任何操作
                break;

            default:

                if (serilizerDialog != null && serilizerDialog.isShowing()) {
                    serilizerDialog.dismiss();
                }
                closePage();
                if (steri826 != null) {
                    steri826 = (Steri826) event.pojo;
                    if (steri826.work_left_time_l==0) {
                        return;
                    }
                }else {
                    steri829 = (Steri829) event.pojo;
                    if (steri829.work_left_time_l==0) {
                        return;
                    }

                }
                contain.getChildAt(0).setVisibility(View.INVISIBLE);
                contain.getChildAt(1).setVisibility(View.VISIBLE);
                sterilizerWorkingView.upStatusSter(event.pojo);
                break;
        }
    }


    private void closePage() {
        if (UIService.getInstance().isCurrentPage(PageKey.SterilizerOrder)) {
            UIService.getInstance().popBack();
        }
        if (UIService.getInstance().isCurrentPage(PageKey.SterilizerIntelligenceCleaning)) {
            UIService.getInstance().popBack();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}