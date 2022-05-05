package com.robam.common.pojos.device.newTreaty;

/**
 * 新协议故障码
 */
public interface NewTreatyFaultCode {

    /**
     * 无故障
     */
    short noFault = 0;

    /**
     * 先蒸后烤
     */
    short steamOvenOrderFault = 1;

    /**
     * 温度上传感器故障
     */
    short upTempeSensorFault = 2;

    /**
     * 温度下传感器故障
     */
    short downTempeSensorFault = 3;

    /**
     * 散热风机故障
     */
    short coolingFanFault  = 4;

    /**
     * 通讯故障
     */
    short communicationFault  = 5;

    /**
     * 水位传感器故障
     */
    short waterLevelSensorFault = 6;

    /**
     * 按键板传感器故障
     */
    short keyBoardSensorFault = 7;

    /**
     * 高温报警故障
     */
    short highTemperatureAlarmFault = 8;

    /**
     * 温度加热异常故障
     */
    short abnormalTemperatureHeatingFault = 9;



}
