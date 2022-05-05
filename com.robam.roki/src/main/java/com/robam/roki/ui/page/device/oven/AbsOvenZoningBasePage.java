package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.OvenGrid035Apater;
import com.robam.roki.ui.dialog.AbsOvenModeSettingDialog;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 035烤箱 分区模式
 */

public class AbsOvenZoningBasePage extends BasePage {
    @InjectView(R.id.mode_back)
    ImageView modeBack;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.mgv_zoning_mode_top)
    MyGridView mgvZoningModeTop;

    @InjectView(R.id.mgv_zoning_mode_bottom)
    MyGridView mgvZoningModeBottom;

    @InjectView(R.id.mgv_zoning_mode_compose)
    MyGridView mgvZoningModeCompose;

    @InjectView(R.id.tv_up_title)
    TextView tvUpTitle;
    @InjectView(R.id.tv_bottom_title)
    TextView tvBottomTitle;
    @InjectView(R.id.tv_zoning_title)
    TextView tvZoningTitle;


    List<DeviceConfigurationFunctions> mDatas = new ArrayList<>();
    private List<DeviceConfigurationFunctions> deviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> topFunctions;
    private List<DeviceConfigurationFunctions> bottomFunctions;
    private List<DeviceConfigurationFunctions> composeFunctions;

    public HashMap<String, DeviceConfigurationFunctions> paramMap = new HashMap<>();
    public AbsOvenModeSettingDialog absOvenModeSettingDialog;

    String dt;
    public String guid;

    String title;
    private int cmd;
    private String model;
    private DeviceConfigurationFunctions topList;
    private DeviceConfigurationFunctions bottomList;
    private DeviceConfigurationFunctions composeList;
    AbsOven oven;
    private String tag1;
    long userId = Plat.accountService.getCurrentUserId();
    private String clickCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        View view = inflater.inflate(R.layout.absoven_zoning_mode_show, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        try {
            for (int i = 0; i < mDatas.size(); i++) {
                if ("zoningMode".equals(mDatas.get(i).functionCode)) {
                    deviceConfigurationFunctions = mDatas.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                }
            }
            for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                if ("zoningModeTop".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    topList = deviceConfigurationFunctions.get(i);
                    String upTitleName = deviceConfigurationFunctions.get(i).functionName;
                    tvUpTitle.setText(upTitleName);
                }
                if ("zoningModeBottem".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    bottomList = deviceConfigurationFunctions.get(i);
                    String bottomTitleName = deviceConfigurationFunctions.get(i).functionName;
                    tvBottomTitle.setText(bottomTitleName);
                }
                if ("zoningModeCompose".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    composeList = deviceConfigurationFunctions.get(i);
                    String composeTitleName = deviceConfigurationFunctions.get(i).functionName;
                    tvZoningTitle.setText(composeTitleName);
                }
            }


            topFunctions = topList.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            bottomFunctions = bottomList.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            composeFunctions = composeList.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            setParamMap();

            OvenGrid035Apater topGridAdapter = new OvenGrid035Apater(cx, topFunctions,dt);
            OvenGrid035Apater bottomGridAdapter = new OvenGrid035Apater(cx, bottomFunctions,dt);
            OvenGrid035Apater composeGridAdapter = new OvenGrid035Apater(cx, composeFunctions,dt);
            mgvZoningModeTop.setAdapter(topGridAdapter);
            mgvZoningModeBottom.setAdapter(bottomGridAdapter);
            mgvZoningModeCompose.setAdapter(composeGridAdapter);

            mgvZoningModeTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setTag(topFunctions.get(position).functionCode);
                    setOnClickRespon((String) view.getTag(), "zoningModeTop");
                }
            });

            mgvZoningModeBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setTag(bottomFunctions.get(position).functionCode);
                    setOnClickRespon((String) view.getTag(), "zoningModeBottom");
                }
            });

            mgvZoningModeCompose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setTag(composeFunctions.get(position).functionCode);
                    setOnClickRespon((String) view.getTag(), "zoningModeCompose");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, guid, code, oven.getDc(), new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
                LogUtils.i("20200527", "getReportResponse:::" + getReportResponse.msg);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20200527", "message:::" + t.getMessage());
            }
        });
    }

    private void setParamMap() {
        for (int i = 0; i < topFunctions.size(); i++) {
            paramMap.put(topFunctions.get(i).functionCode, topFunctions.get(i));
        }
        for (int i = 0; i < bottomFunctions.size(); i++) {
            paramMap.put(bottomFunctions.get(i).functionCode, bottomFunctions.get(i));
        }

        for (int i = 0; i < composeFunctions.size(); i++) {
            paramMap.put(composeFunctions.get(i).functionCode, composeFunctions.get(i));
        }

    }

    private void setOnClickRespon(String pos, String tag) {
        paramShow(pos, tag);
    }


    private void paramShow(String code, String tag) {
        try {
            clickCode = code;
            String functionCode = paramMap.get(code).functionCode;
            String functionParams = paramMap.get(code).functionParams;
            JSONObject jsonObject = new JSONObject(functionParams);
            JSONObject functionCode1 = jsonObject.getJSONObject(functionCode);
            String hasDbDesc = functionCode1.getJSONObject("hasDb").getString("value");


            if (oven.PlatInsertStatueValue == 0) {
                //提示转入挡板
                ToastUtils.show(hasDbDesc, Toast.LENGTH_SHORT);
                return;

            }

            switch (tag) {
                case "zoningModeTop":
                case "zoningModeBottom":
                    tag1 = tag;
                    cmd = functionCode1.getInt("cmd");
                    model = functionCode1.getJSONObject("mode").getString("value");
                    JSONArray time1 = functionCode1.getJSONObject("minute").getJSONArray("value");
                    String defaultMinute = functionCode1.getJSONObject("defaultMinute").getString("value");
                    JSONArray temp1 = functionCode1.getJSONObject("temp").getJSONArray("value");
                    String defaultTemp = functionCode1.getJSONObject("defaultTemp").getString("value");
                    String desc = functionCode1.getJSONObject("desc").getString("value");


                    List<Integer> temp = new ArrayList<>();
                    for (int i = 0; i < temp1.length(); i++) {
                        Integer tem = (Integer) temp1.get(i);
                        temp.add(tem);
                    }
                    List<Integer> time = new ArrayList<>();
                    for (int i = 0; i < time1.length(); i++) {
                        Integer tim = (Integer) time1.get(i);
                        time.add(tim);
                    }


                    List<Integer> tempure = TestDatas.createModeDataTemp(temp);
                    List<Integer> timeTemp = TestDatas.createModeDataTime(time);
                    int def1 = Integer.parseInt(defaultTemp) - temp.get(0);
                    int def2 = Integer.parseInt(defaultMinute) - time.get(0);

                    setDialogParam(tempure, timeTemp, "℃", "分钟", def1, def2, desc);


                    break;
                //组合模式
                case "zoningModeCompose":

                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.from, "2");
                    bd.putString(PageArgumentKey.Guid, guid);
                    bd.putString(PageArgumentKey.combination, functionCode1.toString());
                    bd.putString(PageArgumentKey.code, code);
                    UIService.getInstance().postPage(PageKey.AbsOvenZoneSet, bd);


                    break;
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

                send(cmd, model, index2, index1);

            }
        });
    }

    public void send(final int cmd, final String mode, final int setTime, final int setTemp) {
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom(cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();

                }
            });
        } else {
            sendCom(cmd, mode, setTime, setTemp);
        }

    }

    private void sendCom(final int cmd, final String mode, final int setTime, final int setTemp) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (tag1) {
                    case "zoningModeTop":
                        oven.setOvenModelRunMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                sendMul(clickCode);
                                UIService.getInstance().popBack();

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败了哟,请重新下发", Toast.LENGTH_SHORT);
                            }
                        });
                        break;
                    case "zoningModeBottom":
                        oven.setOvenRunModeTopAndBottom(
                                (short) cmd,
                                Short.decode(mode),
                                (short) 0,//setTime
                                (short) 0,//setTempUp
                                (short) 0,//preflag
                                (short) 0,//recipeId
                                (short) 0,//recipeStep
                                (short) 2,//ArgumentNumber
                                (short) 0,//SetTempDown
                                (short) 255,//orderTime_min
                                (short) 255,//orderTime_hour
                                (short) 0,//Rotatebarbecue
                                (short) setTemp,//setTemp2
                                (short) setTime,//setTime2
                                new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        sendMul(clickCode);
                                        UIService.getInstance().popBack();
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                }

                        );

                        break;
                }


            }
        }, 500);


    }


    public void initView() {
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        dt = iDevice.getDt();
        name.setText(title);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (absOvenModeSettingDialog != null) {
            absOvenModeSettingDialog.dismiss();
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
}
