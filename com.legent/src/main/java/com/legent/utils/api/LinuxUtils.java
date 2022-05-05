package com.legent.utils.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.util.Log;

public class LinuxUtils {

	final static String TAG = "linux";

	/*
	 * ****************************************************************
	 * 子函数：获得本地MAC地址
	 * ****************************************************************
	 */
	static public String getMacAddress() {
		String result = "";
		String Mac = "";
		result = callCmd("busybox ifconfig", "HWaddr");

		// 如果返回的result == null，则说明网络不可取
		if (result == null) {
			return "网络出错，请检查网络";
		}

		// 对该行数据进行解析
		// 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
		if (result.length() > 0 && result.contains("HWaddr") == true) {
			Mac = result.substring(result.indexOf("HWaddr") + 6,
					result.length() - 1);
			Log.i(TAG, "Mac:" + Mac + " Mac.length: " + Mac.length());

			if (Mac.length() > 1) {
				Mac = Mac.replaceAll(" ", "");
				result = "";
				String[] tmp = Mac.split(":");
				for (int i = 0; i < tmp.length; ++i) {
					result += tmp[i];
				}
			}
			Log.i(TAG, result + " result.length: " + result.length());
		}
		return result;
	}

	static public String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			// 执行命令cmd，只取结果中含有filter的这一行
			while ((line = br.readLine()) != null
					&& line.contains(filter) == false) {
				// result += line;
				Log.i(TAG, "line: " + line);
			}

			result = line;
			Log.i(TAG, "result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
