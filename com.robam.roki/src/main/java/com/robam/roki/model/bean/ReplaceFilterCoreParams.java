package com.robam.roki.model.bean;

/**
 * Created by 14807 on 2018/11/6.
 */

public class ReplaceFilterCoreParams {

    /**
     * title : 滤芯更换流程
     * value : 1.关闭电源，让主机停水2分钟之后，双手拿住主机前面板左右两边，向外取出主机面板；

     2.按对应提示，将需要更换的滤芯拉斜至45度位置；

     3.逆时针旋转，将滤芯拆除，并倒清余水；

     4.将对应的新滤芯插入，并顺时针旋紧，直至小三角标志对应为止，并将安装好的滤芯推至原位；

     5.装上主机前面板;

     6.长按“复位”键，对应滤芯指示灯由红色恢复蓝色，表示滤芯已复位；

     7.复位完后，按“冲洗”键，对新滤芯进行安装冲洗。

     8.冲洗结束后，请打开净水龙头对整机水路进行清洗，此过程约10~15min；待净水龙头出水清澈为止；至此，换芯完成。
     * paramType : String
     */

    private String title;
    private String value;
    private String paramType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
}
