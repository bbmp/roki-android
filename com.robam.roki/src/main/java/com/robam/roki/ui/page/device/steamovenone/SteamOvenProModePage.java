package com.robam.roki.ui.page.device.steamovenone;

import android.annotation.SuppressLint;
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
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenModeName;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.OvenGrid035Apater;
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
 * c915蒸烤一体机
 */
public class SteamOvenProModePage extends BasePage {
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
    String guid;
    AbsSteameOvenOne steameOvenOne;

    List<DeviceConfigurationFunctions> mDatas;
    private List<DeviceConfigurationFunctions> steamList;
    private List<DeviceConfigurationFunctions> roastList;
    private List<DeviceConfigurationFunctions> fzList;
    public HashMap<String, DeviceConfigurationFunctions> paramMap = new HashMap<>();
    private long userId;
    private String selectTemp;
    private String selectTime;
    private String model;
    private String needDescalingParams;
    private IRokiDialog descalingTipsDialog;
    private IRokiDialog mRokiDialogPro;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_pro_mode, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        userId = Plat.accountService.getCurrentUserId();
        initData();
        return view;
    }

    public void closeProDialog() {
        if (mRokiDialogPro != null && mRokiDialogPro.isShow()) {
            mRokiDialogPro.dismiss();
        }
    }

    String functionCode;

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
            }
        }

        setParamMap();
        OvenGrid035Apater steamAdapter = new OvenGrid035Apater(cx, steamList, steameOvenOne.getDt());
        OvenGrid035Apater roastAdapter = new OvenGrid035Apater(cx, roastList, steameOvenOne.getDt());
        OvenGrid035Apater fzAdapter = new OvenGrid035Apater(cx, fzList, steameOvenOne.getDt());
        mgvSteamMode.setAdapter(steamAdapter);
        mgvRoastMode.setAdapter(roastAdapter);
        mgvAssistMode.setAdapter(fzAdapter);

        mgvSteamMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setTag(steamList.get(position).functionCode);
                String msg = steamList.get(position).msg;
                setOnClickRespon((String) view.getTag(), SteamOvenModeName.steamModel, msg);
                 functionCode = steamList.get(position).functionCode;
                //sendMul(steamList.get(position).functionCode);

            }
        });
        mgvRoastMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setTag(roastList.get(position).functionCode);
                String msg = roastList.get(position).msg;
                setOnClickRespon((String) view.getTag(), SteamOvenModeName.roast, msg);
                 functionCode = roastList.get(position).functionCode;
                //sendMul(roastList.get(position).functionCode);

            }
        });
        mgvAssistMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setTag(fzList.get(position).functionCode);
                String msg = fzList.get(position).msg;
                setOnClickRespon((String) view.getTag(), SteamOvenModeName.fz, msg);
                 functionCode = fzList.get(position).functionCode;
                //sendMul(fzList.get(position).functionCode);

            }
        });
    }

    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, guid, code, steameOvenOne.getDc(), new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
                LogUtils.i("202012041055","sendMul:::");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("202012041055","sendMul:::"+t.getMessage());
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

    }

    private void setOnClickRespon(String pos, String tag, String desc) {
        paramShow(pos, tag, desc);
    }

    boolean allTimeUseTag;

    private void paramShow(String code, String tag, String desc) {
        try {
            String functionParams = paramMap.get(code).functionParams;
            JSONObject jsonObject = new JSONObject(functionParams);
            JSONObject param = jsonObject.getJSONObject("param");
            model = param.getJSONObject("model").getString("value");
            switch (tag) {
                //蒸模式
                case SteamOvenModeName.steamModel:
                    //烤模式
                case SteamOvenModeName.roast:
                    //辅助模式
                case SteamOvenModeName.fz:
                    boolean isSpecial = param.getBoolean("isSpecial");
                    String mode = param.getJSONObject("model").getString("value");
                    String defaultSetTemp = param.getJSONObject("defaultSetTemp").getString("value");
                    String defaultSetTime = param.getJSONObject("defaultSetTime").getString("value");

                    if ("".equals(param.optString("allTimeUse"))) {
                        allTimeUseTag = false;
                    } else {
                        allTimeUseTag = true;
                    }

                    if (needShowDialog(tag, mode)) {
                        descalingDialog();
                        return;
                    }

                    LogUtils.i("202012041111","isSpecial:::"+isSpecial);
                    if (isSpecial) {
                        JSONObject defaultTips = param.getJSONObject("defaultTips");
                        String title = defaultTips.getString("title");
                        String content = defaultTips.getString("content");
                        String positiveButton = defaultTips.getString("positiveButton");
                        String negativeButton = defaultTips.getString("negativeButton");

                        if (steameOvenOne.doorStatusValue == 1 && !TextUtils.equals(mode, "19")) {
                            ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                            return;
                        }
                        if ("19".equals(mode)) {
                            dryTipsDialog(title, content, positiveButton, negativeButton, mode, defaultSetTime, defaultSetTemp);
                        } else if ("21".equals(mode)) {
                            descalingTipsDialog(title, content, positiveButton, negativeButton);
                        }


                    } else {
                        JSONArray setTemp = param.getJSONObject("setTemp").getJSONArray("value");
                        JSONArray setTime = param.getJSONObject("setTime").getJSONArray("value");
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
                        if (steameOvenOne.doorStatusValue == 1) {
                            ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                            return;
                        }

                        //0 不需要 1 需要
                        if (steameOvenOne.weatherDescalingValue != 0) {
                            if (allTimeUseTag) {
                                setDialogParam(tempTure, timeTemp, defaultSetTemp, defaultSetTime, desc, timeList.get(2));
                            } else {
                                if (TextUtils.equals(tag, SteamOvenModeName.roast)) {
                                    setDialogParam(tempTure, timeTemp, defaultSetTemp, defaultSetTime, desc, timeList.get(2));
                                } else {
                                    descalingDialog();
                                }
                            }

                        } else {
                            setDialogParam(tempTure, timeTemp, defaultSetTemp, defaultSetTime, desc, timeList.get(2));
                        }

                    }


                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


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


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String temp = (String) msg.obj;
                    selectTemp = getNumData(temp);
                    break;
                case 2:
                    String time = (String) msg.obj;
                    selectTime = getNumData(time);
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
        mRokiDialogPro = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_17);
        mRokiDialogPro.setOkBtn("开始工作", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSteamOvenFZModel();
                mRokiDialogPro.dismiss();
            }
        });
        mRokiDialogPro.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialogPro.dismiss();
            }
        });
        mRokiDialogPro.setWheelViewData(
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
                }, null, new OnItemSelectedListenerRear() {

                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = contentRear;
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }
                });

        mRokiDialogPro.show();


    }

    //下发干燥模式前的提示框
    private void dryTipsDialog(String title, String content, String buttonText, String buttonText2, final String mode, final String defaultTime, final String defaultTemp) {
        final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        dialogByType.setTitleText(title);
        dialogByType.setContentText(content);
        dialogByType.setCancelBtn(buttonText2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogByType.dismiss();
            }
        });
        dialogByType.setOkBtn(buttonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dryMode(mode, defaultTime, defaultTemp);
                dialogByType.dismiss();
            }
        });
        dialogByType.show();

    }

    //下发除垢模式前的提示框
    private void descalingTipsDialog(String title, String content, String positiveButton, String negative) {
        final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
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
        LogUtils.i("20200507000", "model::" + model);
        if (steameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerState == SteamOvenOnePowerStatus.Wait
                ) {
            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), Short.parseShort(selectTime), Short.parseShort(selectTemp), (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtils.i("202004300123", "success:::");
                            //UIService.getInstance().popBack();
                            LogUtils.i("202012041055","functionCode2:::"+functionCode);
                            sendMul(functionCode);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("202012041055","t:::"+t.getMessage());
                        }
                    });

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(model), Short.parseShort(selectTime), Short.parseShort(selectTemp), (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    //UIService.getInstance().popBack();
                    LogUtils.i("202012041055","functionCode3:::"+functionCode);
                    sendMul(functionCode);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("202012041055","t.getMessage:::"+t.getMessage());
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
                            steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(mode), Short.parseShort(defaultTime), Short.parseShort(defaultTemp), (short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("202012041055","functionCode1:::"+functionCode);
                                    sendMul(functionCode);
                                    //UIService.getInstance().popBack();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });


                        }
                    }, 500);

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            steameOvenOne.setSteameOvenOneRunMode(Short.parseShort(mode), Short.parseShort(defaultTime), Short.parseShort(defaultTemp), (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendMul(functionCode);
                    //UIService.getInstance().popBack();
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (descalingTipsDialog != null) {
            descalingTipsDialog.dismiss();
        }
        closeProDialog();
    }

    //开门事件
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.pojo.getID()))
            return;
        steameOvenOne = (AbsSteameOvenOne) event.pojo;
    }

    private boolean needShowDialog(String tag, String model) {
        if (allTimeUseTag) {
            return false;
        }
        if (TextUtils.equals(model, "22") && (steameOvenOne.weatherDescalingValue != 0)) {
            return true;
        }
        if (TextUtils.equals(tag,SteamOvenModeName.roast)) {
            return false;
        }
        if (steameOvenOne.weatherDescalingValue == 0) {
            return false;
        }
        return true;
    }
}
