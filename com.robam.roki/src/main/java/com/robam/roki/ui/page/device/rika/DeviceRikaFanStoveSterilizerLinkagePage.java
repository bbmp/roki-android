package com.robam.roki.ui.page.device.rika;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaSmartParams;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.RikaSteamLinkageParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/6/1.
 */
public class DeviceRikaFanStoveSterilizerLinkagePage extends BasePage {

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
//    @InjectView(R.id.iv_other_linkage_switch)
//    ImageView mIvOtherLinkageSwitch;
    @InjectView(R.id.tv_recovery)
    TextView mTvRecovery;
    @InjectView(R.id.tv_fan_linkage_name)
    TextView mTvFanLinkageName;
//    @InjectView(R.id.tv_other_linkage_name)
//    TextView mTvOtherLinkageName;
    @InjectView(R.id.tv_fan_folding)
    TextView mTvFanFolding;
    @InjectView(R.id.tv_fan_linkage_dec)
    TextView mTvFanLinkageDec;
//    @InjectView(R.id.tv_other_folding)
//    TextView mTvOtherFolding;
//    @InjectView(R.id.iv_sterilizer_switch)
//    ImageView mIvSterilizerSwitch;
//    @InjectView(R.id.tv_sterilizer_fan_linkage_dec)
//    TextView mTvSterilizerFanLinkageDec;
//    @InjectView(R.id.fan_sterilizer_linkage_dec)
//    LinearLayout mFanSterilizerLinkageDec;
    private boolean flagFanLinkage;
    private boolean flagOtherLinkage;
    private List<String> mMinute;
    private List<String> mStreilizerMinute;
    private IRokiDialog mFanLinkingDialog;
    private IRokiDialog mFanSterilizerLinkingDialog;
    private final int MINUTE = 1;
    private final int STERILIZER_MINUTE = 2;
    private boolean recovery_flag;
    private DeviceConfigurationFunctions mFunctions;
    private String mFrontDec;
    private String mAfterDec;
    private String mSterilizerFrontDec;
    private String mSterilizerAfterDec;
    MyHandler mHandler = new MyHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case MINUTE:
                    setFanLinkageTime((String) msg.obj);
                    break;

                case STERILIZER_MINUTE:
                    setFanSterilizerLinkageTime((String) msg.obj);
                    break;

            }
        }
    };

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {

        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID())) return;

//        if (0 == mRika.sterilDoorLockStatus && mRika.sterilWorkStatus != RikaStatus.STERIL_ALARM) {
//            IRokiDialog rokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
//            rokiDialog.setContentText(R.string.device_alarm_rika_E1_content);
//            rokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
//            rokiDialog.show();
//        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mDeviceConfigurationFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.page_rika_fan_stove_sterilizer_linkage, container, false);
        ButterKnife.inject(this, view);
        initData(mDeviceConfigurationFunctions);
        return view;
    }

    //数据

    private void initData(List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {
        for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
            if ("smokeStoveEliminationLinkage".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                mFunctions = deviceConfigurationFunctions.get(i);
                LogUtils.i("20180607", "mFunctions：" + mFunctions.functionName);
            }
        }
        try {
            List<DeviceConfigurationFunctions> linkageList = mFunctions
                    .subView
                    .subViewModelMap
                    .subViewModelMapSubView
                    .deviceConfigurationFunctions;
            for (int i = 0; i < linkageList.size(); i++) {
                if ("smokeStoveLinkageOnOff".equals(linkageList.get(i).functionCode)) {
                    mTvFanLinkageName.setText(linkageList.get(i).functionName);
                    String functionParams = linkageList.get(i).functionParams;
//                    RikaSteamLinkageParams params = DeviceJsonToBeanUtils.JsonToObject(functionParams, RikaSteamLinkageParams.class);
                    RikaSteamLinkageParams params = JsonUtils.json2Pojo(functionParams, RikaSteamLinkageParams.class);
                    mTvFanFolding.setText(params.getParam().getTitle().getValue());
                    mTvFanLinkageDec.setText(params.getParam().getDec().getValue());

                } else if ("defaultSettings".equals(linkageList.get(i).functionCode)) {
                    mTvRecovery.setText(linkageList.get(i).functionName);
                } else if ("smokeEliminationLinkage".equals(linkageList.get(i).functionCode)) {
//                    mTvOtherLinkageName.setText(linkageList.get(i).functionName);
//                    String functionParams = linkageList.get(i).functionParams;
//                    RikaSteamLinkageParams params = DeviceJsonToBeanUtils.JsonToObject(functionParams, RikaSteamLinkageParams.class);
//                    RikaSteamLinkageParams params = JsonUtils.json2Pojo(functionParams, RikaSteamLinkageParams.class);
//                    mTvOtherFolding.setText(params.getParam().getTitle().getValue());
//                    mTvSterilizerFanLinkageDec.setText(params.getParam().getDec().getValue());

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


    //弹框选择的数据处理
    private void setFanLinkageTime(String minute) {
        LogUtils.i("20180514", "minute:" + minute);
        if (minute.contains(cx.getString(R.string.dialog_minutes_text))) {
            final String min = RemoveManOrsymbolUtil.getRemoveString(minute);
            mFanLinkingDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFanLinkingDialog.dismiss();
//                    mTvFanValue.setText(min);
                    setSmartParams();
                }
            });
        }
    }

    private void setFanSterilizerLinkageTime(String minute) {
        if (minute.contains(cx.getString(R.string.dialog_minutes_text))) {
            final String min = RemoveManOrsymbolUtil.getRemoveString(minute);
            mFanLinkingDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFanSterilizerLinkingDialog.dismiss();
//                    mTvSterilizerFanValue.setText(min);
                    setSmartParams();
                }
            });
        }
    }

    private void setSmartParams() {

        if (mRika == null) {
            return;
        }
        final RikaSmartParams sp = new RikaSmartParams();
        sp.fanAndStoveSwitchLinkage = mChkIsInternalDays.isChecked();
        sp.fanPowerSwitchLinkage = mChkIsInternalDays.isChecked();
        sp.fanTimeDelayShutdownSwitch = mChkIsInternalDays.isChecked();

//        sp.fanDelaySwitchTime = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(mTvFanValue.getText().toString()));
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

    private void stoveSubString(String value) {

        String[] strings = value.split("button");
        for (int i = 0; i < strings.length; i++) {
            if (0 == i) {
                mFrontDec = strings[i];
            } else if (1 == i) {
                mAfterDec = strings[i];
            }
        }
    }

    private void sterilizerSubString(String value) {
        String[] strings = value.split("button");
        for (int i = 0; i < strings.length; i++) {
            if (0 == i) {
                mSterilizerFrontDec = strings[i];
            } else if (1 == i) {
                mSterilizerAfterDec = strings[i];
            }
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


    @OnClick(R.id.chkIsInternalDays)
    public void onMChkIsInternalDaysClicked() {
        if (recovery_flag) return;
        setSmartParams();
    }

//    @OnClick(R.id.iv_other_linkage_switch)
//    public void onMIvOtherLinkageSwitchClicked() {
//
//        if (flagOtherLinkage) {
//            mFanSterilizerLinkageDec.setVisibility(View.VISIBLE);
//            mIvOtherLinkageSwitch.setImageResource(R.mipmap.img_8230s_expand_shang);
//            flagOtherLinkage = false;
//            return;
//        }
//        mFanSterilizerLinkageDec.setVisibility(View.GONE);
//        mIvOtherLinkageSwitch.setImageResource(R.mipmap.img_fan8230s_expand);
//        flagOtherLinkage = true;
//    }

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

//    @OnClick(R.id.iv_sterilizer_switch)
//    public void onMIvSterilizerSwitchClicked() {
//        ToastUtils.showShort(R.string.not_set);
//    }


}