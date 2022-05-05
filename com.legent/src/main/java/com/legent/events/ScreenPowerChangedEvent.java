package com.legent.events;

/**
 * Created by sylar on 15/8/7.
 */
public class ScreenPowerChangedEvent {

    public int powerStatus;

    public ScreenPowerChangedEvent(int powerStatus) {
        this.powerStatus = powerStatus;
    }
}