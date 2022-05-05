package com.robam.common.io.device;

/**
 *
 */
public interface MsgParams {


    /**
     * 回应码 1B 0-成功，1-失败
     */
    String RC = "RC";
    String Key = "KEY";
    String Length = "LENGTH";

    /**
     * • 控制端类型[1Byte]，参考编码表
     */
    String TerminalType = "TerminalType";

    /**
     * 设备类型编码
     */
    String equipmentCoding = "equipmentCoding";

    /**
     * 用户编码[10Byte]
     */
    String UserId = "UserId";

    /**
     * • 是否菜谱烧菜[1Byte]，0不是，1是
     */
    String IsCook = "IsCook";

    /**
     * 报警码[1Byte]
     */
    String AlarmId = "AlarmId";

    /**
     * 事件码[1Byte]
     */
    String EventId = "EventId";


    /**
     * 事件参数 1Byte
     */
    String EventParam = "EventParam";
    String powerConsumption = "powerConsumption";
    String waterConsumption = "waterConsumption";

    /**
     * 温控锅干烧预警开关
     */
    String FryAlarm = "FryAlarm";

    /**
     * 温控锅联动上报开关
     */
    String CombineSwitch = "CombineSwitch";


    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    /**
     * 童锁状态[1Byte]，{0：解锁，1上锁}
     */
    String StoveLock = "StoveLock";

    /**
     * 炉头ID[1Byte]（0左，1右）
     */
    String IhId = "IhId";

    /**
     * • 炉头Num[1Byte], 炉头的数量
     */
    String IhNum = "IhNum";

    /**
     * 炉头信息列表，数量=IhNum
     */
    String StoveHeadList = "StoveHeadList";

    /**
     * 炉头工作状态[1Byte]（0关，1待机，2工作中）
     */
    String IhStatus = "IhStatus";

    /**
     * 炉头功率等级[1Byte]（0-9档）
     */
    String IhLevel = "IhLevel";

    /**
     * 炉头定时关机时间[2BYTE]（0-6000，单位：秒）
     */
    String IhTime = "IhTime";

    /**
     * 自动关火开关（0关，1开）
     */

    String AutoPowerOff = "AutoPowerOff";

    String StoveLevel = "StoveLevel";

    String AutoPowerOffTime = "AutoPowerOffTime";

    String AutoPowerOffTimeForOne = "AutoPowerOffTimeForOne";
    String AutoPowerOffTimeForTwo = "AutoPowerOffTimeForTwo";
    String AutoPowerOffTimeForThree = "AutoPowerOffTimeForThree";
    String AutoPowerOffTimeForFour = "AutoPowerOffTimeForFour";
    String AutoPowerOffTimeForFive = "AutoPowerOffTimeForFive";
    // -------------------------------------------------------------------------------
    //燃气传感器

    String GasSensorStatus = "GasSensorStatus";

    String GasCon = "GasCon";

    String Alarm = "Alarm";
    // -------------------------------------------------------------------------------

    /**
     * 烟机电磁灶开关联动 联动参数:灶具开，烟机开（0关，1开） 1B
     */
    String IsPowerLinkage = "IsPowerLinkage";

    /**
     * 烟机档位联动开关 联动参数:烟机档位是否联动（0关，1开） 1B
     */
    String IsLevelLinkage = "IsLevelLinkage";

    /**
     * 电磁灶关机后烟机延时关机 联动参数:电磁灶关机后，烟机是否延时关机（0关，1开） 1B
     */
    String IsShutdownLinkage = "IsShutdownLinkage";

    /**
     * 电磁灶关机后烟机延时关机时间 联动参数:电磁灶关机后烟机延时关机时间（延时时间，单位分钟，1~5分钟） 1B
     */
    String ShutdownDelay = "ShutdownDelay";

    /**
     * 灶具传过来的2个温度参数
     */
    String Pot_Temp = "Pot_Temp";

    String Pot_keybood = "Pot_keybood";

    /**
     * 温控锅按键值上报
     */
    String Key_value = "Key_value";


    /**
     * 油烟机清洗提示开关 [1Byte], 0不提示，1提示
     */
    String IsNoticClean = "IsNoticClean";

    /**
     * 是否开启定时通风[1BYTE]
     */
    String IsTimingVentilation = "IsTimingVentilation";

    /**
     * 定时通风间隔时间[1BYTE],单位天
     */
    String TimingVentilationPeriod = "TimingVentilationPeriod";


    /**
     * 是否开启每周通风[1BYTE]
     */
    String IsWeeklyVentilation = "IsWeeklyVentilation";

    /**
     * 每周通风的时间--周几
     */
    String WeeklyVentilationDate_Week = "WeeklyVentilationDate_Week";

    /**
     * 每周通风的时间--小时
     */
    String WeeklyVentilationDate_Hour = "WeeklyVentilationDate_Hour";

    /**
     * 每周通风的时间--分钟
     */
    String WeeklyVentilationDate_Minute = "WeeklyVentilationDate_Minute";

    /**
     * 功率等级[1Byte]（0、1、2、3、6档）
     */
    String FanLevel = "FanLevel";

    /**
     * 工作状态[1Byte]（0关机，1开机）
     */
    String FanStatus = "FanStatus";

    /**
     * 灯开关［1Byte］（0关，1开）
     */
    String FanLight = "FanLight";

    /**
     * 是否需要清洗［1Byte］（0不需要，1需要）
     */
    String NeedClean = "NeedClean";

    /**
     * 油烟机定时工作 定时时间，[1Byte]（单位：分钟）
     */
    String FanTime = "FanTime";

    /**
     * 油烟机wifi状态 -1未查询到 0断网 1链接路由器 2链接服务器，[1Byte]
     */
    String FanWIfi = "FanWifi";

    /**
     * 防回烟
     */
    String BackSmoke = "BackSmoke";


    //等候时间
    String waitTime = "waitTime";

    //空气检测
    String aerialDetection = "aerialDetection";

    //油杯
    String oilCup = "oilCup";

    /**
     * 智能烟感状态
     */
    String smartSmokeStatus = "smartSmokeStatus";

    /**
     * 红外温度上报  前四字节第一路温度，后四字节第二路温度
     */
    String InfrTempRatureRepor = "InfrTempRatureRepor";

    /**
     * 防干烧报警状态
     */
    String dryBurningAlarmStatus = "dryBurningAlarmStatus";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    /**
     * 消毒柜工作状态[1Byte]（0关机，1开机）
     */
    String SteriStatus = "SteriStatus";

    /**
     * 设置消毒柜工作时间
     */
    String SteriTime = "SteriTime";


    /**
     * 预约剩余时间
     */
    String SteriReminderTime = "SteriReminderTime";

    /**
     * 安全锁定
     */
    String SteriSecurityLock = "SteriSecurityLock";


    /**
     * ORDER_TIME[1Byte] {0:预约取消，1，2，3，4…24预约时间}
     */
    String SteriReserveTime = "SteriReserveTime";

    /**
     * DRYING_TIME[1Byte] {0取消烘干，>1 为烘干时间}
     */
    String SteriDryingTime = "SteriDryingTime";
    /**
     * CLEAN_TIME[1Byte] {0取消保洁，60，保洁时间}
     */
    String SteriCleanTime = "SteriCleanTime";
    /**
     * DISINFECT_TIME[1Byte] {0取消消毒,150消毒时间}
     */
    String SteriDisinfectTime = "SteriDisinfectTime";
    /**
     * ON_OFF [1Byte] {0取消童锁，1，开童锁}
     */
    String SteriLock = "SteriLock";
    /**
     * WORK_TIME_LEFT{0:无/剩余时间到，>剩余时间} 分钟字节
     */
    String SteriWorkLeftTimeL = "SteriWorkLeftTimeL";
    /**
     * WORK_TIME_LEFT{0:无/剩余时间到，>剩余时间} 小时字节
     */
    String SteriWorkLeftTimeH = "SteriWorkLeftTimeH";
    /**
     * DOORLOCK{0：门锁关，1 门锁关}
     */
    String SteriDoorLock = "SteriDoorLock";
    /**
     * ALARM {0xff:无报警，
     * 0x00:门控报警，
     * 0x01: 紫外线灯管不工作或上层传感器不良
     * 0x02: 温度传感器不良}
     */
    String SteriAlarmStatus = "SteriAlarmStatus";
    /**
     * 是否开启定时消毒[1BYTE]
     */
    String SteriSwitchDisinfect = "SteriSwitchDisinfect";
    /**
     * 定时消毒间隔时间[1BYTE],单位天
     */
    String SteriInternalDisinfect = "SteriInternalDisinfect";
    /**
     * 是否开启每周消毒[1BYTE]
     */
    String SteriSwitchWeekDisinfect = "SteriSwitchWeekDisinfect";
    /**
     * 每周消毒的时时间[1BYTE]
     */
    String SteriWeekInternalDisinfect = "SteriWeekInternalDisinfect";
    /**
     * 消毒柜峰谷电时间[1BYTE]
     */
    String SteriPVDisinfectTime = "SteriPVDisinfectTime";

    /**
     * 暖碟温度
     */
    String warmDishTempValue = "warmDishTempValue";
    /**
     * 消毒柜参数：
     * TEM[1Byte]  温度值
     * HUM [1Byte]  湿度值
     * GERM [1Byte] 细菌值
     * 臭氧[1Byte]
     */
    String SteriParaTem = "SteriParaTem";
    String SteriParaHum = "SteriParaHum";
    String SteriParaGerm = "SteriParaGerm";
    String SteriParaOzone = "SteriParaOzone";

    // ---------------------------------------------------------------------------------------------
    // 蒸汽炉json参数
    // ---------------------------------------------------------------------------------------------

    /**
     * 童锁状态
     */
    String SteamLock = "SteamLock";
    /**
     * 蒸汽炉工作状态
     */
    String SteamStatus = "SteamStatus";
    String OrderTime = "OrderTime";
    /**
     * 蒸汽炉工作模式
     */
    String SteamMode = "SteamMode";
    /**
     * 蒸汽炉警告
     */
    String SteamAlarm = "SteamAlarm";
    /**
     * 蒸汽炉门阀状态
     */
    String SteamDoorState = "SteamDoorState";
    /**
     * 蒸汽炉工作当前温度和剩余时间
     */
    String SteamTemp = "SteamTemp";
    String SteamTime = "SteamTime";
    /**
     * 蒸汽炉设定时间
     */
    String SteamTempSet = "SteamTempSet";
    String SteamTimeSet = "SteamTimeSet";
    String PreFlag = "preFlag";
    //设置烹饪模式
    String SetMeum = "SetMeum";

    String setSteamAutoMode = "setSteamAutoMode";
    /**
     * 蒸汽炉菜谱id 菜谱步骤
     */

    String SteamRecipeId = "SteamRecipeId";
    String SteamRecipeStep = "SteamRecipeStep";
    String SteamRecipeTotalStep = "SteamRecipeStep";

    String SteamRecipeKey = "SteamRecipeKey";
    String SteamRecipeLength = "SteamRecipeLength";
    String SteamRecipeValue = "SteamRecipeValue";

    String SteamRecipeUniqueKey = "SteamRecipeUniqueKey";
    String SteamRecipeUniqueLength = "SteamRecipeUniqueLength";
    String SteamRecipeUniqueValue = "SteamRecipeUniqueValue";

    String SteamRecipeConcreteKey = "SteamRecipeConcreteKey";
    String SteamRecipeConcreteLength = "SteamRecipeConcreteLength";
    String SteamOvenWaterBoxKey = "SteamOvenWaterBoxKey";
    String SteamOvenWaterBoxLength = "SteamOvenWaterBoxLength";
    String SteamOvenWaterBoxValue = "SteamOvenWaterBoxValue";
    String SteamOvenCurrentStageKey = "SteamOvenCurrentStageKey";
    String SteamOvenCurrentStageLength = "SteamOvenCurrentStageLength";
    String SteamOvenCurrentStageValue = "SteamOvenCurrentStageValue";

    /**
     * 新增蒸箱设备275
     */
    String setSteamModeSendKey = "setSteamModeSendKey";
    String setSteamModeSendLength = "setSteamModeSendLength";
    String setSteamModeSendValue = "setSteamModeSendValue";

    String setSteamTemptureSendKey = "setSteamTemptureSendKey";
    String setSteamTemptureSendLength = "setSteamTemptureSendLength";
    String setSteamTemptureSendValue = "setSteamTemptureSendValue";

    String setSteamTimeSendKey = "setSteamTimeSendKey";
    String setSteamTimeSendLength = "setSteamTimeSendLength";
    String setSteamTimeSendValue = "setSteamTimeSendValue";

    /**
     * 新增蒸箱235
     */

    String SteamAutoRecipeModeKey = "SteamAutoRecipeModeKey";
    String SteamAutoRecipeModeLength = "SteamAutoRecipeModeLength";
    String SteamAutoRecipeModeValue = "SteamAutoRecipeModeValue";

    String DescaleModeStageKey = "DescaleModeStageKey";
    String DescaleModeStageLength = "DescaleModeStageLength";
    String DescaleModeStageValue = "DescaleModeStageValue";


    String WeatherDescalingKey = "WeatherDescalingKey";
    String WeatherDescalingLength = "WeatherDescalingLength";
    String WeatherDescalingValue = "WeatherDescalingValue";

    //2020年11月5日 10:22:26 新增
    String SetTime2_key = "SetTime2_key";
    String SetTime2_length = "SetTime2_length";
    String SetTime2_value = "SetTime2_value";

    // ------------------------------------------------------------------------
    // 微波炉json参数
    // ------------------------------------------------------------------------
    String MicroWaveStatus = "MicroWaveStatus";
    String MicroWaveMode = "MicroWaveMode";
    String MicroWaveWeight = "MicroWaveWeight";
    String MicroWaveLight = "MicroWaveLight";
    String MicroWavePower = "MicroWavePower";
    String MicroWaveTime = "MicroWaveTime";
    String MicroWaveDoorState = "MicroWaveDoorState";
    String MicroWaveStepState = "MicroWaveStepState";
    String MicroWaveSettime = "MicroWaveSettime";
    String MicroWaveError = "MicroWaveError";
    String MicroWaveRestartNow = "MicroWaveRestartNow";
    String MicroWaveStage = "MicroWaveStage";
    String MicroWaveLinkdMode1 = "MicroWaveLinkdMode1";
    String MicroWaveLinkdMode2 = "MicroWaveLinkdMode2";
    String MicroWaveLinkdMode3 = "MicroWaveLinkdMode3";
    String MicroWaveLinkTime1 = "MicroWaveLinkTime1";
    String MicroWaveLinkTime2 = "MicroWaveLinkTime2";
    String MicroWaveLinkTime3 = "MicroWaveLinkTime3";
    String MicroWaveLinkPower1 = "MicroWaveLinkPower1";
    String MicroWaveLinkPower2 = "MicroWaveLinkPower2";
    String MicroWaveLinkPower3 = "MicroWaveLinkPower3";
    String MicroWaveRecipe = "MicroWaveRecipe";//微波炉菜谱ID
    String MicroRecipeTotalStep = "MicroRecipeTotalStep";
    String MicroRecipeStep = "MicroRecipeStep";//菜谱步骤
//     String ArgumentNumber = "ArgumentNumber";//参数个数
    // ---------------------------------------------------------------------------------------------
    // 烤箱Json参数
    // ---------------------------------------------------------------------------------------------

    /**
     * 烤箱工作状态
     */
    String OvenStatus = "OvenStatus";
    /**
     * 烤箱工作模式
     */
    String OvenMode = "OvenMode";
    /**
     * 烤箱警告
     */
    String OvenAlarm = "OvenAlarm";
    /**
     * 烤箱运行状态
     */
    String OvenRunP = "OvenRunP";
    /**
     * 烤箱烤叉旋转
     */
    String OvenRevolve = "OvenRevolve";
    /**
     * 烤箱灯光
     */
    String OvenLight = "OvenLight";
    /**
     * 烤箱设置温度和设置时间
     */
    String OvenSetTime = "OvenSetTime";
    String OvenSetTemp = "OvenSetTemp";
    /**
     * 烤箱工作当前温度和剩余时间
     */
    String OvenTemp = "OvenTemp";
    String OvenTime = "OvenTime";
    String OvenPreFlag = "OvenPreflag";
    String OvenTempBelow = "OvenTempBelow";
    String OvenTempUp = "OvenTempUp";
    //新增指令by 周定钧
    /**
     * 剩余时间leftTime
     * 烤箱自动模式
     * 菜谱ID[1Byte]
     * 菜谱总步骤数[1Byte]
     * 菜谱步骤[1Byte]
     * 参数个数[1Byte]
     * 设置下温度[1Byte]
     */
    String leftTime = "LeftTime";
    String ovenAutoMode = "ovenAutoMode";
    String ovenLight = "ovenLight";
    String OvenRecipeId = "OvenRecipeId";//菜谱ID
    String OvenRecipeTotalStep = "OvenRecipeTotalStep";
    String OvenRecipeStep = "OvenRecipeStep";//菜谱步骤
    String ArgumentNumber = "ArgumentNumber";//参数个数
    String SetTempDown = "SetTempDown";
    String SetTempDownKey = "SetTempDownKey";
    String SetTempDownLength = "SetTempDownLength";
    String SetTempDownValue = "SetTempDownValue";
    //    String CurrentTempDown = "CurrentTempDown";
    String CurrentTempDownKey = "CurrentTempDownKey";
    String CurrentTempDownLength = "CurrentTempDownLength";
    String CurrentTempDownValue = "CurrentTempDownValue";
    String OrderTime_key = "OrderTime_key";
    String SetTime_H_key = "SetTime_H_key";
    String SetTime_H = "SetTime_H";
    String SetTime_H_length = "SetTime_H_length";
    String SetTime_H_Value = "SetTime_H_Value";
    String OrderTime_value_min = "OrderTime_value_min";
    String OrderTime_value_hour = "OrderTime_value_hour";
    String OrderTime_length = "OrderTime_length";

    //新增

    //旋转烤
    String Rotatebarbecue = "Rotatebarbecue";
    String RotatebarbecueLength = "RotatebarbecueLength";
    String RotatebarbecueValue = "RotatebarbecueValue";


    String SteamLight = "SteamLight";
    String SteameOvenOneWaterbox = "SteameOvenOneWaterbox";
    String CurrentStatusKey = "CurrentStatusKey";
    String CurrentStatusLength = "CurrentStatusLength";
    String CurrentStatusValue = "CurrentStatusValue";

    String PlatInsertStatuekey = "PlatInsertStatuekey";
    String PlatInsertStatueLength = "PlatInsertStatueLength";
    String PlatInsertStatueValue = "PlatInsertStatueValue";

    String SetTemp2Key = "SetTemp2Key";
    String SetTemp2Length = "SetTemp2Length";
    String SetTemp2Value = "SetTemp2Value";

    String SetTime2Key = "SetTime2Key";
    String SetTime2Length = "SetTime2Length";
    String SetTime2Value = "SetTime2Value";
    //设置高位时间
    String SetTimeHKey = "SetTimeHKey";
    String SetTimeHLength = "SetTimeHLength";
    String SetTimeHValue = "SetTimeHValue";

    String TempKey = "TempKey";
    String TempLength = "TempLength";
    String TempValue = "TempValue";


    String LeftTime2Key = "LeftTime2Key";
    String LeftTime2Length = "LeftTime2Length";
    String LeftTime2Value = "LeftTime2Value";

    String status2Key = "status2Key";
    String status2Length = "status2Length";
    String status2Values = "status2Values";


    //新增多段总数
    String TotalKey = "TotalKey";
    String TotalLength = "TotalLength";
    String TotalValue = "TotalValue";

    /**
     * 烤箱新增多段模式字段
     */
    String OvenStagekey = "OvenStagekey";
    String OvenStageLength = "OvenStageLength";
    String OvenStageValue = "OvenStageValue";

    String OvenStep1Modekey = "OvenStep1Modekey";
    String OvenStep1ModeLength = "OvenStep1ModeLength";
    String OvenStep1ModeValue = "OvenStep1ModeValue";

    String OvenStep1SetTempkey = "OvenStep1SetTempkey";
    String OvenStep1SetTempLength = "OvenStep1SetTempLength";
    String OvenStep1SetTempValue = "OvenStep1SetTempValue";

    String OvenStep1SetTimekey = "OvenStep1SetTimekey";
    String OvenStep1SetTimeLength = "OvenStep1SetTimeLength";
    String OvenStep1SetTimeValue = "OvenStep1SetTimeValue";

    String OvenStep2Modekey = "OvenStep2Modekey";
    String OvenStep2ModeLength = "OvenStep2ModeLength";
    String OvenStep2ModeValue = "OvenStep2ModeValue";

    String OvenStep2SetTempkey = "OvenStep2SetTempkey";
    String OvenStep2SetTempLength = "OvenStep2SetTempLength";
    String OvenStep2SetTempValue = "OvenStep2SetTempValue";

    String OvenStep2SetTimekey = "OvenStep2SetTimekey";
    String OvenStep2SetTimeLength = "OvenStep2SetTimeLength";
    String OvenStep2SetTimeValue = "OvenStep2SetTimeValue";

    String OvenStep3Modekey = "OvenStep3Modekey";
    String OvenStep3ModeLength = "OvenStep3ModeLength";
    String OvenStep3ModeValue = "OvenStep3ModeValue";

    String OvenStep3SetTempkey = "OvenStep3SetTempkey";
    String OvenStep3SetTempLength = "OvenStep3SetTempLength";
    String OvenStep3SetTempValue = "OvenStep3SetTempValue";

    String OvenStep3SetTimekey = "OvenStep3SetTimekey";
    String OvenStep3SetTimeLength = "OvenStep3SetTimeLength";
    String OvenStep3SetTimeValue = "OvenStep3SetTimeValue";

    /**
     * 净水器工作状态
     */
    String WaterPurifiyStatus = "WaterPurifiyStatus";
    /**
     * 净水器工作制水水量
     */
    String WaterPurifiyModel = "WaterPurifiyModel";
    /**
     * 净水器工作状态
     */
    String WaterPurifierStatus = "WaterPurifierStatus";
    /**
     * 净水器警报
     */
    String WaterPurifierAlarm = "WaterPurifierAlarm";
    /**
     * 净水器已经工作时间
     */
    String WaterWorkTime = "WaterWorkTime";
    /**
     * 已经净化的水
     */
    String WaterCleand = "WaterCleand";
    /**
     * 净水器滤芯状态_PP
     */
    String WaterFilterStatus_pp = "WaterFilterStatus_pp";
    /**
     * 净水器滤芯状态_CTO
     */
    String WaterFilterStatus_cto = "WaterFilterStatus_cto";
    /**
     * 净水器滤芯状态_RO1
     */
    String WaterFilterStatus_ro1 = "WaterFilterStatus_ro1";
    /**
     * 净水器滤芯状态_RO2
     */
    String WaterFilterStatus_ro2 = "WaterFilterStatus_ro2";
    /**
     * 净水器滤芯剩余的时间pp
     */
    String WaterFilter_time_pp = "WaterFilter_time_pp";
    String WaterFilter_time_cto = "WaterFilter_time_cto";
    String WaterFilter_time_ro1 = "WaterFilter_time_ro1";
    String WaterFilter_time_ro2 = "WaterFilter_time_ro2";

    /**
     * 净水器每日饮水量
     */
    String WaterEveryDay = "WaterEveryDay";
    /**
     * 净水器净水前水质
     */
    String WaterQualityBefore = "WaterQualityBefore";
    /**
     * 净水器净水后水质
     */
    String WaterQualityAfter = "WaterQualityAfter";
    /**
     * 净水器开关
     */
    String WaterPurifierSwitch = "WaterPurifierSwitch";
    /**
     * 净水器制水
     */
    String WaterPurifierClean = "WaterPurifierClean";
    /**
     * 净水器冲洗
     */
    String WaterPurifierWash = "WaterPurifierWash";
    /**
     * 净水器滤芯到期时间
     */
    String WaterPurifierFiliter = "WaterPurifierFiliter";
    /**
     * 净水器当日饮水量上报
     */
    String WaterPurifierDayReport = "WaterPurifierDayReport";
    /**
     * 净水器设置制水升量
     */
    String WaterPurifierKettelCount = "WaterPurifierKettelCount";

    /**
     * 设置连续制水的key
     */
    String SetWaterPurifierSystemKey = " SetWaterPurifierSystemKey";
    String SetWaterPurifierSystemLength = "SetWaterPurifierSystemLength";
    String SetSetWaterPurifierSystemValue = "SetSetWaterPurifierSystemValue";

    String WaterCurrentQuilityKey = "WaterCurrentQuilityKey";
    String WaterCurrentQuilityLength = "WaterCurrentQuilityLength";
    String WaterCurrentQuilityValue = "WaterCurrentQuilityValue";
    /**
     * 设置省电模式
     */
    String setWaterPurifierPowerSavingKey = "setWaterPurifierPowerSavingKey";
    String SetWaterPurifierPowerSavingLength = "SetWaterPurifierPowerSavingLength";
    String SetWaterPurifierPowerSavingValue = "SetWaterPurifierPowerSavingValue";


    /**
     * 变频爆炒时间
     */
    String R8230SFryTime = "R8230SFryTime";
    /**
     * 变频爆炒开关
     */
    String R8230SFrySwitch = "R8230SFrySwitch";

    /**
     * 智能烟感开关 0关  1开
     */
    String FanSmartSmokeSwitch = "FanSmartSmokeSwitch";


    /**
     * 倒油杯提示功能开关 0关  1开
     */
    String FanPourOilCupTipSwitch = "FanPourOilCupTipSwitch";

    //油杯提示功能开关
    String FanCupOilSwitch = "FanCupOilSwitch";

    //智能烟感开关
    String FanReducePower = "FanReducePower";

    String gesture = "gesture";

    String RcValue = "RcValue";

    //过温保护

    String OverTempProtectSwitchKey = "OverTempProtectSwitchKey";
    String OverTempProtectSwitchLength = "OverTempProtectSwitchLength";
    String OverTempProtectSwitch = "OverTempProtectSwitch";

    //过温保护设置温度
    String OverTempProtectSetKey = "OverTempProtectSetKey";
    String OverTempProtectSetLength = "OverTempProtectSetLength";
    String OverTempProtectSet = "OverTempProtectSet";
    /**
     * 一体机状态
     */
    String SteameOvenStatus = "SteameOvenStatus";
    String SteameOvenStatus_Key = "SteameOvenStatusKey";
    String SteameOvenStatus_Length = "SteameOvenStatusKeyLength";

    /**
     * 一体机操作状态
     */
    String SteameOvenPowerOnStatus = "SteameOvenPowerOnStatus";

    /**
     * 一体机工作状态
     */
    String SteameOvenWorknStatus = "SteameOvenWorknStatus";


    /**
     * 一体机预约时间
     */
    String SteameOvenOrderTime_min = "SteameOvenOrderTime_min";
    String SteameOvenOrderTime_hour = "SteameOvenOrderTime_hour";


    /**
     * 一体机工作模式
     */
    String SteameOvenMode = "SteameOvenMode";
    String SteameOvenMode_Key = "SteameOvenModeKey";
    String SteameOvenMode_Length = "SteameOvenModeLength";


    /**
     * 一体机温度和时间
     */
    String SteameOvenTemp = "SteameOvenTemp";
    String SteameOvenTime = "SteameOvenTime";
    String SteameOvenTemp2 = "SteameOvenTemp2";
    String SteameOvenTime2 = "SteameOvenTime2";

    String OvenSteamTemp = "OvenSteamTemp";
    String OvenSteamTime = "OvenSteamTime";
    String OvenSteamMode = "OvenSteamMode";
    String OvenSteamUp = "OvenSteamUp";
    String OvenSteamBelow = "OvenSteamBelow";

    /**
     * 一体机设置温度
     */
    String SteameOvenSetTemp = "SteameOvenSetTemp";
    String SteameOvenSetTemp_Length = "SteameOvenSetTemp_Length";
    String SteameOvenSetTemp_Value = "SteameOvenSetTemp_Value";
    String SteameOvenSetTemp_Key = "SteameOvenSetTemp_Key";

    /**
     * 一体机设置时间
     */
    String SteameOvenSetTime = "SteameOvenSetTime";
    String SteameOvenSetTime_Length = "SteameOvenSetTime_Length";
    String SteameOvenSetTime_Value = "SteameOvenSetTime_Value";
    String SteameOvenSetTime_Key = "SteameOvenSetTime_Key";

    /**
     * 一体机设置PreFlag
     */
    String SteameOvenPreFlag = "SteameOvenPreFlag";

    /**
     * 一体机设置ModelType
     */
    String SteameOvenModelType = "SteameOvenModelType";

    /**
     * 一体机自动菜谱模式
     */
    String SteamOvenAutoRecipeMode = "SteamOvenAutoRecipeMode";
    String SteamOvenAutoRecipeModeLength = "SteamOvenAutoRecipeModeLength";
    String AutoRecipeModeValue = "AutoRecipeModeValue";


    String SteamOvenAutoRecipeModeValue = "SteamOvenAutoRecipeModeValue";

    /**
     * 一体机菜谱标识
     */
    String SteameOvenRecipeFlag = "SteameOvenRecipeFlag";

    /**
     * 一体机设置菜谱ID
     */
    String SteameOvenRecipeId = "SteameOvenRecipeId";
    String SteameOvenRecipeValue = "SteameOvenRecipeValue";
    String SteameOvenRecipeLength = "SteameOvenRecipeLength";

    /**
     * 一体机设置菜谱总步骤数
     */
    String SteameOvenRecipeTotalsteps = "SteameOvenRecipeTotalsteps";
    String SteameOvenRecipeTotalstepsLength = "SteameOvenRecipeTotalstepsLength";
    String SteameOvenRecipeTotalstepsValue = "SteameOvenRecipeTotalstepsValue";

    /**
     * 一体机设置菜谱步骤
     */
    String SteameOvenRecipesteps = "SteameOvenRecipesteps";
    String SteameOvenRecipestepsLength = "SteameOvenRecipeTotalstepsLength";
    String SteameOvenRecipestepsValue = "SteameOvenRecipeTotalstepsValue";

    /**
     * 一体机设置下温度
     */
    String SteameOvenSetDownTemp = "SteameOvenSetDownTemp";
    String SteameOvenSetDownTemp_Lenght = "SteameOvenSetDownTemp_Lenght";
    String SteameOvenSetDownTemp_Vaue = "SteameOvenSetDownTemp_Vaue";

    /**
     * 一体机下温度
     */
    String SteameOvenDownTemp = "SteameOvenDownTemp";
    String SteameOvenDownTemp_Lenght = "SteameOvenDownTemp_Lenght";
    String SteameOvenDownTemp_Vaue = "SteameOvenDownTemp_Vaue";
    /**
     * 一体机蒸汽
     */
    String SteameOvenSteam = "SteameOvenSteam";
    String SteameOvenSteam_Length = "SteameOvenSteamt_Length";
    String SteameOvenSteam_Value = "SteameOvenSteam_Value";

    /**
     * 一体机自动模式
     */
    String SteameOvenCpMode = "SteameOvenPcMode";
    String SteameOvenCpMode_Length = "SteameOvenCpMode_Length";
    String SteameOvenCpMode_Value = "SteameOvenCpMode_Value";

    /**
     * 一体机照明灯
     */
    String SteameOvenLight = "SteameOvenLight";
    String SteameOvenLight_Length = "SteameOvenLight_Length";
    String SteameOvenLight_Value = "SteameOvenLight_Value";

    /**
     * 一体机多段烹饪
     */
    String steameOvenTotalNumberOfSegments_Key = "steameOvenTotalNumberOfSegments_Key";//总段数
    String steameOvenTotalNumberOfSegments_Length = "steameOvenTotalNumberOfSegments_Length";
    String steameOvenTotalNumberOfSegments_Value = "steameOvenTotalNumberOfSegments_Value";

    String SteameOvenSectionOfTheStep_Key = "SteameOvenSectionOfTheStep_Key";//段步骤
    String SteameOvenSectionOfTheStep_Length = "SteameOvenSectionOfTheStep_Length";
    String SteameOvenSectionOfTheStep_Value = "SteameOvenSectionOfTheStep_Value";


    /**
     * 一体机预设标志
     */
    String SteameOvenPreset = "SteameOvenPreset";

    /**
     * 一体机考叉旋转
     */
    String SteameOvenRevolve = "SteameOvenRevolve";
    String SteameOvenRevolve_Length = "SteameOvenRevolve_Length";
    String SteameOvenRevolve_Value = "SteameOvenRevolve_Value";

    /**
     * 水箱更改
     */
    String SteameOvenWaterChanges = "SteameOvenWaterChanges";
    String SteameOvenWaterChanges_Length = "SteameOvenWaterChanges_Length";
    String SteameOvenWaterChanges_Value = "SteameOvenWaterChanges_Value";

    /**
     * 一体机工作完成参数
     */
    String SteameOvenWorkComplete = "SteameOvenWorkComplete";
    String SteameOvenWorkComplete_Length = "SteameOvenWorkComplete_Length";
    String SteameOvenWorkComplete_Value = "SteameOvenWorkComplete_Value";

    /**
     * 一体机开关事件参数
     */
    String setSteameOvenSwitchControl = "setSteameOvenSwitchControl";
    String setSteameOvenSwitchControl_Length = "setSteameOvenSwitchControl_Length";
    String setSteameOvenSwitchControl_Value = "setSteameOvenSwitchControl_Value";


    //一体机状态查询回应参数
    String SteameOvenAlarm = "SteameOvenAlarm";//故障
    String SteameOvenLeftTime = "SteameOvenLeftTime";
    String SteameOvenLeftMin = "SteameOvenLeftMin";
    String SteameOvenLeftHours = "SteameOvenLeftHours";
    String SteameOvenWaterStatus = "SteameOvenWaterStatus";
    String SteameOvenCpStep = "SteameOvenCpStep";//自动模式介
    String SteameOvenCpStep_Lenght = "SteameOvenCpStep_Lenght";
    String SteameOvenCpStep_Value = "SteameOvenCpStep_Value";

    //一体机工作事件上报
    String setSteameOvenBasicMode_Key = "setSteameOvenBasicMode_Key";//设置基本模式
    String setSteameOvenBasicMode_Length = "setSteameOvenBasicMode_Length";
    String setSteameOvenBasicMode_value = "setSteameOvenBasicMode_value";

    String DeviceId = "Guid";


    //自动模式阶
    String CpStepKey = "CpStepKey";
    String CpStepLength = "CpStepLength";
    String CpStepValue = "CpStepValue";

    //蒸汽Steam
    String SteamKey = "SteamKey";
    String SteamLength = "SteamLength";
    String SteamValue = "SteamValue";


    //多段烹饪步骤
    String MultiStepCookingStepsKey = "MultiStepCookingStepsKey";
    String MultiStepCookingStepsLength = "MultiStepCookingStepsLength";
    String MultiStepCookingStepsValue = "MultiStepCookingStepsValue";

    //多段当前步骤
    String MultiStepCurrentStepsKey = "MultiStepCurrentStepsKey";
    String MultiStepCurrentStepsLength = "MultiStepCurrentStepsLength";
    String MultiStepCurrentStepsValue = "MultiStepCurrentStepsValue";

    String SteameOvenPreFlagKey = "SteameOvenPreFlagKey";
    String SteameOvenPreFlagLength = "SteameOvenPreFlagLength";
    String SteameOvenPreFlagValue = "SteameOvenPreFlagValue";

    String weatherDescalingKey = "weatherDescalingKey";
    String weatherDescalingLength = "weatherDescalingLength";
    String weatherDescalingValue = "weatherDescalingValue";

    String doorStatusKey = "doorStatusKey";
    String doorStatusLength = "doorStatusLength";
    String doorStatusValue = "doorStatusValue";

    String time_H_key = "time_H_key";
    String time_H_length = "time_H_length";
    String time_H_Value = "time_H_Value";


    //--------------------------------------RIKA----start------------------------------------------

    //带品类个数
    String numberOfCategory = "numberOfCategory";
    //品类编码
    String categoryCode = "categoryCode";
    //
    String fanHeader = "fanHeader";
    String fanHeader_Key = "header_Key";
    String fanHeader_Leng = "header_Leng";


    String rikaFanWorkStatusHeader = "rikaFanWorkStatusHeader";
    String rikaFanWorkStatusHeader_Key = "rikaFanWorkStatusHeader_Key";
    String rikaFanWorkStatusHeader_Leng = "rikaFanWorkStatusHeader_Leng";


    //烟机工作状态
    String rikaFanWorkStatus = "rikaFanWorkStatus";


    String rikaFanPowerHeader = "rikaFanPowerHeader";
    String rikaFanPowerHeader_Key = "rikaFanPowerHeader_Key";
    String rikaFanPowerHeader_Leng = "rikaFanPowerHeader_Leng";

    //烟机档位
    String rikaFanPower = "rikaFanPower";

    String rikaFanLightHeader = "rikaFanLightHeader";
    String rikaFanLightHeader_Key = "rikaFanLightHeader_Key";
    String rikaFanLightHeader_Leng = "rikaFanLightHeader_Leng";
    //烟机灯
    String rikaFanLight = "rikaFanLight";

    //烟机清洗提醒
    String rikaFanCleaningRemind = "rikaFanCleaningRemind";
    String rikaFanCleaningRemindHeader = "rikaFanCleaningRemindHeader";
    String rikaFanCleaningRemindHeader_Key = "rikaFanCleaningRemindHeader_Key";
    String rikaFanCleaningRemindHeader_Leng = "rikaFanCleaningRemindHeader_Leng";

    //止回阀状态值
    String theCheckValueHeader_Value = "theCheckValueHeader_Value";
    //定时剩余时间
    String waitTimeValue = "waitTimeValue";
    //风门状态
    String damperHeader = "damperHeader";
    String damperHeader_Key = "damperHeader_Key";
    String damperHeader_Leng = "damperHeader_Leng";
    //左风门
    String damperLeft = "damperLeft";
    //右风门
    String damperRight = "damperRight";
    //清洗时间
    String cleaningUseTime = "cleaningUseTime";

    //灶具头部
    String stoveHeader_Key = "stoveHeader_Key";
    String stoveHeader_Leng = "stoveHeader_Leng";

    //炉头数量
    String headNumber = "headNumber";
    //童锁状态
    String lockStatus = "lockStatus";


    //左灶具炉头工作状态
    String stoveHeadLeftWorkStatus = "stoveHeadLeftWorkStatus";
    //左灶具炉头功率
    String stoveHeadLeftPower = "stoveHeadLeftPower";
    //左定时关机设置的剩余时间
    String stoveHeadLeftRemainingTime = "stoveHeadLeftRemainingTime";
    //左报警状态
    String stoveHeadLeftAlarmStatus = "stoveHeadLeftAlarmStatus";
    //左菜谱ID
    String stoveHeadLeftRecipeId = "stoveHeadLeftRecipeId";
    //左菜谱步骤
    String stoveHeadLeftRecipeStep = "stoveHeadLeftRecipeStep";


    //右灶具炉头工作状态
    String stoveHeadRightWorkStatus = "stoveHeadRightWorkStatus";
    //右灶具炉头功率
    String stoveHeadRightPower = "stoveHeadRightPower";
    //右定时关机设置的剩余时间
    String stoveHeadRightRemainingTime = "stoveHeadRightRemainingTime";
    //右报警状态
    String stoveHeadRightAlarmStatus = "stoveHeadRightAlarmStatus";
    //右菜谱ID
    String stoveHeadRightRecipeId = "stoveHeadRightRecipeId";
    //右菜谱步骤
    String stoveHeadRightRecipeStep = "stoveHeadRightRecipeStep";

    //消毒柜头部
    String sterilHeader_Key = "sterilHeader_Key";
    String sterilHeader_Leng = "sterilHeader_Leng";

    //消毒柜工作状态
    String sterilWorkStatus = "sterilWorkStatus";
    String sterilWorkStatusHeader = "sterilWorkStatusHeader";
    String sterilWorkStatusHeader_Key = "sterilWorkStatusHeader_Key";
    String sterilWorkStatusHeader_Leng = "sterilWorkStatusHeader_Leng";
    String sterilWorkModelTime = "sterilWorkModelTime";
    String sterilOrderTime = "sterilOrderTime";
    String sterilWarmDishTemp = "sterilWarmDishTemp";

    //消毒柜童锁状态
    String sterilLockStatus = "sterilLockStatus";
    String sterilLockStatusHeader = "sterilLockStatusHeader";
    String sterilLockStatusHeader_Key = "sterilLockStatusHeader_Key";
    String sterilLockStatusHeader_Leng = "sterilLockStatusHeader_Leng";
    //消毒柜工作剩余时间
    String sterilWorkTimeLeft = "sterilWorkTimeLeft";
    //消毒柜门锁状态
    String sterilDoorLockStatus = "sterilDoorLockStatus";
    //消毒柜报警状态
    String sterilAlarmStatus = "sterilAlarmStatus";

    //消毒柜清除油渍
    String sterilRemoveOilySoiled = "sterilRemoveOilySoiled";
    //消毒柜温度值

    String sterilHeaderTemp_Value = "sterilHeaderTemp_Value";
    //消毒柜湿度值

    String sterilHeaderHum_Value = "sterilHeaderHum_Value";

    String sterilHeaderOzone_Value = "sterilHeaderOzone_Value";

    //消毒柜细菌值

    String sterilHeaderGerm_Value = "sterilHeaderGerm_Value";

    //消毒柜排风机头部
    String sterilExhaustFanHeader = "sterilExhaustFanHeader";
    String sterilExhaustFan_Key = "sterilExhaustFan_Key";
    String sterilExhaustFan_Leng = "sterilExhaustFan_Leng";

    //消毒柜排风机状态
    String sterilExhaustFanStatus = "sterilExhaustFanStatus";

    //蒸汽炉头部
    String steamHeader = "steamHeader";
    String steamHeader_Key = "steamHeader_Key";
    String steamHeader_Leng = "steamHeader_Leng";

    //蒸汽炉童锁状态
    String steamLockStatus = "steamLockStatus";
    //蒸汽炉工作状态
    String steamWorkStatus = "steamWorkStatus";
    String steamWorkStatusHeader = "steamWorkStatusHeader";
    String steamWorkStatusHeader_Key = "steamWorkStatusHeader_Key";
    String steamWorkStatusHeader_Leng = "steamWorkStatusHeader_Leng";


    //蒸汽炉报警状态
    String steamAlarmStatus = "steamAlarmStatus";
    //蒸汽炉运行模式
    String steamRunModel = "steamRunModel";
    String steamRunModelHeader = "steamRunModelHeader";
    String steamRunModelHeader_Key = "steamRunModelHeader_Key";
    String steamRunModelHeader_Leng = "steamRunModelHeader_Leng";
    //蒸汽炉工作温度
    String steamWorkTemp = "steamWorkTemp";
    //蒸汽炉工作剩余时间
    String steamWorkRemainingTime = "steamWorkRemainingTime";
    //蒸汽炉门锁状态
    String steamDoorState = "steamDoorState";
    //蒸汽炉设置温度
    String steamSetTemp = "steamSetTemp";
    //蒸汽炉设置时间
    String steamSetTime = "steamSetTime";
    //蒸汽炉预约时间
    String steamOrderTime = "steamOrderTime";
    //蒸汽炉照明灯
    String steamLightState = "steamLightState";
    String steamLightStateHeader = "steamLightStateHeader";
    String steamLightStateHeader_Key = "steamLightStateHeader_Key";
    String steamLightStateHeader_Leng = "steamLightStateHeader_Leng";

    //蒸汽炉清除油渍
    String steamRemoveOilySoiled = "steamRemoveOilySoiled";

    //蒸汽炉菜谱
    String steamRecipeHeader = "steamRecipeHeader";
    String steamRecipe_Key = "steamRecipe_Key";
    String steamRecipe_Leng = "steamRecipe_Leng";

    //蒸汽炉菜谱ID
    String steamRecipeId = "steamRecipeId";

    //蒸汽炉菜谱步鄹
    String steamRecipeStep = "steamRecipeStep";

    //蒸汽炉水箱状态头部
    String steamWaterHeader = "steamWaterHeader";
    String steamWater_Key = "steamWater_Key";
    String steamWater_Leng = "steamWater_Leng";

    //蒸汽炉水箱状态
    String steamWaterSwitchStatus = "steamWaterSwitchStatus";

    //蒸汽炉废水位头部
    String steamWasteWaterHeader = "steamWasteWaterHeader";
    String steamWasteWater_Key = "steamWasteWater_Key";
    String steamWasteWater_Leng = "steamWasteWater_Leng";

    //蒸汽炉废水位超标
    String steamWasteWaterExcessive = "steamWasteWaterExcessive";
    //抽废水提醒
    String steamWasteWaterRemind = "steamWasteWaterRemind";
    //抽废水状态
    String steamWasteWaterStatus = "steamWasteWaterStatus";


    String steamExhaustFanHeader = "steamExhaustFanHeader";
    String steamExhaustFan_Key = "steamExhaustFan_Key";
    String steamExhaustFan_Leng = "steamExhaustFan_Leng";
    //蒸汽炉排风机状态
    String steamExhaustFanStatus = "steamExhaustFanStatus";

    //烟机报警事件
    String fanAlarmHeader = "fanAlarmHeader";
    String fanAlarmHeader_Key = "fanAlarmHeader_Key";
    String fanAlarmHeader_Leng = "fanAlarmHeader_Leng";
    String fanAlarmCode = "fanAlarmCode";

    //灶具报警事件
    String stoveAlarmHeader = "stoveAlarmHeader";
    String stoveAlarmHeader_Key = "stoveAlarmHeader_Key";
    String stoveAlarmHeader_Leng = "stoveAlarmHeader_Leng";
    String stoveAlarmCode = "stoveAlarmCode";

    //消毒柜报警事件
    String sterilAlarmHeader = "sterilAlarmHeader";
    String sterilAlarmHeader_Key = "sterilAlarmHeader_Key";
    String sterilAlarmHeader_Leng = "sterilAlarmHeader_Leng";
    String sterilAlarmCode = "sterilAlarmCode";


    //蒸汽炉报警事件
    String steamAlarmHeader = "steamAlarmHeader";
    String steamAlarmHeader_Key = "steamAlarmHeader_Key";
    String steamAlarmHeader_Leng = "steamAlarmHeader_Leng";
    String steamAlarmCode = "steamAlarmCode";

    //一体机报警事件
    String steamOvenAlarmHeader = "steamAlarmHeader";
    String steamOvenAlarmHeader_Key = "steamAlarmHeader_Key";
    String steamOvenAlarmHeader_Leng = "steamAlarmHeader_Leng";
    String steamOvenAlarmCode = "steamAlarmCode";

    //烟机常用智能互动
    String fanIntelligentInteractiveHeader = "fanIntelligentInteractiveHeader";
    String fanIntelligentInteractiveHeader_Key = "fanIntelligentInteractiveHeader_Key";
    String fanIntelligentInteractiveHeader_Leng = "fanIntelligentInteractiveHeader_Leng";

    //烟机灶具开关联动
    String fanAndStoveSwitchLinkage = "fanAndStoveSwitchLinkage";

    //烟机档位开关联动
    String fanPowerSwitchLinkage = "fanPowerSwitchLinkage";

    //灶具关机后烟机延时关机开关
    String fanTimeDelayShutdownSwitch = "fanTimeDelayShutdownSwitch";

    //灶具关机后烟机延时关机时间
    String fanDelaySwitchTime = "fanDelaySwitchTime";

    //一体机开始工作,烟机联动工作开关
    String fanSteamOvenLinkage = "fanSteamOvenLinkage";

    //一体机结束工作,烟机延迟关闭开关
    String fanSteamOvenDelayShutdownSwitch = "fanSteamOvenDelayShutdownSwitch";

    //油烟机清洗提示开关
    String fanCleaningPromptSwitch = "fanCleaningPromptSwitch";

    //是否开启定时通风
    String fanOpenRegularVentilation = "fanOpenRegularVentilation";

    //定时通风间隔时间
    String fanRegularVentilationIntervalTime = "RegularVentilationIntervalTime";

    //是否开启每周通风
    String fanOpenWeeklyVentilation = "fanOpenWeeklyVentilation";

    //每周通风的时间
    String fanTheWeeklyVentilationTime = "fanTheWeeklyVentilationTime";


    //爆炒时间开关
    String fanTimeForAwhileSwitch = "fanTimeForAwhileSwitch";
    //爆炒时间
    String fanTimeForAwhile = "fanTimeForAwhile";

    //消毒柜常用智能互动
    String steriIntelligentInteractiveHeader = "steriIntelligentInteractiveHeader";
    String steriIntelligentInteractiveHeader_Key = "steriIntelligentInteractiveHeader_Key";
    String steriIntelligentInteractiveHeader_Leng = "steriIntelligentInteractiveHeader_Leng";

    //定时消毒开关
    String steriTimeDisinfectionSwitch = "steriTimeDisinfectionSwitch";
    //定时消毒间隔时间
    String steriTimeDisinfectionTimeInterval = "steriTimeDisinfectionTimeInterval";

    //每周消毒开关
    String steriWeeklyDisinfectionSwitch = "steriWeeklyDisinfectionSwitch";

    //每周消毒的时间
    String steriWeeklyDisinfectionTime = "steriWeeklyDisinfectionTime";

    //消毒柜开启电时间
    String steriOpenElectricityTime = "steriOpenElectricityTime";


    //烟机事件编码
    String fanEventCode = "fanEventCode";
    //烟机事件参数
    String fanEventArg = "fanEventArg";

    //灶具事件编码
    String stoveEventCode = "stoveEventCode";
    //灶具事件参数
    String stoveEventArg = "stoveEventArg";


    //消毒柜事件编码
    String sterilEventCode = "sterilEventCode";
    //消毒柜事件参数
    String sterilEventArg = "sterilEventArg";

    //蒸汽炉事件编码
    String steamEventCode = "steamEventCode";
    //蒸汽炉事件参数
    String steamEventArg = "steamEventArg";

    //蒸烤一体机事件编码
    String steamOvenEventCode = "steamOvenEventCode";
    //蒸烤一体机事件参数
    String steamOvenEventArg = "steamOvenEventArg";

    //蒸烤一体机头部
    String steamOvenHeader = "steamOvenHeader";
    String steamOvenHeader_Key = "steamOvenHeader_Key";
    String steamOvenHeader_Leng = "steamOvenHeader_Leng";

    String steamOvenHeader1 = "steamOvenHeader1";
    String steamOvenHeader_Key1 = "steamOvenHeader_Key1";
    String steamOvenHeader_Leng1 = "steamOvenHeader_Leng1";


    String steamOvenModelOne = "steamOvenModelOne";
    String steamOvenTempOne = "steamOvenTempOne";
    String steamOvenTimeOne = "steamOvenTimeOne";
    String steamOvenModelTwo = "steamOvenModelTwo";
    String steamOvenTempTwo = "steamOvenTempTwo";
    String steamOvenTimeTwo = "steamOvenTimeTwo";
    String steamOvenModel = "steamOvenModel";
    String stemOvenTime = "stemOvenTime";


    //童锁状态
    String steamOvenLockStatus = "steamOvenLockStatus";

    //一体机工作状态（Status）
    String steamOvenWorkStatus = "steamOvenWorkStatus";
    //一体机报警状态
    String steamOvenAlarmStatus = "steamOvenAlarmStatus";
    //一体机运行模式
    String steamOvenRunModel = "steamOvenRunModel";
    //一体机工作温度
    String steamOvenWorkTemp = "steamOvenWorkTemp";
    //一体机剩余工作时间
    String steamOvenTimeWorkRemaining = "steamOvenTimeWorkRemaining";

    //一体机门状态 0x00: 开门状态，0x01：关门状态
    String steamOvenDoorState = "steamOvenDoorState";
    //一体机设置温度（SET_TEMP）
    String steamOvenSetTemp = "steamOvenSetTemp";
    //一体机预设标志位
    String steamOvenPreFlag = "steamOvenPreFlag";
    //一体机设置时间（SET_TTIME）
    String steamOvenSetTime = "steamOvenSetTime";
    //预约时间（ORDER_TIME）
    String steamOvenOrderTime = "steamOvenOrderTime";
    //照明灯（ LIGHT）0为关，1为开
    String steamOvenLight = "steamOvenLight";
    //总步骤数(多段、除垢有效)
    String steamOvenTotalNumber = "steamOvenTotalNumber";
    //当前步骤数(多段、除垢有效)
    String steamOvenCurrentNumber = "steamOvenCurrentNumber";
    //自动菜谱
    String steamOvenAutomaticRecipe = "steamOvenAutomaticRecipe";
    //是否需要清油
    String steamOvenCleanOil = "steamOvenCleanOil";

    //菜谱信息头部
    String steamOvenRecipeHeader = "steamOvenRecipeHeader";
    //菜谱信息Key
    String steamOvenRecipeKey = "steamOvenRecipeKey";
    //菜谱信息Leng
    String steamOvenRecipeLeng = "steamOvenRecipeLeng";
    //菜谱ID
    String steamOvenRecipeId = "steamOvenRecipeId";
    //菜谱步骤
    String steamOvenRecipeStep = "steamOvenRecipeStep";

    //水箱开关状态 1为关,0为开
    String steamOvenWaterStatus = "steamOvenWaterStatus";
    //废水状态
    String steamOvenAbolishWaterHeader = "steamOvenAbolishWaterHeader";
    String steamOvenAbolishWaterKey = "steamOvenAbolishWaterKey";
    String steamOvenAbolishWaterLeng = "steamOvenAbolishWaterLeng";
    //废水水位超标
    String steamOvenAbolishWaterOverproof = "steamOvenAbolishWaterOverproof";
    //抽废水提醒
    String steamOvenAbolishWaterRemind = "steamOvenAbolishWaterRemind";
    //抽废水状态
    String steamOvenAbolishWaterTake = "steamOvenAbolishWaterTake";
    //抽废水开关
    String steamOvenAbolishWaterSwitchStatus = "steamOvenAbolishWaterSwitchStatus";
    //排风状态
    String steamOvenExhaustHeader = "steamOvenExhaustHeader";
    String steamOvenExhaustKey = "steamOvenExhaustKey";
    String steamOvenExhaustLeng = "steamOvenExhaustLeng";
    //
    String steamOvenExhaustStatus = "steamOvenExhaustStatus";
    //烟机联动工作开关
    String steamOvenFanLinkWorkSwitch = "steamOvenFanLinkWorkSwitch";
    //烟机延迟关闭开关
    String steamOvenFanDelayedCloseSwitch = "steamOvenFanDelayedCloseSwitch";

    //一体机多段烹饪头
    String SteamOvenMultiStepHeader = "SteamOvenMultiStepHeader";
    String steamOvenMultiStepKey = "steamOvenMultiStepKey";
    String SteamOvenMultiStepLenght = "SteamOvenMultiStepLenght";

    //一体机自动烹饪菜谱模式
    String SteamOvenCookingModelKey = "SteamOvenCookingModelKey";
    String SteamOvenCookingModelLenght = "SteamOvenCookingModelLenght";
    //自动菜谱模式
    String steamOvenCookingModel = "steamOvenCookingModel";
    //自动菜谱模式时间
    String steamOvenCookingTime = "steamOvenCookingTime";

    //------------------------------------------RIKA-----stop-------------------------

    //------------------------------------------火鸡电磁灶-----start-------------------------

    //开关状态
    String cookerSwitchStatus = "cookerSwitchStatus";

    //模式
    String cookerModel = "cookerModel";

    //火力
    String cookerFire = "cookerFire";

    //设置温度
    String setCookerTemp = "setCookerTemp";

    //温度
    String cookerTemp = "cookerTemp";

    //定时开关
    String cookerTimingSwitch = "cookerTimingSwitch";

    //定时时间
    String cookerTimingTime = "cookerTimingTime";

    //加热时长
    String cookerHeatingTime = "cookerHeatingTime";

    //当前动作
    String cookerCurrentAction = "cookerCurrentAction";

    //菜谱执行状态
    String cookerRecipePerformStatus = "cookerRecipePerformStatus";

    //当前菜谱编号
    String cookerRecipeCode = "cookerRecipeCode";

    //当前菜谱步鄹编号
    String cookerRecipeStepCode = "cookerRecipeStepCode";

    //菜谱当前步鄹的目标温度
    String cookerRecipeStepTargetTemp = "cookerRecipeStepTargetTemp";

    //当前菜谱步鄹剩余时间
    String cookerRecipeStepRemainTime = "cookerRecipeStepRemainTime";

    //菜谱烹饪总时长
    String cookerRecipeCookingTotalTime = "cookerRecipeCookingTotalTime";

    //报警故障代码
    String cookerAlarmCode = "cookerAlarmCode";

    //Wifi固件版本号
    String wifiVersion = "wifiVersion";

    //语音播报声音模式
    String cookerVoiceMode = "cookerVoiceMode";

    //语音播报音量等级
    String cookerVoiceLevel = "cookerVoiceLevel";

    //电磁炉简易智能默认温度--动作
    String cookerAction = "cookerAction";

    //默认温度
    String tempTureAll = "tempTureAll";

    //事件
    String paramEvent = "paramEvent";

    //升级状态
    String statusCode = "statusCode";
    //
    String paramEventId = "paramEventId";

    //干烧预警烟锅联动开关
    String potBurningWarnSwitch = "potBurningWarnSwitch";
    //锅状态
    String Pot_status = "Pot_status";
    String TemperatureReportOne = "TemperatureReportOne";

    String TemperatureReportTwo = "TemperatureReportTwo";

    String BraiseAlarm = "BraiseAlarm";

    //定时通风剩余时间
    String RegularVentilationRemainingTime = "RegularVentilationRemainingTime";
    //烟灶联动通风剩余时间
    String FanStoveLinkageVentilationRemainingTime = "FanStoveLinkageVentilationRemainingTime";
    //定时提醒剩余时间
    String PeriodicallyRemindTheRemainingTime = "PeriodicallyRemindTheRemainingTime";

    String OverTempProtectStatus = "OverTempProtectStatus";

    //关机（按关机键）剩余时间
    String PresTurnOffRemainingTime = "PresTurnOffRemainingTime";

    //162KEY
    //烟机电磁灶开关联动
    String FanStovePower = "FanStovePower";
    //烟机档位联动开关
    String FanPowerLink = "FanPowerLink";
    //电磁灶关机后烟机延时关机开关
    String StoveShutDelay = "StoveShutDelay";
    //电磁灶关机后烟机延时关机时间
    String StoveShutDelayTime = "StoveShutDelayTime";
    //油烟机清洗提示开关
    String FanCleanPower = "FanCleanPower";
    //定时通风
    String TimeAirPower = "TimeAirPower";
    String TimeAirPowerDay = "TimeAirPowerDay";
    //通风的时间
    String AirTimePower = "AirTimePower";
    String AirTimeWeek = "AirTimeWeek";
    String AirTimeHour = "AirTimeHour";
    String AirTimeMinute = "AirTimeMinute";
    //爆炒时间
    String FryStrongTimePower = "FryStrongTimePower";
    String FryStrongTime = "FryStrongTime";
    //倒油杯提示开关
    String CupOilPower = "CupOilPower";
    //定时提醒设置开关
    String TimeReminderSetSwitch = "TimeReminderSetSwitch";
    //定时提醒设置时间
    String TimeReminderSetTime = "TimeReminderSetTime";
    //智能烟感开关
    String FanFeelPower = "FanFeelPower";
    //防干烧提示开关
    String ProtectTipDryPower = "ProtectTipDryPower";
    //防干烧开关
    String ProtectDryPower = "ProtectDryPower";


    String FanStovePowerKey = "FanStovePowerKey";
    String FanStovePowerLength = "FanStovePowerLength";

    String FanPowerLinkKey = "FanPowerLinkKey";
    String FanPowerLinkLength = "FanPowerLinkLength";

    String StoveShutDelayKey = "StoveShutDelayKey";
    String StoveShutDelayLength = "StoveShutDelayLength";

    String StoveShutDelayTimeKey = "StoveShutDelayTimeKey";
    String StoveShutDelayTimeLength = "StoveShutDelayTimeLength";


    String TimeAirPowerKey = "TimeAirPowerKey";
    String TimeAirPowerLength = "TimeAirPowerLength";

    String AirTimePowerKey = "AirTimePowerKey";
    String AirTimePowerLength = "AirTimePowerLength";

    String FanFeelPowerKey = "FanFeelPowerKey";
    String FanFeelPowerLength = "FanFeelPowerLength";

    String FanCleanPowerKey = "FanCleanPowerKey";
    String FanCleanPowerLength = "FanCleanPowerLength";

    String FryStrongTimePowerKey = "FryStrongTimePowerKey";
    String FryStrongTimePowerLength = "FryStrongTimePowerLength";

    String CupOilPowerKey = "CupOilPowerKey";
    String CupOilPowerLength = "CupOilPowerLength";

    String ProtectTipDryPowerKey = "ProtectTipDryPowerKey";
    String ProtectTipDryPowerLength = "ProtectTipDryPowerLength";

    String ProtectDryPowerKey = "ProtectDryPowerKey";
    String ProtectDryPowerLength = "ProtectDryPowerLength";

    String GestureControlPowerKey = "GestureControlPowerKey";
    String GestureControlPowerLength = "GestureControlPowerLength";

    //----------------------洗碗机------------------------

    String PowerMode = "PowerMode";//电源模式
    String DishWasherWorkMode = "DishWasherWorkMode";//洗碗机工作模式
    String LowerLayerWasher = "LowerLayerWasher";//下层洗开关
    String AutoVentilation = "AutoVentilation";//自动换气
    String EnhancedDrySwitch = "EnhancedDrySwitch";//加强干燥开关
    String AppointmentSwitch = "AppointmentSwitch";//预约开关
    String AppointmentTime = "AppointmentTime";//预约时间

    String SaltFlushKey = "SaltFlushKey";//冲盐挡位
    String SaltFlushLength = "SaltFlushLength";//冲盐挡位
    String SaltFlushValue = "SaltFlushValue";//冲盐挡位

    String RinseAgentPositionKey = "RinseAgentPositionKey";//漂洗剂档位
    String RinseAgentPositionLength = "RinseAgentPositionLength";//漂洗剂档位
    String RinseAgentPositionValue = "RinseAgentPositionValue";//漂洗剂档位

    String powerStatus = "powerStatus";//电源状态
    String DishWasherRemainingWorkingTime = "DishWasherRemainingWorkingTime";//剩余工作时间
    String EnhancedDryStatus = "EnhancedDryStatus";//将强干燥状态
    String AppointmentSwitchStatus = "AppointmentSwitchStatus";//预约开关状态

    String AppointmentRemainingTime = "AppointmentRemainingTime";//预约剩余时间

    String DishWasherFanSwitch = "DishWasherFanSwitch";//风机开关
    String DoorOpenState = "DoorOpenState";//开门状态
    String LackRinseStatus = "LackRinseStatus";//缺漂洗剂状态
    String LackSaltStatus = "LackSaltStatus";//缺盐状态
    String AbnormalAlarmStatus = "AbnormalAlarmStatus";//异常报警状态
    String CurrentWaterTemperatureKey = "CurrentWaterTemperatureKey";//当前水温
    String CurrentWaterTemperatureLength = "CurrentWaterTemperatureLength";//当前水温
    String CurrentWaterTemperatureValue = "CurrentWaterTemperatureValue";//当前水温

    String SetWorkTimeKey = "SetWorkTimeKey";//设置工作时间
    String SetWorkTimelength = "SetWorkTimelength";//设置工作时间
    String SetWorkTimeValue = "SetWorkTimeValue";//设置工作时间


    String DishWasherAlarm = "DishWasherAlarm";//洗碗机报警

    //洗碗机事件编码
    String DishWasherEventCode = "DishWasherEventCode";
    //洗碗机事件参数
    String DishWasherEventArg = "DishWasherEventArg";

    //--------------------藏宝盒----------------------

    //音量大小调节
    String VolumeControlKey = "VolumeControlKey";
    String VolumeControlLen = "VolumeControlLen";
    String VolumeControlValue = "VolumeControlValue";

    //藏宝盒信号强度
    String RSSI = "RSSI";
    //藏宝盒音量
    String Volume = "Volume";
    //藏宝盒SSID
    String SSID = "SSID";


    String newVersion_len = "newVersion_len";
    String newVersion_val = "newVersion_val";

    String the_upgrade_len = "the_upgrade_len";
    String the_upgrade_val = "the_upgrade_val";

    String startupgrade_len = "startupgrade_len";
    String startupgrade_val = "startupgrade_val";

   /* --------------------------------------------------------集成灶新增-------------------------------------------------------------------*/
    /**
     * 炉头类型 0 ：燃气灶 1 ：电磁灶
     */
    String headType = "headType" ;

    /**
     * 电源控制
     */
    String powerCtrlKey = "powerCtrlKey" ;
    String powerCtrlLength = "powerCtrlLength" ;
    String powerCtrlKeyValue = "powerCtrlValue" ;

    /**
     * 电源状态
     */
    String powerState = "powerState" ;
}