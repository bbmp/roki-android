package com.robam.roki.ui.page.device.steam;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Oven.OvenModeName;
import com.robam.common.pojos.device.Steamoven.SteamMode;
import com.robam.common.pojos.device.Steamoven.SteamModeName;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.AbsOvenModeSettingDialog;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.robam.common.util.FanLockUtils.mGuid;

public class SteamSubPage extends AbsSteamBasePage {

    boolean falg = true;

    public void clickMain(String code) {
//        if (getIsCon()) {
//            ToastUtils.show(getString(R.string.oven_dis_con), Toast.LENGTH_SHORT);
//            return;
//        }
        switch (code) {
            case OvenModeName.more:
                if (falg) {
                    ovenFirstView.setUpData(moreList);
                    falg = false;
                } else {
                    ovenFirstView.removeMoreView();
                    falg = true;
                }

                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "点击蒸箱场景功能:" + "更多", "roki_设备");
                    }
                }
                break;

            default:
                goRecipe(code);
                //sendMul(code);
                break;
        }
    }

    private int cmd;
    private String mode;
    private String mCode;

    //跳转到菜谱
    private void goRecipe(String code) {
        mCode = code;
        if (paramMapMore == null) {
            return;
        }
        try {
            String functionName = paramMapMore.get(code).functionName;
            if (dt != null) {
                if (!"".equals(dt)) {
                    ToolUtils.logEvent(dt, "点击蒸箱场景功能:" + functionName, "roki_设备");
                }
            }
            String funParam = paramMapMore.get(code).functionParams;
            JSONObject jsonObject = new JSONObject(funParam);
            if (!isSpecial) {
                long cookId = jsonObject.getLong("cookbookId");
                RecipeDetailPage.show(cookId, RecipeDetailPage.DeviceRecipePage,
                        RecipeRequestIdentification.RECIPE_SORTED);
            } else {
                JSONObject obj = jsonObject.optJSONObject(code);
                cmd = obj.optInt("cmd");
                mode = obj.optJSONObject("mode").optString("value");
                JSONArray minute = obj.optJSONObject("minute").optJSONArray("value");
                String defaultMinute = obj.optJSONObject("defaultMinute").optString("value");
                JSONArray temp = obj.optJSONObject("temp").optJSONArray("value");
                String defaultTemp = obj.optJSONObject("defaultTemp").optString("value");
                String desc = obj.optJSONObject("desc").optString("value");

                List<Integer> tempList = new ArrayList<>();
                for (int i = 0; i < temp.length(); i++) {
                    Integer tem = (Integer) temp.get(i);
                    tempList.add(tem);
                }
                List<Integer> minuteList = new ArrayList<>();
                for (int i = 0; i < minute.length(); i++) {
                    Integer tim = (Integer) minute.get(i);
                    minuteList.add(tim);
                }
                List<Integer> temps = TestDatas.createModeDataTemp(tempList);
                List<Integer> minutes = TestDatas.createModeDataTime(minuteList);
                int defTemp = Integer.parseInt(defaultTemp) - tempList.get(0);
                int defaultMins = Integer.parseInt(defaultMinute) - minuteList.get(0);

                if (steam.doorState == 0) {
                    ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
                    return;
                }
                if (steam.descaleModeStageValue != 0||steam.WeatherDescalingValue == 1) {
                    descalingDialog();
                    return;
                }
                setDialogParam(temps, minutes, "℃", "分钟", defTemp, defaultMins, desc);
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
                LogUtils.i("202011041438", "mCode:::success");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void clickOther(String code) {
//        if (getIsCon()) {
//            ToastUtils.show(getString(R.string.oven_dis_con), Toast.LENGTH_SHORT);
//            return;
//        }
        if (isSpecial) {
            switch (code) {
                case SteamModeName.steamingMode:
                    if (steam.doorState == 0) {
                        ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(PageArgumentKey.Guid, mGuid);
                    bundle.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                    bundle.putString(PageArgumentKey.title, "蒸模式");
                    UIService.getInstance().postPage(PageKey.AbsSteamMode, bundle);
                    break;
                case SteamModeName.steamerCookbook:
                    if (steam.doorState == 0) {
                        ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (dt != null) {
                        if (!"".equals(dt)) {
                            ToolUtils.logEvent(dt, "点击:蒸箱自动烹饪菜谱", "roki_设备");
                        }
                    }


                    String platformCode = "";
                    for (int i = 0; i < otherList.size(); i++) {
                        if ((SteamModeName.steamerCookbook).equals(otherList.get(i).functionCode)) {
                            String params = otherList.get(i).functionParams;
                            try {
                                if (params != null && !"".equals(params)) {
                                    JSONObject jsonObject = new JSONObject(params);
                                    platformCode = jsonObject.optString("platformCode");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Bundle autoRecipesBd = new Bundle();
                    autoRecipesBd.putString(PageArgumentKey.RecipeId, DeviceType.RZQL);
                    autoRecipesBd.putString(PageArgumentKey.platformCode, platformCode);
                    autoRecipesBd.putString(PageArgumentKey.Guid, mGuid);
                    UIService.getInstance().postPage(PageKey.RecipeCategoryList, autoRecipesBd);


                    break;
                //专业模式
                case SteamModeName.professionalModel:
                    if (steam.doorState == 0) {
                        ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (dt != null) {
                        if (!"".equals(dt)) {
                            ToolUtils.logEvent(dt, "点击:蒸箱专业模式", "roki_设备");
                        }
                    }
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(PageArgumentKey.Guid, mGuid);
                    bundle1.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                    bundle1.putString(PageArgumentKey.title, "专业模式");
                    bundle1.putString(PageArgumentKey.descaling, needDescalingParams);
                    UIService.getInstance().postPage(PageKey.SteamProMode, bundle1);

                    break;
                //本地自动菜谱
                case SteamModeName.localCookbook:
                    if (steam.doorState == 0) {
                        ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (dt != null) {
                        if (!"".equals(dt)) {
                            ToolUtils.logEvent(dt, "点击:本地菜谱", "roki_设备");
                        }
                    }


                    Bundle bundle2 = new Bundle();
                    bundle2.putString(PageArgumentKey.Guid, mGuid);
                    bundle2.putString(PageArgumentKey.descaling, needDescalingParams);
                    bundle2.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                    bundle2.putString(PageArgumentKey.title, "本地菜谱");
                    UIService.getInstance().postPage(PageKey.SteamLocalRecipe, bundle2);


                    break;
                //diy菜谱
                case SteamModeName.diy:
//                    if (steam.doorState == 0) {
//                        ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
//                        return;
//                    }
                    if (dt != null) {
                        if (!"".equals(dt)) {
                            ToolUtils.logEvent(dt, "点击:烹饪DIY", "roki_设备");
                        }
                    }
                    Bundle bundle3 = new Bundle();
                    bundle3.putString(PageArgumentKey.Guid, mGuid);
                    bundle3.putString(PageArgumentKey.descaling, needDescalingParams);
                    bundle3.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                    bundle3.putString(PageArgumentKey.title, "烹饪DIY");
                    UIService.getInstance().postPage(PageKey.SteamDiyList, bundle3);
                    break;
                //烟机联动
                case SteamModeName.linkage:
                    if (steam.doorState == 0) {
                        ToastUtils.show("门未关好，请先关好门", Toast.LENGTH_SHORT);
                        return;
                    }
                    Bundle bundle4 = new Bundle();
                    bundle4.putString(PageArgumentKey.Guid, mGuid);
                    bundle4.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                    UIService.getInstance().postPage(PageKey.SteamFanLinkage, bundle4);
                    break;
                default:
                    ToastUtils.show("即将开放，敬请期待", Toast.LENGTH_SHORT);
                    break;
            }
        } else {
            switch (code) {
                case SteamModeName.steamingMode:
                    Bundle bundle = new Bundle();
                    bundle.putString(PageArgumentKey.Guid, mGuid);
                    bundle.putSerializable(PageArgumentKey.List, (Serializable) otherList);
                    bundle.putString(PageArgumentKey.title, "蒸模式");
                    UIService.getInstance().postPage(PageKey.AbsSteamMode, bundle);
                    break;
                case SteamModeName.steamerCookbook:
                    if (dt != null) {
                        if (!"".equals(dt)) {
                            ToolUtils.logEvent(dt, "点击:蒸箱自动烹饪菜谱", "roki_设备");
                        }
                    }
                    String platformCode = "";
                    for (int i = 0; i < otherList.size(); i++) {
                        if ((SteamModeName.steamerCookbook).equals(otherList.get(i).functionCode)) {
                            String params = otherList.get(i).functionParams;
                            try {
                                if (params != null && !"".equals(params)) {
                                    JSONObject jsonObject = new JSONObject(params);
                                    platformCode = jsonObject.optString("platformCode");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.platformCode, platformCode);
                    bd.putString(PageArgumentKey.RecipeId, DeviceType.RZQL);
                    bd.putString(PageArgumentKey.Guid, mGuid);
                    UIService.getInstance().postPage(PageKey.RecipeCategoryList, bd);
                    break;
                default:
                    ToastUtils.show("即将开放，敬请期待", Toast.LENGTH_SHORT);
                    break;
            }
        }

    }


    private void setDialogParam(final List<Integer> temp,
                                List<Integer> time,
                                String str1,
                                String str2,
                                int defNum1,
                                int defNum2,
                                final String str3) {
        List<String> listButton = TestDatas.createDialogText(str1, str2, "取消", "开始工作", str3);
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

        if (steam.status == SteamStatus.Off || steam.status == SteamStatus.Wait) {
            steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom(cmd, mode, setTime, setTemp);

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200704", t.getMessage());
                }
            });
        } else {

            sendCom(cmd, mode, setTime, setTemp);

        }


    }


    private void sendCom(int cmd, String mode, int setTime, int setTemp) {
        steam.setSteamCookModule((short) cmd,
                Short.decode(mode), (short) setTemp, (short) setTime,
                (short) 0, (short) 0, (short) 0, (short) 0, (short) 0,
                (short) 0, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("202011041438", "mCode:::" + mCode);
                        sendMul(mCode);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20200704", t.getMessage());

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
