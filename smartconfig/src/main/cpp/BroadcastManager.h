#ifndef BROADCAST_MANAGER_H
#define BROADCAST_MANAGER_H

#include <string>
#include "Common.h"
#include "Thread.h"

/**
 * 配网结果
 */
typedef enum
{
	CONFIG_OK			= 0,	// 配网成功
	CONFIG_STOP			= 1,	// 主动停止配网
	CONFIG_TIMEOUT		= 2		// 配网超时
} ConfigResult;

class BroadcastManager : public Thread
{
public:
	virtual void loop();
	static BroadcastManager & instance();
	void sendBroadcast();
	void stopBroadcast();
	void continueBroadcast();
private:
	BroadcastManager();
	virtual ~BroadcastManager();
	bool m_flag;
	u32 m_count;
};

#endif // BROADCAST_MANAGER_H
