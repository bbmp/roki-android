package com.robam.roki.ui.activity3.device.base.adapter;

import android.graphics.drawable.Drawable;
import skin.support.content.res.SkinCompatResources;
import com.robam.common.RobamApp;
import com.robam.roki.R;

public class ImageSelectUtil {
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
            case "cookbookFnc2":
                return R.mipmap.ytj_mode_2_selecte_3x;
            case "cookbookFnc3":
                return R.mipmap.ytj_mode_3_selecte_3x;
            case "cookbookFnc5":
                return R.mipmap.ytj_mode_5_selecte_3x;
            case "cookbookFnc6":
                return R.mipmap.ytj_mode_6_selecte_3x;
            case "cookbookFnc7":
                return R.mipmap.ytj_mode_7_selecte_3x;

            case "cookbookFnc9":
                return R.mipmap.ytj_mode_9_selecte_3x;
            case "cookbookFnc10":
                return R.mipmap.ytj_mode_10_selecte_3x;

            case "cookbookFnc12":
                return R.mipmap.ytj_mode_12_selecte_3x;
                case "cookbookFnc13":
                return R.mipmap.ytj_mode_13_selecte_3x;
            case "cookbookFnc14":
                return R.mipmap.ytj_mode_14_selecte_3x;

            case "cookbookFnc22":
                return R.mipmap.ytj_mode_22_selecte_3x;
            case "cookbookFnc23":
                return R.mipmap.ytj_mode_23_selecte_3x;
            case "cookbookFnc24":
                return R.mipmap.ytj_mode_24_selecte_3x;

            case "cookbookFnc32":
                return R.mipmap.ytj_mode_32_selecte_3x;
            case "cookbookFnc33":
                return R.mipmap.ytj_mode_33_selecte_3x;
            case "cookbookFnc34":
                return R.mipmap.ytj_mode_34_selecte_3x;
            case "cookbookFnc35":
                return R.mipmap.ytj_mode_35_selecte_3x;
            case "steamingMode":
                return R.mipmap.ytj_steamingmode_3x;
            case "roastModel":
                return R.mipmap.ytj_roastmodel_3x;
            case "airfryFunction":
                return R.mipmap.ytj_airfryfunction_3x;
            case "addSteamRoastModel":
                return R.mipmap.ytj_addsteamroastmodel_3x;
            case "multiStepModel":
                return R.mipmap.ytj_multistepmodel_3x;
            case "localCookbook":
                return R.mipmap.ytj_localcookbook_3x;
//            case "zkyAutoRecipes":
//                return R.mipmap.ytj_localcookbook_3x;
            case "fzMode":
                return R.mipmap.ytj_fzmode_3x;
            default:
                return R.mipmap.ytj_mode_5_selecte_3x;




        }

    }


    public static final int getImageUnSelect(String functionCode){
        switch (functionCode){
            case "cookbookFnc2":
                return R.mipmap.ytj_mode_2_3x;
            case "cookbookFnc3":
                return R.mipmap.ytj_mode_3_3x;

            case "cookbookFnc5":
                return R.mipmap.ytj_mode_5_3x;
            case "cookbookFnc6":
                return R.mipmap.ytj_mode_6_3x;
            case "cookbookFnc7":
                return R.mipmap.ytj_mode_7_3x;
            case "cookbookFnc8":
                return R.mipmap.ytj_mode_8_3x;
            case "cookbookFnc9":
                return R.mipmap.ytj_mode_9_3x;
            case "cookbookFnc10":
                return R.mipmap.ytj_mode_10_3x;

            case "cookbookFnc12":
                return R.mipmap.ytj_mode_12_3x;
            case "cookbookFnc13":
                return R.mipmap.ytj_mode_13_3x;

            case "cookbookFnc14":
                return R.mipmap.ytj_mode_14_3x;

            case "cookbookFnc22":
                return R.mipmap.ytj_mode_22_3x;
            case "cookbookFnc23":
                return R.mipmap.ytj_mode_23_3x;
            case "cookbookFnc24":
                return R.mipmap.ytj_mode_24_3x;
            case "cookbookFnc32":
                return R.mipmap.ytj_mode_32_3x;
            case "cookbookFnc33":
                return R.mipmap.ytj_mode_33_3x;
            case "cookbookFnc34":
                return R.mipmap.ytj_mode_34_3x;
            case "cookbookFnc35":
                return R.mipmap.ytj_mode_35_3x;

            default:
                return R.mipmap.ytj_mode_5_3x;



        }

    }
}
