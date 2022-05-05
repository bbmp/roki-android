package com.robam.roki.ui.page.device.integratedStove;

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
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.SubView;
import com.legent.ui.IForm;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.IntegratedStoveStatusChangedEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.events.RikaSteamWorkEvent;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.ChuGouEnum;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.IntegratedStoveConstant;
import com.robam.common.pojos.device.integratedStove.RepiceEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModel;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneEnum;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringUtil;

import java.util.IllformedLocaleException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/9.
 */

public class IntegratedStoveWorkPage extends BasePage {

    AbsIntegratedStove integratedStove;
    String tag;
    String mViewBackgroundImg;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.ll_rika_z)
    LinearLayout ll_rika_z;
    @InjectView(R.id.tv_recipe)
    TextView tv_recipe;
    @InjectView(R.id.tv_model)
    TextView mTvModel;
    @InjectView(R.id.tv_steam)
    TextView tv_steam;
    @InjectView(R.id.tv_temp)
    TextView mTvTemp;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.tv_model_content)
    TextView mTvModelContent;
    @InjectView(R.id.tv_steam_content)
    TextView tv_steam_content;
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

    @InjectView(R.id.fl_pause)
    FrameLayout fl_pause;
    @InjectView(R.id.iv_pause)
    ImageView iv_pause;
    @InjectView(R.id.tv_pause)
    TextView tv_pause;
    @InjectView(R.id.fl_add_steam)
    FrameLayout fl_add_steam;
    @InjectView(R.id.iv_add_steam)
    ImageView iv_add_steam;
    @InjectView(R.id.iv_finish)
    ImageView iv_finish;

    @InjectView(R.id.tv_chugou_message)
    TextView tv_chugou_message;

    /**
     * 当前运行状态
     */
    private short mSteamWorkStatus;
    private IRokiDialog mCloseDialog;
    private List<DeviceConfigurationFunctions> mDatas;
    /**
     * 当前工作模式
     */
    private short steamRunModel;
    /**
     * 是否需要水
     */
    private String needWater = "0";
    private int outTime;

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (integratedStove == null || !Objects.equal(integratedStove.getID(), event.device.getID())) {
            return;
        }
        if (!event.isConnected ) {
//            ToastUtils.showLong(R.string.device_new_connected);
            UIService.getInstance().popBack();
        }
    }

    @Subscribe
    public void onEvent(IntegStoveStatusChangedEvent event) {
        if (integratedStove == null || !Objects.equal(integratedStove.getID(), event.pojo.getID())) {
            return;
        }
        integratedStove = event.pojo;
        if (!integratedStove.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            UIService.getInstance().popBack();
            return;
        }
        if (integratedStove.getID().equals(event.pojo.getID())) {
            if ((integratedStove.workState == IntegStoveStatus.workState_free
                    || integratedStove.powerState != IntegStoveStatus.powerState_on) && integratedStove.faultCode == 0
            ) {
                UIService.getInstance().popBack();
                return;
            }
            try {
                updateUI();
            }catch (Exception e){
                e.getMessage() ;
            }

        }

    }


    private void updateUI() {
        //菜谱模式
        mTvDeviceModelName.setText(integratedStove.getDt());
        if (integratedStove.recipeId != 0) {
            setRecipeMode();
            ll_rika_z.setVisibility(View.GONE);
            tv_recipe.setVisibility(View.VISIBLE);
        } else {
            tv_recipe.setVisibility(View.GONE);
            ll_rika_z.setVisibility(View.VISIBLE);
            //运行模式
             steamRunModel = integratedStove.mode;
            SteamOvenModeEnum match = SteamOvenModeEnum.match(steamRunModel);
            String value = match.getValue();
            if (match == SteamOvenModeEnum.CHUGOU) {
                ll_rika_z.setVisibility(View.GONE);
                tv_recipe.setVisibility(View.VISIBLE);
                tv_recipe.setText(value);
                fl_pause.setVisibility(View.GONE);
                fl_add_steam.setVisibility(View.GONE);
                mFlRunStop.setVisibility(View.GONE);
                short chugouType = integratedStove.chugouType;
                tv_chugou_message.setVisibility(View.VISIBLE);
                String chugouMessage = "";
                chugouMessage = ChuGouEnum.match(chugouType) == null ? "" : ChuGouEnum.match(chugouType).getValue() ;

                //工作状态
                short steamWorkStatus = integratedStove.workState;
                if (steamWorkStatus == IntegStoveStatus.workState_work) {
                    startAnimation();
                    mTvWorkStateName.setText("除垢中");
                    mTvWorkStateName.setTextSize(29);
                    mTvWorkDec.setVisibility(View.GONE);
                    //第二段第三段有暂停
                    if (ChuGouEnum.match(chugouType) != ChuGouEnum.CHUGOU1){
                        String[] split = chugouMessage.split("&");
                        if (StringUtil.isEmpty(chugouMessage) || split == null || split.length < 2){
                            return;
                        }
                        tv_chugou_message.setText(split[1]);
                    }else {
                        tv_chugou_message.setText(chugouMessage);
                    }
                } else if (steamWorkStatus == IntegStoveStatus.workState_work_time_out) {
                    //工作暂停
                    stopAnimation();
                    mTvWorkStateName.setText(R.string.device_stop);
                    mTvWorkStateName.setTextSize(29);
                    mTvWorkDec.setVisibility(View.GONE);
                    //第二段第三段有暂停
                    if (ChuGouEnum.match(chugouType) != ChuGouEnum.CHUGOU1){
                        String[] split = chugouMessage.split("。");
                        if (StringUtil.isEmpty(chugouMessage) || split == null || split.length < 2){
                            return;
                        }
                        tv_chugou_message.setText(split[0]);
                    }else {
                        tv_chugou_message.setText(chugouMessage);
                    }
                }
                return;
            }
            mTvModelContent.setText(value);
            mTvTempContent.setText(integratedStove.setUpTemp + "℃");
            int setTime = integratedStove.setTimeH * 256 + integratedStove.setTime;
            mTvTimeContent.setText(setTime + "min");
            if (SteamOvenHelper.isShowSteam(steamRunModel)) {
                tv_steam.setVisibility(View.VISIBLE);
                tv_steam_content.setVisibility(View.VISIBLE);
                tv_steam_content.setText(SteamOvenHelper.getSteamContent(integratedStove.steam));
            } else {
                tv_steam.setVisibility(View.GONE);
                tv_steam_content.setVisibility(View.GONE);
            }
            //是否多段
            if (SteamOvenHelper.isMult(integratedStove.sectionNumber)) {
                mLlMult.setVisibility(View.VISIBLE);
                setMultShow(integratedStove.sectionNumber);
            } else {
                mLlMult.setVisibility(View.GONE);
            }

        }
        //工作状态
        short steamWorkStatus = integratedStove.workState;
        //结束按钮
        GlideApp.with(cx).load(integratedStove.finishUrl == null ? R.mipmap.img_run_stop : integratedStove.finishUrl)
                .placeholder(R.mipmap.img_run_stop)
                .error(R.mipmap.img_run_stop)
                .into(iv_finish);

        if (steamWorkStatus == IntegStoveStatus.workState_work) {
            startAnimation();
            mTvWorkStateName.setText(R.string.work_remaining_time);
            mTvWorkStateName.setTextSize(16);
             outTime = integratedStove.restTimeH * 256 + integratedStove.restTime;
            String time = TimeUtils.secToHourMinSec(outTime);
            //菜谱模式
            if (integratedStove.recipeId != 0){
                time = TimeUtils.secToHourMinSec(integratedStove.totalRemainSecondsH * 256 + integratedStove.totalRemainSeconds);
            }
            mTvWorkDec.setText(time);
            mTvWorkDec.setTextSize(30);
            tv_pause.setText("暂停");
            GlideApp.with(cx).load(integratedStove.workingPauseUrl == null ? R.drawable.ic_pause_ytj : integratedStove.workingPauseUrl)
                    .placeholder(R.drawable.ic_pause_ytj)
                    .error(R.drawable.ic_pause_ytj)
                    .into(iv_pause);
            //是否可以加蒸汽
            if (SteamOvenHelper.isAddSteam(steamRunModel) && !SteamOvenHelper.isMult(integratedStove.sectionNumber)) {
                fl_add_steam.setVisibility(View.VISIBLE);
                if (integratedStove.workingAddSteamUrl != null) {
                    GlideApp.with(cx).load(integratedStove.workingAddSteamUrl)
                            .placeholder(R.drawable.icon_add_steam)
                            .error(R.drawable.icon_add_steam)
                            .into(iv_add_steam);
                }else {
                    GlideApp.with(cx).load(R.drawable.icon_add_steam).into(iv_add_steam);
                }
            } else {
                fl_add_steam.setVisibility(View.INVISIBLE);
            }
        } else if (steamWorkStatus == IntegStoveStatus.workState_work_time_out) {
            //工作暂停
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_stop);
            mTvWorkStateName.setTextSize(16);
            String time = TimeUtils.secToHourMinSec(integratedStove.restTimeH * 256 + integratedStove.restTime);
            //菜谱模式
            if (integratedStove.recipeId != 0){
                time = TimeUtils.secToHourMinSec(integratedStove.totalRemainSecondsH * 256 + integratedStove.totalRemainSeconds);
            }
            mTvWorkDec.setText(time);
            mTvWorkDec.setTextSize(30);
            tv_pause.setText("继续");
            GlideApp.with(cx).load(integratedStove.workingPauseHUrl == null ? R.drawable.ic_start_ytj : integratedStove.workingPauseHUrl)
                    .placeholder(R.drawable.ic_start_ytj)
                    .error(R.drawable.ic_start_ytj)
                    .into(iv_pause);
//            iv_pause.setImageResource(R.drawable.ic_start_ytj);
            fl_add_steam.setVisibility(View.INVISIBLE);
        } else if (steamWorkStatus == IntegStoveStatus.workState_preheat) {
            startAnimation();
            mTvWorkStateName.setText(R.string.device_preheating);
            mTvWorkStateName.setTextSize(16);
            mTvWorkDec.setText(+integratedStove.curTemp + "℃");
            mTvWorkDec.setTextSize(30);
            tv_pause.setText("暂停");
            GlideApp.with(cx).load(integratedStove.workingPauseUrl == null ? R.drawable.ic_pause_ytj : integratedStove.workingPauseUrl)
                    .placeholder(R.drawable.ic_pause_ytj)
                    .error(R.drawable.ic_pause_ytj)
                    .into(iv_pause);
//            iv_pause.setImageResource(R.drawable.ic_pause_ytj);
            fl_add_steam.setVisibility(View.INVISIBLE);
        } else if (steamWorkStatus == IntegStoveStatus.workState_preheat_time_out) {
            //预热暂停
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_stop);
            mTvWorkStateName.setTextSize(16);
            String time = TimeUtils.secToHourMinSec(integratedStove.restTimeH * 256 + integratedStove.restTime);
            //菜谱模式
            if (integratedStove.recipeId != 0){
                time = TimeUtils.secToHourMinSec(integratedStove.totalRemainSecondsH * 256 + integratedStove.totalRemainSeconds);
            }
            mTvWorkDec.setText(time);
            mTvWorkDec.setTextSize(30);
            mFlRunStop.setVisibility(View.VISIBLE);
            mTvWorkDec.setVisibility(View.VISIBLE);
            tv_pause.setText("继续");
            GlideApp.with(cx).load(integratedStove.workingPauseHUrl == null ? R.drawable.ic_start_ytj : integratedStove.workingPauseHUrl)
                    .placeholder(R.drawable.ic_start_ytj)
                    .error(R.drawable.ic_start_ytj)
                    .into(iv_pause);
//            iv_pause.setImageResource(R.drawable.ic_start_ytj);
            fl_add_steam.setVisibility(View.INVISIBLE);
        } else if (steamWorkStatus == IntegStoveStatus.workState_complete) {
            stopAnimation();
            mTvWorkStateName.setText(R.string.device_finish);
            mTvWorkStateName.setTextSize(26);
            mTvWorkDec.setVisibility(View.GONE);
            mFlRunStop.setVisibility(View.GONE);
            fl_pause.setVisibility(View.GONE);
            fl_add_steam.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置菜谱模式顶部显示
     */
    public void setRecipeMode() {
        ll_rika_z.setVisibility(View.GONE);
        tv_recipe.setVisibility(View.VISIBLE);
        if (integratedStove.cookMap != null) {
            String recipeName = integratedStove.cookMap.get(String.valueOf(integratedStove.recipeId)) == null ? "" : integratedStove.cookMap.get(String.valueOf(integratedStove.recipeId));
            tv_recipe.setText("P" + integratedStove.recipeId + " " + recipeName);

        }else {
            try {
                RepiceEnum match = RepiceEnum.match(integratedStove.recipeId);
                tv_recipe.setText("P" + integratedStove.recipeId + " " + match.getValue());
                ll_rika_z.setVisibility(View.GONE);
                tv_recipe.setVisibility(View.VISIBLE);
            }catch (Exception e){
                e.getMessage();
            }

        }
        if (integratedStove.cookNeedWaterMap != null){
            needWater = integratedStove.cookNeedWaterMap.get(String.valueOf(integratedStove.recipeId)) == null ? "0" : integratedStove.cookNeedWaterMap.get(String.valueOf(integratedStove.recipeId));
        }
    }

    /**
     * 设置正常工作模式显示
     */
    public void setMode() {

    }


    /**
     * 设置多段状态的显示
     */
    public void setMultShow(short sectionNumber) {
        if (sectionNumber == 2) {
            mBtnThere.setVisibility(View.GONE);
        }
        if (integratedStove.curSectionNbr == 1) {
            mBtnOne.setBackgroundResource(R.drawable.shape_rika_mult_btn_bg);
            mBtnTwo.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnThere.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
        } else if (integratedStove.curSectionNbr == 2) {
            mBtnOne.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnTwo.setBackgroundResource(R.drawable.shape_rika_mult_btn_bg);
            mBtnThere.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
        } else if (integratedStove.curSectionNbr == 3) {
            mBtnOne.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnTwo.setBackgroundResource(R.drawable.shape_integrated_mult_btn);
            mBtnThere.setBackgroundResource(R.drawable.shape_rika_mult_btn_bg);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        integratedStove = bd == null ? null : (AbsIntegratedStove) bd.getSerializable(PageArgumentKey.INTEGRATED_STOVE);
        tag = bd == null ? null : bd.getString(PageArgumentKey.tag);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
        View view = inflater.inflate(R.layout.page_integ_stove_work, container, false);
        ButterKnife.inject(this, view);
        mLlMult.setVisibility(View.GONE);
        Glide.with(cx).load(mViewBackgroundImg).into(mIvBg);

        updateUI();
        return view;
    }

    /**
     * 开启动画
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

    /**
     * 加蒸汽
     */
    @OnClick(R.id.fl_add_steam)
    public void addSteam() {
        if (outTime < 120){
            ToastUtils.showShort("当前模式最后两分钟不能加蒸汽");
            return;
        }
        if (!SteamOvenHelper.isWaterBoxState(integratedStove.waterBoxState)) {
            ToastUtils.showShort("水箱已弹出，请检查水箱状态");
            return;
        }
        if (integratedStove.steamState != 2) {
            ToastUtils.showShort("距离上次加蒸汽时间太近");
            return;
        }
//        if (SteamOvenHelper.isDescale(integratedStove.descaleFlag)) {
//            ToastUtils.showShort("设备需要除垢后才能加蒸汽，请先除垢");
//            return;
//        }
        integratedStove.setSteamWorkStatus((short) 1, (short) 16, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort("加蒸汽成功");
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    /**
     * 暂停 开始
     */
    @OnClick(R.id.fl_pause)
    public void onMFlPauseClicked() {

        //暂停状态
        if (SteamOvenHelper.isPause(integratedStove.workState)) {
            //需要水的模式
            if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(integratedStove.mode)) || SteamOvenHelper.isRecipeWater(needWater)) {
                if (SteamOvenHelper.isDescale(integratedStove.descaleFlag)) {
                    ToastUtils.showShort("设备需要除垢后才能继续工作，请先除垢");
                    return;
                }
                if (!SteamOvenHelper.isWaterBoxState(integratedStove.waterBoxState)) {
                    ToastUtils.showShort("水箱已弹出，请检查水箱状态");
                    return;
                }
                if (!SteamOvenHelper.isWaterLevelState(integratedStove.waterLevelState)) {
                    ToastUtils.showShort("水箱缺水，请加水");
                    return;
                }
            }
        }
        integratedStove.setSteamWorkStatus(SteamOvenHelper.isPause(integratedStove.workState) ? IntegStoveStatus.workCtrl_continue : IntegStoveStatus.workCtrl_time_out, (short) 4, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @OnClick(R.id.fl_run_stop)
    public void onMFlRunStopClicked() {
        LogUtils.i("20180502", " mRika:" + integratedStove.steamWorkStatus);
        mCloseDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        mCloseDialog.setTitleText(R.string.close_work);
        mCloseDialog.setContentText(R.string.is_close_work);
        mCloseDialog.show();
        mCloseDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseDialog.dismiss();
                integratedStove.setSteamWorkStatus(IntegStoveStatus.workCtrl_stop, (short) 4, new VoidCallback() {
                    @Override
                    public void onSuccess() {
//                                UIService.getInstance().postPage(PageKey.AbsRikaDevice);
                        UIService.getInstance().popBack();
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
