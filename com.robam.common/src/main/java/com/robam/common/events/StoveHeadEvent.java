package com.robam.common.events;


/**
 * Created by Administrator on 2017/7/6.
 */
public class StoveHeadEvent {
    private String headId;

    public StoveHeadEvent(String headId) {
        this.headId = headId;
    }

    public String getHeadId() {
        return headId;
    }
}
