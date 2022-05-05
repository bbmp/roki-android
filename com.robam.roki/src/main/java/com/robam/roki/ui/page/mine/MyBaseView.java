package com.robam.roki.ui.page.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.legent.utils.EventUtils;
import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.page.login.action.ClickAction;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import butterknife.ButterKnife;

/**
 * @ClassName: MyBaseView
 * @Description:
 * @Author: Hxw
 * @CreateDate: 2021/3/22 9:19
 */
public abstract class MyBaseView extends FrameLayout implements UIListeners.IRefresh, View.OnTouchListener, ClickAction {
    /**
     * Activity
     */
    protected  FragmentActivity activity ;
    /**
     * Context
     */
    protected  Context cx ;
    /** 根布局 */
    private View mRootView;
    public MyBaseView(@NonNull Context context) {
        super(context);
        this.cx= context ;
        init(context ,null);
    }
    public MyBaseView(Context context, FragmentActivity activity) {
        super(context);
        this.activity = activity;
        this.cx = context ;
        init(context ,null);
    }

    public MyBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.cx= context ;
        init(context ,attrs);
    }

    public MyBaseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.cx= context ;
        init(context ,attrs);
    }
    void init(Context cx, AttributeSet attrs) {
        if (getLayoutId() <= 0){
            return;
        }
        mRootView = LayoutInflater.from(cx).inflate(getLayoutId(), this, true);
        if (!mRootView.isInEditMode()) {
            ScreenAdapterTools.getInstance().loadView(mRootView);
            ButterKnife.inject(this, mRootView);
            initView();
            initData();
        }
    }
    /**
     * 根据资源 id 获取一个 View 对象
     */
    public <V extends View> V findViewBy(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    /**
     *  根据资源 id 获取String
     * @param id
     * @return
     */
    public String getString( int id){
        return getResources().getString(id);
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
     * 获取布局view
     * @return
     */
    @NonNull
    public View getView() {
        return mRootView;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onRefresh() {

    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }
}
