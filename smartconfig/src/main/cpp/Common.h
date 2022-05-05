#ifndef _COMMON_H_
#define _COMMON_H_

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <string>
#include <errno.h>
#include <unistd.h>
#ifndef WIN32
	#include <android/log.h>
#endif

typedef unsigned char      u8;
typedef unsigned short     u16;
typedef unsigned int       u32;

#ifndef WIN32
	#define Log(...) __android_log_print(ANDROID_LOG_INFO, "CloudSDK", __VA_ARGS__)
#else
	void WLog(const char * fmt, ...);
	#define Log printf("%s[%d]", __FUNCTION__, __LINE__); WLog
#endif

#endif

