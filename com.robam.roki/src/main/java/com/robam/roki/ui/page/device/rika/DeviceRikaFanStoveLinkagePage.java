package com.robam.roki.ui.page.device.rika;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaSmartParams;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.RikaSteamLinkageParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/9.
 * 烟灶联动 烟蒸联动
 */

public class DeviceRikaFanStoveLinkagePage extends BasePage {


    AbsRika mRika;
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
    private boolean flagFanLinkage;
    private boolean flagOtherLinkage;
    private boolean recovery_flag;
    private DeviceConfigurationFunctions mFunctions;

    private void setSmartParams() {

        if (mRika == null) {
            return;
        }
        final RikaSmartParams sp = new RikaSmartParams();
        sp.fanAndStoveSwitchLinkage = mChkIsInternalDays.isChecked();
        sp.fanPowerSwitchLinkage = mChkIsInternalDays.isChecked();
        sp.fanTimeDelayShutdownSwitch = mChkIsInternalDays.isChecked();
        mRika.setSmartConfig(sp, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                recovery_flag = false;
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
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        View view = inflater.inflate(R.layout.page_rika_fan_stove_linkage, container, false);
        ButterKnife.inject(this, view);
        initData(mDeviceConfigurationFunctions);
        return view;
    }


    //数据
    private void initData(List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {
        for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
            if ("smokeCookerSteamingLinkage".equals(deviceConfigurationFunctions.get(i).functionCode)) {
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
                    RikaSteamLinkageParams params = JsonUtils.json2Pojo(functionParams, RikaSteamLinkageParams.class);
                    mTvFanFolding.setText(params.getParam().getTitle().getValue());
                    mTvFanLinkageDec.setText(params.getParam().getDec().getValue());
                } else if ("smokeSteamLinkageOnOff".equals(linkageList.get(i).functionCode)) {
                    mTvOtherLinkageName.setText(linkageList.get(i).functionName);
                    String functionParams = linkageList.get(i).functionParams;
//                    RikaSteamLinkageParams params = DeviceJsonToBeanUtils.JsonToObject(functionParams, RikaSteamLinkageParams.class);
                    RikaSteamLinkageParams params = JsonUtils.json2Pojo(functionParams, RikaSteamLinkageParams.class);
                    mTvOtherFolding.setText(params.getParam().getTitle().getValue());
                    mTvOtherLinkageDec.setText(params.getParam().getDec().getValue());

                } else if ("defaultSettings".equals(linkageList.get(i).functionCode)) {
                    mTvRecovery.setText(linkageList.get(i).functionName);
                }
            }


            boolean isEmpty = mRika == null;
            if (isEmpty) return;
            mRika.getSmartConfig(new Callback<RikaSmartParams>() {

                @Override
                public void onSuccess(RikaSmartParams rikaSmartParams) {
                    refresh(rikaSmartParams);
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.showThrowable(t);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh(RikaSmartParams rikaSmartParams) {

        if (rikaSmartParams == null || mChkIsInternalDays == null) return;
        mChkIsInternalDays.setChecked(rikaSmartParams.fanAndStoveSwitchLinkage);
//        mChkIsInternalDays.setChecked(rikaSmartParams.fanPowerSwitchLinkage);
//        mChkIsInternalDays.setChecked(rikaSmartParams.fanTimeDelayShutdownSwitch);
    }

    //恢复出厂
    private void recovery() {
        recovery_flag = true;
        refresh(new RikaSmartParams());
        setSmartParams();
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

    @OnClick(R.id.fan_pic_show_1)
    public void onMFanPicShow1Clicked() {
        if (flagFanLinkage) {
            mFanLinkageDec.setVisibility(View.VISIBLE);
            mFanPicShow1.setImageResource(R.mipmap.img_8230s_expand_shang);
            flagFanLinkage = false;
            return;
        }
        mFanLinkageDec.setVisibility(View.GONE);
        mFanPicShow1.setImageResource(R.mipmap.img_fan8230s_expand);
        flagFanLinkage = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRika==null) {
            return;
        }
    }

    String function;
    //烟灶联动
    @OnClick(R.id.chkIsInternalDays)
    public void onMChkIsInternalDaysClicked() {
        if (recovery_flag) return;

        boolean checked = mChkIsInternalDays.isChecked();
        if (checked) {
            function="关";
        } else {
            function="开";
        }

        if (mRika!=null) {
            ToolUtils.logEvent(mRika.getDt(), mFunctions.functionName+":"+function, "roki_设备");
        }
        setSmartParams();
    }

    @OnClick(R.id.iv_other_linkage_switch)
    public void onMIvOtherLinkageSwitchClicked() {

        if (flagOtherLinkage) {
            mLlOtherLinkageDec.setVisibility(View.VISIBLE);
            mIvOtherLinkageSwitch.setImageResource(R.mipmap.img_8230s_expand_shang);
            flagOtherLinkage = false;
            return;
        }
        mLlOtherLinkageDec.setVisibility(View.GONE);
        mIvOtherLinkageSwitch.setImageResource(R.mipmap.img_fan8230s_expand);
        flagOtherLinkage = true;
    }

    @OnClick(R.id.iv_fan_steam_link)
    public void onMIvFanSteamLinkClicked() {
        ToastUtils.showShort(R.string.not_set);
    }

    @OnClick(R.id.tv_recovery)
    public void onMTvRecoveryClicked() {

        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialog.setTitleText(R.string.title_leave_factory);
        dialog.setContentText(R.string.regain_leave_factory_setting);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                recovery();
            }
        });
    }
}