package com.legent;

/**
 * 回调接口，通知成功或失败(成功时没有回传值)
 *
 * @author sylar
 */
public interface VoidCallback {

    /**
     * 成功时通知
     */
    void onSuccess();

    /**
     * 失败时通知
     *
     * @param t
     */
    void onFailure(Throwable t);

}