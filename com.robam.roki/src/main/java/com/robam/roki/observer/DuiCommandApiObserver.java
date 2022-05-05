package com.robam.roki.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;


public class DuiCommandApiObserver implements CommandObserver {
    public static final String ROKI_FAN_POWER = "roki.fan.power";//开关烟机
    public static final String ROKI_FAN_LEVEL = "roki.fan.level";//档位设置
    public static final String ROKI_FAN_LIGHT = "roki.fan.light";//灯光控制
    public static final String ROKI_FAN_LEVEL_CHANGE = "roki.fan.level.change";//档位调节
    public static final String ROKI_RECIPE_SEARCH = "roki.recipe.search";//菜谱搜索

    public static final String ROKI_STEAM_SET = "roki.steam.set";//蒸箱温度设置
    public static final String ROKI_STEAM_POWER = "roki.steam.power";//开关蒸箱


    public static final String ROKI_OVEN_POWER = "roki.oven.power";//开关烤箱
    public static final String ROKI_OVEN_SET = "roki.oven.set";//专业模式
    public static final String ROKI_MW_SET = "roki.mw.set";//微波炉专业模式
    public static final String ROKI_MW_POWER = "roki.mw.power";//微波炉开关


    public static final String ROKI_STERILIZER_POWER = "roki.sterilizer.power";//消毒柜开关机
    public static final String ROKI_STERILIZER_SET = "roki.sterilizer.set";//消毒柜专业模式设置



    public static final String ROKI_STEAM_BAKE_POWER = "roki.steam_bake.power";//蒸烤一体机开关
    public static final String ROKI_STEAM_BAKE_SET = "roki.steam_bake.set";//蒸烤一体机专业模式设置




    private CallCallback mCallCallback;


    public interface CallCallback {
        void onCallResult(String commandApi, String data);
    }

    //注册
    public void register(CallCallback callback) {
        mCallCallback = callback;
        try {
            DDS.getInstance().getAgent().subscribe(new String[]{
                    ROKI_FAN_POWER,
                    ROKI_FAN_LEVEL,
                    ROKI_FAN_LIGHT,
                    ROKI_FAN_LEVEL_CHANGE,
                    ROKI_RECIPE_SEARCH,
                    ROKI_STEAM_SET,
                    ROKI_STEAM_POWER,
                    ROKI_OVEN_POWER,
                    ROKI_OVEN_SET,
                    ROKI_MW_SET,
                    ROKI_MW_POWER,
                    ROKI_STERILIZER_POWER,
                    ROKI_STERILIZER_SET,
                    ROKI_STEAM_BAKE_POWER,
                    ROKI_STEAM_BAKE_SET
            }, this);
        } catch (Exception e) {

        }
    }

    //注销
    public void unregister() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }


    @Override
    public void onCall(String command, String data) {
        if (mCallCallback != null) {
            mCallCallback.onCallResult(command, data);
        }


    }
}
