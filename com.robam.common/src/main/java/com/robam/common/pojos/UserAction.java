package com.robam.common.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.legent.pojos.AbsStorePojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/21.
 */

public class UserAction extends AbsStorePojo<Long> implements Serializable {
    @DatabaseField(generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField()
    public long userId;//userid

    @DatabaseField()
    public String timeDate;//日期

    @DatabaseField()
    public short mode;//模式

    @DatabaseField()
    public short fire;//火力

    @DatabaseField()
    public short weight;//重量

    @DatabaseField()
    public short timeCook;//时间

    @DatabaseField()
    public String name;//模式名称

    public UserAction(){

    }



    public UserAction(long userId, String name, String timeDate, short mode, short fire, short weight, short timeCook) {
        this.userId = userId;
        this.timeDate = timeDate;
        this.mode = mode;
        this.fire = fire;
        this.weight = weight;
        this.timeCook = timeCook;
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
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

    public short getFire() {
        return fire;
    }

    public void setFire(short fire) {
        this.fire = fire;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public short getTimeCook() {
        return timeCook;
    }

    public void setTimeCook(short timeCook) {
        this.timeCook = timeCook;
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
