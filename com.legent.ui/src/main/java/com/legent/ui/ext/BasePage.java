package com.legent.ui.ext;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.legent.ui.AbsPage;
import com.legent.ui.R;
import com.legent.utils.LogUtils;

public abstract class BasePage extends AbsPage {


    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Drawable dr = rootView.getBackground();
        setStateBarFixer2();
        if (dr == null) {
            setRootBg();
        }

    }

    protected void setRootBg() {
        setRootBgRes(R.drawable.default_background);
    }

    protected void setRootBgColor(final int color) {
        if (rootView != null) {
            rootView.setBackgroundColor(color);
        }
    }

    protected void setRootBgRes(final int resid) {
        if (rootView != null) {
            rootView.setBackgroundResource(resid);
        }
    }

    protected static int getStatusBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置状态栏占位
     */
    protected void setStateBarFixer2(){
        if (rootView != null){
            View mStateBarFixer = rootView.findViewById(R.id.status_bar_fix);
            if (mStateBarFixer != null){
                ViewGroup.LayoutParams layoutParams = mStateBarFixer.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = getStatusBarHeight(getActivity());
                mStateBarFixer.setLayoutParams(layoutParams);
//          根布局只能是linearlayout,太局限
//                mStateBarFixer.setBackgroundColor(Color.WHITE);
            }else {
                Class<? extends BasePage> aClass = getClass();
                LogUtils.i("class_name" , aClass.getName());
                if (!"com.robam.roki.ui.page.HomePage".equals(aClass.getName()) && !"com.robam.roki.ui.page.WelcomePage".equals(aClass.getName()) ){
                    setMargins(rootView ,0 ,getStatusBarHeight(getActivity()) ,0 , 0 );
                }
            }
        }

    }


    public  void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

}
