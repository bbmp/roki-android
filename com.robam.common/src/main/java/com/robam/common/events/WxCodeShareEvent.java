package com.robam.common.events;

public class WxCodeShareEvent {
    public String code;
    public String state;

    public WxCodeShareEvent(String code, String state) {
        this.code = code;
        this.state = state;
    }

    public WxCodeShareEvent() {

    }
}
