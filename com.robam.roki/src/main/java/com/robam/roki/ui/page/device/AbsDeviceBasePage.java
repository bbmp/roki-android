package com.robam.roki.ui.page.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.ui.ext.BasePage;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

/**
 * Created by 14807 on 2018/4/20.
 */

public abstract class AbsDeviceBasePage extends BasePage {


    protected View mRoot;
    protected boolean mFirstData = true;

    protected abstract int getContentLayoutId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRoot == null) {
            int layoutId = getContentLayoutId();
            //初始化当前的根布局，但是不在创建时就添加到container
            View root = inflater.inflate(layoutId, container, false);
            ScreenAdapterTools.getInstance().loadView(root);
            initWidget(root);
            initData();
            mRoot = root;
        }
        return mRoot;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mFirstData) {
            mFirstData = false;

        }

    }

    protected void initWidget(View root) {

    }

    protected void initData() {

    }


}
