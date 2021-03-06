package com.robam.roki.ui.page.device.rika;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.events.RikaSteamWorkEvent;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModel;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/9.
 */

public class DeviceRikaWorkPage extends BasePage {

    AbsRika mRika;
    String tag;
    String mViewBackgroundImg;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.tv_model)
    TextView mTvModel;
    @InjectView(R.id.tv_temp)
    TextView mTvTemp;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.tv_model_content)
    TextView mTvModelContent;
    @InjectView(R.id.tv_temp_content)
    TextView mTvTempContent;
    @InjectView(R.id.tv_time_content)
    TextView mTvTimeContent;
    @InjectView(R.id.btn_one)
    Button mBtnOne;
    @InjectView(R.id.btn_two)
    Button mBtnTwo;
    @InjectView(R.id.btn_there)
    Button mBtnThere;
    @InjectView(R.id.ll_mult)
    LinearLayout mLlMult;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout mLlRunAnimation;
    Animation circleRotateDown;
    Animation circleRotateUp;
    @InjectView(R.id.iv_run_down)
    ImageView mIvRunDown;
    @InjectView(R.id.iv_run_up)
    ImageView mIvRunUp;
    @InjectView(R.id.tv_work_state_name)
    TextView mTvWorkStateName;
    @InjectView(R.id.tv_work_dec)
    TextView mTvWorkDec;
    @InjectView(R.id.fl_run_stop)
    FrameLayout mFlRunStop;
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout mFlRunAndStop;
    private short mSteamWorkStatus;
    boolean sige = false;
    private IRokiDialog mCloseDialog;

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID()))
            return;
        mRika = event.pojo;
        short steamWorkRemainingTime = event.pojo.steamWorkRemainingTime;
        updateUI(steamWorkRemainingTime);
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.device.getID()))
            return;
        if (!mRika.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            UIService.getInstance().popBack();
        }
    }

    @Subscribe
    public void onEvent(RikaSteamWorkEvent event) {
        short steamEventCode = event.steamEventCode;
        short steamEventArg = event.steamEventArg;
        if (steamEventCode == 10 && steamEventArg == 6) {
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_finish);
            mTvWorkStateName.setTextSize(30);
            mTvWorkDec.setVisibility(View.GONE);
            sige = true;
        }
    }

    private void updateUI(short workTime) {
        short steamRunModel = mRika.steamRunModel;
        mSteamWorkStatus = mRika.steamWorkStatus;
        if (RikaModel.Steame.XIANNENZHENG == steamRunModel) {
            mTvDeviceModelName.setText(R.string.device_steamOvenOne_name_xianNenZheng);
            mTvModelContent.setText(R.string.device_steamOvenOne_name_xianNenZheng);
        } else if (RikaModel.Steame.YIYANGZHENG == steamRunModel) {
            mTvDeviceModelName.setText(R.string.device_steamOvenOne_name_yingYangZheng);
            mTvModelContent.setText(R.string.device_steamOvenOne_name_yingYangZheng);
        } else if (RikaModel.Steame.QINGLIZHENG == steamRunModel) {
            mTvDeviceModelName.setText(R.string.device_steamOvenOne_name_qiangLiZheng);
            mTvModelContent.setText(R.string.device_steamOvenOne_name_qiangLiZheng);
        } else if (RikaModel.Steame.BAOWEN == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_baowen);
            mTvModelContent.setText(R.string.model_baowen);
        } else if (RikaModel.Steame.FAJIAO == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_fajiao);
            mTvModelContent.setText(R.string.model_fajiao);
        } else if (RikaModel.Steame.CHUGOU == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_chugou);
            mTvModelContent.setText(R.string.model_chugou);
        } else if (RikaModel.Steame.KUAIZHENGMANDUN == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_kuaimain);
            mTvModelContent.setText(R.string.model_kuaimain);
        } else if (RikaModel.Steame.SHAJUN == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_shajun);
            mTvModelContent.setText(R.string.model_shajun);
        } else if (RikaModel.Steame.MIFAN == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_mifan_cooking);
            mTvModelContent.setText(R.string.model_mifan_cooking);
        } else if (RikaModel.Steame.MANTOU == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_mantou_cooking);
            mTvModelContent.setText(R.string.model_mantou_cooking);
        } else if (RikaModel.Steame.WUHUAROU == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_wuhuarou_cooking);
            mTvModelContent.setText(R.string.model_wuhuarou_cooking);
        } else if (RikaModel.Steame.JIEDONG == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_jiedong_cooking);
            mTvModelContent.setText(R.string.model_jiedong_cooking);
        } else if (RikaModel.Steame.MIANSHI == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_mianshi_cooking);
            mTvModelContent.setText(R.string.model_mianshi_cooking);
        } else if (RikaModel.Steame.SHUCAI == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_shucai_cooking);
            mTvModelContent.setText(R.string.model_shucai_cooking);
        } else if (RikaModel.Steame.TIJIN == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_tijin_cooking);
            mTvModelContent.setText(R.string.model_tijin_cooking);
        } else if (RikaModel.Steame.GAODIAN == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_gaodian_cooking);
            mTvModelContent.setText(R.string.model_gaodian_cooking);
        } else if (RikaModel.Steame.DANLEI == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_dan_cooking);
            mTvModelContent.setText(R.string.model_dan_cooking);
        } else if (RikaModel.Steame.YULEI == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_yu_cooking);
            mTvModelContent.setText(R.string.model_yu_cooking);
        } else if (RikaModel.Steame.ROULEI == steamRunModel) {
            mTvDeviceModelName.setText(R.string.model_rou_cooking);
            mTvModelContent.setText(R.string.model_rou_cooking);
        } else if (RikaModel.Steame.NO_MOEL == steamRunModel) {
            mTvDeviceModelName.setText(R.string.no_model);
            mTvModelContent.setText(R.string.no_model);
        }
        mTvTempContent.setText(mRika.steamSetTemp + "???");
        mTvTimeContent.setText(mRika.steamSetTime + "min");
        short steamWorkStatus = mRika.steamWorkStatus;
        if (steamWorkStatus == RikaStatus.STEAM_RUN) {
            startAnimation();
            mTvWorkStateName.setText(R.string.work_remaining_time);
            mTvWorkStateName.setTextSize(16);
            String time = TimeUtils.secToHourMinSec(workTime);
            mTvWorkDec.setText(time);
            mTvWorkDec.setTextSize(30);
            mFlRunStop.setVisibility(View.VISIBLE);
            mTvWorkDec.setVisibility(View.VISIBLE);
            sige = false;
        } else if (steamWorkStatus == RikaStatus.STEAM_STOP) {
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_stop);
            mTvWorkStateName.setTextSize(30);
            String time = TimeUtils.secToHourMinSec(workTime);
            mTvWorkDec.setText("?????????????????? " + time);
            mTvWorkDec.setTextSize(16);
            mFlRunStop.setVisibility(View.VISIBLE);
            mTvWorkDec.setVisibility(View.VISIBLE);
            sige = false;
        } else if (steamWorkStatus == RikaStatus.STEAM_PREHEAT) {
            startAnimation();
            mTvWorkStateName.setText(R.string.device_preheating);
            mTvWorkStateName.setTextSize(30);
            mTvWorkDec.setText("???????????????" + mRika.steamWorkTemp + "???");
            mTvWorkDec.setTextSize(16);
            mFlRunStop.setVisibility(View.VISIBLE);
            mTvWorkDec.setVisibility(View.VISIBLE);
            sige = false;
        } else if (steamWorkStatus == RikaStatus.STEAM_ON && sige) {
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_finish);
            mTvWorkStateName.setTextSize(30);
            mTvWorkDec.setVisibility(View.GONE);
            mFlRunStop.setVisibility(View.GONE);
        } else if (steamWorkStatus == RikaStatus.STEAM_NOT || steamWorkStatus == RikaStatus.STEAM_OFF || steamWorkStatus == RikaStatus.STEAM_ON && !sige) {
            sige = false;
            if (mCloseDialog != null && mCloseDialog.isShow()) {
                mCloseDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        tag = bd == null ? null : bd.getString(PageArgumentKey.tag);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
        View view = inflater.inflate(R.layout.page_rika_work, container, false);
        ButterKnife.inject(this, view);
        mLlMult.setVisibility(View.GONE);
        Glide.with(cx).load(mViewBackgroundImg).into(mIvBg);
        updateUI(mRika.steamWorkRemainingTime);
        return view;
    }

    /**
     * ????????????
     */
    void startAnimation() {
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            mIvRunUp.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mIvRunDown.startAnimation(circleRotateDown);
        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            mIvRunUp.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mIvRunUp.startAnimation(circleRotateUp);
        }
    }

    /**
     * ???????????????????????????
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

    @OnClick(R.id.fl_run_stop)
    public void onMFlRunStopClicked() {
        LogUtils.i("20180502", " mRika:" + mRika.steamWorkStatus);

        mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        mCloseDialog.setTitleText(R.string.close_work);
        mCloseDialog.setContentText(R.string.is_close_work);
        mCloseDialog.show();
        mCloseDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseDialog.dismiss();
                mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                        (short) 49, (short) 1, RikaStatus.STEAM_ON, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                UIService.getInstance().postPage(PageKey.AbsRikaDevice);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
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

}
