package com.robam.common.pojos.device.newTreaty;

/**
 * 新协议编码值
 */
public interface NewTreatyValue {

    /**
     * 电源状态值 关
     */
    short powerStateValue_Off = 0;

    /**
     * 电源状态值 待机
     */
    short powerStateValue_Await = 1;

    /**
     * 电源状态值 开
     */
    short powerStateValue_On = 2;

    /**
     * 电源状态值 故障
     */
    short powerStateValue_Fault = 3;

    /**
     * 电源状态值 产测
     */
    short powerStateValue_Test = 10;

    /**
     * 电源控制 关
     */
    short powerCtrlValue_Off = 0;

    /**
     * 电源控制 开
     */
    short powerCtrlValue_On = 1;



}
