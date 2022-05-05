package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.view.EmojiEmptyView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class SmartParamsPage extends HeadPage implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    static final String[] week = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    private final int WEEK = 1;
    private final int DAYS = 2;
    private final int MINUTES = 3;
    private final int CLOCKTIME = 4;
    @InjectView(R.id.emptyView)
    EmojiEmptyView emptyView;
    @InjectView(R.id.mainView)
    ScrollView mainView;

    @InjectView(R.id.chkIsPowerLinkage)
    CheckBoxView chkIsPowerLinkage;//联动设置 开关
    @InjectView(R.id.txtShutdownDelay)
    TextView txtShutdownDelay;//延时关机 分钟选择
    @InjectView(R.id.chkIsShutdownLinkage)
    CheckBoxView chkIsShutdownLinkage;//延时关机 开关
    @InjectView(R.id.chkIsLevelLinkage)
    CheckBoxView chkIsLevelLinkage;
    @InjectView(R.id.txtTimingVentilationPeriod)
    TextView txtTimingVentilationPeriod;//通风换气 天数选择
    @InjectView(R.id.chkIsTimingVentilation)
    CheckBoxView chkIsTimingVentilation;//通风换气 开关
    @InjectView(R.id.txtWeeklyVentilationDate_Week)
    TextView txtWeeklyVentilationDateWeek;// 每周固定换气 周几选择
    @InjectView(R.id.txtWeeklyVentilationDate_Time)
    TextView txtWeeklyVentilationDateTime;// 每周固定换气 小时选择
    @InjectView(R.id.chkIsWeeklyVentilation)
    CheckBoxView chkIsWeeklyVentilation;//每周固定换气 开关
    @InjectView(R.id.fan_pic_show_1)
    ImageView fanShow1;
    @InjectView(R.id.fan_pic_show_2)
    ImageView fanShow2;
    @InjectView(R.id.fan_ll_set)
    LinearLayout fanSet;
    @InjectView(R.id.fan_ll_air_refresh)
    LinearLayout fanAirRefresh;
    @InjectView(R.id.disconnect_tip)
    TextView disconnecTip;

    final List<Integer> hourList = new ArrayList<>();
    final List<Integer> minList = new ArrayList<>();
    /* @InjectView(R.id.chkIsNoticClean)
    CheckBoxView chkIsNoticClean;//清洁提示 开关
    @InjectView(R.id.chkIsCleanBy360)
    CheckBoxView chkIsCleanBy360;*/

    AbsFan fan;
    private boolean recovery_flag;
    private boolean flag = true;
    private boolean flag1 = true;
    private IRokiDialog mSheTimeDialog = null;
    private IRokiDialog mSheClockTimeDialog = null;

    Handler mHandelr = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WEEK:
                    setWeekData((String) msg.obj);
                    break;
                case DAYS:
                    setDatsData((String) msg.obj);
                    break;
                case MINUTES:
                    setMinutesData((String) msg.obj);
                    break;
                case CLOCKTIME:
                    setClocktimeData((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        String guid = getArguments().getString(PageArgumentKey.Guid);
        fan = Plat.deviceService.lookupChild(guid);
        View view = layoutInflater.inflate(R.layout.page_smart_params, viewGroup, false);
        ButterKnife.inject(this, view);
        //  fan = Utils.getDefaultFan();
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.txtRestore)
    public void onClickRestore() {

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

    @Override
    public void onCheckedChanged(CompoundButton chkBox, boolean isChecked) {
        try {

            if (recovery_flag) return;
           /* if (chkBox == chkIsCleanBy360) {
                Log.i("onSuccessonSuccess","onCheckedChanged()1");
                //setSmartParamsOn360();
            } else */
            if (chkBox == chkIsPowerLinkage) {//联动
                Log.i("onSuccessonSuccess", "onCheckedChanged()2");
                setSmartParams();
            } else if (chkBox == chkIsTimingVentilation) {//通风换气
                Log.i("onSuccessonSuccess", "onCheckedChanged()3");
                setSmartParams();
            }/*else if(chkBox==chkIsNoticClean){
                Log.i("onSuccessonSuccess","onCheckedChanged()4");
                setSmartParams();
            }*/
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    //分钟
    private List<String> getMinutesList() {
        List<String> mMinutesList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            mMinutesList.add(i + StringConstantsUtil.STRING_MINUTES);
        }
        return mMinutesList;
    }

    //天数
    private List<String> getDaysList() {
        List<String> mDaysList = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            mDaysList.add(i + StringConstantsUtil.STRING_DAY);
        }
        return mDaysList;
    }

    //星期
    private List<String> getWeekList() {
        List<String> mWeekList = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            switch (i) {
                case 1:
                    mWeekList.add("周一");
                    break;
                case 2:
                    mWeekList.add("周二");
                    break;
                case 3:
                    mWeekList.add("周三");
                    break;
                case 4:
                    mWeekList.add("周四");
                    break;
                case 5:
                    mWeekList.add("周五");
                    break;
                case 6:
                    mWeekList.add("周六");
                    break;
                case 7:
                    mWeekList.add("周日");
                    break;
            }
        }
        return mWeekList;
    }

    //小时
    private List<String> getTimeHour() {
        List<String> hourList = new ArrayList<>();
        for (int i = 00; i <= 23; i++) {
            hourList.add(i + StringConstantsUtil.STR_HOUR);
        }
        return hourList;
    }

    //分钟
    private List<String> getTimeMinute() {
        List<String> minuteList = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minuteList.add(i + StringConstantsUtil.STRING_MIN);
        }
        return minuteList;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtShutdownDelay:
                if (!chkIsPowerLinkage.isChecked()) {
                    //ToastUtils.show("请开启联动", Toast.LENGTH_SHORT);
                    break;
                }

                showListSelectDialog(txtShutdownDelay, getMinutesList());
                break;
            case R.id.txtTimingVentilationPeriod:
                if (!chkIsTimingVentilation.isChecked()) {
                    //ToastUtils.show("请开启通风换气", Toast.LENGTH_SHORT);
                    break;
                }
                showListSelectDialog(txtTimingVentilationPeriod, getDaysList());
                break;
            case R.id.txtWeeklyVentilationDate_Week:
                if (!chkIsTimingVentilation.isChecked()) {
                    //ToastUtils.show("请开启通风换气", Toast.LENGTH_SHORT);
                    break;
                }
                showListSelectDialog(txtWeeklyVentilationDateWeek, getWeekList());
                break;
            case R.id.txtWeeklyVentilationDate_Time:
                if (!chkIsTimingVentilation.isChecked()) {
                    //ToastUtils.show("请开启通风换气", Toast.LENGTH_SHORT);
                    break;
                }
                showTimeSetDialog();
                break;

            default:
                break;
        }
    }

    void initData() {

        boolean isEmpty = fan == null;
        switchView(isEmpty);
        if (isEmpty) return;
        if (!fan.isConnected()) {
            mainView.setVisibility(View.GONE);
            disconnecTip.setVisibility(View.VISIBLE);
            return;
        } else {
            mainView.setVisibility(View.VISIBLE);
            disconnecTip.setVisibility(View.GONE);
        }
        fan.getSmartConfig(new Callback<SmartParams>() {

            @Override
            public void onSuccess(SmartParams smartParams) {
                refresh(smartParams);
                setListener();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
                setListener();
            }
        });


        RokiRestHelper.getSmartParams360(fan.getID(), new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean status) {
//                chkIsCleanBy360.setChecked(status);
//                setListenerOn360();
            }

            @Override
            public void onFailure(Throwable t) {
//                setListenerOn360();
//                ToastUtils.showShort("获取360燃气卫士配置失败");
            }
        });
    }

    //省心省电点击事件
    @OnClick(R.id.fan_pic_show_1)
    public void onClickShow1() {
        SmartParams smartParams = new SmartParams();
        if (flag) {
            fanSet.setVisibility(View.VISIBLE);
            fanShow1.setImageResource(R.mipmap.img_8230s_expand_shang);
            showWeeklyVentilationDate(smartParams.WeeklyVentilationDate_Hour,
                    smartParams.WeeklyVentilationDate_Minute);
            // setSmartParams();
            flag = false;
            return;
        }
        fanSet.setVisibility(View.GONE);
        fanShow1.setImageResource(R.mipmap.img_fan8230s_expand);
        flag = true;
    }

    //空气焕然一新点击事件
    @OnClick(R.id.fan_pic_show_2)
    public void onClickSHow2() {
        SmartParams smartParams = new SmartParams();
        if (flag1) {
            fanAirRefresh.setVisibility(View.VISIBLE);
            fanShow2.setImageResource(R.mipmap.img_8230s_expand_shang);
            //refresh(new SmartParams());
            //  setSmartParams();
            flag1 = false;
            return;
        }
        fanAirRefresh.setVisibility(View.GONE);
        fanShow2.setImageResource(R.mipmap.img_fan8230s_expand);
        flag1 = true;
    }

    void refresh(SmartParams smartParams) {
        Log.i("SmartParams-->", "联动:" + smartParams.IsPowerLinkage +
                " 延时关机 开关:" + smartParams.IsShutdownLinkage + " 延时关机时间:" + smartParams.ShutdownDelay + " 档位联动:" + smartParams.IsLevelLinkage + " 通风换气开关:" + smartParams.IsTimingVentilation +
                " 定时通风间隔时间:" + smartParams.TimingVentilationPeriod + " 是否开启每周通风:" + smartParams.IsWeeklyVentilation
                + " 每周通风的时间--周几:" + smartParams.WeeklyVentilationDate_Week + " 每周通风小时分钟" + smartParams.WeeklyVentilationDate_Hour + "/" + smartParams.WeeklyVentilationDate_Minute
                + " 油烟机清洗提示开关:" + smartParams.IsNoticClean
        );
        if (smartParams == null) return;

        chkIsPowerLinkage.setChecked(smartParams.IsPowerLinkage);
        //chkIsLevelLinkage.setChecked(smartParams.IsPowerLinkage);
        chkIsShutdownLinkage.setChecked(smartParams.IsShutdownLinkage);
        chkIsTimingVentilation.setChecked(smartParams.IsTimingVentilation);
        chkIsWeeklyVentilation.setChecked(smartParams.IsWeeklyVentilation);
        //  chkIsNoticClean.setChecked(smartParams.IsNoticClean);

        txtShutdownDelay.setText(String.valueOf(smartParams.ShutdownDelay));
        txtTimingVentilationPeriod.setText(String.valueOf(smartParams.TimingVentilationPeriod));
        if (smartParams.WeeklyVentilationDate_Week >= 1 && smartParams.WeeklyVentilationDate_Week <= 7) {
            txtWeeklyVentilationDateWeek.setText(week[smartParams.WeeklyVentilationDate_Week - 1]);
        }

        showWeeklyVentilationDate(smartParams.WeeklyVentilationDate_Hour,
                smartParams.WeeklyVentilationDate_Minute);

        setOnOffStatusForText(smartParams.IsPowerLinkage, smartParams.IsTimingVentilation);
    }

    void setListener() {
        if (!this.isAdded()) return;

        chkIsPowerLinkage.setOnCheckedChangeListener(this);
        //chkIsLevelLinkage.setOnCheckedChangeListener(this);
        //chkIsShutdownLinkage.setOnCheckedChangeListener(this);
        chkIsTimingVentilation.setOnCheckedChangeListener(this);
        //chkIsWeeklyVentilation.setOnCheckedChangeListener(this);

        //chkIsNoticClean.setOnCheckedChangeListener(this);

        txtShutdownDelay.setOnClickListener(this);
        txtTimingVentilationPeriod.setOnClickListener(this);
        txtWeeklyVentilationDateWeek.setOnClickListener(this);
        txtWeeklyVentilationDateTime.setOnClickListener(this);
    }

 /*   void setListenerOn360() {
        chkIsCleanBy360.setOnCheckedChangeListener(this);
    }*/

    private void recovery() {
        recovery_flag = true;
        refresh(new SmartParams());
        Log.i("onSuccessonSuccess", "recovery()");
        setSmartParams();
        //chkIsCleanBy360.setChecked(false);
    }


    void setSmartParams() {
        if (fan == null) {
            return;
        }
        final SmartParams sp = new SmartParams();
        sp.IsPowerLinkage = chkIsPowerLinkage.isChecked();
        sp.IsLevelLinkage = chkIsPowerLinkage.isChecked();
        sp.IsShutdownLinkage = chkIsPowerLinkage.isChecked();
        sp.IsTimingVentilation = chkIsTimingVentilation.isChecked();
        sp.IsWeeklyVentilation = chkIsTimingVentilation.isChecked();
        //  sp.IsNoticClean = chkIsNoticClean.isChecked();
        //将设置的延迟时间保存在本地，方便toast显示

        String shutDownTime = txtShutdownDelay.getText().toString();
        PreferenceUtils.setInt(PageArgumentKey.ShutDownDelay, Integer.parseInt(shutDownTime));
        sp.ShutdownDelay = Short.parseShort(shutDownTime);

        sp.TimingVentilationPeriod = Short.parseShort(txtTimingVentilationPeriod.getText().toString());
        int weekDayIndex = Arrays.asList(week).indexOf(txtWeeklyVentilationDateWeek.getText().toString());
        sp.WeeklyVentilationDate_Week = (short) (weekDayIndex + 1);
        sp.WeeklyVentilationDate_Hour = Short.parseShort(txtWeeklyVentilationDateTime.getTag(R.id.tag_weekly_ventilation_date_hour).toString());
        sp.WeeklyVentilationDate_Minute = Short.parseShort(txtWeeklyVentilationDateTime.getTag(R.id.tag_weekly_ventilation_date_minute).toString());

        Log.i("SmartParams-->设置", "联动:" + sp.IsPowerLinkage +
                " 延时关机 开关:" + sp.IsShutdownLinkage + " 延时关机时间:" + sp.ShutdownDelay + " 档位联动:" + sp.IsLevelLinkage + " 通风换气开关:" + sp.IsTimingVentilation +
                " 定时通风间隔时间:" + sp.TimingVentilationPeriod + " 是否开启每周通风:" + sp.IsWeeklyVentilation
                + " 每周通风的时间--周几:" + sp.WeeklyVentilationDate_Week + " 每周通风小时分钟" + sp.WeeklyVentilationDate_Hour + "/" + sp.WeeklyVentilationDate_Minute
                + " 油烟机清洗提示开关:" + sp.IsNoticClean
        );
        fan.setSmartConfig(sp, new VoidCallback() {

            @Override
            public void onSuccess() {
                Log.i("SmartParams", "设置成功12");
                setOnOffStatusForText(sp.IsPowerLinkage, sp.IsTimingVentilation);
                ToastUtils.showShort("设置成功");
                recovery_flag = false;
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("SmartParams", "设置失败1233");
                ToastUtils.showThrowable(t);
            }
        });

    }


    private void showTimeSetDialog() {
        mSheClockTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        mSheClockTimeDialog.setWheelViewData(getTimeHour(), null, getTimeMinute(), false, 12, 0, 30, new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {
                Message message = mHandelr.obtainMessage();
                message.what = CLOCKTIME;
                message.obj = contentFront;
                mHandelr.sendMessage(message);
            }
        }, null, new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                Message message = mHandelr.obtainMessage();
                message.what = CLOCKTIME;
                message.obj = contentRear;
                mHandelr.sendMessage(message);
            }
        });

        mSheClockTimeDialog.show();
//        TimeSetDialog.show(cx, "设置时间", 12, 30, 23, 59,
//                new TimeSetDialog.TimeSeletedCallback() {
//                    @Override
//                    public void onTimeSeleted(int hour, int minute) {
//                        showWeeklyVentilationDate(hour, minute);
//                        Log.i("onSuccessonSuccess","showTimeSetDialog()");
//                        setSmartParams();
//                    }
//
//                });
    }

    void showListSelectDialog(final View parent, List<String> list) {
        mSheTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        switch (parent.getId()) {
            case R.id.txtShutdownDelay:
                //"设置延时时间（分钟）";
                mSheTimeDialog.setWheelViewData(null, list, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message message = mHandelr.obtainMessage();
                        message.what = MINUTES;
                        message.obj = contentCenter;
                        mHandelr.sendMessage(message);
                    }
                }, null);
                mSheTimeDialog.show();
                break;
            case R.id.txtTimingVentilationPeriod:
                //"设置天数（天）";
                mSheTimeDialog.setWheelViewData(null, list, null, false, 0, 2, 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message message = mHandelr.obtainMessage();
                        message.what = DAYS;
                        message.obj = contentCenter;
                        mHandelr.sendMessage(message);
                    }
                }, null);
                mSheTimeDialog.show();
                break;
            case R.id.txtWeeklyVentilationDate_Week:
                //"设置周几";第六个默认值
                mSheTimeDialog.setWheelViewData(null, list, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message message = mHandelr.obtainMessage();
                        message.what = WEEK;
                        message.obj = contentCenter;
                        mHandelr.sendMessage(message);
                    }
                }, null);
                mSheTimeDialog.show();
                break;
        }
    }


    private void setWeekData(String content) {

        txtWeeklyVentilationDateWeek.setTag(WEEK);
        canAndOkBtnListener(txtWeeklyVentilationDateWeek, content);
    }

    private void setDatsData(String content) {
        String removeString = RemoveManOrsymbolUtil.getRemoveString(content);
        txtTimingVentilationPeriod.setTag(DAYS);
        canAndOkBtnListener(txtTimingVentilationPeriod, removeString);

    }

    private void setMinutesData(String content) {
        String removeString = RemoveManOrsymbolUtil.getRemoveString(content);
        txtShutdownDelay.setTag(MINUTES);
        canAndOkBtnListener(txtShutdownDelay, removeString);
    }

    private void setClocktimeData(String content) {

        if (content.contains(StringConstantsUtil.STRING_MIN)) {
            String removetMinString = RemoveManOrsymbolUtil.getRemoveString(content);
            minList.add(Integer.parseInt(removetMinString));

        }
        if (content.contains(StringConstantsUtil.STR_HOUR)) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(content);
            hourList.add(Integer.parseInt(removeTimeString));
        }
        mSheClockTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSheClockTimeDialog != null && mSheClockTimeDialog.isShow()) {
                    mSheClockTimeDialog.dismiss();
                    showWeeklyVentilationDate(hourList.get(hourList.size() - 1), minList.get(minList.size() - 1));
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


    private void canAndOkBtnListener(final TextView view, final String content) {
        mSheTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtils.i("20170919", "v:" + v.getTag());

                if (mSheTimeDialog != null && mSheTimeDialog.isShow()) {
                    mSheTimeDialog.dismiss();
                    int tag = (int) view.getTag();
                    switch (tag) {
                        case WEEK:
                            txtWeeklyVentilationDateWeek.setText(content);
                            setSmartParams();
                            break;
                        case MINUTES:
                            txtShutdownDelay.setText(content);
                            setSmartParams();
                            break;
                        case DAYS:
                            txtTimingVentilationPeriod.setText(content);
                            setSmartParams();
                            break;
                    }
                }
            }
        });

        mSheTimeDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    //设置时间标签
    void showWeeklyVentilationDate(int hour, int minute) {
        txtWeeklyVentilationDateTime.setText(String.format("%02d:%02d", hour, minute));
        txtWeeklyVentilationDateTime.setTag(R.id.tag_weekly_ventilation_date_hour, hour);
        txtWeeklyVentilationDateTime.setTag(R.id.tag_weekly_ventilation_date_minute, minute);
    }

    void switchView(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        mainView.setVisibility(!isEmpty ? View.VISIBLE : View.GONE);
    }

    /**
     * 联动设置是否开启
     * 通风换气设置是否开启
     *
     * @param
     * @param
     */
    private void setOnOffStatusForText(boolean ld, boolean tf) {
        txtShutdownDelay.setTextColor(ld ? r.getColor(R.color.c07) : r.getColor(R.color.c03));

        txtTimingVentilationPeriod.setTextColor(tf ? r.getColor(R.color.c07) : r.getColor(R.color.c03));
        txtWeeklyVentilationDateWeek.setTextColor(tf ? r.getColor(R.color.c07) : r.getColor(R.color.c03));
        txtWeeklyVentilationDateTime.setTextColor(tf ? r.getColor(R.color.c07) : r.getColor(R.color.c03));
    }

}
