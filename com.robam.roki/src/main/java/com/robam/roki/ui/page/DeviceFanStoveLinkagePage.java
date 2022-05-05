package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Message;
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
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.bean.DeviceFanStoveLinkage;
import com.robam.roki.model.helper.HelperFanData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/5/9.
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

    private List<DeviceConfigurationFunctions> mDates;
    private String mFroneDesc;
    private String mAfterDesc;
    private IRokiDialog mRokiDialog;
    private final int MINUTE = 1;
    private List<String> mMinute;
    private boolean recovery_flag;
    private boolean flag = true;

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
                        setSmartParams();

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

    private void initDate(List<DeviceConfigurationFunctions> dates) {

        if (dates == null || dates.size() == 0) return;
        for (int i = 0; i < dates.size(); i++) {
            if ("smokeStoveLinkage".equals(dates.get(i).functionCode)) {
                String name = dates.get(i).functionName;
                String title = dates.get(i).subView.title;
                mTvDeviceModelName.setText(title);
                mTvFanName.setText(name);
                List<DeviceConfigurationFunctions> functions = dates.get(i).subView.subViewModelMap.
                        subViewModelMapSubView.deviceConfigurationFunctions;
                if (functions != null && functions.size() != 0) {
                    for (int j = 0; j < functions.size(); j++) {
                        String functionParams = functions.get(j).functionParams;
                        LogUtils.i("20180509", "functionParams:" + functionParams);
//                        DeviceFanStoveLinkage fanStoveLinkage = DeviceJsonToBeanUtils.JsonToObject(functionParams, DeviceFanStoveLinkage.class);
                        try {
                            DeviceFanStoveLinkage fanStoveLinkage = JsonUtils.json2Pojo(functionParams, DeviceFanStoveLinkage.class);
                            String tips = fanStoveLinkage.getParam().getTips().getValue();
                            mTvFanFolding.setText(tips);
                            String value = fanStoveLinkage.getParam().getDesc().getValue();
                            subString(value);
                            List<Integer> list = fanStoveLinkage.getParam().getMinute().getValue();
                            mMinute = HelperFanData.getTimeMinute(list);
                            String initMinute = fanStoveLinkage.getParam().getDefaultMinute().getValue();
                            mTvMinute.setText(initMinute);

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
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                refresh(smartParams);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
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

    void setSmartParams() {
        if (fan == null) {
            return;
        }
        final SmartParams sp = new SmartParams();
        sp.IsPowerLinkage = mChkIsInternalDays.isChecked();
        sp.IsLevelLinkage = mChkIsInternalDays.isChecked();
        sp.IsShutdownLinkage = mChkIsInternalDays.isChecked();
        sp.ShutdownDelay = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(mTvMinute.getText().toString()));

        fan.setSmartConfig(sp, new VoidCallback() {

            @Override
            public void onSuccess() {
                setOnOffStatusForText(sp.IsShutdownLinkage);
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                recovery_flag = false;
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }

    //刷新
    private void refresh(SmartParams smartParams) {

        if (smartParams == null) return;
        if (mChkIsInternalDays == null) return;
        mChkIsInternalDays.setChecked(smartParams.IsPowerLinkage);
        mChkIsInternalDays.setChecked(smartParams.IsLevelLinkage);
        mChkIsInternalDays.setChecked(smartParams.IsShutdownLinkage);
        mTvMinute.setText(String.valueOf(smartParams.ShutdownDelay));
        setOnOffStatusForText(smartParams.IsShutdownLinkage);
    }

    //恢复出厂
    private void recovery() {
        recovery_flag = true;
        refresh(new SmartParams());
        setSmartParams();
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
        setSmartParams();
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

            mRokiDialog.setWheelViewData(null, mMinute, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
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
