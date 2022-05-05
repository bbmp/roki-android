package com.legent.plat.constant;

/**
 * Created by as on 2017-08-18.
 */

public interface IPlatRokiFamily {
    interface IRokiDevicePlat {
        String RQZ01 = "RQZ01";
        String RQZ02 = "RQZ02";
    }

    /**
     * 品类类, 静态变量
     */
    // 油烟机
    String _COOKER = "COOKER";
    //电磁灶
    String _HOB = "HOB";
    //消毒柜
    String _STERILZER = "STERILIZER";
    //电烤箱
    String _OVEN = "OVEN";
    //蒸汽炉
    String _STEAM_OVEN = "STEAMOVEN";
    //微波炉
    String _MICRO_WAVE = "MICROWAVE";
    //净水器
    String _WATER_PURIFIER = "WATERPURIFIER";

    //燃气传感器
    String R0003 = "R0003";

    //火鸡灶
    String KDC01 ="KDC01";

    /**
     * 油烟机
     */
    String R8700 = "R8700";
    /**
     * 油烟机（pad）
     */
    String R9700 = "R9700";
    /**
     * 油烟机（pad）
     */
    String R8230 = "R8230";
    /**
     * 油烟机（pad）
     */
    String _8230S = "8230S";
    /**
     * 油烟机（pad）
     */
    String _8231S = "8231S";
    String _8230C = "8230C";
    /**
     * 油烟机8229 added by zhaiyuanyi
     */
    String R8229 = "R8229";
    String _8235S = "8235S";


    /**
     * 油烟机66A2
     */
    String R66A2 = "R66A2";
    /**
     * 油烟机66A2H
     */
    String _66A2H = "66A2H";
    /**
     * 油烟机5610
     */
    String R5610 = "R5610";
    /**
     * 油烟机R68A0
     */
    String R68A0 = "R68A0";
    String _68A0S = "68A0S";
    /**
     * 油烟机5910
     */
    String R5910 = "R5910";
    String SE638 = "SE638";
    String HE905 = "HE905";
    String _5910S = "5910S";
    String _5916S = "5916S";

    /**
     * 电磁灶
     */
    /**
     * 电磁灶
     */
    String R9W70 = "R9W70";
    String HI704 = "HI704";

    String _9B30C = "9B30C";

    /**
     * 燃气灶
     */
    String R9W851 = "9W851";

    /**
     * 燃气灶   add by zhaiyuanyi
     */
    String R9B12 = "R9B12";

    /**
     * 燃气灶   add by zhaiyuanyi
     */
    String R9B37 = "R9B37";

    /**
     * 燃气灶   add by zhaiyuanyi
     */
    String R9B39 = "R9B39";

    /**
     * 燃气灶
     */
    String R9B39E = "9B39E";

    /**
     * 消毒柜
     */
    String RR829 = "RR829";
    /**
     * 消毒柜
     */
    String RR826 = "RR826";
    String R826L = "R826L";
    String XS855 = "XS855";

    /**
     * 电烤箱
     */
    String RR039 = "RR039";




    /**
     * 电烤箱
     */
    String RR026 = "RR026";
    String HK906 = "HK906";
    /**
     * 电烤箱
     */
    String RR028 = "RR028";
    /**
     * 电烤箱
     */
    String RR016 = "RR016";

    /**
     * 电烤箱
     */
    String RR075 = "RR075";

    /**
     * 微波炉
     */
    String RM509 = "RM509";

    /**
     * 微波炉
     */
    String RM526 = "RM526";

    /**
     * 蒸汽炉
     */
    String RS209 = "RS209";

    /**
     * 蒸汽炉S226
     */
    String RS226 = "RS226";
    String HS906 = "HS906";



    /**
     * 蒸汽炉S226
     */
    String RS275 = "RS275";

    /**
     * 蒸汽炉S226
     */
    String RS228 = "RS228";

    /**
     * 净水器
     */
    String RJ312 = "RJ312";

    /**
     * 净水器
     */
    String RJ321 = "RJ321";
    String RJ320 = "RJ320";

    //by yinwei
    /**
     * 一体机
     */
    String RC906 = "RC906";
    String ZKY01 = "ZKY01";

    /**
     * RIKA烟灶蒸
     */
    String RIKA_Z = "90B8Z";
    String RIKAZ = "RIKAZ";


    /**
     * RIKA烟灶消
     */
    String RIKA_X = "90B8X";
    String RIKAX = "RIKAX";

    /**
     * RIKA烟灶一体机
     */
    String RIKAY = "RIKAY";
    /**
     * 集成灶
     */
    String RJCZ = "RJCZ";
    /**
     * 温控锅
     */
    String R0001 = "R0001";


    String RQZ01 = "RQZ01";
    String RQZ02 = "RQZ02";
    String RQZ05 = "RQZ05";


    /**
     * 洗碗机
     */
    String WB755="WB755";
    /**
     * 藏宝盒
     */
    String KC306 = "KC306";

    /***
     * r任涛增加开始
     ***/
    String all = R8700 + "," + R9700 + "," + R8230 + ","+ _8230S +","+_5910S+","+_8231S+ ","+_8230C+","+ R8229 + "," + R66A2 + "," + R5610 + "," + R9W70 + "," + R9B12 + "," + R9B37 + "," + R9B39 + "," + RR829 + "," + RR826 +"," +RR075 + ","+ RR039 + ","+RR028 + "," + RM509 + ","
            + RS209 + ","+RS226+","+RS228+","+RS275+"," + RJ312+"," + RJ321+","+ RJ320+","+RM526+ "," + RC906+","+_66A2H+","+R0003+","+ RIKA_Z+ "," + RIKA_X+","+KDC01+","+_9B30C+","+HI704+","+HS906+","+HK906+","+R826L;




}
