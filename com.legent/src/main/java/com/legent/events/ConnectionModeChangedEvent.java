package com.legent.events;

/**
 * Created by sylar on 15/8/7.
 */
public class ConnectionModeChangedEvent {

    public int connectionMode;

    public ConnectionModeChangedEvent(int connectionMode) {
        this.connectionMode = connectionMode;
    }

}