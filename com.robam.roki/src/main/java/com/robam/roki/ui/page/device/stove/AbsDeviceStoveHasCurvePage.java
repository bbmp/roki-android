package com.robam.roki.ui.page.device.stove;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CureFinishEvent;
import com.robam.common.events.NewBieGuideEvent;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.services.StoveAlarmManager;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.StoveBackgroundFunParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.StoveOtherFuncAdapter;
import com.robam.roki.ui.adapter.StoveOtherFuncHasCurveAdapter;
import com.robam.roki.ui.dialog.StoveQuickOffFireDialog;
import com.robam.roki.ui.dialog.type.DialogType_30;
import com.robam.roki.ui.form.CookCurveActivity;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.DeviceJsonToBeanUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StoveLevelUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 9B010 带曲线灶具
 *
 * @param <Device>
 */
public class AbsDeviceStoveHasCurvePage<Device extends Stove> extends DeviceCatchFilePage
        implements View.OnTouchListener {

    Device stove;
    String mGuid;
    String mDeviceCategory;
    @InjectView(R.id.iv_bg)
    ImageView ivBg;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView ivDeviceMore;
    @InjectView(R.id.img_right_head)
    ImageView img_right_head;
    @InjectView(R.id.img_left_head)
    ImageView img_left_head;
    @InjectView(R.id.tv_left_head_state)
    TextView tvLeftHeadState;
    @InjectView(R.id.tv_left_pot_temp)
    TextView tvLeftPotTemp;
    @InjectView(R.id.v_center_line)
    View vCenterLine;
    @InjectView(R.id.tv_right_head_state)
    TextView tvRightHeadState;
    @InjectView(R.id.tv_right_pot_temp)
    TextView tvRightPotTemp;
    @InjectView(R.id.tv_off_line_text)
    TextView tvOffLineText;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.layout_bg)
    FrameLayout layoutBg;

    private IRokiDialog mStoveLevelAlarm_03;
    Vibrator mVibrator;
    private StoveOtherFuncHasCurveAdapter mStoveOtherFuncAdapter;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions = new ArrayList<>();
    private List<DeviceConfigurationFunctions> mMainFunList;
    private long curveId = 0;
    DialogType_30 rokiDialog = null;
    //快速关火弹窗
    StoveQuickOffFireDialog stoveQuickOffFireDialog = null;
    //左右灶显示参数
    StoveBackgroundFunParams mParamsLeft, mParamsRight;
    DeviceConfigurationFunctions mLeftFunctions, mRightFunctions;
    // 无人锅
    private Pot pot = null;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mStoveOtherFuncAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    setDataToView((Reponses.DeviceResponse) msg.obj);
                    break;
            }
        }
    };

    @Subscribe
    public void onEvent(StoveAlarmEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.stove.getID()))
            return;
        switch (event.alarm.getID()) {
            case StoveAlarmManager.E_1:
                if (IRokiFamily.RQZ02.equals(event.stove.getDp())) {
                    mStoveLevelAlarm_03.setContentText(R.string.device_alarm_stove_9b39_E1_content);
                    mStoveLevelAlarm_03.setToastShowTime(DialogUtil.LENGTH_LONG);
                    mStoveLevelAlarm_03.show();
                } else if (IRokiFamily.RQZ01.equals(event.stove.getDp())) {
                    //mStoveLevelAlarm_03.setContentText(R.string.device_alarm_stove_9w70_E1_content);
                }

                break;
        }
    }

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("20180518", event.pojo.getID());
        if (stove == null || !Objects.equal(stove.getID(), event.pojo.getID()))
            return;
        tvOffLineText.setVisibility(View.GONE);
        rokiDialog.setStatus(stove);
        stoveQuickOffFireDialog.setStatus(stove);
        updataStoveStatus();
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.showLong(R.string.device_new_connected);
            tvOffLineText.setVisibility(View.VISIBLE);
        }

    }
    //上条曲线记录结束
    @Subscribe
    public void onEvent(CureFinishEvent event) {
        curveId = 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDeviceCategory = bd == null ? null : bd.getString(PageArgumentKey.deviceCategory);
        stove = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.page_device_stove_has_curve, container, false);
        ButterKnife.inject(this, view);
        initData();
        setDeepTheme();
        mStoveLevelAlarm_03 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        rokiDialog = RokiDialogFactory.createDialogType_30(cx);
        stoveQuickOffFireDialog = new StoveQuickOffFireDialog(cx);
        openPageQuery(1);
        return view;
    }

    private void setDeepTheme() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.pot_black_bg));
            StatusBarUtils.setTextDark(getContext(), false);

        }

    }

    protected void setDataToView(Reponses.DeviceResponse deviceResponse) {

        if (mDevice instanceof Stove) {
            stove = (Device) mDevice;
        }

        if (!stove.isConnected()) {
            ToastUtils.showLong(R.string.device_new_connected);
            if (tvOffLineText != null) {
                tvOffLineText.setVisibility(View.VISIBLE);
            }
        }

        try {
            ModelMap modelMap = deviceResponse.modelMap;
            tvDeviceModelName.setText(deviceResponse.title);
            Glide.with(cx).load(deviceResponse.viewBackgroundImg).into(ivBg);
//            MainFunc mainFunc = modelMap.mainFunc;
//            if(mMainFunList!=null){
//                mMainFunList = mainFunc.deviceConfigurationFunctions;
//                mDeviceConfigurationFunctions.addAll(mMainFunList);
//            }
            mParamsLeft = DeviceJsonToBeanUtils.JsonToObject(modelMap.backgroundFunc.deviceConfigurationFunctions.get(0).functionParams, StoveBackgroundFunParams.class);
            mParamsRight = DeviceJsonToBeanUtils.JsonToObject(modelMap.backgroundFunc.deviceConfigurationFunctions.get(1).functionParams, StoveBackgroundFunParams.class);
            mLeftFunctions = modelMap.backgroundFunc.deviceConfigurationFunctions.get(0);
            mRightFunctions = modelMap.backgroundFunc.deviceConfigurationFunctions.get(1);
            //主功能区
            OtherFunc otherFunc = modelMap.otherFunc;
            List<DeviceConfigurationFunctions> otherFuncList = otherFunc.deviceConfigurationFunctions;
            mDeviceConfigurationFunctions.addAll(otherFuncList);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(cx, 1);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = recyclerview.getAdapter().getItemViewType(position);
                    if (viewType == StoveOtherFuncAdapter.OTHER_VIEW) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
            mStoveOtherFuncAdapter = new StoveOtherFuncHasCurveAdapter(cx, stove, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    if (mMainFunList == null || mMainFunList.size() == 0) return;

                }
            });
            mStoveOtherFuncAdapter.setOnClickOpenCurve(new StoveOtherFuncHasCurveAdapter.OnClickOpenCurve() {
                @Override
                public void onClickOpenCurve() {
                    if(!checkPot()){
                        return;
                    }
                    if(curveId!=0){//有曲线正在记录
                        Bundle bd = new Bundle();
                        bd.putString(PageArgumentKey.Guid, stove.getGuid().getGuid());
                        bd.putInt(PageArgumentKey.HeadId, 1);
                        bd.putLong(PageArgumentKey.curveId, curveId);
                        bd.putString(PageArgumentKey.pot, pot.getID());
                        CookCurveActivity.start(activity, bd);
                        return;
                    }
                    if (stove.leftHead.status == 0 && stove.rightHead.status == 0) {
                        ToastUtils.show("炉头均处于关闭状态", Toast.LENGTH_SHORT);
                        return;
                    }
                    rokiDialog.setCancelBtn(0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.show("左灶开启烹饪曲线", Toast.LENGTH_LONG);
//                            query(0);
                        }
                    });
                    rokiDialog.setOkBtn(0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.show("右灶开启烹饪曲线", Toast.LENGTH_LONG);
                            rokiDialog.dismiss();
                            query(1);
                        }
                    });
                    rokiDialog.show();

                }
            });
            mStoveOtherFuncAdapter.setOnClickOffFire(new StoveOtherFuncHasCurveAdapter.OnClickOffFire() {
                @Override
                public void onClickOffFire() {
                    if (stove.leftHead.status == 0 && stove.rightHead.status == 0) {
                        ToastUtils.show("炉头均处于关闭状态", Toast.LENGTH_SHORT);
                        return;
                    }
                    stoveQuickOffFireDialog.show();
                }

            });

            recyclerview.setLayoutManager(gridLayoutManager);
            recyclerview.setAdapter(mStoveOtherFuncAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    //查看父烟机下是否有无人锅
    private boolean checkPot(){
        //查询灶具父烟机下是否有无人锅
        String parentGuid = stove.getParent().getGuid().getGuid();
        AbsDeviceHub deviceHub;
        deviceHub = Plat.deviceService.lookupChild(parentGuid);
         pot = deviceHub.getChildPot();
        if (pot==null) {
            ToastUtils.show("请先添加无人锅", Toast.LENGTH_SHORT);
            return false;
        }
        if(!pot.isConnected()){
            ToastUtils.show("无人锅已离线", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
    private void updataStoveStatus() {
        short leftStatus = stove.leftHead.status;
        String stoveStatus = StoveLevelUtils.getStoveLevel(leftStatus, mParamsLeft);
        tvLeftHeadState.setText(stoveStatus);
        String stoveLeftTemp = StoveLevelUtils.getStovePotTemp(stove.leftTemp, mParamsLeft);
        tvLeftPotTemp.setText(stoveLeftTemp);
        String stoveLefttColor = StoveLevelUtils.getStovePotTextColor(stove.leftTemp, mParamsLeft);
        if(!TextUtils.isEmpty(stoveLefttColor)){
            tvLeftPotTemp.setTextColor(Color.parseColor(stoveLefttColor));
        }
        if (leftStatus == 2) {
            Glide.with(cx)
                    .asGif()
                    .load(mLeftFunctions.backgroundImgH)
                    .into(img_left_head);
        } else {
            Glide.with(cx)
                    .load(R.mipmap.icon_stove_left)
                    .into(img_left_head);
            tvLeftPotTemp.setText("锅温");
//            tvLeftPotTemp.setTextColor(Color.parseColor(stoveLefttColor));
            tvLeftPotTemp.setTextColor(Color.parseColor(mParamsLeft.getTempDTO().getpotTempDTO().getColor()));
        }

        short mRightLevel = stove.rightHead.level;
        String stoveLevel = StoveLevelUtils.getStoveLevel(mRightLevel, mParamsRight);
        tvRightHeadState.setText(stoveLevel);
        String stoveRightTemp = StoveLevelUtils.getStovePotTemp(stove.rightTemp, mParamsRight);
        tvRightPotTemp.setText(stoveRightTemp);
        String stoveRightColor = StoveLevelUtils.getStovePotTextColor(stove.rightTemp, mParamsRight);
        if(!TextUtils.isEmpty(stoveRightColor)){
            tvRightPotTemp.setTextColor(Color.parseColor(stoveRightColor));
        }
        if (mRightLevel > 0) {
            Glide.with(cx)
                    .asGif()
                    .load(mRightFunctions.backgroundImgH)
                    .into(img_right_head);
        } else {
            Glide.with(cx)
                    .load(R.mipmap.icon_stove_right)
                    .into(img_right_head);
            tvRightPotTemp.setText("锅温");
            tvRightPotTemp.setTextColor(Color.parseColor(mParamsRight.getTempDTO().getpotTempDTO().getColor()));
        }

    }

    private void query(int headId) {
        //mGuid 暂时写死241
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        if (rcReponse.payload == null) {
                            cookingCurveSave(headId);
                        } else {
                            curveId = rcReponse.payload.curveCookbookId;
                            Bundle bd = new Bundle();
                            bd.putString(PageArgumentKey.Guid, stove.getGuid().getGuid());
                            bd.putInt(PageArgumentKey.HeadId, headId);
                            bd.putLong(PageArgumentKey.curveId, curveId);
                            bd.putString(PageArgumentKey.pot, pot.getID());
//                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
                            CookCurveActivity.start(activity, bd);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    private void cookingCurveSave(int headId) {
        String deviceGuid = stove.getGuid().getGuid();
        int id = 0;
        String model = "";
        String setTemp = "";
        String setTime = "";
        RokiRestHelper.cookingCurveSave(deviceGuid, id, model, setTemp, setTime, headId,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveSaveRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveSaveRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        if (rcReponse.payload != 0) {
                            curveId = rcReponse.payload;
                            Bundle bd = new Bundle();
                            bd.putString(PageArgumentKey.Guid, deviceGuid);
                            bd.putInt(PageArgumentKey.HeadId, headId);
                            bd.putLong(PageArgumentKey.curveId, curveId);
                            bd.putString(PageArgumentKey.pot, pot.getID());
//                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
                            CookCurveActivity.start(activity, bd);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
                    }
                });

    }

    private void openPageQuery(int headId) {
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {
                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        if (rcReponse.payload != null) {
                            curveId = rcReponse.payload.curveCookbookId;
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (stove == null) {
            return;
        }
        if (stove.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), stove.getDt(), null);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            StatusBarUtils.setTextDark(getContext(), true);
        }
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }


    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, stove.getID());
        bd.putString(PageArgumentKey.stove, "stove");
        UIService.getInstance().postPage(PageKey.StoveDeviceMore, bd);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }



}
