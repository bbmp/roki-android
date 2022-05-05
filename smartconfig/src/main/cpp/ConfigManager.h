#ifndef CONFIG_MANAGER_H
#define CONFIG_MANAGER_H

#include <list>
#include <string>
#include "Thread.h"
#include "Common.h"
#include "BroadcastManager.h"

using std::string;

/**
 * 配网管理
 */
class ConfigManager : public Thread
{
public:
	static ConfigManager & instance();
	void startConfig(string ssid, string pwd, string appType, string appId, string userId, Callback callback);
	void stopConfig(ConfigResult result);
	virtual void loop();
	virtual void stop();
	string getAppType();
	string getAppId();
	string getUserId();
private:
	ConfigManager();
	virtual ~ConfigManager();
	void getPacket(u8 data, u8 pos, u8 * result);
	u8 makeData(u8 * result);
	void sendBroadcast(u8 * data, u8 length);
	void sendMuiltcast(u8 * result, u8 length);
	void safeStop();
private:
	string m_password;
	string m_ssid;
	string m_appType;
	string m_appId;
	string m_userId;
	bool m_stopConfig;
	bool m_addOver;
	Callback m_notify;
	ConfigResult m_result;
};

#endif // CONFIG_MANAGER_H
