#include <jni.h>
#include <string>

#include "ConfigManager.h"
#include "BroadcastManager.h"

extern "C"
{
JavaVM* g_jvm = NULL;
JNIEnv* g_configEnv = NULL;
JNIEnv* g_broadcastEnv = NULL;
jobject g_callback;

/**
* 当线程启动时，回调该方法执行一些初始化任务
*/
void initJNI(int type)
{
	if (g_jvm != NULL)
	{
		if (type == 0)
		{
			g_broadcastEnv = NULL;
			g_jvm->AttachCurrentThread(&g_broadcastEnv, NULL);
			Log("初始化广播JNI.\n");
		}
		else if (type == 2)
		{
			g_configEnv = NULL;
			g_jvm->AttachCurrentThread(&g_configEnv, NULL);
			Log("初始化配网JNI.\n");
		}
	}
}

/**
 * 当需要销毁整个线程时，释放一些外部资源比如JNI
 */
void stopJNI(int type)
{
	if (g_jvm != NULL)
	{
		g_jvm->DetachCurrentThread();
		if (type == 0)
		{
			g_broadcastEnv = NULL;
			Log("关闭广播JNI.\n");
		}
		else if (type == 2)
		{
			g_configEnv = NULL;
			Log("关闭配网JNI.\n");
		}
	}
}

/**
 * 配网结果回调函数
 */
void onNotify(int code)
{
	if (g_configEnv == NULL || g_callback == NULL)
	{
		return;
	}
	jclass cbc = g_configEnv->GetObjectClass(g_callback);
	jmethodID mid = g_configEnv->GetMethodID(cbc, "onResult", "(I)V");
	if (mid == NULL)
	{
		Log("查询onResult方法失败！\n");
		return;
	}
	g_configEnv->CallVoidMethod(g_callback, mid, code);
	g_configEnv->DeleteLocalRef(cbc);
}

/**
* 开始配网
*/
JNIEXPORT void JNICALL Java_com_cook_config_ConfigManager_startConfig(JNIEnv* env, jobject obj, jstring ssid, jstring pwd, jstring appType, jstring appId, jstring userId, jobject callback)
{
	env->GetJavaVM(&g_jvm);
	g_callback = env->NewGlobalRef(callback);
	ConfigManager::instance().initCallback(initJNI, stopJNI);
	BroadcastManager::instance().initCallback(initJNI, stopJNI);
	string ssidStr = env->GetStringUTFChars(ssid, NULL);
	string pwdStr = env->GetStringUTFChars(pwd, NULL);
	string appTypeStr = env->GetStringUTFChars(appType, NULL);
	string appIdStr = env->GetStringUTFChars(appId, NULL);
	string userIdStr = env->GetStringUTFChars(userId, NULL);
	ConfigManager::instance().startConfig(ssidStr, pwdStr, appTypeStr, appIdStr, userIdStr, onNotify);
}

/**
* 停止配网
*/
JNIEXPORT void JNICALL Java_com_cook_config_ConfigManager_stopConfig(JNIEnv* env, jobject obj)
{
	ConfigManager::instance().stopConfig(CONFIG_STOP);
}
}

