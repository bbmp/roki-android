package com.robam.common.pojos.device.dishWasher;

public interface DishWasherWorkMode {
    short Modeless=0;//无模式
    short StrongWash=1;//强力洗-
    short DailyWash=2;//日常洗-
    short EnergyWash=3;//节能洗-
    short FastWash=4;//快速洗-
    short IntelligentWash=5;//智能洗
    short CrystalWash=6;//晶亮洗-
    short Drain=7;//排水
    short SelfTest=8;//自检
    short Ventilation=9;//自动换气
    short VentilationWait=10;//自动换气等待

}
