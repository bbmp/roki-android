package com.robam.common.events;

/**
 * Created by Administrator on 2019/8/21.
 */

public class ClearTextEvent {
    boolean isClear;

    public ClearTextEvent(boolean isClear) {
        this.isClear = isClear;
    }

    public boolean isClear() {
        return isClear;
    }
}
