

package com.robam.roki.ui.view.wheelview;

import com.legent.utils.LogUtils;

final class SmoothScrollTimerTask implements Runnable {

    int mRealTotalOffset;
    int mRealOffset;
    int mOffset;
    final LoopView mLoopView;

    SmoothScrollTimerTask(LoopView loopview, int offset) {
        mLoopView = loopview;
        mOffset = offset;
        mRealTotalOffset = Integer.MAX_VALUE;
        mRealOffset = 0;
    }

    @Override
    public final void run() {

        if (mRealTotalOffset == Integer.MAX_VALUE) {
            mRealTotalOffset = mOffset;
        }
        mRealOffset = (int) ((float) mRealTotalOffset * 0.1F);

        if (mRealOffset == 0) {
            if (mRealTotalOffset < 0) {
                mRealOffset = -1;
            } else {
                mRealOffset = 1;
            }
        }
        if (Math.abs(mRealTotalOffset) <= 0) {
            mLoopView.cancelFuture();
            mLoopView.mHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
        } else {
            mLoopView.mTotalScrollY = mLoopView.mTotalScrollY + mRealOffset;
            mLoopView.mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
            mRealTotalOffset = mRealTotalOffset - mRealOffset;
        }
    }
}
