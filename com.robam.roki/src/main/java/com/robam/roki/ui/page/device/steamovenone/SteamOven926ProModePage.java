package com.robam.roki.ui.page.device.steamovenone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.SteamOvenOneProModeAdapter;
import com.robam.roki.ui.dialog.AbsOvenExpDialog;
import com.robam.roki.ui.page.device.oven.MyGridView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 专业模式
 * CQ915、CQ908蒸烤一体机
 */
public class SteamOven926ProModePage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.mgv_steam_mode)
    MyGridView mgvSteamMode;
    @InjectView(R.id.mgv_roast_mode)
    MyGridView mgvRoastMode;
    @InjectView(R.id.mgv_assist_mode)
    MyGridView mgvAssistMode;
    @InjectView(R.id.mgv_fry_mode)
    MyGridView mgvFryMode;
    String guid;
    AbsSteameOvenOne steameOvenOne;

    List<DeviceConfigurationFunctions> mDatas;
    private List<DeviceConfigurationFunctions> steamList;
    private List<DeviceConfigurationFunctions> roastList;
    private List<DeviceConfigurationFunctions> fzList;
    private List<DeviceConfigurationFunctions> fryList;
    public HashMap<String, DeviceConfigurationFunctions> paramMap = new HashMap<>();
    private long userId;
    private String selectTemp;
    private String selectTime;
    private String model;
    private String needDescalingParams;
    private String currentCode;
    private IRokiDialog descalingTipsDialog;
    private String imgType;
    private IRokiDialog dialogByType;
    private IRokiDialog dialogByType1;
    private IRokiDialog mRokiDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_pro_mode, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        imgType = bd == null ? null : bd.getString(PageArgumentKey.imgType);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        userId = Plat.accountService.getCurrentUserId();
        initData();
        return view;
    }


    private void initData() {
        tvTitle.setText(title);

        for (int i = 0; i < mDatas.size(); i++) {
            //蒸模式
            if (mDatas.get(i).functionCode.equals(SteamOvenModeName.steamModel)) {
                steamList = mDatas.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                //烤模式
            } else if (mDatas.get(i).functionCode.equals(SteamOvenModeName.roast)) {
                roastList = mDatas.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;

                //辅助模式
            } else if (mDatas.get(i).functionCode.equals(SteamOvenModeName.fz)) {
                fzList = mDatas.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            } else if (mDatas.get(i).functionCode.equals(SteamOvenModeName.fry)) {
                fryList = mDatas.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            }
        }

        setParamMap();
        SteamOvenOneProModeAdapter steamAdapter = new SteamOvenOneProModeAdapter(cx, steamList, steameOvenOne.getDt(), imgType);
        SteamOvenOneProModeAdapter roastAdapter = new SteamOvenOneProModeAdapter(cx, roastList, steameOvenOne.getDt(), imgType);
        SteamOvenOneProModeAdapter fzAdapter = new SteamOvenOneProModeAdapter(cx, fzList, steameOvenOne.getDt(), imgType);
        SteamOvenOneProModeAdapter fryAdapter = new SteamOvenOneProModeAdapter(cx, fryList, steameOvenOne.getDt(), imgType);
        mgvSteamMode.setAdapter(steamAdapter);
        mgvRoastMode.setAdapter(roastAdapter);
        mgvAssistMode.setAdapter(fzAdapter);
        mgvFryMode.setAdapter(fryAdapter);

        mgvSteamMode.setVerticalSpacing(50);
        mgvRoastMode.setVerticalSpacing(50);
        mgvAssistMode.setVerticalSpacing(50);
        mgvFryMode.setVerticalSpacing(50);
        mgvSteamMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                    ToastUtils.showShort(R.string.device_alarm_close_content);
                    return;
                }
                view.setTag(steamList.get(position).functionCode);
                String msg = steamList.get(position).msg;
                currentCode = steamList.get(position).functionCode;
                setOnClickRespon((String) view.getTag(), SteamOvenModeName.steamModel, msg);
            }
        });
        mgvRoastMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                    ToastUtils.showShort(R.string.device_alarm_close_content);
                    return;
                }
                view.setTag(roastList.get(position).functionCode);
                String msg = roastList.get(position).msg;
                currentCode = roastList.get(position).functionCode;
                setOnClickRespon((String) view.getTag(), SteamOvenModeName.roast, msg);
            }
        });
        mgvAssistMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                    ToastUtils.showShort(R.string.device_alarm_close_content);
                    return;
                }
                view.setTag(fzList.get(position).functionCode);
                String msg = fzList.get(position).msg;
                currentCode = fzList.get(position).functionCode;
                setOnClickRespon((String) view.getTag(), SteamOvenModeName.fz, msg);

            }
        });
        mgvFryMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                    ToastUtils.showShort(R.string.device_alarm_close_content);
                    return;
                }
                view.setTag(fryList.get(position).functionCode);
                String msg = fryList.get(position).msg;
                currentCode = fryList.get(position).functionCode;
                setOnClickRespon((String) view.getTag(), SteamOvenModeName.fry, msg);
            }
        });
    }

    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, guid, code, steameOvenOne.getDc(), new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
//                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setParamMap() {
        for (int i = 0; i < steamList.size(); i++) {
            paramMap.put(steamList.get(i).functionCode, steamList.get(i));
        }
        for (int i = 0; i < roastList.size(); i++) {
            paramMap.put(roastList.get(i).functionCode, roastList.get(i));
        }
        for (int i = 0; i < fzList.size(); i++) {
            paramMap.put(fzList.get(i).functionCode, fzList.get(i));
        }
        for (int i = 0; i < fryList.size(); i++) {
            paramMap.put(fryList.get(i).functionCode, fryList.get(i));
        }
    }

    private void setOnClickRespon(String pos, String tag, String desc) {
        paramShow(pos, tag, desc);

    }

    boolean allTimeUseTag;

    private void paramShow(String code, String tag, String desc) {
        try {

            String functionParams = paramMap.get(code).functionParams;
            LogUtils.i("20200602","functionParams:::"+functionParams);
            JSONObject jsonObject = new JSONObject(functionParams);
            JSONObject param = jsonObject.optJSONObject("param");
            model = param.optJSONObject("model").optString("value");
            LogUtils.i("20200602","model:::"+model);
            switch (tag) {
                    //蒸模式
                case SteamOvenModeName.steamModel:
                    //烤模式
                case SteamOvenModeName.roast:
                    //辅助模式
                case SteamOvenModeName.fz:
                case SteamOvenModeName.fry:
                    boolean isSpecial = param.optBoolean("isSpecial");
                    String mode = param.optJSONObject("model").optString("value");

                    if ("".equals(param.optString("allTimeUse"))) {
                        allTimeUseTag = false;
                    } else {
                        allTimeUseTag = true;
                    }
                    if (steameOvenOne.doorStatusValue == 1 && !TextUtils.equals(mode, "19")) {
                        ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (isSpecial) {

                        JSONObject defaultTips = param.optJSONObject("defaultTips");
                        String title = defaultTips.optString("title");
                        String content = defaultTips.optString("content");
                        String positiveButton = defaultTips.optString("positiveButton");
                        String negativeButton = defaultTips.optString("negativeButton");
                        String defaultSetTemp = param.optJSONObject("defaultSetTemp").optString("value");
                        String defaultSetTime = param.optJSONObject("defaultSetTime").optString("value");
                        //干燥
                        if ("19".equals(mode)) {
                            dryTipsDialog(title, content, positiveButton, negativeButton, mode, defaultSetTime, defaultSetTemp);
                            //除垢
                        } else if ("21".equals(mode)) {
                            descalingTipsDialog(title, content, positiveButton, negativeButton);
                        }


                    } else {
                        //EXP
                        if ("10".equals(mode)) {
                            OvenExpParamBean ovenParams = JsonUtils.json2Pojo(functionParams, OvenExpParamBean.class);
                            if (ovenParams != null) {
                                List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
                                List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
                                List<Integer> time = ovenParams.getParam().getMinute().getValue();


                                String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();

                                String timeDefault = ovenParams.getParam().getMinuteDefault().getValue();

                                String tempDiff = ovenParams.getParam().getTempDiff().getValue();
                                String tempStart = ovenParams.getParam().getTempStart().getValue();
                                String downMin = ovenParams.getParam().getTempMin().getValue();
                                String descTips = ovenParams.getParam().getDesc().getValue();

                                List<Integer> tempUpList = TestDatas.createTempertureDatas(tempUp);
                                List<Integer> timeList = TestDatas.createModeDataTime(time);

                                int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
                                int deDiff = Integer.parseInt(tempDiff);
                                int deNum2 = deNum1 - deDiff;
                                int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
                                int deStart = Integer.parseInt(tempStart);
                                int min = Integer.parseInt(downMin);

                                //0 不需要 1 需要
                                if (steameOvenOne.weatherDescalingValue != 0) {
                                    if (allTimeUseTag) {
                                        setEXPDialogParam(tempUpList, tempDown, timeList, "℃", "℃", "分钟", descTips, deNum1, deNum2, deNum3, deDiff, deStart, min);
                                    } else {
                                        descalingDialog();
                                    }
                                } else {
                                    setEXPDialogParam(tempUpList, tempDown, timeList, "℃", "℃", "分钟", descTips, deNum1, deNum2, deNum3, deDiff, deStart, min);
                                }

                            }

                        } else {
                            JSONArray setTemp = param.optJSONObject("setTemp").optJSONArray("value");
                            JSONArray setTime = param.optJSONObject("setTime").optJSONArray("value");
                            String defaultSetTemp = param.optJSONObject("defaultSetTemp").optString("value");
                            String defaultSetTime = param.optJSONObject("defaultSetTime").optString("value");
                            List<Integer> tempList = new ArrayList<>();
                            for (int i = 0; i < setTemp.length(); i++) {
                                Integer temp = (Integer) setTemp.get(i);
                                tempList.add(temp);
                            }
                            List<Integer> timeList = new ArrayList<>();
                            for (int i = 0; i < setTime.length(); i++) {
                                Integer time = (Integer) setTime.get(i);
                                timeList.add(time);
                            }
                            List<Integer> tempTure = TestDatas.createModeDataTemp(tempList);
                            List<Integer> timeTemp = TestDatas.createModeDataTime(timeList);
                            //0 不需要 1 需要
                            if (steameOvenOne.weatherDescalingValue != 0) {
                                if (allTimeUseTag) {
                                    setDialogParam(tempTure, timeTemp, defaultSetTemp, defaultSetTime, desc, timeList.get(2));
                                } else {
                                    descalingDialog();
                                }

                            } else {
                                setDialogParam(tempTure, timeTemp, defaultSetTemp, defaultSetTime, desc, timeList.get(2));
                            }

                        }

                    }


                    break;
            }


        } catch (Exception e) {
            LogUtils.i("20200515", e.getMessage());
            e.printStackTrace();
        }


    }


    protected AbsOvenExpDialog absOvenExpDialog;

    private void setEXPDialogParam(List<Integer> tempUp, List<Integer> tempDown, List<Integer> time,
                                   String str1, String str2, String str3, String str4, int defNum1,
                                   int defNum2, int defNum3, int stepC, int defaultValue, int min) {
        List<String> listButton = TestDatas.createExpDialogText(str1, str2, str3, str4);
        absOvenExpDialog = new AbsOvenExpDialog(cx, tempUp, tempDown, time, listButton, defNum1, defNum2, defNum3, stepC, defaultValue, min);
        absOvenExpDialog.showDialog(absOvenExpDialog);
        absOvenExpDialog.setListener(new AbsOvenExpDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int index1, int index2, int index3) {
                sendExpCommand(model, index3, index1, index2);
            }
        });
    }

    private void descalingDialog() {
        String descalingTitle = null;
        String descalingContent = null;
        String descalingButton = null;
        try {
            if (!"".equals(needDescalingParams)) {
                JSONObject jsonObject = new JSONObject(needDescalingParams);
                JSONObject needDescaling = jsonObject.getJSONObject("needDescaling");
                descalingTitle = needDescaling.getString("title");
                descalingContent = needDescaling.getString("content");
                descalingButton = needDescaling.getString("positiveButton");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        descalingTipsDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        descalingTipsDialog.setTitleText(descalingTitle);
        descalingTipsDialog.setContentText(descalingContent);
        descalingTipsDialog.setOkBtn(descalingButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descalingTipsDialog != null) {
                    descalingTipsDialog.dismiss();
                }
            }
        });
        descalingTipsDialog.show();


    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String temp = (String) msg.obj;
                    selectTemp = getNumData(temp);
                    LogUtils.i("20200525", "selectTemp::" + temp);
                    break;
                case 2:
                    String time = (String) msg.obj;
                    selectTime = getNumData(time);
                    LogUtils.i("20200525", "selectTime::" + time);
                    break;
                default:
                    break;
            }

        }
    };


    //从字符中提取数字
    private String getNumData(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    private void setDialogParam(List<Integer> tempList, List<Integer> timeList, String defaultTemp, String defaultTime, String desc, int count) {
        short newDefaultTemp = (Short.parseShort(defaultTemp));
        short newDefaultTime = (Short.parseShort(defaultTime));

        //拿到时间温度的索引值
        int indexTemp = newDefaultTemp - tempList.get(0);
        int indexTime = newDefaultTime - timeList.get(0);
        if (timeList.size() > 1) {
            indexTime = indexTime / (timeList.get(1) - timeList.get(0));
        }
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_17);
        mRokiDialog.setWheelViewData(
                HelperRikaData.getTempData2(tempList),
                null,
                HelperRikaData.getTimeData3(timeList, count),
                desc,
                false,
                indexTemp,
                0,
                indexTime,
                new OnItemSelectedListenerFrone() {
                    @Override
                    public void onItemSelectedFront(String contentFront) {

                        Message msg = mHandler.obtainMessage();
                        msg.obj = contentFront;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                },
                null,
                new OnItemSelectedListenerRear() {
                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = contentRear;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }
                }
        );


        mRokiDialog.setOkBtn("开始工作", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSteamOvenFZModel();
                mRokiDialog.dismiss();
            }
        });
        mRokiDialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });
        mRokiDialog.setCanceledOnTouchOutside(true);
        mRokiDialog.show();


    }


    //下发干燥模式前的提示框
    private void dryTipsDialog(String title, String content, String buttonText, String buttonText2, final String mode, final String defaultTime, final String defaultTemp) {
        dialogByType1 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        dialogByType1.setTitleText(title);
        dialogByType1.setContentText(content);
        dialogByType1.setCancelBtn(buttonText2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType1.dismiss();
            }
        });
        dialogByType1.setOkBtn(buttonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dryMode(mode, defaultTime, defaultTemp);
                dialogByType1.dismiss();
            }
        });
        dialogByType1.show();

    }

    //下发除垢模式前的提示框
    private void descalingTipsDialog(String title, String content, String positiveButton, String negative) {
        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        dialogByType.setTitleText(title);
        dialogByType.setContentText(content);
        dialogByType.setCancelBtn(negative, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
            }
        });
        dialogByType.setOkBtn(positiveButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
            }
        });
        dialogByType.show();
    }


    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }

    //蒸、烤、辅助模式
    private void sendSteamOvenFZModel() {
        if (steameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerState == SteamOvenOnePowerStatus.Wait) {
            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), Short.parseShort(selectTime), Short.parseShort(selectTemp), (short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("202005250444","success");
                                    sendMul(currentCode);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("202005250444",t.getMessage());
                                }
                            });
                        }
                    }, 500);

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("202005250444",t.getMessage());
                }
            });
        } else {
            steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), Short.parseShort(selectTime), Short.parseShort(selectTemp), (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("202005250444","success");
                    sendMul(currentCode);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("202005250444",t.getMessage());
                }
            });
        }
    }

    //CQ908专业模式
    private void sendExpCommand(final String model, final int time, final int tempUp, final int tempDown) {

        if (steameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerState == SteamOvenOnePowerStatus.Wait
        ) {

            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), (short) time, (short) tempUp, (short) 0, (short) tempDown, (short) 255, (short) 255, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("20200602","onSuccess:::");
                                    sendMul(currentCode);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20200602","error:::"+t.getMessage());
                                }
                            });

                        }
                    }, 500);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200602","error:::"+t.getMessage());
                }
            });

        } else {
            steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), (short) time, (short) tempUp, (short) 0, (short) tempDown, (short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("20200602","onSuccess:::");
                    sendMul(currentCode);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200602","error:::"+t.getMessage());
                }
            });

        }


    }

    /**
     * 干燥模式
     */
    private void dryMode(final String mode, final String defaultTime, final String defaultTemp) {
        if (steameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerState == SteamOvenOnePowerStatus.Wait
        ) {
            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dryModeCommand(mode, defaultTime, defaultTemp);
                        }
                    }, 500);

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            dryModeCommand(mode, defaultTime, defaultTemp);

        }
    }

    private void dryModeCommand(String mode, String defaultTime, String defaultTemp) {
        steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(mode), Short.parseShort(defaultTime), Short.parseShort(defaultTemp), (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                sendMul(currentCode);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (descalingTipsDialog != null) {
            descalingTipsDialog.dismiss();
        }

        if (absOvenExpDialog != null) {
            absOvenExpDialog.dismiss();
        }

        if (dialogByType != null) {
            dialogByType.dismiss();
        }

        if (dialogByType1 != null) {
            dialogByType1.dismiss();
        }

        if (mRokiDialog != null) {
            mRokiDialog.dismiss();
        }
    }

    //开门事件
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.pojo.getID()))
            return;
        steameOvenOne = (AbsSteameOvenOne) event.pojo;
    }


}
