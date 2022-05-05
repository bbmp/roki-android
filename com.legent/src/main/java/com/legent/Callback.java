package com.legent;

/**
 * 回调接口，通知成功或失败
 *
 * @param <Result>
 * @author sylar
 */
public interface Callback<Result> {

    /**
     * 成功时通知
     *
     * @param result
     */
    void onSuccess(Result result);

    /**
     * 失败时通知
     *
     * @param t
     */
    void onFailure(Throwable t);
}