
package com.robam.roki.ui.view.wheelview;

import com.legent.utils.LogUtils;

/**
 * 选中的item实现runnable接口
 */
final class OnItemSelectedFroneRunnable implements Runnable {
    final LoopView mLoopViewFrone;

    OnItemSelectedFroneRunnable(LoopView loopviewFrone) {
        mLoopViewFrone = loopviewFrone;

    }

    @Override
    public final void run() {
        LogUtils.i("20170829","mOnItemSelectedListenerFrone:"+mLoopViewFrone.mOnItemSelectedListenerFrone);
        LogUtils.i("20170829","xxx:"+mLoopViewFrone.getItemsContent(mLoopViewFrone.getSelectedItem()));
        LogUtils.i("20170829","yyy:"+mLoopViewFrone.getSelectedItem());
        mLoopViewFrone.mOnItemSelectedListenerFrone.onItemSelectedFront(mLoopViewFrone.getItemsContent(mLoopViewFrone.getSelectedItem()));
    }
}
