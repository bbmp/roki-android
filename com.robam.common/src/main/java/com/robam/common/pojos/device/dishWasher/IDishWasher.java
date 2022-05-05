package com.robam.common.pojos.device.dishWasher;

import com.legent.VoidCallback;
import com.robam.common.pojos.device.IPauseable;

public interface IDishWasher extends IPauseable {

    /**
     * 状态控制
     */
    void setDishWasherStatusControl(short status,VoidCallback voidCallback);
    /**
     * 设置洗碗机童锁
     */
    void setDishWasherLock(short status, VoidCallback callback);

    /**
     * 设置洗碗机的工作模式
     */
    //void setDishWasherWorkMode(short workMode,short bottomWasherSwitch,short autoVentilation,VoidCallback voidCallback);

    /**
     * 洗碗机用户操作设置设置
     */
    void userSet(short arg,short flushSalt,short rinse,VoidCallback voidCallback);




}
