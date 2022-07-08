package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  一体机新协议模式对应的枚举
 */

public enum SteamOvenFaultEnum {
    /**
     *
     */
    NO_FAULT(0,"无故障"),
    XIANZHENGHOUKAO(1,"先蒸后烤"),
    U_TEMP_FAULT(2,"温度上传感器故障"),
    D_TEMP_FAULT(3,"温度下传感器故障 "),
    RADITATING_FAULT(4,"散热风机故障 "),
    NEWS_FAULT(5,"通讯故障"),
    WATER_FAULT(6,"水位传感器故障 "),
    BUTTON_FAULT(7,"按键板传感器故障"),
    HIGH_FAULT(8,"高温报警故障"),
    TEMP_HEAT_FAULT(9,"温度加热异常故障 "),
    COOL_FAULT(10,"冷气阀故障"),
//    LACKWATER(11,"缺水故障"),
    Updraft_fan_FAULT(12,"上风机故障"),
    Microwave_Frequency_Conversion(13,"微波变频故障"),
    ;

    private int code;
    private String value;

    private SteamOvenFaultEnum(int code, String message) {
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

    public static SteamOvenFaultEnum match(int key) {

        SteamOvenFaultEnum result = null;

        for (SteamOvenFaultEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static SteamOvenFaultEnum catchMessage(String msg) {

        SteamOvenFaultEnum result = null;

        for (SteamOvenFaultEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
