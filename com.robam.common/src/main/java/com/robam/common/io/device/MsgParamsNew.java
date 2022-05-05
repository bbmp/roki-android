package com.robam.common.io.device;

/**
 * 新协议
 */
public interface MsgParamsNew {

    /**
     * 属性设置功能区分
     * 0 ：设置一体机属性（单模式）
     * 1 ：设置一体机单一工作状态 暂停 开始 停止
     * 2 ：设置多段
     * 3 : 设置菜谱
     * 4 : 控制烟机风量 开启 关闭
     */
    String type = "type";
    /*--------------------------一体机--------------------------------*/
    /**
     * 电源状态
     */
    String powerState = "powerState" ;

    /**
     * 电源控制
     */
    String powerCtrl = "powerCtrl" ;
    String powerCtrlKey = "powerCtrlKey" ;
    String powerCtrlLength = "powerCtrlLength" ;
    /**
     * 工作状态
     */
    String workState = "workState" ;
    /**
     * 工作控制
     */
    String workCtrl  = "workCtrl" ;
    String workCtrlKey  = "workCtrlKey" ;
    String workCtrlLength  = "workCtrlLength" ;
    /**
     * 设置预约时间
     */
    String setOrderMinutes  = "setOrderMinutes" ;
    String setOrderMinutesKey  = "setOrderMinutesKey" ;
    String setOrderMinutesLength  = "setOrderMinutesLength" ;
    /**
     * 剩余预约时间
     */
    String orderLeftMinutes = "orderLeftMinutes" ;
    /**
     * 故障码
     */
    String faultCode = "faultCode" ;
    /**
     * 灯开关
     */
    String lightSwitch = "lightSwitch" ;
    String lightSwitchKey = "lightSwitchKey" ;
    String lightSwitchLength = "lightSwitchLength" ;
    /**
     * 旋转烤开关
     */
    String rotateSwitch = "rotateSwitch" ;
    /**
     * 水箱状态
     */
    String waterBoxState = "waterBoxState" ;
    /**
     * 水箱控制
     */
    String waterBoxCtrl = "waterBoxCtrl" ;
    String waterBoxCtrlKey = "waterBoxCtrlKey" ;
    String waterBoxCtrlLength = "waterBoxCtrlLength" ;
    /**
     * 水位状态
     */
    String waterLevelState = "waterLevelState" ;
    /**
     * 门状态
     */
    String doorState = "doorState" ;
    /**
     * 门控制开关
     */
    String doorSwitch = "doorSwitch" ;
    String doorSwitchKey = "doorSwitchKey" ;
    String doorSwitchLength = "doorSwitchLength" ;
    /**
     * 加蒸汽工作状态
     */
    String steamState = "steamState" ;
    /**
     * 加蒸汽控制
     */
    String steamCtrl = "steamCtrl" ;
    String steamCtrlKey = "steamCtrlKey" ;
    String steamCtrlLength = "steamCtrlLength" ;
    /**
     * 菜谱编号
     */
    String recipeId = "recipeId" ;
    String recipeIdKey = "recipeIdKey" ;
    String recipeIdLength = "recipeIdLength" ;
    /**
     * 菜谱设置总时间
     */
    String recipeSetMinutes = "recipeSetMinutes" ;
    String recipeSetMinutesH = "recipeSetMinutesH" ;
    String recipeSetMinutesKey = "recipeSetMinutesKey" ;
    String recipeSetMinutesLength = "recipeSetMinutesLength" ;
    /**
     * 当前温度 上温度
     */
    String curTemp = "curTemp" ;
    /**
     * 当前温度 下温度
     */
    String curTemp2 = "curTemp2" ;
    /**
     * 剩余总时间
     */
    String totalRemainSeconds = "totalRemainSeconds" ;
    String totalRemainSeconds2 = "totalRemainSeconds2" ;
    /**
     * 除垢请求标识
     */
    String descaleFlag = "descaleFlag" ;
    /**
     * 当前蒸模式累计工作时间
     */
    String curSteamTotalHours = "curSteamTotalHours" ;
    /**
     * 蒸模式累计需除垢时间
     */
    String curSteamTotalNeedHours = "curSteamTotalNeedHours" ;
    /**
     * 实际运行时间
     */
    String cookedTime = "cookedTime" ;
    /**
     * 除垢状态
     */
    String chugouType = "chugouType" ;
    /**
     * 当前段数/段序
     */
    String curSectionNbr = "curSectionNbr" ;
    /**
     * 设置段数
     */
    String sectionNumber = "sectionNumber" ;
    String sectionNumberKey = "sectionNumberKey" ;
    String sectionNumberLength = "sectionNumberLength" ;
    /**
     * 首段模式
     */
    String mode = "mode" ;
    String modeKey = "modeKey" ;
    String modeLength = "modeLength" ;
    /**
     * 首段设置的上温度
     */
    String setUpTemp = "setUpTemp" ;
    String setUpTempKey = "setUpTempKey" ;
    String setUpTempLength = "setUpTempLength" ;
    /**
     * 首段设置的下温度
     */
    String setDownTemp = "setDownTemp" ;
    String setDownTempKey = "setDownTempKey" ;
    String setDownTempLength = "setDownTempLength" ;
    /**
     * 首段设置的时间
     */
    String setTime = "setTime" ;
    String setTimeH = "setTimeH" ;
    String setTimeKey = "setTimeKey" ;
    String setTimeLength = "setTimeLength" ;
    /**
     * 首段剩余的时间
     */
    String restTime = "restTime" ;
    String restTimeH = "restTimeH" ;
    /**
     * 首段蒸汽量
     */
    String steam = "steam" ;
    String steamKey = "steamKey" ;
    String steamLength = "steamLength" ;

    /**
     * 第2段段模式
     */
    String mode2 = "mode2" ;
    /**
     * 第2段设置的上温度
     */
    String setUpTemp2 = "setUpTemp2" ;
    /**
     * 第2段设置的下温度
     */
    String setDownTemp2 = "setDownTemp2" ;
    /**
     * 第2段设置的时间
     */
    String setTime2 = "setTime2" ;
    String setTimeH2 = "setTimeH2" ;
    /**
     * 第2段剩余的时间
     */
    String restTime2= "restTime2" ;
    String restTimeH2= "restTimeH2" ;
    /**
     * 第2段蒸汽量
     */
    String steam2 = "steam2" ;

    /**
     * 第3段段模式
     */
    String mode3 = "mode3" ;
    /**
     * 第3段设置的上温度
     */
    String setUpTemp3 = "setUpTemp3" ;
    /**
     * 第3段设置的下温度
     */
    String setDownTemp3 = "setDownTemp3" ;
    /**
     * 第3段设置的时间
     */
    String setTime3 = "setTime3" ;
    String setTimeH3 = "setTimeH3" ;
    /**
     * 第3段剩余的时间
     */
    String restTime3= "restTime3" ;
    String restTimeH3= "restTimeH3" ;
    /**
     * 第3段蒸汽量
     */
    String steam3 = "steam3" ;

    /*--------------------------烟机--------------------------------*/
    /**
     * 电源状态 烟机
     */
    String fan_powerState = "powerState" ;

    /**
     * 电源控制 烟机
     */
    String fan_powerCtrl = "powerCtrl" ;
    String fan_powerCtrlKey = "powerCtrlKey" ;
    String fan_powerCtrlLength= "powerCtrlLength" ;
    /**
     * 档位 烟机
     */
    String fan_gear = "gear" ;
    String fan_gearKey = "gearKey" ;
    String fan_gearLength = "gearLength" ;
    /**
     * 灯开关 烟机
     */
    String fan_lightSwitch = "lightSwitch" ;
    String fan_lightSwitchKey = "lightSwitchKey" ;
    String fan_lightSwitchLength = "lightSwitchLength" ;
    /**
     * 烟灶联动开关 烟机
     */
    String fan_stove_linkage = "fan_stove_linkage" ;

    /*--------------------------灶具--------------------------------*/
    /**
     * 电源状态 灶具
     */
    String stove_powerState = "powerState" ;

    /**
     * 电源控制 灶具
     */
    String stove_powerCtrl = "powerCtrl" ;
    String stove_powerCtrlKey = "powerCtrlKey" ;
    String stove_powerCtrlLength= "powerCtrlLength" ;
    /**
     * 炉头数量 灶具
     */
    String stove_head_num = "stove_head_num" ;
    /**
     * 炉头类型 灶具
     */
    String stove_head_type = "stove_head_type" ;
    /**
     * 功率档位 灶具
     */
    String stove_gear = "stove_gear" ;

    /**
     * 童锁 灶具
     */
    String stove_v_chip = "v_chip" ;
    /**
     * 菜谱ID 灶具
     */
    String repice_id = "repice_id" ;
    /**
     * 灶具温度  灶具
     */
    String stove_temp = "stove_temp" ;
    /**
     * 已工作时间 灶具
     */
    String stove_time = "stove_time" ;


    /**
     * 报警状态 灶具
     */
    String stove_faultCode = "stove_faultCode" ;

    /**
     * 电源状态 灶具
     */
    String stove_powerState2 = "powerState2" ;

    /**
     * 电源控制 灶具
     */
    String stove_powerCtrl2 = "powerCtrl2" ;
    String stove_powerCtrlKey2 = "powerCtrlKey2" ;
    String stove_powerCtrlLength2= "powerCtrlLength2" ;

    /**
     * 炉头类型 灶具
     */
    String stove_head_type2 = "stove_head_type2" ;
    /**
     * 功率档位 灶具
     */
    String stove_gear2 = "stove_gear2" ;

    /**
     * 童锁 灶具
     */
    String stove_v_chip2 = "stove_v_chip2" ;
    /**
     * 菜谱ID 灶具
     */
    String repice_id2 = "repice_id2" ;
    /**
     * 灶具温度  灶具
     */
    String stove_temp2 = "stove_temp2" ;
    /**
     * 已工作时间 灶具
     */
    String stove_time2 = "stove_time2" ;

    /**
     * 报警状态 灶具
     */
    String stove_faultCode2 = "stove_faultCode2" ;
}