package com.robam.common.events;

/**
 * 左右滑动管理
 *
 * @author zdj
 */
public class SlippingEvent {
    public String slipOrientation;

    public SlippingEvent(String slipOrientation) {
        this.slipOrientation = slipOrientation;
    }

}
