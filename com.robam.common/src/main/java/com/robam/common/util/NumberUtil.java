package com.robam.common.util;


public class NumberUtil {
    public static String converString(int num) {
        String unit = "";
        double newNum = 0.0;
        if (num < 1000) {
            return String.valueOf(num);
        }

        if (num >= 1000 && num < 10000) {
            unit = "k";
            newNum = num / 1000.0;
        }

        if (num >= 10000) {
            unit = "w";
            newNum = num / 10000.0;
        }

        String numStr = String.format("%." + 2 + "f", newNum);
        return numStr + unit;
    }

}
