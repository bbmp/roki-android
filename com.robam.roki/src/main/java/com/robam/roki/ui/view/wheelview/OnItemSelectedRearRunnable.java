
package com.robam.roki.ui.view.wheelview;


/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedRearRunnable implements Runnable {
    final LoopView mLoopViewRear;

    OnItemSelectedRearRunnable(LoopView loopViewRear) {
        mLoopViewRear = loopViewRear;
    }

    @Override
    public final void run() {

        mLoopViewRear.mOnItemSelectedListenerRear.onItemSelectedRear(mLoopViewRear.getItemsContent(mLoopViewRear.getSelectedItem()));
    }
}
