package com.legent.plat.events;

public class ViewChangedEvent {
    public String currentDevice;


    public ViewChangedEvent(String currentDevice) {
        this.currentDevice = currentDevice;
    }

    public String getCurrentDevice() {
        return currentDevice;
    }
}