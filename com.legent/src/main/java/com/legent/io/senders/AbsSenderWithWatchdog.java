package com.legent.io.senders;

import com.legent.io.msgs.IMsg;
import com.legent.services.TaskService;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

abstract public class AbsSenderWithWatchdog<Msg extends IMsg> extends
		AbsSender<Msg> {

	protected final static long CHECK_PERIOD = 100 * 5;
	protected ScheduledFuture<?> watchdogFuture;

	public AbsSenderWithWatchdog() {

		watchdogFuture = TaskService.getInstance().scheduleWithFixedDelay(
				watchdogTask, 100, CHECK_PERIOD, TimeUnit.MILLISECONDS);
	}

	@Override
	public void dispose() {
		super.dispose();

		if (watchdogFuture != null) {
			watchdogFuture.cancel(true);
		}
	}

	protected Runnable watchdogTask = new Runnable() {

		@Override
		public void run() {
			checkTimeout();
		}
	};
}
