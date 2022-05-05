package com.legent.events;

/**
 * Created by sylar on 15/8/7.
 */
public class WifiConnectEvent {

    public String gate;
    public String local;

    public WifiConnectEvent(String gate, String local) {
        this.gate = gate;
        this.local = local;
    }

}