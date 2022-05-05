package com.cook.config;

public class ConfigManager {
	private static ConfigManager manager = new ConfigManager();

	private ConfigManager() {
	}

	public static ConfigManager instance() {
		return manager;
	}

	static {
		System.loadLibrary("SmartConfig");
	}

	/**
	 * 开始配网
	 */
	public native void startConfig(String ssid, String pwd, String appType, String appId, String userId, ConfigCallback callback);

	/**
	 * 停止配网
	 */
	public native void stopConfig();
}
