package com.legent.plat.events;

/**
 * Created by Administrator on 2017/6/9.
 */
public class DismissTexAutoSetEvent {

    public DismissTexAutoSetEvent() {
    }

    private boolean isDisplay = false;



    public DismissTexAutoSetEvent(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }
}
