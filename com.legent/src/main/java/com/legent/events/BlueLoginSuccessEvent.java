package com.legent.events;

/**
 * 蓝牙弹出窗登录成功
 */
public class BlueLoginSuccessEvent {

    public boolean loginSuccess;

    public BlueLoginSuccessEvent(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}
