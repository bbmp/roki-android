package com.robam.roki.ui.page.device.steamovenone;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenOtherEvent;
import com.robam.common.events.SteamOvenOneDescalingEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenOneWorkFinishEvent;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;
import com.robam.roki.utils.chart.DynamicLineChartManager;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 925工作页面烹饪曲线
 */

public class AbsSteamOvenWorkingCurve925Page<SteamOvenOne extends AbsSteameOvenOne> extends DeviceCatchFilePage {
    SteamOvenOne mSteamOvenOne;
    protected IDevice mDevice;
    private boolean mCompleteSign = false;
    private IRokiDialog mCloseDialog;

    @InjectView(R.id.contain)
    FrameLayout contain;

    private PayLoadCookBook mPayLoadCookBook;
    //横屏
    private AbsSteamOvenWorkingCurve925HView curveCookBooksHView;
    //竖屏
    private AbsSteamOvenWorkingCurve925VView curveCookBooksVView;

    private boolean isLoadCurveData = false;
    String temperatureCurveParams = "";
    String curveID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mPayLoadCookBook = bd == null ? null : (PayLoadCookBook) bd.getSerializable("Item");
        curveID = bd == null ? null : bd.getString("curveID");
        View view = inflater.inflate(R.layout.page_device_steamovenone_curve925_work, container, false);
        ButterKnife.inject(this, view);
        if (mSteamOvenOne == null) {
            mSteamOvenOne = Plat.deviceService.lookupChild(mGuid);
        }
//        initData();
        query();
        return view;
    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse obj) {

    }

    private void query() {
        //mGuid 暂时写死241
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        isLoadCurveData = true;
                        if (rcReponse.payload != null) {
                            temperatureCurveParams = rcReponse.payload.temperatureCurveParams;
                            initView();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        isLoadCurveData = true;
                        initView();
                    }
                });
    }

    private void initView() {
        curveCookBooksHView = new AbsSteamOvenWorkingCurve925HView(cx, mPayLoadCookBook);
        contain.addView(curveCookBooksHView);
        curveCookBooksHView.setOnClickChangePageLister(
                new AbsSteamOvenWorkingCurve925HView.OnClickChangePage() {
                    @Override
                    public void onclickChange(int VIndex) {

                        ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        contain.getChildAt(0).setVisibility(View.INVISIBLE);
                        contain.getChildAt(1).setVisibility(View.VISIBLE);

                    }
                }
        );
        curveCookBooksVView = new AbsSteamOvenWorkingCurve925VView(cx, temperatureCurveParams, mSteamOvenOne, curveID);
        contain.addView(curveCookBooksVView);
        curveCookBooksVView.setOnClickChangePageLister(new AbsSteamOvenWorkingCurve925VView.OnClickChangePage() {
            @Override
            public void onclickChange(int VIndex) {

                ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
            }

        });
        curveCookBooksVView.setOnClickmDeviceSwitchLister(new AbsSteamOvenWorkingCurve925VView.OnClickmDeviceSwitch() {
            @Override
            public void onClickmDeviceSwitch() {
                onMIvDeviceSwitchClicked();
            }
        });
        contain.getChildAt(0).setVisibility(View.INVISIBLE);
        contain.getChildAt(1).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        mSteamOvenOne = (SteamOvenOne) event.pojo;
        //预热   工作
        if ((event.pojo.workState == SteamOvenOneWorkStatus.PreHeat &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) || (event.pojo.workState == SteamOvenOneWorkStatus.Working &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
            if (isLoadCurveData) {
//            curveCookBooksHView.drawPoint((AbsSteameOvenOne) event.pojo);
                curveCookBooksVView.drawPoint((AbsSteameOvenOne) event.pojo);
//        curveCookBooksVView.updateStatus(event.pojo);
            }

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
                UIService.getInstance().popBack();
            }
        }
        if (event.steameOvenOne.SteameOvenWorkComplete == 1) {
            mCompleteSign = false;
        }
        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.OperatingState) {
            mCompleteSign = false;
        }

    }

    //开门事件
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID()))
            return;
        mSteamOvenOne = (SteamOvenOne) event.pojo;
    }

    public void onMIvDeviceSwitchClicked() {
        if (mSteamOvenOne == null) {
            return;
        }
        if (!mSteamOvenOne.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        if (mSteamOvenOne.powerState == SteamOvenOnePowerStatus.Off) {
            ToastUtils.show("已关机", Toast.LENGTH_SHORT);
            return;
        }
        ToolUtils.logEvent(mSteamOvenOne.getDt(), "关机", "roki_设备");


        if (mSteamOvenOne.powerState == SteamOvenOnePowerStatus.On && mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.OperatingState) {
            mSteamOvenOne.setSteameOvenStatus_Off(new VoidCallback() {
                @Override
                public void onSuccess() {
                    ToastUtils.show("已关机", Toast.LENGTH_SHORT);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            if (mSteamOvenOne.powerState == SteamOvenOnePowerStatus.Wait && mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.NoStatus) {
                mSteamOvenOne.setSteameOvenStatus_Off(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.show("已关机", Toast.LENGTH_SHORT);
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
//                                        ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
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
}
