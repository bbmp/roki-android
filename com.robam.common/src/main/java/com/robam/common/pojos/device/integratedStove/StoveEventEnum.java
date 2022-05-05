package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  集成烟机新协事件对应枚举
 */

public enum StoveEventEnum {
    /**
     *
     */
    EVENT_1(1,"灶功率调整"),
    EVENT_2(2,"无锅提醒"),
    EVENT_3(3,"温度变化事件"),
    ;

    private int code;
    private String value;

    private StoveEventEnum(int code, String message) {
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

    public static StoveEventEnum match(int key) {

        StoveEventEnum result = null;

        for (StoveEventEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static StoveEventEnum catchMessage(String msg) {

        StoveEventEnum result = null;

        for (StoveEventEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
