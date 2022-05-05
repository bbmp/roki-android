package com.robam.roki.ui.page.login;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.page.login.action.ClickAction;
import com.robam.roki.ui.page.login.action.TitleBarAction;

import java.util.List;

import butterknife.ButterKnife;
import retrofit.http.PUT;


/**
 *
 * @author hxw
 */
public abstract class MyBasePage<T> extends BasePage implements TitleBarAction , ClickAction {

    /** 标题栏对象 */
    private TitleBar mTitleBar;
    /** 当前是否加载过 */
    protected boolean mLoading;
    /** 根布局 */
    private View mRootView;
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (getLayoutId() <= 0) {
            return null;
        }
        LogUtils.i("MyBaseFrag" , getClass().getName());
        mLoading = false;
        mRootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.inject(this, mRootView);
        initView();
        setStateBarFixer();
        return mRootView;
    }

    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 根据资源 id 获取一个 View 对象
     */
    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    public Bundle getBundle() {
        return getArguments();
    }

    @Override
    public void onLeftClick(View view) {
        UIService.getInstance().popBack();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLoading) {
            mLoading = true;
            initData();
            onFragmentResume(true);
            return;
        }
            onFragmentResume(false);
    }
    /**
     * Fragment 可见回调
     *
     * @param first                 是否首次调用
     */
    protected void onFragmentResume(boolean first) {}
    /**
     * 这个 Fragment 是否已经加载过了
     */
    public boolean isLoading() {
        return mLoading;
    }

    /**
     * 获取布局view
     * @return
     */
    @NonNull
    @Override
    public View getView() {
        return mRootView;
    }
    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
    /**
     * Fragment 按键事件派发
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            // 这个子 Fragment 必须是 BaseFragment 的子类，并且处于可见状态
            if (!(fragment instanceof MyBasePage) ||
                    fragment.getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
                continue;
            }
            // 将按键事件派发给子 Fragment 进行处理
            if (((MyBasePage<?>) fragment).dispatchKeyEvent(event)) {
                // 如果子 Fragment 拦截了这个事件，那么就不交给父 Fragment 处理
                return true;
            }
        }
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                return onKeyDown(event.getKeyCode(), event);
            case KeyEvent.ACTION_UP:
                return onKeyUp(event.getKeyCode(), event);
            default:
                return false;
        }
    }

    /**
     * 按键按下事件回调
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 默认不拦截按键事件
        return false;
    }

    /**
     * 按键抬起事件回调
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // 默认不拦截按键事件
        return false;
    }
    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null || !isLoading()) {
            mTitleBar = obtainTitleBar((ViewGroup) getView());
        }
        return mTitleBar;
    }

    /**
     * 设置状态栏占位
     */
    protected void setStateBarFixer(){
        View mStateBarFixer = findViewById(R.id.status_bar_fix);
        if (mStateBarFixer != null){
            mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(getActivity())));
            mStateBarFixer.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void setStateBarFixer2() {
        if (rootView != null){
            View mStateBarFixer = rootView.findViewById(com.legent.ui.R.id.status_bar_fix);
            if (mStateBarFixer != null){
                mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(getActivity())));
                mStateBarFixer.setBackgroundColor(Color.WHITE);
            }else {
//                Class<? extends BasePage> aClass = getClass();
//                LogUtils.i("class_name" , aClass.getName());
//                if (!"com.robam.roki.ui.page.HomePage".equals(aClass.getName()) && !"com.robam.roki.ui.page.WelcomePage".equals(aClass.getName()) ){
//                    setMargins(rootView ,0 ,getStatusBarHeight(getActivity()) ,0 , 0 );
//                }
            }
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStateBarTransparent(){
        StatusBarUtils.setTransparent(getContext());
//        if (Build.VERSION.SDK_INT >= 21){
//            View decorView = activity.getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
    }
}
