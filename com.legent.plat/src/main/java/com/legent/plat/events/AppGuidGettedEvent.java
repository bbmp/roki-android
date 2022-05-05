package com.legent.plat.events;

/**
 * 成功获取AppGuid的通知事件
 *
 * @author sylar
 */
public class AppGuidGettedEvent {

    public String appGuid;

    public AppGuidGettedEvent(String appGuid) {
        this.appGuid = appGuid;
    }

}