package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.roki.R;
import com.robam.roki.model.bean.OvenParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.OvenGrid035Apater;
import com.robam.roki.ui.adapter.OvenGridApater;
import com.robam.roki.ui.dialog.AbsOvenModeSettingDialog;
import com.robam.roki.ui.dialog.AbsOvenModeSettingHasDbDialog;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/7/6.
 * 专业模式
 */

public class AbsOvenGridBasePage extends BasePage {

    @InjectView(R.id.mode_back)
    ImageView modeBack;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.oven_mode_show)
    GridView ovenModeShow;

    public long userId = Plat.accountService.getCurrentUserId();

    String dc;
    String dt;
    public String guid;

    String title;
    List<DeviceConfigurationFunctions> mDatas = new ArrayList<>();

    List<DeviceConfigurationFunctions> modeList = new ArrayList<>();
    public HashMap<String, String> paramMap = new HashMap<>();
    public HashMap<String, String> descMap = new HashMap<>();
    private String functionName;
    private String hasDb;
    AbsOven oven;
    public String clickPos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        View view = inflater.inflate(R.layout.absoven_more_mode_show, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    public void initView() {
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        if (iDevice instanceof AbsOven) {
            oven = (AbsOven) iDevice;
        }
        dt = iDevice.getDt();
        dc = iDevice.getDc();
        name.setText(title);
    }


    protected void initData() {
        try {
            if ("RQ035".equals(mDatas.get(0).deviceType)) {
                modeList = mDatas.get(0).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                setParamMap();
                OvenGrid035Apater ovenGridApater = new OvenGrid035Apater(cx, modeList,dt);
                ovenModeShow.setAdapter(ovenGridApater);
                ovenModeShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LogUtils.i("2020070201", "functionCode::" + modeList.get(position).functionCode);
                        view.setTag(modeList.get(position).functionCode);
                        functionName = modeList.get(position).functionName;
                        setOnClickRespon((String) view.getTag());
                        LogUtils.i("2020070201", "tag::" + view.getTag());
                    }
                });
            } else {
                modeList = mDatas.get(0).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;

                setParamMap();
                OvenGridApater ovenGridApater = new OvenGridApater(cx, modeList);
                ovenModeShow.setAdapter(ovenGridApater);
                ovenModeShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setTag(modeList.get(position).functionCode);
                        functionName = modeList.get(position).functionName;
                        setOnClickRespon((String) view.getTag());
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setOnClickRespon(String pos) {
        clickPos = pos;
        paramShow(pos);
    }



    int cmd;
    String model;
    String defaultTime;
    String defaultTemp;
    List<Integer> time;
    List<Integer> temp;
    public AbsOvenModeSettingDialog absOvenModeSettingDialog;
    public AbsOvenModeSettingHasDbDialog absOvenModeSettingHasDbDialog;

    String desc = null;

    short barbecue;


    private void paramShow(String code) {
        try {

            if ("RQ035".equals(mDatas.get(0).deviceType)) {
                List<DeviceConfigurationFunctions> deviceConfigurationFunctions = mDatas.get(0)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                    if (deviceConfigurationFunctions.get(i).functionCode.equals(code)) {
                        String functionParams = deviceConfigurationFunctions.get(i).functionParams;
                        JSONObject jsonObject = new JSONObject(functionParams);
                        JSONObject code2 = jsonObject.optJSONObject(code);
                        cmd = code2.optInt("cmd");
                        model = code2.optJSONObject("mode").optString("value");
                        defaultTime = code2.optJSONObject("defaultMinute").optString("value");
                        defaultTemp = code2.optJSONObject("defaultTemp").optString("value");
                        JSONArray timeArray = code2.optJSONObject("minute").optJSONArray("value");
                        JSONArray tempArray = code2.optJSONObject("temp").optJSONArray("value");
                        hasDb = code2.optJSONObject("hasDb").optString("value");


                        if (time != null) {
                            time.clear();
                        }
                        if (temp != null) {
                            temp.clear();
                        }
                        time = new ArrayList<>();
                        temp = new ArrayList<>();
                        for (int j = 0; j < timeArray.length(); j++) {
                            Integer min = (Integer) timeArray.get(j);
                            time.add(min);
                        }
                        for (int i1 = 0; i1 < tempArray.length(); i1++) {
                            Integer temps = (Integer) tempArray.get(i1);
                            temp.add(temps);
                        }

                        desc = code2.optJSONObject("desc").optString("value");


                        List<Integer> tempure = TestDatas.createModeDataTemp(temp);
                        List<Integer> timeTemp = TestDatas.createModeDataTime(time);
                        int def1 = Integer.parseInt(defaultTemp) - temp.get(0);
                        int def2 = Integer.parseInt(defaultTime) - time.get(0);


                        //挡板插入状态 0 未插入 1 插入
                        if (oven.PlatInsertStatueValue == 0) {
                            setDialogParamHasDb(tempure, timeTemp, "℃", "分钟", def1, def2, desc);
                        } else if (oven.PlatInsertStatueValue == 1) {
                            ToastUtils.show(hasDb, Toast.LENGTH_SHORT);
                        }

                    }
                }


            } else {
                OvenParams ovenParams = JsonUtils.json2Pojo(paramMap.get(code), OvenParams.class);
                cmd = ovenParams.getCmd();
                model = ovenParams.getParam().getModel().getValue();
                defaultTemp = ovenParams.getParam().getDefaultSetTemp().getValue();
                defaultTime = ovenParams.getParam().getDefaultSetTime().getValue();
                time = ovenParams.getParam().getSetTime().getValue();
                temp = ovenParams.getParam().getSetTemp().getValue();
                desc = descMap.get(code);

                List<Integer> tempure = TestDatas.createModeDataTemp(temp);
                List<Integer> timeTemp = TestDatas.createModeDataTime(time);
                int def1 = Integer.parseInt(defaultTemp) - temp.get(0);
                int def2 = Integer.parseInt(defaultTime) - time.get(0);
                setDialogParam(tempure, timeTemp, "℃", "分", def1, def2, desc);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDialogParam(final List<Integer> temp, List<Integer> time, String str1, String str2, int defNum1, int defNum2, final String str3) {
        List<String> listButton = TestDatas.createDialogText(str1, str2, "取消", "开始工作", str3);
        absOvenModeSettingDialog = new AbsOvenModeSettingDialog(cx, temp, time, listButton, defNum1, defNum2);
        absOvenModeSettingDialog.show(absOvenModeSettingDialog);
        absOvenModeSettingDialog.setListener(new AbsOvenModeSettingDialog.PickListener() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int index1, int index2) {
                if (dt != null) {
                    ToolUtils.logEvent(dt, "开始烤箱模式温度时间工作:" + functionName + ":" + index1 + ":" + index2, "roki_设备");
                }
                send(cmd, model, index2, index1);

            }
        });
    }


    private void setDialogParamHasDb(final List<Integer> temp, List<Integer> time, String str1, String str2, int defNum1, int defNum2, final String str3) {
        List<String> listButton = TestDatas.createDialogText(str1, str2, "取消", "开始工作", str3);
        absOvenModeSettingHasDbDialog = new AbsOvenModeSettingHasDbDialog(cx, temp, time, listButton, defNum1, defNum2);
        absOvenModeSettingHasDbDialog.show(absOvenModeSettingHasDbDialog);
        absOvenModeSettingHasDbDialog.setListener(new AbsOvenModeSettingHasDbDialog.PickListener() {
                                                      @Override
                                                      public void onCancel() {

                                                      }

                                                      @Override
                                                      public void onConfirm(int index1, int index2, boolean isOpen, boolean isClose) {

                                                          if (isOpen) {
                                                              barbecue = 1;
                                                              send1(cmd, model, index2, index1);
                                                          } else {
                                                              barbecue = 0;
                                                              send1(cmd, model, index2, index1);
                                                          }
                                                      }
                                                  }
        );
    }


    public void send(int cmd, String mode, int setTime, int setTemp) {

    }


    private void setParamMap() {
        for (int i = 0; i < modeList.size(); i++) {
            paramMap.put(modeList.get(i).functionCode, modeList.get(i).functionParams);
            descMap.put(modeList.get(i).functionCode, modeList.get(i).msg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (absOvenModeSettingDialog != null) {
            absOvenModeSettingDialog.dismiss();
        }
        if (absOvenModeSettingHasDbDialog != null) {
            absOvenModeSettingHasDbDialog.dismiss();
        }
    }

    @OnClick({R.id.mode_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
            default:
                break;
        }
    }


    public void send1(int cmd, String mode, int setTime, int setTemp) {
    }


    public void sendCom1(int cmd, String mode, int setTime, int setTemp) {
    }


}
