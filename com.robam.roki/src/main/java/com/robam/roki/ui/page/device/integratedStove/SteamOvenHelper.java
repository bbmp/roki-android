package com.robam.roki.ui.page.device.integratedStove;

import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;


public class SteamOvenHelper {
    /**
     * 判断工作模式是否需要水
     * @return true 需要 false 不需要
     */
    public static boolean isWater(SteamOvenModeEnum modeEnum){
        if (modeEnum == SteamOvenModeEnum.XIANNENZHENG
          || modeEnum == SteamOvenModeEnum.YIYANGZHENG
          || modeEnum == SteamOvenModeEnum.GAOWENZHENG
          || modeEnum == SteamOvenModeEnum.WEIBOZHENG
          || modeEnum == SteamOvenModeEnum.ZHIKONGZHENG
          || modeEnum == SteamOvenModeEnum.JIASHIBEIKAO
          || modeEnum == SteamOvenModeEnum.SHAJUN
                ||modeEnum==SteamOvenModeEnum.JIASHIQIANGSHAOKAO
          || modeEnum == SteamOvenModeEnum.JIEDONG
          || modeEnum == SteamOvenModeEnum.FAJIAO
           ||modeEnum==SteamOvenModeEnum.JIASHIFENGBEIKAO
                || modeEnum == SteamOvenModeEnum.SHOUDONGJIASHIKAO
        ){
            return true ;
        }
        return false ;
    }


    public static boolean isMicro(SteamOvenModeEnum modeEnum){
        if (modeEnum == SteamOvenModeEnum.WEIBO
                || modeEnum == SteamOvenModeEnum.WEIBOKAO
                || modeEnum == SteamOvenModeEnum.WEIBOZHENG

        ){
            return true ;
        }
        return false ;
    }
    /**
     * 判断菜谱是否需要水
     * @return
     */
    public static boolean isRecipeWater(String needWater){
        if ("1".equals(needWater)
        ){
            return true ;
        }
        return false ;
    }


    /**
     * 判断水箱状态
     * @param state 0  1 2 3
     * @return true 闭合
     */
    public static boolean isWaterBoxState(short state){
        if ( state == 1 || state == 2 || state == 3
        ){
            return false ;
        }
        return true ;
    }

    /**
     * 判断水位状态
     * @param state 0 不缺水 1 缺水
     * @return true 闭合
     */
    public static boolean isWaterLevelState(short state){
        if ( state == 1
        ){
            return false ;
        }
        return true ;
    }
    /**
     * 判断门状态
     * @param state 0 闭合 其他 未闭合
     * @return true 闭合
     */
    public static boolean isDoorState(short state){
        if ( state == 0
        ){
            return true ;
        }
        return false ;
    }

    /**
     * 判断是否可以开门工作
     * @param modeEnum 干燥可以开门工作
     * @return true 可以
     */
    public static boolean isOpenDoorWork(SteamOvenModeEnum modeEnum){
        if ( modeEnum == SteamOvenModeEnum.GANZAO||modeEnum == SteamOvenModeEnum.CHUGOU
        ){
            return true ;
        }
        return false ;
    }

    /**
     * 判断是否工作中
     * @return
     */
    public static boolean isWork(short state){
        if (state == IntegStoveStatus.workState_free || state == IntegStoveStatus.workState_complete){
            return false ;
        }else {
            return true ;
        }
    }

    /**
     * 判断是否工作中(暂停不算工作 9YC03)
     * @return
     */
    public static boolean isWork2(short state){
        if (state == IntegStoveStatus.workState_preheat || state == IntegStoveStatus.workState_work){
            return true ;
        }else {
            return false ;
        }
    }
    /**
     * 判断是否暂停
     * @param state
     * @return
     */
    public static boolean isPause(short state){
        if (state ==  IntegStoveStatus.workState_preheat_time_out
                || state ==  IntegStoveStatus.workState_work_time_out
        ){
            return true ;
        }else{
            return false ;
        }
    }

    /**
     * 判断是否预约
     * @param state
     * @return
     */
    public static boolean isOrder(short state){
        if (state ==  IntegStoveStatus.workState_order
        ){
            return true ;
        }else{
            return false ;
        }
    }

    /**
     * 判断是否可以加蒸汽
     * @param mode
     * @return
     */
    public static boolean isAddSteam(short mode){
        SteamOvenModeEnum match = SteamOvenModeEnum.match(mode);
        if (match == SteamOvenModeEnum.FENGBEIKAO
                || match == SteamOvenModeEnum.BEIKAO
                || match == SteamOvenModeEnum.FENGSHANKAO
                || match == SteamOvenModeEnum.QIANGSHAOKAO
                || match == SteamOvenModeEnum.SHAOKAO
//                || match == SteamOvenModeEnum.KONGQIZHA
                || match == SteamOvenModeEnum.EXP
                || match == SteamOvenModeEnum.KUAIRE

        ){
            return true ;
        }else {
            return false ;
        }
    }
    /**
     * 判断是否多段
     * @param sectionNumber
     * @return
     */
    public static boolean isMult(short sectionNumber){
        if (sectionNumber > 1
        ){
            return true ;
        }else {
            return false ;
        }
    }
    /**
     * 判断是否澎湃蒸 显示蒸汽量
     * @param mode
     * @return
     */
    public static boolean isShowSteam(short mode){
        SteamOvenModeEnum match = SteamOvenModeEnum.match(mode);
        if (match == SteamOvenModeEnum.ZHIKONGZHENG||match ==SteamOvenModeEnum.JIASHIFENGBEIKAO||match ==SteamOvenModeEnum.JIASHIFENGBEIKAO
               || match ==SteamOvenModeEnum.JIASHIQIANGSHAOKAO||match ==SteamOvenModeEnum.JIASHIQIANGSHAOKAO||match==SteamOvenModeEnum.JIASHIBEIKAO
        ||match ==SteamOvenModeEnum.JIASHISHAOKAO){
            return true ;
        }else {
            return false ;
        }
    }

    /**
     * 判断是否需要除垢
     * @param descale
     * @return
     */
    public static boolean isDescale(short descale){
        if (descale == 0){
            return false ;
        }else {
            return true ;
        }
    }

    /**
     * 获取烟机档位值
     * @param state
     * @return
     */
    public static String getSteamContent(short state){
        if (0 == state){
            return "无" ;
        }
        else if (1 == state){
            return "小" ;
        }
        else if (2 == state){
            return "中" ;
        }
        else if (3 == state){
            return "大" ;
        }
        else if (4 == state){
            return "极大" ;
        }else {
            return "未知" ;
        }
    }

    /**
     * 判断集成灶是否某一设备在工作中
     * @param integratedStove
     * @return
     */
    public static boolean isIntegratedStoveWork(AbsIntegratedStove integratedStove){
        if (integratedStove.fan_powerState == IntegStoveStatus.powerState_on
                || integratedStove.stove_powerState == IntegStoveStatus.powerState_on
                || integratedStove.powerState == IntegStoveStatus.powerState_on
        ){
            return true ;
        }else {
            return false ;
        }
    }
}
