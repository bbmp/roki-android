package com.legent.services;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.legent.IAsyncTask;

import java.util.concurrent.Callable;

abstract public class AbsTask<Result> implements IAsyncTask<Result> {

	protected boolean isRunning;
	protected Object[] params;
	protected Result result;
	protected Throwable t;
	private Object objLock = new Object();
	/**
	 * 后台任务执行体
	 */
	private Callable<Result> callable = new Callable<Result>() {

		@Override
		public Result call() throws Exception {
			try {
				isRunning = true;
				result = null;
				t = null;
				return doInBackground(params);
			} finally {
				isRunning = false;
			}
		}
	};
	/**
	 * 任务执行状态回调
	 */
	private FutureCallback<Result> callback = new FutureCallback<Result>() {

		@Override
		public void onFailure(final Throwable r) {
			TaskService.getInstance().postUiTask(new Runnable() {

				@Override
				public void run() {
					AbsTask.this.onFailure(new Exception(r));
				}
			});
		}

		public void onSuccess(final Result result) {
			TaskService.getInstance().postUiTask(new Runnable() {

				@Override
				public void run() {
					AbsTask.this.onSuccess(result);
				}
			});
		}

	};

	/**
	 * 后台线程上运行任务
	 */
	@Override
	abstract public Result doInBackground(Object... params) throws Exception;

	/**
	 * 预处理工作（主线程上），在执行后台任务前
	 */
	@Override
	public void onPreExecute() {
	}

	/**
	 * 后台任务完成，通知主线程
	 */
	@Override
	public void onSuccess(Result result) {
	}

	/**
	 * 后台任务异常结束，通知主线程
	 */
	@Override
	public void onFailure(Throwable t) {
		t.printStackTrace();
	}

	/**
	 * 任务运行状态
	 *
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * 开始执行任务
	 *
	 * @param params
	 */
	public ListenableFuture<Result> execute(Object... params) {
		if (isRunning)
			return null;

		this.params = params;

		// 预处理
		onPreExecute();

		// 装载任务
		ListenableFuture<Result> future = TaskService.getInstance().service
				.submit(callable);

		// 设置监控回调
		com.google.common.util.concurrent.Futures.addCallback(future, callback );
		return future;
	}

	// -------------------------------------------------------------------------------
	// 以下为线程内同步的工具方法
	//
	//
	// -------------------------------------------------------------------------------

	protected void waitSelf(long timeout) throws InterruptedException {
		synchronized (objLock) {
			objLock.wait(timeout);
		}
	}

	protected void waitSelf() throws InterruptedException {
		synchronized (objLock) {
			objLock.wait();
		}
	}

	protected void notifySelf() {
		synchronized (objLock) {
			objLock.notify();
		}
	}

	protected void notifySelfOnSuccess(Result result) {
		this.result = result;
		notifySelf();
	}

	protected void notifySelfonFailure(Throwable e) {
		this.t = e;
		notifySelf();
	}

}
