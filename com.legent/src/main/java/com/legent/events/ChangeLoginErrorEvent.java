package com.legent.events;

/**
 * Created by 14807 on 2018/9/10.
 */

public class ChangeLoginErrorEvent {

    public int status;

    public ChangeLoginErrorEvent(int status) {
        this.status = status;
    }
}
