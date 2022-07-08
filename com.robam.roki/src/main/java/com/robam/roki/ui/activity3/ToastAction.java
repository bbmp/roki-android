package com.robam.roki.ui.activity3;

import androidx.annotation.StringRes;

import com.hjq.toast.ToastUtils;

/**
 *    desc   : 吐司意图
 * @author r210190
 */
public interface ToastAction {

    default void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    default void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    default void toast(Object object) {
        ToastUtils.show(object);
    }
}