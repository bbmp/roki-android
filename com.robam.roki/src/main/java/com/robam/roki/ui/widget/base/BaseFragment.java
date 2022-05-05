package com.robam.roki.ui.widget.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.robam.roki.ui.page.login.action.ActivityAction;
import com.robam.roki.ui.page.login.action.BundleAction;
import com.robam.roki.ui.page.login.action.ClickAction;
import com.robam.roki.ui.page.login.action.HandlerAction;
import com.robam.roki.ui.page.login.action.KeyboardAction;
import com.robam.roki.ui.page.login.action.ResourcesAction;

import java.util.List;

public abstract class BaseFragment<A extends BaseActivity> extends Fragment implements ActivityAction, ResourcesAction, HandlerAction, ClickAction, BundleAction, KeyboardAction {
    private A mActivity;

    private View mRootView;

    private boolean mLoading;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (A)requireActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() <= 0)
            return null;
        this.mLoading = false;
        this.mRootView = inflater.inflate(getLayoutId(), container, false);
        initView();
        return this.mRootView;
    }

    public void onResume() {
        super.onResume();
        if (!this.mLoading) {
            this.mLoading = true;
            initData();
            onFragmentResume(true);
            return;
        }
        if (this.mActivity != null && this.mActivity.getLifecycle().getCurrentState() == Lifecycle.State.STARTED) {
            onActivityResume();
        } else {
            onFragmentResume(false);
        }
    }

    protected void onFragmentResume(boolean first) {}

    protected void onActivityResume() {}

    public void onDestroyView() {
        super.onDestroyView();
        this.mRootView = null;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mLoading = false;
        removeCallbacks();
    }

    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    public boolean isLoading() {
        return this.mLoading;
    }

    @NonNull
    public View getView() {
        return this.mRootView;
    }

    public A getAttachActivity() {
        return this.mActivity;
    }

    public <V extends View> V findViewById(@IdRes int id) {
        return (V)this.mRootView.findViewById(id);
    }

    public Bundle getBundle() {
        return getArguments();
    }

    public void startActivityForResult(Class<? extends Activity> clazz, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(clazz, callback);
    }

    public void startActivityForResult(Intent intent, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(intent, (Bundle)null, callback);
    }

    public void startActivityForResult(Intent intent, Bundle options, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(intent, options, callback);
    }

    public void finish() {
        if (this.mActivity == null || this.mActivity.isFinishing() || this.mActivity.isDestroyed())
            return;
        this.mActivity.finish();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (!(fragment instanceof BaseFragment) || fragment
                    .getLifecycle().getCurrentState() != Lifecycle.State.RESUMED)
                continue;
            if (((BaseFragment)fragment).dispatchKeyEvent(event))
                return true;
        }
        switch (event.getAction()) {
            case 0:
                return onKeyDown(event.getKeyCode(), event);
            case 1:
                return onKeyUp(event.getKeyCode(), event);
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}

