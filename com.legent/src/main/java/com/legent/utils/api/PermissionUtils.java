package com.legent.utils.api;

import com.legent.utils.ShellUtils;
import android.content.Context;
import android.content.pm.PackageManager;

public class PermissionUtils {

	/**
	 * 检查是否有权限
	 * 
	 * @param cx
	 * @param permission
	 * @return
	 */
	public static boolean checkPermission(Context cx, String permission) {
		int perm = cx.checkCallingOrSelfPermission(permission);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	
	/**
	 * 检查是否root
	 * @return
	 */
    public static boolean checkRootPermission() {
        return ShellUtils.execCommand("echo root", true, false).result == 0;
    }

	/**
	 * 访问登记属性，读取或写入登记check-in数据库属性表的权限
	 */
	final static public String ACCESS_CHECKIN_PROPERTIES = "android.permission.ACCESS_CHECKIN_PROPERTIES";

	/**
	 * 获取错略位置，通过WiFi或移动基站的方式获取用户错略的经纬度信息，定位精度大概误差在30~1500米
	 */
	final static public String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
	
	/**
	 * 后台下载组件，通过Http层进行文件的下载任务.
	 */
	final static public String ACCESS_DOWNLOAD_MANAGER = "android.permission.ACCESS_DOWNLOAD_MANAGER";

	/**
	 * 获取精确位置，通过GPS芯片接收卫星的定位信息，定位精度达10米以内
	 */
	final static public String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

	/**
	 * 访问定位额外命令，允许程序访问额外的定位提供者指令
	 */
	final static public String ACCESS_LOCATION_EXTRA_COMMANDS = "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS";

	/**
	 * 获取模拟定位信息，获取模拟定位信息，一般用于帮助开发者调试应用
	 */
	final static public String ACCESS_MOCK_LOCATION = "android.permission.ACCESS_MOCK_LOCATION";

	/**
	 * 获取网络状态，获取网络信息状态，如当前的网络连接是否有效
	 */
	final static public String ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";

	/**
	 * 访问Surface Flinger，Android平台上底层的图形显示支持，一般用于游戏或照相机预览界面和底层模式的屏幕截图
	 */
	final static public String ACCESS_SURFACE_FLINGER = "android.permission.ACCESS_SURFACE_FLINGER";

	/**
	 * 获取WiFi状态,获取当前WiFi接入的状态以及WLAN热点的信息
	 */
	final static public String ACCESS_WIFI_STATE = "android.permission.ACCESS_WIFI_STATE";

	/**
	 * 账户管理.获取账户验证信息，主要为GMail账户信息，只有系统级进程才能访问的权限
	 */
	final static public String ACCOUNT_MANAGER = "android.permission.ACCOUNT_MANAGER";

	/**
	 * 验证账户 允许一个程序通过账户验证方式访问账户管理ACCOUNT_MANAGER相关信息
	 */
	final static public String AUTHENTICATE_ACCOUNTS = "android.permission.AUTHENTICATE_ACCOUNTS";

	/**
	 * 电量统计 获取电池电量统计信息
	 */
	final static public String BATTERY_STATS = "android.permission.BATTERY_STATS";

	/**
	 * 绑定小插件 允许一个程序告诉appWidget服务需要访问小插件的数据库，只有非常少的应用才用到此权限
	 */
	final static public String BIND_APPWIDGET = "android.permission.BIND_APPWIDGET";

	/**
	 * 绑定设备管理 请求系统管理员接收者receiver，只有系统才能使用
	 */
	final static public String BIND_DEVICE_ADMIN = "android.permission.BIND_DEVICE_ADMIN";

	/**
	 * 绑定输入法 请求InputMethodService服务，只有系统才能使用
	 */
	final static public String BIND_INPUT_METHOD = "android.permission.BIND_INPUT_METHOD ";

	/**
	 * 绑定RemoteView 必须通过RemoteViewsService服务来请求，只有系统才能用
	 */
	final static public String BIND_REMOTEVIEWS = "android.permission.BIND_REMOTEVIEWS";

	/**
	 * 绑定壁纸 必须通过WallpaperService服务来请求，只有系统才能用
	 */
	final static public String BIND_WALLPAPER = "android.permission.BIND_WALLPAPER";

	/**
	 * 使用蓝牙 允许程序连接配对过的蓝牙设备
	 */
	final static public String BLUETOOTH = "android.permission.BLUETOOTH";

	/**
	 * 蓝牙管理 允许程序进行发现和配对新的蓝牙设备
	 */
	final static public String BLUETOOTH_ADMIN = "android.permission.BLUETOOTH_ADMIN";

	/**
	 * 变成砖头 能够禁用手机，非常危险，顾名思义就是让手机变成砖头
	 */
	final static public String BRICK = "android.permission.BRICK";

	/**
	 * 应用删除时广播 当一个应用在删除时触发一个广播
	 */
	final static public String BROADCAST_PACKAGE_REMOVED = "android.permission.BROADCAST_PACKAGE_REMOVED";

	/**
	 * 收到短信时广播 当收到短信时触发一个广播
	 */
	final static public String BROADCAST_SMS = "android.permission.BROADCAST_SMS";

	/**
	 * 连续广播 允许一个程序收到广播后快速收到下一个广播
	 */
	final static public String BROADCAST_STICKY = "android.permission.BROADCAST_STICKY";

	/**
	 * WAP PUSH广播 WAP PUSH服务收到后触发一个广播
	 */
	final static public String BROADCAST_WAP_PUSH = "android.permission.BROADCAST_WAP_PUSH";

	/**
	 * 拨打电话 允许程序从非系统拨号器里输入电话号码
	 */
	final static public String CALL_PHONE = "android.permission.CALL_PHONE";

	/**
	 * 通话权限 允许程序拨打电话，替换系统的拨号器界面
	 */
	final static public String CALL_PRIVILEGED = "android.permission.CALL_PRIVILEGED";

	/**
	 * 拍照权限 允许访问摄像头进行拍照
	 */
	final static public String CAMERA = "android.permission.CAMERA";

	/**
	 * 改变组件状态 改变组件是否启用状态
	 */
	final static public String CHANGE_COMPONENT_ENABLED_STATE = "android.permission.CHANGE_COMPONENT_ENABLED_STATE";

	/**
	 * 改变配置 允许当前应用改变配置，如定位
	 */
	final static public String CHANGE_CONFIGURATION = "android.permission.CHANGE_CONFIGURATION";

	/**
	 * 改变网络状态 改变网络状态如是否能联网
	 */
	final static public String CHANGE_NETWORK_STATE = "android.permission.CHANGE_NETWORK_STATE";

	/**
	 * 改变WiFi多播状态 改变WiFi多播状态
	 */
	final static public String CHANGE_WIFI_MULTICAST_STATE = "android.permission.CHANGE_WIFI_MULTICAST_STATE";

	/**
	 * 改变WiFi状态 改变WiFi状态
	 */
	final static public String CHANGE_WIFI_STATE = "android.permission.CHANGE_WIFI_STATE";

	/**
	 * 清除应用缓存 清除应用缓存
	 */
	final static public String CLEAR_APP_CACHE = "android.permission.CLEAR_APP_CACHE";

	/**
	 * 清除用户数据 清除应用的用户数据
	 */
	final static public String CLEAR_APP_USER_DATA = "android.permission.CLEAR_APP_USER_DATA";

	/**
	 * 底层访问权限 允许CWJ账户组访问底层信息
	 */
	final static public String CWJ_GROUP = "android.permission.CWJ_GROUP";

	/**
	 * 手机优化大师扩展权限 手机优化大师扩展权限
	 */
	final static public String CELL_PHONE_MASTER_EX = "android.permission.CELL_PHONE_MASTER_EX";

	/**
	 * 控制定位更新 允许获得移动网络定位信息改变
	 */
	final static public String CONTROL_LOCATION_UPDATES = "android.permission.CONTROL_LOCATION_UPDATES";

	/**
	 * 删除缓存文件 允许应用删除缓存文件
	 */
	final static public String DELETE_CACHE_FILES = "android.permission.DELETE_CACHE_FILES";

	/**
	 * 删除应用 允许程序删除应用
	 */
	final static public String DELETE_PACKAGES = "android.permission.DELETE_PACKAGES";

	/**
	 * 电源管理 允许访问底层电源管理
	 */
	final static public String DEVICE_POWER = "android.permission.DEVICE_POWER";

	/**
	 * 应用诊断 允许程序到RW到诊断资源
	 */
	final static public String DIAGNOSTIC = "android.permission.DIAGNOSTIC";

	/**
	 * 禁用键盘锁 允许程序禁用键盘锁
	 */
	final static public String DISABLE_KEYGUARD = "android.permission.DISABLE_KEYGUARD";

	/**
	 * 转存系统信息 允许程序获取系统dump信息从系统服务
	 */
	final static public String DUMP = "android.permission.DUMP";

	/**
	 * 状态栏控制 允许程序扩展或收缩状态栏
	 */
	final static public String EXPAND_STATUS_BAR = "android.permission.EXPAND_STATUS_BAR";

	/**
	 * 工厂测试模式 允许程序运行工厂测试模式
	 */
	final static public String FACTORY_TEST = "android.permission.FACTORY_TEST";

	/**
	 * 使用闪光灯 允许访问闪光灯
	 */
	final static public String FLASHLIGHT = "android.permission.FLASHLIGHT";

	/**
	 * 强制后退 允许程序强制使用back后退按键，无论Activity是否在顶层
	 */
	final static public String FORCE_BACK = "android.permission.FORCE_BACK";

	/**
	 * 访问账户Gmail列表 访问GMail账户列表
	 */
	final static public String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

	/**
	 * 获取应用大小 获取应用的文件大小
	 */
	final static public String GET_PACKAGE_SIZE = "android.permission.GET_PACKAGE_SIZE";

	/**
	 * 获取任务信息 允许程序获取当前或最近运行的应用
	 */
	final static public String GET_TASKS = "android.permission.GET_TASKS";

	/**
	 * 允许全局搜索 允许程序使用全局搜索功能
	 */
	final static public String GLOBAL_SEARCH = "android.permission.GLOBAL_SEARCH";

	/**
	 * 硬件测试 访问硬件辅助设备，用于硬件测试
	 */
	final static public String HARDWARE_TEST = "android.permission.HARDWARE_TEST";

	/**
	 * 注射事件 允许访问本程序的底层事件，获取按键、轨迹球的事件流
	 */
	final static public String INJECT_EVENTS = "android.permission.INJECT_EVENTS";

	/**
	 * 安装定位提供 安装定位提供
	 */
	final static public String INSTALL_LOCATION_PROVIDER = "android.permission.INSTALL_LOCATION_PROVIDER";

	/**
	 * 安装应用程序 允许程序安装应用
	 */
	final static public String INSTALL_PACKAGES = "android.permission.INSTALL_PACKAGES";

	/**
	 * 内部系统窗口 允许程序打开内部窗口，不对第三方应用程序开放此权限
	 */
	final static public String INTERNAL_SYSTEM_WINDOW = "android.permission.INTERNAL_SYSTEM_WINDOW";

	/**
	 * 访问网络 访问网络连接，可能产生GPRS流量
	 */
	final static public String INTERNET = "android.permission.INTERNET";

	/**
	 * 结束后台进程 允许程序调用killBackgroundProcesses(String).方法结束后台进程
	 */
	final static public String KILL_BACKGROUND_PROCESSES = "android.permission.KILL_BACKGROUND_PROCESSES";

	/**
	 * 管理账户 允许程序管理AccountManager中的账户列表
	 */
	final static public String MANAGE_ACCOUNTS = "android.permission.MANAGE_ACCOUNTS";

	/**
	 * 管理程序引用 管理创建、摧毁、Z轴顺序，仅用于系统
	 */
	final static public String MANAGE_APP_TOKENS = "android.permission.MANAGE_APP_TOKENS";

	/**
	 * 高级权限 允许mTweak用户访问高级系统权限
	 */
	final static public String MTWEAK_USER = "android.permission.MTWEAK_USER";

	/**
	 * 社区权限 允许使用mTweak社区权限
	 */
	final static public String MTWEAK_FORUM = "android.permission.MTWEAK_FORUM";

	/**
	 * 软格式化 允许程序执行软格式化,删除系统配置信息
	 */
	final static public String MASTER_CLEAR = "android.permission.MASTER_CLEAR";

	/**
	 * 修改声音设置 修改声音设置信息
	 */
	final static public String MODIFY_AUDIO_SETTINGS = "android.permission.MODIFY_AUDIO_SETTINGS";

	/**
	 * 修改电话状态 修改电话状态，如飞行模式，但不包含替换系统拨号器界面
	 */
	final static public String MODIFY_PHONE_STATE = "android.permission.MODIFY_PHONE_STATE";

	/**
	 * 格式化文件系统 格式化可移动文件系统，比如格式化清空SD卡
	 */
	final static public String MOUNT_FORMAT_FILESYSTEMS = "android.permission.MOUNT_FORMAT_FILESYSTEMS";

	/**
	 * 挂载文件系统 挂载、反挂载外部文件系统
	 */
	final static public String MOUNT_UNMOUNT_FILESYSTEMS = "android.permission.MOUNT_UNMOUNT_FILESYSTEMS";

	/**
	 * 允许NFC通讯 允许程序执行NFC近距离通讯操作，用于移动支持
	 */
	final static public String NFC = "android.permission.NFC";

	/**
	 * 永久Activity 创建一个永久的Activity，该功能标记为将来将被移除
	 */
	final static public String PERSISTENT_ACTIVITY = "android.permission.PERSISTENT_ACTIVITY";

	/**
	 * 处理拨出电话 允许程序监视，修改或放弃播出电话
	 */
	final static public String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";

	/**
	 * 读取日程提醒 允许程序读取用户的日程信息
	 */
	final static public String READ_CALENDAR = "android.permission.READ_CALENDAR";

	/**
	 * 读取联系人 允许应用访问联系人通讯录信息
	 */
	final static public String READ_CONTACTS = "android.permission.READ_CONTACTS";

	/**
	 * 屏幕截图 读取帧缓存用于屏幕截图
	 */
	final static public String READ_FRAME_BUFFER = "android.permission.READ_FRAME_BUFFER";

	/**
	 * 读取收藏夹和历史记录 读取浏览器收藏夹和历史记录
	 */
	final static public String READ_HISTORY_BOOKMARKS = "com.android.browser.permission.READ_HISTORY_BOOKMARKS";

	/**
	 * 读取输入状态，读取当前键的输入状态，仅用于系统
	 */
	final static public String READ_INPUT_STATE = "android.permission.READ_INPUT_STATE";

	/**
	 * 读取系统日志
	 */
	final static public String READ_LOGS = "android.permission.READ_LOGS";

	/**
	 * 读取电话状态
	 */
	final static public String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";

	/**
	 * 读取短信内容
	 */
	final static public String READ_SMS = "android.permission.READ_SMS";

	/**
	 * 读取同步设置 读取同步设置，读取Google在线同步设置
	 */
	final static public String READ_SYNC_SETTINGS = "android.permission.READ_SYNC_SETTINGS";

	/**
	 * 读取同步状态 读取同步状态，获得Google在线同步状态
	 */
	final static public String READ_SYNC_STATS = "android.permission.READ_SYNC_STATS";

	/**
	 * 重启设备
	 */
	final static public String REBOOT = "android.permission.REBOOT";

	/**
	 * 开机自动允许
	 */
	final static public String RECEIVE_BOOT_COMPLETED = "android.permission.RECEIVE_BOOT_COMPLETED";

	/**
	 * 接收彩信
	 */
	final static public String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

	/**
	 * 接收短信
	 */
	final static public String RECEIVE_SMS = "android.permission.RECEIVE_SMS";

	/**
	 * 接收Wap Push 接收WAP PUSH信息
	 */
	final static public String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";

	/**
	 * 录音 录制声音通过手机或耳机的麦克
	 */
	final static public String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

	/**
	 * 排序系统任务，重新排序系统Z轴运行中的任务
	 */
	final static public String REORDER_TASKS = "android.permission.REORDER_TASKS";

	/**
	 * 结束系统任务，结束任务通过restartPackage(String)方法，该方式将在外来放弃
	 */
	final static public String RESTART_PACKAGES = "android.permission.RESTART_PACKAGES";

	/**
	 * 发送短信
	 */
	final static public String SEND_SMS = "android.permission.SEND_SMS";

	/**
	 * 设置Activity观察器一般用于monkey测试
	 */
	final static public String SET_ACTIVITY_WATCHER = "android.permission.SET_ACTIVITY_WATCHER";

	/**
	 * 设置闹铃提醒，设置闹铃提醒
	 */
	final static public String SET_ALARM = "com.android.alarm.permission.SET_ALARM";

	/**
	 * 设置总是退出，设置程序在后台是否总是退出
	 */
	final static public String SET_ALWAYS_FINISH = "android.permission.SET_ALWAYS_FINISH";

	/**
	 * 设置动画缩放，设置全局动画缩放
	 */
	final static public String SET_ANIMATION_SCALE = "android.permission.SET_ANIMATION_SCALE";

	/**
	 * 设置调试程序，设置调试程序，一般用于开发
	 */
	final static public String SET_DEBUG_APP = "android.permission.SET_DEBUG_APP";

	/**
	 * 设置屏幕方向，设置屏幕方向为横屏或标准方式显示，不用于普通应用
	 */
	final static public String SET_ORIENTATION = "android.permission.SET_ORIENTATION";

	/**
	 * 设置应用参数，设置应用的参数，已不再工作具体查看addPackageToPreferred(String) 介绍
	 */
	final static public String SET_PREFERRED_APPLICATIONS = "android.permission.SET_PREFERRED_APPLICATIONS";

	/**
	 * 设置进程限制，允许程序设置最大的进程数量的限制
	 */
	final static public String SET_PROCESS_LIMIT = "android.permission.SET_PROCESS_LIMIT";

	/**
	 * 设置系统时间，设置系统时间
	 */
	final static public String SET_TIME = "android.permission.SET_TIME";

	/**
	 * 设置系统时区，设置系统时区
	 */
	final static public String SET_TIME_ZONE = "android.permission.SET_TIME_ZONE";

	/**
	 * 设置桌面壁纸，设置桌面壁纸
	 */
	final static public String SET_WALLPAPER = "android.permission.SET_WALLPAPER";

	/**
	 * 设置壁纸建议，设置壁纸建议
	 */
	final static public String SET_WALLPAPER_HINTS = "android.permission.SET_WALLPAPER_HINTS";

	/**
	 * 发送永久进程信号，发送一个永久的进程信号
	 */
	final static public String SIGNAL_PERSISTENT_PROCESSES = "android.permission.SIGNAL_PERSISTENT_PROCESSES";

	/**
	 * 状态栏控制 ，允许程序打开、关闭、禁用状态栏
	 */
	final static public String STATUS_BAR = "android.permission.STATUS_BAR";

	/**
	 * 访问订阅内容，访问订阅信息的数据库
	 */
	final static public String SUBSCRIBED_FEEDS_READ = "android.permission.SUBSCRIBED_FEEDS_READ";

	/**
	 * 写入订阅内容，写入或修改订阅内容的数据库
	 */
	final static public String SUBSCRIBED_FEEDS_WRITE = "android.permission.SUBSCRIBED_FEEDS_WRITE";

	/**
	 * 显示系统窗口，显示系统窗口
	 */
	final static public String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";

	/**
	 * 更新设备状态，更新设备状态
	 */
	final static public String UPDATE_DEVICE_STATS = "android.permission.UPDATE_DEVICE_STATS";

	/**
	 * 使用证书，允许程序请求验证从AccountManager
	 */
	final static public String USE_CREDENTIALS = "android.permission.USE_CREDENTIALS";

	/**
	 * 使用SIP视频，允许程序使用SIP视频服务
	 */
	final static public String USE_SIP = "android.permission.USE_SIP";

	/**
	 * 使用振动，允许振动
	 */
	final static public String VIBRATE = "android.permission.VIBRATE";

	/**
	 * 唤醒锁定，允许程序在手机屏幕关闭后后台进程仍然运行
	 */
	final static public String WAKE_LOCK = "android.permission.WAKE_LOCK";

	/**
	 * 写入GPRS接入点设置，写入网络GPRS接入点设置
	 */
	final static public String WRITE_APN_SETTINGS = "android.permission.WRITE_APN_SETTINGS";

	/**
	 * 写入日程提醒，写入日程，但不可读取
	 */
	final static public String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

	/**
	 * 写入联系人，写入联系人，但不可读取
	 */
	final static public String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";

	/**
	 * 写入外部存储，允许程序写入外部存储，如SD卡上写文件
	 */
	final static public String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

	/**
	 * 写入Google地图数据，允许程序写入Google Map服务数据
	 */
	final static public String WRITE_GSERVICES = "android.permission.WRITE_GSERVICES";

	/**
	 * 写入收藏夹和历史记录 ，写入浏览器历史记录或收藏夹，但不可读取
	 */
	final static public String WRITE_HISTORY_BOOKMARKS = "com.android.browser.permission.WRITE_HISTORY_BOOKMARKS";

	/**
	 * 读写系统敏感设置，允许程序读写系统安全敏感的设置项
	 */
	final static public String WRITE_SECURE_SETTINGS = "android.permission.WRITE_SECURE_SETTINGS";

	/**
	 * 读写系统设置，允许读写系统设置项
	 */
	final static public String WRITE_SETTINGS = "android.permission.WRITE_SETTINGS";

	/**
	 * 编写短信，允许编写短信
	 */
	final static public String WRITE_SMS = "android.permission.WRITE_SMS";

	/**
	 * 写入在线同步设置，写入Google在线同步设置
	 */
	final static public String WRITE_SYNC_SETTINGS = "android.permission.WRITE_SYNC_SETTINGS";

}
