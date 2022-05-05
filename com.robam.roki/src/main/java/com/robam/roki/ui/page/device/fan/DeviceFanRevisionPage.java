package com.robam.roki.ui.page.device.fan;


import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.HideFunc;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.services.ScreenPowerService;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanCleanLockEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.KitchenCleanEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.Fan8229;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.FanBackgroundFuncAdapter;
import com.robam.roki.ui.adapter.FanOtherFuncAdapter;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceFanRevisionPage<Fan extends AbsFan> extends DeviceCatchFilePage {
    Fan fan;
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
    @InjectView(R.id.ry_other_func)
    RecyclerView mRyOtherFunc;
    @InjectView(R.id.Gv_background_func)
    GridView mGvBackgroundFunc;
    @InjectView(R.id.tv_off_line_text)
    protected TextView mTvOffLineText;
    @InjectView(R.id.iv_lock_bg)
    ImageView mIvLockBg;
    @InjectView(R.id.oilclean_lock_bg)
    ImageView mOilcleanLockBg;
    private List<DeviceConfigurationFunctions> mainFunList;
    private List<DeviceConfigurationFunctions> otherFuncList;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions = new ArrayList<>();
    FanOtherFuncAdapter mFanOtherFuncAdapter;
    FanBackgroundFuncAdapter mFanBackgroundFuncAdapter;
    private String mUrl;
    private String mOilTitle;
    private List<DeviceConfigurationFunctions> hideFuncList;
    public boolean isSlideUlock = false;
    private String alarmTitle = "";
    private String messageHead = "";
    private String messageBack = "";
    private String clickText = "";
    private String oilNetDismantUrl;
    private String oilNetDismantTitle;
    private String protocolVersionParams;

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        LogUtils.i("20200611", "FanStatusChangedEvent:::" + event.pojo.toString());
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID()))
            return;
        mTvOffLineText.setVisibility(View.INVISIBLE);
        fan = (Fan) event.pojo;
        if (isShowTag) {
            if (fan.overTempProtectStatus == 1 && fan.smartSmokeStatus == 1) {
                isShowTag = false;
                redSmartConfig();
            }
        }

        updateCleanLock(event.pojo);
    }

    private void redSmartConfig() {
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                short isOverTempProtectSet = smartParams.IsOverTempProtectSet;
                alarmDialog(isOverTempProtectSet);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    boolean isShowTag = true;

    private void alarmDialog(short overTemp) {
        final IRokiDialog alarmDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_16);
        alarmDialog.setTitleText("".equals(alarmTitle) ? "过温报警" : alarmTitle);
        String textHead = "".equals(messageHead) ? "当前温度已超过" : messageHead;
        String textBehind = "".equals(messageBack) ? "℃" : messageBack;
        alarmDialog.setContentText(textHead + overTemp + textBehind);
        alarmDialog.setOkBtn("".equals(clickText) ? getString(R.string.fan_Know) : clickText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmDialog.dismiss();
            }
        });
        alarmDialog.setCanceledOnTouchOutside(false);
        alarmDialog.show();
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.showLong(R.string.device_new_connected);
            mTvOffLineText.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        boolean isCountdown = PreferenceUtils.getBool("isCountdownFan", false);
        if (!isCountdown) {
            EventUtils.postEvent(new KitchenCleanEvent());
        }

        if (fan == null) {
            return;
        }
    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse obj) {
        LogUtils.i("20180815", "obj:" + obj.deviceType);
        if (obj == null || null == mTvDeviceModelName) {
            return;
        }
        if (mDevice instanceof AbsFan) {
            fan = (Fan) mDevice;
        }
        if (!fan.isConnected()) {
            ToastUtils.showLong(R.string.device_new_connected);
            if (mTvOffLineText != null) {
                mTvOffLineText.setVisibility(View.VISIBLE);
            }
        }
        if (obj.title != null && mTvDeviceModelName != null) {
            String title = obj.title;
            mTvDeviceModelName.setText(title);
        }
        String backgroundImg = obj.viewBackgroundImg;
        if (!TextUtils.isEmpty(backgroundImg)) {
            Glide.with(cx).load(backgroundImg).into(mIvBg);
        } else {
            Glide.with(cx).load(R.mipmap.fan_default_img).into(mIvBg);
        }
        //油网提醒，灯控区
        BackgroundFunc backgroundFunc = obj.modelMap.backgroundFunc;
        List<DeviceConfigurationFunctions> backFunc = backgroundFunc.deviceConfigurationFunctions;
        //拿到油网拆卸动态连接
        if (backFunc != null && backFunc.size() > 0) {
            for (int i = 0; i < backFunc.size(); i++) {
                if ("oilNetworkState".equals(backFunc.get(i).functionCode)) {
                    List<DeviceConfigurationFunctions> oilList = backFunc.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                    if (oilList != null) {
                        for (int j = 0; j < oilList.size(); j++) {
                            if ("oilNetDismant".equals(oilList.get(j).functionCode)) {
                                mOilTitle = oilList.get(j).functionName;
                                mUrl = oilList.get(j).subViewName;
                            }
                        }
                    }
                }


            }
        }

        //主区域
        MainFunc mainFunc = obj.modelMap.mainFunc;
        mainFunList = mainFunc.deviceConfigurationFunctions;
        mDeviceConfigurationFunctions.addAll(mainFunList);
        OtherFunc otherFunc = obj.modelMap.otherFunc;
        otherFuncList = otherFunc.deviceConfigurationFunctions;
        mDeviceConfigurationFunctions.addAll(otherFuncList);

        HideFunc hideFunc = obj.modelMap.hideFunc;
        if (hideFunc != null) {
            hideFuncList = hideFunc.deviceConfigurationFunctions;
            for (int i = 0; i < hideFuncList.size(); i++) {
                if ("cleanUnlock".equals(hideFuncList.get(i).functionCode)) {
                    String functionParams = hideFuncList.get(i).functionParams;
                    //清洗锁定采用滑动解锁(8235S)
                    isSlideUlock = "1".equals(functionParams);
                }
                //过温报警弹框Text
                if ("overTempAlarm".equals(hideFuncList.get(i).functionCode)) {
                    String functionParams = hideFuncList.get(i).functionParams;
                    try {
                        JSONObject jsonObject = new JSONObject(functionParams);
                        JSONObject param = jsonObject.getJSONObject("param");
                        alarmTitle = param.getString("title");
                        String message = param.getString("message");
                        String[] buttons = message.split("button");
                        messageHead = buttons[0];
                        messageBack = buttons[1];
                        JSONArray click = param.getJSONArray("click");
                        clickText = (String) click.get(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //油网拆卸(R68A0的油网拆卸url)
                if ("oilNetDismant".equals(hideFuncList.get(i).functionCode)) {
                    oilNetDismantUrl = hideFuncList.get(i).subViewName;
                    oilNetDismantTitle = hideFuncList.get(i).functionName;
                }


            }
        }


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(cx, mainFunList.size());
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mRyOtherFunc.getAdapter().getItemViewType(position);
                if (viewType == FanOtherFuncAdapter.OTHER_VIEW) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRyOtherFunc.setLayoutManager(gridLayoutManager);
        mFanOtherFuncAdapter = new FanOtherFuncAdapter(cx, mDeviceConfigurationFunctions, fan);
        mRyOtherFunc.setAdapter(mFanOtherFuncAdapter);
        mRyOtherFunc.setItemAnimator(null);
        mFanBackgroundFuncAdapter = new FanBackgroundFuncAdapter(cx,
                backFunc, fan, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {

            }
        }, mDeviceConfigurationFunctions);
        mGvBackgroundFunc.setAdapter(mFanBackgroundFuncAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_fan, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mFanOtherFuncAdapter) {
            mFanOtherFuncAdapter.closeEventUtils();
        }
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_device_switch)
    public void onViewClicked() {
        if (fan == null) return;
        if (!fan.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        if (fan.level != 0) {

            ToolUtils.logEvent(fan.getDt(), "关机", "roki_设备");

            final IRokiDialog closeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
            closeDialog.setTitleText(R.string.device_off);
            closeDialog.setContentText(R.string.device_off_desc);
            closeDialog.show();
            closeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog.dismiss();
                    fan.restFanCleanTime(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
                                @Override
                                public void onSuccess() {

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
        } else if (FanStatus.CleanLock == fan.status) {
            ToastUtils.showShort(R.string.device_lock_text);
        } else if (fan.status == FanStatus.On) {
            fan.restFanCleanTime(new VoidCallback() {
                @Override
                public void onSuccess() {
                    fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showShort(R.string.device_close_text);
                            Log.i("20190808123", fan.status + "");
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
            ToastUtils.showShort(R.string.device_close_text);
        }
    }

    //更多
    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        if (fan == null) return;
        if ("R68A0".equals(fan.getDt()) || IRokiFamily._68A0S.equals(fan.getDt())) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, fan.getID());
            bd.putString(PageArgumentKey.Url, oilNetDismantUrl);
            bd.putString(PageArgumentKey.title, oilNetDismantTitle);
            UIService.getInstance().postPage(PageKey.FanDeviceMore, bd);
        } else {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, fan.getID());
            bd.putString(PageArgumentKey.Url, mUrl);
            bd.putString(PageArgumentKey.title, mOilTitle);
            UIService.getInstance().postPage(PageKey.FanDeviceMore, bd);
        }

    }


    @OnClick(R.id.oilclean_lock_bg)
    public void onMOilcleanLockBgClicked() {

    }


    short cleanLockId = 15;
    short cleanLockEventParams = 1;

    void updateCleanLock(AbsFan fan) {
        if (fan.status == FanStatus.CleanLock || (fan.eventId == cleanLockId && fan.eventParam == cleanLockEventParams)) {
            mIvLockBg.setVisibility(View.VISIBLE);
            mOilcleanLockBg.setVisibility(View.VISIBLE);
        } else {
            mIvLockBg.setVisibility(View.GONE);
            mOilcleanLockBg.setVisibility(View.GONE);

        }

    }

    @Subscribe
    public void onEvent(FanCleanLockEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.fan.getID())) {
            return;
        }
        updateCleanLock(event.fan);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFanOtherFuncAdapter.closeTask();
    }
}