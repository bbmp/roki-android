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
 * ηηΆθε¨ 5916s
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

    //ζ’ε€εΊε
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
        // ηζΊη΅η£ηΆεΌε³θε¨(0ε³οΌ1εΌ)
        mFanStatusComposite.IsPowerLinkage = (short) (cbFanAutoSwitch.isChecked() ? 1 : 0);
        //ηζΊζ‘£δ½θε¨εΌε³οΌ0ε³οΌ1εΌοΌ  ηη
        mFanStatusComposite.IsLevelLinkage = (short) (cbStirFry.isChecked() ? 1 : 0);
        //η΅η£ηΆε³ζΊεηζΊε»ΆζΆε³ζΊεΌε³οΌ0ε³οΌ1εΌοΌ
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

        // ηζΊη΅η£ηΆεΌε³θε¨(0ε³οΌ1εΌ)
        sp.IsPowerLinkage = cbFanAutoSwitch.isChecked();
        //η΅η£ηΆε³ζΊεηζΊε»ΆζΆε³ζΊεΌε³οΌ0ε³οΌ1εΌοΌ
        sp.IsShutdownLinkage = cbDelay.isChecked();

        //ηζΊζ‘£δ½θε¨εΌε³οΌ0ε³οΌ1εΌοΌ  ηη
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
        //ε½εΌε―ηΆε·ζΆοΌηζΊθͺε¨εΌε―
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
                        LogUtils.i("20190928", "ζ§θ‘ζε");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20190928", t.getMessage());
                    }
                });

            }
        });


        //ε½ε³ι­ηΆε·ζΆοΌηζΊε»ΆζΆ ειε³ι­
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
                        LogUtils.i("20190928", "ζ§θ‘ζε");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20190928", t.getMessage());
                    }
                });

            }
        });

        //ηηζ¨‘εΌ
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
                        LogUtils.i("20190928", "ζ§θ‘ζε");
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
                //ζ―ε¦εΌε―ε?ζΆιι£[1BYTE]
                mFanStatusComposite.IsTimingVentilation = (short) (smartParams.IsTimingVentilation ? 1 : 0);
                // ζ²ΉηζΊζΈζ΄ζη€ΊεΌε³[1Byte], 0δΈζη€ΊοΌ1ζη€Ί
                mFanStatusComposite.IsNoticClean = (short) (smartParams.IsNoticClean ? 1 : 0);
                //ε?ζΆιι£ι΄ιζΆι΄[1BYTE],εδ½ε€©
                mFanStatusComposite.TimingVentilationPeriod = smartParams.TimingVentilationPeriod;
                //ηζΊη΅η£ηΆεΌε³θε¨(0ε³οΌ1εΌ)[1Byte]
                mFanStatusComposite.IsPowerLinkage = (short) (smartParams.IsPowerLinkage ? 1 : 0);
                //ηζΊζ‘£δ½θε¨εΌε³οΌ0ε³οΌ1εΌοΌ[1Byte]
                mFanStatusComposite.IsLevelLinkage = (short) (smartParams.IsLevelLinkage ? 1 : 0);
                //η΅η£ηΆε³ζΊεηζΊε»ΆζΆε³ζΊεΌε³οΌ0ε³οΌ1εΌοΌ[1Byte]
                mFanStatusComposite.IsShutdownLinkage = (short) (smartParams.IsShutdownLinkage ? 1 : 0);
                //η΅η£ηΆε³ζΊεηζΊε»ΆζΆε³ζΊζΆι΄οΌε»ΆζΆζΆι΄οΌεδ½ειοΌ1~5ειοΌ[1Byte]
                mFanStatusComposite.ShutdownDelay = smartParams.ShutdownDelay;
                //ζ―ε¦εΌε―ζ―ε¨ιι£[1BYTE]
                mFanStatusComposite.IsWeeklyVentilation = (short) (smartParams.IsWeeklyVentilation ? 1 : 0);
                //ζ―ε¨ιι£ηζΆι΄--ε¨ε 
                mFanStatusComposite.WeeklyVentilationDate_Week = smartParams.WeeklyVentilationDate_Week;
                //ζ―ε¨ιι£ηζΆι΄--ε°ζΆ
                mFanStatusComposite.WeeklyVentilationDate_Hour = smartParams.WeeklyVentilationDate_Hour;
                //ζ―ε¨ιι£ηζΆι΄--ει
                mFanStatusComposite.WeeklyVentilationDate_Minute = smartParams.WeeklyVentilationDate_Minute;
                refresh(mFanStatusComposite);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }


    //ε·ζ°
    private void refresh(FanStatusComposite fanStatusComposite) {

        if (fanStatusComposite == null) return;

        //ηηΆθε¨εΌε³
        if (fanStatusComposite.IsPowerLinkage == 1) {
            cbFanAutoSwitch.setChecked(true);
        } else {
            cbFanAutoSwitch.setChecked(false);
        }
        //ε»ΆζΆεΌε³
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
        //ηηεΌε³
        if (fanStatusComposite.IsLevelLinkage == 1) {
            cbStirFry.setChecked(true);
        } else {
            cbStirFry.setChecked(false);
        }
        //ε»ΆζΆζΆι΄
        tvDelayTime.setText(String.valueOf(fanStatusComposite.ShutdownDelay));

    }








    @Override
    public void onResume() {
        super.onResume();
        if (fan == null) {
            return;
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


        mRokiDialog.setCancelBtn("εζΆ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });

        mRokiDialog.setOkBtn("η‘?ε?", new View.OnClickListener() {
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
