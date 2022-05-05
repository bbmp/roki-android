package com.robam.roki.utils;

import com.robam.common.pojos.Dc;

import java.util.List;

public class ListUtils {
    public static List<Dc> getListBefore(List<Dc>js_dcs){
        if (js_dcs.size()<=2) {
            return js_dcs;
        }else{
            return js_dcs.subList(0,2);
        }
    }
}
