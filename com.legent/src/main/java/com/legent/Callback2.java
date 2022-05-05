package com.legent;

/**
 * 回调接口，完成时通知
 *
 * @param <Result>
 * @author sylar
 */
public interface Callback2<Result> {

    /**
     * 完成时通知
     *
     * @param result
     */
    void onCompleted(Result result);
}