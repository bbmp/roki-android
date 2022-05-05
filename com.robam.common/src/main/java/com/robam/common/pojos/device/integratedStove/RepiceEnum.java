package com.robam.common.pojos.device.integratedStove;

/**
 * @author r210190
 *  除垢阶段对应的文案
 */

public enum RepiceEnum {
    /**
     *
     */
    REPICE_44(44,"低脂杏鲍菇"),
    REPICE_45(45,"手枪腿"),
    REPICE_46(46,"轻脂鸡块"),
    REPICE_47(47,"酱烤玉米"),
    REPICE_48(48,"脆皮炸鲜奶"),
    REPICE_49(49,"欧包"),
    REPICE_50(50,"香酥鸭"),
    REPICE_51(51,"蒜烤马铃薯"),
    REPICE_52(52,"黑胡椒烤肋排"),
    REPICE_53(53,"烤猪蹄"),
    REPICE_10(10,"清蒸明虾"),
    REPICE_11(11,"扇贝粉丝"),
    REPICE_12(12,"豆豉蒸排骨"),
    REPICE_54(54,"烤鱼排"),
    REPICE_55(55,"蒸烤茄子"),
    REPICE_56(56,"芝士肉酱焗饭"),
    REPICE_13(13,"酸奶"),
    REPICE_57(57,"新法扒鸡"),
    REPICE_14(14,"蒸馒头"),
    REPICE_58(58,"蜜汁五花肉"),
    REPICE_15(15,"清蒸水饺"),
    REPICE_59(59,"酥嫩大鸡排"),
    REPICE_16(16,"家乡清蒸鸡"),
    REPICE_17(17,"蛋黄酥"),
    REPICE_18(18,"戚风蛋糕"),
    REPICE_19(19,"香蕉松糕"),
    REPICE_1(1,"剁椒鱼头"),
    REPICE_2(2,"清蒸大闸蟹"),
    REPICE_3(3,"红薯粉蒸肉"),
    REPICE_4(4,"花菇木耳蒸鸡"),
    REPICE_5(5,"肉饼蒸蛋"),
    REPICE_6(6,"腊味合蒸"),
    REPICE_7(7,"红糖发糕"),
    REPICE_8(8,"上汤娃娃菜"),
    REPICE_9(9,"鸡蛋羹"),
    REPICE_60(60,"蜜汁西兰花"),
    REPICE_61(61,"清蒸鲈鱼"),
    REPICE_62(62,"五谷丰登"),
    REPICE_63(63,"梅菜扣肉"),
    REPICE_20(20,"蔓越莓饼干"),
    REPICE_64(64,"鲜肉月饼"),
    REPICE_21(21,"烤牛排"),
    REPICE_22(22,"葡式蛋挞"),
    REPICE_23(23,"奶油蛋糕卷"),
    REPICE_24(24,"椰蓉夹心餐包"),
    REPICE_25(25,"烤披萨"),
    REPICE_26(26,"奶油泡芙"),
    REPICE_27(27,"培根蔬菜卷"),
    REPICE_28(28,"酱汁烤鱿鱼"),
    REPICE_29(29,"果香烤鸡"),
    REPICE_30(30,"金牌烤鱼"),
    REPICE_31(31,"锡纸烤排骨"),
    REPICE_32(32,"奥尔良鸡翅"),
    REPICE_33(33,"香烤鳕鱼"),
    REPICE_34(34,"果香烤猪扒"),
    REPICE_35(35,"彩椒牛肉串"),
    REPICE_36(36,"海盐秋刀鱼"),
    REPICE_37(37,"烤红薯"),
    REPICE_38(38,"棒棒翅"),
    REPICE_39(39,"迷迭香烤羊排"),
    REPICE_40(40,"照烧汁三文鱼"),
    REPICE_41(41,"炸薯条"),
    REPICE_42(42,"炸油条"),
    REPICE_43(43,"炸鸡米花"),


    ;

    private int code;
    private String value;

    private RepiceEnum(int code, String message) {
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

    public static RepiceEnum match(int key) {

        RepiceEnum result = null;

        for (RepiceEnum s : values()) {
            if (s.getCode()==key) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static RepiceEnum catchMessage(String msg) {

        RepiceEnum result = null;

        for (RepiceEnum s : values()) {
            if (s.getValue().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

}
