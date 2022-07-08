package com.robam.roki.ui.fragment;

import com.robam.base.BaseFragment;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.ui.activity3.ToastAction;

import okhttp3.Call;

/**
 *    author : huxw
 *    time   : 2018/10/18
 *    desc   : Fragment 业务基类
 */
public abstract class AppFragment<A extends AppActivity> extends BaseFragment<A>
        implements ToastAction {


    /**
     * 显示加载对话框
     */
    public void showDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.showDialog();
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.hideDialog();
    }

}