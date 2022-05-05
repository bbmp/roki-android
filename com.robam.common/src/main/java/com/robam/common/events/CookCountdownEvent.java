package com.robam.common.events;

/**
 * 烧菜过程中，单步倒计时事件
 *
 * @author sylar
 */
public class CookCountdownEvent {

    public int stepIdnex;
    public int remainTime;

    public CookCountdownEvent(int stepIndex, int remainTime) {
        this.stepIdnex = stepIndex;
        this.remainTime = remainTime;
    }
}