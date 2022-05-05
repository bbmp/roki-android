package com.robam.common.pojos.device.newTreaty;

/**
 * 新协议事件码
 */
public interface NewTreatyEventCode {

    /**
     * 启动工作
     */
    short startWork = 1;

    /**
     * 暂停工作
     */
    short suspendWork = 2;

    /**
     * 继续工作
     */
    short continueWork = 3;

    /**
     * 完成工作
     */
    short completeWork = 4;

    /**
     * 终止工作
     */
    short finishWork  = 5;

    /**
     * 预约工作
     */
    short  orderWork = 6;

    /**
     * 段结束提醒
     */
    short partEndReminder = 7;

    /**
     * 缺水提醒
     */
    short waterShortageReminder = 8;

    /**
     * 除垢提醒
     */
    short descalingReminder = 9;

    /**
     * 加蒸汽
     */
    short addSteamReminder = 10;

    /**
     * 加蒸汽
     */
    short doorControlReminder = 11;


}
