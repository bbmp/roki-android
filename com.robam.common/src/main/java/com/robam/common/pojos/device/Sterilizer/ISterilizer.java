package com.robam.common.pojos.device.Sterilizer;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;

/**
 * Created by zhaiyuanyi on 15/11/19.
 */
public interface ISterilizer extends IPauseable {
    String getSterilizerModel();


    /**
     * 读取消毒柜峰谷定时设置
     *
     */
    void getSteriPVConfig(Callback<SteriSmartParams> callback);

    /**
     * 设置消毒柜峰谷定时命令开启
     * 是否开启定时消毒[1BYTE] SteriSwitchDisinfect
     *定时消毒间隔时间[1BYTE], SteriInternalDisinfect  单位天
     *是否开启每周消毒[1BYTE]  SteriSwitchWeekDisinfect
     *每周消毒的时时间[1BYTE]  SteriWeekInternalDisinfect
     *消毒柜峰谷电时间[1BYTE]  SteriPVDisinfectTime
     *单位：小时
     */
    void setSteriPVConfig(SteriSmartParams smartParams, VoidCallback callback);
}
