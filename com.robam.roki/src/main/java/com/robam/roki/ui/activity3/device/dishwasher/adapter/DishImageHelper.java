package com.robam.roki.ui.activity3.device.dishwasher.adapter;

import android.graphics.drawable.Drawable;

import com.robam.common.RobamApp;
import com.robam.roki.R;

import skin.support.content.res.SkinCompatResources;

public class DishImageHelper {
    public static final Drawable getResIdSelect(String functionCode){
        return SkinCompatResources.getDrawable(RobamApp.getInstance(), getImageSelect(functionCode));
    }

    public static final Drawable getResIdUnSelect(String functionCode){
        return SkinCompatResources.getDrawable(RobamApp.getInstance(), getImageUnSelect(functionCode));
    }
//    private int getColorSkin(int resId){
//        return SkinCompatResources.getColor(getContext() ,resId) ;
//    }

    public static final int getImageSelect(String functionCode){
        switch (functionCode){
            case "strongWash":
                return R.mipmap.icon_dish_qlx_on;
            case "intelligentWash":
                return R.mipmap.icon_dish_znx_on;
            case "quickWash":
                return R.mipmap.icon_dish_ksx_on;
            case "dayWash":
                return R.mipmap.icon_dish_rcx_on;
            case "jinliangxiWash":
                return R.mipmap.icon_dish_jlx_on;
            case "func_zyms":
                return R.mipmap.xwj_func_zyms_3x;
            case "other_func_jstj":
                return R.mipmap.icon_dish_jstj;
            case "autoVentilation":
                return R.mipmap.icon_dish_zdhq;
            case "crystalBrightWash":
                return R.mipmap.icon_dish_jlx_on;
            case "dailyWash":
                return R.mipmap.icon_dish_rcx_on;
            case "energySavingWashing":
                return R.mipmap.icon_dish_jnx_on;
            case "zyms_znx":
                return R.mipmap.icon_dish_znx_on;
            case "zyms_ksx":
                return R.mipmap.icon_dish_ksx_on;
            default:
                return R.mipmap.icon_dish_zdhq;
        }

    }


    public static final int getImageUnSelect(String functionCode){
        switch (functionCode){
            case "strongWash":
                return R.mipmap.icon_dish_qlx;
            case "intelligentWash":
                return R.mipmap.icon_dish_znx;
            case "quickWash":
                return R.mipmap.icon_dish_ksx;
            case "dayWash":
                return R.mipmap.icon_dish_rcx;
            case "jinliangxiWash":
                return R.mipmap.icon_dish_jlx;
            case "func_zyms":
                return R.mipmap.xwj_func_zyms_3x;
            case "other_func_jstj":
                return R.mipmap.icon_dish_jstj;
            case "autoVentilation":
                return R.mipmap.icon_dish_zdhq;
            case "crystalBrightWash":
                return R.mipmap.icon_dish_jlx;
            case "dailyWash":
                return R.mipmap.icon_dish_rcx;
            case "energySavingWashing":
                return R.mipmap.icon_dish_jnx;
            case "zyms_znx":
                return R.mipmap.icon_dish_znx;
            case "zyms_ksx":
                return R.mipmap.icon_dish_ksx;
            default:
                return R.mipmap.icon_dish_zdhq;



        }

    }
}
