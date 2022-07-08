package com.robam.roki.ui.page.device.rika;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RikaAlarmEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.events.RikaSterilizerChildLockEvent;
import com.robam.common.events.RikaSterilizerWorkFinishEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaAlarmCodeBean;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.ChildLockDialog;
import com.robam.roki.ui.page.device.AbsDeviceBasePage;
import com.robam.roki.ui.view.SlideLockView;
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.legent.ContextIniter.cx;

/**
 * Created by 14807 on 2018/2/9.
 */

public class DeviceRikaXWorkPage extends BasePage implements OnTouchListener {

    AbsRika mRika;
    String tag;
    String mViewBackgroundImg;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    //    @InjectView(R.id.tv_model)
//    TextView mTvModel;
    Animation circleRotateDown;
    Animation circleRotateUp;
    @InjectView(R.id.iv_run_down)
    ImageView mIvRunDown;
    @InjectView(R.id.iv_run_up)
    ImageView mIvRunUp;
    @InjectView(R.id.fl_run_stop)
    FrameLayout mFlRunStop;
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout mFlRunAndStop;
    @InjectView(R.id.fl_child_lock)
    FrameLayout mFlChildLock;
    @InjectView(R.id.tv_water_text)
    TextView mTvWaterText;
    @InjectView(R.id.iv_child_lock)
    ImageView mIvChildLock;
    @InjectView(R.id.tv_working_residue_time_desc)
    TextView mTvWorkingResidueTimeDesc;
    @InjectView(R.id.tv_working_residue_time)
    TextView mTvWorkingResidueTime;
    @InjectView(R.id.tv_sterilizer_desc)
    TextView mTvSterilizerDesc;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout mLlRunAnimation;
    @InjectView(R.id.iv_lock_bg)
    ImageView mIvLockBg;
    @InjectView(R.id.slideLockView)
    SlideLockView mSlideLockView;
    @InjectView(R.id.tv_child_lock)
    TextView mTvChildLock;
    @InjectView(R.id.rl_lock)
    RelativeLayout mRlLock;
    private short mLockStatus;
    boolean sige = false;
    Vibrator mVibrator;
    private IRokiDialog mCloseDialog;
    private boolean isShow = true;

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID()))
            return;
        mRika = event.pojo;
        int sterilWorkTimeLeft = event.pojo.sterilWorkTimeLeft;
        updateUI(sterilWorkTimeLeft);
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.showLong(R.string.device_new_connected);
            UIService.getInstance().popBack();
        }
    }

    @Subscribe
    public void onEvent(RikaSterilizerChildLockEvent event) {
        mLockStatus = event.sterilEventArg;
//        lockDialog();

    }

    @Subscribe
    public void onEvent(RikaSterilizerWorkFinishEvent event) {
        short sterilEventArg = event.sterilEventArg;
        if (sterilEventArg != -1) {
            if (1 == sterilEventArg) {//==1 强制结束工作
                UIService.getInstance().postPage(PageKey.AbsRikaDevice);
            } else if (0 == sterilEventArg) {
                mTvWorkingResidueTime.setText(R.string.device_finish);
                mTvWorkingResidueTime.setTextSize(36);
                mTvWorkingResidueTimeDesc.setVisibility(View.GONE);
                mTvSterilizerDesc.setVisibility(View.GONE);
                mFlRunStop.setVisibility(View.GONE);
                mFlChildLock.setVisibility(View.GONE);
                sige = true;
            }
        }
    }

    private void updateUI(int time) {
        short sterilWorkStatus = mRika.sterilWorkStatus;
        mLockStatus = mRika.sterilLockStatus;
        String residueTime = TimeUtils.secToHourMinSec(time);
        mTvWorkingResidueTime.setText(residueTime);
        startAnimation();
        if (RikaStatus.STERIL_DISIDFECT == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.device_sterilizer);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.device_sterilizer);
        } else if (RikaStatus.STERIL_CLEAN == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_clean_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_clean_text);
        } else if (RikaStatus.STERIL_DRYING == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_drying_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_drying_text);
        } else if (RikaStatus.STERIL_APPOINATION == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_pre_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText("预约中");
            mTvWorkingResidueTimeDesc.setTextSize(36);
            mTvWorkingResidueTime.setText("剩余时间:" + residueTime);
            mTvWorkingResidueTime.setTextSize(16);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_pre_text);
        } else if (RikaStatus.STERIL_DEGERMING == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_degerming_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_degerming_text);
        } else if (RikaStatus.STERIL_INTELLIGENT_DETECTION == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_intelligent_detection_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_intelligent_detection_text);
        } else if (RikaStatus.STERIL_INDUCTION_STERILIZATION == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_induction_sterilization_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_induction_sterilization_text);
        } else if (RikaStatus.STERIL_WARM_DISH == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_warm_dish_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_warm_dish_text);
        } else if (RikaStatus.STERIL_APPOINATION_DRYING == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_pre_drying_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText("预约中");
            mTvWorkingResidueTimeDesc.setTextSize(36);
            mTvWorkingResidueTime.setText("剩余时间:" + residueTime);
            mTvWorkingResidueTime.setTextSize(16);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;

        } else if (RikaStatus.STERIL_APPOINATION_CLEAN == sterilWorkStatus) {
            mTvDeviceModelName.setText(R.string.sterilize_pre_degerming_text);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText("预约中");
            mTvWorkingResidueTimeDesc.setTextSize(36);
            mTvWorkingResidueTime.setText("剩余时间:" + residueTime);
            mTvWorkingResidueTime.setTextSize(16);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;

        } else if (RikaStatus.STERIL_COER_DISIDFECT == sterilWorkStatus) {
            if (isShow){
                ToastUtils.showLong(R.string.is_close_work_content_15);
//                Toast.makeText(cx,R.string.is_close_work_content_15,6000);
                isShow = false;
            }
            mTvDeviceModelName.setText(R.string.sterilize_core_dish_text);
            mTvSterilizerDesc.setVisibility(View.VISIBLE);
            mTvSterilizerDesc.setText(R.string.rika_clean_ozone);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
            sige = false;
        } else if (RikaStatus.STERIL_ON == sterilWorkStatus && sige) {
            stopAnimation();
            mTvWorkingResidueTime.setText(R.string.device_finish);
            mTvWorkingResidueTime.setTextSize(36);
            mTvWorkingResidueTimeDesc.setVisibility(View.GONE);
            mTvSterilizerDesc.setVisibility(View.GONE);
            mFlChildLock.setVisibility(View.GONE);
        } else if (RikaStatus.STERIL_NOT == sterilWorkStatus || RikaStatus.STERIL_OFF == sterilWorkStatus || RikaStatus.STERIL_ON == sterilWorkStatus && !sige) {
            sige = false;
            if (mCloseDialog != null && mCloseDialog.isShow()) {
                mCloseDialog.dismiss();
            }
            UIService.getInstance().popBack();
        } else if (RikaStatus.STERIL_ALARM == sterilWorkStatus && mRika.sterilDoorLockStatus == 0) {
            mTvSterilizerDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvSterilizerDesc.setText(R.string.device_door_off);
            mFlRunStop.setVisibility(View.VISIBLE);
            mFlChildLock.setVisibility(View.VISIBLE);
        }
        lockDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        tag = bd == null ? null : bd.getString(PageArgumentKey.tag);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        tag = bd == null ? null : bd.getString(PageArgumentKey.tag);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
        View view = inflater.inflate(R.layout.page_rika_x_work, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    protected void initData() {
        updateUI(mRika.sterilWorkTimeLeft);
        mIvLockBg.setOnTouchListener(this);
        Glide.with(cx).load(mViewBackgroundImg).into(mIvBg);
        setListener();
    }

    public void setListener() {
        // 获取系统振动器服务
        mVibrator = (Vibrator) cx.getSystemService(VIBRATOR_SERVICE);
        mSlideLockView.setLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                // 启动震动器 100ms
                mVibrator.vibrate(100);
                mRika.setSterilizerLockStatus(MsgKeys.setDeviceRunStatus_Req, (short) 1, RikaStatus.STERIL_CATEGORYCODE
                        , (short) 1, (short) 50, (short) 1, RikaStatus.STERIL_LOCK_OFF, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                mIvLockBg.setVisibility(View.GONE);
                                mRlLock.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
            }

        });
    }

    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            mIvRunDown.startAnimation(circleRotateDown);

        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            mIvRunUp.startAnimation(circleRotateUp);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            mIvRunDown.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            mIvRunUp.clearAnimation();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAnimation();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }


    @OnClick(R.id.fl_child_lock)
    public void onMFlWaterClicked() {
        if (mRika == null) return;

        if (mRika.sterilWorkStatus == RikaStatus.STERIL_COER_DISIDFECT) {
            ToastUtils.showShort(R.string.device_rikax_close);
            return;
        }

        mRika.setSterilizerLockStatus(MsgKeys.setDeviceRunStatus_Req, (short) 1,
                RikaStatus.STERIL_CATEGORYCODE, (short) 1, (short) 50, (short) 1, RikaStatus.STERIL_LOCK_ON,
                new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mIvLockBg.setVisibility(View.VISIBLE);
                        mRlLock.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

    }

    ChildLockDialog mChildLockDialog;

    private void lockDialog() {

        if (mLockStatus == RikaStatus.STERIL_LOCK_ON) {
//            if (mChildLockDialog == null) {
//                mChildLockDialog = ChildLockDialog.build(cx, R.layout.child_lock, null, mRika);
//                Window win = mChildLockDialog.getWindow();
//                win.getDecorView().setPadding(0, 0, 0, 0);
//                win.setGravity(Gravity.BOTTOM);
//                WindowManager m = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
//                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//                WindowManager.LayoutParams lp = win.getAttributes();
//                lp.height = d.getHeight();
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                win.setAttributes(lp);
//                mChildLockDialog.show();
//            }
            mIvLockBg.setVisibility(View.VISIBLE);
            mRlLock.setVisibility(View.VISIBLE);
        } else {

//            if (mLockStatus == RikaStatus.STERIL_LOCK_OFF && mChildLockDialog.isShowing()) {
//                mChildLockDialog.dismiss();
//                mChildLockDialog = null;
//            }
            mIvLockBg.setVisibility(View.GONE);
            mRlLock.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fl_run_stop)
    public void onMFlRunStopClicked() {

        if (mRika.sterilWorkStatus != RikaStatus.STERIL_COER_DISIDFECT) {
            mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
            mCloseDialog.setTitleText(R.string.close_work);
            mCloseDialog.setContentText(R.string.is_close_work);
            mCloseDialog.show();
            mCloseDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCloseDialog.dismiss();
                    if (RikaStatus.STERIL_ON != mRika.sterilWorkStatus) {
                        short n = 0;
                        mRika.setSterilizerWorkStatus(MsgKeys.setDeviceRunStatus_Req, (short) 1,
                                RikaStatus.STERIL_CATEGORYCODE, (short) 1, (short) 49,
                                (short) 4, RikaStatus.STERIL_ON
                                , n, n, n, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });
                    }
                }
            });
            mCloseDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCloseDialog.isShow()) {
                        mCloseDialog.dismiss();
                    }
                }
            });

        } else {
            if (RikaStatus.STERIL_COER_DISIDFECT == mRika.sterilWorkStatus) {
                ToastUtils.showShort(R.string.device_rikax_close);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }


}
