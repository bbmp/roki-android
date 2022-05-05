package com.robam.common.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by yinwei on 2017/8/24.
 */

public class OvenUserAction extends AbsStorePojo<Long> implements Serializable {
    @DatabaseField(generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField()
    public long userid;//

    @DatabaseField()
    public String timeDate;//日期

    @DatabaseField()
    public short mode;//模式

    @DatabaseField()
    public short temperature;//温度

    @DatabaseField()
    public short timeCook;//时间

    @DatabaseField()
    public String name;//模式名称

    @DatabaseField()
    public short temperUp;

    @DatabaseField()
    public short temperDown;

    public OvenUserAction(){

    }

    public OvenUserAction(long userid, String name, String timeDate, short mode, short temperature,short temperUp,short temperDown, short timeCook) {

        this.name = name;
        this.userid=userid;
        this.timeDate = timeDate;
        this.mode = mode;
        this.temperature = temperature;
        this.temperUp = temperUp;
        this.temperDown = temperDown;
        this.timeCook = timeCook;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public short getTemperUp() {
        return temperUp;
    }

    public void setTemperUp(short temperUp) {
        this.temperUp = temperUp;
    }

    public short getTemperDown() {
        return temperDown;
    }

    public void setTemperDown(short temperDown) {
        this.temperDown = temperDown;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public short getMode() {
        return mode;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public short getTemperature() {
        return temperature;
    }

    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    public short getTimeCook() {
        return timeCook;
    }

    public void setTimeCook(short timeCook) {
        this.timeCook = timeCook;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
