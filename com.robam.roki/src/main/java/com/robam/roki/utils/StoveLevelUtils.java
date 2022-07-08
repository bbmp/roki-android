package com.robam.roki.utils;

import static com.robam.roki.utils.DeviceJsonToBeanUtils.getJsonStringByEntity;

import com.robam.roki.model.bean.StoveBackgroundFunParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 14807 on 2018/7/2.
 */

public class StoveLevelUtils {

    public static String getStoveLevel(short level, StoveBackgroundFunParams paramBean) {
        StoveBackgroundFunParams.ParamBean param = paramBean.getParam();

        switch (level) {
            case 0:
                return param.get_$0().getValue();
            case 1:

                return param.get_$1().getValue();
            case 2:

                return param.get_$2().getValue();
            case 3:

                return param.get_$3().getValue();
            case 4:

                return param.get_$4().getValue();
            case 5:

                return param.get_$5().getValue();
            case 6:

                return param.get_$6().getValue();
            case 7:

                return param.get_$7().getValue();
            case 8:

                return param.get_$8().getValue();

            case 9:

                return param.get_$9().getValue();

            case 10:
                if (param.get_$10() == null) {
                    return "P10";
                }
                return param.get_$10().getValue();

        }

        return "";
    }

    public static String getStovePotTemp(short temp, StoveBackgroundFunParams stoveBackgroundFunParams) {
        StoveBackgroundFunParams.TempDTO tempB = stoveBackgroundFunParams.getTempDTO();
        if (temp >= tempB.getpotTempDTO().getValue().get(0) && temp < tempB.getpotTempDTO().getValue().get(1)) {
           return getKey(getJsonStringByEntity(tempB),tempB.getpotTempDTO().getColor());
        } else if (temp >= tempB.getPotTempLDTO().getValue().get(0) && temp < tempB.getPotTempLDTO().getValue().get(1)) {
            return getKey(getJsonStringByEntity(tempB), tempB.getPotTempLDTO().getColor());
        }else if (temp >= tempB.getPotTempMDTO().getValue().get(0) && temp < tempB.getPotTempMDTO().getValue().get(1)) {
            return getKey(getJsonStringByEntity(tempB), tempB.getPotTempMDTO().getColor());
        }else if (temp >= tempB.getPotTempHDTO().getValue().get(0) && temp < tempB.getPotTempHDTO().getValue().get(1)) {
            return getKey(getJsonStringByEntity(tempB), tempB.getPotTempHDTO().getColor());
        }
        return "";
    }
    public static String getStovePotTextColor(short temp, StoveBackgroundFunParams stoveBackgroundFunParams) {
        StoveBackgroundFunParams.TempDTO tempB = stoveBackgroundFunParams.getTempDTO();
        if (temp >= tempB.getpotTempDTO().getValue().get(0) && temp < tempB.getpotTempDTO().getValue().get(1)) {
            return tempB.getpotTempDTO().getColor();
        } else if (temp >= tempB.getPotTempLDTO().getValue().get(0) && temp < tempB.getPotTempLDTO().getValue().get(1)) {
            return tempB.getPotTempLDTO().getColor();
        }else if (temp >= tempB.getPotTempMDTO().getValue().get(0) && temp < tempB.getPotTempMDTO().getValue().get(1)) {
            return tempB.getPotTempMDTO().getColor();
        }else if (temp >= tempB.getPotTempHDTO().getValue().get(0) && temp < tempB.getPotTempHDTO().getValue().get(1)) {
            return tempB.getPotTempHDTO().getColor();
        }
        return "";
    }

    public static String getKey(String strJson,String strColor){
        try {
            JSONObject jsonObject = new JSONObject(strJson);
            Iterator<String> objs =  jsonObject.keys();
            String key;
            while (objs.hasNext()){
                key = objs.next();
                System.err.println("json_key: "+key);
                JSONObject jo = jsonObject.getJSONObject(key);
                Iterator<String> ks =  jo.keys();
                String k;
                while (ks.hasNext()){
                    k = ks.next();
                    System.err.println("json_k: "+k);
                    String v = jo.getString(k);
                    System.err.println("json_v: "+v);
                    if(strColor.equals(v)){
                        return key;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
