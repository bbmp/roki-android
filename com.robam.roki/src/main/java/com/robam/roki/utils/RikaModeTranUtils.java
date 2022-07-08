package com.robam.roki.utils;


import com.robam.roki.R;

import java.util.ArrayList;
import java.util.List;

import static com.legent.ContextIniter.cx;


/**
 * Created by 14807 on 2018/4/13.
 */

public class RikaModeTranUtils {


    public static List<String> getSteamModelTran(short model) {

        List<String> list = new ArrayList<>();
        if (model == 0) {
            list.add(cx.getString(R.string.no_model));
        } else if (2 == model) {
            list.add(cx.getString(R.string.model_rou_cooking));
        } else if (3 == model) {
            list.add(cx.getString(R.string.model_yu_cooking));
        } else if (4 == model) {
            list.add(cx.getString(R.string.model_dan_cooking));
        } else if (5 == model) {
            list.add(cx.getString(R.string.model_gaodian_cooking));
        } else if (6 == model) {
            list.add(cx.getString(R.string.model_tijin_cooking));
        } else if (7 == model) {
            list.add(cx.getString(R.string.model_shucai_cooking));
        } else if (8 == model) {
            list.add(cx.getString(R.string.model_mianshi_cooking));
        } else if (9 == model) {
            list.add(cx.getString(R.string.model_jiedong_cooking));
        } else if (10 == model) {
            list.add(cx.getString(R.string.model_wuhuarou_cooking));
        } else if (11 == model) {
            list.add(cx.getString(R.string.model_mantou_cooking));
        }else if (12 == model){
            list.add(cx.getString(R.string.model_mifan_cooking));
        }else if (13 == model){
            list.add(cx.getString(R.string.model_qiangli));
        }else if (14 == model){
            list.add(cx.getString(R.string.model_shajun));
        }else if (15 == model){
            list.add(cx.getString(R.string.model_kuaimain));
        }else if (16 == model){
            list.add(cx.getString(R.string.model_yingyang));
        }else if (17 == model){
            list.add(cx.getString(R.string.model_xiannen));
        }else if (18 == model){
            list.add(cx.getString(R.string.model_fajiao));
        }else if (19 == model){
            list.add(cx.getString(R.string.model_baowen));
        }else if (20 == model){
            list.add(cx.getString(R.string.model_chugou));
        }

        return list;
    }
}
