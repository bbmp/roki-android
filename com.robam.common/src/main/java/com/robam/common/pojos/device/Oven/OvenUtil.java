package com.robam.common.pojos.device.Oven;

/**
 * Created by Rent on 2016/7/15.
 */
public class OvenUtil {
    public static String getOvenModel(int index) {
        String str = "";
        switch (index) {
            case 130:
                str = "快热";
                break;
            case 132:
                str = "风焙烤";
                break;
            case 134:
                str = "焙烤";
                break;
            case 136:
                str = "底加热";
                break;
            case 138:
                str = "解冻";
                break;
            case 140:
                str = "风扇烤";
                break;
            case 142:
                str = "烤烧";
                break;
            case 144:
                str = "强烤烧";
                break;
            default:break;
        }
        return str;
    }
}
