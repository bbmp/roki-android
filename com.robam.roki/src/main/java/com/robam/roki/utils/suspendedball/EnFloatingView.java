package com.robam.roki.utils.suspendedball;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.robam.roki.R;

import pl.droidsonroids.gif.GifImageView;


/**
 * @ClassName EnFloatingView
 * @Description 悬浮窗
 * @Author Yunpeng Li
 * @Creation 2018/3/15 下午5:04
 * @Mender Yunpeng Li
 * @Modification 2018/3/15 下午5:04
 */
public class EnFloatingView extends FloatingMagnetView {

    private GifImageView mIcon;

    public EnFloatingView(@NonNull Context context) {
        super(context, null);
        inflate(context, R.layout.floating_view, this);
        mIcon = (GifImageView) findViewById(R.id.icon);
    }

    public void setIconImage(@DrawableRes int resId) {
//        mIcon.setImageResource(resId);
//        mIcon.setGifResource(resId);
        mIcon.setImageResource(resId);
    }

}
