package com.robam.roki.ui.page.device.fan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.adapter.FanBackgroundFuncAdapter.isNewProtocol;

/**
 * Created by RuanWei on 2019/9/27.
 * 假日模式 5916s
 */
public class DeviceFanVentilation2Page extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;

    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;

    @InjectView(R.id.tv_day)
    TextView tvDay;

    @InjectView(R.id.tv_week)
    TextView tvWeek;

    @InjectView(R.id.tv_time)
    TextView tvTime;

    @InjectView(R.id.tv_auto_air)
    TextView tvAutoAir;

    @InjectView(R.id.cb_auto_air)
    CheckBoxView cbAutoAir;

    @InjectView(R.id.tv_timing_air)
    TextView tvTimingAir;

    @InjectView(R.id.cb_timing_ar)
    CheckBoxView cbTimingAir;


    @InjectView(R.id.cb_quick_fry)
    CheckBoxView cbQuickFry;

    @InjectView(R.id.tv_timing_air_end)
    TextView tvTimingAirEnd;


    @InjectView(R.id.tv_recovery)
    TextView tvRecovery;


    @InjectView(R.id.tv_quick_fry)
    TextView tvQuickFry;

    @InjectView(R.id.tv_quick_fry_min)
    TextView tvQuickFryMin;

    @InjectView(R.id.tv_quick_fry_end)
    TextView tvQuickFryEnd;

    @InjectView(R.id.tv_timing_air_center)
    TextView tvTimingAirCenter;

    @InjectView(R.id.quick_fry_layout)
    LinearLayout quickFryLayout;

    AbsFan fan;
    private List<DeviceConfigurationFunctions> mDatas;

    private List<String> mHours;
    private List<String> mMinute;
    private List<String> mDays;
    private List<String> mWeek;
    private final int WEEK = 1;
    private final int DAYS = 2;
    private final int TIME = 3;
    private final int QUICK_FRY_TIME = 4;

    private IRokiDialog mRokiDialog;
    private IRokiDialog mSheClockTimeDialog = null;

    @SuppressLint("HandlerLeak")
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
                case QUICK_FRY_TIME:
                    setQuickFry((String) msg.obj);
                    break;
            }
        }
    };


    private String titles;
    private String tips;
    private String defaultWeek;
    private String defaultHour;
    private String defaultMin;
    private String defaultDay;
    private String protocolVersionParams;

    private boolean recovery_flag;
    SmartParams sp = new SmartParams();
    private FanStatusComposite mFanStatusComposite = new FanStatusComposite();
    private String defalutStriFriedMin;
    private List<String> mStriFriedMin;

    final List<Integer> hourList = new ArrayList<>();
    final List<Integer> minList = new ArrayList<>();

    private void setDay(String data) {
        if (data.contains(cx.getString(R.string.fan_day))) {
            String removeDayString = RemoveManOrsymbolUtil.getRemoveString(data);
            tvDay.setTag(DAYS);
            canAndOkBtnListener(tvDay, removeDayString);
        }

    }

    private void setQuickFry(String obj) {
        String removeDayString = RemoveManOrsymbolUtil.getRemoveString(obj);
        tvQuickFryMin.setTag(QUICK_FRY_TIME);
        canAndOkBtnListener(tvQuickFryMin, removeDayString);

    }

    private void setWeek(String data) {
        tvWeek.setTag(WEEK);
        canAndOkBtnListener(tvWeek, data);

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
                            tvWeek.setText(content);
                            if (isNewProtocol) {
                                setParams();
                            } else {
                                setSmartParams();
                            }
                            break;
                        case DAYS:
                            tvDay.setText(content);
                            if (isNewProtocol) {
                                setParams();
                            } else {
                                setSmartParams();
                            }
                            break;
                        case TIME:
                            tvTime.setText(content);
                            if (isNewProtocol) {
                                setParams();
                            } else {
                                setSmartParams();
                            }
                        case QUICK_FRY_TIME:
                            tvQuickFryMin.setText(content);
                            setFryStrong();

                            break;
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


    private void setTime(String data) {

        if (data.contains(StringConstantsUtil.STRING_MIN)) {
            String removeMinString = RemoveManOrsymbolUtil.getRemoveString(data);
            minList.add(Integer.parseInt(removeMinString));

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
                    if (isNewProtocol) {
                        setParams();
                    } else {
                        setSmartParams();
                    }
                }
            }
        });

        mSheClockTimeDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSheClockTimeDialog != null && mSheClockTimeDialog.isShow()) {
                    mSheClockTimeDialog.dismiss();
                }
            }
        });


    }

    //时间融合
    void setTimeFusion(int hour, int minute) {
        tvTime.setText(String.format("%02d:%02d", hour, minute));
        tvTime.setTag(R.id.tag_weekly_ventilation_date_hour, hour);
        tvTime.setTag(R.id.tag_weekly_ventilation_date_minute, minute);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fan_ventilation2, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        protocolVersionParams = bd == null ? null : bd.getString(PageArgumentKey.protocolVersionParams);
        initData();
        return view;
    }


    private void initData() {
        if (mDatas == null || mDatas.size() == 0) return;
        for (int i = 0; i < mDatas.size(); i++) {
            if ("ventilation".equals(mDatas.get(i).functionCode)) {
                LogUtils.i("20200717","mDatas:::"+mDatas.toString());
                String title = mDatas.get(i).functionName;
                tvDeviceModelName.setText(title);
                List<DeviceConfigurationFunctions> functions = mDatas.get(i).subView.subViewModelMap.
                        subViewModelMapSubView.deviceConfigurationFunctions;
                if (functions != null && functions.size() != 0) {
                    for (int j = 0; j < functions.size(); j++) {
                        String functionParams = functions.get(j).functionParams;
                        LogUtils.i("20190927111", functionParams);
                        try {
                            DeviceFanVentilation fanVentilation = JsonUtils.json2Pojo(functionParams, DeviceFanVentilation.class);
                            //1
                            titles = fanVentilation.getParam().getTitle().getValue();

                            defaultDay = fanVentilation.getParam().getDefaultDay().getValue();
                            String[] buttons = titles.split("button");
                            String str2 = buttons[1];

                            tvDay.setText(defaultDay);
                            tvAutoAir.setText(str2);

                            defaultWeek = fanVentilation.getParam().getDefaultWeek().getValue();
                            defaultHour = fanVentilation.getParam().getDefaultHour().getValue();
                            defaultMin = fanVentilation.getParam().getDefaultMinute().getValue();

                            //2
                            tips = fanVentilation.getParam().getTips().getValue();
                            String[] buttons1 = tips.split("button");
                            String str11 = buttons1[0];
                            String str33 = buttons1[2];
                            tvTimingAir.setText(str11);
                            String[] split = str33.split("3");
                            String timingCenterText = split[0];
                            String timingEndText = split[1];

                            tvTimingAirCenter.setText(timingCenterText + "3");
                            tvTimingAirEnd.setText(timingEndText);

                            tvWeek.setText(defaultWeek);
                            tvTime.setText(defaultHour + ":" + defaultMin);


                            List<Integer> daySection = fanVentilation.getParam().getDay().getValue();//天数
                            mDays = HelperFanData.getListDay(daySection);


                            mWeek = fanVentilation.getParam().getWeek().getValue();
                            List<Integer> hourSection = fanVentilation.getParam().getHour().getValue();
                            List<Integer> minuteSection = fanVentilation.getParam().getMinute().getValue();
                            mHours = HelperFanData.getTimeHour2(hourSection);
                            mMinute = HelperFanData.getTimeMinute2(minuteSection);


                            String desc = fanVentilation.getParam().getDesc().getValue();//3
                            defalutStriFriedMin = fanVentilation.getParam().getDefaultStirFriedMinute().getValue();
                            List<Integer> value = fanVentilation.getParam().getStirFriedMinute().getValue();
                            mStriFriedMin = HelperFanData.getTimeMinute(value);
                            if (TextUtils.equals(fan.getDt(),"8236S")) {
                                String[] buttons2 = desc.split("button");
                                String s1 = buttons2[0];
                                String s2 = buttons2[1];
                                quickFryLayout.setVisibility(View.VISIBLE);
                                tvQuickFry.setText(s1);

                                tvQuickFryMin.setText(defalutStriFriedMin);
                                tvQuickFryEnd.setText(s2);
                            } else {
                                quickFryLayout.setVisibility(View.GONE);
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        boolean isEmpty = fan == null;
                        if (isEmpty) return;
                        redSmartConfig();
                    }
                }
            }
        }


    }


    private void redSmartConfig() {
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
                mFanStatusComposite.IsWeeklyVentilation = (short) (smartParams.IsWeeklyVentilation ? 1 : 0);
                mFanStatusComposite.WeeklyVentilationDate_Week = smartParams.WeeklyVentilationDate_Week;
                mFanStatusComposite.WeeklyVentilationDate_Hour = smartParams.WeeklyVentilationDate_Hour;
                mFanStatusComposite.WeeklyVentilationDate_Minute = smartParams.WeeklyVentilationDate_Minute;

                mFanStatusComposite.gestureControlSwitch = smartParams.gesture;
                mFanStatusComposite.R8230S_Switch = smartParams.R8230S_Switch;
                mFanStatusComposite.R8230S_Time = smartParams.R8230S_Time;
                if (isAdded()) {
                    refresh(mFanStatusComposite);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }


    //出厂刷新
    private void refresh(FanStatusComposite fanStatusComposite) {
        if (fanStatusComposite == null) return;
        //是否开启定时通风[1BYTE]
        if (fanStatusComposite.IsTimingVentilation == 1) {
            //④ 自动换气开关
            cbAutoAir.setChecked(true);
            if (isAdded()) {
                tvDay.setTextColor(getResources().getColor(R.color.c11));
            }

        } else {
            //④ 自动换气开关
            cbAutoAir.setChecked(false);
            if (isAdded()) {
                tvDay.setTextColor(getResources().getColor(R.color.text_color_gray_3));
            }

        }
        if (fanStatusComposite.IsWeeklyVentilation == 1) {
            if (isAdded()) {
                //⑤ 周_
                tvWeek.setTextColor(getResources().getColor(R.color.c11));
                //⑤ 几点
                tvTime.setTextColor(getResources().getColor(R.color.c11));
            }
            //⑥ 定时换气开关
            cbTimingAir.setChecked(true);
        } else {
            if (isAdded()) {
                //⑤ 周_
                tvWeek.setTextColor(getResources().getColor(R.color.text_color_gray_3));
                //⑤ 几点
                tvTime.setTextColor(getResources().getColor(R.color.text_color_gray_3));
            }


            //⑥ 定时换气开关
            cbTimingAir.setChecked(false);
        }
        //③ _天未使用烟机，自动开启换气
        tvDay.setText(String.valueOf(fanStatusComposite.TimingVentilationPeriod));

        if (fanStatusComposite.WeeklyVentilationDate_Week >= 1 && fanStatusComposite.WeeklyVentilationDate_Week <= 7) {
            tvWeek.setText(mWeek.get(fanStatusComposite.WeeklyVentilationDate_Week - 1));
        }
        setTimeFusion(fanStatusComposite.WeeklyVentilationDate_Hour,
                fanStatusComposite.WeeklyVentilationDate_Minute);


        if (fanStatusComposite.R8230S_Switch == 1) {
            if (isAdded()) {
                //① 延长变频爆炒时间颜色
                tvQuickFryMin.setTextColor(getResources().getColor(R.color.c11));
            }
            //② 延长变频爆炒时间开关
            cbQuickFry.setChecked(true);
        } else {
            if (isAdded()) {
                //① 延长变频爆炒时间颜色
                tvQuickFryMin.setTextColor(getResources().getColor(R.color.text_color_gray_3));
            }
            //② 延长变频爆炒时间开关
            cbQuickFry.setChecked(false);
        }
        //① 延长变频爆炒时间
        tvQuickFryMin.setText(String.valueOf(fanStatusComposite.R8230S_Time));
    }


    //设置时间
    private void setTimeDialog() {
        if (mHours == null && mMinute == null) return;
        if (!cbTimingAir.isChecked()) return;
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

    //设置周
    private void setWeekDialog() {
        if (mWeek == null || mWeek.size() == 0) return;
        if (!cbTimingAir.isChecked()) return;
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

    //设置天数
    private void setDayDialog() {
        if (mDays == null || mDatas.size() == 0) return;
        if (!cbAutoAir.isChecked()) return;

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

    //设置爆炒延长时间
    private void setQuickFryDialog() {
        if (mStriFriedMin == null || mDatas.size() == 0) return;
        if (!cbQuickFry.isChecked()) return;

        if (mRokiDialog != null) {
            mRokiDialog = null;
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        if (mRokiDialog != null && !mRokiDialog.isShow()) {

            mRokiDialog.setWheelViewData(null, mStriFriedMin, null, false, 0, 2, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = QUICK_FRY_TIME;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);

            mRokiDialog.show();


        }
    }


    @OnClick({R.id.iv_back, R.id.tv_recovery, R.id.cb_auto_air,
            R.id.cb_timing_ar, R.id.cb_quick_fry, R.id.tv_day,
            R.id.tv_week, R.id.tv_time, R.id.tv_quick_fry_min})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.tv_recovery:
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

                break;
            case R.id.cb_auto_air:
            case R.id.cb_timing_ar:

                if (recovery_flag) return;
                if (isNewProtocol) {
                    setParams();
                } else {
                    setSmartParams();
                }
                break;
            case R.id.cb_quick_fry:
                setFryStrong();
                break;
            case R.id.tv_day:
                setDayDialog();
                break;
            case R.id.tv_week:
                setWeekDialog();
                break;
            case R.id.tv_time:
                setTimeDialog();
                break;
            case R.id.tv_quick_fry_min:
                setQuickFryDialog();
                break;
        }

    }

    private void setFryStrong() {
        if (cbQuickFry.isChecked()) {
            if (isAdded()) {
                tvQuickFryMin.setTextColor(getResources().getColor(R.color.c11));
            }

            mFanStatusComposite.R8230S_Switch = (short) (cbQuickFry.isChecked() ? 1 : 0);

        } else {
            if (isAdded()) {
                tvQuickFryMin.setTextColor(getResources().getColor(R.color.text_color_gray_3));
            }
            mFanStatusComposite.R8230S_Switch = (short) (cbQuickFry.isChecked() ? 1 : 0);
        }

        mFanStatusComposite.R8230S_Time = Short.parseShort(tvQuickFryMin.getText().toString());


        fan.setFryStrongTime(mFanStatusComposite, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20190929", "设置成功");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190929", t.getMessage());

            }
        });
    }


    void setSmartParams() {
        LogUtils.i("20190731", "setSmartParams");
        if (fan == null) {
            return;
        }
        //是否开启定时通风[1BYTE]
        sp.IsTimingVentilation = cbAutoAir.isChecked();
        //是否开启每周通风[1BYTE]
        sp.IsWeeklyVentilation = cbTimingAir.isChecked();
        //定时通风间隔时间[1BYTE],单位天
        sp.TimingVentilationPeriod = Short.parseShort(defaultDay);
        short weekValue = HelperFanData.getWeekValue(defaultWeek);
        sp.WeeklyVentilationDate_Week = weekValue;
        sp.WeeklyVentilationDate_Hour = Short.parseShort(defaultHour);
        sp.WeeklyVentilationDate_Minute = Short.parseShort(defaultMin);

        fan.setSmartConfig(sp, new VoidCallback() {

            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                recovery_flag = false;
                setParams();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }


    private void setParams() {
        LogUtils.i("20190731", "setWeekParams");
        if (fan == null) {
            return;
        }
        //是否开启定时通风[1BYTE]
        mFanStatusComposite.IsTimingVentilation = (short) (cbAutoAir.isChecked() ? 1 : 0);
        //是否开启每周通风[1BYTE]
        mFanStatusComposite.IsWeeklyVentilation = (short) (cbTimingAir.isChecked() ? 1 : 0);
        mFanStatusComposite.R8230S_Switch = (short) (cbQuickFry.isChecked() ? 1 : 0);
        mFanStatusComposite.R8230S_Time = Short.parseShort(tvQuickFryMin.getText().toString());

        //定时通风间隔时间[1BYTE],单位天
        mFanStatusComposite.TimingVentilationPeriod = Short.parseShort(tvDay.getText().toString());
        String week = tvWeek.getText().toString();
        short weekValue = HelperFanData.getWeekValue(week);
        mFanStatusComposite.WeeklyVentilationDate_Week = weekValue;
        if (tvTime.getTag(R.id.tag_weekly_ventilation_date_hour) != null) {

            mFanStatusComposite.WeeklyVentilationDate_Hour = Short.parseShort(tvTime.getTag(R.id.tag_weekly_ventilation_date_hour).toString());
        }
        if (tvTime.getTag(R.id.tag_weekly_ventilation_date_minute) != null) {

            mFanStatusComposite.WeeklyVentilationDate_Minute = Short.parseShort(tvTime.getTag(R.id.tag_weekly_ventilation_date_minute).toString());
        }
        List<Integer> listKey = new ArrayList<>();

        listKey.add(6);
        listKey.add(7);

        LogUtils.i("201909298888", "R8230S_Time::::" + mFanStatusComposite.R8230S_Time);
        fan.setFanCombo(mFanStatusComposite, (short) 2, listKey, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_sterilizer_succeed);
                recovery_flag = false;
                redSmartConfig();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190928", t.getMessage());
            }
        });


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
        if (isNewProtocol) {
            //mFanStatusComposite.R8230S_Switch = (short) 0;
            //mFanStatusComposite.R8230S_Time = (short) 3;
            //mFanStatusComposite.gestureControlSwitch = (short) 1;
            //fan.setFryStrongTime(mFanStatusComposite, new VoidCallback() {
            //    @Override
            //    public void onSuccess() {
            //        new Handler().postDelayed(new Runnable() {
            //            @Override
            //            public void run() {
            //                ToastUtils.showShort(R.string.device_sterilizer_succeed);
            //                setChecked();
            //            }
            //        },500);
            //    }
            //    @Override
            //    public void onFailure(Throwable t) {
            //        LogUtils.i("20190929", t.getMessage());
            //    }
            //});

            List<Integer> listKey = new ArrayList<>();
            FanStatusComposite fanStatusComposite = new FanStatusComposite();
            mFanStatusComposite.IsPowerLinkage =fanStatusComposite.IsPowerLinkage;
            mFanStatusComposite.IsShutdownLinkage =fanStatusComposite.IsShutdownLinkage;
            mFanStatusComposite.IsShutdownLinkage =fanStatusComposite.ShutdownDelay;
            mFanStatusComposite.IsLevelLinkage = fanStatusComposite.IsLevelLinkage;
            mFanStatusComposite.R8230S_Switch = fanStatusComposite.R8230S_Switch;
            mFanStatusComposite.R8230S_Time = fanStatusComposite.R8230S_Time;
            mFanStatusComposite.IsTimingVentilation=fanStatusComposite.IsTimingVentilation;
            mFanStatusComposite.TimingVentilationPeriod=fanStatusComposite.TimingVentilationPeriod;
            mFanStatusComposite.IsWeeklyVentilation=fanStatusComposite.IsWeeklyVentilation;
            mFanStatusComposite.WeeklyVentilationDate_Week=fanStatusComposite.WeeklyVentilationDate_Week;
            mFanStatusComposite.WeeklyVentilationDate_Hour=fanStatusComposite.WeeklyVentilationDate_Hour;
            mFanStatusComposite.WeeklyVentilationDate_Minute=fanStatusComposite.WeeklyVentilationDate_Minute;
            mFanStatusComposite.gestureControlSwitch = fanStatusComposite.gestureControlSwitch;
            listKey.add(1);
            listKey.add(2);
            listKey.add(3);
            listKey.add(4);
            listKey.add(6);
            listKey.add(7);
            listKey.add(8);
            listKey.add(13);
            fan.setFanCombo(mFanStatusComposite, (short) 8, listKey, new VoidCallback() {
                @Override
                public void onSuccess() {
                    ToastUtils.showShort(R.string.device_sterilizer_succeed);
                    recovery_flag = false;
                    redSmartConfig();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });



        } else {
            fan.setSmartConfig(new SmartParams(), new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(R.string.device_sterilizer_succeed);
                            recovery_flag = false;
                        }
                    },500);

                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.showThrowable(t);
                }
            });


        }

    }

    private void setChecked() {
        if (mFanStatusComposite==null) {
            return;
        }
        if (mFanStatusComposite.R8230S_Switch == 1) {
            if (cbQuickFry != null && tvQuickFryMin != null) {
                cbQuickFry.setChecked(true);
                if (isAdded()) {
                    tvQuickFryMin.setTextColor(getResources().getColor(R.color.c11));
                }

            }


        } else {
            if (cbQuickFry != null && tvQuickFryMin != null) {
                cbQuickFry.setChecked(false);
                if (isAdded()) {
                    tvQuickFryMin.setTextColor(getResources().getColor(R.color.text_color_gray_3));
                }

            }

        }
        if (tvQuickFryMin != null) {
            tvQuickFryMin.setText(String.valueOf(mFanStatusComposite.R8230S_Time));
        }

    }


}
