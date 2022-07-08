package com.robam.roki.ui.activity3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.legent.ui.ext.utils.StatusBarCompat;
import com.legent.utils.EventUtils;
import com.robam.base.BaseActivity;
import com.robam.base.BaseDialog;
import com.robam.roki.R;
import com.robam.roki.ui.page.login.action.TitleBarAction;

import skin.support.utils.SkinStatusBarUtils;
import skin.support.widget.SkinCompatSupportable;


/**
 *    desc   : 业务 Activity 基类
 *    @author r210190
 */
public abstract class AppActivity extends BaseActivity
        implements ToastAction, TitleBarAction, SkinCompatSupportable {

    /** 标题栏对象 */
    private TitleBar mTitleBar;
    /** 状态栏沉浸 */
    private ImmersionBar mImmersionBar;

    /** 加载对话框 */
    private BaseDialog mDialog;
    /** 对话框数量 */
    private int mDialogTotal;


    /**
     * 显示加载对话框
     */
    public void showDialog() {

    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {

    }

    protected void setStateBarFixer() {

        View mStateBarFixer = findViewById(R.id.status_bar_fix);
        if (mStateBarFixer != null) {
            ViewGroup.LayoutParams layoutParams = mStateBarFixer.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = SkinStatusBarUtils.getStatusbarHeight(this);
            mStateBarFixer.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        EventUtils.regist(this);

        //状态栏透明
        SkinStatusBarUtils.translucent(this);
        setStateBarFixer();
        StatusBarCompat.updateStatusTextColor(this);
        if (getTitleBar() != null) {
            getTitleBar().setOnTitleBarListener(this);
        }

        // 初始化沉浸式状态栏
//        if (isStatusBarEnabled()) {
//            getStatusBarConfig().init();
//
//            // 设置标题栏沉浸
//            if (getTitleBar() != null) {
//                ImmersionBar.setTitleBar(this, getTitleBar());
//            }
//        }
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean isStatusBarEnabled() {
        return true;
    }

    /**
     * 状态栏字体深色模式
     */
    public boolean isStatusBarDarkFont() {
        return true;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    public ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式状态栏
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(isStatusBarDarkFont())
                // 指定导航栏背景颜色
                .navigationBarColor(android.R.color.white)
                // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
                .autoDarkModeEnable(true, 0.2f);
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(@StringRes int id) {
        setTitle(getString(id));
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        if (getTitleBar() != null) {
            getTitleBar().setTitle(title);
        }
    }

    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = obtainTitleBar(getContentView());
        }
        return mTitleBar;
    }

    @Override
    public void onLeftClick(View view) {
        onBackPressed();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void applySkin() {
        StatusBarCompat.updateStatusTextColor(this);
    }

    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventUtils.unregist(this);
        }catch (Exception e){
            e.getMessage();
        }

    }
}