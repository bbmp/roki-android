package com.legent.utils.api;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class ApiUtils {

	final static String OEM_ANDROID_ID = "9774d56d682e549c";

	/**
	 * 获取设备唯一标识符
	 * 
	 */
	public static String getClientId(Context cx) {

		String token = getMacByShellCmd();

		if (Strings.isNullOrEmpty(token))
			token = WifiUtils.getLocalMac(cx);

		if (Strings.isNullOrEmpty(token))
			token = NetworkUtils.getLocalMac();

		if (Strings.isNullOrEmpty(token))
			token = LinuxUtils.getMacAddress();

		if (Strings.isNullOrEmpty(token))
			token = Settings.System.getString(cx.getContentResolver(),
					Settings.System.ANDROID_ID);

		if (Strings.isNullOrEmpty(token)
				|| Objects.equal(token, OEM_ANDROID_ID))
			token = android.os.Build.SERIAL;

		return token;
	}


	public static String getNewClientId(Context cx){
		try {
			List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface nif : all) {
				if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

				byte[] macBytes = nif.getHardwareAddress();
				if (macBytes == null) {
					return "";
				}

				StringBuilder res1 = new StringBuilder();
				for (byte b : macBytes) {
					res1.append(String.format("%02X:", b));
				}

				if (res1.length() > 0) {
					res1.deleteCharAt(res1.length() - 1);
				}
				return res1.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "02:00:00:00:00:00";
	}

	/**
	 * 判断当前应用运行在前台还是后台
	 * 
	 */
	public static boolean isBackground(Context cx) {

		ActivityManager am = (ActivityManager) cx
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(cx.getPackageName())) {
				return appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
			}
		}
		return false;
	}

	/**
	 * 获取所有能分享的APP
	 * 
	 */
	static public List<ResolveInfo> getShareTargets(Context cx) {
		List<ResolveInfo> mApps = Lists.newArrayList();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("image/*"); // "text/plain"
		PackageManager pm = cx.getPackageManager();
		mApps = pm.queryIntentActivities(intent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}

	/**
	 * market://details?id=<java包名> market://search?q=pname:<java包名>
	 * market://search?q=pub:<开发者名称> market://search?q=<关键词>
	 * 
	 */

	/**
	 * 跳转进市场
	 * 
	 */
	static public void gotoAndroidMarket(Context cx) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse("market://details?id=" + cx.getPackageName());
		// Uri uri = Uri.parse("market://details?id=" + "com.tencent.mm");
		intent.setData(uri);
		cx.startActivity(intent);
	}

	/**
	 * 跳转进市场并搜索
	 * 
	 */
	static public void searchOnAndroidMarket(Context cx, String publisherName) {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(String.format("market://search?q=pub:%s",
				publisherName)));
		cx.startActivity(intent);
	}

	static String getMacByShellCmd() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (Exception ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

}
