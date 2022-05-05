package com.robam.roki.model;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ResourcesUtils;
import com.robam.roki.R;

import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/8/5.
 */
public class CrmHelper {

    //--------------------------------------------------------------------------------------------------------------------
    //加载数据
    //--------------------------------------------------------------------------------------------------------------------


    static private Map<Integer, PCR> map;
    /**
     * 所有省
     */
    static private List<PCR> mProvinceDatas = Lists.newArrayList();

    static private Ordering<PCR> ordering = Ordering.natural().nullsFirst()
            .onResultOf(new Function<PCR, Integer>() {
                public Integer apply(PCR foo) {
                    return foo.id;
                }
            });

    static {
        loadDatas();
    }


    static private void loadDatas() {

        if (map != null) return;

        //构造 map
        map = Maps.newHashMap();
        String json = ResourcesUtils.raw2String(R.raw.sys_pcr);

        List<PCR> pcrs = JsonUtils.json2List(json, PCR.class);
        pcrs = ordering.sortedCopy(pcrs);
        for (PCR pcr : pcrs) {
            map.put(pcr.id, pcr);
        }

        //
        PCR parent;
        for (PCR pcr : pcrs) {
            if (pcr.parentId == -1) {
                mProvinceDatas.add(pcr);
            } else {
                if (map.containsKey(pcr.parentId)) {
                    parent = map.get(pcr.parentId);
                    parent.children.add(pcr);
                }
            }
        }

    }

    static public List<PCR> getProvinces() {
        return mProvinceDatas;
    }

    static public PCR getPcr(int id) {
        return map.get(id);
    }

    static public CrmArea getCrmArea(String provinceId, String cityId, String countyId) {
        CrmArea ca = new CrmArea();
        try {
            ca.province = getPcr(Integer.parseInt(provinceId));
        } catch (Exception e) {
        }
        try {
            ca.city = getPcr(Integer.parseInt(cityId));
        } catch (Exception e) {
        }
        try {
            ca.county = getPcr(Integer.parseInt(countyId));
        } catch (Exception e) {
        }

        return ca;
    }

    static public CrmArea getCrmArea(int provinceId, int cityId, int countyId) {

        CrmArea ca = new CrmArea();
        ca.province = getPcr(provinceId);
        ca.city = getPcr(cityId);
        ca.county = getPcr(countyId);
        return ca;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //加载数据
    //--------------------------------------------------------------------------------------------------------------------

}
