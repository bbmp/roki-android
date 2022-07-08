package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  一体机新协议模式对应的枚举
 */

public enum SteamOvenModeEnum {
    /**
     *风焙烤、风扇烤、强烤烧、空气炸
     */
    NO_MOEL(0,"无模式"),
    XIANNENZHENG(1,"鲜嫩蒸"),
    YIYANGZHENG(2,"营养蒸"),
    GAOWENZHENG(3,"高温蒸 "),
    WEIBOZHENG(4,"微蒸 "),
    ZHIKONGZHENG(5,"澎湃蒸"),
    KUAIRE(6,"快热"),
    FENGBEIKAO(7,"风焙烤"),
    BEIKAO(8,"焙烤"),
    FENGSHANKAO(9,"风扇烤"),
    QIANGSHAOKAO(10,"强烤烧"),
    SHAOKAO(11,"烤烧"),
    KUAISUYURE(12,"快速预热"),
    GUOSHUHONGGAN(13,"果蔬烘干"),
    EXP(14,"专业烤"),
    WEIBOKAO(15,"微烤"),
    JIANKAO(16,"煎烤"),
    DIJIARE(17,"底加热"),
    KONGQIZHA(18,"空气炸"),
    WEIBO(19,"单微"),
    ECO(20,"ECO"),
    SHOUDONGJIASHIKAO(21,"加湿烤"),
    JIASHIBEIKAO(22,"加湿烤"),
    JIASHIFENGBEIKAO(23,"加湿烤-风焙烤"),
    JIASHIQIANGSHAOKAO(24,"加湿烤-强烤烧"),
    JIASHISHAOKAO(25,"加湿烤-烤烧"),
    FAJIAO(32,"发酵"),
    GANZAO(33,"干燥"),
    SHAJUN(34,"杀菌"),
    CHUGOU(35,"除垢"),
    BAOWEN(36,"保温"),
    JIEDONG(37,"解冻"),
    QINGJIE(38,"清洁"),
    FURE(39,"复热"),;



    private int code;
    private String value;

    private SteamOvenModeEnum(int code, String message) {
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

    public static SteamOvenModeEnum match(int key) {

        SteamOvenModeEnum result = null;

        for (SteamOvenModeEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static SteamOvenModeEnum catchMessage(String msg) {

        SteamOvenModeEnum result = null;

        for (SteamOvenModeEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
