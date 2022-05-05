package com.robam.roki.ui.video;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JzvdStd;

/**
 * 列表静音播放 全屏非静音
 * @author huxw
 * @date 2021/12/5
 */

public class JzvdStdVolumeAfterFullscreen extends JzvdStd {
    public JzvdStdVolumeAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdVolumeAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (screen == SCREEN_FULLSCREEN) {
            mediaInterface.setVolume(1f, 1f);
        } else {
            mediaInterface.setVolume(0f, 0f);
        }
    }

    /**
     * 进入全屏模式的时候关闭静音模式
     */
    @Override
    public void gotoFullscreen() {
        super.gotoFullscreen();
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (mediaInterface != null)
            mediaInterface.setVolume(1f, 1f);
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        if (mediaInterface != null)
            mediaInterface.setVolume(0f, 0f);
    }
}
