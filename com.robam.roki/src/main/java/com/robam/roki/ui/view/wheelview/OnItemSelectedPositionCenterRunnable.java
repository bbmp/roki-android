
package com.robam.roki.ui.view.wheelview;


/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedPositionCenterRunnable implements Runnable {
    final LoopView mLoopViewRear;

    OnItemSelectedPositionCenterRunnable(LoopView loopViewRear) {
        mLoopViewRear = loopViewRear;
    }

    @Override
    public final void run() {

        mLoopViewRear.mOnItemSelectedListenerCenterPosition.onItemSelectedRear(mLoopViewRear.getSelectedItem());
    }
}
