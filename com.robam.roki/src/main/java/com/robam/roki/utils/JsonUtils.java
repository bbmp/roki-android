package com.robam.roki.utils;

import com.legent.pojos.AbsPojo;

import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;


/**
 * Created by 14807 on 2018/5/22.
 */

public class JsonUtils extends AbsPojo {


    public static String getValue(String params, short leftLevel) {
        String value = null;
        try {
            JSONObject jsonObject = new JSONObject(params);
            JSONObject param = (JSONObject) jsonObject.get("param");
            JSONObject paramValue = (JSONObject) param.get(String.valueOf(leftLevel));
            value = (String) paramValue.get("value");
            paramValue.get("tips");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getTips(String params, short leftLevel) {
        String tips = null;
        try {
            JSONObject jsonObject = new JSONObject(params);
            JSONObject param = (JSONObject) jsonObject.get("param");
            JSONObject paramValue = (JSONObject) param.get(String.valueOf(leftLevel));
            tips = (String) paramValue.get("tips");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tips;
    }

    public static Map getJson2Map(String params) {
        Map maps = (Map) JSON.parse(params);
        return maps;
    }


}
