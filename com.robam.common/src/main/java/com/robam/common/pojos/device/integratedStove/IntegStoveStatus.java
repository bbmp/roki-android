package com.robam.common.pojos.device.integratedStove;

/**
 * 集成灶
 */

public interface IntegStoveStatus {
    /*-----------------------------------*/
    /**
     * 关机 电源状态
     */
    short powerState_off = 0 ;
    /**
     * 待机 电源状态
     */
    short powerState_wait = 1 ;
    /**
     * 开机 电源状态
     */
    short powerState_on = 2 ;


    short powerState_lock_clean = 4 ;
    /*-----------------------------------*/
    /**
     * 关机 电源控制
     */
    short powerCtrl_off = 0 ;
    /**
     * 开机 电源控制
     */
    short powerCtrl_on = 1 ;
    /*-----------------------------------*/
    /**
     * 工作状态 空闲
     */
    short workState_free = 0 ;
    /**
     * 工作状态 预约
     */
    short workState_order = 1 ;
    /**
     * 工作状态 预热
     */
    short workState_preheat = 2 ;
    /**
     * 工作状态 预热暂停
     */
    short workState_preheat_time_out = 3 ;
    /**
     * 工作状态 工作
     */
    short workState_work = 4 ;
    /**
     * 工作状态 工作暂停
     */
    short workState_work_time_out = 5 ;
    /**
     * 工作状态 完成工作
     */
    short workState_complete = 6 ;
    /**
     * 工作状态 自检
     */
    short workState_check = 7 ;
    /*-----------------------------------*/
    /**
     * 工作控制 停止
     */
    short workCtrl_stop = 0 ;
    /**
     * 工作控制 启动
     */
    short workCtrl_start = 1 ;
    /**
     * 工作控制 暂停
     */
    short workCtrl_time_out = 2 ;
    /**
     * 工作控制 预约
     */
    short workCtrl_order = 3 ;
    /**
     * 工作控制 继续
     */
    short workCtrl_continue = 4 ;
    /**
     * 工作控制 自检
     */
    short workCtrl_inspect = 5 ;

}
