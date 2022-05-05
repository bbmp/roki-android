package com.robam.common.pojos.device.steameovenone;

/**
 * @author r210190
 * 蒸烤一体机枚举
 */

public enum SteamOvenOneEnum {
    /**
     * 风焙烤
     */
    FENGPEIKAO(3, "风培烤"),
    PEIKAO(4, "培烤"),
    FENGSHANKAO(5, "风扇烤"),
    SHAOKAO(6, "烧烤");
    private int type;
    private String name;

    SteamOvenOneEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static SteamOvenOneEnum getEnum(int type) {
        switch (type) {
            case 3:
                return FENGPEIKAO;
            case 4:
                return PEIKAO;
            case 5:
                return FENGSHANKAO;
            case 6:
                return SHAOKAO;
            default:
                return null;

        }
    }
}
