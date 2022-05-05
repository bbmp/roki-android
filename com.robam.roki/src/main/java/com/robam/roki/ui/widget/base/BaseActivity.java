package com.robam.roki.ui.widget.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;


import com.robam.roki.ui.page.login.action.ActivityAction;
import com.robam.roki.ui.page.login.action.BundleAction;
import com.robam.roki.ui.page.login.action.ClickAction;
import com.robam.roki.ui.page.login.action.HandlerAction;
import com.robam.roki.ui.page.login.action.KeyboardAction;

import java.util.List;
import java.util.Random;

public abstract class BaseActivity extends AppCompatActivity implements ActivityAction, ClickAction, HandlerAction, BundleAction, KeyboardAction {
    private SparseArray<OnActivityCallback> mActivityCallbacks;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    protected void initActivity() {
        initLayout();
        initView();
        initData();
    }

    protected void initLayout() {
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            initSoftKeyboard();
        }
    }

    protected void initSoftKeyboard() {
        getContentView().setOnClickListener(v -> hideKeyboard(getCurrentFocus()));
    }

    protected void onDestroy() {
        super.onDestroy();
        removeCallbacks();
    }

    public void finish() {
        hideKeyboard(getCurrentFocus());
        super.finish();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public Bundle getBundle() {
        return getIntent().getExtras();
    }

    public ViewGroup getContentView() {
        return (ViewGroup)findViewById(16908290);
    }

    public Context getContext() {
        return (Context)this;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (!(fragment instanceof BaseFragment) || fragment
                    .getLifecycle().getCurrentState() != Lifecycle.State.RESUMED)
                continue;
            if (((BaseFragment)fragment).dispatchKeyEvent(event))
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        hideKeyboard(getCurrentFocus());
        super.startActivityForResult(intent, requestCode, options);
    }

    public void startActivityForResult(Class<? extends Activity> clazz, OnActivityCallback callback) {
        startActivityForResult(new Intent((Context)this, clazz), (Bundle)null, callback);
    }

    public void startActivityForResult(Intent intent, OnActivityCallback callback) {
        startActivityForResult(intent, (Bundle)null, callback);
    }

    public void startActivityForResult(Intent intent, @Nullable Bundle options, OnActivityCallback callback) {
        if (this.mActivityCallbacks == null)
            this.mActivityCallbacks = new SparseArray(1);
        int requestCode = (new Random()).nextInt((int)Math.pow(2.0D, 16.0D));
        this.mActivityCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode, options);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        OnActivityCallback callback;
        if (this.mActivityCallbacks != null && (callback = (OnActivityCallback)this.mActivityCallbacks.get(requestCode)) != null) {
            callback.onActivityResult(resultCode, data);
            this.mActivityCallbacks.remove(requestCode);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    public static interface OnActivityCallback {
        void onActivityResult(int param1Int, @Nullable Intent param1Intent);
    }
}

