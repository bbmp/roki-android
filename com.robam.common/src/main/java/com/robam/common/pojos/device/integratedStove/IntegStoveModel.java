package com.robam.common.pojos.device.integratedStove;

/**
 * Created by 14807 on 2018/4/26.
 */

public interface IntegStoveModel {
    /**
     * 烟机风量
     */
    String SMOKE_AIR_VOLUME = "smokeAirVolume";
    /**
     * 专业模式 蒸烤炸
     */
     String STEAMING_ROAST_MODE = "steamingRoastMode";
    /**
     * 本地自动菜谱
     */
     String LOCAL_COOKBOOK = "localCookbook";
    /**
     * 烟灶蒸烤联动
     */
     String SMOKE_COOKER_STEAMING_ROAST_LINKAGE= "smokeCookerSteamingRoastLinkage";
    /**
     * 烟灶蒸烤联动
     */
    String SMOKE_COOKER_STEAMING_LINKAGE= "smokeCookerSteamingLinkage";
    /**
     * 辅助模式
     */
     String MISCELLANOUS= "miscellaneousFunctionNew";
    /**
     * 辅助模式
     */
     String MULTI_STEP_MODEL= "multiStepModel";
    /**
     * 本地工作页面
     */
    String RUN_TIME_DOWN_VIEW= "runTimeDownView";

    /**
     * 结束图标
     */
    String FINISH= "finish";
    /**
     * 暂停
     */
    String WORKING_PAUSE = "working_pause";
    /**
     * 加蒸汽
     */
    String WORKING_ADD_STEAM = "workingAddSteam";

}
