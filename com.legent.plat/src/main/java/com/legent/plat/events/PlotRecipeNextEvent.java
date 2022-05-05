package com.legent.plat.events;

import regulation.dto.StatusResult;

/**
 * Created by as on 2017-02-22.
 */

public class PlotRecipeNextEvent {
    public static final String RULE_CODE_OVERTIME = "RULE_OVERTIME"; //超时规则
    public static final String RULE_CODE_UP_THRESHOLD = "RULE_UP_THRESHOLD"; //up阈值规则
    public static final String RULE_CODE_DOWN_THRESHOLD = "RULE_DOWN_THRESHOLD"; //down阈值规则
    public static final String RULE_CODE_TEMPERATURE_TREND = "RULE_TEMPERATURE_TREND"; //温度趋势规则
    public static final String RULE_CODE_ACCELERATE_TREND = "RULE_ACCELERATE_TREND"; //温度变化趋势规则
    public static final String RULE_CODE_SECTION_TEMP = "RULE_SECTION_TEMP"; //区间趋势规则
    public static final String RULE_CODE_RULE_MANUAL = "RULE_MANUAL"; //手动处理规则
    private String type;
    private StatusResult statusResult;

    public int getStepIndex() {
        return stepIndex;
    }

    private int stepIndex;

    public PlotRecipeNextEvent(String type) {
        this.type = type;
    }

    public PlotRecipeNextEvent(String type, Object... objects) {
        this.type = type;
        this.statusResult = (StatusResult) objects[0];
        this.stepIndex = (Integer) objects[1];
    }

    public StatusResult getStatusResult() {
        return statusResult;
    }

    public String getType() {
        return type;
    }
}
