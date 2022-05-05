package com.robam.roki.model.helper;

import com.google.common.collect.Lists;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.List;

/**
 * Created by 14807 on 2018/2/7.
 */

public class HelperWaterData {


    //取数据
    public static List<String> getHotRecipeData(List<Integer> hotRecipeList, String numberCompany) {
        List<String> list = Lists.newArrayList();

        for (int i = hotRecipeList.get(0); i <= hotRecipeList.get(1); i += hotRecipeList.get(2)) {
            list.add(i + numberCompany);
        }
        return list;
    }

}
