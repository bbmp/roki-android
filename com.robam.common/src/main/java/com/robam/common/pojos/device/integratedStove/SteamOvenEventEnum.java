package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  一体机新协事件对应枚举
 */

public enum SteamOvenEventEnum {
    /**
     *
     */
    START_WORK(1,"启动工作"),
    PAUSE_WORK(2,"暂停工作"),
    GO_ON_WORK(3,"继续工作 "),
    COMPLETE_WORK(4,"完成工作 "),
    STOP_WORK(5,"终止工作"),
    ORDER_WORK(6,"预约工作 "),
    MUIT(7,"段提醒"),
    //WATER(8,"缺水提醒"),
    CHUGOU(9,"除垢提醒"),
    ADD_STEAM(10,"加蒸汽提醒"),
    DOOR(11,"门控提醒"),
    TEMP(12,"温度变化事件"),
    ;

    private int code;
    private String value;

    private SteamOvenEventEnum(int code, String message) {
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

    public static SteamOvenEventEnum match(int key) {

        SteamOvenEventEnum result = null;

        for (SteamOvenEventEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static SteamOvenEventEnum catchMessage(String msg) {

        SteamOvenEventEnum result = null;

        for (SteamOvenEventEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
