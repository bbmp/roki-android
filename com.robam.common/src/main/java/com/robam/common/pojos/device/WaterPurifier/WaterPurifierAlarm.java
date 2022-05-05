package com.robam.common.pojos.device.WaterPurifier;

/**
 * Created by Rent on 2016/5/30.
 */
public interface WaterPurifierAlarm {
    short Water_Leak= 3;//漏水
    short Water_Making =4;//长时间制水
    short Water_None=5;//缺水
    short Filter_Fail=2;//滤芯失效
    short Kettel_Ok=255;//无故障
}
