package com.robam.roki.ui.activity3.device.dishwasher.bean;

/**
 * @author r210190
 *  一体机新协议模式对应的枚举
 */

public enum DishModeEnum {
    /**
     *风焙烤、风扇烤、强烤烧、空气炸
     */
    NO_MOEL(0,"无模式"),
    QIANGLIXI(1,"强力洗"),
    RICHANGXI(2,"日常洗"),
    JIENENGXI(3,"节能洗"),
    KUAISUXI(4,"快速洗"),
    ZHINENGXI(5,"智能洗"),
    JINGLIANGXI(6,"晶亮洗"),
    AUTOVEN(9,"自动换气"),
    AUTOVEN2(10,"自动换气等待中"),
 ;



    private int code;
    private String value;

    private DishModeEnum(int code, String message) {
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

    public static DishModeEnum match(int key) {

        DishModeEnum result = null;

        for (DishModeEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static DishModeEnum catchMessage(String msg) {

        DishModeEnum result = null;

        for (DishModeEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
