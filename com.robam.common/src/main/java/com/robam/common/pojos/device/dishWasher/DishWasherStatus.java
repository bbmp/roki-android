package com.robam.common.pojos.device.dishWasher;

public interface DishWasherStatus {

    short off=0;//关机
    short wait=1;//待机
    short working=2;//工作中
    short pause=3;//暂停
    short end=4;//结束

    short appointmentSwitchOff=0;
    short appointmentSwitchOn=1;

}
