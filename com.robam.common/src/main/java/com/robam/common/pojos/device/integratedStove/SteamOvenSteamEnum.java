package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  一体机新协议模式对应的枚举
 */

public enum SteamOvenSteamEnum {
    /**
     *风焙烤、风扇烤、强烤烧、空气炸
     */
    NO_STEAM(0,"无蒸汽"),
    SMALL(1,"小"),
    MIDDLE(2,"中"),
    MAX(3,"大 "),
    ;



    private int code;
    private String value;

    private SteamOvenSteamEnum(int code, String message) {
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

    public static SteamOvenSteamEnum match(int key) {

        SteamOvenSteamEnum result = null;

        for (SteamOvenSteamEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static SteamOvenSteamEnum catchMessage(String msg) {

        SteamOvenSteamEnum result = null;

        for (SteamOvenSteamEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
