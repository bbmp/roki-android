package com.robam.common.events;

/**
 * Created by Administrator on 2017/6/28.
 */
public class Stove9B39FireEvent {
    private boolean isCook;
    private short   stoveHeadId;
    private short   stoveLevel;


    public Stove9B39FireEvent(boolean isCook, short stoveHeadId, short stoveLevel) {
        this.isCook = isCook;
        this.stoveHeadId = stoveHeadId;
        this.stoveLevel = stoveLevel;
    }


    public boolean isCook() {
        return isCook;
    }

    public short getStoveHeadId() {
        return stoveHeadId;
    }

    public short getStoveLevel() {
        return stoveLevel;
    }
}
