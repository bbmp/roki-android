package com.robam.roki.ui.page.device.rika;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.events.RikaSteamOvenWorkEvent;
import com.robam.common.events.RikaSteamWorkEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModel;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.ChildLockDialog;
import com.robam.roki.ui.view.SlideLockView;
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by 14807 on 2018/2/9.
 */

public class DeviceRikaYWorkPage extends BasePage implements OnTouchListener {

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

    @InjectView(R.id.tv_model)
    TextView mTvModel;
    @InjectView(R.id.tv_temp)
    TextView mTvTemp;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.tv_recipe)
    TextView mTvRecipe;
    @InjectView(R.id.auto_work_layout)
    LinearLayout mAutoLayout;
    @InjectView(R.id.recipe_work_layout)
    LinearLayout mRecipeLayout;

    @InjectView(R.id.ll_mult)
    LinearLayout mMultiLayout;
    @InjectView(R.id.btn_one)
    Button btnOne;
    @InjectView(R.id.btn_two)
    Button btnTwo;

    @InjectView(R.id.rl_lock)
    RelativeLayout mRlLock;
    private short mLockStatus;
    boolean sige = false;
    Vibrator mVibrator;
    private IRokiDialog mCloseDialog;
    private boolean isShow = true;
    private boolean isComplete = false;

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID()))
            return;
        mRika = event.pojo;
        int steamOvenTimeWorkRemaining = event.pojo.steamOvenTimeWorkRemaining;
        if (mRika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_NOT) {
            ToastUtils.showShort(R.string.steam_oven_invalid_error);
            UIService.getInstance().popBack();
            return;
        }
        if (mRika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT || mRika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN) {
            isComplete = false;
        }
        updateUI(steamOvenTimeWorkRemaining);
    }

    @Subscribe
    public void onEvent(RikaSteamOvenWorkEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.rika.getID()))
            return;
        if (event.steamOvenEventCode == 15) {
            if (event.steamOvenEventArg == 0) {
                complete();
            } else if (event.steamOvenEventArg == 1) {
                if (mRika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_OFF) {
                    UIService.getInstance().popBack();
                }
            }
        }
        if (event.steamOvenEventCode == 10) {
            if (event.steamOvenEventArg == 1) {
                if (mRika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_OFF) {
                    UIService.getInstance().popBack();
                }
            }
        }
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

    private void complete() {
        isComplete = true;
        stopAnimation();
        mTvDeviceModelName.setText("已完成");
        mTvSterilizerDesc.setVisibility(View.GONE);
        mTvWorkingResidueTimeDesc.setVisibility(View.GONE);
        mFlRunStop.setVisibility(View.GONE);
    }

    private void updateUI(int time) {
        if (isComplete) {
            return;
        }
        if (mRika.steamOvenTotalNumber == 2) {
            mMultiLayout.setVisibility(View.VISIBLE);
            if (mRika.steamOvenCurrentNumber == 2) {
                btnOne.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_mult_btn));
                btnTwo.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_mult_btn_bg));
            } else if (mRika.steamOvenCurrentNumber == 21) {
                btnOne.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_mult_btn));
                btnTwo.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_mult_btn));
            }
        } else {
            mMultiLayout.setVisibility(View.INVISIBLE);
        }
        short workStatus = mRika.steamOvenWorkStatus;
        mLockStatus = mRika.sterilLockStatus;
        String residueTime = TimeUtils.secToHourMinSec(time);
        mTvWorkingResidueTime.setVisibility(View.VISIBLE);
        mTvWorkingResidueTime.setText(residueTime);
        startAnimation();
        if (mRika.steamOvenAutomaticRecipe == 255) {
            if (mRika.steamOvenRunModel == RikaModel.SteamOven.BAOWEN) {
                mAutoLayout.setVisibility(View.INVISIBLE);
            } else {
                mAutoLayout.setVisibility(View.VISIBLE);
            }
            mRecipeLayout.setVisibility(View.GONE);
            mTvModel.setText(getModelName(mRika.steamOvenRunModel));
            mTvDeviceModelName.setText(getModelName(mRika.steamOvenRunModel));
            if (mRika.steamOvenRunModel == RikaModel.SteamOven.CHUGOU || mRika.steamOvenRunModel == RikaModel.SteamOven.GANZAO) {
                mTvTemp.setText("--℃");
            } else {
                mTvTemp.setText(mRika.steamOvenSetTemp + "℃");
            }
            mTvTime.setText(mRika.steamOvenSetTime+ "min");
        } else {
            mAutoLayout.setVisibility(View.GONE);
            mRecipeLayout.setVisibility(View.VISIBLE);
            mTvDeviceModelName.setText(getRecipeName(mRika.steamOvenAutomaticRecipe));
            mTvRecipe.setText("P"+mRika.steamOvenAutomaticRecipe+getRecipeName(mRika.steamOvenAutomaticRecipe));
        }

        if (RikaStatus.STEAMOVEN_RUN == workStatus) {
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText(R.string.work_remaining_time);
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            if (mRika.steamOvenRunModel == RikaModel.SteamOven.BAOWEN) {
                mTvWorkingResidueTimeDesc.setText("保温中");
                mTvWorkingResidueTimeDesc.setTextSize(36);
                mTvWorkingResidueTime.setVisibility(View.GONE);
            }
            sige = false;
//            mTvModel.setText(R.string.device_sterilizer);
        } else if (RikaStatus.STEAMOVEN_STOP == workStatus) {
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText("暂停中");
            mTvWorkingResidueTimeDesc.setTextSize(16);
            mTvWorkingResidueTime.setTextSize(36);
            sige = false;
//            mTvModel.setText(R.string.sterilize_clean_text);
        } else if (RikaStatus.STEAMOVEN_PREHEAT == workStatus) {
            mTvSterilizerDesc.setVisibility(View.GONE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setText("预热中");
            mTvWorkingResidueTimeDesc.setTextSize(36);
            mTvWorkingResidueTime.setTextSize(20);
            mTvWorkingResidueTime.setText("当前温度: " + mRika.steamOvenWorkTemp+ "℃" );
            mFlRunStop.setVisibility(View.VISIBLE);
            sige = false;
//            mTvModel.setText(R.string.sterilize_drying_text);
        } else if (RikaStatus.STERIL_ALARM == workStatus && mRika.steamOvenDoorState == 0) {
            mTvSterilizerDesc.setVisibility(View.VISIBLE);
            mTvWorkingResidueTimeDesc.setVisibility(View.VISIBLE);
            mTvSterilizerDesc.setText(R.string.device_door_off);
            mFlRunStop.setVisibility(View.VISIBLE);
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
        updateUI(mRika.steamOvenTimeWorkRemaining);
        mIvLockBg.setOnTouchListener(this);
        Glide.with(cx).load(mViewBackgroundImg).into(mIvBg);
        setListener();
        mFlChildLock.setVisibility(View.GONE);
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
        isComplete = false;
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

        if (mLockStatus == RikaStatus.STEAMOVEN_ALARM) {
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
            mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
            mCloseDialog.setTitleText(R.string.close_work);
            mCloseDialog.setContentText(R.string.is_close_work);
            mCloseDialog.show();
            mCloseDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCloseDialog.dismiss();
                    if (RikaStatus.STEAMOVEN_ON != mRika.steamOvenWorkStatus) {
                        short n = 0;
                        mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                                (short) 49, (short) 1, RikaStatus.STEAMOVEN_OFF, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        UIService.getInstance().popBack();
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
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    private String getModelName(int model) {
        switch (model) {
            case 21:
                return "高温蒸";
            case 16:
                return "营养蒸";
            case 22:
                return "风焙烤";
            case 23:
                return "加湿烤";
            case 14:
                return "杀菌";
            case 20:
                return "除垢";
            case 24:
                return "干燥";
            case 19:
                return "保温";
            case 18:
                return "发酵";
            case 9:
                return "解冻";
            default:
                return "多段";
        }
    }

    public String getRecipeName(int recipe) {
        switch (recipe) {
            case 1:
                return "清蒸鲈鱼";
            case 2:
                return "文蛤蒸蛋";
            case 3:
                return "豆豉蒸排骨";
            case 4:
                return "蒸馒头";
            case 5:
                return "烤红薯";
            case 6:
                return "奥尔良烤翅";
            case 7:
                return "戚风蛋糕";
            case 8:
                return "锡纸烤排骨";
            case 9:
                return "香酥鸡";
            case 10:
                return "重庆烤鱼";
            case 11:
                return "烤牛排";
            case 12:
                return "脆皮猪肘";
            default:
                return "";
        }
    }
}
