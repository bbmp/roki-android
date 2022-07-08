package com.robam.common.pojos.device.steameovenone;

public interface SteamOvenOnePowerOnStatusNew  {

//
    short Off =0;

    short Open =1;






    short rerun = 4;
    short pause = 2;

    short workPauseSteam=5;



    /**
     * 空闲
     */
    short noWork=0;

    /**
     * 预约
     */
    short PreWork=1;
    /**
     * 预约加热
     */
    short preWorkHeat=2;
    /**
     * 预约加热暂停
     */
    short preWorkHeatPause=3;


    /**
     * 暂停
     */
    short workPause=3;
    /**
     * 工作中
     */
    short Working=4;

    /**
     * 工作 完成
     */
    short finish =6;

    /**
     * 自检
     */
    short checkSelf=7;


//    /**
//     * 工作控制
//     */
//    short workCtrl=3;



    //待机
    short waitForWork =1;

    short opening=2;


    short working=4;

}
