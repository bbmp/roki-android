package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  除垢阶段对应的文案
 */

public enum ChuGouEnum {
    /**
     *
     */
    NO_CHUGOU(0,"无"),
    CHUGOU1(1,"正在除垢清理，请耐心等待。"),
    CHUGOU2(2,"请倒掉水箱中的余水，重新加满清水，推入水箱，点击产品端的开始键。&正在除垢清理，请耐心等待。"),
    CHUGOU3(3,"请倒掉水箱中的余水，重新加满清水，推入水箱，点击产品端的开始键。&正在除垢清理，请耐心等待。"),
    WEIBOZHENG(4,"除垢结束 "),

    ;

    private int code;
    private String value;

    private ChuGouEnum(int code, String message) {
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

    public static ChuGouEnum match(int key) {

        ChuGouEnum result = null;

        for (ChuGouEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static ChuGouEnum catchMessage(String msg) {

        ChuGouEnum result = null;

        for (ChuGouEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
