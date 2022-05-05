package com.legent.ui;


import android.content.Context;

public class DefaultMenuListener implements IMenuListener {

	private static DefaultMenuListener instance = new DefaultMenuListener();

	synchronized public static DefaultMenuListener getInstance() {
		return instance;
	}

	private DefaultMenuListener() {
	}

	@Override
	public void onMenuClick(Context cx, String menuKey) {

		String pageKey = menuKey;
		UIService.getInstance().postPage(pageKey, null);
	}

}
