package com.robam.roki.ui.page.device.integratedStove;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.SubView;
import com.legent.plat.pojos.device.SubViewModelMapSubView;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.mdialog.MessageDialog;
import com.robam.roki.ui.widget.base.BaseDialog;
import com.robam.roki.utils.ToolUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/5.
 * 烟机风量页
 */

public class IntegratedStoveAirVolumePage extends BasePage {

    AbsIntegratedStove integratedStove;
    List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    LinearLayout mLlFanVolumeClose;
    //    @InjectView(R.id.tv_steam_work_desc)
//    TextView mTvSteamWorkDesc;
    @InjectView(R.id.tv_stove_left)
    TextView mTvStoveLeft;
    @InjectView(R.id.iv_fan_off_left)
    ImageView mIvFanOffLeft;
    @InjectView(R.id.iv_fan_on_ring_left)
    ImageView mIvFanOnRingLeft;
    @InjectView(R.id.iv_fan_on_wheel_left)
    ImageView mIvFanOnWheelLeft;
    @InjectView(R.id.fl_img_on_left)
    FrameLayout mFlImgOnLeft;
    @InjectView(R.id.tv_stove_right)
    TextView mTvStoveRight;
    @InjectView(R.id.iv_fan_off_right)
    ImageView mIvFanOffRight;
    @InjectView(R.id.iv_fan_on_ring_right)
    ImageView mIvFanOnRingRight;
    @InjectView(R.id.iv_fan_on_wheel_right)
    ImageView mIvFanOnWheelRight;
    @InjectView(R.id.fl_img_on_right)
    FrameLayout mFlImgOnRight;
    @InjectView(R.id.ll_fan_and_stove_bg)
    LinearLayout mLlFanAndStoveBg;
    @InjectView(R.id.iv_fan_close_volume)
    ImageView mIvFanCloseVolume;
    @InjectView(R.id.iv_fan_open_volume)
    ImageView mIvFanOpenVolume;

    //    private short mRikaFanPower;
    Animation leftClockwiseAnimation;//左顺时针
    Animation leftCounterclockwiseAnimation;//左逆时针

    Animation centerClockwiseAnimation;//中顺时针
    Animation centerCounterclockwiseAnimation;//中逆时针

    Animation rightClockwiseAnimation;//右顺时针
    Animation rightCounterclockwiseAnimation;//右逆时针
    @InjectView(R.id.tv_stove_center)
    TextView mTvStoveCenter;
    @InjectView(R.id.iv_fan_off_center)
    ImageView mIvFanOffCenter;
    @InjectView(R.id.iv_fan_on_ring_center)
    ImageView mIvFanOnRingCenter;
    @InjectView(R.id.iv_fan_on_wheel_center)
    ImageView mIvFanOnWheelCenter;
    @InjectView(R.id.fl_img_on_center)
    FrameLayout mFlImgOnCenter;
    @InjectView(R.id.ll_left)
    LinearLayout mLlLeft;
    @InjectView(R.id.ll_center)
    LinearLayout mLlCenter;
    @InjectView(R.id.ll_right)
    LinearLayout mLlRight;
    /**
     * 弱档位
     */
    @InjectView(R.id.iv_volume_min)
    ImageView iv_volume_min;
    /**
     * 中档位
     */
    @InjectView(R.id.iv_volume_medium)
    ImageView iv_volume_medium;
    /**
     * 强档位
     */
    @InjectView(R.id.iv_volume_max)
    ImageView iv_volume_max;
    /**
     * 爆炒档位
     */
    @InjectView(R.id.iv_volume_xl)
    ImageView iv_volume_xl;

    private DeviceConfigurationFunctions mFunctions;
    String leftRight;
    String center;

    @Override
    public void onResume() {
        super.onResume();
        if (integratedStove == null) {
            return;
        }
    }

    @Subscribe
    public void onEvent(IntegStoveStatusChangedEvent event) {

        if (integratedStove == null || !Objects.equal(integratedStove.getID(), event.pojo.getID())) {
            return;

        }
        this.integratedStove = event.pojo;
        changedStatus();
    }

    private void changedStatus() {
        if (integratedStove.fan_gear == 0) {
            stopCenterAnimation();
        } else {
            mIvFanCloseVolume.setVisibility(View.VISIBLE);
            mIvFanOpenVolume.setVisibility(View.GONE);

            startCenterClockwiseAnimation();
            startCenterCounterclockwiseAnimation();

        }
        if (integratedStove.fan_gear == 1) {
            iv_volume_min.setImageResource(R.drawable.ic_volume_s_on);
            iv_volume_medium.setImageResource(R.drawable.ic_volume_m);
            iv_volume_max.setImageResource(R.drawable.ic_volume_xl);
            iv_volume_xl.setImageResource(R.drawable.ic_maxs_h);
        } else if (integratedStove.fan_gear == 2) {
            iv_volume_min.setImageResource(R.drawable.ic_volume_s);
            iv_volume_medium.setImageResource(R.drawable.ic_volume_m_on);
            iv_volume_max.setImageResource(R.drawable.ic_volume_xl);
            iv_volume_xl.setImageResource(R.drawable.ic_maxs_h);
        } else if (integratedStove.fan_gear == 3) {
            iv_volume_min.setImageResource(R.drawable.ic_volume_s);
            iv_volume_medium.setImageResource(R.drawable.ic_volume_m);
            iv_volume_max.setImageResource(R.drawable.ic_volume_xl_on);
            iv_volume_xl.setImageResource(R.drawable.ic_maxs_h);
        } else if (integratedStove.fan_gear == 6) {
            iv_volume_min.setImageResource(R.drawable.ic_volume_s);
            iv_volume_medium.setImageResource(R.drawable.ic_volume_m);
            iv_volume_max.setImageResource(R.drawable.ic_volume_xl);
            iv_volume_xl.setImageResource(R.drawable.ic_maxs);
        } else {
            iv_volume_min.setImageResource(R.drawable.ic_volume_s);
            iv_volume_medium.setImageResource(R.drawable.ic_volume_m);
            iv_volume_max.setImageResource(R.drawable.ic_volume_xl);
            iv_volume_xl.setImageResource(R.drawable.ic_maxs_h);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mDeviceConfigurationFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        integratedStove = bd == null ? null : (AbsIntegratedStove) bd.getSerializable(PageArgumentKey.INTEGRATED_STOVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_integstove_fan_air_volume, container, false);
        ButterKnife.inject(this, view);
        initViews(mDeviceConfigurationFunctions);
        changedStatus();
        return view;
    }

    private void initViews(List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIService.getInstance().popBack();
            }
        });
        for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
            if ("smokeAirVolume".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                mFunctions = deviceConfigurationFunctions.get(i);
            }
        }
        try {
            String title = mFunctions.subView.title;
            mTvDeviceModelName.setText(title);
            SubView subView = mFunctions.subView;
            SubViewModelMapSubView subViewModelMapSubView = subView.subViewModelMap.subViewModelMapSubView;
            List<DeviceConfigurationFunctions> deviceVolumeList = subViewModelMapSubView.deviceConfigurationFunctions;
            for (int i = 0; i < deviceVolumeList.size(); i++) {
                if ("onOffLeft".equals(deviceVolumeList.get(i).functionCode)) {
                    fanLeftAndRight();
                    mTvStoveLeft.setText(deviceVolumeList.get(i).functionName);
                } else if ("onOffRight".equals(deviceVolumeList.get(i).functionCode)) {
                    fanLeftAndRight();
                    mTvStoveRight.setText(deviceVolumeList.get(i).functionName);
                } else if ("onOffMid".equals(deviceVolumeList.get(i).functionCode)) {
                    mTvStoveCenter.setText(deviceVolumeList.get(i).functionName);
                    fanNumCenter();
                } else if ("offButton".equals(deviceVolumeList.get(i).functionCode)) {
                    ImageUtils.displayImage(cx, deviceVolumeList.get(i).backgroundImg, mIvFanCloseVolume);
                }
            }

        } catch (Exception e) {
        }
    }

    private void fanLeftAndRight() {
        leftRight = "leftRight";
        mLlLeft.setVisibility(View.VISIBLE);
        mLlRight.setVisibility(View.VISIBLE);
        mLlCenter.setVisibility(View.GONE);
    }

    private void fanNumCenter() {
        center = "center";
        mLlLeft.setVisibility(View.INVISIBLE);
        mLlRight.setVisibility(View.INVISIBLE);
        mLlCenter.setVisibility(View.VISIBLE);
    }


    /**
     * 开启中部顺时针动画
     */
    void startCenterClockwiseAnimation() {
        mFlImgOnCenter.setVisibility(View.VISIBLE);
        mIvFanOffCenter.setVisibility(View.GONE);
        if (centerClockwiseAnimation == null) {
            centerClockwiseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.device_rika_clockwise_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            centerClockwiseAnimation.setInterpolator(lin);
            mIvFanOnWheelCenter.startAnimation(centerClockwiseAnimation);
        }
    }

    /**
     * 开启中部逆时针动画
     */
    void startCenterCounterclockwiseAnimation() {
        mFlImgOnCenter.setVisibility(View.VISIBLE);
        mIvFanOffCenter.setVisibility(View.GONE);
        if (centerCounterclockwiseAnimation == null) {
            centerCounterclockwiseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.device_rika_counterclockwise_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            centerCounterclockwiseAnimation.setInterpolator(lin);
            mIvFanOnRingCenter.startAnimation(centerCounterclockwiseAnimation);

        }
    }


    /**
     * 开启左侧顺时针动画
     */
    void startLeftClockwiseAnimation() {
        mFlImgOnLeft.setVisibility(View.VISIBLE);
        mIvFanOffLeft.setVisibility(View.GONE);
        if (leftClockwiseAnimation == null) {
            leftClockwiseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.device_rika_clockwise_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            leftClockwiseAnimation.setInterpolator(lin);
            mIvFanOnWheelLeft.startAnimation(leftClockwiseAnimation);
        }
    }

    /**
     * 开启左侧逆时针动画
     */
    void startLeftCounterclockwiseAnimation() {
        mFlImgOnLeft.setVisibility(View.VISIBLE);
        mIvFanOffLeft.setVisibility(View.GONE);
        if (leftCounterclockwiseAnimation == null) {
            leftCounterclockwiseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.device_rika_counterclockwise_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            leftCounterclockwiseAnimation.setInterpolator(lin);
            mIvFanOnRingLeft.startAnimation(leftCounterclockwiseAnimation);

        }
    }

    /**
     * 开启右侧顺时针动画
     */
    void startRightClockwiseAnimation() {
        mFlImgOnRight.setVisibility(View.VISIBLE);
        mIvFanOffRight.setVisibility(View.GONE);
        if (rightClockwiseAnimation == null) {
            rightClockwiseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.device_rika_clockwise_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            rightClockwiseAnimation.setInterpolator(lin);
            mIvFanOnWheelRight.startAnimation(rightClockwiseAnimation);
        }
    }

    /**
     * 开启右侧逆时针动画
     */
    void startRightCounterclockwiseAnimation() {
        mFlImgOnRight.setVisibility(View.VISIBLE);
        mIvFanOffRight.setVisibility(View.GONE);
        if (rightCounterclockwiseAnimation == null) {
            rightCounterclockwiseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.device_rika_counterclockwise_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            rightCounterclockwiseAnimation.setInterpolator(lin);
            mIvFanOnRingRight.startAnimation(rightCounterclockwiseAnimation);

        }
    }

    /**
     * 关闭左侧动画
     */
    private void stopLeftAnimation() {
        mFlImgOnLeft.setVisibility(View.GONE);
        mIvFanOffLeft.setVisibility(View.VISIBLE);
        if (leftCounterclockwiseAnimation != null) {
            leftCounterclockwiseAnimation.cancel();
            mIvFanOnRingLeft.clearAnimation();
            leftCounterclockwiseAnimation = null;
        }
        if (leftClockwiseAnimation != null) {
            leftClockwiseAnimation.cancel();
            mIvFanOnWheelLeft.clearAnimation();
            leftClockwiseAnimation = null;
        }
    }

    /**
     * 关闭中侧动画
     */
    private void stopCenterAnimation() {
        if (mFlImgOnCenter != null) {
            mFlImgOnCenter.setVisibility(View.GONE);
        }
        if (mIvFanOffCenter != null) {
            mIvFanOffCenter.setVisibility(View.VISIBLE);
        }
        if (centerCounterclockwiseAnimation != null) {
            centerCounterclockwiseAnimation.cancel();
            mIvFanOnRingCenter.clearAnimation();
            centerCounterclockwiseAnimation = null;
        }
        if (centerClockwiseAnimation != null) {
            centerClockwiseAnimation.cancel();
            mIvFanOnWheelCenter.clearAnimation();
            centerClockwiseAnimation = null;
        }
    }

    /**
     * 关闭右侧动画
     */
    private void stopRightAnimation() {
        mFlImgOnRight.setVisibility(View.GONE);
        mIvFanOffRight.setVisibility(View.VISIBLE);
        if (rightCounterclockwiseAnimation != null) {
            rightCounterclockwiseAnimation.cancel();
            mIvFanOnWheelRight.clearAnimation();
            rightCounterclockwiseAnimation = null;
        }
        if (rightClockwiseAnimation != null) {
            rightClockwiseAnimation.cancel();
            mIvFanOnRingRight.clearAnimation();
            rightClockwiseAnimation = null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLeftAnimation();
        stopRightAnimation();
        ButterKnife.reset(this);

    }

    @OnClick({R.id.iv_fan_close_volume, R.id.iv_volume_min, R.id.iv_volume_medium, R.id.iv_volume_max, R.id.iv_volume_xl})
    public void onViewClicked(View view) {
        if (integratedStove.fan_powerState == IntegStoveStatus.powerState_lock_clean){
            ToastUtils.showShort("烟机清洗锁定中");
            return;
        }
        switch (view.getId()) {

            //关闭烟机风量
            case R.id.iv_fan_close_volume:
                if (integratedStove.fan_powerState == IntegStoveStatus.powerState_off ){
                    ToastUtils.showShort("烟机未开启");
                    return;
                }
                if (SteamOvenHelper.isWork2(integratedStove.workState)) {
                    new MessageDialog.Builder(getActivity())
                            // 标题可以不用填写
                            .setTitle("提示")
                            // 内容必须要填写
                            .setMessage("一体机正在工作，\n请勿关闭烟机")
                            // 确定按钮文本
                            .setConfirm("确认")
                            // 设置 null 表示不显示取消按钮
                            .setCancel(null)
                            // 设置点击按钮后不关闭对话框
                            //.setAutoDismiss(false)
                            .setListener(new MessageDialog.OnListener() {
                                @Override
                                public void onConfirm(BaseDialog dialog) {
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                }
                            })
                            .show();
                    return;
                }
                if (integratedStove.stove_powerState != 0 ) {
                    new MessageDialog.Builder(getActivity())
                            // 标题可以不用填写
                            .setTitle("提示")
                            // 内容必须要填写
                            .setMessage("灶具正在工作，\n关闭风感会产生油烟")
                            // 确定按钮文本
                            .setConfirm("确认关闭")
                            // 设置 null 表示不显示取消按钮
                            .setCancel("取消")
                            // 设置点击按钮后不关闭对话框
                            //.setAutoDismiss(false)
                            .setListener(new MessageDialog.OnListener() {

                                @Override
                                public void onConfirm(BaseDialog dialog) {
                                    setVolume((short) 0, (short) 0);
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {

                                }
                            })
                            .show();
                    return;
                }
                setVolume((short) 0, (short) 0);

                break;
            case R.id.iv_volume_min:
//                integratedStove.fan_gear =1 ;
                setVolume((short) 1, (short) 1);
                changedStatus();
                break;
            case R.id.iv_volume_medium:
//                integratedStove.fan_gear = 2 ;
                setVolume((short) 1, (short) 2);
                changedStatus();
                break;
            case R.id.iv_volume_max:
//                integratedStove.fan_gear = 3 ;
                setVolume((short) 1, (short) 3);
                changedStatus();
                break;
            case R.id.iv_volume_xl:
//                integratedStove.fan_gear = 6 ;
                setVolume((short) 1, (short) 6);
                changedStatus();
                break;
            default:
                break;

        }
    }

    private void setVolume(short fan_powerCtrl, short fan_gear) {
        integratedStove.setFanVolume(fan_powerCtrl,
                fan_gear, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        integratedStove.fan_powerState = IntegStoveStatus.powerState_on;
                        integratedStove.fan_gear = fan_gear ;
                        changedStatus();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }
}
