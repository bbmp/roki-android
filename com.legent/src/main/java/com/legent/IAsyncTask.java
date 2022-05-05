package com.legent;

/**
 * 异步任务接口
 *
 * @param <Result>
 * @author sylar
 */
public interface IAsyncTask<Result> extends Callback<Result> {

    /**
     * 前台预处理
     */
    void onPreExecute();

    /**
     * 后台执行耗时任务
     *
     * @param params
     * @return Result
     * @throws Exception
     */
    Result doInBackground(Object... params) throws Exception;
}