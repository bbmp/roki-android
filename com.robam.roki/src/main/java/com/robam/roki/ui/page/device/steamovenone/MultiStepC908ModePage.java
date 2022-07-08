package com.robam.roki.ui.page.device.steamovenone;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.SteamOvenBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.AbsSteamOvenOneSelectDialog;
import com.robam.roki.utils.DataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.JsonUtils;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * CQ908
 * 蒸烤一体机多段模式
 */
public class MultiStepC908ModePage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @InjectView(R.id.ll_multi_one)
    LinearLayout llMultiOne;
    @InjectView(R.id.tv_mode1)
    TextView tvMode1;
    @InjectView(R.id.tv_temp1)
    TextView tvTemp1;
    @InjectView(R.id.tv_temp1_title)
    TextView tvTemp1Title;
    @InjectView(R.id.tv_temp1_2)
    TextView tvTemp1_2;
    @InjectView(R.id.tv_temp1_2_title)
    TextView tvTemp1_2Title;

    @InjectView(R.id.tv_time1)
    TextView tvTime1;

    @InjectView(R.id.ll_multi_two)
    LinearLayout llMultiTwo;
    @InjectView(R.id.tv_mode2)
    TextView tvMode2;
    @InjectView(R.id.tv_temp2)
    TextView tvTemp2;
    @InjectView(R.id.tv_temp2_title)
    TextView tvTemp2Title;
    @InjectView(R.id.tv_temp2_2_title)
    TextView tvTemp2_2Title;

    @InjectView(R.id.tv_temp2_2)
    TextView tvTemp2_2;
    @InjectView(R.id.tv_time2)
    TextView tvTime2;


    @InjectView(R.id.ll_multi_three)
    LinearLayout llMultiThree;
    @InjectView(R.id.tv_mode3)
    TextView tvMode3;
    @InjectView(R.id.tv_temp3_title)
    TextView tvTemp3Title;
    @InjectView(R.id.tv_temp3_2_title)
    TextView tvTemp3_2Title;
    @InjectView(R.id.tv_temp3)
    TextView tvTemp3;
    @InjectView(R.id.tv_temp3_2)
    TextView tvTemp3_2;
    @InjectView(R.id.tv_time3)
    TextView tvTime3;

    @InjectView(R.id.tv_multi_name)
    TextView tvMultiName;
    @InjectView(R.id.tv_show_multi_txt)
    TextView tvShowMultiTxt;

    @InjectView(R.id.tv_begin_work)
    TextView tvBeginiWork;

    @InjectView(R.id.rl_add)
    RelativeLayout rlAdd;

    String guid;
    AbsSteameOvenOne steameOvenOne;
    List<DeviceConfigurationFunctions> mDatas;
    private long userId;
    private List<Integer> keyList;
    private List<SteamOvenBean> beanListone, beanListtwo, beanListthree;
    List<String> modeListOne = new ArrayList<>();
    List<String> modeListTwo = new ArrayList<>();
    List<String> modeListThree = new ArrayList<>();
    List<Integer> defaultModeList = new ArrayList<>();
    List<Integer> tempList;
    List<Integer> timeList;
    private String codeOne, codeTwo, codeThree;
    private AbsSteamOvenOneSelectDialog absSteamOvenOneSelectDialog;
    String needDescalingParams;
    boolean allTimeUseTag;
    boolean allTimeUseTag2;
    boolean allTimeUseTag3;
    boolean isSpecial;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_multi_step_mode_c908, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        isSpecial = bd == null ? null : bd.getBoolean(PageArgumentKey.isRunning);
        userId = Plat.accountService.getCurrentUserId();
        initData();
        initView();
        return view;
    }


    private void initData() {
        try {
            for (int i = 0; i < mDatas.size(); i++) {
                if ("ddModel".equals(mDatas.get(i).functionCode)) {
                    String functionParams = mDatas.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = jsonObject.optJSONObject("param");
                    JSONObject oneObj = param.optJSONObject("1");
                    JSONObject twoObj = param.optJSONObject("2");
                    JSONObject threeObj = param.optJSONObject("3");
                    JSONArray one = oneObj.getJSONArray("modes");
                    JSONArray two = twoObj.getJSONArray("modes");
                    JSONArray three = threeObj.getJSONArray("modes");
                    defaultModeList.add(Integer.parseInt(oneObj.optString("defaultMode")));
                    defaultModeList.add(Integer.parseInt(twoObj.optString("defaultMode")));
                    defaultModeList.add(Integer.parseInt(threeObj.optString("defaultMode")));
                    JSONObject allmode = param.optJSONObject("allmode");
//
                    Map json2Map = JsonUtils.getJson2Map(allmode.toString());
                    keyList = new ArrayList<>();
                    Set<String> strings = json2Map.keySet();
                    for (String str : strings) {
                        if (StringUtils.isNumber(str)) {
                            keyList.add(Integer.valueOf(str));
                        } else {
                            keyList.add(999);
                        }
                    }
                    Collections.sort(keyList);

                    if (beanListone != null) {
                        beanListone.clear();
                    }
                    if (beanListtwo != null) {
                        beanListtwo.clear();
                    }
                    if (beanListthree != null) {
                        beanListthree.clear();
                    }
                    beanListone = new ArrayList<>();
                    beanListtwo = new ArrayList<>();
                    beanListthree = new ArrayList<>();
                    for (int j = 0; j < keyList.size(); j++) {
                        Integer integer = keyList.get(j);

                        for (int k = 0; k < one.length(); k++) {
                            String key = one.optString(k);
                            if (key.equals(String.valueOf(integer))) {
                                SteamOvenBean steamOvenBean = new SteamOvenBean();
                                JSONObject jsonOb = allmode.optJSONObject(String.valueOf(integer));
                                String model = jsonOb.optJSONObject("model").optString("value");
                                JSONArray setTemp = jsonOb.optJSONObject("setTemp").optJSONArray("value");
                                JSONArray setTime = jsonOb.optJSONObject("setTime").optJSONArray("value");
                                String defaultSetTemp = jsonOb.optJSONObject("defaultSetTemp").optString("value");
                                String defaultSetTime = jsonOb.optJSONObject("defaultSetTime").optString("value");
                                steamOvenBean.setCode(String.valueOf(integer));
                                steamOvenBean.setModeName(model);
                                steamOvenBean.setTemp(array2List(setTemp));
                                steamOvenBean.setTime(array2List(setTime));
                                steamOvenBean.setDefaultTemp(defaultSetTemp);
                                steamOvenBean.setDefaultTime(defaultSetTime);
                                beanListone.add(steamOvenBean);
                            }
                        }


                        for (int k = 0; k < two.length(); k++) {
                            String key = two.optString(k);
                            if (key.equals(String.valueOf(integer))) {
                                SteamOvenBean steamOvenBean = new SteamOvenBean();
                                JSONObject jsonOb = allmode.optJSONObject(String.valueOf(integer));
                                String model = jsonOb.optJSONObject("model").optString("value");
                                JSONArray setTemp = jsonOb.optJSONObject("setTemp").optJSONArray("value");
                                JSONArray setTime = jsonOb.optJSONObject("setTime").optJSONArray("value");
                                String defaultSetTemp = jsonOb.optJSONObject("defaultSetTemp").optString("value");
                                String defaultSetTime = jsonOb.optJSONObject("defaultSetTime").optString("value");
                                steamOvenBean.setCode(String.valueOf(integer));
                                steamOvenBean.setModeName(model);
                                steamOvenBean.setTemp(array2List(setTemp));
                                steamOvenBean.setTime(array2List(setTime));
                                steamOvenBean.setDefaultTemp(defaultSetTemp);
                                steamOvenBean.setDefaultTime(defaultSetTime);
                                beanListtwo.add(steamOvenBean);
                            }
                        }

                        for (int k = 0; k < three.length(); k++) {
                            String key = three.optString(k);
                            if (key.equals(String.valueOf(integer))) {
                                SteamOvenBean steamOvenBean = new SteamOvenBean();
                                JSONObject jsonOb = allmode.optJSONObject(String.valueOf(integer));
                                String model = jsonOb.optJSONObject("model").optString("value");
                                JSONArray setTemp = jsonOb.optJSONObject("setTemp").optJSONArray("value");
                                JSONArray setTime = jsonOb.optJSONObject("setTime").optJSONArray("value");
                                String defaultSetTemp = jsonOb.optJSONObject("defaultSetTemp").optString("value");
                                String defaultSetTime = jsonOb.optJSONObject("defaultSetTime").optString("value");
                                steamOvenBean.setCode(String.valueOf(integer));
                                steamOvenBean.setModeName(model);
                                steamOvenBean.setTemp(array2List(setTemp));
                                steamOvenBean.setTime(array2List(setTime));
                                steamOvenBean.setDefaultTemp(defaultSetTemp);
                                steamOvenBean.setDefaultTime(defaultSetTime);
                                beanListthree.add(steamOvenBean);
                            }
                        }


                    }
                }
            }

            parsingData();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        tvTitle.setText(title);
    }


    private List<Integer> array2List(JSONArray jsonArray) {
        List<Integer> templist = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Integer tem = null;
            try {
                tem = (Integer) jsonArray.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            templist.add(tem);
        }

        return TestDatas.createModeDataTemp(templist);
    }


    private void parsingData() {
        if (modeListOne != null) {
            modeListOne.clear();
        }
        if (modeListTwo != null) {
            modeListTwo.clear();
        }
        if (modeListThree != null) {
            modeListThree.clear();
        }
        if (tempList != null) {
            tempList.clear();
        }
        if (timeList != null) {
            timeList.clear();
        }
        for (int i = 0; i < beanListone.size(); i++) {
            String modeName = beanListone.get(i).getModeName();
            modeListOne.add(modeName);
        }

        for (int i = 0; i < beanListtwo.size(); i++) {
            String modeName = beanListtwo.get(i).getModeName();
            modeListTwo.add(modeName);
        }

        for (int i = 0; i < beanListthree.size(); i++) {
            String modeName = beanListthree.get(i).getModeName();
            modeListThree.add(modeName);
        }

    }


    @OnClick({R.id.iv_back, R.id.rl_add, R.id.tv_begin_work})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.rl_add:
                addFunction();
                break;
            //开始工作
            case R.id.tv_begin_work:
                if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                    ToastUtils.showShort(R.string.device_alarm_close_content);
                    return;
                }
                if (steameOvenOne.doorStatusValue == 1) {
                    ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                    return;
                }
                if (clickNum<=0) {
                    ToastUtils.show("至少选择一段", Toast.LENGTH_SHORT);
                    return;
                }
                boolean need = false;
                if (needShowDialog(codeOne,1)) {
                    need = true;
                }
                if (needShowDialog(codeTwo,2)) {
                    need = true;
                }
                if (needShowDialog(codeThree,3)) {
                    need = true;
                }
                if (need) {
                    descalingDialog();
                    return;
                }
                LogUtils.i("20200623","codeOne:::"+codeOne);
                LogUtils.i("20200623","codeTwo:::"+codeTwo);
                String temp = tvTemp1.getText().toString().trim();
                String time = tvTime1.getText().toString().trim();
                String temp2 = tvTemp2.getText().toString().trim();
                String time2 = tvTime2.getText().toString().trim();
                String temp3 = tvTemp3.getText().toString().trim();
                String time3 = tvTime3.getText().toString().trim();
                switch (clickNum) {
                    case 1:
                        sendCommand((short) 2,
                                (short) 1,
                                codeOne
                                ,Integer.valueOf(DataUtils.getNumeric(temp))
                                ,Integer.valueOf(DataUtils.getNumeric(time))
                                ,"0"
                                ,0
                                ,0
                                ,"0"
                                ,0
                                ,0);
                        break;
                    case 2:
                        sendCommand((short) 3,
                                (short) 2,
                                codeOne
                                ,Integer.valueOf(DataUtils.getNumeric(temp))
                                ,Integer.valueOf(DataUtils.getNumeric(time))
                                ,codeTwo
                                ,Integer.valueOf(DataUtils.getNumeric(temp2))
                                ,Integer.valueOf(DataUtils.getNumeric(time2))
                                ,"0"
                                ,0
                                ,0);
                        break;
                    case 3:
                        sendCommand((short) 4,
                                (short) 3,
                                codeOne
                                ,Integer.valueOf(DataUtils.getNumeric(temp))
                                ,Integer.valueOf(DataUtils.getNumeric(time))
                                ,codeTwo
                                ,Integer.valueOf(DataUtils.getNumeric(temp2))
                                ,Integer.valueOf(DataUtils.getNumeric(time2))
                                ,codeThree
                                ,Integer.valueOf(DataUtils.getNumeric(temp3))
                                ,Integer.valueOf(DataUtils.getNumeric(time3))
                        );
                        break;
                }

                break;
        }
    }

    int clickNum = 0;//点击次数

    private void addFunction() {

        if (clickNum == 0) {
            showSelectDialog(beanListone, defaultModeList.get(clickNum));
        } else if (clickNum == 1) {
            showSelectDialog(beanListtwo, defaultModeList.get(clickNum));
        } else if (clickNum == 2) {
            showSelectDialog(beanListthree, defaultModeList.get(clickNum));
        }
    }


    private void showSelectDialog(List<SteamOvenBean> steamOvenBeans, final int defaultMode) {
        List<String> str = new ArrayList<>();
        str.add("");
        str.add("℃");
        str.add("分钟");
        absSteamOvenOneSelectDialog = new AbsSteamOvenOneSelectDialog(cx, steamOvenBeans, keyList, str, defaultMode);
        absSteamOvenOneSelectDialog.showDiyDialog(absSteamOvenOneSelectDialog);
        absSteamOvenOneSelectDialog.setListener(new AbsSteamOvenOneSelectDialog.PickListener() {
            @Override
            public void onCancel() {
                absSteamOvenOneSelectDialog.dismiss();
            }

            @Override
            public void onConfirm(SteamOvenBean deviceOvenDiyParams, Integer integer2, Integer integer, Integer integer1) {
                clickNum++;
                String modeName = deviceOvenDiyParams.getModeName();
                switch (clickNum) {
                    case 1:
                        codeOne = deviceOvenDiyParams.getCode();
                        tvMode1.setText(modeName);
                        tvTemp1.setText(integer + "℃");
                        tvTime1.setText(integer1 + "分钟");
                        llMultiOne.setVisibility(View.VISIBLE);
                        tvMultiName.setText("第二段");
                        tvShowMultiTxt.setText("点击添加第二段");
                        break;
                    case 2:
                        codeTwo = deviceOvenDiyParams.getCode();
                        tvMode2.setText(modeName);
                        tvTemp2.setText(integer + "℃");
                        tvTime2.setText(integer1 + "分钟");

                        llMultiTwo.setVisibility(View.VISIBLE);
                        tvMultiName.setText("第三段");
                        tvShowMultiTxt.setText("点击添加第三段");
                        break;
                    case 3:
                        codeThree = deviceOvenDiyParams.getCode();
                        tvMode3.setText(modeName);
                        tvTemp3.setText(integer + "℃");
                        tvTime3.setText(integer1 + "分钟");
                        llMultiThree.setVisibility(View.VISIBLE);
                        rlAdd.setVisibility(View.GONE);
                        break;

                }

            }
        });

    }


    private void sendCommand(final short arg, final short totalNumber, final String mode1,
                             final int temp1, final int time1, final String mode2, final int temp2,
                             final int time2, final String mode3, final int temp3, final int time3) {
        if (steameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerState == SteamOvenOnePowerStatus.Wait) {
            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    setMultiStep(arg, totalNumber, mode1, (short) temp1, (short) time1, mode2, (short) temp2, (short) time2,mode3,(short)temp3,(short)time3);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200319", "开机：" + t.getMessage());
                    ToastUtils.show("指令下发失败", 2000);
                }
            });
        } else {
            setMultiStep(arg, totalNumber, mode1, (short) temp1, (short) time1, mode2, (short) temp2, (short) time2,mode3,(short)temp3,(short)time3);

        }

    }


    private void setMultiStep(short arg, short totalNumbers, String mode1, short temp1, short time1,
                              String mode2, short temp2, short time2,String mode3,short temp3,short time3) {
        steameOvenOne.setSteamOvenOneMultiStepModeCQ908(arg, totalNumbers, Short.parseShort(mode1), temp1,
                time1, Short.parseShort(mode2), temp2, time2, Short.parseShort(mode3), temp3, time3, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        LogUtils.i("20200603","success");
                        clickNum=0;
                        UIService.getInstance().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20200603",t.getMessage());
                        ToastUtils.show("指令下发失败", 2000);
                    }
                });
    }

    private boolean needShowDialog(String model , int clickNum) {
        if (TextUtils.isEmpty(model)) {
            return false;
        }
        if (TextUtils.equals(model, "13") && (steameOvenOne.weatherDescalingValue != 0)) {
            return true;
        }
        if (TextUtils.equals(model, "14") && (steameOvenOne.weatherDescalingValue != 0)) {
            return true;
        }
        if (TextUtils.equals(model, "15") && (steameOvenOne.weatherDescalingValue != 0)) {
            return true;
        }
        if (TextUtils.equals(model, "16") && (steameOvenOne.weatherDescalingValue != 0)) {
            return true;
        }
        if (TextUtils.equals(model, "22") && (steameOvenOne.weatherDescalingValue != 0)) {
            return true;
        }
        if (steameOvenOne.weatherDescalingValue == 0) {
            return false;
        }
        return false;
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
