package com.robam.roki.utils.suspendedball;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.robam.roki.R;



/**
 * @ClassName EnFloatingView
 * @Description 悬浮窗
 * @Author Yunpeng Li
 * @Creation 2018/3/15 下午5:04
 * @Mender Yunpeng Li
 * @Modification 2018/3/15 下午5:04
 */
public class EnFloatingView extends FloatingMagnetView {

    private ImageView mIcon;

    public EnFloatingView(@NonNull Context context) {
        super(context, null);
        inflate(context, R.layout.floating_view, this);
        mIcon = (ImageView) findViewById(R.id.icon);
    }

    public void setIconImage(@DrawableRes int resId) {
//        mIcon.setImageResource(resId);
//        mIcon.setGifResource(resId);
        mIcon.setImageResource(resId);
    }

}
