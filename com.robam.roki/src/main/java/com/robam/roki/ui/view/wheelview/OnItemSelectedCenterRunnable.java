
package com.robam.roki.ui.view.wheelview;

/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedCenterRunnable implements Runnable {
    final LoopView mLoopViewCenter;

    OnItemSelectedCenterRunnable(LoopView loopviewCenter) {
        mLoopViewCenter = loopviewCenter;
    }
    @Override
    public final void run() {
        mLoopViewCenter.mOnItemSelectedListenerCenter.onItemSelectedCenter(mLoopViewCenter.getItemsContent(mLoopViewCenter.getSelectedItem()));
    }
}
