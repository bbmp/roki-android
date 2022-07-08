package com.robam.roki.ui.activity3.device.base.other;

import android.util.Log;

import com.google.gson.Gson;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.net.Connector;
import com.robam.roki.ui.activity3.device.base.ParamBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/08
 *     desc   : 对设备功能进行排序  智能套系4.0改版需要排序
 *     version: 1.0
 * </pre>
 */
public class SortUtils {

    public static List<DeviceConfigurationFunctions> funSort(List<DeviceConfigurationFunctions> datas , int module){
        ArrayList<DeviceConfigurationFunctions> deviceConfigurationFunctions = new ArrayList<>();
        for (DeviceConfigurationFunctions data : datas) {
            if (data.functionParams != null) {
                Log.e("FanFunctionActivity" , data.functionParams ) ;
                ParamBean paramBean = new Gson().fromJson(data.functionParams, ParamBean.class);
                //顺序
                data.order = paramBean.order.order ;
                //所需要获取的模块
                if (paramBean.order.module == module){
                    deviceConfigurationFunctions.add(data);
                }
            }
        }

        return sort(deviceConfigurationFunctions);
    }


    private static List<DeviceConfigurationFunctions> sort(List<DeviceConfigurationFunctions> datas){
        Collections.sort(datas, new Comparator<DeviceConfigurationFunctions>() {
            @Override
            public int compare(DeviceConfigurationFunctions fun1, DeviceConfigurationFunctions fun2) {
                if (fun1.order > fun2.order) {
                    return 1 ;
                }else if (fun1.order < fun2.order){
                    return  -1 ;
                }
                return 0;
            }
        });
        return datas ;
    }
}
