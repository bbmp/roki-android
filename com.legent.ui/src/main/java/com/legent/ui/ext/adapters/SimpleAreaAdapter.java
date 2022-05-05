package com.legent.ui.ext.adapters;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legent.IDispose;
import com.legent.ui.R;
import com.legent.utils.api.ResourcesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/8/13.
 */
public class SimpleAreaAdapter implements IDispose {

    private static SimpleAreaAdapter instance = new SimpleAreaAdapter();

    synchronized public static SimpleAreaAdapter getInstance() {
        return instance;
    }

    /**
     * 数据是否已经初始化
     */
    protected boolean isInited = false;

    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    protected JSONObject mJsonObj;
    /**
     * 所有省
     */
    protected List<String> mProvinceDatas = Lists.newArrayList();
    /**
     * key - 省 value - 市s
     */
    protected Map<String, List<String>> mCitisDatasMap = Maps.newHashMap();
    /**
     * key - 市 values - 区s
     */
    protected Map<String, List<String>> mAreaDatasMap = Maps.newHashMap();

    private SimpleAreaAdapter() {
        initDatas();
    }

    @Override
    public void dispose() {

        isInited = false;
        mJsonObj = null;
        mProvinceDatas.clear();
        mCitisDatasMap.clear();
        mAreaDatasMap.clear();
    }

    /**
     * 获取省份列表
     * @return 省份列表
     */
    public List<String> getProvinceDatas() {
        initDatas();
        return mProvinceDatas;
    }

    /**
     * 根据省份获取市列表
     * @param province 省份
     * @return 市列表
     */
    public List<String> getCities(String province) {
        initDatas();

        List<String> res = mCitisDatasMap.get(province);
        if (res == null || res.size() == 0) {
            res = Lists.newArrayList();
            res.add("");
        }

        return res;
    }


    /**
     * 根据市获取县区列表
     * @param city 市
     * @return 县区列表
     */
    public List<String> getConties(String city) {
        initDatas();

        List<String> res = mAreaDatasMap.get(city);
        if (res == null || res.size() == 0) {
            res = Lists.newArrayList();
            res.add("");
        }

        return res;
    }


    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {
            if (isInited) return;
            String json = ResourcesUtils.raw2String(R.raw.city);

            mJsonObj = new JSONObject(json);

            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvinceDatas.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                // 每个省的json对象
                JSONObject jsonP = jsonArray.getJSONObject(i);

                // 省名字
                String province = jsonP.getString("p");
                mProvinceDatas.add(province);

                //城市列表
                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }

                List<String> mCitiesDatas = Lists.newArrayList();
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);

                    // 市名字
                    String city = jsonCity.getString("n");
                    mCitiesDatas.add(city);
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }

                    // 当前市的所有区
                    List<String> mAreasDatas = Lists.newArrayList();
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        // 区域的名称
                        String area = jsonAreas.getJSONObject(k).getString("s");
                        mAreasDatas.add(area);
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }
                mCitisDatasMap.put(province, mCitiesDatas);
            }
            isInited = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //加载数据
    //--------------------------------------------------------------------------------------------------------------------
}
