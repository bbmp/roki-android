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
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.DeviceFanVentilation;
import com.robam.roki.model.helper.HelperFanData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/5/15.
 * 通风换气页面
 */

public class DeviceFanVentilationPage extends BasePage {

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
    @InjectView(R.id.fan_pic_show_1)
    ImageView mFanPicShow1;
    @InjectView(R.id.tv_day)
    TextView mTvDay;
    @InjectView(R.id.tv_day_dec)
    TextView mTvDayDec;
    @InjectView(R.id.tv_dec_front)
    TextView mTvDecFront;
    @InjectView(R.id.tv_week)
    TextView mTvWeek;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.tv_dec_after)
    TextView mTvDecAfter;
    @InjectView(R.id.fan_linkage_dec)
    LinearLayout mFanLinkageDec;
    @InjectView(R.id.tv_recovery)
    TextView mTvRecovery;

    private List<DeviceConfigurationFunctions> mDates;
    private List<String> mHours;
    private List<String> mMinute;
    private List<String> mDays;
    private List<String> mWeek;
    private IRokiDialog mRokiDialog;
    private IRokiDialog mSheClockTimeDialog = null;
    private boolean flag = true;
    final List<Integer> hourList = new ArrayList<>();
    final List<Integer> minList = new ArrayList<>();
    private String mFroneDesc;
    private String mCenterDesc;
    private String mAfterDesc;


    private final int WEEK = 1;
    private final int DAYS = 2;
    private final int TIME = 3;
    private boolean recovery_flag;

    MyHandler mHandler = new MyHandler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DAYS:
                    setDay((String) msg.obj);
                    break;
                case WEEK:
                    setWeek((String) msg.obj);
                    break;
                case TIME:
                    setTime((String) msg.obj);
                    break;
                default:
                    break;
            }

        }
    };
    private String initDay;
    private String initWeek;
    private String hours;
    private String minute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fan_ventilation, container, false);
        LogUtils.i("20190731","onCreateView");
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
        if (fan == null) {
            return;
        }
    }

    private void initDate(List<DeviceConfigurationFunctions> dates) {

        if (dates == null || dates.size() == 0) return;
        for (int i = 0; i < dates.size(); i++) {
            if ("ventilation".equals(dates.get(i).functionCode)) {
                String name = dates.get(i).functionName;
                String title = dates.get(i).subView.title;
                mTvDeviceModelName.setText(title);
                mTvFanName.setText(name);
                List<DeviceConfigurationFunctions> functions = dates.get(i).subView.subViewModelMap.
                        subViewModelMapSubView.deviceConfigurationFunctions;
                if (functions != null && functions.size() != 0) {
                    for (int j = 0; j < functions.size(); j++) {
                        String functionParams = functions.get(j).functionParams;
                        LogUtils.i("20190613", " functionParams:" + functionParams);
//                        DeviceFanVentilation fanVentilation = DeviceJsonToBeanUtils.JsonToObject(functionParams, DeviceFanVentilation.class);
                        try {
                            DeviceFanVentilation fanVentilation = JsonUtils.json2Pojo(functionParams, DeviceFanVentilation.class);
                            mTvFanFolding.setText(fanVentilation.getParam().getTitle().getValue());
                            List<Integer> daySection = fanVentilation.getParam().getDay().getValue();
                            //天
                            mDays = HelperFanData.getListDay(daySection);
                            initDay = fanVentilation.getParam().getDefaultDay().getValue();
                            mTvDay.setText(initDay);
                            //周
                            mWeek = fanVentilation.getParam().getWeek().getValue();
                            initWeek = fanVentilation.getParam().getDefaultWeek().getValue();
                            mTvWeek.setText(initWeek);
                            List<Integer> hourSection = fanVentilation.getParam().getHour().getValue();
                            List<Integer> minuteSection = fanVentilation.getParam().getMinute().getValue();
                            //小时
                            mHours = HelperFanData.getTimeHour(hourSection);
                            //分钟
                            mMinute = HelperFanData.getTimeMinute(minuteSection);
                            hours = fanVentilation.getParam().getDefaultHour().getValue();
                            minute = fanVentilation.getParam().getDefaultMinute().getValue();
                            mTvTime.setText(hours + ":" + minute);
                            String value = fanVentilation.getParam().getDesc().getValue();
                            subString(value);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        mTvDayDec.setText(mFroneDesc);
        mTvDecFront.setText(mCenterDesc);
        mTvDecAfter.setText(mAfterDesc);
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
            if (1 == i) {
                mFroneDesc = strings[i];
            } else if (2 == i) {
                mCenterDesc = strings[i];
            } else if (3 == i) {
                mAfterDesc = strings[i];
            }
        }

    }

    private void setDay(String data) {
        if (data.contains(cx.getString(R.string.fan_day))) {
            String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
            mTvDay.setTag(DAYS);
            canAndOkBtnListener(mTvDay, removeString);
        }
    }


    private void setWeek(String data) {
        mTvWeek.setTag(WEEK);
        canAndOkBtnListener(mTvWeek, data);
    }


    private void setTime(String data) {
        if (data.contains(StringConstantsUtil.STRING_MIN)) {
            String removetMinString = RemoveManOrsymbolUtil.getRemoveString(data);
            minList.add(Integer.parseInt(removetMinString));
        }
        if (data.contains(StringConstantsUtil.STR_HOUR)) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(data);
            hourList.add(Integer.parseInt(removeTimeString));
        }
        mSheClockTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSheClockTimeDialog != null && mSheClockTimeDialog.isShow()) {
                    mSheClockTimeDialog.dismiss();
                    setTimeFusion(hourList.get(hourList.size() - 1), minList.get(minList.size() - 1));
                    setSmartParams();
                }
            }
        });

        mSheClockTimeDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    //时间融合
    void setTimeFusion(int hour, int minute) {
        mTvTime.setText(String.format("%02d:%02d", hour, minute));
        mTvTime.setTag(R.id.tag_weekly_ventilation_date_hour, hour);
        mTvTime.setTag(R.id.tag_weekly_ventilation_date_minute, minute);
    }

    private void canAndOkBtnListener(final TextView view, final String content) {
        mRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRokiDialog != null && mRokiDialog.isShow()) {
                    mRokiDialog.dismiss();
                    int tag = (int) view.getTag();
                    switch (tag) {
                        case WEEK:
                            mTvWeek.setText(content);
                            setSmartParams();

                            break;
                        case DAYS:
                            mTvDay.setText(content);
                            setSmartParams();

                            break;
                        case TIME:
                            mTvTime.setText(content);
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

    void setSmartParams() {
        if (fan == null) {
            return;
        }
        final SmartParams sp = new SmartParams();
        sp.IsTimingVentilation = mChkIsInternalDays.isChecked();
        sp.IsWeeklyVentilation = mChkIsInternalDays.isChecked();
        sp.TimingVentilationPeriod = Short.parseShort(RemoveManOrsymbolUtil.getRemoveString(mTvDay.getText().toString()));
        String week = mTvWeek.getText().toString();
        short weekValue = HelperFanData.getWeekValue(week);
        sp.WeeklyVentilationDate_Week = weekValue;
        sp.WeeklyVentilationDate_Hour = Short.parseShort(mTvTime.getTag(R.id.tag_weekly_ventilation_date_hour).toString());
        sp.WeeklyVentilationDate_Minute = Short.parseShort(mTvTime.getTag(R.id.tag_weekly_ventilation_date_minute).toString());

        fan.setSmartConfig(sp, new VoidCallback() {

            @Override
            public void onSuccess() {
                boolean isTimingVentilation = sp.IsTimingVentilation;

                if (fan!=null) {
                    if (isTimingVentilation) {
                        ToolUtils.logEvent(fan.getDt(), "通风换气开关:开" + sp.TimingVentilationPeriod + ":" + sp.WeeklyVentilationDate_Week + ":" + sp.WeeklyVentilationDate_Hour + ":" + sp.WeeklyVentilationDate_Minute, "roki_设备");
                    } else {
                        ToolUtils.logEvent(fan.getDt(), "通风换气开关:关" + sp.TimingVentilationPeriod + ":" + sp.WeeklyVentilationDate_Week + ":" + sp.WeeklyVentilationDate_Hour + ":" + sp.WeeklyVentilationDate_Minute, "roki_设备");
                    }
                }

                setOnOffStatusForText(sp.IsPowerLinkage, sp.IsTimingVentilation);
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                recovery_flag = false;
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }

    //恢复出厂
    private void recovery() {
        recovery_flag = true;
        refresh(new SmartParams());
        setSmartParams();
    }

    //出厂刷新
    private void refresh(SmartParams smartParams) {

        if (smartParams == null) return;
        mChkIsInternalDays.setChecked(smartParams.IsTimingVentilation);
        mChkIsInternalDays.setChecked(smartParams.IsWeeklyVentilation);
        mTvDay.setText(String.valueOf(smartParams.TimingVentilationPeriod));
        if (smartParams.WeeklyVentilationDate_Week >= 1 && smartParams.WeeklyVentilationDate_Week <= 7) {
            if (mTvWeek != null) {
                LogUtils.i("20190613", "WeeklyVentilationDate_Week:" + smartParams.WeeklyVentilationDate_Week);
                mTvWeek.setText(mWeek.get(smartParams.WeeklyVentilationDate_Week - 1));
            }
        }
        setTimeFusion(smartParams.WeeklyVentilationDate_Hour,
                smartParams.WeeklyVentilationDate_Minute);
        setOnOffStatusForText(smartParams.IsPowerLinkage, smartParams.IsTimingVentilation);
    }

    private void setOnOffStatusForText(boolean ld, boolean tf) {
        if (mTvDay != null) {
            mTvDay.setTextColor(tf ? r.getColor(R.color.c11) : r.getColor(R.color.c03));
        }
        if (mTvWeek != null) {
            mTvWeek.setTextColor(tf ? r.getColor(R.color.c11) : r.getColor(R.color.c03));
        }
        if (mTvTime != null) {
            mTvTime.setTextColor(tf ? r.getColor(R.color.c11) : r.getColor(R.color.c03));
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

    //通风换气开关
    @OnClick(R.id.chkIsInternalDays)
    public void onMChkIsInternalDaysClicked() {
        if (recovery_flag) return;
        SmartParams smartParams = new SmartParams();
        setTimeFusion(smartParams.WeeklyVentilationDate_Hour,
                smartParams.WeeklyVentilationDate_Minute);
        setSmartParams();
    }

    @OnClick(R.id.fan_pic_show_1)
    public void onMFanPicShow1Clicked() {

        if (flag) {
            SmartParams smartParams = new SmartParams();
            setTimeFusion(smartParams.WeeklyVentilationDate_Hour,
                    smartParams.WeeklyVentilationDate_Minute);
            mFanPicShow1.setImageResource(R.mipmap.img_8230s_expand_shang);
            mFanLinkageDec.setVisibility(View.GONE);
            flag = false;
            return;
        }

        flag = true;
        mFanPicShow1.setImageResource(R.mipmap.img_fan8230s_expand);
        mFanLinkageDec.setVisibility(View.VISIBLE);

    }

    //天
    @OnClick(R.id.tv_day)
    public void onMTvDayClicked() {
        if (mDays == null || mDates.size() == 0) return;
        if (!mChkIsInternalDays.isChecked()) return;
        if (mRokiDialog != null) {
            mRokiDialog = null;
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        if (mRokiDialog != null && !mRokiDialog.isShow()) {

            mRokiDialog.setWheelViewData(null, mDays, null, false, 0, 2, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {

                    Message msg = mHandler.obtainMessage();
                    msg.what = DAYS;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);

            mRokiDialog.show();


        }

    }

    //周
    @OnClick(R.id.tv_week)
    public void onMTvWeekClicked() {
        if (mWeek == null || mWeek.size() == 0) return;
        if (!mChkIsInternalDays.isChecked()) return;
        if (mRokiDialog != null) {
            mRokiDialog = null;
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        if (mRokiDialog != null && !mRokiDialog.isShow()) {

            mRokiDialog.setWheelViewData(null, mWeek, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = WEEK;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);

            mRokiDialog.show();


        }

    }

    //时间
    @OnClick(R.id.tv_time)
    public void onMTvTimeClicked() {
        if (mHours == null && mMinute == null) return;
        if (!mChkIsInternalDays.isChecked()) return;
        mSheClockTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        mSheClockTimeDialog.setWheelViewData(mHours, null, mMinute, false, 12, 0, 30,
                new OnItemSelectedListenerFrone() {
                    @Override
                    public void onItemSelectedFront(String contentFront) {
                        Message message = mHandler.obtainMessage();
                        message.what = TIME;
                        message.obj = contentFront;
                        mHandler.sendMessage(message);
                    }
                }, null, new OnItemSelectedListenerRear() {
                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        Message message = mHandler.obtainMessage();
                        message.what = TIME;
                        message.obj = contentRear;
                        mHandler.sendMessage(message);
                    }
                });

        mSheClockTimeDialog.show();

    }

    //恢复出厂点击
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

