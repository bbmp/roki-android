package com.robam.roki.ui.page.device.gassensor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.GasParamsTemp;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.GasItemDisplayView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/5/30.
 * 燃气卫士
 */

public class AbsDeviceSensorView extends BasePage {

    @InjectView(R.id.iv_bg)
    ImageView ivBg;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView ivDeviceMore;
    @InjectView(R.id.iv_run_down)
    ImageView ivRunDown;
    @InjectView(R.id.iv_run_up)
    ImageView ivRunUp;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout llRunAnimation;
    @InjectView(R.id.tv_work_state_name)
    TextView tvWorkStateName;
    @InjectView(R.id.tv_work_dec)
    TextView tvWorkDec;
    @InjectView(R.id.imageView9)
    ImageView imageView9;
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout flRunAndStop;
    @InjectView(R.id.addview_cu)
    FrameLayout addview_cu;
    @InjectView(R.id.gas_con)
    TextView gasCon;
    @InjectView(R.id.gas_item_show)
    GasItemDisplayView gasItemDisplayView;
    @InjectView(R.id.gas_alarm)
    GasAlarmSensorView gasAlarm;
    @InjectView(R.id.txt_disconnect)
    TextView txtDisConnect;

    List<GasParamsTemp> gasParamList = new ArrayList<>();
    Animation circleRotateDown;
    Animation circleRotateUp;
    String mGuid;
    String deviceName;
    GasSensor gasSensor;
    IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警
//    private Tracker tracker;

    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        if (mGuid.equals(event.device.getGuid().getGuid())){
            String name = event.device.getName();
            tvDeviceModelName.setText(name);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        gasSensor = Plat.deviceService.lookupChild(mGuid);
        LogUtils.i("20180606", "gas::" + gasSensor.getDc());
        deviceName = bd == null ? null : bd.getString(PageArgumentKey.deviceName);
        View view = inflater.inflate(R.layout.abs_device_gassensor, container, false);
        ButterKnife.inject(this, view);
//        tracker = MobApp.getTracker();
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gasSensor==null) {
            return;
        }
        if (gasSensor.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), gasSensor.getDt(), null);
        }

}

    protected void initView() {
        GasParamsTemp gasParamsTemp1 = new GasParamsTemp(R.mipmap.ic_gas_self_check, "一键自检", "快速检测报警机制是否正常");
        GasParamsTemp gasParamsTemp2 = new GasParamsTemp(R.mipmap.ic_gas_chuli, "燃气泄漏处理", "快速掌握应急处理技巧");
        gasParamList.add(gasParamsTemp1);
        gasParamList.add(gasParamsTemp2);
        gasItemDisplayView.onRefresh(cx, gasParamList);
        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        gasItemDisplayView.setOnRecycleItemClick(new GasItemDisplayView.OnRecycleItemClick() {
            @Override
            public void itemClick(View view, int position) {

                recycleItemDeal(position);

            }
        });
        gasAlarm.setOnDealClickLister(new GasAlarmSensorView.OnDealClick() {
            @Override
            public void dealClick(int num) {
                alarmDeal(num);
            }
        });
    }

    //报警和故障点击事件
    private void alarmDeal(int position) {
        switch (position) {
            case 0:
                UIService.getInstance().postPage(PageKey.GasAlarmDeal);
                break;
            case 1:
                AlarmDialog();
                break;
        }
    }


    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (gasSensor == null || !Objects.equal(gasSensor.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            if (isStartAnimation) {
                stopAnimation();
            }
            ToastUtils.show("燃气卫士已离线", Toast.LENGTH_SHORT);
            txtDisConnect.setVisibility(View.VISIBLE);
        } else {
            txtDisConnect.setVisibility(View.GONE);
        }
    }

    //recycle正常状态item的点击处理事件
    private void recycleItemDeal(int position) {
        switch (position) {
            case 0:
                if (!gasSensor.isConnected()) {
                    if (isStartAnimation) {
                        stopAnimation();
                    }
                    ToastUtils.show("燃气卫士已离线", Toast.LENGTH_SHORT);
                    break;
                }
                checkDialog();
                break;
            case 1:
                if (!gasSensor.isConnected()) {
                    if (isStartAnimation) {
                        stopAnimation();
                    }
                    ToastUtils.show("燃气卫士已离线", Toast.LENGTH_SHORT);
                    break;
                }
                UIService.getInstance().postPage(PageKey.GasAlarmDeal);
                break;
            default:
                break;
        }
    }

    private void checkDialog() {
        iRokiDialogAlarmType_01.setTitleText("温馨提示");
        iRokiDialogAlarmType_01.setContentText("燃气卫士启动自检会触发报警，烟机将自动开启，并收到煤气泄漏短信通知。");
        iRokiDialogAlarmType_01.setOkBtn("开始自检", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_01.dismiss();
                gasSensor.setGasSelfCheckCom(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (isStartAnimation) {
                            stopAnimation();
                        }
                        UIService.getInstance().postPage(PageKey.GasSelfCheck);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
        iRokiDialogAlarmType_01.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
                    iRokiDialogAlarmType_01.dismiss();
                }
            }
        });
        iRokiDialogAlarmType_01.show();
    }


    private void AlarmDialog() {
        iRokiDialogAlarmType_01.setTitleText("一键售后");
        iRokiDialogAlarmType_01.setContentText("拨打95105855");
        iRokiDialogAlarmType_01.setOkBtn("直接拨打", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_01.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        iRokiDialogAlarmType_01.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
                    iRokiDialogAlarmType_01.dismiss();
                }
            }
        });
        iRokiDialogAlarmType_01.show();
    }

    boolean isStartAnimation = false;

    /**
     * 开启动画
     */
    void startAnimation() {
        isStartAnimation = true;
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            ivRunDown.startAnimation(circleRotateDown);
        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            ivRunUp.startAnimation(circleRotateUp);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    public void stopAnimation() {
        isStartAnimation = false;
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            ivRunDown.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            ivRunUp.clearAnimation();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        UIService.getInstance().returnHome();
        stopAnimation();
    }

    @OnClick(R.id.iv_device_more)
    public void onClickMore() {
        // ToastUtils.show("我是更多", Toast.LENGTH_SHORT);
        LogUtils.i("20180601", "guid1::" + mGuid);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, gasSensor.getID());
        UIService.getInstance().postPage(PageKey.DeviceMoreGasInfo, bd);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isStartAnimation) {
            stopAnimation();
        }
        ButterKnife.reset(this);
    }
}
