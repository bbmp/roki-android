package com.robam.roki.ui.page.device.stove;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.bean.StoveBurnWithoutWaterParams;
import com.robam.roki.model.helper.HelperStoveData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.page.device.AbsDeviceBasePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/5/29.
 * 灶具防干烧
 */

public class StoveBurnWithoutWaterPage extends AbsDeviceBasePage {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.setting)
    TextView mSetting;
    @InjectView(R.id.stove_tip)
    TextView mStoveTip;
    @InjectView(R.id.tv_desc)
    TextView mTvDesc;
    @InjectView(R.id.stove_check)
    CheckBoxView mStoveCheck;
    @InjectView(R.id.toggle)
    LinearLayout mToggle;
    @InjectView(R.id.line)
    View mLine;
    @InjectView(R.id.total_setting)
    LinearLayout mTotalSetting;
    @InjectView(R.id.stove_return)
    ImageView mStoveReturn;
    @InjectView(R.id.tv_gear_fire_five)
    TextView mTvGearFireFive;
    @InjectView(R.id.tv_setting_time_five)
    TextView mTvSettingTimeFive;
    @InjectView(R.id.tv_min_off_fire_five)
    TextView mTvMinOffFireFive;
    @InjectView(R.id.tv_setting_click_five)
    TextView mTvSettingClickFive;
    @InjectView(R.id.tv_gear_fire_four)
    TextView mTvGearFireFour;
    @InjectView(R.id.tv_setting_time_four)
    TextView mTvSettingTimeFour;
    @InjectView(R.id.tv_min_off_fire_four)
    TextView mTvMinOffFireFour;
    @InjectView(R.id.tv_setting_click_four)
    TextView mTvSettingClickFour;
    @InjectView(R.id.tv_gear_fire_three)
    TextView mTvGearFireThree;
    @InjectView(R.id.tv_setting_time_three)
    TextView mTvSettingTimeThree;
    @InjectView(R.id.tv_min_off_fire_three)
    TextView mTvMinOffFireThree;
    @InjectView(R.id.tv_setting_click_three)
    TextView mTvSettingClickThree;
    @InjectView(R.id.tv_gear_fire_two)
    TextView mTvGearFireTwo;
    @InjectView(R.id.tv_setting_time_two)
    TextView mTvSettingTimeTwo;
    @InjectView(R.id.tv_min_off_fire_two)
    TextView mTvMinOffFireTwo;
    @InjectView(R.id.tv_setting_click_two)
    TextView mTvSettingClickTwo;
    @InjectView(R.id.tv_gear_fire_one)
    TextView mTvGearFireOne;
    @InjectView(R.id.tv_setting_time_one)
    TextView mTvSettingTimeOne;
    @InjectView(R.id.tv_min_off_fire_one)
    TextView mTvMinOffFireOne;
    @InjectView(R.id.tv_setting_click_one)
    TextView mTvSettingClickOne;
    private IRokiDialog mTimingDialog;
    private Stove stove;
    private String mThisTitle;
    private String mTipText;
    private List<DeviceConfigurationFunctions> mConfigurationFunctionsList;
    private short timeForOne;
    private short timeForTwo;
    private short timeForThree;
    private short timeForFour;
    private short timeForFive;
    private short power;
    private List<StoveBurnWithoutWaterParams.ParamBean.GearsBean> mGearsList;
    private String mDesc;
    private List<String> mTimeData;
    public static final short OFF = 0;
    public static final short ON = 1;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setTimeData((String) msg.obj, msg.arg1);
                    break;
                case 2:
                    updateUi();
                    break;
                case 3:
                    updateOffOrOn();
                    break;
            }
        }

    };
    private List<Integer> mTimeSection;

    private void updateOffOrOn() {
        if (power == 0) {
            mStoveCheck.setChecked(false);
        } else {
            mStoveCheck.setChecked(true);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        stove = bd == null ? null : (Stove) bd.getSerializable(PageArgumentKey.Bean);
        mThisTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        mConfigurationFunctionsList = (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.stove_burn_without_water_page;
    }

    @Override
    protected void initWidget(View root) {
        ButterKnife.inject(this, root);
        getTimeForStove();
    }

    @Override
    protected void initData() {
        if (mConfigurationFunctionsList == null || mConfigurationFunctionsList.size() == 0) return;
        for (int i = 0; i < mConfigurationFunctionsList.size(); i++) {
            if ("dryBurn".equals(mConfigurationFunctionsList.get(i).functionCode)) {
                mTipText = mConfigurationFunctionsList.get(i).functionName;
                String functionParams = mConfigurationFunctionsList.get(i).functionParams;
                LogUtils.i("20180720", " functionParams:" + functionParams);
                try {
                    StoveBurnWithoutWaterParams stoveBurnWithoutWaterParams = JsonUtils.json2Pojo(functionParams, StoveBurnWithoutWaterParams.class);
                    mGearsList = stoveBurnWithoutWaterParams.getParam().getGears();
                    mDesc = stoveBurnWithoutWaterParams.getParam().getDesc().getValue();
                    mTimeSection = stoveBurnWithoutWaterParams.getParam().getMinute().getValue();
                    for (int j = 0; j < mGearsList.size(); j++) {
                        int gear = mGearsList.get(j).getGear();
                        String title = mGearsList.get(j).getTitle();
                        String minuteDefault = mGearsList.get(j).getMinuteDefault();
                        String btnText = mGearsList.get(j).getButton();
                        switch (gear) {
                            case 5:
                                subString(gear, title);
                                mTvSettingTimeFive.setText(minuteDefault);
                                mTvSettingClickFive.setText(btnText);
                                break;
                            case 4:
                                subString(gear, title);
                                mTvSettingTimeFour.setText(minuteDefault);
                                mTvSettingClickFour.setText(btnText);
                                break;
                            case 3:
                                subString(gear, title);
                                mTvSettingTimeThree.setText(minuteDefault);
                                mTvSettingClickThree.setText(btnText);
                                break;
                            case 2:
                                subString(gear, title);
                                mTvSettingTimeTwo.setText(minuteDefault);
                                mTvSettingClickTwo.setText(btnText);
                                break;
                            case 1:
                                subString(gear, title);
                                mTvSettingTimeOne.setText(minuteDefault);
                                mTvSettingClickOne.setText(btnText);
                                break;
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void subString(int gear, String value) {
        String[] strings = value.split("button");
        for (int i = 0; i < strings.length; i++) {
            if (0 == i) {
                switch (gear) {
                    case 5:
                        mTvGearFireFive.setText(strings[i]);
                        break;
                    case 4:
                        mTvGearFireFour.setText(strings[i]);
                        break;
                    case 3:
                        mTvGearFireThree.setText(strings[i]);
                        break;
                    case 2:
                        mTvGearFireTwo.setText(strings[i]);
                        break;
                    case 1:
                        mTvGearFireOne.setText(strings[i]);
                        break;
                    default:
                        break;
                }

            } else if (1 == i) {
                switch (gear) {
                    case 5:
                        mTvMinOffFireFive.setText(strings[i]);
                        break;
                    case 4:
                        mTvMinOffFireFour.setText(strings[i]);
                        break;
                    case 3:
                        mTvMinOffFireThree.setText(strings[i]);
                        break;
                    case 2:
                        mTvMinOffFireTwo.setText(strings[i]);
                        break;
                    case 1:
                        mTvMinOffFireOne.setText(strings[i]);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    private void getTimeForStove() {
        if (stove == null) {
            return;
        }
        stove.setAutoPowerOffLook(new VoidCallback() {
            @Override
            public void onSuccess() {
                power = stove.power;
                timeForOne = stove.AutoPowerOffOne;
                timeForTwo = stove.AutoPowerOffTwo;
                timeForThree = stove.AutoPowerOffThree;
                timeForFour = stove.AutoPowerOffFour;
                timeForFive = stove.AutoPowerOffFive;
                //成功后在更新UI
                Message message = mHandler.obtainMessage();
                message.what = 2;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(R.string.device_Failure_text, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stove==null) {
            return;
        }
        if (stove.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), stove.getDt() + ":防干烧设定页", null);
        }
    }

    private void updateUi() {
        mTitle.setText(mThisTitle);
        mStoveTip.setText(mTipText);
        mTvDesc.setText(mDesc);
        mStoveCheck.setChecked(power == 1);
        if (power == 1) {
            mTotalSetting.setVisibility(View.VISIBLE);
        } else {
            mTotalSetting.setVisibility(View.GONE);
        }
        mTvSettingTimeFive.setText(timeForFive + "");
        mTvSettingTimeFour.setText(timeForFour + "");
        mTvSettingTimeThree.setText(timeForThree + "");
        mTvSettingTimeTwo.setText(timeForTwo + "");
        mTvSettingTimeOne.setText(timeForOne + "");

    }

    @OnClick({R.id.stove_return, R.id.stove_check, R.id.tv_setting_click_five, R.id.tv_setting_click_four,
            R.id.tv_setting_click_three, R.id.tv_setting_click_two, R.id.tv_setting_click_one})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stove_return:
                UIService.getInstance().popBack();
                break;
            case R.id.stove_check:
                if (mStoveCheck.isChecked()) {
                    sendOffOrOnCommand(ON, (short) 0, cx.getString(R.string.device_without_setting_open));

                    if (stove!=null) {

                        ToolUtils.logEvent(stove.getDt(), "防干烧设定:开", "roki_设备");
                    }


                } else {
                    sendOffOrOnCommand(OFF, (short) 0, cx.getString(R.string.device_without_setting_colo));

                    if (stove!=null) {
                        ToolUtils.logEvent(stove.getDt(), "防干烧设定:关", "roki_设备");
                    }


                }
                break;
            case R.id.tv_setting_click_five:
                for (int i = 0; i < mGearsList.size(); i++) {
                    if (5 == mGearsList.get(i).getGear()) {
                        wheelViewMethod(mGearsList.get(i).getGear(), Integer.parseInt(mGearsList.get(i).getMinuteDefault()) - 1);
                    }
                }
                break;
            case R.id.tv_setting_click_four:
                for (int i = 0; i < mGearsList.size(); i++) {
                    if (4 == mGearsList.get(i).getGear()) {
                        wheelViewMethod(mGearsList.get(i).getGear(), Integer.parseInt(mGearsList.get(i).getMinuteDefault()) - 1);
                    }
                }
                break;
            case R.id.tv_setting_click_three:
                for (int i = 0; i < mGearsList.size(); i++) {
                    if (3 == mGearsList.get(i).getGear()) {
                        wheelViewMethod(mGearsList.get(i).getGear(), Integer.parseInt(mGearsList.get(i).getMinuteDefault()) - 1);
                    }
                }
                break;
            case R.id.tv_setting_click_two:
                for (int i = 0; i < mGearsList.size(); i++) {
                    if (2 == mGearsList.get(i).getGear()) {
                        wheelViewMethod(mGearsList.get(i).getGear(), Integer.parseInt(mGearsList.get(i).getMinuteDefault()) - 1);
                    }
                }
                break;
            case R.id.tv_setting_click_one:
                for (int i = 0; i < mGearsList.size(); i++) {
                    if (1 == mGearsList.get(i).getGear()) {
                        wheelViewMethod(mGearsList.get(i).getGear(), Integer.parseInt(mGearsList.get(i).getMinuteDefault()) - 1);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void sendOffOrOnCommand(final short power, short argument, final String str) {
        stove.setStoveAutoPowerOff(power, argument, new VoidCallback() {
            @Override
            public void onSuccess() {

                ToastUtils.show(str, Toast.LENGTH_SHORT);
                if (power == 0) {
                    mTotalSetting.setVisibility(View.GONE);
                } else {
                    mTotalSetting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(R.string.device_without_setting_failed, Toast.LENGTH_SHORT);
                Message msg = Message.obtain();
                msg.what = 3;
                mHandler.sendMessage(msg);

            }
        });
    }

    private void wheelViewMethod(final int gear, int defaultIndex) {

        if (mTimingDialog != null) {
            mTimingDialog = null;
        }
        if (mTimingDialog == null) {
            mTimingDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_03);
        }
        mTimingDialog.setWheelViewData(null, HelperStoveData.getTimeData(mTimeSection), null, false, 0, defaultIndex, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                LogUtils.i("20180702", "contentCenter:" + contentCenter);
                Message message = mHandler.obtainMessage();
                message.what = 1;
                message.arg1 = gear;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        }, null);
        mTimingDialog.show();
    }

    private void setTimeData(String data, final int gear) {
        String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        final int time = Integer.parseInt(removeString);
        mTimingDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimingDialog != null && mTimingDialog.isShow()) {
                    mTimingDialog.dismiss();
                }
            }
        });

        mTimingDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimingDialog.dismiss();
                sendPowerOffTimeCommand((short) gear, (short) time);
            }
        });
    }

    private void sendPowerOffTimeCommand(final short gear, final short time) {
        stove.setStoveAutoPowerOffTime(gear, time, new VoidCallback() {
            @Override
            public void onSuccess() {
                updateTime(gear, time);
                ToastUtils.show(R.string.device_setting_success, Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(R.string.device_setting_failed, Toast.LENGTH_SHORT);
            }
        });
    }

    private void updateTime(short gear, short time) {

        switch (gear) {
            case 5:
                mTvSettingTimeFive.setText(time + "");
                break;
            case 4:
                mTvSettingTimeFour.setText(time + "");
                break;
            case 3:
                mTvSettingTimeThree.setText(time + "");
                break;
            case 2:
                mTvSettingTimeTwo.setText(time + "");
                break;
            case 1:
                mTvSettingTimeOne.setText(time + "");
                break;
            default:
                break;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
