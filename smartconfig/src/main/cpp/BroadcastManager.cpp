#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include "ConfigManager.h"
#include "BroadcastManager.h"
#include "BroadcastMessage.h"

#define DEVICE_PORT        		7680			// 设备广播监听端口号

BroadcastManager::BroadcastManager()
{
	m_flag = true;
	m_sleepTime = 500;
	m_count = 0;
	m_jniFlag = 0;
}

BroadcastManager::~BroadcastManager()
{
}

BroadcastManager & BroadcastManager::instance()
{
	static BroadcastManager instance;
	return instance;
}

/**
 * 配网时需要发送
 */
void BroadcastManager::sendBroadcast()
{
	int sock = socket(AF_INET, SOCK_DGRAM, 0);
	int on = 1;
	setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &on ,sizeof(on));
	setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &on ,sizeof(on));
	struct timeval tv_out = {1, 0};
	tv_out.tv_sec = 1;
	setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &tv_out, sizeof(tv_out));
	int recvLength = 100 * 1024;
	if(setsockopt(sock, SOL_SOCKET, SO_SNDBUF, (void *)&recvLength, sizeof(recvLength)) < 0)
	{
		perror("Get sockopt error!");
		close(sock);
		return;
	}

	BroadcastMessage msg;
	u8 * data = NULL;
	u8 retLength = (u8)msg.getData(0, data);
	if (data == NULL)
	{
		Log("广播包数据错误！");
		return;
	}

	struct sockaddr_in addr = {0};
	addr.sin_family = AF_INET;
	addr.sin_port = htons(DEVICE_PORT);
	addr.sin_addr.s_addr = inet_addr("255.255.255.255");
	//addr.sin_addr.s_addr = INADDR_BROADCAST;

	usleep(2000);
	for(int i = 0; i < 1; i++)
	{
		int ret = (int)sendto(sock, data, retLength, 0, (struct sockaddr *)&addr, sizeof(addr));
		if(ret < 0)
		{
			perror("Send broadcast message error!");
		}
		usleep(200000);
	}
	free(data);

	unsigned char buffer[512] = {0};
	struct sockaddr_in clientAddr;
	socklen_t size = sizeof(clientAddr);
	while(1)
	{
		memset(buffer, 0, 512);
		long ret = recvfrom(sock, buffer, 512, 0, (struct sockaddr *)&clientAddr, &size);
		if(ret <= 0)
		{
			break;
		}
		char *devIP = inet_ntoa(clientAddr.sin_addr);
		//Log("广播包回复IP：%s 端口：%d\n", devIP, ntohs(clientAddr.sin_port));

		if (!(buffer[0] == 0xAA && buffer[1] == 0xBB))
		{
			Log("广播包数据头错误.\n");
			continue;
		}

		if (buffer[2] != 11)
		{
			Log("广播命令码错误.\n");
			continue;
		}

		u8 optLength = buffer[3];
		u8 checksum  = buffer[4];

		u8 *opt = buffer + 7;
		// 计算数据校验和
		int localCheck = 0;
		for (int i = 0; i < optLength; i++)
		{
			localCheck += opt[i];
		}

		if (checksum != (localCheck & 0xFF))
		{
			Log("广播包数据校验和错误. %d  %d", checksum, localCheck & 0xFF);
			continue;
		}

		if (msg.handle(opt, optLength, NULL, 0))
		{
			ConfigManager::instance().stopConfig(CONFIG_OK);
			break;
		}
	}
	close(sock);
}

void BroadcastManager::stopBroadcast()
{
	m_flag = false;
}

void BroadcastManager::continueBroadcast()
{
	m_flag = true;
}

/**
 * 循环调用函数
**/
void BroadcastManager::loop()
{
	if (m_init != NULL)
	{
		m_init(m_jniFlag);
		m_init = NULL;
	}

	if (m_flag)
	{
		// 发送一次广播包
		sendBroadcast();
	}
}
