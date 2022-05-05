
package com.robam.roki.ui.view.wheelview;


/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedPositionRunnable implements Runnable {
    final LoopView mLoopViewRear;

    OnItemSelectedPositionRunnable(LoopView loopViewRear) {
        mLoopViewRear = loopViewRear;
    }

    @Override
    public final void run() {

        mLoopViewRear.mOnItemSelectedListenerPosition.onItemSelectedRear(mLoopViewRear.getSelectedItem());
    }
}
