package com.legent;

/**
 * 回调接口，通知成功或失败(成功时没有回传值)
 *
 * @author sylar
 */
public interface VoidCallback3<T> {

    /**
     * 完成时通知
     */
    void onCompleted(T t);

}