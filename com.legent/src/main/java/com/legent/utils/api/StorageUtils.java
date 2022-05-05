package com.legent.utils.api;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 
 <!-- 在SDCard中创建与删除文件权限 --> <uses-permission
 * android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/> <!--
 * 往SDCard写入数据权限 --> <uses-permission
 * android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>&nbsp;
 */
public class StorageUtils {

	/**
	 * 获取SD卡绝对路径
	 * 
	 */
	public static String getExternalStoragePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 获取App 图片缓存路径
	 * 
	 */
	public static String getImageCachPath(Context cx) {
		return String.format("%s/%s", getCachPath(cx), "image");
	}

	/**
	 * 获取App缓存路径
	 * 
	 */
	public static String getCachPath(Context cx) {

		File dir = getCachDir(cx);
		return dir.getPath();
	}

	/**
	 * 获取App缓存目录
	 * 
	 */
	public static File getCachDir(Context cx) {

		File file = null;
		if (StorageUtils.existSDCard()) {
			file = cx.getExternalCacheDir();
		}

		if (file == null) {
			file = cx.getCacheDir();
		}
		return file;
	}

	static public boolean existSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	static public long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	static public long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

}
