package com.robam.roki.observer;

import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.NativeApiObserver;

/*
 * 注册NativeApiObserver, 用于客户端响应DUI平台技能配置里的资源调用指令,
 * 同一个NativeApiObserver可以处理多个native api.
 */
public class DuiNativeApiObserver implements NativeApiObserver {

    public static final String ROKI_FAN_POWER = "roki.fan.power";
    public static final String ROKI_FAN_LEVEL = "roki.fan.level";
    public static final String ROKI_FAN_LIGHT = "roki.fan.light";
    public static final String ROKI_FAN_STATE = "roki.fan.state";
    public static final String ROKI_RECIPE_SEARCH_COUNT = "roki.recipe.search.count";
    //蒸箱
    public static final String ROKI_STEAM_STATE = "roki.steam.state";

    //烤箱
    public static final String ROKI_OVEN_STATE = "roki.oven.state";

    //微波炉
    public static final String ROKI_MW_STATE = "roki.mw.state";

    //消毒柜
    public static final String ROKI_STERILIZER_STATE = "roki.sterilizer.state";

    //蒸烤一体机
    public static final String ROKI_STEAM_BAKE_STATE = "roki.steam_bake.state";



    private QueryCallback mQueryCallback;

    @Override
    public void onQuery(String nativeApi, String data) {
        Log.d("20190710", "nativeApi: " + nativeApi + "  data: " + data);


        if (mQueryCallback != null) {
            mQueryCallback.onQueryResult(nativeApi, data);
        }


    }

    public interface QueryCallback {

        void onQueryResult(String nativeApi, String data);
    }


    // 注册当前更新消息
    public void regist(QueryCallback queryCallback) {
        mQueryCallback = queryCallback;
        try {
            DDS.getInstance().getAgent().subscribe(new String[]{
                            ROKI_FAN_POWER,
                            ROKI_FAN_LEVEL,
                            ROKI_FAN_LIGHT,
                            ROKI_FAN_STATE,
                            ROKI_RECIPE_SEARCH_COUNT,
                            ROKI_STEAM_STATE,
                            ROKI_OVEN_STATE,
                            ROKI_MW_STATE,
                            ROKI_STERILIZER_STATE,
                            ROKI_STEAM_BAKE_STATE
                    },
                    this);
            Log.d("20190710", "regist: ");
        } catch (Exception e) {
            Log.d("20190710", "e: " + e.toString());
        }


    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }


}
