package com.robam.common.pojos.device.rika;

import com.legent.utils.LogUtils;

/**
 * Created by 14807 on 2018/4/2.
 * Rika 报警码存储bean
 */

public class RikaAlarmCodeBean {

    private short fanAlarmCode;//烟机

    private short stoveAlarmCode;//灶具

    private short sterilAlarmCode;//消毒柜

    private short steamAlarmCode;//蒸汽炉

    private short steamOvenAlarmCode;//一体机


    public RikaAlarmCodeBean(short stoveAlarmCode, short fanAlarmCode, short sterilAlarmCode, short steamAlarmCode, short steamOvenAlarmCode) {
        this.stoveAlarmCode = stoveAlarmCode;
        this.fanAlarmCode = fanAlarmCode;
        this.sterilAlarmCode = sterilAlarmCode;
        this.steamAlarmCode = steamAlarmCode;
        this.steamOvenAlarmCode = steamOvenAlarmCode;
        LogUtils.i("20180614"," steamAlarmCode:" + steamAlarmCode);
    }

    public short getFanAlarmCode() {
        return fanAlarmCode;
    }

    public void setFanAlarmCode(short fanAlarmCode) {
        this.fanAlarmCode = fanAlarmCode;
    }

    public short getStoveAlarmCode() {
        return stoveAlarmCode;
    }

    public void setStoveAlarmCode(short stoveAlarmCode) {
        this.stoveAlarmCode = stoveAlarmCode;
    }

    public short getSterilAlarmCode() {
        return sterilAlarmCode;
    }

    public void setSterilAlarmCode(short sterilAlarmCode) {
        this.sterilAlarmCode = sterilAlarmCode;
    }

    public short getSteamAlarmCode() {
        return steamAlarmCode;
    }

    public void setSteamAlarmCode(short steamAlarmCode) {
        this.steamAlarmCode = steamAlarmCode;
    }

    public short getSteamOvenAlarmCode() {
        return steamOvenAlarmCode;
    }

    public void setSteamOvenAlarmCode(short steamOvenAlarmCode) {
        this.steamOvenAlarmCode = steamOvenAlarmCode;
    }

    @Override
    public String toString() {
        return "RikaAlarmCodeBean{" +
                "fanAlarmCode=" + fanAlarmCode +
                ", stoveAlarmCode=" + stoveAlarmCode +
                ", sterilAlarmCode=" + sterilAlarmCode +
                ", steamAlarmCode=" + steamAlarmCode +
                '}';
    }
}
