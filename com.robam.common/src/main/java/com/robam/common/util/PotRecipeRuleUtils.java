package com.robam.common.util;

import com.legent.utils.LogUtils;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/28.
 * PS: Not easy to write code, please indicate.
 */
public class PotRecipeRuleUtils {


    public static String getRecipeRuleByCode(String code) {

        String rule = null;
        LogUtils.i("20190428", "code:" + code);
        switch (code) {

            case "RULE_OVERTIME"://超时规则
                rule = "auto";
                return rule;

            case "RULE_UP_THRESHOLD"://up阈值规则
                rule = "auto";
                return rule;
            case "RULE_DOWN_THRESHOLD": //down阈值规则
                rule = "auto";
                return rule;
            case "RULE_TEMPERATURE_TREND"://温度趋势规则
                rule = "auto";
                return rule;
            case "RULE_ACCELERATE_TREND"://温度变化趋势规则
                rule = rule;
                return "auto";
            case "RULE_SECTION_TEMP"://区间趋势规则
                rule = "auto";
                return rule;
            case "RULE_CODE_RULE_MANUAL"://手动处理规则

                return "";
        }

        return rule;
    }

}
