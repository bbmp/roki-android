package com.robam.roki.utils;

/**
 * Created by 14807 on 2018/10/16.
 * 微波炉档位转译
 */

public class MicroWaveFireUtils {

    public static String fireTransition(short fire) {

        if (fire == 7) {
            return 6 + "";
        }

        if (fire == 8) {
            return 4 + "";
        }
        if (fire == 9) {
            return 2 + "";
        }

        if (fire == 10) {
            return 6 + "";
        }

        if (fire == 11) {
            return 4 + "";
        }

        if (fire == 12) {
            return 2 + "";
        }

        return fire + "";
    }

}
