package com.robam.roki.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.j256.ormlite.stmt.query.In;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.paramCode;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.PlatformCode;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatusNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatusNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatusNew;
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.ui.view.RecipeParamShowView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/15.
 */
public class RecipeNewUtils {
    public static short WORKING = 1;
    public static short PAUSE = 2;
    public static short OFF = 3;
    public static short MICROOFF = 100;
    public static short PREHEAT = 9;

    /**
     * 验证当前菜谱当前菜谱
     */
    public static boolean CheckRecipeValid(Recipe recipe) {
        return recipe != null;
    }

    /**
     * 判断当前菜谱步骤 steps是否有效
     */
    public static boolean
    CheckRecipeStepsValid(Recipe recipe) {
        return !(!CheckRecipeValid(recipe) || recipe.js_cookSteps == null || recipe.js_cookSteps.size() == 0);
    }

    /**
     * 判断当前菜谱是否有品类模式
     */
    public static boolean ifRecipeHasCategory(Recipe recipe) {
        return CheckRecipeValid(recipe);
    }

    /**
     * 获取菜谱步骤
     */
    public static int getRecipeStepNo(Recipe recipe) {
        if (!CheckRecipeStepsValid(recipe))
            return 0;
        return recipe.js_cookSteps.size();
    }

    /**
     * 检查菜谱某步骤是否含有平台参数列表
     */
    public static boolean CheckRecipeStepsParam(CookStep cookstep) {
        return !(cookstep == null || cookstep.getjs_PlatformCodes() == null || cookstep.getjs_PlatformCodes().size() == 0);
    }

    /**
     * 获取平台参数by dp
     */
    public static Map<String, paramCode> getRecipeStepParamByDP(CookStep cookstep, String dp) {
        if (!CheckRecipeStepsParam(cookstep) || Strings.isNullOrEmpty(dp))
            return new HashMap<String, paramCode>();
        if (cookstep.getjs_PlatformCodes() == null || cookstep.getjs_PlatformCodes().size() == 0) {
            return new HashMap<String, paramCode>();
        }
        Map<String, paramCode> map = new HashMap<String, paramCode>();
        for (PlatformCode plat : cookstep.getjs_PlatformCodes()) {
            if (dp.equals(plat.platCode)) {
                for (paramCode paramCode : plat.getJs_paramCodes()) {
                    map.put(paramCode.code, paramCode);
                }
                break;
            }
        }
        return map;
    }

    /**
     * 获取默认平台
     */
    public static Map<String, paramCode> getRecipeDefaultStepParam(CookStep cookstep) {
        if (!CheckRecipeStepsParam(cookstep))
            return new HashMap<String, paramCode>();
        if (cookstep.getjs_PlatformCodes() == null || cookstep.getjs_PlatformCodes().size() == 0)
            return new HashMap<String, paramCode>();
        Map<String, paramCode> map = new HashMap<String, paramCode>();
        PlatformCode plat = cookstep.getjs_PlatformCodes().get(0);
        for (paramCode paramCode : plat.getJs_paramCodes()) {
            map.put(paramCode.code, paramCode);
        }
        return map;
    }

    public static boolean isRecipeIsStoveOfRecipe(Recipe recipe) {
        return !(recipe.getJs_dcs().size() > 1 || !DeviceType.RRQZ.equals(recipe.getJs_dcs().get(0)));
    }

    /**
     * 设置设备预下发指令
     */
    public static void setDevicePreSetModel(Context cx, IDevice iDevice, RecipeParamShowView stepPage, int current_PageItemIndex, Callback<Integer> callback) {
        if (iDevice == null) return;
        LogUtils.i("2020070102", "recipeParamShowView:::22:::" + stepPage.paramMap);
        if (DeviceType.RDKX.equals(iDevice.getDc())) {
            setOvenPreSetModel(iDevice, stepPage, callback);
        } else if (DeviceType.RZQL.equals(iDevice.getDc())) {
            setSteamPreSetModel(iDevice, stepPage, callback);
        } else if (DeviceType.RWBL.equals(iDevice.getDc())) {
            setMicrowavePreSetModel(iDevice, stepPage, callback);
        } else if (DeviceType.RRQZ.equals(iDevice.getDc())) {
            setStoveOnStart(iDevice, stepPage, callback);
        } else if (DeviceType.RZKY.equals(iDevice.getDc())) {
            setSteamAndOvenPreSetModel(iDevice, stepPage, callback);
        }
    }

    private static void setStoveOnStart(IDevice iDevice, RecipeParamShowView stepPage, final Callback<Integer> callback) {
        List<AbsFan> fan = Utils.getFan();
    }

    private static void setSteamAndOvenPreSetModel(IDevice iDevice, RecipeParamShowView stepPage, final Callback<Integer> callback) {

        if (iDevice.getDt().equals("DB620")||iDevice.getDt().equals("CQ920")){
            setSteamOvenPreSetParam(stepPage, (AbsSteameOvenOneNew) iDevice, callback);
        }else {
            setSteamOvenPreSetParam(stepPage, (AbsSteameOvenOne) iDevice, MsgKeys.setSteameOvenAutomaticMode_Req, callback);
        }

    }

    private static void setSteamOvenPreSetParam(RecipeParamShowView stepPage, final AbsSteameOvenOneNew steamOven,
                                                final Callback<Integer> callback) {
        try {
            if (!steamOven.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (steamOven.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                callback.onSuccess(1);
                return;
            }
            final Map<String, paramCode> paramMap = stepPage.paramMap;
            if (steamOven.powerState == SteamOvenOnePowerStatus.On && steamOven.powerOnStatus != SteamOvenOnePowerOnStatus.WorkingStatus) {
                sendCommand(steamOven, paramMap, callback);
            } else if (steamOven.powerState == SteamOvenOnePowerStatus.Wait || steamOven.powerState == SteamOvenOnePowerStatus.Off) {
                steamOven.setSteameOvenStatus_on(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendCommand(steamOven, paramMap, callback);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            } else {
                callback.onSuccess(2);
                return;
            }

        } catch (Exception e) {

        }


    }
    private static void setSteamOvenPreSetParam(RecipeParamShowView stepPage, final AbsSteameOvenOne steamOven,
                                                final short msgKeys, final Callback<Integer> callback) {
        try {
            if (!steamOven.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (steamOven.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                callback.onSuccess(1);
                return;
            }
            final Map<String, paramCode> paramMap = stepPage.paramMap;
            if (steamOven.powerState == SteamOvenOnePowerStatus.On && steamOven.powerOnStatus != SteamOvenOnePowerOnStatus.WorkingStatus) {
                sendCommand(steamOven, paramMap, callback);
            } else if (steamOven.powerState == SteamOvenOnePowerStatus.Wait || steamOven.powerState == SteamOvenOnePowerStatus.Off) {
                steamOven.setSteameOvenStatus_on(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendCommand(steamOven, paramMap, callback);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            } else {
                callback.onSuccess(2);
                return;
            }

        } catch (Exception e) {

        }
    }


    private static boolean WorkBeforeCheck(AbsSteameOvenOneNew steamOven,int mSteamModel){
        SteamOvenFaultEnum faultEnum = SteamOvenFaultEnum.match(steamOven.faultCode);
        if (SteamOvenFaultEnum.NO_FAULT != faultEnum&&steamOven.faultCode!=11) {
            if (faultEnum != null) {
                com.hjq.toast.ToastUtils.show(SteamOvenFaultEnum.match(steamOven.faultCode).getValue());
            }else {
                com.hjq.toast.ToastUtils.show("设备端故障未处理，请及时处理");
            }
            return false;
        }
        if (SteamOvenHelper.isWork2(steamOven.workState)) {
            com.hjq.toast.ToastUtils.show("设备已占用");
            return false;
        }
        //门已打开 而且不能开门工作
        if (!SteamOvenHelper.isDoorState(steamOven.doorState) && !SteamOvenHelper.isOpenDoorWork(SteamOvenModeEnum.match(mSteamModel)) ){
            com.hjq.toast.ToastUtils.show("门未关好，请检查并确认关好门");
            return false;
        }

        /**
         * 判断是否需要水
         */
        if (SteamOvenHelper.isWater(SteamOvenModeEnum.match(mSteamModel))) {
            if (SteamOvenHelper.isDescale(steamOven.descaleFlag)) {
                com.hjq.toast.ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");
                return false;
            }
            if (!SteamOvenHelper.isWaterBoxState(steamOven.waterBoxState)) {
                com.hjq.toast.ToastUtils.show("水箱已弹出，请检查水箱状态");
                return false;
            }
            if (!SteamOvenHelper.isWaterLevelState(steamOven.waterLevelState)) {
                com.hjq.toast.ToastUtils.show("水箱缺水，请加水");
                return false;
            }
        }
        return true;
    }
    private static void sendCommand(final AbsSteameOvenOneNew steamOven, Map<String, paramCode> paramMap, final Callback<Integer> callback) {

//        paramCode paramCode = paramMap.get(MsgParams.OvenSteamMode);
//        Log.e("结果",)
        if (!WorkBeforeCheck(steamOven,paramMap.get(MsgParams.OvenSteamMode).value)){
            return;
        }


        if (steamOven.workState==5){

            steamOven.setSteamWorkStatus(SteamOvenHelper.isPause(steamOven.workState) ? IntegStoveStatus.workCtrl_continue : IntegStoveStatus.workCtrl_time_out, (short) 4, new VoidCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess(0);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });

        }else {

            steamOven.setSteameOvenOneRunMode(Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamMode).value)),
                    Integer.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTime).value)) / 60,
                    Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTemp).value)), (short) 0, (short) 0, new VoidCallback() {
                        public void onSuccess() {

                            callback.onSuccess(0);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            callback.onSuccess(1);
                        }
                    });
        }
    }
    private static void sendCommand(final AbsSteameOvenOne steamOven, Map<String, paramCode> paramMap, final Callback<Integer> callback) {
        paramCode paramCode = paramMap.get(MsgParams.OvenSteamMode);
        if (paramCode != null && "EXP".equals(paramCode.valueName)) {
            steamOven.setSteameOvenOneRunMode(SteamOvenOneModel.EXP, Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTime).value / 60)), Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamUp).value)),
                    (short) 0, Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamBelow).value)), (short) 255, (short) 255, new VoidCallback() {
                        public void onSuccess() {
                            callback.onSuccess(0);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            callback.onSuccess(1);
                        }
                    });

        } else {
            steamOven.setSteameOvenOneRunMode(Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamMode).value)), Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTime).value / 60)), Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTemp).value)), (short) 0, new VoidCallback() {
                public void onSuccess() {
                    callback.onSuccess(0);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onSuccess(1);
                }
            });
        }
    }

    /**
     * 设置烤箱预下发指令工作, callback 0表示成功，1发送指令失败，2设备处于其他状态
     */
    private static void setOvenPreSetModel(IDevice iDevice, RecipeParamShowView stepPage, final Callback<Integer> callback) {
        List<AbsOven> oven = Utils.getOven();
        for (int i = 0; i < oven.size(); i++) {
            if (oven.get(i).getID().equals(iDevice.getID())) {
                LogUtils.i("2020070102", "recipeParamShowView:::333:::" + stepPage.toString());
                LogUtils.i("2020070102", "recipeParamShowView:::333:::" + stepPage.paramMap);
                setOvenPreSetParam(stepPage, oven.get(i), callback);
            }
        }
    }

    private static void setOvenPreSetParam(final RecipeParamShowView recipeStepDetailView, final AbsOven oven, final Callback<Integer> callback) {
        LogUtils.i("2020070102", "recipeParamShowView:::444:::" + recipeStepDetailView.toString());
        LogUtils.i("2020070102", "recipeParamShowView:::444:::" + recipeStepDetailView.paramMap);
        try {
            if (!oven.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (oven.status == OvenStatus.AlarmStatus) {
                callback.onSuccess(1);
                return;
            }
            final Map<String, paramCode> paramMap = recipeStepDetailView.paramMap;
            paramCode paramCode = recipeStepDetailView.paramMap.get(MsgParams.OvenMode);
            short model = 0;
            if ("快热".equals(paramCode.valueName)) {
                model = 1;
            } else if ("风焙烤".equals(paramCode.valueName)) {
                model = 2;
            } else if ("焙烤".equals(paramCode.valueName)) {
                model = 3;
            } else if ("底加热".equals(paramCode.valueName)) {
                model = 4;
            } else if ("风扇烤".equals(paramCode.valueName)) {
                model = 6;
            } else if ("烤烧".equals(paramCode.valueName)) {
                model = 7;
            } else if ("强烤烧".equals(paramCode.valueName)) {
                model = 8;
            } else if ("EXP".equals((paramCode.valueName))) {
                model = 9;
            } else if ("快速预热".equals(paramCode.valueName)) {
                model = 10;
            } else if ("煎烤".equals(paramCode.valueName)) {
                model = 11;
            } else if ("果蔬烘干".equals(paramCode.valueName)) {
                model = 12;
            } else if ("发酵".equals(paramCode.valueName)) {
                model = 13;
            } else if ("杀菌".equals(paramCode.valueName)) {
                model = 14;
            } else if ("保温".equals(paramCode.valueName)) {
                model = 15;
            } else if ("上层分区-快热".equals(paramCode.valueName)) {
                model = 16;
            } else if ("上层分区-风扇烤".equals(paramCode.valueName)) {
                model = 17;
            } else if ("上层分区-风焙烤".equals(paramCode.valueName)) {
                model = 18;
            } else if ("上层分区-烧烤".equals(paramCode.valueName)) {
                model = 19;
            } else if ("下层分区-风焙烤".equals(paramCode.valueName)) {
                model = 20;
            } else if ("下层分区-煎烤".equals(paramCode.valueName)) {
                model = 21;
            }
            if (oven.PlatInsertStatueValue == 0) {
                if (model > 15) {
                    callback.onSuccess(4);
                    return;
                }
            } else {
                if (model <= 15) {
                    callback.onSuccess(5);
                    return;
                }
            }
            if (oven.status == OvenStatus.On) {//当前烤箱为开支状态，直接发送预设指令 OvenTime  OvenTemp
                sendOvenCommand(oven, recipeStepDetailView, callback);
            } else if (oven.status == OvenStatus.Wait || oven.status == OvenStatus.Off) {//若烤箱待机或关闭，先开机后发指令
                oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("2020070102", "paramMap:::666:::" + paramMap.toString());

                        //问题
                        sendOvenCommand(oven, recipeStepDetailView, callback);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                        callback.onSuccess(1);
                    }
                });
            } else {
                callback.onSuccess(2);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void sendOvenCommand(final AbsOven oven, RecipeParamShowView recipeStepDetailView, final Callback<Integer> callback) {
        Map<String, paramCode> paramMap = recipeStepDetailView.paramMap;
        if (!IRokiFamily.RR039.equals(oven.getDt())) {
            LogUtils.i("202007010999", "paramMap:::" + recipeStepDetailView.paramMap.toString());
            paramCode paramCode = recipeStepDetailView.paramMap.get(MsgParams.OvenMode);
            short model = 0;
            if ("快热".equals(paramCode.valueName)) {
                model = 1;
            } else if ("风焙烤".equals(paramCode.valueName)) {
                model = 2;
            } else if ("焙烤".equals(paramCode.valueName)) {
                model = 3;
            } else if ("底加热".equals(paramCode.valueName)) {
                model = 4;
            } else if ("风扇烤".equals(paramCode.valueName)) {
                model = 6;
            } else if ("烤烧".equals(paramCode.valueName)) {
                model = 7;
            } else if ("强烤烧".equals(paramCode.valueName)) {
                model = 8;
            } else if ("EXP".equals((paramCode.valueName))) {
                model = 9;
            } else if ("快速预热".equals(paramCode.valueName)) {
                model = 10;
            } else if ("煎烤".equals(paramCode.valueName)) {
                model = 11;
            } else if ("果蔬烘干".equals(paramCode.valueName)) {
                model = 12;
            } else if ("发酵".equals(paramCode.valueName)) {
                model = 13;
            } else if ("杀菌".equals(paramCode.valueName)) {
                model = 14;
            } else if ("保温".equals(paramCode.valueName)) {
                model = 15;
            } else if ("上层分区-快热".equals(paramCode.valueName)) {
                model = 16;
            } else if ("上层分区-风扇烤".equals(paramCode.valueName)) {
                model = 17;
            } else if ("上层分区-风焙烤".equals(paramCode.valueName)) {
                model = 18;
            } else if ("上层分区-烧烤".equals(paramCode.valueName)) {
                model = 19;
            } else if ("下层分区-风焙烤".equals(paramCode.valueName)) {
                model = 20;
            } else if ("下层分区-煎烤".equals(paramCode.valueName)) {
                model = 21;
            }
            if (model > 19) {
                recipeStepDetailView.isSlice = true;
            } else {
                recipeStepDetailView.isSlice = false;
            }
            if (oven.PlatInsertStatueValue == 0) {
                if (model > 15) {
                    callback.onSuccess(4);
                    return;
                }
            } else {
                if (model <= 15) {
                    callback.onSuccess(5);
                    return;
                }
            }
             if (paramCode.valueName.equals("EXP")) {
                oven.setOvenRunMode((short) 9, Short.valueOf(String.valueOf(paramMap.get("OvenTime").value / 60)), Short.valueOf(String.valueOf(paramMap.get("OvenTempUp").value)),
                        (short) 0, (short) 0, (short) 0, (short) 1, Short.valueOf(String.valueOf(paramMap.get("OvenTempBelow").value)), (short) 255, (short) 255, new VoidCallback() {

                            @Override
                            public void onSuccess() {
                                callback.onSuccess(0);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                callback.onSuccess(1);
                            }
                        });
            } else {
                short ovenTime;
                short ovenTemp;
                if (OvenMode.SCFJ_KR == model || OvenMode.SCFJ_FSK == model ||
                        OvenMode.SCFJ_FBK == model || OvenMode.SCFJ_SK == model) {
                    ovenTime = (short) (paramMap.get("OvenUpLayerTime").value / 60);
                    ovenTemp = (short) paramMap.get("OvenUpLayerTemp").value;
                    oven.setOvenRunMode(
                            model,//mode
                            Short.valueOf(ovenTime),//setTime
                            Short.valueOf(ovenTemp),//setTempUp
                            (short) 0,//preflag
                            (short) 0,//recipeId
                            (short) 0,//recipeStep
                            (short) 0,//ArgumentNumber
                            (short) 0,//SetTempDown
                            new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    callback.onSuccess(0);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    callback.onSuccess(1);
                                }
                            });
                } else if (OvenMode.XCFJ_FBK == model || OvenMode.XCFJ_JK == model) {
                    ovenTime = (short) (paramMap.get("OvenDownLayerTime").value / 60);
                    ovenTemp = (short) paramMap.get("OvenDownLayerTemp").value;
//                    oven.setOvenRunModeTopAndBottom(
//                            (short) MsgKeys.SetOven_RunMode_Req,
//                            model,
//                            (short) 0,//setTime
//                            (short) 0,//setTempUp
//                            (short) 0,//preflag
//                            (short) 0,//recipeId
//                            (short) 0,//recipeStep
//                            (short) 2,//ArgumentNumber
//                            (short) 0,//SetTempDown
//                            (short) 255,//orderTime_min
//                            (short) 255,//orderTime_hour
//                            (short) 0,//Rotatebarbecue
//                            (short) Short.valueOf(ovenTemp),//setTemp2
//                            (short) Short.valueOf(ovenTime),//setTime2
//                            new VoidCallback() {
//                                @Override
//                                public void onSuccess() {
//                                    callback.onSuccess(0);
//                                }
//
//                                @Override
//                                public void onFailure(Throwable t) {
//                                    callback.onSuccess(1);
//                                }
//                            }
//
//                    );
                    LogUtils.i("202007010999", "model:" + model + " ovenTime:" + ovenTime + " ovenTemp:" + ovenTemp);
                    oven.setOvenSubstrateRunMode(
                            model,//mode
                            (short) 0,//setTime
                            (short) 0,//setTempUp
                            (short) 0,//preflag
                            (short) 0,//recipeId
                            (short) 0,//recipeStep
                            (short) 2,//ArgumentNumber
                            (short) 0,//SetTempDown
                            Short.valueOf(ovenTemp),
                            Short.valueOf(ovenTime),
                            new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    callback.onSuccess(0);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    callback.onSuccess(1);
                                }
                            });


                } else {
                    ovenTime = (short) (paramMap.get("OvenTime").value / 60);
                    ovenTemp = (short) paramMap.get("OvenTemp").value;
                    oven.setOvenRunMode(
                            model,//mode
                            Short.valueOf(ovenTime),//setTime
                            Short.valueOf(ovenTemp),//setTempUp
                            (short) 0,//preflag
                            (short) 0,//recipeId
                            (short) 0,//recipeStep
                            (short) 0,//ArgumentNumber
                            (short) 0,//SetTempDown
                            new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    callback.onSuccess(0);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    callback.onSuccess(1);
                                }
                            });
                }


            }

        } else {
            if (Short.valueOf(String.valueOf(paramMap.get("OvenMode").value)) == 9) {
                ToastUtils.show("039不支持EXP模式", Toast.LENGTH_SHORT);
                return;
            }

            oven.setOvenRecipeParams(Short.valueOf(String.valueOf(paramMap.get("OvenMode").value)),
                    Short.valueOf(String.valueOf(paramMap.get("OvenTime").value / 60)),
                    Short.valueOf(String.valueOf(paramMap.get("OvenTemp").value)), (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            callback.onSuccess(0);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20171130", "t:" + t.getMessage());
                            callback.onSuccess(1);
                        }
                    });
        }
    }

    private static void setSteamPreSetModel(IDevice iDevice, RecipeParamShowView stepPage, final Callback<Integer> callback) {
        List<AbsSteamoven> steamOven = Utils.getSteam();
        for (int i = 0; i < steamOven.size(); i++) {
            if (iDevice.getGuid().getGuid().equals(steamOven.get(i).getGuid().getGuid())) {
                //发送命令
                setSteamPreSetParam(stepPage, steamOven.get(i), callback);
            }
        }
    }

    /**
     * 设置蒸箱预下发指令工作, callback 0表示成功，1发送指令失败，2设备处于其他状态
     */

    private static void setSteamPreSetParam(RecipeParamShowView recipeStepDetailView, final AbsSteamoven steam, final Callback<Integer> callback) {
        try {
            if (!steam.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (steam.status == SteamStatus.AlarmStatus || steam.doorState == 0) {
                ToastUtils.show("电蒸箱报警,恢复后才可烹饪", Toast.LENGTH_SHORT);
                return;
            }
            final Map<String, paramCode> paramMap = recipeStepDetailView.paramMap;
            if (steam.status == SteamStatus.On) {//当前蒸箱为开支状态，直接发送预设指令 SteamTime  SteamTemp

                if (paramMap.get("SteamMode")!=null) {
                    int steamMode = paramMap.get("SteamMode").value;
                    steam.setSteamWorkMode(Short.valueOf(String.valueOf(steamMode)),
                            Short.valueOf(String.valueOf(paramMap.get("SteamTemp").value)),
                            Short.valueOf(String.valueOf(paramMap.get("SteamTime").value / 60)),
                            (short) 0,
                            new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    callback.onSuccess(0);
                                    LogUtils.i("202011111552","133:::success");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    callback.onSuccess(1);
                                    LogUtils.i("202011111552","133:::fail");
                                }
                            });
                }else{
                    steam.setSteamProMode(Short.valueOf(String.valueOf(paramMap.get("SteamTime").value / 60)),
                            Short.valueOf(String.valueOf(paramMap.get("SteamTemp").value)), 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    callback.onSuccess(0);
                                    LogUtils.i("202011111552","141:::success");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    callback.onSuccess(1);
                                    LogUtils.i("202011111552","141:::fail");
                                }
                            });
                }





            } else if (steam.status == SteamStatus.Wait || steam.status == SteamStatus.Off) {//若烤箱待机或关闭，先开机后发指令
                steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        if (paramMap.get("SteamMode")!=null) {
                            int steamMode = paramMap.get("SteamMode").value;
                            steam.setSteamWorkMode(Short.valueOf(String.valueOf(steamMode)),
                                    Short.valueOf(String.valueOf(paramMap.get("SteamTemp").value)),
                                    Short.valueOf(String.valueOf(paramMap.get("SteamTime").value / 60)),
                                    (short) 0,
                                    new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            callback.onSuccess(0);
                                            LogUtils.i("202011111552","133:::success");
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            callback.onSuccess(1);
                                            LogUtils.i("202011111552","133:::fail");
                                        }
                                    });
                        }else{
                            steam.setSteamProMode(Short.valueOf(String.valueOf(paramMap.get("SteamTime").value / 60)),
                                    Short.valueOf(String.valueOf(paramMap.get("SteamTemp").value)), 0, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            callback.onSuccess(0);
                                            LogUtils.i("202011111552","141:::success");
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            callback.onSuccess(1);
                                            LogUtils.i("202011111552","141:::fail");

                                        }
                                    });

                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                        callback.onSuccess(1);
                    }
                });
            } else {
                callback.onSuccess(2);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置微波炉预下发指令工作, callback 0表示成功，1发送指令失败，2设备处于其他状态
     */
    private static void setMicrowavePreSetModel(IDevice iDevice, RecipeParamShowView recipeStepDetailView, final Callback<Integer> callback) {
        try {
            AbsMicroWave microWave = null;
            List<AbsMicroWave> microWaves = Utils.getMicrowave();
            for (int i = 0; i < microWaves.size(); i++) {
                if (iDevice.getDt().equals(microWaves.get(i).getDt())) {
                    microWave = microWaves.get(i);
                }
            }
            if (!microWave.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (microWave.state == MicroWaveStatus.Alarm || microWave.doorState == 1) {
                callback.onSuccess(1);
                return;
            }
            final Map<String, paramCode> paramMap = recipeStepDetailView.paramMap;
            LogUtils.i("20180122", "MicroWaveMode:" + Short.valueOf(String.valueOf(paramMap.get("MicroWaveMode").value))
                    + "MicroWaveTime:" + Short.valueOf(String.valueOf(paramMap.get("MicroWaveTime").value)) + "MicroWavePower:"
                    + Short.valueOf(String.valueOf(paramMap.get("MicroWavePower").value))
            );
            String modelName = paramMap.get("MicroWaveMode").valueName;
            short pow = Short.valueOf(String.valueOf(paramMap.get("MicroWavePower").value));
            short power = pow;
            if (power == 0) return;
            if (microWave.state == MicroWaveStatus.Wait) {//当微波炉为待机状态
                microWave.setMicroWaveProModeHeat(Short.valueOf(String.valueOf(paramMap.get("MicroWaveMode").value)),
                        Short.valueOf(String.valueOf(paramMap.get("MicroWaveTime").value)),
                        power, (short) 0, new VoidCallback() {

                            @Override
                            public void onSuccess() {
                                //LogUtils.out("microWave.onSuccess()");
                                callback.onSuccess(0);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                //LogUtils.out("microWave.onFailure()");
                                callback.onSuccess(1);
                            }
                        });
            } else {
                //LogUtils.out("microWave is not MicroWaveStatus.Wait");
                callback.onSuccess(2);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static short microPower(String name, short pow) {
        short temp = 0;
        if ("组合加热".equals(name)) {
            if (pow == 6) {
                return temp = 10;
            } else if (pow == 4) {
                return temp = 11;
            } else if (pow == 2) {
                return temp = 12;
            }
        } else if ("烧烤加热".equals(name)) {
            if (pow == 6) {
                return temp = 7;
            } else if (pow == 4) {
                return temp = 8;
            } else if (pow == 2) {
                return temp = 9;
            }
        } else {
            return pow;
        }
        return 0;
    }


    /**
     * 设置设备开始指令
     */
    public static void setDeviceStatusModel(Context cx, short status, IDevice iDevice, Callback<Integer> callback) {
        try {
            LogUtils.i("2020035", "status::" + status);
            if (iDevice == null) return;
            if (DeviceType.RDKX.equals(iDevice.getDc())) {
                if (status == RecipeNewUtils.WORKING)
                    status = OvenStatus.Working;
                else if (status == RecipeNewUtils.PAUSE)
                    status = OvenStatus.Pause;
                else if (status == RecipeNewUtils.OFF)
                    status = OvenStatus.Off;

                setOvenStatusModel(status, iDevice, callback);
            } else if (DeviceType.RZQL.equals(iDevice.getDc())) {
                if (status == RecipeNewUtils.WORKING)
                    status = SteamStatus.Working;
                else if (status == RecipeNewUtils.PAUSE)
                    status = SteamStatus.Pause;
                else if (status == RecipeNewUtils.OFF)
                    status = SteamStatus.Off;

                setSteamStatusModel(status, iDevice, callback);
            } else if (DeviceType.RWBL.equals(iDevice.getDc())) {
                if (status == RecipeNewUtils.WORKING)
                    status = MicroWaveStatus.Run;
                else if (status == RecipeNewUtils.PAUSE)
                    status = MicroWaveStatus.Pause;
                else if (status == RecipeNewUtils.OFF)
                    status = RecipeUtil.MICROOFF;
                setMicroStatusModel(status, iDevice, callback);
            } else if (DeviceType.RZKY.equals(iDevice.getDc())) {
                LogUtils.i("20171122", "status::" + status);
                if (iDevice.getDt().equals("DB620")||iDevice.getDt().equals("CQ920")){


                }else {
                    if (status == SteamOvenOnePowerOnStatus.WorkingStatus)
                        status = SteamOvenOnePowerOnStatus.WorkingStatus;
                    if (status == SteamOvenOnePowerOnStatus.Pause)
                        status = SteamOvenOnePowerOnStatus.Pause;
                    if (status == SteamOvenOnePowerStatus.RecipeOff)
                        status = SteamOvenOnePowerStatus.RecipeOff;
                }

                setSteamAndOvenStatusModel(status, iDevice, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setSteamAndOvenStatusModel(short status, IDevice iDevice, final Callback<Integer> callback) {
//        AbsSteameOvenOne steameOvenOne = Utils.getDefaultSteameOven();

        if (iDevice.getDt().equals("DB620")||iDevice.getDt().equals("CQ920")){
            setSteamAndOvenStatusParam(status, (AbsSteameOvenOneNew) iDevice, callback);
        }else {
            setSteamAndOvenStatusParam(status, (AbsSteameOvenOne) iDevice, callback);
        }
    }
    private static void setSteamAndOvenStatusParam(short status, final AbsSteameOvenOneNew steamOven, final Callback<Integer> callback) {
        if (!steamOven.isConnected()) {
            callback.onSuccess(3);
            return;
        }
        Log.e("发送",status+"---");
//        if (steamOven.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
//            callback.onSuccess(4);
//            return;
//        }
//        if (status == SteamOvenOnePowerOnStatus.WorkingStatus) {
//            if (steamOven.powerState != SteamOvenOnePowerOnStatusNew.Open//SteamOvenOnePowerStatus.On
//                    && steamOven.workState != SteamOvenOneWorkStatusNew.PreHeat
//                    && steamOven.workState != SteamOvenOneWorkStatusNew.Working) {
//                callback.onSuccess(2);
//                return;
//            }
//
//            if (steamOven.powerOnStatus != SteamOvenOnePowerOnStatusNew.Working) {
//                callback.onSuccess(2);
//                return;
//            }
//
//        LogUtils.i("20171122结果ooo", "steamOven::" + steamOven.powerState + " " + status);

                short key=status==0?SteamOvenOnePowerStatusNew.powerCtrlKey:SteamOvenOnePowerStatusNew.workCtrlKey;
                steamOven.setSteamWorkStatus(status, key, new VoidCallback() {


                    @Override
                    public void onSuccess() {
                        callback.onSuccess(0);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        callback.onSuccess(1);
                    }
                });


//        if (status == 1) {
////            steamOven.setSteameOvenStatus();
//            steamOven.setSteameOvenStatus_Off( new VoidCallback() {
//
//
//                @Override
//                public void onSuccess() {
//                    callback.onSuccess(0);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    callback.onSuccess(1);
//                }
//            });
//        }else{


//            steamOven.setSteameOvenStatus_on( );

//            steamOven.setSteameOvenStatus();
        }

    private static void setSteamAndOvenStatusParam(short status, final AbsSteameOvenOne steamOven, final Callback<Integer> callback) {
        if (!steamOven.isConnected()) {
            callback.onSuccess(3);
            return;
        }
        if (steamOven.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
            callback.onSuccess(4);
            return;
        }
        if (status == SteamOvenOnePowerOnStatus.WorkingStatus) {
            if (steamOven.powerState != SteamOvenOnePowerStatus.On && steamOven.workState != SteamOvenOneWorkStatus.PreHeat
                    && steamOven.workState != SteamOvenOneWorkStatus.Working) {
                callback.onSuccess(2);
                return;
            }
        } else if (status == SteamOvenOnePowerOnStatus.Pause) {
            if (steamOven.powerOnStatus != SteamOvenOnePowerOnStatus.WorkingStatus) {
                callback.onSuccess(2);
                return;
            }
        }
        LogUtils.i("20171122", "steamOven::" + steamOven.powerState + " " + status);
        if (status == SteamOvenOnePowerStatus.RecipeOff) {
            steamOven.setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.NoStatus, new VoidCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess(0);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onSuccess(1);
                }
            });
        } else {
            LogUtils.i("20180126", "power:::" + steamOven.powerState + " status::" + status);
//            steamOven.setSteamOvenOneStatusControl(status, new VoidCallback() {
//                @Override
//                public void onSuccess() {
//                    callback.onSuccess(0);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    callback.onSuccess(1);
//                }
//            });
            //以前注释掉的
            steamOven.setSteameOvenStatus(steamOven.powerState, status, new VoidCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess(0);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onSuccess(1);
                }
            });
        }
    }

    /**
     * 设置烤箱指令开始工作   callback 0 成功，1 指令发送失败，2 下发参数失败,请检查设备,3 设备已离线
     */
    private static void setOvenStatusModel(short status, IDevice iDevice, final Callback<Integer> callback) {
        List<AbsOven> oven = Utils.getOven();
        if (iDevice instanceof AbsOven) {
            String guid = iDevice.getGuid().getGuid();
            for (int i = 0; i < oven.size(); i++) {
                DeviceGuid deviceGuid = oven.get(i).getGuid();
                if (null != deviceGuid) {
                    String guidGuid = deviceGuid.getGuid();
                    if (guid.equals(guidGuid)) {
                        setOvenStatusParam(status, oven.get(i), callback);
                    }
                }
            }
        }
    }

    private static void setOvenStatusParam(short status, AbsOven oven, final Callback<Integer> callback) {
        try {
            if (!oven.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (oven.status == OvenStatus.AlarmStatus) {//判断有无报警
                callback.onSuccess(4);
                return;
            }
            if (status == OvenStatus.Working) {//开始工作之前，利用烤箱状态和温度时间去判断当前是否处于预设状态
                if (((oven.status2Values != OvenStatus.On && oven.status2Values != OvenStatus.Pause && oven.status2Values != OvenStatus.PreHeat)) && (oven.status != OvenStatus.On && oven.status != OvenStatus.Pause && oven.status != OvenStatus.PreHeat)) {//工作之前判断是否从预设或暂停状态进来
                    callback.onSuccess(2);
                    return;
                }
            } else if (status == OvenStatus.Pause) {//暂停工作之前，判断烤箱是否处于工作工作状态或者预热状态
                if ((oven.status2Values != OvenStatus.Working && oven.status2Values != OvenStatus.PreHeat) && (oven.status != OvenStatus.Working && oven.status != OvenStatus.PreHeat)) {
                    callback.onSuccess(2);
                    return;
                }
            }
            oven.setRecipeOvenStatus(status, new VoidCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess(0);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onSuccess(1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setSteamStatusModel(short status, IDevice iDevice, final Callback<Integer> callback) {
        LogUtils.i("20180207", "steam_status:" + status + " setSteamStatusModel");
        List<AbsSteamoven> steamOven = Utils.getSteam();
        for (int i = 0; i < steamOven.size(); i++) {
            if (iDevice.getDt().equals(steamOven.get(i).getDt())) {
                //发送命令
                LogUtils.i("20180207", "steam_status111:" + status + " setSteamStatusModel");
                setSteamStatusParam(status, steamOven.get(i), callback);
            }
        }
    }


    /**
     * 设置蒸箱指令开始工作   callback 0 成功，1 指令发送失败，2 下发参数失败,请检查设备,3 设备已离线
     */
    private static void setSteamStatusParam(short status, final AbsSteamoven steam, final Callback<Integer> callback) {
        try {
            // final Steam209 steam = (Steam209) Utils.getDefaultSteam();
            if (!steam.isConnected()) {
                callback.onSuccess(3);
                return;
            }
           /* if (steam.status == SteamStatus.AlarmStatus || steam.doorState == 0) {//判断有无报警
                callback.onSuccess(4);
                return;
            }*/
            if (status == SteamStatus.Working) {//开始工作之前，利用蒸箱状态和温度时间去判断当前是否处于预设状态
                if (steam.status != SteamStatus.On && steam.status != SteamStatus.Pause && steam.status != SteamStatus.PreHeat) {//工作之前判断是否从预设或暂停状态进来
                    callback.onSuccess(2);
                    return;
                }
            } else if (status == SteamStatus.Pause) {//暂停工作之前，判断蒸箱是否处于工作工作状态
                if (steam.status != SteamStatus.Working && steam.status != SteamStatus.PreHeat) {
                    callback.onSuccess(2);
                    return;
                }
            }
            LogUtils.i("20180207", "steam_status:" + status + " ffff");
            steam.setSteamStatus(status, new VoidCallback() {

                @Override
                public void onSuccess() {
                    LogUtils.i("20180207", "success" + " 3333");
                    callback.onSuccess(0);
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 设置微波炉指令开始工作   callback 0 成功，1 指令发送失败，2 下发参数失败,请检查设备,3 设备已离线
     */
    private static void setMicroStatusModel(short status, IDevice iDevice, final Callback<Integer> callback) {
        try {
            AbsMicroWave microWave = null;
            List<AbsMicroWave> microWaveList = Utils.getMicrowave();
            for (int i = 0; i < microWaveList.size(); i++) {
                if (iDevice.getDt().equals(microWaveList.get(i).getDt())) {
                    microWave = microWaveList.get(i);
                }
            }
            if (!microWave.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (microWave.state == MicroWaveStatus.Alarm || microWave.doorState == 1) {
                callback.onSuccess(4);
                return;
            }
            if (status == MicroWaveStatus.Run) {//开始工作之前
                if (microWave.state != MicroWaveStatus.Setting && microWave.state != MicroWaveStatus.Pause) {//工作之前判断是否从预设或暂停状态进来
                    callback.onSuccess(2);
                    return;
                }
            } else if (status == MicroWaveStatus.Pause) {//暂停工作之前，判断微波炉是否处于工作工作状态
                if (microWave.state != MicroWaveStatus.Run) {
                    callback.onSuccess(2);
                    return;
                }
            } else if (status == RecipeUtil.MICROOFF) {
                if (microWave.state == MicroWaveStatus.Run) {
                    status = MicroWaveStatus.Setting;
                } else if (microWave.state == MicroWaveStatus.Pause) {
                    status = MicroWaveStatus.Wait;
                } else {
                    status = MicroWaveStatus.Wait;
                }
            }
            microWave.setMicroWaveState(status, new VoidCallback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess(0);
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onSuccess(1);
                }
            });

        } catch (Exception e) {

        }
    }

}
