

package com.robam.roki.ui.view.wheelview;



/**
 * 滑动任务类此算法不变所以定义成final类
 */
final class InertiaTimerTask implements Runnable {

    float a;
    final float velocityY;
    final LoopView mLoopView;


    InertiaTimerTask(LoopView loopview, float velocityY) {
        super();
        mLoopView = loopview;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            mLoopView.cancelFuture();
            mLoopView.mHandler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        LoopView loopview = mLoopView;
        loopview.mTotalScrollY = loopview.mTotalScrollY - i;
        if (!mLoopView.mIsLoop) {
            float itemHeight = mLoopView.mLineSpacingMultiplier * mLoopView.mMaxTextHeight;
            if (mLoopView.mTotalScrollY <= (int) ((float) (-mLoopView.mInitPosition) * itemHeight)) {
                a = 2F;
                mLoopView.mTotalScrollY = (int) ((float) (-mLoopView.mInitPosition) * itemHeight);
            } else if (mLoopView.mTotalScrollY >= (int) ((float) (mLoopView.mItems.size() - 1 - mLoopView.mInitPosition) * itemHeight)) {
                mLoopView.mTotalScrollY = (int) ((float) (mLoopView.mItems.size() - 1 - mLoopView.mInitPosition) * itemHeight);
                a = -2F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        mLoopView.mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
