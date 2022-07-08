package com.robam.roki.ui.page.device.fan;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.bean.DeviceFanStoveLinkage;
import com.robam.roki.model.helper.HelperFanData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.adapter.FanBackgroundFuncAdapter.isNewProtocol;

/**
 * Created by 14807 on 2018/5/9.
 * 烟灶联动
 */

public class DeviceFanStoveLinkagePage extends BasePage {

    AbsFan fan;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.tv_fan_name)
    TextView mTvFanName;
    @InjectView(R.id.chkIsInternalDays)
    CheckBoxView mChkIsInternalDays;
    @InjectView(R.id.tv_fan_folding)
    TextView mTvFanFolding;
    @InjectView(R.id.fan_pic_show)
    ImageView mFanPicShow;
    @InjectView(R.id.tv_front_desc)
    TextView mTvFrontDesc;
    @InjectView(R.id.tv_minute)
    TextView mTvMinute;
    @InjectView(R.id.tv_after_desc)
    TextView mTvAfterDesc;
    @InjectView(R.id.fan_linkage_dec)
    LinearLayout mFanLinkageDec;
    @InjectView(R.id.tv_recovery)
    TextView mTvRecovery;
    @InjectView(R.id.tv_desc)
    TextView mTvDesc;
    private FanStatusComposite mFanStatusComposite = new FanStatusComposite();
    private List<DeviceConfigurationFunctions> mDates;
    private String mFroneDesc;
    private String mAfterDesc;
    private IRokiDialog mRokiDialog;
    private final int MINUTE = 1;
    private List<String> mMinute;
    private boolean recovery_flag;
    private boolean flag = true;
    SmartParams sp = new SmartParams();
    MyHandler mHandler = new MyHandler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MINUTE:
                    setMinute((String) msg.obj);
                    break;
            }
        }
    };

    private void setMinute(final String minute) {

        if (minute.contains(cx.getString(R.string.dialog_minutes_text))) {
            final String min = RemoveManOrsymbolUtil.getRemoveString(minute);
            mRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRokiDialog != null && mRokiDialog.isShow()) {
                        mRokiDialog.dismiss();
                        mTvMinute.setText(min);
                        if (isNewProtocol) {
                            setMinParams();
                        } else {
                            setSmartParams();
                        }
                    }
                }
            });
            mRokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_stove_linkage, container, false);
        Bundle bd = getArguments();
        mDates = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        ButterKnife.inject(this, view);
        initDate(mDates);
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        if (fan==null) {
            return;
        }
        if (fan.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), fan.getDt() + ":烟灶联动页", null);
        }
    }

    private void initDate(List<DeviceConfigurationFunctions> dates) {

        if (dates == null || dates.size() == 0) return;
        for (int i = 0; i < dates.size(); i++) {
            if ("smokeStoveLinkage".equals(dates.get(i).functionCode)) {
                String title = dates.get(i).functionName;
                mTvDeviceModelName.setText(title);
                mTvFanName.setText(title);
                List<DeviceConfigurationFunctions> functions = dates.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                if (functions != null && functions.size() != 0) {
                    for (int j = 0; j < functions.size(); j++) {
                        String functionParams = functions.get(j).functionParams;
                        try {
                            DeviceFanStoveLinkage fanStoveLinkage = JsonUtils.json2Pojo(functionParams, DeviceFanStoveLinkage.class);
                            String tips = fanStoveLinkage.getParam().getTips().getValue();
                            mTvFanFolding.setText(tips);
                            String value = fanStoveLinkage.getParam().getDesc().getValue();
                            subString(value);

                            List<Integer> list = fanStoveLinkage.getParam().getMinute().getValue();
                            mMinute = HelperFanData.getTimeMinute(list);
                            mTvMinute.setText(mMinute.get(5));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        mTvFrontDesc.setText(mFroneDesc);
        mTvAfterDesc.setText(mAfterDesc.substring(0, 11));
        mTvDesc.setText(mAfterDesc.substring(11));

        boolean isEmpty = fan == null;
        if (isEmpty) return;
        redSmartConfig();
    }

    private void redSmartConfig() {
//        ProgressDialogHelper.setRunning(cx, true);
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                sp = smartParams;
                mFanStatusComposite.IsTimingVentilation = (short) (smartParams.IsTimingVentilation ? 1 : 0);
                mFanStatusComposite.IsNoticClean = (short) (smartParams.IsNoticClean ? 1 : 0);
                mFanStatusComposite.TimingVentilationPeriod = smartParams.TimingVentilationPeriod;
                mFanStatusComposite.IsPowerLinkage = (short) (smartParams.IsPowerLinkage ? 1 : 0);
                mFanStatusComposite.IsLevelLinkage = (short) (smartParams.IsLevelLinkage ? 1 : 0);
                mFanStatusComposite.IsShutdownLinkage = (short) (smartParams.IsShutdownLinkage ? 1 : 0);
                mFanStatusComposite.ShutdownDelay = smartParams.ShutdownDelay;
                //mFanStatusComposite.TimingVentilationPeriod = smartParams.TimingVentilationPeriod;
                mFanStatusComposite.IsWeeklyVentilation = (short) (smartParams.IsWeeklyVentilation ? 1 : 0);
                mFanStatusComposite.WeeklyVentilationDate_Week = smartParams.WeeklyVentilationDate_Week;
                mFanStatusComposite.WeeklyVentilationDate_Hour = smartParams.WeeklyVentilationDate_Hour;
                mFanStatusComposite.WeeklyVentilationDate_Minute = smartParams.WeeklyVentilationDate_Minute;
                refresh(mFanStatusComposite);
                ProgressDialogHelper.setRunning(cx, false);

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    private void subString(String value) {
        String[] strings = value.split("button");
        for (int i = 0; i < strings.length; i++) {
            if (0 == i) {
                mFroneDesc = strings[i];
            } else if (1 == i) {
                mAfterDesc = strings[i];
            }
        }
    }
    private void setMinParams() {
        if (fan == null) {
            return;
        }
        mFanStatusComposite.IsPowerLinkage = (short) (mChkIsInternalDays.isChecked() ? 1 : 0);
        mFanStatusComposite.IsLevelLinkage = (short) (mChkIsInternalDays.isChecked() ? 1 : 0);
        mFanStatusComposite.IsShutdownLinkage = (short) (mChkIsInternalDays.isChecked() ? 1 : 0);
        if (!TextUtils.isEmpty(mTvMinute.getText().toString())){
            mFanStatusComposite.ShutdownDelay = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(mTvMinute.getText().toString()));
        }
        List<Integer> listKey = new ArrayList<>();
        listKey.add(1);
        listKey.add(2);
        listKey.add(3);
        listKey.add(4);
        ProgressDialogHelper.setRunning(cx, true);
        fan.setFanCombo(mFanStatusComposite, (short) 4, listKey, new VoidCallback() {
            @Override
            public void onSuccess() {
//                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                if (mFanStatusComposite.IsShutdownLinkage == 1) {
                    setOnOffStatusForText(true);
                } else {
                    setOnOffStatusForText(false);
                }
                recovery_flag = false;
                redSmartConfig();
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    void setSmartParams() {
        LogUtils.i("20190731", "setSmartParams:" + isNewProtocol);
        if (fan == null) {
            return;
        }
        sp.IsPowerLinkage = mChkIsInternalDays.isChecked();
        sp.IsLevelLinkage = mChkIsInternalDays.isChecked();
        sp.IsShutdownLinkage = mChkIsInternalDays.isChecked();
        if (mTvMinute.getText()!= null
                && mTvMinute.getText().toString() != null
                && mTvMinute.getText().toString().length() !=0){
            sp.ShutdownDelay = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(mTvMinute.getText().toString()));
        }
        fan.setSmartConfig(sp, new VoidCallback() {

            @Override
            public void onSuccess() {
                setOnOffStatusForText(sp.IsShutdownLinkage);
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                recovery_flag = false;
                redSmartConfig();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }

    //刷新
    private void refresh(FanStatusComposite fanStatusComposite) {

        if (fanStatusComposite == null) return;
        if (mChkIsInternalDays != null){
            if (fanStatusComposite.IsPowerLinkage == 1) {
                mChkIsInternalDays.setChecked(true);
            } else {
                mChkIsInternalDays.setChecked(false);
            }
            if (fanStatusComposite.IsLevelLinkage == 1) {
                mChkIsInternalDays.setChecked(true);
            } else {
                mChkIsInternalDays.setChecked(false);
            }
            if (fanStatusComposite.IsShutdownLinkage == 1) {
                mChkIsInternalDays.setChecked(true);
            } else {
                mChkIsInternalDays.setChecked(false);
            }
        }

        if (fanStatusComposite.IsShutdownLinkage == 1) {
            setOnOffStatusForText(true);
        } else {
            setOnOffStatusForText(false);
        }
        mTvMinute.setText(String.valueOf(fanStatusComposite.ShutdownDelay));

    }

    //恢复出厂
    private void recovery() {
        recovery_flag = true;
        refresh(new FanStatusComposite());
        recoveryP();

    }

    private void recoveryP() {
        if (fan == null) {
            return;
        }
        fan.setSmartConfig(new SmartParams(), new VoidCallback() {
            @Override
            public void onSuccess() {
                setOnOffStatusForText(sp.IsShutdownLinkage);
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                recovery_flag = false;
                redSmartConfig();
            }
            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    private void setOnOffStatusForText(boolean tf) {
        mTvMinute.setTextColor(tf ? r.getColor(R.color.c11) : r.getColor(R.color.c03));
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

    @OnClick(R.id.chkIsInternalDays)
    public void onMChkIsInternalDaysClicked() {
        if (recovery_flag) return;
        if (isNewProtocol){
            setMinParams();
        }else {
            setSmartParams();
        }
    }

    @OnClick(R.id.fan_pic_show)
    public void onMFanPicShowClicked() {
        if (flag) {
            mFanPicShow.setImageResource(R.mipmap.img_8230s_expand_shang);
            mFanLinkageDec.setVisibility(View.GONE);
            flag = false;
            return;
        }
        flag = true;
        mFanPicShow.setImageResource(R.mipmap.img_fan8230s_expand);
        mFanLinkageDec.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_minute)
    public void onMTvMinuteClicked() {

        if (mMinute == null || mMinute.size() == 0) return;
        if (!mChkIsInternalDays.isChecked()) return;

        if (mRokiDialog != null) {
            mRokiDialog = null;
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        if (mRokiDialog != null && !mRokiDialog.isShow()) {

            mRokiDialog.setWheelViewData(null, mMinute, null, false, 0, 5, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MINUTE;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);

            mRokiDialog.show();

        }

    }

    @OnClick(R.id.tv_recovery)
    public void onViewClicked() {

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
