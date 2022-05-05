package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenModeName;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.model.bean.OvenExpParamBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.AbsOvenExpDialog;
import com.robam.roki.ui.dialog.AbsOvenModeSettingDialog;
import com.robam.roki.ui.dialog.AbsOvenModeSettingHasDbDialog;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class AbsOvenPage extends AbsOvenBasePage {

    boolean falg = true;
    private String mode1;
    private int cmd1;
    private String tag1 = "";
    private String clickCode;


    //点击上面四个
    public void clickMain(String code) {
        if (dt != null) {
            ToolUtils.logEvent(dt, "点击烤箱场景功能:" + "更多", "roki_设备");
        }
        if (!isSpecial) {
            switch (code) {

                case OvenModeName.more:
                    if (falg) {
                        ovenFirstView.setUpData(moreList);
                        falg = false;
                    } else {
                        ovenFirstView.removeMoreView();
                        falg = true;
                    }

                    break;
                default:
                    //跳转到菜谱
                    goRecipe(code);
                    sendMul(code);
                    break;
            }
        } else {
            //035
            try {
                if (dt.equals("Q082A")) {
                    clickCode = code;
                    String functionCode = paramMapMore.get(code).functionCode;
                    String functionParams = paramMapMore.get(code).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject functionCode1 = jsonObject.getJSONObject(functionCode);
                    cmd1 = functionCode1.getInt("cmd");
                    mode1 = functionCode1.getJSONObject("mode").getString("value");
                    String desc = paramMapMore.get(code).msg;
//                    String hasDb = functionCode1.getJSONObject("hasDb").getString("value");
                    JSONArray time1 = functionCode1.getJSONObject("minute").getJSONArray("value");
                    String defaultMinute = functionCode1.getJSONObject("defaultMinute").getString("value");
                    JSONArray temp1 = functionCode1.getJSONObject("temp").getJSONArray("value");
                    String defaultTemp = functionCode1.getJSONObject("defaultTemp").getString("value");
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


                    setDialogParam2(tempure, timeTemp, "℃", "分钟", def1, def2, desc);
                } else {
                    clickCode = code;
                    String functionCode = paramMapMore.get(code).functionCode;
                    String functionParams = paramMapMore.get(code).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject functionCode1 = jsonObject.getJSONObject(functionCode);
                    cmd1 = functionCode1.getInt("cmd");
                    mode1 = functionCode1.getJSONObject("mode").getString("value");
                    String desc = functionCode1.getJSONObject("desc").getString("value");
                    String hasDb = functionCode1.getJSONObject("hasDb").getString("value");

                    JSONObject modeParamsObject = new JSONObject(modeParams);
                    JSONObject modeParam = modeParamsObject.getJSONObject("param");

                    JSONObject modeObj = modeParam.getJSONObject(mode1);
                    String isCombination = modeObj.getString("isCombination");
                    String isOnlyTop = modeObj.getString("isOnlyTop");
                    String isOnlyBottem = modeObj.getString("isOnlyBottem");


                    // 分层模式
                    if ("true".equals(isCombination)) {

                        if (oven.PlatInsertStatueValue == 0) {
                            //提示转入挡板
                            ToastUtils.show(hasDb, Toast.LENGTH_SHORT);

                        } else {
                            //仅上层
                            if ("true".equals(isOnlyTop)) {
                                tag1 = "zoningModeTop";

                                JSONArray time1 = functionCode1.getJSONObject("minute").getJSONArray("value");
                                String defaultMinute = functionCode1.getJSONObject("defaultMinute").getString("value");
                                JSONArray temp1 = functionCode1.getJSONObject("temp").getJSONArray("value");
                                String defaultTemp = functionCode1.getJSONObject("defaultTemp").getString("value");

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


                                setDialogParam2(tempure, timeTemp, "℃", "分钟", def1, def2, desc);
                            } else if ("true".equals(isOnlyBottem)) {
                                tag1 = "zoningModeBottom";
                                JSONArray time1 = functionCode1.getJSONObject("minute").getJSONArray("value");
                                String defaultMinute = functionCode1.getJSONObject("defaultMinute").getString("value");
                                JSONArray temp1 = functionCode1.getJSONObject("temp").getJSONArray("value");
                                String defaultTemp = functionCode1.getJSONObject("defaultTemp").getString("value");

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


                                setDialogParam2(tempure, timeTemp, "℃", "分钟", def1, def2, desc);


                            } else {
                                //上下层
                                Bundle bd = new Bundle();
                                bd.putString(PageArgumentKey.from, "1");
                                bd.putString(PageArgumentKey.Guid, mGuid);
                                bd.putString(PageArgumentKey.combination, functionCode1.toString());
                                bd.putString(PageArgumentKey.code, clickCode);
                                UIService.getInstance().postPage(PageKey.AbsOvenZoneSet, bd);
                            }


                        }
                        //全腔
                    } else {

                        JSONArray time1 = functionCode1.getJSONObject("minute").getJSONArray("value");
                        String defaultMinute = functionCode1.getJSONObject("defaultMinute").getString("value");

                        JSONArray temp1 = functionCode1.getJSONObject("temp").getJSONArray("value");
                        String defaultTemp = functionCode1.getJSONObject("defaultTemp").getString("value");


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
                        List<Integer> temperatureList = TestDatas.createModeDataTemp(temp);
                        List<Integer> timeList = TestDatas.createModeDataTime(time);
                        int def1 = Integer.parseInt(defaultTemp) - temp.get(0);
                        int def2 = Integer.parseInt(defaultMinute) - time.get(0);

                        //挡板插入状态
                        if (oven.PlatInsertStatueValue == 0) {
                            //专业模式
                            if (Integer.valueOf(mode1) <= 15) {
                                setDialogParamHasDb(temperatureList, timeList, "℃", "分钟", def1, def2, desc);
                            } else {
                                //分区模式
                                //提示装入挡板
                                ToastUtils.show(hasDb, Toast.LENGTH_SHORT);
                            }
                        } else {
                            //专业模式
                            if (Integer.valueOf(mode1) <= 15) {
                                //提示拆卸挡板
                                ToastUtils.show(hasDb, Toast.LENGTH_SHORT);
                            } else {
                                setDialogParamHasDb(temperatureList, timeList, "℃", "分钟", def1, def2, desc);
                            }
                        }

                    }
                }


            } catch (Exception e) {
                ToastUtils.show(e.getMessage(), Toast.LENGTH_SHORT);
                e.printStackTrace();

            }


        }

    }


    private void setDialogParam2(final List<Integer> temp, List<Integer> time, String str1, String str2,
                                 int defNum1, int defNum2, final String str3) {
        //警报时是否弹出？
        if(alarmDialog()){
            return;
        }
        List<String> listButton = TestDatas.createDialogText(str1, str2, "取消", "开始工作", str3);
        absOvenModeSettingDialog = new AbsOvenModeSettingDialog(cx, temp, time, listButton, defNum1, defNum2);
        absOvenModeSettingDialog.show(absOvenModeSettingDialog);
        absOvenModeSettingDialog.setListener(new AbsOvenModeSettingDialog.PickListener() {

            @Override
            public void onCancel() {
                absOvenModeSettingDialog.dismiss();
            }

            @Override
            public void onConfirm(int index1, int index2) {
                absOvenModeSettingDialog.dismiss();
                send3(cmd1, mode1, index2, index1);
            }
        });
    }

    private boolean alarmDialog(){
        if (dt.equals("Q082A")) {
            AbsOven deviceOven = Plat.deviceService.lookupChild(mGuid);
            if (deviceOven.status == OvenStatus.AlarmStatus) {
                AlarmDataUtils.ovenAlarmStatus(oven.alarm, oven);
                return true;
            }
        }
        return false;
    }

    public void send3(final int cmd, final String mode, final int setTime, final int setTemp) {
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom3(cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();

                }
            });
        } else {
            sendCom3(cmd, mode, setTime, setTemp);
        }

    }


    private void sendCom3(final int cmd, final String mode, final int setTime, final int setTemp) {
        switch (tag1) {
            case "zoningModeTop":
                oven.setOvenModelRunMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul(clickCode);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
                break;
            case "zoningModeBottom":
                oven.setOvenRunModeTopAndBottom((short) cmd, Short.decode(mode), (short) 0, (short) 0,
                        (short) 0, (short) 0, (short) 0, (short) 2, (short) 0, (short) 255, (short) 255,
                        (short) 0, (short) setTemp, (short) setTime, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                sendMul(clickCode);
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                break;
            default:
                oven.setOvenModelRunMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul(clickCode);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
                break;
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (absOvenModeSettingHasDbDialog != null && absOvenModeSettingHasDbDialog.isShowing()) {
            absOvenModeSettingHasDbDialog.dismiss();
        }

        if (absOvenExpDialog != null && absOvenExpDialog.isShowing()) {
            absOvenExpDialog.dismiss();
        }

        if (absOvenModeSettingDialog != null && absOvenModeSettingDialog.isShowing()) {
            absOvenModeSettingDialog.dismiss();
        }


    }


    short barbecue;

    private void setDialogParamHasDb(final List<Integer> temp, List<Integer> time, String str1,
                                     String str2, int defNum1, int defNum2, final String str3) {
        List<String> listButton = TestDatas.createDialogText(str1, str2, "取消", "开始工作", str3);
        absOvenModeSettingHasDbDialog = new AbsOvenModeSettingHasDbDialog(cx, temp, time, listButton, defNum1, defNum2);
        absOvenModeSettingHasDbDialog.show(absOvenModeSettingHasDbDialog);
        absOvenModeSettingHasDbDialog.setListener(new AbsOvenModeSettingHasDbDialog.PickListener() {
                                                      @Override
                                                      public void onCancel() {
                                                          if (absOvenModeSettingHasDbDialog != null) {
                                                              absOvenModeSettingHasDbDialog.dismiss();
                                                          }
                                                      }

                                                      @Override
                                                      public void onConfirm(int index1, int index2, boolean isOpen, boolean isClose) {

                                                          if (isOpen) {
                                                              barbecue = 1;
                                                              send2(cmd1, mode1, index2, index1);
                                                          } else {
                                                              barbecue = 0;
                                                              send2(cmd1, mode1, index2, index1);
                                                          }

                                                          if (absOvenModeSettingHasDbDialog != null) {
                                                              absOvenModeSettingHasDbDialog.dismiss();
                                                          }
                                                      }
                                                  }
        );
    }


    public void send2(final int cmd, final String mode, final int setTime, final int setTemp) {
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom2(cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            sendCom2(cmd, mode, setTime, setTemp);
        }
    }


    private void sendCom2(final int cmd, final String mode, final int setTime, final int setTemp) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                oven.setOvenBakeDIYMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, barbecue, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendMul(clickCode);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }
        }, 500);
    }


    //跳转到菜谱
    private void goRecipe(String code) {
        try {
            String funParam = paramMapMore.get(code).functionParams;
            JSONObject jsonObject = new JSONObject(funParam);
            LogUtils.i("20190823", jsonObject.toString());
            long cookId = jsonObject.getLong("cookbookId");

            RecipeDetailPage.show(cookId, RecipeDetailPage.DeviceRecipePage, RecipeRequestIdentification.RECIPE_SORTED, mGuid);

            if (dt != null) {
                ToolUtils.logEvent(dt, "点击烤箱场景功能:" + paramMapMore.get(code).functionName, "roki_设备");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, mGuid, code, dc, new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    public void clickOther(String code) {
        //警报时是否弹出？
        if(alarmDialog()){
            return;
        }
        switch (code) {
            case OvenModeName.roastModel:
                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "点击:烤模式", "roki_设备");
                    }
                }

                Bundle bundle1 = new Bundle();
                bundle1.putString(PageArgumentKey.Guid, mGuid);
                bundle1.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                bundle1.putString(PageArgumentKey.title, "烤模式");
                UIService.getInstance().postPage(PageKey.AbsOvenMode, bundle1);

                break;

            case OvenModeName.professionalBaking:
                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "点击:专业烘烤", "roki_设备");
                    }
                }
                paramShow(code);
                break;

            //专业模式
            case OvenModeName.professionalModel:
                String functionName = "专业模式";
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(OvenModeName.professionalModel)) {
                        functionName = otherList.get(i).functionName;
                    }
                }
                Bundle bundle2 = new Bundle();
                bundle2.putString(PageArgumentKey.Guid, mGuid);
                bundle2.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                bundle2.putString(PageArgumentKey.title, functionName);
                UIService.getInstance().postPage(PageKey.AbsOvenMode, bundle2);
                break;
            //烤箱自动烹饪菜谱
            case OvenModeName.roastCookbook:
                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "点击:烤箱自动菜谱", "roki_设备");
                    }
                }
                String platformCode = "";
                for (int i = 0; i < otherList.size(); i++) {
                    if (("roastCookbook").equals(otherList.get(i).functionCode)) {
                        String params = otherList.get(i).functionParams;
                        if (params != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(params);
                                platformCode = jsonObject.optString("platformCode");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                //挡板插入状态
//                if (oven.PlatInsertStatueValue == 0) {
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.RecipeId, DeviceType.RDKX);
                bd.putSerializable(PageArgumentKey.platformCode, platformCode);
                bd.putSerializable(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.RecipeCategoryList, bd);
//                } else {
//                    ToastUtils.show("此功能不需要隔板，请将隔板拆除", Toast.LENGTH_SHORT);
//                }

                break;
            //分区模式
            case OvenModeName.zoningMode:
                //挡板插入状态 0 未插入  1 插入
                if (oven.PlatInsertStatueValue == 0) {
                    ToastUtils.show("未检测到隔板，请先在腔体内部装入隔板。", Toast.LENGTH_LONG);
                    return;
                } else {
                    String zoningName = "分区模式";
                    for (int i = 0; i < otherList.size(); i++) {
                        if (otherList.get(i).functionCode.equals(OvenModeName.zoningMode)) {
                            zoningName = otherList.get(i).functionName;
                        }
                    }
                    Bundle bundle4 = new Bundle();
                    bundle4.putString(PageArgumentKey.Guid, mGuid);
                    bundle4.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                    bundle4.putString(PageArgumentKey.title, zoningName);
                    UIService.getInstance().postPage(PageKey.AbsOvenZoning, bundle4);
                }

                break;
            //本地自动菜谱
            case OvenModeName.localCookbook:
                String localCookbookName = "本地菜谱";
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(OvenModeName.localCookbook)) {
                        localCookbookName = otherList.get(i).functionName;
                    }
                }

                Bundle bundle5 = new Bundle();
                bundle5.putString(PageArgumentKey.Guid, mGuid);
                bundle5.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                bundle5.putString(PageArgumentKey.title, localCookbookName);
                UIService.getInstance().postPage(PageKey.AbsOvenLocalRecipe, bundle5);


                break;
            //烘烤DIY
            case OvenModeName.diy:
                String diyName = "烘烤DIY";
                for (int i = 0; i < otherList.size(); i++) {
                    if (otherList.get(i).functionCode.equals(OvenModeName.diy)) {
                        diyName = otherList.get(i).functionName;
                    }
                }
                Bundle bundle6 = new Bundle();
                bundle6.putString(PageArgumentKey.Guid, mGuid);
                bundle6.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                bundle6.putString(PageArgumentKey.title, diyName);
                UIService.getInstance().postPage(PageKey.AbsOvenBakeDiyList, bundle6);
                break;
            //烟机联动
            case OvenModeName.linkage:
                Bundle bundle7 = new Bundle();
                bundle7.putString(PageArgumentKey.Guid, mGuid);
                bundle7.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                UIService.getInstance().postPage(PageKey.AbsOvenFanLinkage, bundle7);
                break;

            default:
                ToastUtils.show("即将开放，敬请期待", Toast.LENGTH_SHORT);
                break;
        }


    }


    int cmd;
    String mode;
    String desc;

    private void paramShow(String code) {
        OvenExpParamBean ovenParams = null;
        try {
            ovenParams = JsonUtils.json2Pojo(paramMap.get(code), OvenExpParamBean.class);
            cmd = ovenParams.getParam().getCmd().getValue();
            mode = ovenParams.getParam().getModel().getValue();
            desc = ovenParams.getParam().getDesc().getValue();
            List<Integer> tempUp = ovenParams.getParam().getUpTemp().getValue();
            String tempUpDefault = ovenParams.getParam().getUpTempDefault().getValue();
            List<Integer> time = ovenParams.getParam().getMinute().getValue();
            List<Integer> tempDown = ovenParams.getParam().getDownTemp().getValue();
            String timeDefault = ovenParams.getParam().getMinuteDefault().getValue();
            String tempDiff = ovenParams.getParam().getTempDiff().getValue();
            String tempStart = ovenParams.getParam().getTempStart().getValue();
            String downMin = ovenParams.getParam().getTempMin().getValue();
            String desc = ovenParams.getParam().getDesc().getValue();
            List<Integer> tempT = TestDatas.createTempertureDatas(tempUp);
            List<Integer> timeT = TestDatas.createModeDataTime(time);
            int deNum1 = Integer.parseInt(tempUpDefault) - tempUp.get(0);
            int deDiff = Integer.parseInt(tempDiff);
            int deNum2 = deNum1 - deDiff;
            int deNum3 = Integer.parseInt(timeDefault) - time.get(0);
            int deStart = Integer.parseInt(tempStart);
            int min = Integer.parseInt(downMin);
            setDialogParam(tempT, tempDown, timeT, "℃", "℃", "分钟", desc, deNum1, deNum2,
                    deNum3, deDiff, deStart, min);
            if (dt != null) {
                if (!"".equals(dt)) {
                    ToolUtils.logEvent(dt, "开始专业烘烤:" + tempT + ":" + tempDown + ":" + timeT, "roki_设备");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setDialogParam(List<Integer> tempUp, List<Integer> tempDown, List<Integer> time,
                                String str1, String str2, String str3, String str4, int defNum1,
                                int defNum2, int defNum3, int stepC, int defaultValue, int min) {
        List<String> listButton = TestDatas.createExpDialogText(str1, str2, str3, str4);
        absOvenExpDialog = new AbsOvenExpDialog(cx, tempUp, tempDown, time, listButton, defNum1, defNum2,
                defNum3, stepC, defaultValue, min);
        absOvenExpDialog.showDialog(absOvenExpDialog);
        absOvenExpDialog.setListener(new AbsOvenExpDialog.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(int index1, int index2, int index3) {
                send(cmd, mode, index1, index2, index3);
            }
        });
    }


    private void send(int cmd, String mode, int setTempUp, int setTempDown, int setTime) {
        if (dt.equals("Q082A")) {
            oven.setOvenEXPModelRunMode((short) cmd, Short.decode(mode), (short) setTime,
                    (short) setTempUp, (short) setTempDown, null);
        } else {
            if (oven.status == OvenStatus.Off) {
                oven.setOvenStatus(OvenStatus.On, null);
            } else {
                oven.setOvenEXPModelRunMode((short) cmd, Short.decode(mode), (short) setTime,
                        (short) setTempUp, (short) setTempDown, null);
            }
        }

    }


}
