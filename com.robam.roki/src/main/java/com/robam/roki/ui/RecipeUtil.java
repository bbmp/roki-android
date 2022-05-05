package com.robam.roki.ui;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.VoidCallback;
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
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.ui.page.RecipeStepPage;
import com.robam.roki.ui.view.RecipeStepDetailView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/15.
 */
public class RecipeUtil {
    public static short WORKING = 1;
    public static short PAUSE = 2;
    public static short OFF = 3;
    public static short MICROOFF = 100;
    public static short PREHEAT = 9;

    public static short WORKRUN906 = 2;
    public static short POWERONSTATE = 0;

    /**
     * 验证当前菜谱当前菜谱
     */
    public static boolean CheckRecipeValid(Recipe recipe) {
        return recipe != null;
    }

    /**
     * 判断当前菜谱步骤 steps是否有效
     */
    public static boolean CheckRecipeStepsValid(Recipe recipe) {
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
        if (map.size() == 0) {
            List<PlatformCode> platformCodes = cookstep.getjs_PlatformCodes();
            PlatformCode platformCode = platformCodes.get(0);
            for (paramCode paramCode : platformCode.getJs_paramCodes()) {
                map.put(paramCode.code, paramCode);
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
    public static void setDevicePreSetModel(Context cx, RecipeStepPage stepPage, int current_PageItemIndex, Callback<Integer> callback) {
        //LogUtils.out(current_PageItemIndex + "");
        RecipeStepDetailView recipeStepDetailView = (RecipeStepDetailView) stepPage.adapter.getViews().get(current_PageItemIndex);
        if (DeviceType.RDKX.equals(recipeStepDetailView.category)) {
            //recipeStepDetailView.deviceInfo.dp;
            if (true) {//将来再次判断设备平台对应的指令发送形式
                setOvenPreSetModel(recipeStepDetailView, callback);
            }
        } else if (DeviceType.RZQL.equals(recipeStepDetailView.category)) {
            if (true) {//将来再次判断设备平台对应的指令发送形式
                setSteamPreSetModel(recipeStepDetailView, callback);
            }

        } else if (DeviceType.RWBL.equals(recipeStepDetailView.category)) {
            if (true) {//将来再次判断设备平台对应的指令发送形式
                setMicrowavePreSetModel(recipeStepDetailView, callback);
            }
        } else if (DeviceType.RZKY.equals(recipeStepDetailView.category)) {
            setSteamAndOvenPreSetModel(recipeStepDetailView, callback);
        }
    }

    private static void setSteamAndOvenPreSetModel(RecipeStepDetailView recipeStepDetailView, final Callback<Integer> callback) {
        AbsSteameOvenOne steamAndOven = Utils.getDefaultSteameOven();
        setSteamOvenPreSetParam(recipeStepDetailView, steamAndOven, MsgKeys.setSteameOvenAutomaticMode_Req, callback);
    }

    /**
     * 设置烤箱预下发指令工作, callback 0表示成功，1发送指令失败，2设备处于其他状态
     */
    private static void setOvenPreSetModel(RecipeStepDetailView recipeStepDetailView, final Callback<Integer> callback) {
        LogUtils.i("20171116", "OvenPreSetModel:" + recipeStepDetailView.type);
        List<AbsOven> oven = Utils.getOven();
        for (int i = 0; i < oven.size(); i++) {
            if (recipeStepDetailView.type != null && recipeStepDetailView.type.equals(oven.get(i).getDt())) {
                setOvenPreSetParam(recipeStepDetailView, oven.get(i), MsgKeys.SetOven_RunMode_Req, callback);
            }
        }

    }

    private static void setSteamOvenPreSetParam(RecipeStepDetailView recipeStepDetailView, final AbsSteameOvenOne steamOven,
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
            final Map<String, paramCode> paramMap = recipeStepDetailView.paramMap;
            if (steamOven.powerStatus == SteamOvenOnePowerStatus.On && steamOven.powerOnStatus != SteamOvenOnePowerOnStatus.WorkingStatus) {
                sendCommand(steamOven, paramMap, callback);
            } else if (steamOven.powerStatus == SteamOvenOnePowerStatus.Wait || steamOven.powerStatus == SteamOvenOnePowerStatus.Off) {
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

    private static void sendCommand(final AbsSteameOvenOne steamOven, Map<String, paramCode> paramMap, final Callback<Integer> callback) {
        paramCode paramCode = paramMap.get(MsgParams.OvenSteamMode);
//        LogUtils.i("20171214","paramCode::"+paramCode.valueName);
        if (paramCode.valueName.equals("EXP")) {
            steamOven.setSteameOvenOneRunMode(SteamOvenOneModel.EXP, Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTime).value / 60)), Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamUp).value)),
                    (short) 1, Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamBelow).value)), (short) 255, (short) 255, new VoidCallback() {
                        public void onSuccess() {
                            callback.onSuccess(0);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            callback.onSuccess(1);
                        }
                    });

        } else {
            LogUtils.i("20171130", "here is xiafa:" + Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamMode).value)));
            steamOven.setSteameOvenOneRunMode(Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamMode).value)), Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTime).value / 60)), Short.valueOf(String.valueOf(paramMap.get(MsgParams.OvenSteamTemp).value)), (short) 1, new VoidCallback() {
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

    private static void setOvenPreSetParam(RecipeStepDetailView recipeStepDetailView, final AbsOven oven,
                                           final short msgKeys, final Callback<Integer> callback) {
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
            if (oven.status == OvenStatus.On) {//当前烤箱为开支状态，直接发送预设指令 OvenTime  OvenTemp
                sendOvenCommand(oven, paramMap, msgKeys, callback);
            } else if (oven.status == OvenStatus.Wait || oven.status == OvenStatus.Off) {//若烤箱待机或关闭，先开机后发指令
                oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        sendOvenCommand(oven, paramMap, msgKeys, callback);
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

    private static void sendOvenCommand(final AbsOven oven, Map<String, paramCode> paramMap, final short msgKeys, final Callback<Integer> callback) {
        if (oven.getGuid().toString().contains("016") || oven.getGuid().toString().contains("026") ||
                oven.getGuid().toString().contains("028") || oven.getGuid().toString().contains("075")) {
            paramCode paramCode = paramMap.get(MsgParams.OvenMode);
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
            }


            if (paramCode.valueName.equals("EXP")) {

                oven.setOvenRunMode((short) 9, Short.valueOf(String.valueOf(paramMap.get("OvenTime").value / 60)), Short.valueOf(String.valueOf(paramMap.get("OvenTempUp").value)),
                        (short) 1, (short) 0, (short) 0, (short) 1, Short.valueOf(String.valueOf(paramMap.get("OvenTempBelow").value)), (short) 255, (short) 255, new VoidCallback() {

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
                LogUtils.i("20171213", " time " + paramMap.get("OvenTime").value / 60 + " Temp " + paramMap.get("OvenTemp").value);
                oven.setOvenRunMode(model, Short.valueOf(String.valueOf(paramMap.get("OvenTime").value / 60)), Short.valueOf(String.valueOf(paramMap.get("OvenTemp").value)), (short) 1, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
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

        } else {
            LogUtils.i("20171130", "msgKeys::" + msgKeys);
            LogUtils.i("20171130", "OvenTime: " + paramMap.get("OvenTime").value / 60);
            LogUtils.i("20171130", "OvenTime: " + paramMap.get("OvenTemp").value);
            oven.setOvenRecipeParams(Short.valueOf(String.valueOf(paramMap.get("OvenMode").value)),
                    Short.valueOf(String.valueOf(paramMap.get("OvenTime").value / 60)),
                    Short.valueOf(String.valueOf(paramMap.get("OvenTemp").value)), (short) 1, new VoidCallback() {
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

    private static void setSteamPreSetModel(RecipeStepDetailView recipeStepDetailView, final Callback<Integer> callback) {
        List<AbsSteamoven> steam = Utils.getSteam();
        for (int i = 0; i < steam.size(); i++) {
            if (recipeStepDetailView.type != null && recipeStepDetailView.type.equals(steam.get(i).getDt())) {
                setSteamPreSetParam(recipeStepDetailView, steam.get(i), callback);
            }
        }
      /*  if(Utils.getDefaultSteam()!=null&&Utils.getDefaultSteam().getGuid().toString().contains("209")){
            Steam209 steam= (Steam209) Utils.getDefaultSteam();
            setSteamPreSetParam(recipeStepDetailView,steam,callback);
        }else if (Utils.getDefaultSteam()!=null&&Utils.getDefaultSteam().getGuid().toString().contains("226")){
            Steam226 steam= (Steam226) Utils.getDefaultSteam();
            setSteamPreSetParam(recipeStepDetailView,steam,callback);
        }*/
    }

    /**
     * 设置蒸箱预下发指令工作, callback 0表示成功，1发送指令失败，2设备处于其他状态
     */

    private static void setSteamPreSetParam(RecipeStepDetailView recipeStepDetailView, final AbsSteamoven steam, final Callback<Integer> callback) {
        try {
            // final Steam209 steam = (Steam209) Utils.getDefaultSteam();
            if (!steam.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (steam.status == SteamStatus.AlarmStatus || steam.doorState == 0) {
                callback.onSuccess(1);
                return;
            }
            final Map<String, paramCode> paramMap = recipeStepDetailView.paramMap;
            if (steam.status == SteamStatus.On) {//当前蒸箱为开支状态，直接发送预设指令 SteamTime  SteamTemp
                steam.setSteamProMode(Short.valueOf(String.valueOf(paramMap.get("SteamTime").value / 60)),
                        Short.valueOf(String.valueOf(paramMap.get("SteamTemp").value)), 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                callback.onSuccess(0);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                callback.onSuccess(1);
                            }
                        });
            } else if (steam.status == SteamStatus.Wait || steam.status == SteamStatus.Off) {//若烤箱待机或关闭，先开机后发指令
                //LogUtils.i("recipe_steam", "steam.status == SteamStatus.Wait || steam.status == SteamStatus.Off");
                steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                        Log.i("dfbfdbfngnfgn", "paramMap.get(SteamTime).value" + paramMap.get("SteamTime").value);
                        Log.i("dfbfdbfngnfgn", "paramMap.get(SteamTime).value/60" + paramMap.get("SteamTime").value / 60);
                        //LogUtils.i("recipe_steam", "setSteamStatus_onSuccess");
                        steam.setSteamProMode(Short.valueOf(String.valueOf(paramMap.get("SteamTime").value / 60)),
                                Short.valueOf(String.valueOf(paramMap.get("SteamTemp").value)), 1, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        //LogUtils.i("recipe_steam", "setSteamProMode_onSuccess");
                                        callback.onSuccess(0);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        //LogUtils.i("recipe_steam", "setSteamProMode_Throwable");
                                        callback.onSuccess(1);
                                    }
                                });
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
    private static void setMicrowavePreSetModel(RecipeStepDetailView recipeStepDetailView, final Callback<Integer> callback) {
        try {
            AbsMicroWave microWave = null;
            List<AbsMicroWave> microWaves = Utils.getMicrowave();
            for (int i = 0; i < microWaves.size(); i++) {
                if (recipeStepDetailView.type != null && recipeStepDetailView.type.equals(microWaves.get(i).getDt())) {
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
            if (microWave.state == MicroWaveStatus.Wait) {//当微波炉为待机状态
                microWave.setMicroWaveProModeHeat(Short.valueOf(String.valueOf(paramMap.get("MicroWaveMode").value)),
                        Short.valueOf(String.valueOf(paramMap.get("MicroWaveTime").value)),
                        Short.valueOf(String.valueOf(paramMap.get("MicroWavePower").value)), (short) 1, new VoidCallback() {
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


    /**
     * 设置设备开始指令
     */
    public static void setDeviceStatusModel(Context cx, short status, RecipeStepPage stepPage, Callback<Integer> callback) {
        try {
            RecipeStepDetailView recipeStepDetailView = stepPage.working_recipeStepDetailView;
            if (recipeStepDetailView == null) return;
            LogUtils.i("20171122", "status::" + status);
            LogUtils.i("20171122", "category:" + recipeStepDetailView.category);
            if (DeviceType.RDKX.equals(recipeStepDetailView.category)) {
                //recipeStepDetailView.deviceInfo.dp;
                if (true) {//将来再次判断设备平台对应的指令发送形式
                    if (status == RecipeUtil.WORKING)
                        status = OvenStatus.Working;
                    else if (status == RecipeUtil.PAUSE)
                        status = OvenStatus.Pause;
                    else if (status == RecipeUtil.OFF)
                        status = OvenStatus.Off;
                    setOvenStatusModel(recipeStepDetailView.type, status, callback);
                }
            } else if (DeviceType.RZQL.equals(recipeStepDetailView.category)) {
                if (true) {//将来再次判断设备平台对应的指令发送形式
                    if (status == RecipeUtil.WORKING)
                        status = SteamStatus.Working;
                    else if (status == RecipeUtil.PAUSE)
                        status = SteamStatus.Pause;
                    else if (status == RecipeUtil.OFF)
                        status = SteamStatus.Off;
                    setSteamStatusModel(recipeStepDetailView.type, status, callback);
                }
            } else if (DeviceType.RWBL.equals(recipeStepDetailView.category)) {
                if (true) {//将来再次判断设备平台对应的指令发送形式
                    if (status == RecipeUtil.WORKING)
                        status = MicroWaveStatus.Run;
                    else if (status == RecipeUtil.PAUSE)
                        status = MicroWaveStatus.Pause;
                    else if (status == RecipeUtil.OFF)
                        status = RecipeUtil.MICROOFF;
                    setMicroStatusModel(recipeStepDetailView.type, status, callback);
                }
            } else if (DeviceType.RZKY.equals(recipeStepDetailView.category)) {
                LogUtils.i("20171122", "status::" + status);
                if (status == SteamOvenOnePowerOnStatus.WorkingStatus)
                    status = SteamOvenOnePowerOnStatus.WorkingStatus;
                if (status == SteamOvenOnePowerOnStatus.Pause)
                    status = SteamOvenOnePowerOnStatus.Pause;
                if (status == SteamOvenOnePowerStatus.RecipeOff)
                    status = SteamOvenOnePowerStatus.RecipeOff;

                setSteamAndOvenStatusModel(recipeStepDetailView.type, status, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置烤箱指令开始工作   callback 0 成功，1 指令发送失败，2 下发参数失败,请检查设备,3 设备已离线
     */
    private static void setOvenStatusModel(String type, short status, final Callback<Integer> callback) {
        LogUtils.i("20171110", "status::" + status);
        List<AbsOven> oven = Utils.getOven();
        for (int i = 0; i < oven.size(); i++) {
            if (type != null && type.equals(oven.get(i).getDt())) {
                setOvenStatusParam(status, oven.get(i), callback);
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
                if (oven.status != OvenStatus.On && oven.status != OvenStatus.Pause && oven.status != OvenStatus.PreHeat) {//工作之前判断是否从预设或暂停状态进来
                    callback.onSuccess(2);
                    return;
                }
            } else if (status == OvenStatus.Pause) {//暂停工作之前，判断烤箱是否处于工作工作状态或者预热状态
                if (oven.status != OvenStatus.Working) {
                    callback.onSuccess(2);
                    return;
                }
            }
            LogUtils.i("2020035", "getGuid:" + oven.getGuid().getGuid());
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

    private static void setSteamStatusModel(String type, short status, final Callback<Integer> callback) {
        List<AbsSteamoven> steam = Utils.getSteam();
        for (int i = 0; i < steam.size(); i++) {
            if (type != null && type.equals(steam.get(i).getDt())) {
                setSteamStatusParam(status, steam.get(i), callback);
            }
        }
       /*if (Utils.getDefaultSteam()!=null&&Utils.getDefaultSteam().getGuid().toString().contains("209")){
           Steam209 steam= (Steam209) Utils.getDefaultSteam();
           setSteamStatusParam(status,steam,callback);
       }else if(Utils.getDefaultSteam()!=null&&Utils.getDefaultSteam().getGuid().toString().contains("226")){
           Steam226 steam= (Steam226) Utils.getDefaultSteam();
           setSteamStatusParam(status,steam,callback);
       }*/

    }

    private static void setSteamAndOvenStatusModel(String type, short status, final Callback<Integer> callback) {
        AbsSteameOvenOne steameOvenOne = Utils.getDefaultSteameOven();
        setSteamAndOvenStatusParam(status, steameOvenOne, callback);
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
            if (steamOven.powerStatus != SteamOvenOnePowerStatus.On && steamOven.worknStatus != SteamOvenOneWorkStatus.PreHeat && steamOven.worknStatus != SteamOvenOneWorkStatus.Working) {
                callback.onSuccess(2);
                return;
            }
        } else if (status == SteamOvenOnePowerOnStatus.Pause) {
            if (steamOven.powerOnStatus != SteamOvenOnePowerOnStatus.WorkingStatus) {
                callback.onSuccess(2);
                return;
            }
        }
        LogUtils.i("20171122", "steamOven::" + steamOven.powerStatus + " " + status);
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
            steamOven.setSteameOvenStatus(steamOven.powerStatus, status, new VoidCallback() {
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
     * 设置蒸箱指令开始工作   callback 0 成功，1 指令发送失败，2 下发参数失败,请检查设备,3 设备已离线
     */
    private static void setSteamStatusParam(short status, final AbsSteamoven steam, final Callback<Integer> callback) {
        try {
            // final Steam209 steam = (Steam209) Utils.getDefaultSteam();
            if (!steam.isConnected()) {
                callback.onSuccess(3);
                return;
            }
            if (steam.status == SteamStatus.AlarmStatus || steam.doorState == 0) {//判断有无报警
                callback.onSuccess(4);
                return;
            }
            if (status == SteamStatus.Working) {//开始工作之前，利用蒸箱状态和温度时间去判断当前是否处于预设状态
                if (steam.status != SteamStatus.On && steam.status != SteamStatus.Pause && steam.status != SteamStatus.PreHeat) {//工作之前判断是否从预设或暂停状态进来
                    callback.onSuccess(2);
                    return;
                }
            } else if (status == SteamStatus.Pause) {//暂停工作之前，判断蒸箱是否处于工作工作状态
                if (steam.status != SteamStatus.Working) {
                    callback.onSuccess(2);
                    return;
                }
            }
            steam.setSteamStatus(status, new VoidCallback() {
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

    /**
     * 设置微波炉指令开始工作   callback 0 成功，1 指令发送失败，2 下发参数失败,请检查设备,3 设备已离线
     */
    private static void setMicroStatusModel(String type, short status, final Callback<Integer> callback) {
        try {
            AbsMicroWave microWave = null;
            List<AbsMicroWave> microWaves = Utils.getMicrowave();
            for (int i = 0; i < microWaves.size(); i++) {
                if (type != null && type.equals(microWaves.get(i).getDt())) {
                    microWave = microWaves.get(i);
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
