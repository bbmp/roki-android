package com.robam.common.pojos.device.newTreaty;

/**
 * 新协议编号
 * 属性的传输按照"编号-长度-值"的小端组合表示,英文是"key-len-value"
 */
public interface NewTreatyCode {

    /**
     * 电源状态
     */
    short powerStateCode = 1;

    /**
     * 电源控制
     */
    short powerCtrlCode = 2;

    /**
     * 工作状态
     */
    short workState = 3;

    /**
     * 工作控制
     */
    short workCtrl = 4;

    /**
     * 设置预约时间
     */
    short setOrderMinutes = 5;

    /**
     * 剩余预约时间
     */
    short orderLeftMinutes = 6;

    /**
     * 故障码
     */
    short faultCode = 7;

    /**
     * 灯开关
     */
    short lightSwitch = 8;

    /**
     * 旋转烤开关
     */
    short rotateSwitch = 9;

    /**
     * 水箱状态
     */
    short waterBoxState = 10;

    /**
     * 水箱控制
     */
    short waterBoxCtrl = 11;

    /**
     * 水位状态
     */
    short waterLevelState = 12;

    /**
     * 门状态
     */
    short doorState = 13;

    /**
     * 门控制开关
     */
    short doorSwitch = 14;

    /**
     * 加蒸汽工作状态
     */
    short steamState = 15;

    /**
     *加蒸汽控制
     */
    short steamCtrl = 16;

    /**
     *菜谱编号
     */
    short recipeId = 17;

    /**
     *菜谱设置总时间
     */
    short recipeSetMinutes = 18;

    /**
     *当前温度/上温度
     */
    short curTemp = 19;

    /**
     *当前EXP模式当前下温度
     */
    short curTemp2 = 20;

    /**
     *总剩余时间
     */
    short totalRemainSeconds = 21;

    /**
     *除垢请求标志
     */
    short descaleFlag = 22;

    /**
     *当前蒸模式累计工作时间
     */
    short curSteamTotalHours = 23;
    /**
     *蒸模式累计需除垢时间
     */
    short curSteamTotalNeedHours = 24;

    /**
     *实际运行时间
     */
    short cookedTime = 25;

    /**
     *当前段数/段序号
     */
    short curSectionNbr = 99;

    /**
     *设置段数
     */
    short sectionNumber = 100;

    /**
     *(首段)设置专业/辅助模式:mode
     */
    short mode = 101;

    /**
     *(首段)设置温度/上温度
     */
    short setUpTemp = 102;

    /**
     *(首段)设置下温度
     */
    short setDownTemp = 103;

    /**
     *(首段)设置时间:
     */
    short setTime = 104;

    /**
     *(首段)剩余时间
     */
    short restTime = 105;

    /**
     *UserID
     */
    short UserID = 255;
/**
 * 未定义名称编码：
 * 111 第二段设置模式
 * 112 第二段设置温度/上温度
 * 113 第二段设置下温度
 * 114 第二段设置时间
 * 115 第二段剩余时间
 * 121 第三段设置模式
 * 130 第三段参数保留供以后使用
 * 254 控制端类型
 */





}
