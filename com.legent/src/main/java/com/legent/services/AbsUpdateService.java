package com.legent.services;

import android.content.Context;

abstract public class AbsUpdateService {

	protected Object[] params;

	public void start(Context cx, Object... params) {
		this.params = params;

		checkVersion(cx, new CheckVersionListener() {

			@Override
			public void onWithoutNewest() {
				onWithout();
			}

			@Override
			public void onWithNewest(String downUrl, Object... params) {
				onNewest(downUrl, params);
			}

			@Override
			public void onCheckFailure(Exception ex) {
				onFailure(ex);
			}
		});
	}

	protected void onNewest(String downUrl, Object... params) {
		// 有新版本
		download(downUrl);
	}

	protected void onWithout() {
		// 没有新版本
	}

	protected void onFailure(Exception ex) {
		// 检查版本出错
	}

	abstract public void checkVersion(Context cx, CheckVersionListener listener);

	abstract protected void download(String downUrl);

	public interface CheckVersionListener {
		void onWithNewest(String downUrl, Object... params);

		void onWithoutNewest();

		void onCheckFailure(Exception ex);
	}
}
