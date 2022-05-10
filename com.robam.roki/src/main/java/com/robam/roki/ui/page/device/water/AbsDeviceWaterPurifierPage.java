package com.robam.roki.ui.page.device.water;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.WaterPurifiyAlarmEvent;
import com.robam.common.events.WaterPurifiyStatusChangedEvent;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.ui.view.SlideLockView;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;


import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by 14807 on 2018/10/19.
 */
public class AbsDeviceWaterPurifierPage<WaterPurifier extends AbsWaterPurifier> extends DeviceCatchFilePage {

    WaterPurifier mWaterPurifier;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView mIvDeviceMore;
    @InjectView(R.id.tv_off_line_text)
    TextView mTvOffLineText;
    @InjectView(R.id.rv_filter_element_status)
    TextView mRvFilterElementStatus;
    @InjectView(R.id.tv_filter_element_show)
    TextView mTvFilterElementShow;
    @InjectView(R.id.iv_filter_element_show)
    ImageView mIvFilterElementShow;
    @InjectView(R.id.tv_filter_about_element_show)
    TextView mTvFilterAboutElementShow;
    @InjectView(R.id.iv_filter_about_element_show)
    ImageView mIvFilterAboutElementShow;
    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @InjectView(R.id.iv_lock_bg)
    ImageView mIvLockBg;
    @InjectView(R.id.slv_lock)
    SlideLockView mSlvLock;
    @InjectView(R.id.tv_child_lock)
    TextView mTvChildLock;
    @InjectView(R.id.tv_ppm)
    TextView mTvPpm;
    @InjectView(R.id.rl_lock)
    RelativeLayout mRlLock;
    @InjectView(R.id.layout_bg)
    FrameLayout mLayoutBg;
    WaterPurifierOtherFuncAdapter mOtherFuncAdapter;
    @InjectView(R.id.iv_water_orange)
    ImageView mIvWaterOrange;
    @InjectView(R.id.iv_water_green)
    ImageView mIvWaterGreen;
    @InjectView(R.id.iv_water_blue)
    ImageView mIvWaterBlue;
    @InjectView(R.id.iv_water_gray)
    ImageView mIvWaterGray;
    @InjectView(R.id.iv_water_circle)
    ImageView mIvWaterCircle;
    @InjectView(R.id.tv_water_state)
    TextView mTvWaterState;
    @InjectView(R.id.tv_water_dec)
    TextView mTvWaterDec;
    Animation circleRotateDown;
    Animation circleRotateDownOrange;
    Animation circleRotateUp;
    @InjectView(R.id.ll_filter_element_status)
    LinearLayout mLlFilterElementStatus;
    @InjectView(R.id.fr_filter_element_show)
    RelativeLayout mFrFilterElementShow;
    @InjectView(R.id.fr_filter_about_element_show)
    RelativeLayout mFrFilterAboutElementShow;
    private List<DeviceConfigurationFunctions> mOtherFuncList;
    private List<DeviceConfigurationFunctions> mBackgroundFuncList;
    String singAboutExpire = null;
    String singExpire = null;
    private BackgroundFunc mBackgroundFunc;
    private List<DeviceConfigurationFunctions> mExpireList;
    private List<DeviceConfigurationFunctions> mAboutexpireList;
    boolean sing = true;
    private IRokiDialog dialogByType;

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (mWaterPurifier == null || !Objects.equal(mWaterPurifier.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.showLong(R.string.oven_dis_con);
            mTvOffLineText.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onEvent(WaterPurifiyStatusChangedEvent event) {//轮训查询数据
        if (mWaterPurifier == null || !Objects.equal(mWaterPurifier.getID(), event.pojo.getID()))
            return;
        mWaterPurifier = (WaterPurifier) event.pojo;
        mRvFilterElementStatus.setText(setWaterBeforeQuality(mWaterPurifier.input_tds, mWaterPurifier.output_tds));
        eventFilterStatus();
        switch (mWaterPurifier.status) {
            case WaterPurifierStatus.Off:
            case WaterPurifierStatus.Wait:
                setClose();
                break;
            case WaterPurifierStatus.On:
                setOpen();
                break;
            case WaterPurifierStatus.Wash://冲洗
                setWashModel();
                break;
            case WaterPurifierStatus.Purify://制水
                setCleanModel();
                break;
            case WaterPurifierStatus.AlarmStatus://报警
                setAlarmModel();
                break;
            default:
                break;
        }
        if (!mWaterPurifier.isConnected()) {
            ToastUtils.showLong(R.string.oven_dis_con);
            if (mTvOffLineText != null) {
                mTvOffLineText.setVisibility(View.VISIBLE);
            }
        } else {
            if (mTvOffLineText != null) {
                mTvOffLineText.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void eventFilterStatus() {

        if (mWaterPurifier.filter_state_pp == 255 || mWaterPurifier.filter_state_pp == 0) {
            singExpire = "1";
            getImgDataExpire();
        } else if (mWaterPurifier.filter_state_cto == 255 || mWaterPurifier.filter_state_cto == 0) {
            singExpire = "2";
            getImgDataExpire();
        } else if (mWaterPurifier.filter_state_ro1 == 255 || mWaterPurifier.filter_state_ro1 == 0) {
            singExpire = "3";
            getImgDataExpire();
        } else if (mWaterPurifier.filter_state_ro2 == 255 || mWaterPurifier.filter_state_ro2 == 0) {
            singExpire = "4";
            getImgDataExpire();
        } else if (mWaterPurifier.filter_state_pp != 255 && mWaterPurifier.filter_state_cto != 255
                && mWaterPurifier.filter_state_ro1 != 255 && mWaterPurifier.filter_state_ro2 != 255
                || mWaterPurifier.filter_state_pp != 0 && mWaterPurifier.filter_state_cto != 0
                && mWaterPurifier.filter_state_ro1 != 0 && mWaterPurifier.filter_state_ro2 != 0) {
            mFrFilterElementShow.setVisibility(View.GONE);
            mIvWaterBlue.setImageResource(R.mipmap.ic_water_blue);
        }

        if (mWaterPurifier.filter_state_pp <= 10 && mWaterPurifier.filter_state_pp > 0) {
            singAboutExpire = "1";
            getImgDataAboutExpire();
        } else if (mWaterPurifier.filter_state_cto <= 10 && mWaterPurifier.filter_state_cto > 0) {
            singAboutExpire = "2";
            getImgDataAboutExpire();
        } else if (mWaterPurifier.filter_state_ro1 <= 10 && mWaterPurifier.filter_state_ro1 > 0) {
            singAboutExpire = "3";
            getImgDataAboutExpire();
        } else if (mWaterPurifier.filter_state_ro2 <= 10 && mWaterPurifier.filter_state_ro2 > 0) {
            singAboutExpire = "4";
            getImgDataAboutExpire();
        } else if (mWaterPurifier.filter_state_pp > 10 && mWaterPurifier.filter_state_cto > 10 &&
                mWaterPurifier.filter_state_ro1 > 10 && mWaterPurifier.filter_state_ro2 > 10) {
            mFrFilterAboutElementShow.setVisibility(View.GONE);
        }
    }

    //滤芯到期
    private void getImgDataExpire() {
        if (mBackgroundFuncList != null && mBackgroundFuncList.size() > 0) {
            mIvWaterBlue.setImageResource(R.mipmap.ic_water_gray);
            mTvWaterState.setText(R.string.device_water_filter_expire_text);
            mTvWaterState.setTextSize(16);
            stopAnimation();
            for (int i = 0; i < mBackgroundFuncList.size(); i++) {
                if ("FilterElementExpire".equals(mBackgroundFuncList.get(i).functionCode)) {
                    String functionName = mBackgroundFuncList.get(i).functionName;
                    String img = mBackgroundFuncList.get(i).backgroundImg;
                    Glide.with(cx).load(img).into(mIvFilterElementShow);
                    mTvFilterElementShow.setText(functionName);
                    mFrFilterAboutElementShow.setVisibility(View.GONE);
                    mFrFilterElementShow.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    //滤芯即将到期
    private void getImgDataAboutExpire() {
        LogUtils.i("20181130", "sing:" + sing);
        if (sing) {
            EventUtils.postEvent(new WaterPurifiyAlarmEvent(mWaterPurifier, 255));
            sing = false;
        }
        if (mBackgroundFuncList != null && mBackgroundFuncList.size() > 0) {
            for (int i = 0; i < mBackgroundFuncList.size(); i++) {
                if ("filterElementDue".equals(mBackgroundFuncList.get(i).functionCode)) {
                    String functionName = mBackgroundFuncList.get(i).functionName;
                    String img = mBackgroundFuncList.get(i).backgroundImg;
                    Glide.with(cx).load(img).into(mIvFilterAboutElementShow);
                    mTvFilterAboutElementShow.setText(functionName);

                    if (singExpire == null) {
                        mFrFilterAboutElementShow.setVisibility(View.VISIBLE);
                        mFrFilterElementShow.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    protected String setWaterBeforeQuality(short tdsBefore, short tdsAfter) {
        short tdsTemp;
        if (tdsBefore != 0) {
            float precent = (float) (tdsBefore - tdsAfter) / tdsBefore;
            if (precent < 0.85) {
                tdsTemp = (short) (tdsAfter / (1 - precent));
                return waterQuality(tdsTemp);
            } else {
                return waterQuality(tdsBefore);
            }
        } else {
            return "优";
        }
    }

    protected String waterQuality(short tdswater) {
        short tds = tdswater;
        if (tds >= 0 && tds <= 80) {
            return "优";
        } else if (tds > 80 && tds <= 300) {
            return "良";
        } else if (tds > 300 && tds <= 600) {
            return "中";
        } else if (tds > 600 && tds <= 1000) {
            return "差";
        } else if (tds > 1000) {
            return "极差";
        } else {
            return null;
        }


    }

    private void setClose() {
        stopAnimation();
        mTvWaterState.setText(R.string.device_water_open_faucet_text);
        mTvWaterState.setTextSize(16);
        mTvWaterDec.setVisibility(View.GONE);
        mTvPpm.setVisibility(View.GONE);
        if (tag) {
            mOtherFuncAdapter.notifyItemChanged(2);
            LogUtils.i("20191011111", "setCleanModel:::" + mWaterPurifier.status);
            tag = false;
        }
    }

    private void setOpen() {
        stopAnimation();
        mTvWaterState.setText(cx.getString(R.string.device_water_purification_water_quality_text)
                + ":" + waterQuality(mWaterPurifier.output_tds));
        mTvWaterState.setTextSize(14);
        mTvWaterDec.setVisibility(View.VISIBLE);
        mTvWaterDec.setText(setTDS(mWaterPurifier.output_tds));
        mTvWaterDec.setTextSize(46);
        mTvPpm.setVisibility(View.VISIBLE);
        if (tag) {
            mOtherFuncAdapter.notifyItemChanged(2);
            LogUtils.i("20191011111", "setCleanModel:::" + mWaterPurifier.status);
            tag = false;
        }
    }

    //报警
    private void setAlarmModel() {
        mTvWaterDec.setVisibility(View.GONE);
        if (mWaterPurifier.filter_state_pp == 255 || mWaterPurifier.filter_state_cto == 255
                || mWaterPurifier.filter_state_ro1 == 255 || mWaterPurifier.filter_state_ro2 == 255
                ) {
            mTvWaterState.setText(R.string.device_water_filter_expire_text);
            mTvWaterState.setTextSize(16);
        }
        if (mWaterPurifier.alarm != 2) {
            mTvWaterState.setText(R.string.device_alarm);
            mTvWaterState.setTextSize(16);
        }
        mTvPpm.setVisibility(View.GONE);
        mIvWaterBlue.setImageResource(R.mipmap.ic_water_gray);
        stopAnimation();
        if (tag) {
            mOtherFuncAdapter.notifyItemChanged(2);
            LogUtils.i("20191011111", "setCleanModel:::" + mWaterPurifier.status);
            tag = false;
        }
    }

    //制水
    private void setCleanModel() {
        startAnimation();
        if (mWaterPurifier.waterCleanQulity == 1) {
            mTvWaterState.setText(R.string.device_water_clean_water_text);
            mTvWaterState.setTextSize(16);
            mTvWaterDec.setVisibility(View.GONE);
        } else if (mWaterPurifier.waterCleanQulity == 2) {
            mTvWaterState.setText(R.string.device_water_pure_water_text);
            mTvWaterState.setTextSize(16);
            mTvWaterDec.setVisibility(View.GONE);
        } else {
            mTvWaterState.setText(R.string.device_water_pure_water_text);
            mTvWaterState.setTextSize(16);
            mTvWaterDec.setVisibility(View.GONE);
        }
        mTvPpm.setVisibility(View.GONE);
        if (tag) {
            mOtherFuncAdapter.notifyItemChanged(2);
            LogUtils.i("20191011111", "setCleanModel:::" + mWaterPurifier.status);
            tag = false;
        }

    }

    boolean tag = false;

    //冲洗
    private void setWashModel() {
        startAnimation();
        mTvWaterState.setText(R.string.device_water_filter_wash_text);
        mTvWaterDec.setText(cx.getString(R.string.device_water_time_remaining_wash_text) + (18 - mWaterPurifier.work_time) + "s");
        mTvWaterDec.setVisibility(View.VISIBLE);
        mTvWaterDec.setTextSize(14);
        mTvPpm.setVisibility(View.GONE);
        if (!tag) {
            mOtherFuncAdapter.notifyItemChanged(2);
            LogUtils.i("20191011111", "setWashModel:::" + mWaterPurifier.status);
            tag = true;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_water_base, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse obj) {

        mWaterPurifier = (WaterPurifier) mDevice;
        mRvFilterElementStatus.setText(setWaterBeforeQuality(mWaterPurifier.input_tds, mWaterPurifier.output_tds));
        if (!mWaterPurifier.isConnected()) {
            ToastUtils.showLong(R.string.oven_dis_con);
            if (mTvOffLineText != null) {
                mTvOffLineText.setVisibility(View.VISIBLE);
            }
        }
        String deviceTitle = obj.title;
        mTvDeviceModelName.setText(deviceTitle);
        String backgroundImg = obj.viewBackgroundImg;
        Glide.with(cx).load(backgroundImg).into(mIvBg);
        OtherFunc otherFunc = obj.modelMap.otherFunc;
        mOtherFuncList = otherFunc.deviceConfigurationFunctions;
        mOtherFuncAdapter = new WaterPurifierOtherFuncAdapter(mOtherFuncList, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {

                if (!mWaterPurifier.isConnected()) {
                    ToastUtils.showLong(R.string.oven_dis_con);
                    return;
                }
                //下面横条item点击事件
                otherFuncItemEvent(view);
            }
        });
        mRecyclerview.setAdapter(mOtherFuncAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));

        mBackgroundFunc = obj.modelMap.backgroundFunc;
        mBackgroundFuncList = mBackgroundFunc.deviceConfigurationFunctions;
        if (mBackgroundFuncList != null && mBackgroundFuncList.size() > 0) {
            for (int i = 0; i < mBackgroundFuncList.size(); i++) {

                if ("filterElementDue".equals(mBackgroundFuncList.get(i).functionCode)) {
                    mAboutexpireList = mBackgroundFuncList.get(i).subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                } else if ("FilterElementExpire".equals(mBackgroundFuncList.get(i).functionCode)) {
                    mExpireList = mBackgroundFuncList.get(i).subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                }

            }
        }


    }

    private void otherFuncItemEvent(View view) {
        String tag = view.getTag().toString();
        LogUtils.i("20181030", "tag:" + tag);
        switch (tag) {
            case "filterCoreState":
                Bundle filterCoreStateBundle = new Bundle();
                filterCoreStateBundle.putSerializable(PageArgumentKey.Bean, mWaterPurifier);
                filterCoreStateBundle.putSerializable(PageArgumentKey.List, (Serializable) mOtherFuncList);
                UIService.getInstance().postPage(PageKey.WaterPurifierFilterCoreState, filterCoreStateBundle);
                break;
            case "statistics":
                Bundle statisticsBundle = new Bundle();
                statisticsBundle.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.HouseholdDrinkingWaterStatistics, statisticsBundle);
                break;
            //冲洗功能
            case "remoteIrrigation":
                if (mWaterPurifier.status == WaterPurifierStatus.AlarmStatus) {
                    short alarm = mWaterPurifier.alarm;
                    AlarmDataUtils.onWaterPurifiyAlarmEvent(mWaterPurifier, alarm);

                } else {
                    if (mWaterPurifier.status == WaterPurifierStatus.Off) {
                        return;
                    }
                    if (mWaterPurifier.status != WaterPurifierStatus.Wash) {
                        showDialog();
                    }
                }
                break;
            default:
                LogUtils.i("20191012", tag);
                ToastUtils.showShort("即将开放...");
                break;
        }
    }

    private void showDialog() {
        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        dialogByType.setTitleText("冲洗滤芯");
        dialogByType.setContentText("确认开启冲洗滤芯功能？");
        dialogByType.show();
        dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogByType != null && dialogByType.isShow()) {
                    dialogByType.dismiss();
                }
                setRemoteIrr();
            }
        });
        dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogByType != null && dialogByType.isShow()) {
                    dialogByType.dismiss();
                }
            }
        });
    }

    private void setRemoteIrr() {
        mWaterPurifier.setRemoteIrrigation((short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20191011", "成功+mWaterPurifier:::  " + mWaterPurifier.status);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20191011", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    protected String setTDS(short tds) {
        if (tds >= 0 && tds < 10) {
            return "00" + tds;
        } else if (tds < 100 && tds >= 10) {
            return "0" + tds;
        } else if (tds >= 100) {
            return tds + "";
        } else {
            return "-";
        }
    }

    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate_water);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            mIvWaterGreen.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mIvWaterGreen.startAnimation(circleRotateDown);
        }

        if (circleRotateDownOrange == null) {
            circleRotateDownOrange = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate_water);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDownOrange.setInterpolator(lin);
            mIvWaterOrange.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mIvWaterOrange.startAnimation(circleRotateDownOrange);
        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            mIvWaterBlue.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mIvWaterBlue.startAnimation(circleRotateUp);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            mIvWaterGreen.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            mIvWaterBlue.clearAnimation();
        }

        if (circleRotateDownOrange != null) {
            circleRotateDownOrange.cancel();
            circleRotateDownOrange = null;
            mIvWaterOrange.clearAnimation();
        }
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {

        Bundle bundle = new Bundle();
        bundle.putString(PageArgumentKey.Guid, mGuid);
        UIService.getInstance().postPage(PageKey.WaterPurifierDeviceMore, bundle);
    }

    @OnClick({R.id.fr_filter_element_show, R.id.fr_filter_about_element_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fr_filter_element_show:
                if (View.VISIBLE == mFrFilterElementShow.getVisibility()) {


                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PageArgumentKey.Bean, mWaterPurifier);
                    bundle.putSerializable(PageArgumentKey.List, (Serializable) mExpireList);
                    bundle.putSerializable(PageArgumentKey.mAboutexpireList, (Serializable) mAboutexpireList);
                    bundle.putString(PageArgumentKey.singExpire, singExpire);
                    UIService.getInstance().postPage(PageKey.WaterPurifierFilterAboutExpire, bundle);
                }

                break;
            case R.id.fr_filter_about_element_show:
                if (View.VISIBLE == mFrFilterAboutElementShow.getVisibility()) {

                    Bundle bundleAbout = new Bundle();
                    bundleAbout.putSerializable(PageArgumentKey.Bean, mWaterPurifier);
                    bundleAbout.putSerializable(PageArgumentKey.mAboutexpireList, (Serializable) mAboutexpireList);
                    bundleAbout.putString(PageArgumentKey.singAboutExpire, singAboutExpire);
                    UIService.getInstance().postPage(PageKey.WaterPurifierFilterAboutExpire, bundleAbout);
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mWaterPurifier == null) {
            return;
        }
    }


    public class WaterPurifierOtherFuncAdapter extends RecyclerView.Adapter<WaterPurifierOtherFuncViewHolder> {

        private LayoutInflater mInflater;
        List<DeviceConfigurationFunctions> mList;
        private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

        public WaterPurifierOtherFuncAdapter(List<DeviceConfigurationFunctions> otherFuncList
                , OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            mList = otherFuncList;
            mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
            mInflater = LayoutInflater.from(cx);
        }


        @Override
        public WaterPurifierOtherFuncViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_otherfunc_page, parent, false);

            WaterPurifierOtherFuncViewHolder otherFuncViewHolder = new WaterPurifierOtherFuncViewHolder(view);
            otherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRecyclerViewItemClickListener.onItemClick(v);
                }
            });
            return otherFuncViewHolder;
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public void onBindViewHolder(WaterPurifierOtherFuncViewHolder holder, int position) {
            if (mList != null && mList.size() > 0) {
                if (position == 2) {
                    if (mWaterPurifier.status == WaterPurifierStatus.Wash) {
                        Glide.with(cx)
                                .load(mList.get(position).backgroundImgH)
//                                .crossFade()
                                .into(holder.mImageView);
                        holder.mTvName.setTextColor(getResources().getColor(R.color.c001));
                        holder.mTvDesc.setTextColor(getResources().getColor(R.color.c001));
                    } else {
                        Glide.with(cx).load(mList.get(position).backgroundImg)
//                                .crossFade()
                                .into(holder.mImageView);
                        holder.mTvName.setTextColor(getResources().getColor(R.color.c002));
                        holder.mTvDesc.setTextColor(getResources().getColor(R.color.c002));
                    }
                } else {
                    Glide.with(cx)
                            .load(mList.get(position).backgroundImg)
//                            .crossFade()
                            .into(holder.mImageView);
                    holder.mTvName.setTextColor(getResources().getColor(R.color.c002));
                    holder.mTvDesc.setTextColor(getResources().getColor(R.color.c002));
                }

                holder.mTvName.setText(mList.get(position).functionName);
                holder.mTvDesc.setText(mList.get(position).msg);
            }
            holder.mItemView.setTag(mList.get(position).functionCode);
        }

    }

    class WaterPurifierOtherFuncViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTvDesc;
        TextView mTvName;
        LinearLayout mItemView;


        public WaterPurifierOtherFuncViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mImageView = itemView.findViewById(R.id.iv_view);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
            mItemView = itemView.findViewById(R.id.itemView);

        }
    }

}