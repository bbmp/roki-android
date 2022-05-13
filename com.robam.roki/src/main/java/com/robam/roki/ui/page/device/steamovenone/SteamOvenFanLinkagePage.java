package com.robam.roki.ui.page.device.steamovenone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.Payload;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 蒸烤一体机C915
 * 烟机联动
 */
public class SteamOvenFanLinkagePage extends BasePage {
    @InjectView(R.id.mode_back)
    ImageView modeBack;

    @InjectView(R.id.name)
    TextView name;

    @InjectView(R.id.tv_full_ventilation)
    TextView tvFullVentilation;

    @InjectView(R.id.tv_full_ventilation_cb)
    CheckBoxView tvFullVentilationCb;


    @InjectView(R.id.tv_full_ventilation_content)
    TextView tvFullVentilationContent;

    @InjectView(R.id.tv_open_door_ventilation)
    TextView tvOpenDoorVentilation;

    @InjectView(R.id.cb_open_door_ventilation_cb)
    CheckBoxView cbOpenDoorVentilationCb;

    @InjectView(R.id.tv_content_pre)
    TextView tvContentPre;

    @InjectView(R.id.tv_content_center)
    TextView tvContentCenter;

    @InjectView(R.id.tv_content_behind)
    TextView tvContentBehind;

    String guid;
    private List<DeviceConfigurationFunctions> mDatas;
    List<Payload> mPayloadlist = new ArrayList<>();
    AbsSettingDialog absSettingDialog;
    private List<Integer> minList;
    private String defaultMinute;
    private int delayTime;
    private boolean enableClick;

    private Payload payload1;
    private Payload payload2;
    AbsSteameOvenOne steameOvenOne;
    private long userId;


    String FullKey = "RZKY:EVENT:153:20:0";
    String OpenKey = "RZKY:EVENT:153:19:0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steam_oven_fan_linkage, container, false);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        userId = Plat.accountService.getCurrentUserId();
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        name.setText(title);
        try {


            for (int i = 0; i < mDatas.size(); i++) {
                //全程换气
                if (mDatas.get(i).functionCode.equals("fullVentilation")) {
                    String functionParams = mDatas.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = jsonObject.getJSONObject("param");
                    String tips = param.getJSONObject("tips").getString("value");
                    String desc = param.getJSONObject("desc").getString("value");
                    tvFullVentilation.setText(tips);
                    tvFullVentilationContent.setText(desc);
                }

                if (mDatas.get(i).functionCode.equals("openVentilation")) {
                    String functionParams = mDatas.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = jsonObject.getJSONObject("param");
                    String tips = param.getJSONObject("tips").getString("value");
                    String desc = param.getJSONObject("desc").getString("value");
                    JSONArray mins = param.getJSONObject("minute").getJSONArray("value");
                    defaultMinute = param.getJSONObject("defaultMinute").getString("value");
                    List<Integer> times = new ArrayList<>();
                    for (int j = 0; j < mins.length(); j++) {
                        Integer time = (Integer) mins.get(j);
                        times.add(time);
                    }
                    minList = TestDatas.createModeDataTime(times);
                    tvOpenDoorVentilation.setText(tips);
                    String[] buttons = desc.split("button");
                    String pre = buttons[0];
                    String behind = buttons[1];

                    tvContentPre.setText(pre);
                    tvContentBehind.setText(behind);


                }


            }
            doThings();
            getData();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void doThings() {
        try {

            //全程换气
            tvFullVentilationCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //打开开门换气
                        setAllLinkage(true);
                    } else {
                        setAllLinkage(false);
                    }
                }
            });
            //开门换气
            cbOpenDoorVentilationCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        enableClick = true;
                        tvContentCenter.setTextColor(getResources().getColor(R.color.c11));
                        setOpenLinkage(true, Integer.valueOf(defaultMinute));
                    } else {
                        enableClick = false;
                        tvContentCenter.setTextColor(getResources().getColor(R.color.gray_text));
                        setOpenLinkage(false, Integer.valueOf(defaultMinute));
                    }
                }
            });


            //选择分钟
            tvContentCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (enableClick) {
                        setDelayTimeDialog(Integer.valueOf(defaultMinute));
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (absSettingDialog != null) {
            absSettingDialog.dismiss();
        }
    }

    @OnClick({R.id.mode_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
        }
    }


    //查询设备联动
    private void getData() {

        CloudHelper.DeviceReactQuery(guid, new Callback<Reponses.QueryDeviceReact>() {
            @Override
            public void onSuccess(Reponses.QueryDeviceReact queryDeviceReact) {

                Map<String, Payload> map = queryDeviceReact.payloadMap;
                if (mPayloadlist != null) {
                    mPayloadlist.clear();
                }
                if (map.size() != 0) {
                    Set<String> strings = map.keySet();
                    for (String key : strings) {
                        Payload key1 = map.get(key);
                        mPayloadlist.add(key1);
                        //全程开启烟机
                        if (key.equals(FullKey)) {
                            payload1 = map.get(key);
                        }

                        //工作结束开门才开启烟机
                        if (key.equals(OpenKey)) {
                            payload2 = map.get(key);
                        }
                    }

                }


                for (int i = 0; i < mPayloadlist.size(); i++) {

                    if (mPayloadlist.get(i).cmd == 134) {
                        tvFullVentilationCb.setChecked(mPayloadlist.get(i).enable);
                    } else {
                        enableClick = mPayloadlist.get(i).enable;
                        cbOpenDoorVentilationCb.setChecked(mPayloadlist.get(i).enable);

                        if (cbOpenDoorVentilationCb.isChecked()) {
                            tvContentCenter.setText(mPayloadlist.get(i).duration + "");
                            tvContentCenter.setTextColor(getResources().getColor(R.color.c11));
                        } else {
                            tvContentCenter.setText(defaultMinute);
                            tvContentCenter.setTextColor(getResources().getColor(R.color.gray_text));
                        }
                    }

                }


            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190906", t.toString());
                t.printStackTrace();

            }
        });


    }


    //设置全程换气
    private void setAllLinkage(boolean enable) {
        Map<String, Payload> payloadMap = new HashMap<>();

        Payload fullPayload = new Payload();
        if (payload1 != null) {
            fullPayload.enable = enable;
            fullPayload.duration = payload1.duration;
            fullPayload.gear = payload1.gear;
            fullPayload.cmd = payload1.cmd;
            fullPayload.category = payload1.category;
        } else {
            fullPayload.enable = enable;
            fullPayload.duration = 0;
            fullPayload.gear = 3;
            fullPayload.cmd = 134;
            fullPayload.category = "RZKY";
        }


        Payload openPayload = new Payload();
        if (payload2 != null) {
            openPayload.enable = payload2.enable;
            openPayload.duration = payload2.duration;
            openPayload.gear = payload2.gear;
            openPayload.cmd = payload2.cmd;
            openPayload.category = payload2.category;
        } else {
            openPayload.enable = true;
            openPayload.duration = 3;
            openPayload.gear = 3;
            openPayload.cmd = 144;
            openPayload.category = "RZKY";
        }


        payloadMap.put(FullKey, fullPayload);
        payloadMap.put(OpenKey, openPayload);

        try {
            CloudHelper.setLinkage(guid, payloadMap, new Callback<Reponses.SetDeviceLinkage>() {
                @Override
                public void onSuccess(Reponses.SetDeviceLinkage setDeviceLinkage) {
                    String msg = setDeviceLinkage.msg;
                    LogUtils.i("20200213", "msg:::" + msg);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200213", t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //设置开门换气
    private void setOpenLinkage(boolean enable, int delayTime) {
        Map<String, Payload> payloadMap = new HashMap<>();

        Payload fullPayloads = new Payload();
        if (payload1 != null) {
            fullPayloads.enable = payload1.enable;
            fullPayloads.duration = payload1.duration;
            fullPayloads.gear = payload1.gear;
            fullPayloads.cmd = payload1.cmd;
            fullPayloads.category = payload1.category;
        } else {
            fullPayloads.enable = true;
            fullPayloads.duration = 0;
            fullPayloads.gear = 3;
            fullPayloads.cmd = 134;
            fullPayloads.category = "RZKY";
        }
        Payload openPayload = new Payload();
        if (payload2 != null) {

            openPayload.enable = enable;
            openPayload.duration = delayTime;
            openPayload.gear = payload2.gear;
            openPayload.cmd = payload2.cmd;
            openPayload.category = payload2.category;
        } else {
            openPayload.enable = enable;
            openPayload.duration = delayTime;
            openPayload.gear = 3;
            openPayload.cmd = 144;
            openPayload.category = "RZKY";
        }


        payloadMap.put(OpenKey, openPayload);
        payloadMap.put(FullKey, fullPayloads);


        CloudHelper.setLinkage(guid, payloadMap, new Callback<Reponses.SetDeviceLinkage>() {
            @Override
            public void onSuccess(Reponses.SetDeviceLinkage setDeviceLinkage) {
                String msg = setDeviceLinkage.msg;
                LogUtils.i("20200213", "msg:::" + msg);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20200213", t.getMessage());

            }
        });
    }


    private void setDelayTimeDialog(int num) {
        List<String> listButton = TestDatas.createButtonText("分钟", "取消", "确定", null);
        absSettingDialog = new AbsSettingDialog<>(cx, minList, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Object index) {
                delayTime = (int) index;
                enableClick = true;
                setOpenLinkage(true, delayTime);
                tvContentCenter.setText(delayTime + "");


            }
        });
    }



}
