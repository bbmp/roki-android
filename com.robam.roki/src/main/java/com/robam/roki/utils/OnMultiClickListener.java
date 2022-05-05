package com.robam.roki.utils;

import android.view.View;

import com.legent.plat.Plat;
import com.robam.common.pojos.device.fan.IFan;

import java.util.Calendar;

/**
 * @author r210190
 */
public abstract class OnMultiClickListener  implements View.OnClickListener {
    private int MIN_CLICK_DELAY_TIME = 1000;//多少秒点击一次 默认8秒
    private long lastClickTime = 0;

    public OnMultiClickListener() {
    }

    /**
     * 设置多少秒之内
     *
     * @param time
     */
    public OnMultiClickListener(int time) {
        this.MIN_CLICK_DELAY_TIME = time;
    }

    @Override
    public void onClick(View view) {
//        if (Plat.accountService.isLogon()){
//            return;
//        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onMoreClick(view);
        } else {
            onMoreErrorClick();
        }
    }

    /**
     * 在N秒之内的 ==1 次点击回调次方法
     *
     * @param view
     */
    protected abstract void onMoreClick(View view);

    /**
     * 在N秒之内的 >= 2次点击回调次方法
     */
    protected abstract void onMoreErrorClick();


}
