package com.legent.ui.ext.popoups;

import android.content.Context;

/**
 * Created by zhoudingjun on 2016/11/8.
 */

public class Dp2PxUtils {
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
