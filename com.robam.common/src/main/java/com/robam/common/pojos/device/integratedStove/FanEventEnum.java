package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  集成烟机新协事件对应枚举
 */

public enum FanEventEnum {
    /**
     *
     */
    EVENT_1(1,"档位变化"),
    EVENT_2(2,"定时提醒"),
    EVENT_3(3,"清洗提醒"),
    EVENT_4(4,"清洗锁定"),
    EVENT_5(5,"挡风板拆卸"),
    EVENT_6(6,"定时工作完成"),
    EVENT_7(7,"干烧提醒"),
    EVENT_8(8,"倒油杯提醒"),
    EVENT_9(9,"防干烧提醒事"),
    EVENT_10(10,"空气质量检测"),
    ;

    private int code;
    private String value;

    private FanEventEnum(int code, String message) {
        this.code = code;
        this.value = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String message) {
        this.value = message;
    }

    public static FanEventEnum match(int key) {

        FanEventEnum result = null;

        for (FanEventEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static FanEventEnum catchMessage(String msg) {

        FanEventEnum result = null;

        for (FanEventEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
