package com.robam.roki.ui.activity3.device.base.skipview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.robam.widget.layout.SettingBar;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SkipSettingBar extends SettingBar implements SkinCompatSupportable {
    private  SkinCompatBackgroundHelper mSkinCompatBackgroundHelper;



    public SkipSettingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSkinCompatBackgroundHelper = new SkinCompatBackgroundHelper(this);
        mSkinCompatBackgroundHelper.loadFromAttributes(attrs, defStyleAttr);
        // 步骤二：马上处理换肤
         mSkinCompatBackgroundHelper.applySkin();
    }


    @Override
    public void applySkin() {
        if (mSkinCompatBackgroundHelper != null) {
            mSkinCompatBackgroundHelper.applySkin();
        }
    }
}
