package com.robam.roki.ui.page.device.fan;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
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
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.bean.DeviceFanStoveLinkage;
import com.robam.roki.model.helper.HelperFanData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.adapter.FanBackgroundFuncAdapter.isNewProtocol;

/**
 * Created by RuanWei on 2019/9/27.
 * 烟灶联动 5916s
 */

public class DeviceFanStoveLinkage2Page extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;

    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;


    @InjectView(R.id.tv_fan_auto)
    TextView tvFanAuto;

    @InjectView(R.id.cb_fan_auto_switch)
    CheckBoxView cbFanAutoSwitch;


    @InjectView(R.id.tv_delay)
    TextView tvDelay;

    @InjectView(R.id.cb_delay)
    CheckBoxView cbDelay;


    @InjectView(R.id.tv_stir_fry)
    TextView tvStirFry;

    @InjectView(R.id.cb_stir_fry)
    CheckBoxView cbStirFry;

    @InjectView(R.id.tv_delay_time)
    TextView tvDelayTime;

    @InjectView(R.id.tv_delay_end)
    TextView tvDelayEnd;


    private List<DeviceConfigurationFunctions> mDates;
    AbsFan fan;
    private List<String> mMinute;

    private IRokiDialog mRokiDialog;

    private final int MINUTE = 1;
    private FanStatusComposite mFanStatusComposite = new FanStatusComposite();
    SmartParams sp = new SmartParams();

    private String tips;
    private String mins;

    private boolean recovery_flag;

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
    private String contentdata;

    private void setMinute(String obj) {
        mins = obj.substring(0, 1);
        tvDelayTime.setText(mins);
        if (isNewProtocol) {
            setMinParams();
        } else {
            setSmartParams();
        }

    }

    //恢复出厂
    private void recovery() {
        recovery_flag = true;
        refresh(new FanStatusComposite());
        if (isNewProtocol) {
            recoveryNew();
        }else{
            recoveryP();

        }

    }

    private void recoveryNew() {
        if (fan == null) {
            return;
        }
        List<Integer> listKey = new ArrayList<>();
        FanStatusComposite fanStatusComposite = new FanStatusComposite();
        mFanStatusComposite.IsPowerLinkage =fanStatusComposite.IsPowerLinkage;
        mFanStatusComposite.IsShutdownLinkage =fanStatusComposite.IsShutdownLinkage;
        mFanStatusComposite.ShutdownDelay =fanStatusComposite.ShutdownDelay;
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
    }


    private void recoveryP() {
        if (fan == null) {
            return;
        }
        fan.setSmartConfig(new SmartParams(), new VoidCallback() {
            @Override
            public void onSuccess() {
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

    private void setMinParams() {
        if (fan == null) {
            return;
        }
        // 烟机电磁灶开关联动(0关，1开)
        mFanStatusComposite.IsPowerLinkage = (short) (cbFanAutoSwitch.isChecked() ? 1 : 0);
        //烟机档位联动开关（0关，1开）  爆炒
        mFanStatusComposite.IsLevelLinkage = (short) (cbStirFry.isChecked() ? 1 : 0);
        //电磁灶关机后烟机延时关机开关（0关，1开）
        mFanStatusComposite.IsShutdownLinkage = (short) (cbDelay.isChecked() ? 1 : 0);

        mFanStatusComposite.ShutdownDelay = Short.valueOf(mins);
        List<Integer> listKey = new ArrayList<>();
        listKey.add(1);
        listKey.add(2);
        listKey.add(3);
        listKey.add(4);
        fan.setFanCombo(mFanStatusComposite, (short) 4, listKey, new VoidCallback() {
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
    }


    void setSmartParams() {
        LogUtils.i("20190731", "setSmartParams:" + isNewProtocol);
        if (fan == null) {
            return;
        }

        // 烟机电磁灶开关联动(0关，1开)
        sp.IsPowerLinkage = cbFanAutoSwitch.isChecked();
        //电磁灶关机后烟机延时关机开关（0关，1开）
        sp.IsShutdownLinkage = cbDelay.isChecked();

        //烟机档位联动开关（0关，1开）  爆炒
        sp.IsLevelLinkage = cbStirFry.isChecked();
        sp.ShutdownDelay = Short.parseShort(mins);

        fan.setSmartConfig(sp, new VoidCallback() {

            @Override
            public void onSuccess() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_stove_linkage2, container, false);
        Bundle bd = getArguments();
        mDates = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        ButterKnife.inject(this, view);
        initData();
        initSetData();
        return view;
    }

    private void initSetData() {
        //当开启灶具时，烟机自动开启
        cbFanAutoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFanStatusComposite.IsPowerLinkage = 1;
                } else {
                    mFanStatusComposite.IsPowerLinkage = 0;
                }
                fan.setPowerLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("20190928", "执行成功");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20190928", t.getMessage());
                    }
                });

            }
        });


        //当关闭灶具时，烟机延时 分钟关闭
        cbDelay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFanStatusComposite.IsShutdownLinkage = 1;
                    if (isAdded()) {
                        tvDelayTime.setTextColor(getResources().getColor(R.color.c11));
                    }

                } else {
                    mFanStatusComposite.IsShutdownLinkage = 0;
                    if (isAdded()) {
                        tvDelayTime.setTextColor(getResources().getColor(R.color.text_color_gray_3));
                    }


                }
                fan.setShutdownLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("20190928", "执行成功");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20190928", t.getMessage());
                    }
                });

            }
        });

        //爆炒模式
        cbStirFry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFanStatusComposite.IsLevelLinkage = 1;
                } else {
                    mFanStatusComposite.IsLevelLinkage = 0;
                }
                fan.setLevelLinkageSwitch(mFanStatusComposite, (short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("20190928", "执行成功");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20190928", t.getMessage());
                    }
                });

            }
        });
    }


    private void initData() {

        if (mDates == null || mDates.size() == 0) return;
        for (int i = 0; i < mDates.size(); i++) {
            if ("smokeStoveLinkage".equals(mDates.get(i).functionCode)) {
                String title = mDates.get(i).functionName;
                tvDeviceModelName.setText(title);
                List<DeviceConfigurationFunctions> functions = mDates.get(i).subView.subViewModelMap.
                        subViewModelMapSubView.deviceConfigurationFunctions;
                if (functions != null && functions.size() != 0) {
                    for (int j = 0; j < functions.size(); j++) {
                        String functionParams = functions.get(j).functionParams;
                        try {
                            DeviceFanStoveLinkage fanStoveLinkage = JsonUtils.json2Pojo(functionParams, DeviceFanStoveLinkage.class);
                            tips = fanStoveLinkage.getParam().getTips().getValue();
                            String titles = fanStoveLinkage.getParam().getTitle().getValue();
                            String desc = fanStoveLinkage.getParam().getDesc().getValue();
                            List<Integer> list = fanStoveLinkage.getParam().getMinute().getValue();
                            String defaultMinute = fanStoveLinkage.getParam().getDefaultMinute().getValue();
                            mMinute = HelperFanData.getTimeMinute(list);
                            tvFanAuto.setText(titles);
                            tvStirFry.setText(desc);

                            String[] buttons = tips.split("button");
                            String str1 = buttons[0];
                            String str2 = buttons[1];


                            tvDelay.setText(str1);
                            tvDelayEnd.setText(str2);

                            tvDelayTime.setText(defaultMinute);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        boolean isEmpty = fan == null;
        if (isEmpty) return;
        redSmartConfig();

    }

    private void redSmartConfig() {
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                sp = smartParams;
                //是否开启定时通风[1BYTE]
                mFanStatusComposite.IsTimingVentilation = (short) (smartParams.IsTimingVentilation ? 1 : 0);
                // 油烟机清洗提示开关[1Byte], 0不提示，1提示
                mFanStatusComposite.IsNoticClean = (short) (smartParams.IsNoticClean ? 1 : 0);
                //定时通风间隔时间[1BYTE],单位天
                mFanStatusComposite.TimingVentilationPeriod = smartParams.TimingVentilationPeriod;
                //烟机电磁灶开关联动(0关，1开)[1Byte]
                mFanStatusComposite.IsPowerLinkage = (short) (smartParams.IsPowerLinkage ? 1 : 0);
                //烟机档位联动开关（0关，1开）[1Byte]
                mFanStatusComposite.IsLevelLinkage = (short) (smartParams.IsLevelLinkage ? 1 : 0);
                //电磁灶关机后烟机延时关机开关（0关，1开）[1Byte]
                mFanStatusComposite.IsShutdownLinkage = (short) (smartParams.IsShutdownLinkage ? 1 : 0);
                //电磁灶关机后烟机延时关机时间（延时时间，单位分钟，1~5分钟）[1Byte]
                mFanStatusComposite.ShutdownDelay = smartParams.ShutdownDelay;
                //是否开启每周通风[1BYTE]
                mFanStatusComposite.IsWeeklyVentilation = (short) (smartParams.IsWeeklyVentilation ? 1 : 0);
                //每周通风的时间--周几
                mFanStatusComposite.WeeklyVentilationDate_Week = smartParams.WeeklyVentilationDate_Week;
                //每周通风的时间--小时
                mFanStatusComposite.WeeklyVentilationDate_Hour = smartParams.WeeklyVentilationDate_Hour;
                //每周通风的时间--分钟
                mFanStatusComposite.WeeklyVentilationDate_Minute = smartParams.WeeklyVentilationDate_Minute;
                refresh(mFanStatusComposite);
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

        //烟灶联动开关
        if (fanStatusComposite.IsPowerLinkage == 1) {
            cbFanAutoSwitch.setChecked(true);
        } else {
            cbFanAutoSwitch.setChecked(false);
        }
        //延时开关
        if (fanStatusComposite.IsShutdownLinkage == 1) {
            cbDelay.setChecked(true);
            if (isAdded()) {
                tvDelayTime.setTextColor(getResources().getColor(R.color.c11));
            }

        } else {
            cbDelay.setChecked(false);
            if (isAdded()) {
                tvDelayTime.setTextColor(getResources().getColor(R.color.text_color_gray_3));
            }

        }
        //爆炒开关
        if (fanStatusComposite.IsLevelLinkage == 1) {
            cbStirFry.setChecked(true);
        } else {
            cbStirFry.setChecked(false);
        }
        //延时时间
        tvDelayTime.setText(String.valueOf(fanStatusComposite.ShutdownDelay));

    }








    @Override
    public void onResume() {
        super.onResume();
        if (fan == null) {
            return;
        }
        if (fan.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), fan.getDt() + ":烟灶联动页", null);
        }
    }

    private void setTimeDialog() {

        if (mMinute == null || mMinute.size() == 0) return;


        if (mRokiDialog != null) {
            mRokiDialog = null;
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        if (mRokiDialog != null && !mRokiDialog.isShow()) {

            mRokiDialog.setWheelViewData(null, mMinute, null, false,
                    0, 5, 0, null, new OnItemSelectedListenerCenter() {
                        @Override
                        public void onItemSelectedCenter(String contentCenter) {
                            contentdata = contentCenter;
                        }
                    }, null);

            mRokiDialog.show();

        }


        mRokiDialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });

        mRokiDialog.setOkBtn("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = mHandler.obtainMessage();
                msg.what = MINUTE;
                msg.obj = contentdata;
                mHandler.sendMessage(msg);
                mRokiDialog.dismiss();
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_recovery, R.id.tv_delay_time})
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
            case R.id.tv_delay_time:
                if (cbDelay.isChecked()) {
                    setTimeDialog();
                }

                break;
        }

    }

}
