package com.legent.services;

import android.os.Handler;
import android.os.Looper;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.legent.IAsyncTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskService extends AbsService {

	final static int ScheduledThreadPoolSize = 20;
	static private TaskService instance = new TaskService();

	synchronized static public TaskService getInstance() {
		return instance;
	}

	public Handler handler;
	public ListeningExecutorService service;
	public ScheduledExecutorService scheduledService;

	private TaskService() {
		handler = new Handler(Looper.getMainLooper());

		service = MoreExecutors.listeningDecorator(Executors
				.newCachedThreadPool());

		scheduledService = Executors
				.newScheduledThreadPool(ScheduledThreadPoolSize);

	}

	@Override
	public void dispose() {
		super.dispose();

		if (!service.isShutdown()) {
			service.shutdown();
			try {
				service.awaitTermination(200, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				service.shutdownNow();
			} finally {
				service = null;
			}
		}

		if (!scheduledService.isShutdown()) {
			scheduledService.shutdown();
			try {
				scheduledService.awaitTermination(200, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				scheduledService.shutdownNow();
			} finally {
				scheduledService = null;
			}
		}
	}

	public void postUiTask(Runnable r) {

		handler.post(r);
	}

	public void postUiTask(Runnable r, long delayMillis) {

		handler.postDelayed(r, delayMillis);
	}

	public void removeUiTask(Runnable r) {
		handler.removeCallbacks(r);
	}

	public <Result> ListenableFuture<Result> postAsyncTask(
			AbsTask<Result> asyncTask, Object... params) {
		return asyncTask.execute(params);
	}

	public <Result> ListenableFuture<Result> postAsyncTask(
			final IAsyncTask<Result> asyncTask, Object... params) {
		AbsTask<Result> callable = new AbsTask<Result>() {

			@Override
			public Result doInBackground(Object... params) throws Exception {
				return asyncTask.doInBackground(params);
			}

			@Override
			public void onPreExecute() {
				asyncTask.onPreExecute();
			}

			@Override
			public void onSuccess(Result result) {
				asyncTask.onSuccess(result);
			}

			@Override
			public void onFailure(Throwable e) {
				asyncTask.onFailure(e);
			}

		};

		return callable.execute(params);
	}

	public ScheduledFuture<?> schedule(Runnable runnable, long period,
			TimeUnit unit) {
		return scheduledService.schedule(runnable, period, unit);
	}


	private ScheduledFuture<?> scheduledFuture;

	/**
	 * 以固定周期频率执行任务 当执行任务的时间大于我们指定的间隔时间时，它并不会在指定间隔时开辟一个新的线程并发执行这个任务。而是等待该线程执行完毕。
	 *
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable,
			long initialDelay, long period, TimeUnit unit) {
		return scheduledService.scheduleAtFixedRate(runnable, initialDelay,
					period, unit);


	}

	/**
	 * 以固定延迟时间进行执行
	 *
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable,
			long initialDelay, long delay, TimeUnit unit) {

		return scheduledService.scheduleWithFixedDelay(runnable, initialDelay,
				delay, unit);
	}

	public ScheduledFuture<?> scheduleTaskAfterDelay(Runnable runnable, long delay, TimeUnit unit){

		return scheduledService.schedule(runnable,delay,unit);
	}
}
