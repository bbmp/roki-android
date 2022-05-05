#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include "ConfigManager.h"

#define CONFIG_BROADCAST_PORT   7683			// 配网广播包端口号

ConfigManager::ConfigManager()
{
	m_addOver = false;
	m_stopConfig = false;
	m_sleepTime = 1;
	m_jniFlag = 2;
	m_notify = NULL;
}

ConfigManager::~ConfigManager()
{
}

ConfigManager & ConfigManager::instance()
{
	static ConfigManager instance;
	return instance;
}

string ConfigManager::getAppType()
{
	return m_appType;
}

string ConfigManager::getAppId()
{
	return m_appId;
}

string ConfigManager::getUserId()
{
	return m_userId;
}

void ConfigManager::stop()
{
	Thread::stop();
	m_addOver = true;
}

/**
 * 开始配网
 */
void ConfigManager::startConfig(string ssid, string pwd, string appType, string appId, string userId, Callback callback)
{
	m_addOver = false;
	m_stopConfig = false;
	m_ssid = ssid;
	m_password = pwd;
	m_appType = appType;
	m_appId = appId;
	m_userId = userId;
	m_notify = callback;
	start();
	BroadcastManager::instance().start();
	Log("开始配网SSID:%s, 密码：%s, AppType:%s, AppId:%s, UserId:%s.", ssid.c_str(), pwd.c_str(), appType.c_str(), appId.c_str(), userId.c_str());
}

/**
 * 停止配置需要做的事情
 */
void ConfigManager::stopConfig(ConfigResult result)
{
	Log("停止配网.");
	m_stopConfig = true;
	m_result = result;
}

/**
 * 安全停止
 */
void ConfigManager::safeStop()
{
	if (!m_stopConfig)
	{
		return;
	}
	m_addOver = true;
	stop();
	BroadcastManager::instance().stop();
	if (m_notify != NULL)
	{
		m_notify(m_result);
	}
}

void ConfigManager::loop()
{
	if (m_init != NULL)
	{
		m_init(m_jniFlag);
		m_init = NULL;
	}

	// 交替发送多播和广播包
	u8 data[512] = {0};
	u8 length = makeData(data);
	long startTime = time(NULL);
	int index = 0;
	while (!m_addOver)
	{
		safeStop();
		if(time(NULL) - startTime > 180)
		{
			Log("搜索设备超时");
			stopConfig(CONFIG_TIMEOUT);
			break;
		}

		if(index % 3 == 0)
		{
			//Log("添加设备之广播包");
			sendBroadcast(data, length);
			usleep(300000);
		}
		else
		{
			//Log("添加设备之多播包");
			sendMuiltcast(data, length);
			usleep(300000);
		}
		index++;
	}
}

/**
 * 发送配网广播包
 */
void ConfigManager::sendBroadcast(u8 * result, u8 length)
{
	// UDP发送
	int sd = socket(AF_INET, SOCK_DGRAM, 0);
	int on = 1;
	setsockopt(sd, SOL_SOCKET, SO_BROADCAST, &on ,sizeof(on));
	struct sockaddr_in addr = {0};
	addr.sin_family = AF_INET;
	addr.sin_port = htons(CONFIG_BROADCAST_PORT);
	addr.sin_addr.s_addr = inet_addr("255.255.255.255");

	sendto(sd, result, 0, 0, (struct sockaddr *)&addr, sizeof(addr));
	sendto(sd, result, 0, 0, (struct sockaddr *)&addr, sizeof(addr));
	sendto(sd, result, 0, 0, (struct sockaddr *)&addr, sizeof(addr));

	unsigned char data[255] = {0};
	for(int i = 0; i <= length; i++)
	{
		if (i > 63)
		{
			continue;
		}

		unsigned char tmp[3] = {0};
		if (i == 0)
		{
			getPacket(length, 1, tmp);
		}
		else
		{
			getPacket(result[i - 1], (u8) (i + 1), tmp);
		}

		int ret = (int)sendto(sd, data, tmp[0], 0, (struct sockaddr *)&addr, sizeof(addr));
		if(ret < 0)
		{
			perror("Send to error");
			break;
		}
		usleep(10000);
		ret = (int)sendto(sd, data, tmp[1], 0, (struct sockaddr *)&addr, sizeof(addr));
		if(ret < 0)
		{
			perror("Send to error");
			break;
		}
		usleep(10000);
		ret = (int)sendto(sd, data, tmp[2], 0, (struct sockaddr *)&addr, sizeof(addr));
		if(ret < 0)
		{
			perror("Send to error");
			break;
		}
		usleep(10000);
	}
	close(sd);
}

/**
 * 发送配网多播包
 */
void ConfigManager::sendMuiltcast(u8 * result, u8 length)
{
	u8 naddr;
	//UDP发送
	int sd = socket(AF_INET, SOCK_DGRAM, 0);
	struct sockaddr_in addr = {0};
	addr.sin_family = AF_INET;
	addr.sin_port = htons(1234);

	size_t i;
	for(i = 0; i < length; i += 2)
	{
		if(i == 0)
		{
			//234.0x7e.len.0x55, 发送长度，0x7e,0x55用来定位长度包
			naddr = (u8)((234 << 24) | (0x7e << 16) | (length << 8) | 0xaa);
			addr.sin_addr.s_addr = htonl(naddr);
			sendto(sd, result, 1, 0, (struct sockaddr *)&addr, sizeof(addr));
			usleep(1000);
		}

		//234.i.text[i].0x55, 逐字节发送文本，0x55用来校验
		naddr = (u8)((234 << 24) | (i << 16) | (result[i] << 8) | result[i + 1]);
		addr.sin_addr.s_addr = htonl(naddr);
		sendto(sd, result, 1, 0, (struct sockaddr *)&addr, sizeof(addr));
		usleep(10000);
	}
	close(sd);
}

/**
 * 生产三个数据包，第一个是包序号，第二个是包内容，第三个是包校验
 */
void ConfigManager::getPacket(u8 data, u8 pos, u8 * result)
{
	u8 position = pos;
	if (data > 128)
	{
		position = (u8) ((1 << 6) + pos);
	}

	result[0] = position;

	if (data < 128)
	{
		data += 128;
	}
	result[1] = data;
	result[2] = (u8) ((1 << 7) | ((position ^ data) & 0x7f));
}

/**
 * 拼接要发送的数据内容
 */
u8 ConfigManager::makeData(u8 * result)
{
	u8 index = 0;
	//Log("添加设备之广播包:%s", m_password.c_str());
	memcpy(result + index, m_password.c_str(), m_password.length());
	index += m_password.length();
	result[index++] = '\0';
	memcpy(result + index, m_ssid.c_str(), m_ssid.length());
	index += m_ssid.length();
	result[index++] = '\0';

	index++;

	for(int i = 0; i < index - 1; i++)
	{
		result[index - 1] ^= result[i];
	}
	return index;
}
