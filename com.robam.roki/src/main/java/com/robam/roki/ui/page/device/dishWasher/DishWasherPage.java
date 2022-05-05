package com.robam.roki.ui.page.device.dishWasher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 洗碗机
 * 强力洗 智能洗 快速洗 晶亮洗 日常洗 节能洗
 */
public class DishWasherPage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @InjectView(R.id.ll_lower_wash)
    LinearLayout llLowerWash;
    @InjectView(R.id.tv_lower_wash)
    TextView tvLowerWash;
    @InjectView(R.id.cb_lower_wash)
    CheckBoxView cbLowerWash;
    @InjectView(R.id.tv_lower_wash_desc)
    TextView tvLowerWashDesc;

    @InjectView(R.id.ll_dry)
    LinearLayout llDry;
    @InjectView(R.id.tv_dry)
    TextView tvDry;
    @InjectView(R.id.cb_dry)
    CheckBoxView cbDry;
    @InjectView(R.id.tv_dry_desc)
    TextView tvDryDesc;

    @InjectView(R.id.ll_auto_ventilation)
    LinearLayout llAutoVentilation;
    @InjectView(R.id.tv_auto_ventilation)
    TextView tvAutoVentilation;
    @InjectView(R.id.cb_auto_ventilation)
    CheckBoxView cbAutoVentilation;
    @InjectView(R.id.tv_auto_ventilation_desc)
    TextView tvAutoVentilationDesc;

    @InjectView(R.id.tv_tips)
    TextView tvTips;

    @InjectView(R.id.tv_reservation)
    TextView tvReservation;
    @InjectView(R.id.tv_start_work)
    TextView tvStartWork;


    String guid, dt;
    DeviceConfigurationFunctions bean;
    AbsDishWasher absDishWasher;
    private List<Integer> hourList, minuteList;
    private String defaultHour, defaultMin, desc;
    String selectHour, selectMinute;
    private int model;
    private short lowerSwitch;
    private short ventilationSwitch;
    private short drySwitch;
    private String modeParams;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    selectHour = getNumeric((String) msg.obj);
                    break;
                case 2:
                    selectMinute = getNumeric((String) msg.obj);
                    break;
            }
        }
    };
    private String openString;
    private JSONArray types;
    private String text1, text2, text3, text4;
    private IRokiDialog mRokiDialog;


    public static String getNumeric(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        bean = bd == null ? null : (DeviceConfigurationFunctions) bd.getSerializable(PageArgumentKey.Bean);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        modeParams = bd == null ? null : bd.getString(PageArgumentKey.modeInfo);
        View view = inflater.inflate(R.layout.dish_wash_page, container, false);
        ButterKnife.inject(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        try {
            List<DeviceConfigurationFunctions> deviceConfigurationFunctions = bean
                    .subView
                    .subViewModelMap
                    .subViewModelMapSubView
                    .deviceConfigurationFunctions;

            String params = bean.functionParams;
            JSONObject obj = new JSONObject(params);
            model = obj.getInt("model");
            for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {

                //下层洗
                if (DishWasherName.underwash.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    String functionParams = deviceConfigurationFunctions.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    String title = jsonObject.getJSONObject("title").getString("value");
                    String desc = jsonObject.getJSONObject("desc").getString("value");
                    tvLowerWash.setText(title);
                    tvLowerWashDesc.setText(desc);

                    //加强干燥
                } else if (DishWasherName.drying.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    String functionParams = deviceConfigurationFunctions.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    String title = jsonObject.getJSONObject("title").getString("value");
                    String desc = jsonObject.getJSONObject("desc").getString("value");
                    tvDry.setText(title);
                    tvDryDesc.setText(desc);


                    //自动换气
                } else if (DishWasherName.autoVentilation.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    String functionParams = deviceConfigurationFunctions.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    String title = jsonObject.getJSONObject("title").getString("value");
                    String desc = jsonObject.getJSONObject("desc").getString("value");
                    tvAutoVentilation.setText(title);
                    tvAutoVentilationDesc.setText(desc);

                    //预计
                } else if (DishWasherName.estimate.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    String functionParams = deviceConfigurationFunctions.get(i).functionParams;
                    String[] buttons = functionParams.split("Button");
                    text1 = buttons[0];//预计用电量
                    text2 = buttons[1];//kw·h,最高水温
                    text3 = buttons[2];//℃,用水量
                    text4 = buttons[3];//L
                    //预约时间
                } else if (DishWasherName.appointment.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    String functionName = deviceConfigurationFunctions.get(i).functionName;
                    tvReservation.setText(functionName);
                    String functionParams = deviceConfigurationFunctions.get(i).functionParams;

                    JSONObject jsonObject = new JSONObject(functionParams);

                    defaultHour = jsonObject.getJSONObject("defalutHour").getString("value");
                    defaultMin = jsonObject.getJSONObject("defalutMin").getString("value");
                    LogUtils.i("20200427888", "defaultMin::" + defaultMin);
                    JSONArray hourJsonArray = jsonObject.getJSONObject("hour").getJSONArray("value");
                    JSONArray minuteJsonArray = jsonObject.getJSONObject("minute").getJSONArray("value");
                    desc = jsonObject.getJSONObject("desc").getString("value");
                    List<Integer> hours = new ArrayList<>();
                    List<Integer> minutes = new ArrayList<>();

                    for (int j = 0; j < hourJsonArray.length(); j++) {
                        Integer temp = (Integer) hourJsonArray.get(j);
                        hours.add(temp);
                    }
                    for (int j = 0; j < minuteJsonArray.length(); j++) {
                        Integer time = (Integer) minuteJsonArray.get(j);
                        minutes.add(time);
                    }
                    hourList = TestDatas.createModeDataTime(hours);
                    minuteList = TestDatas.createModeDataTime(minutes);

                    //开始工作
                } else if (DishWasherName.startWork.equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    String functionName = deviceConfigurationFunctions.get(i).functionName;
                    tvStartWork.setText(functionName);
                    String functionParams = deviceConfigurationFunctions.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    openString = jsonObject.getJSONObject("open").getString("value");

                }

            }
            cbLowerWash.setChecked(absDishWasher.LowerLayerWasher != 0);//下层洗开关
            if (cbLowerWash.isChecked()) {
                cbDry.setChecked(false);
            } else {
                cbDry.setChecked(true);
            }
            cbAutoVentilation.setChecked(absDishWasher.AutoVentilation != 0);//自动换气开关


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setDialogParam(List<Integer> hourList, List<Integer> minuteList, String defaultHour, String defaultMinute, String desc) {
        short newDefaultHour = (Short.parseShort(defaultHour));
        short newDefaultMinute = (Short.parseShort(defaultMinute));
        //拿到时间温度的索引值
        int indexHour = 0;
        int indexMinute = 0;
        for (int i = 0; i < hourList.size(); i++) {
            if (hourList.get(i) == newDefaultHour) {
                indexHour = i;
            }
        }
        for (int i = 0; i < minuteList.size(); i++) {
            if (minuteList.get(i) == newDefaultMinute) {
                indexMinute = i;
            }
        }


        LogUtils.i("20200427888", "minuteList::" + minuteList);
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_22);
        mRokiDialog.setOkBtn("开始工作", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (absDishWasher.DoorOpenState == (short) 1) {
                    tipsDialog();
                } else {
                    final int totalMinute;
                    Integer m = Integer.valueOf(selectMinute);
                    if (!"".equals(selectHour) && !"0".equals(selectHour)) {
                        Integer h = Integer.valueOf(selectHour);
                        totalMinute = h * 60 + m;
                    } else {
                        totalMinute = m;
                    }

                    if (absDishWasher.powerStatus == 0) {
                        absDishWasher.setDishWasherStatusControl((short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {

                                setDishWasherWorkMode((short) model, lowerSwitch, ventilationSwitch, drySwitch, (short) 1, (short) totalMinute);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    } else {
                        setDishWasherWorkMode((short) model, lowerSwitch, ventilationSwitch, drySwitch, (short) 1, (short) totalMinute);
                    }


                }


                mRokiDialog.dismiss();
            }
        });
        mRokiDialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });
        mRokiDialog.setWheelViewData(
                HelperRikaData.getList2StringHour(hourList),
                null,
                HelperRikaData.getList2StringMin(minuteList),
                desc,
                false,
                indexHour,
                0,
                indexMinute,
                new OnItemSelectedListenerFrone() {

                    @Override
                    public void onItemSelectedFront(String contentFront) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = contentFront;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }, null, new OnItemSelectedListenerRear() {

                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = contentRear;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }
                });

        mRokiDialog.show();


    }


    @OnClick({R.id.iv_back, R.id.tv_reservation, R.id.tv_start_work})
    public void onClickView(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            //预约时间
            case R.id.tv_reservation:
                if (childLock()) {
                    lowerSwitch = cbLowerWash.isChecked() ? (short) 1 : (short) 0;
                    ventilationSwitch = cbAutoVentilation.isChecked() ? (short) 1 : (short) 0;
                    drySwitch = cbDry.isChecked() ? (short) 1 : (short) 0;
                    setDialogParam(hourList, minuteList, defaultHour, defaultMin, desc);
                }

                break;
            //开始工作
            case R.id.tv_start_work:
                if (childLock()) {
                    lowerSwitch = cbLowerWash.isChecked() ? (short) 1 : (short) 0;
                    ventilationSwitch = cbAutoVentilation.isChecked() ? (short) 1 : (short) 0;
                    drySwitch = cbDry.isChecked() ? (short) 1 : (short) 0;

                    if (absDishWasher.DoorOpenState == (short) 1) {
                        tipsDialog();
                    } else {
                        setPowerSwitch();
                    }
                }
                break;
        }
    }

    double electricityLowerWashData = 0;
    double waterLowerWashData = 0;
    double temperatureLowerWashData = 0;
    double electricityDryData = 0;
    double waterDryData = 0;
    double temperatureDryData = 0;

    double electricityDefault = 0;
    double waterDefault = 0;
    double temperatureDefault = 0;

    private void initView() {
        absDishWasher = Plat.deviceService.lookupChild(guid);
        dt = absDishWasher.getDt();
        tvTitle.setText(title);

        if (!"".equals(modeParams)) {
            try {
                JSONObject jsonObject = new JSONObject(modeParams);
                JSONArray modelArray = jsonObject.getJSONArray("model");
                for (int i = 0; i < modelArray.length(); i++) {
                    JSONObject obj = modelArray.getJSONObject(i);
                    int mode = obj.getInt("value");
                    if (mode == model) {
                        types = obj.getJSONArray("types");
                    }
                }

                if (types != null) {
                    if (types.length() > 0) {
                        for (int j = 0; j < types.length(); j++) {
                            JSONObject typeObj = types.getJSONObject(j);
                            int type = typeObj.getJSONObject("type").getInt("value");
                            if (type == 1) {
                                llLowerWash.setVisibility(View.VISIBLE);
                                electricityLowerWashData = typeObj.getJSONObject("electricity").getDouble("value");
                                waterLowerWashData = typeObj.getJSONObject("water").getDouble("value");
                                temperatureLowerWashData = typeObj.getJSONObject("temperature").getDouble("value");
                            } else if (type == 2) {
                                electricityDefault = typeObj.getJSONObject("electricity").getDouble("value");
                                waterDefault = typeObj.getJSONObject("water").getDouble("value");
                                temperatureDefault = typeObj.getJSONObject("temperature").getDouble("value");
                            } else if (type == 3) {
                                llDry.setVisibility(View.VISIBLE);
                                electricityDryData = typeObj.getJSONObject("electricity").getDouble("value");
                                waterDryData = typeObj.getJSONObject("water").getDouble("value");
                                temperatureDryData = typeObj.getJSONObject("temperature").getDouble("value");
                            }

                        }


                    }
                } else {
                    tvTips.setVisibility(View.GONE);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        cbLowerWash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                double totalPower = 0;
                double totalWater = 0;
                double maxTemp = 0;
                if (isChecked) {
                    totalPower = totalPower + electricityLowerWashData;
                    totalWater = totalWater + waterLowerWashData;
                    if (temperatureLowerWashData > maxTemp) {
                        maxTemp = temperatureLowerWashData;
                    }

                    if (cbDry.isChecked()) {
                        cbDry.setChecked(false);
                    }

                }

                if (cbDry.isChecked()) {
                    totalPower = totalPower + electricityDryData;
                    totalWater = totalWater + waterDryData;
                    if (temperatureDryData > maxTemp) {
                        maxTemp = temperatureDryData;
                    }


                }

                if (totalPower == 0.0 && maxTemp == 0.0 && totalWater == 0.0) {
                    tvTips.setVisibility(View.VISIBLE);
                    tvTips.setText(text1 + electricityDefault + text2 + temperatureDefault + text3 + waterDefault + text4);
                } else {
                    tvTips.setVisibility(View.VISIBLE);
                    tvTips.setText(text1 + totalPower + text2 + maxTemp + text3 + totalWater + text4);
                }


            }
        });
        cbDry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                double totalPower = 0;
                double totalWater = 0;
                double maxTemp = 0;
                if (isChecked) {
                    totalPower = totalPower + electricityDryData;
                    totalWater = totalWater + waterDryData;
                    if (temperatureDryData > maxTemp) {
                        maxTemp = temperatureDryData;
                    }

                    if (cbLowerWash.isChecked()) {
                        cbLowerWash.setChecked(false);
                    }
                }

                if (cbLowerWash.isChecked()) {
                    totalPower = totalPower + electricityLowerWashData;
                    totalWater = totalWater + waterLowerWashData;
                    if (temperatureLowerWashData > maxTemp) {
                        maxTemp = temperatureLowerWashData;
                    }
                }

                if (totalPower == 0.0 && maxTemp == 0.0 && totalWater == 0.0) {
                    tvTips.setVisibility(View.VISIBLE);
                    tvTips.setText(text1 + electricityDefault + text2 + temperatureDefault + text3 + waterDefault + text4);
                } else {
                    tvTips.setVisibility(View.VISIBLE);
                    tvTips.setText(text1 + totalPower + text2 + maxTemp + text3 + totalWater + text4);
                }
            }
        });


        if (types != null) {
            if (!cbLowerWash.isChecked() && !cbDry.isChecked()) {
                tvTips.setVisibility(View.VISIBLE);
                tvTips.setText(text1 + electricityDefault + text2 + temperatureDefault + text3 + waterDefault + text4);
            }

            if (cbLowerWash.isChecked()) {
                tvTips.setVisibility(View.VISIBLE);
                tvTips.setText(text1 + electricityLowerWashData + text2 + temperatureLowerWashData + text3 + waterLowerWashData + text4);
            }
            if (cbDry.isChecked()) {
                tvTips.setVisibility(View.VISIBLE);
                tvTips.setText(text1 + electricityDryData + text2 + temperatureDryData + text3 + waterDryData + text4);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (mRokiDialog != null) {
            if (mRokiDialog.isShow()) {
                mRokiDialog.dismiss();
            }
        }


    }


    //设置洗碗机工作模式
    private void setDishWasherWorkMode(short workMode, short bottomWasherSwitch, short autoVentilation,
                                       short enhancedDrySwitch, short appointmentSwitch, short appointmentTime) {

        absDishWasher.setDishWasherWorkMode(workMode, bottomWasherSwitch, autoVentilation, enhancedDrySwitch,
                appointmentSwitch, appointmentTime, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("20200416", "success");
                        UIService.getInstance().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20200416", t.getMessage());
                        t.printStackTrace();

                    }
                });
    }

    @Subscribe
    public void onEvent(DisherWasherStatusChangeEvent event) {
        if (absDishWasher == null || !Objects.equal(absDishWasher.getID(), event.pojo.getID())) {
            return;
        }
        absDishWasher = event.pojo;
    }


    private void setPowerSwitch() {
        if (absDishWasher.powerStatus == 0) {
            absDishWasher.setDishWasherStatusControl((short) 1, new VoidCallback() {
                @Override
                public void onSuccess() {

                    setDishWasherWorkMode((short) model, lowerSwitch, ventilationSwitch, drySwitch, (short) 0, (short) 0);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            setDishWasherWorkMode((short) model, lowerSwitch, ventilationSwitch, drySwitch, (short) 0, (short) 0);
        }

    }

    IRokiDialog tipsDialog = null;

    private void tipsDialog() {
        tipsDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_16);
        tipsDialog.setTitleText(R.string.wifi_network_toast);
        tipsDialog.setContentText(openString);
        tipsDialog.show();
        tipsDialog.setOkBtn(R.string.i_know, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipsDialog.isShow()) {
                    tipsDialog.dismiss();
                }
            }
        });

    }


    private boolean childLock() {
        if (absDishWasher.StoveLock == 1) {
            ToastUtils.show("请先到机器端解除童锁", Toast.LENGTH_LONG);
            return false;
        } else {
            return true;
        }
    }


}
