package com.robam.common.events;

/**
 * Created by yinwei on 2017/10/26.
 */

public class WxCodeEvent {
    public String code;
    /**
     * tag 0:登录 1:绑定
     */
    public String tag ;
    public WxCodeEvent(String code){
        this.code = code;
    }
}
