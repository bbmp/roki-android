/*
 * Copyright © 2012-2013 LiuZhongnan. All rights reserved.
 * 
 * Email:qq81595157@126.com
 * 
 * PROPRIETARY/CONFIDENTIAL.
 */

package com.youku.service.download;

import java.util.HashMap;

/**
 * IDownload.视频缓存接口
 * 
 * @author 刘仲男 qq81595157@126.com
 * @version v3.5
 * @created time 2012-10-15 下午4:03:24
 */
public interface IDownload {

	/** 配置文件路径 /youku/offlinedata/ */
//	public static String FILE_PATH = "/videocache/offlinedata/";
//	public static final String FILE_PATH = "/videocache/offlinedata/";

	/** 缩略图名字 */
    String THUMBNAIL_NAME = "1.png";

	/** 配置文件名info */
    String FILE_NAME = "info";

	/** SD卡发生插拔操作的广播动作 */
    String ACTION_SDCARD_CHANGED = "com.youku.service.download.ACTION_SDCARD_CHANGED";

	/** SD卡路径切换后的广播动作 */
    String ACTION_SDCARD_PATH_CHANGED = "com.youku.service.download.ACTION_SDCARD_PATH_CHANGED";

	/** 需要刷新页面的广播动作 */
    String ACTION_THUMBNAIL_COMPLETE = "com.youku.service.download.ACTION_THUMBNAIL_COMPLETE";

	/** 创建下载文件：每当一个创建完毕的广播动作 */
    String ACTION_CREATE_DOWNLOAD_ONE_READY = "com.youku.service.download.ACTION_CREATE_DOWNLOAD_ONE_READY";

	/** 创建下载文件：全部创建完毕的广播动作 */
    String ACTION_CREATE_DOWNLOAD_ALL_READY = "com.youku.service.download.ACTION_CREATE_DOWNLOAD_ALL_READY";

	/** 创建下载文件：每当一个创建失败的广播动作 */
    String ACTION_CREATE_DOWNLOAD_ONE_FAILED = "com.youku.service.download.ACTION_CREATE_DOWNLOAD_ONE_FAILED";

	/** 下载完成的广播动作 */
    String ACTION_DOWNLOAD_FINISH = "com.youku.service.download.ACTION_DOWNLOAD_FINISH";

	/** 下载公用的notify_id */
    int NOTIFY_ID = 2046;

	/** 键-最后的消息taskid */
    String KEY_LAST_NOTIFY_TASKID = "download_last_notify_taskid";

	/** 是否需要奥刷新 */
    String KEY_CREATE_DOWNLOAD_IS_NEED_REFRESH = "isNeedRefresh";

	/**
	 * 是否存在该缓存
	 */
    boolean existsDownloadInfo(String videoId);

	/**
	 * 是否已下载完成
	 */
    boolean isDownloadFinished(String videoId);

	/**
	 * 获得本地下载的视频的相关信息
	 */
    DownloadInfo getDownloadInfo(String videoId);

	/**
	 * Returns 正在缓存的视频缓存列表
	 */
    HashMap<String, DownloadInfo> getDownloadingData();

	/**
	 * 开始下载任务
	 */
    void startDownload(String taskId);

	/**
	 * 暂停下载任务
	 */
    void pauseDownload(String taskId);

	/**
	 * 单个删除视频缓存
	 */
    boolean deleteDownloading(String taskId);

	/**
	 * 删除全部正在缓存的视频
	 */
    boolean deleteAllDownloading();

	/**
	 * 重新获取数据
	 */
    void refresh();

	/***
	 * 开始一个新的下载任务
	 */
    void startNewTask();

	void stopAllTask();

	/**
	 * 获得当前下载SD卡路径/mnt/sdcard
	 */
    String getCurrentDownloadSDCardPath();

	void setCurrentDownloadSDCardPath(String path);

	/**
	 * 能否在3G环境下下载
	 */
    boolean canUse3GDownload();

	void setCanUse3GDownload(boolean flag);

	boolean canUseAcc();

	/** P2P 开关，-1获取失败，0关闭，1开启 */
    void setP2p_switch(int value);

	String getAccPort();

	int getDownloadFormat();

	void setDownloadFormat(int format);

	int getDownloadLanguage();

	void setDownloadLanguage(int language);

}
