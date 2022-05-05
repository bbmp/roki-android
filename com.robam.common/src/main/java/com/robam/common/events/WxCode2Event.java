package com.robam.common.events;

/**
 * @author hxw
 */
public class WxCode2Event {
    public String code;
    public String state ;
    public WxCode2Event(String code ,String state ){
        this.code = code;
        this.state = state ;
    }
}
