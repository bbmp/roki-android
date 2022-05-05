// ICvteSystemGuardian.aidl
package com.cvte.mid.systemguardian;


// Declare any non-default types here with import statements

interface ICvteSystemGuardian {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
     * Turn on or turn off launcher app watching feature
     */
    void setAppWatcherOnOff(boolean on);

	/**
     * Set the time before which wather will reboot the launcher app if it is not feed.
     * Default is 30s. The value will store in flash
     */
	void setAppWatcherOvertime(int seconds);

	/**
	 * Get tthe time before which wather will reboot the launcher app if it is not feed.
	 */
	int getAppWatcherOvertime();

	/**
	 * Get if the watcher is working
	 */
	boolean getAppWatcherOnOff();

	/*
	 * Feed the watcher, prevent it from rebooting the launcher app
	 * This will make watcher work if it is not.
	 */
	void feedAppWatcher();
}
