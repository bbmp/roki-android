package com.legent.plat.events;

public class BlueCloseEvent {
    public boolean isClose;


    public BlueCloseEvent(boolean isClose) {
        this.isClose = isClose;
    }

    public boolean getCurrentDevice() {
        return isClose;
    }
}