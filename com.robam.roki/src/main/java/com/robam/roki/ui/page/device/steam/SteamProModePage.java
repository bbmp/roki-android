package com.robam.roki.ui.page.device.steam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamModeName;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.AbsSteamProfessionalAdapter;
import com.robam.roki.ui.dialog.AbsOvenModeSettingDialog;
import com.robam.roki.ui.page.device.oven.MyGridView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;

import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.util.FanLockUtils.mGuid;

/**
 * 235专业模式页面
 */
public class SteamProModePage extends BasePage {
    public AbsSteamoven steam;
    public String guid;
    String dt;
    String dc;
    @InjectView(R.id.tv_title)
    TextView titleName;
    @InjectView(R.id.mgv_steam_mode_top)
    MyGridView mgvSteamModeTop;
    @InjectView(R.id.mgv_steam_mode_bottom)
    MyGridView mgvSteamModeBottom;
    @InjectView(R.id.mgv_help_mode)
    MyGridView mgvHelpMode;

    @InjectView(R.id.tv_steam_title)
    TextView tvSteamTitle;
    @InjectView(R.id.tv_nutritional_title)
    TextView tvNutritionalTitle;
    @InjectView(R.id.tv_auxiliary_title)
    TextView tvAuxiliaryTitle;


    List<DeviceConfigurationFunctions> mDatas = new ArrayList<>();
    public AbsOvenModeSettingDialog absOvenModeSettingDialog;
    private List<DeviceConfigurationFunctions> deviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> topfunctionList;
    private List<DeviceConfigurationFunctions> bottomFunctionsList;
    private List<DeviceConfigurationFunctions> auxiliaryFunctionsList;
    public long userId = Plat.accountService.getCurrentUserId();
    private String needDescalingParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        steam = Plat.deviceService.lookupChild(mGuid);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        View view = inflater.inflate(R.layout.abs_steam_professional_page, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        for (int i = 0; i < mDatas.size(); i++) {
            String functionCode = mDatas.get(i).functionCode;
            if (functionCode.equals(SteamModeName.professionalModel)) {
                deviceConfigurationFunctions = mDatas.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
            }
        }
        for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
            //蒸模式
            if (deviceConfigurationFunctions.get(i)
                    .functionCode.equals(SteamModeName.mvaporationMode)) {
                topfunctionList = deviceConfigurationFunctions.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                String title = deviceConfigurationFunctions.get(i).subView.title;
                tvSteamTitle.setText(title);

            }
            //菜谱模式
            if (deviceConfigurationFunctions.get(i)
                    .functionCode.equals(SteamModeName.cookbookMode)) {
                bottomFunctionsList = deviceConfigurationFunctions.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                String title = deviceConfigurationFunctions.get(i).subView.title;
                tvNutritionalTitle.setText(title);
            }
            //辅助模式
            if (deviceConfigurationFunctions.get(i)
                    .functionCode.equals(SteamModeName.auxiliaryMode)) {
                auxiliaryFunctionsList = deviceConfigurationFunctions.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                String title = deviceConfigurationFunctions.get(i).subView.title;
                tvAuxiliaryTitle.setText(title);
            }

        }


        AbsSteamProfessionalAdapter topGridAdapter = new AbsSteamProfessionalAdapter(topfunctionList, cx);
        AbsSteamProfessionalAdapter bottomGridAdapter = new AbsSteamProfessionalAdapter(bottomFunctionsList, cx);
        AbsSteamProfessionalAdapter helpGridAdapter = new AbsSteamProfessionalAdapter(auxiliaryFunctionsList, cx);
        mgvSteamModeTop.setAdapter(topGridAdapter);
        if (bottomFunctionsList != null && !bottomFunctionsList.isEmpty()) {
            mgvSteamModeBottom.setAdapter(bottomGridAdapter);
        }
        mgvHelpMode.setAdapter(helpGridAdapter);
        mgvSteamModeTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setTag(topfunctionList.get(position).functionCode);
                setOnClickRespon((String) view.getTag(), SteamModeName.mvaporationMode);
            }
        });
        mgvSteamModeBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                view.setTag(bottomFunctionsList.get(position).functionCode);
                setOnClickRespon((String) view.getTag(), SteamModeName.cookbookMode);
            }
        });

        mgvHelpMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                view.setTag(auxiliaryFunctionsList.get(position).functionCode);
                setOnClickRespon((String) view.getTag(), SteamModeName.auxiliaryMode);

            }
        });
    }

    private void initView() {
        IDevice iDevice = Plat.deviceService.lookupChild(guid);
        dt = iDevice.getDt();
        dc = iDevice.getDc();
        titleName.setText(title);

    }

    @OnClick({R.id.mode_back})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
        }
    }

    private void setOnClickRespon(String pos, String tag) {
        clickPos = pos;
        String params = paramShow(pos, tag);
        parsingData(params, pos);
    }

    private int cmd;
    private String mode;

    private String paramShow(String code, String tag) {
        String functionParams = null;
        try {

            switch (tag) {
                case SteamModeName.mvaporationMode://蒸模式
                    for (int i = 0; i < topfunctionList.size(); i++) {
                        if (topfunctionList.get(i).functionCode.equals(code)) {
                            functionParams = topfunctionList.get(i).functionParams;
                        }
                    }

                    break;
                case SteamModeName.cookbookMode://菜谱模式
                    for (int i = 0; i < bottomFunctionsList.size(); i++) {
                        if (bottomFunctionsList.get(i).functionCode.equals(code)) {
                            functionParams = bottomFunctionsList.get(i).functionParams;
                        }
                    }
                    break;
                case SteamModeName.auxiliaryMode://辅助模式
                    for (int i = 0; i < auxiliaryFunctionsList.size(); i++) {
                        if (auxiliaryFunctionsList.get(i).functionCode.equals(code)) {
                            functionParams = auxiliaryFunctionsList.get(i).functionParams;
                        }
                    }

                    break;

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return functionParams;


    }


    private void parsingData(String params, String pos) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(params);
            JSONObject obj = jsonObject.optJSONObject(pos);
            String s = JSON.toString(obj);
            LogUtils.i("202011041609", "s:::" + s);
            cmd = obj.optInt("cmd");
            mode = obj.optJSONObject("mode").optString("value");
            JSONArray time1 = obj.optJSONObject("minute").optJSONArray("value");
            String defaultMinute = obj.optJSONObject("defaultMinute").optString("value");
            JSONArray temp1 = obj.optJSONObject("temp").optJSONArray("value");
            String defaultTemp = obj.optJSONObject("defaultTemp").optString("value");
            String desc = obj.optJSONObject("desc").optString("value");
            LogUtils.i("202011041609", "desc:::" + desc);
            List<Integer> temp = new ArrayList<>();
            for (int i = 0; i < temp1.length(); i++) {
                Integer tem = null;
                try {
                    tem = (Integer) temp1.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                temp.add(tem);
            }
            List<Integer> time = new ArrayList<>();
            for (int i = 0; i < time1.length(); i++) {
                Integer tim = null;
                try {
                    tim = (Integer) time1.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                time.add(tim);
            }
            List<Integer> tempure = TestDatas.createModeDataTemp(temp);
            List<Integer> timeTemp = TestDatas.createModeDataTime(time);
            int def1 = Integer.parseInt(defaultTemp) - temp.get(0);
            int def2 = Integer.parseInt(defaultMinute) - time.get(0);

            if (steam.doorState == 0) {
                ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
                return;
            }
            if (steam.descaleModeStageValue != 0||steam.WeatherDescalingValue == 1) {
                descalingDialog();
                return;
            }
            setDialogParam(tempure, timeTemp, "℃", "分钟", def1, def2, desc);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setDialogParam(final List<Integer> temp, List<Integer> time, String str1, String str2, int defNum1, int defNum2, final String str3) {
        List<String> listButton = TestDatas.createDialogText(str1, str2, "取消", "开始工作", str3);
        LogUtils.i("202011041609", "str3:::" + str3);
        if (steam.waterboxstate == 0) {
            ToastUtils.show(R.string.device_alarm_water_out, Toast.LENGTH_SHORT);
            return;
        }
        absOvenModeSettingDialog = new AbsOvenModeSettingDialog(cx, temp, time, listButton, defNum1, defNum2);
        absOvenModeSettingDialog.show(absOvenModeSettingDialog);
        absOvenModeSettingDialog.setListener(new AbsOvenModeSettingDialog.PickListener() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int index1, int index2) {
                send(cmd, mode, index1, index2);

            }
        });
    }

    private void send(final int cmd, final String mode, final int setTemp, final int setTime) {
        if (steam != null) {
            ToolUtils.logEvent(steam.getDt(), "开始蒸箱模式温度时间工作:" + mode + ":" + setTemp + ":" + setTime, "roki_设备");
        }
        if (steam.doorState == 0) {
            ToastUtils.show("门未关好，请先关好箱门", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.waterboxstate == 0) {
            ToastUtils.show("水箱已弹出，请确保水箱已放好", Toast.LENGTH_SHORT);
            return;
        }

        if (steam.status == SteamStatus.AlarmStatus) {
            ToastUtils.show("请先解除报警", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.descaleModeStageValue != 0) {
            descalingDialog();
            return;
        }
        if (steam.status == SteamStatus.Off || steam.status == SteamStatus.Wait) {
            steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom(cmd, mode, setTime, setTemp);

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            sendCom(cmd, mode, setTime, setTemp);

        }


    }

    public String clickPos;

    private void sendCom(int cmd, String mode, int setTime, int setTemp) {
        steam.setSteamCookModule((short) cmd,
                Short.decode(mode), (short) setTemp, (short) setTime,
                (short) 0, (short) 0, (short) 0, (short) 0, (short) 0,
                (short) 0, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul(clickPos);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20200704", t.getMessage());

                    }
                });
    }


    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, guid, code, dc, new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (absOvenModeSettingDialog != null && absOvenModeSettingDialog.isShowing()) {
            absOvenModeSettingDialog.dismiss();
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
        final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        dialogByType.setTitleText(descalingTitle);
        dialogByType.setContentText(descalingContent);
        dialogByType.setOkBtn(descalingButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogByType != null) {
                    dialogByType.dismiss();
                }
            }
        });
        dialogByType.show();
    }
}
