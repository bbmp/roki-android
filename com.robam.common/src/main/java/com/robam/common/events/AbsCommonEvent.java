package com.robam.common.events;

/**
 * Created by as on 2017-04-06.
 */

public class AbsCommonEvent {
    public static final int DevicePotStartCooking = 1;
    public static final int DevicePotNotCooking = 2;
    public static final int DevicePotFinishCooking = 3;
    public static final int TitleBarTimeUpdate = 4;//标题栏时间更新事件
    public static final int HomePageSlideEvent = 5;//home页面滑动事件
    public int value;
    public Object[] object;

    public AbsCommonEvent(int v) {
        value = v;
    }

    public AbsCommonEvent(int v, Object... object) {
        value = v;
        this.object = object;
    }
}
