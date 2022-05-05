package com.robam.common.io.device;


public interface MsgKeys {

    // -------------------------------------------------------------------------------
    // 通知类

    //----------------------------------------------------------燃气传感器
    short GasSensor_Status_Check_Req = 128;//燃气传感器状态查询

    short GasSensor_Status_Check_Rep = 129;//燃气传感器状态查询

    short GasSensor_Status_Noti = 130;//燃气传感器状态上报

    short GasSensor_SetCheckSelf_Req = 131;//设置自检请求

    short GasSensor_SetCheckSelf_Rep = 132;//设置自检回复

    short GasSensor_Alarm_Noti = 133;//燃气传感器事件上报

    // -------------------------------------------------------------------------------

    /**
     * 电磁灶报警上报
     */
    short StoveAlarm_Noti = 138;

    /**
     * 电磁灶事件上报
     */
    short StoveEvent_Noti = 139;

    /**
     * 灶具温度事件上报     by zhaiyuanyi 20151029
     */

    short StoveTemp_Noti = 140;

    /**
     * 油烟机事件上报
     */
    short FanEvent_Noti = 148;
    /**
     * 消毒柜报警上报      by zhaiyuanyi 20151120
     */
    short SteriAlarm_Noti = 146;
    /**
     * 消毒柜事件上报      by zhaiyuanyi 20151218
     */
    short SteriEvent_Noti = 152;

    // -------------------------------------------------------------------------------
    // 应答类
    // -------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------电磁灶
    /**
     * 获取电磁灶状态（请求）
     */
    short GetStoveStatus_Req = 128;

    /**
     * 获取电磁灶状态（应答）
     */
    short GetStoveStatus_Rep = 129;

    /**
     * 设置电磁灶状态（请求）
     */
    short SetStoveStatus_Req = 130;

    /**
     * 设置电磁灶状态（应答）
     */
    short SetStoveStatus_Rep = 131;
    /**
     * 设置电磁灶档位（请求）
     */
    short SetStoveLevel_Req = 132;

    /**
     * 设置电磁灶档位（应答）
     */
    short SetStoveLevel_Rep = 133;
    /**
     * 设置电磁灶定时关机（请求）
     */
    short SetStoveShutdown_Req = 134;

    /**
     * 设置电磁灶定时关机（应答）
     */
    short SetStoveShutdown_Rep = 135;

    /**
     * 设置电磁灶童锁（请求）
     */
    short SetStoveLock_Req = 136;

    /**
     * 设置电磁灶童锁（应答）
     */
    short SetStoveLock_Rep = 137;

    /**
     * 9B30C安全设定新增指令
     */

    short SetPowerOn_Req = 162;

    /**
     * 自动关火开关设定回复
     */

    short SetPowerOn_Rep = 163;

    /**
     * 自动关火时间设定
     */

    short setTimePowerOff_Req = 164;
    /**
     * 自动关火时间设定回复
     */

    short setTimePowerOff_Rep = 165;

    /**
     * 自动关火设定查询
     */

    short setPowerOff_Look_Req = 166;

    /**
     * 自动关火设定查询回复
     */

    short setPowerOff_Look_Rep = 167;

    // -------------------------------------------------------------------------------油烟机
    short getGasSensor = 128;

    //-----------------------------------------------------------燃气传感器
    /**
     * 读取智能互动模式设定（请求）
     */
    short GetSmartConfig_Req = 128;

    /**
     * 读取智能互动模式设定（应答）
     */
    short GetSmartConfig_Rep = 129;

    /**
     * 读取油烟机状态（请求）
     */
    short GetFanStatus_Req = 130;

    /**
     * 读取油烟机状态（应答）
     */
    short GetFanStatus_Rep = 131;

    /**
     * 设置油烟机工作状态（请求）
     */
    short SetFanStatus_Req = 132;

    /**
     * 设置油烟机工作状态（应答）
     */
    short SetFanStatus_Rep = 133;

    /**
     * 设置油烟机档位（请求）
     */
    short SetFanLevel_Req = 134;

    /**
     * 设置油烟机档位（应答）
     */
    short SetFanLevel_Rep = 135;

    /**
     * 设置油烟机灯（请求）
     */
    short SetFanLight_Req = 136;

    /**
     * 设置油烟机灯（应答）
     */
    short SetFanLight_Rep = 137;

    /**
     * 设置油烟机整体状态（请求）
     */
    short SetFanAllParams_Req = 138;

    /**
     * 设置油烟机整体状态（应答）
     */
    short SetFanAllParams_Rep = 139;

    /**
     * 重置烟机清洗计时（请求）
     */
    short RestFanCleanTime_Req = 140;

    /**
     * 重置烟机清洗计时（应答）
     */
    short RestFanCleanTime_Rep = 141;

    /**
     * 重启油烟机网络板（请求）
     */
    short RestFanNetBoard_Req = 142;

    /**
     * 重启油烟机网络板（应答）
     */
    short RestFanNetBoard_Rep = 143;

    /**
     * 设置油烟机定时工作（请求）
     */
    short SetFanTimeWork_Req = 144;

    /**
     * 设置油烟机定时工作（应答）
     */
    short SetFanTimeWork_Rep = 145;

    /**
     * 设置智能互动模式（请求）
     */
    short SetSmartConfig_Req = 146;

    /**
     * 设置智能互动模式（应答）
     */
    short SetSmartConfig_Rep = 147;

    /**
     * 设置油烟机重置油杯定时工作（请求）
     */
    short SetFanCleanOirCupTime_Req = 166;

    /**
     * 设置油烟机重置油杯定时工作（应答）
     */
    short SetFanCleanOirCupTime_Rep = 167;

    // ------------------------------------------------------------------------------消毒柜  by zhaiyuanyi 20151120
    /**
     * 设置消毒柜开关（请求）
     */
    short SetSteriPowerOnOff_Req = 128;
    /**
     * 设置消毒柜开关（应答）
     */
    short SetSteriPowerOnOff_Rep = 129;
    /**
     * 设置消毒柜预约时间（请求）
     */
    short SetSteriReserveTime_Req = 130;
    /**
     * 设置消毒柜预约时间（应答）
     */
    short SetSteriReserveTime_Rep = 131;
    /**
     * 设置消毒柜烘干（请求）
     */
    short SetSteriDrying_Req = 132;
    /**
     * 设置消毒柜烘干（应答）
     */
    short SetSteriDrying_Rep = 133;
    /**
     * 设置消毒柜保洁（请求）
     */
    short SetSteriClean_Req = 134;
    /**
     * 设置消毒柜保洁（应答）
     */
    short SetSteriClean_Rep = 135;
    /**
     * 设置消毒柜消毒（请求）
     */
    short SetSteriDisinfect_Req = 136;
    /**
     * 设置消毒柜消毒（应答）
     */
    short SetSteriDisinfect_Rep = 137;
    /**
     * 设置消毒柜童锁（请求）
     */
    short SetSteriLock_Req = 153;
    /**
     * 设置消毒柜童锁（应答）
     */
    short SetSteriLock_Rep = 154;
    /**
     * 查询消毒柜温度／湿度／细菌数（请求）
     */
    short GetSteriParam_Req = 142;
    /**
     * 查询消毒柜温度／湿度／细菌数（应答）
     */
    short GetSteriParam_Rep = 143;
    /**
     * 消毒柜状态查询（请求）
     */
    short GetSteriStatus_Req = 144;
    /**
     * 消毒柜状态查询（应答）
     */
    short GetSteriStatus_Rep = 145;
    /**
     * 读取消毒柜峰谷定时设置(请求)
     */
    short GetSteriPVConfig_Req = 147;
    /**
     * 读取消毒柜峰谷定时设置(应答)
     */
    short GetSteriPVConfig_Rep = 148;
    /**
     * 设置消毒柜峰谷定时开启（请求）
     */
    short SetSteriPVConfig_Req = 149;
    /**
     * 设置消毒柜峰谷定时开启（应答）
     */
    short SetSteriPVConfig_Rep = 150;

    // ------------------------------------------------------------------------------蒸汽炉  by Rosicky 20151214

    /**
     * 设置蒸汽炉工作时间
     */
    short setSteamTime_Req = 129;
    /**
     * 设置蒸汽炉工作时间（应答）
     */
    short setSteamTime_Rep = 130;
    /**
     * 设置蒸汽炉工作温度
     */
    short setSteamTemp_Req = 131;
    /**
     * 蒸汽炉工作温度（应答）
     */
    short setSteamTemp_Rep = 132;
    /**
     * 设置蒸汽炉工作烹饪模式
     */
    short setSteamMode_Req = 133;
    /**
     * 设置蒸汽炉烹饪模式（应答）
     */
    short setSteamMode_Rep = 134;
    /**
     * 蒸汽炉专业模式设置
     */
    short setSteamProMode_Req = 141;
    /**
     * 蒸汽炉专业模式设置（应答）
     */
    short setSteamProMode_Rep = 142;
    /**
     * 蒸汽炉状态查询（请求）
     */
    short GetSteamOvenStatus_Req = 143;
    /**
     * 蒸汽炉状态查询（应答）
     */
    short GetSteamOvenStatus_Rep = 144;
    /**
     * 设置蒸汽炉状态
     */
    short setSteamStatus_Req = 145;
    /**
     * 设置蒸汽炉状态（应答）
     */
    short setSteamStatus_Rep = 146;
    /**
     * 蒸汽炉报警事件上报
     */
    short SteamOvenAlarm_Noti = 149;
    /**
     * 蒸汽炉事件上报
     */
    short SteamOven_Noti = 150;

    /**
     * 菜谱设置
     */
    short SetSteamRecipeReq = 158;
    /**
     * 菜谱设置
     */
    short GetSteamRecipeRep = 159;

    /**
     * 蒸汽炉灯设置
     */
    short SetSteamLightReq = 162;

    /**
     * 蒸汽炉灯回应
     */
    short GetSteamLightRep = 163;

    /**
     * 设置水箱弹出
     */
    short SetSteamWaterTankPOPReq = 164;
    /**
     * 水箱弹出回应
     */
    short GetSteamWaterTankPOPRep = 165;

    /**
     * 设置本地菜谱
     */
    short SetLocalRecipeReq = 172;

    /**
     * 设置本地菜谱回应
     */
    short GetLocalRecipeRep = 173;
    // ------------------------------------------------------------------------------微波炉  by Rosicky 20151217

    /**
     * 设置微波炉开关（请求）
     */
    short setMicroWaveStatus_Req = 128;
    /**
     * 设置微波炉开关（应答）
     */
    short setMicroWaveStates_Rep = 129;
    /**
     * 设置去味模式
     */
    short setMicroWaveClean_Req = 132;

    /**
     * 去味模式回复
     */
    short setMicroWaveClean_Rep = 133;
    /**
     * 设置微波炉品类和加热解冻（请求）
     */
    short setMicroWaveKindsAndHeatCold_Req = 134;
    /**
     * 设置微波炉品类和加热解冻（应答）
     */
    short setMicroWaveKindsAndHeatCold_Rep = 135;
    /**
     * 设置微波炉联动料理请求
     */
    short setMicroWaveLinkedCook_Req = 136;
    /**
     * 设置微波炉联动料理请求(应答)
     */
    short setMicroWaveLinkedCook_Rep = 137;
    /**
     * 设置微波炉专业模式加热（请求）
     */
    short setMicroWaveProModeHeat_Req = 140;
    /**
     * 设置微波炉品类和加热解冻（应答）
     */
    short setMicroWaveProModeHeat_Rep = 141;
    /**
     * 设置微波炉照明灯开关（请求）
     */
    short setMicroWaveLight_Req = 142;
    /**
     * 设置微波炉照明灯开关（应答）
     */
    short setMicroWaveLight_Rep = 143;
    /**
     * 查询微波炉状态（请求）
     */
    short getMicroWaveStatus_Req = 144;
    /**
     * 查询微波炉状态（应答）
     */
    short getMicroWaveStatus_Rep = 145;
    /**
     * 查询微波炉报警（应答）
     */
    short getMicroWaveAlarm_Rep = 149;
    /**
     * 微波炉事件上报
     */
    short MicroWave_Noti = 150;

    /**
     * 菜谱设置
     */
    short MicroWave_Recipe_Req = 158;
    /**
     * 菜谱回应
     */
    short MicroWave_Recipe_Rep = 159;


    // ------------------------------------------------------------------------------电烤箱  by Linxiaobin 20151214

    /**
     * 设置状态控制
     */
    short setOvenStatusControl_Req = 128;
    /**
     * 设置状态控制回应（应答）
     */
    short setOvenStatusControl_Rep = 129;
    /**
     * 设置快热
     */
    short setOvenQuickHeat_Req = 130;
    /**
     * 设置快热回应（应答）
     */
    short setOvenQuickHeat_Rep = 131;
    /**
     * 设置风焙烤
     */
    short setOvenAirBaking_Req = 132;
    /**
     * 设置风焙烤回应（应答）
     */
    short setOvenAirBaking_Rep = 133;
    /**
     * 设置焙烤
     */
    short setOvenToast_Req = 134;
    /**
     * 设置焙烤回应（应答）
     */
    short setOvenToast_Rep = 135;
    /**
     * 设置底加热（请求）
     */
    short setOvenBottomHeat_Req = 136;
    /**
     * 设置底加热回应（应答）
     */
    short setOvenBottomHeat_Rep = 137;
    /**
     * 设置解冻
     */
    short setOvenUnfreeze_Req = 138;
    /**
     * p[
     * 设置解冻回应（应答）
     */
    short setOvenUnfreeze_Rep = 139;
    /**
     * 设置风扇烤
     */
    short setOvenAirBarbecue_Req = 140;
    /**
     * 设置风扇烤回应（应答）
     */
    short setOvenAirBarbecue_Rep = 141;
    /**
     * 设置烧烤
     */
    short setOvenBarbecue_Req = 142;
    /**
     * 设置烧烤回应（应答）
     */
    short setOvenBarbecue_Rep = 143;
    /**
     * 设置强烧烤
     */
    short setOvenStrongBarbecue_Req = 144;

    /**
     * 设置Exp模式
     */
    short setOvenExpModel_Req = 9;
    /**
     * /**
     * 设置风扇烤回应（应答）
     */
    short setOvenStrongBarbecue_Rep = 145;
    /**
     * 设置 烤叉旋转，灯光控制
     */
    short setOvenSpitRotateLightControl_Req = 148;
    /**
     * 设置 烤叉旋转，灯光控制回应（应答）
     */
    short setOvenSpitRotateLightControl_Rep = 149;
    /**
     * 烤箱状态查询（请求）
     */
    short getOvenStatus_Req = 150;
    /**
     * 烤箱状态查询应答（应答）
     */
    short getOvenStatus_Rep = 151;
    /**
     * 烤箱报警事件上报
     */
    short OvenAlarm_Noti = 152;
    /**
     * 烤箱工作事件上报
     */
    short Oven_Noti = 153;
    /**
     * 设置烤箱运行模式
     */
    short SetOven_RunMode_Req = 154;
    /**
     * 设置烤箱运行模式回复
     */
    short GetOven_RunMode_Rep = 155;

    /**
     * 菜谱设置
     */
    short SetSteamOven_Recipe_Req = 158;

    /**
     * 菜谱设置回应
     */
    short GetOven_Recipe_Rep = 159;

    /**
     * 设置烤箱自动模式
     */
    short Set_Oven_Auto_Mode_Req = 160;

    /**
     * 设置烤箱自动模式
     */
    short Get_Oven_Auto_Mode_Rep = 161;

    /**
     * 设置烤箱灯
     */
    short Set_Oven_Light_Req = 162;

    /**
     * 烤箱灯回复
     */
    short Get_Oven_Light_Rep = 163;

    /*
    * 设置烤箱多段烹饪
    */

    short Set_Oven_More_Cook = 164;

    /*
    * 设置烤箱多段烹饪回复
    */

    short Get_Oven_More_Cook = 165;
    // ------------------------------------------------------------------------------净水器  by rentao 20151214
    /**
     * 设置净水器工作模式(请求)
     */
    short setWaterPurifiy_Req = 128;
    /**
     * 净水器状态查询（请求）
     */
    short GetWaterPurifiyStatus_Req = 132;
    /**
     * 净水器状态查询（返回）
     */
    short GetWaterPurifiyStatus_Rep = 133;
    /**
     * 净水器报警上报
     */
    short GetWaterPurifiyAlarm_Rep = 134;

    //新增净水器智能设定指令
    /**
     * 净水器智能设定请求
     * 净水器智能设定回应
     */
    short SetWaterPurifiySmart_Req = 136;//请求
    short getWaterPurifiySmart_Rep = 137;//回应

    short getWaterPurifierStatusSmart_Req = 138;//请求
    short getWaterPurifierStatusSmart_Rep = 139;//应答

    /**
     * 净水器滤芯寿命（返回）
     */
    short GetWaterPurifiyFiliter_Rep = 135;

    /**
     * 开始制水(请求)
     */
    short SetWaterPurifiyWorking_Req = 128;
    /**
     * 开始制水(返回)
     */
    short SetWaterPurifiyWorking_Rep = 129;


    /**
     * 获取温控锅温度(请求)
     */
    short GetPotTemp_Req = 128;
    /**
     * 获取温控锅温度(返回)
     */
    short SetPotTemp_Rep = 129;

    /**
     * 144干烧预警及烟锅联动上报
     */
    short SetPotCom_Req = 144;

    /**
     * 干烧预警及烟锅联动上报回复
     */
    short GetPotCom_Rep = 145;

    /**
     * 干烧预警及烟锅联动上报开关设置
     */
    short SetPotSwitch_Req = 146;

    /**
     * 干烧预警及烟锅联动上报开关设置回复
     */
    short GetPotSwitch_Rep = 147;

    /**
     * 温控锅主动上报
     */
    short ActiveTemp_Rep = 142;

    /**
     * 温控锅报警上报
     */
    short PotAlarm_Report = 143;

    /**
     * 温控锅按键键值上报
     */
    short PotKey_Report = 141;

    /**
     * 温控锅添加设备指令
     */
    short FanAddPot_Req = 53;

    /**
     * 温控锅添加设备指令 返回
     */
    short FanAddPot_Rep = 54;

    /**
     * 温控锅删除设备指令
     */
    short FanDelPot_Req = 38;

    /**
     * 温控锅删除设备指令 返回
     */
    short FanDelPot_Rep = 39;

    short FanStatusComposeCheck_Rep = 149;

    //2019年新增指令
    short SetFanStatusCompose_Req = 162;


    short SetFanStatusCompose_Rep = 163;

    //设置定时提醒
    short SetFanTimingRemind_Req = 168;

    short SetFanTimingRemind_Rep = 169;

    /**
     * 获取温控锅温度(请求)
     *//*
    short GetPotTemp_Req = 96;

    */
    /**
     * 获取温控锅温度(返回)
     */
    short SetPotTemp_Req = 97;


    /*-------------------------------烤蒸一体机------------------------*/
    /**
     * 设置状态控制
     */
    short setSteameOvenStatusControl_Req = 128;
    /**
     * 设置状态控制回应（应答）
     */
    short setSteameOvenStatusControl_Rep = 129;

    /**
     * 一体机状态查询
     */
    short getSteameOvenStatus_Req = 150;

    /**
     * 一体机状态查询(应答)
     */
    short getSteameOvenStatus_Rep = 151;

    /**
     * 报警事件上报
     */
    short SteameOvenAlarm_Noti = 152;
    /**
     * 工作事件上报
     */
    short SteameOven_Noti = 153;
    /**
     * 设置一体机基本模式
     */
    short setSteameOvenBasicMode_Req = 154;

    /**
     * 设置一体机基本模式(应答)
     */
    short setSteameOvenBasicMode_Rep = 155;

    /**
     * 菜谱设置
     */
    short setTheRecipe_Req = 158;

    /**
     * 菜谱设置(应答)
     */
    short setTheRecipe_Rep = 159;

    /**
     * 设置一体机自动模式
     */
    short setSteameOvenAutomaticMode_Req = 160;

    /**
     * 设置一体机自动模式(应答)
     */
    short setSteameOvenAutomaticMode_Rep = 161;

    /**
     * 设置一体机照明灯
     */
    short setSteameOvenFloodlight_Req = 162;

    /**
     * 设置一体机照明灯(应答)
     */
    short setSteameOvenFloodlight_Rep = 163;

    /**
     * 设置一体机加蒸汽
     */
    short setSteameOvensteam_Req = 164;
    /**
     * 设置一体机加蒸汽(应答)
     */
    short setSteameOvensteam_Rep = 165;
    /**
     * 设置一体机多段烹饪
     */
    short setSteameOvenMultistageCooking_Req = 166;

    /**
     * 设置一体机多段烹饪(应答)
     */
    short setSteameOvenMultistageCooking_Rep = 167;

    /**
     * 设置一体机水箱
     */
    short setSteameOvenWater_Req = 168;

    /**
     * 设置一体机水箱(应答)
     */
    short setSteameOvenWater_Rep = 169;

    /**
     * 设置一体机自动菜谱模式
     */
    short setSteameOvenAutoRecipeMode_Req = 170;

    /**
     * 设置一体机自动菜谱模式回复
     */
    short setSteameOvenAutoRecipeMode_Rep = 171;

    /**
     * 设置一体机610多段
     */
    short setSteameOvenAutoRecipeMode610_Req = 175;
    short setSteameOvenAutoRecipeMode610_Rep = 176;

    /*-------------------------------RIKA------------------------*/


    /**
     * 读取智能互动模式设定
     */
    short readIntelligentInteractiveModeSetting_Req = 128;
    /**
     * 读取智能互动模式设定(应答)
     */
    short readIntelligentInteractiveModeSetting_Rep = 129;
    /**
     * 读取设备状态
     */
    short readDeviceStatus_Req = 130;

    /**
     * 读取设备状态(应答)
     */
    short readDeviceStatus_Rep = 131;

    /**
     * 报警事件上报
     */
    short alarmEventReport = 132;


    /**
     * 事件上报
     */
    short eventReport = 133;

    /**
     * 设置设备运行状态
     */
    short setDeviceRunStatus_Req = 140;

    /**
     * 设置设备运行状态怕（应答）
     */
    short setDeviceRunStatus_Rep = 141;

    /**
     * 设置设备定时工作
     */
    short setDeviceRegularWork_Req = 142;

    /**
     * 设置设备定时工作（应答）
     */
    short setDeviceRegularWork_Rep = 143;

    /**
     * 设置设备智能互动模式
     */
    short setDeviceIntelligentInteractiveModel_Req = 144;

    /**
     * 设置设备智能互动模式应答）
     */
    short setDeviceIntelligentInteractiveModel_Rep = 145;

    /**
     * 设置一体机多段烹饪
     */
    short setRikaOvenMultiStep_Req = 148;

    /**
     * 设置一体机多段烹饪应答
     */
    short setRikaOvenMultiStep_Rep = 149;

    /**
     * 设置一体机自动菜谱模式
     */
    short setRikaOveAutoRecipe_Req = 150;

    /**
     * 设置一体机自动菜谱模式应答
     */
    short setRikaOveAutoRecipe_Rep = 151;

    /**
     * 设置一体机智能设定
     */
    short setRikaIntelSet_Req = 152;

    /**
     * 设置一体机智能设定应答
     */
    short setRikaIntelSet_Rep = 153;

    /**
     * 读取一体机智能设定
     */
    short getRikaIntelSet_Req = 154;

    /**
     * 读取一体机智能设定应答
     */
    short getRikaIntelSet_Rep = 155;


     /*-------------------------------火鸡电磁炉------------------------*/


    /**
     * 电磁灶状态查询
     */
    short deviceStatusQuery_Req = 128;

    /**
     * 电磁灶状态查询_回复
     */
    short deviceStatusQuery_Rep = 129;


    /**
     * 电磁灶设定信息查询
     */
    short setDeviceInformationQuery_Req = 130;

    /**
     * 电磁灶设定信息查询_回复
     */
    short setDeviceInformationQuery_Rep = 131;


    /**
     * 设置电磁炉工作状态
     */
    short setDeviceWorkStatus_Req = 132;

    /**
     * 设置电磁炉工作状态_回复
     */
    short setDeviceWorkStatus_Rep = 133;

    /**
     * 设置电磁炉温度
     */
    short setDeviceTemp_Req = 134;
    /**
     * 设置电磁炉温度_回复
     */
    short setDeviceTemp_Rep = 135;


    /**
     * 设置电磁炉火力
     */
    short setDeviceFire_Req = 136;

    /**
     * 设置电磁炉火力_回复
     */
    short setDeviceFire_Rep = 137;

    /**
     * 设置电磁炉定时关机工作
     */
    short setDeviceShutdownWork_Req = 138;

    /**
     * 设置电磁炉定时关机工作_回复
     */
    short setDeviceShutdownWork_Rep = 139;


    /**
     * 设置电磁炉菜谱工作
     */
    short setDeviceRecipeWork_Req = 140;

    /**
     * /**
     * 设置电磁炉菜谱工作_回复
     */
    short setDeviceRecipeWork_Rep = 141;

    /**
     * 设置电磁炉设定信息
     */
    short setDeviceSetInformation_Req = 142;

    /**
     * 设置电磁炉设定信息_回复
     */
    short setDeviceSetInformation_Rep = 143;
    /**
     * 设置电磁炉简易智能默认温度
     */

    short setActionTempTure_Req = 144;

    /**
     * 设置电磁炉简易智能默认温度回复
     */
    short setActionTempTure_Rep = 145;

    /**
     * 报警（故障）上报
     */
    short DeviceAlarm = 146;

    /**
     * 电磁炉工作事件上报
     */
    short DeviceWorkReport = 147;

    /**
     * 电磁炉固件升级状态上报
     */

    short ReportUpdateStatus = 200;

//---------------------------洗碗机--------------------------------------

    /**
     * 设置洗碗机电源
     */
    short setDishWasherPower=128;

    /**
     * 洗碗机电源设置答复
     */
    short getDishWasherPower=129;

    /**
     * 设置洗碗机童锁
     */
    short setDishWasherChildLock=130;
    /**
     * 洗碗机童锁设置答复
     */
    short getDishWasherChildLock=131;
    /**
     * 设置洗碗机工作模式
     */
    short setDishWasherWorkMode=132;
    /**
     * 洗碗机工作模式设置答复
     */
    short getDishWasherWorkMode=133;
    /**
     * 洗碗机状态查询
     */
    short setDishWasherStatus=134;
    /**
     * 洗碗机状态查询回复
     */
    short getDishWasherStatus=135;
    /**
     * 洗碗机用户操作设置设置
     */
    short setDishWasherUserOperate=136;
    /**
     * 洗碗机用户操作设置设置回复
     */
    short getDishWasherUserOperate=137;
    /**
     * 报警事件上报
     */
    short getAlarmEventReport=140;
    /**
     * 事件上报
     */
    short getEventReport=141;


//---------------------------藏宝盒--------------------------------------

    /**
     * 读取藏宝盒状态
     */
    short getHidkitStatus_Req = 130;

    /**
     * 读取藏宝盒状态
     */
    short getHidkitStatus_Rep = 131;

    /**
     * 设置藏宝盒的状态组合命令
     */
    short setHidkitStatusCombined_Req = 132;

    /**
     * 设置藏宝盒的状态组合命令
     */
    short setHidkitStatusCombined_Rep = 133;


    /**
     * 藏宝盒事件上报
     */
    short getHidkitEventReport = 148;

//--------------------------------------新协议指令--------------------------------------------------------
    /**
     * 属性查询
     */
    short getDeviceAttribute_Req = 190;

    /**
     * 属性查询响应
     */
    short getDeviceAttribute_Rep = 191;

    /**
     * 属性设置
     */
    short setDeviceAttribute_Req = 192;

    /**
     * 属性设置响应
     */
    short setDeviceAttribute_Rep = 193;

    /**
     * 事件上报
     */
    short getDeviceEventReport = 194;

    /**
     * 历史事件上报
     */
    short getDeviceHistoryEventReport = 195;

    /**
     * 报警上报
     */
    short getDeviceAlarmEventReport = 197;

    /**
     * 历史报警上报
     */
    short getDeviceHistoryAlarmEventReport = 198;

    /**
     * 属性查询 烟机（针对集成灶设备需要分开查询）
     */
    short getDeviceAttribute_fan_Req = 190;

    /**
     * 属性查询 灶具（针对集成灶设备需要分开查询）
     */
    short getDeviceAttribute_stove_Req = 190;

}
