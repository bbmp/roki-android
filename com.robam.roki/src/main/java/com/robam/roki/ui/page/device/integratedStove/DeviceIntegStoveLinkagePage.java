package com.robam.roki.ui.page.device.integratedStove;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.base.BaseDialog;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModeName;
import com.robam.common.pojos.device.rika.RikaSmartParams;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.IntegratedStoveLinkageParams;
import com.robam.roki.model.bean.RikaSteamLinkageParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.mdialog.MessageDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/9.
 * 烟灶联动 烟蒸烤联动
 */

public class DeviceIntegStoveLinkagePage extends BasePage {


    AbsIntegratedStove mIntegratedStove;
    List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.fan_pic_show_1)
    ImageView mFanPicShow1;
    @InjectView(R.id.fan_linkage_dec)
    LinearLayout mFanLinkageDec;
    @InjectView(R.id.chkIsInternalDays)
    CheckBoxView mChkIsInternalDays;

//    @InjectView(R.id.chkIsFanSteamLink)
//    CheckBoxView mChkFanSteamLink;
    @InjectView(R.id.iv_other_linkage_switch)
    ImageView mIvOtherLinkageSwitch;
    @InjectView(R.id.ll_other_linkage_dec)
    LinearLayout mLlOtherLinkageDec;
    @InjectView(R.id.tv_recovery)
    TextView mTvRecovery;
    @InjectView(R.id.tv_fan_linkage_name)
    TextView mTvFanLinkageName;
    @InjectView(R.id.tv_other_linkage_name)
    TextView mTvOtherLinkageName;
    @InjectView(R.id.tv_fan_folding)
    TextView mTvFanFolding;
    @InjectView(R.id.tv_fan_linkage_dec)
    TextView mTvFanLinkageDec;
    @InjectView(R.id.tv_other_folding)
    TextView mTvOtherFolding;
    @InjectView(R.id.tv_other_linkage_dec)
    TextView mTvOtherLinkageDec;
    private DeviceConfigurationFunctions mFunctions;
    private boolean set = false ;
    /**
     * @param event
     */
    @Subscribe
    public void onEvent(IntegStoveStatusChangedEvent event) {
        if (mIntegratedStove.getID().equals(event.pojo.getID())) {
            mIntegratedStove = (AbsIntegratedStove) event.pojo;
            if (!set) {
                mChkIsInternalDays.setChecked(mIntegratedStove.fan_stove_linkage == 1);
            }else {
                set = false ;
            }
        }
    }


    /**
     * 设置烟灶联动
     */
    private void setSmartParams1() {
        if (mIntegratedStove == null) {
            return;
        }
        if (!mIntegratedStove.isConnected()){
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        set = true ;

       short linkage  = (short) (mIntegratedStove.fan_stove_linkage == 1 ? 0 : 1);
        mIntegratedStove.setSteamOvenSmartConfig(linkage, (short) 5, new VoidCallback() {
            @Override
            public void onSuccess() {
//                set = true ;
                mIntegratedStove.fan_stove_linkage = linkage ;
                mChkIsInternalDays.setChecked(mIntegratedStove.fan_stove_linkage == 1);
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    /**
     * 恢复默认
     */
    private void setSmartParams2() {
        if (mIntegratedStove == null) {
            return;
        }
        if (!mIntegratedStove.isConnected()){
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        set = true ;

        short linkage  = (short) 1;
        mIntegratedStove.setSteamOvenSmartConfig(linkage, (short) 5, new VoidCallback() {
            @Override
            public void onSuccess() {
                mIntegratedStove.fan_stove_linkage = linkage ;
                mChkIsInternalDays.setChecked(mIntegratedStove.fan_stove_linkage == 1);
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mDeviceConfigurationFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mIntegratedStove = bd == null ? null : (AbsIntegratedStove) bd.getSerializable(PageArgumentKey.INTEGRATED_STOVE);
        View view = inflater.inflate(R.layout.page_integ_stove_linkage, container, false);
        ButterKnife.inject(this, view);
        initData(mDeviceConfigurationFunctions);
        mChkIsInternalDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSmartParams1();
            }
        });
        mChkIsInternalDays.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setSmartParams1();
                return true;
            }
        });
//        mChkIsInternalDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setSmartParams1();
//            }
//        });
        mTvRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MessageDialog.Builder(getActivity())
                        // 标题可以不用填写
                        .setTitle("恢复出厂设置")
                        // 内容必须要填写
                        .setMessage("确认恢复默认设置？")
                        // 确定按钮文本
                        .setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        .setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                setSmartParams2();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {

                            }
                        })
                        .show();

            }
        });
        return view;
    }


    //数据
    private void initData(List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {
        for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
            if (RikaModeName.SMOKE_COOKER_STEAMING_LINKAGE.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                mFunctions = deviceConfigurationFunctions.get(i);
                mTvDeviceModelName.setText(deviceConfigurationFunctions.get(i).functionName);
            }
            if (RikaModeName.SMOKE_COOKER_STEAMING_ROAST_LINKAGE.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                mFunctions = deviceConfigurationFunctions.get(i);
                mTvDeviceModelName.setText(deviceConfigurationFunctions.get(i).functionName);
            }
        }
        try {
            List<DeviceConfigurationFunctions> linkageList = mFunctions.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;

            for (int i = 0; i < linkageList.size(); i++) {
                if ("smokeStoveLinkageOnOff".equals(linkageList.get(i).functionCode)) {
                    mTvFanLinkageName.setText(linkageList.get(i).functionName);
                    String functionParams = linkageList.get(i).functionParams;
//                    RikaSteamLinkageParams params = DeviceJsonToBeanUtils.JsonToObject(functionParams, RikaSteamLinkageParams.class);
                    IntegratedStoveLinkageParams params = JsonUtils.json2Pojo(functionParams, IntegratedStoveLinkageParams.class);
                    mTvFanFolding.setText(params.param.title.value);
                    mTvFanLinkageDec.setText(params.param.desc.value);
                    mChkIsInternalDays.setChecked(mIntegratedStove.fan_stove_linkage == 1);
                } else if ("smokeSteamRoastLinkageOnOff".equals(linkageList.get(i).functionCode)) {
                    mTvOtherLinkageName.setText(linkageList.get(i).functionName);
                    String functionParams = linkageList.get(i).functionParams;
//                    RikaSteamLinkageParams params = DeviceJsonToBeanUtils.JsonToObject(functionParams, RikaSteamLinkageParams.class);
                    IntegratedStoveLinkageParams params = JsonUtils.json2Pojo(functionParams, IntegratedStoveLinkageParams.class);
                    mTvOtherFolding.setText(params.param.title.value);
                    mTvOtherLinkageDec.setText(params.param.desc.value);
//                    mChkFanSteamLink.setChecked(true);
//                    mChkFanSteamLink.setEnabled(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mIntegratedStove == null) {
            return;
        }
        if (mIntegratedStove.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), mIntegratedStove.getDt() + ":烟灶联动页", null);
        }
    }

    String function;

    //    //烟灶联动
//    @OnClick(R.id.chkIsInternalDays)
//    public void onMChkIsInternalDaysClicked() {
//        if (recovery_flag) {
//            return;
//        }
//
//        boolean checked = mChkIsInternalDays.isChecked();
//        if (checked) {
//            function = "关";
//        } else {
//            function = "开";
//        }
//
//        if (mIntegratedStove != null) {
//            ToolUtils.logEvent(mIntegratedStove.getDt(), mFunctions.functionName + ":" + function, "roki_设备");
//        }
//    }
    @OnClick(R.id.fan_pic_show_1)
    public void onShow1() {

        if (mFanLinkageDec.getVisibility() != View.VISIBLE) {
            mFanLinkageDec.setVisibility(View.VISIBLE);
            mFanPicShow1.setImageResource(R.drawable.ic_down);

        } else {
            mFanLinkageDec.setVisibility(View.GONE);
            mFanPicShow1.setImageResource(R.drawable.ic_right_24);
        }

    }

    @OnClick(R.id.iv_other_linkage_switch)
    public void onMIvOtherLinkageSwitchClicked() {

        if (mLlOtherLinkageDec.getVisibility() != View.VISIBLE) {
            mLlOtherLinkageDec.setVisibility(View.VISIBLE);
            mIvOtherLinkageSwitch.setImageResource(R.drawable.ic_down);

        } else {
            mLlOtherLinkageDec.setVisibility(View.GONE);
            mIvOtherLinkageSwitch.setImageResource(R.drawable.ic_right_24);
        }

    }

}