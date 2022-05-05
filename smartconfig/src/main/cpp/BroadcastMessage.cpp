#include "BroadcastMessage.h"
#include "ConfigManager.h"

BroadcastMessage::BroadcastMessage()
{
}

BroadcastMessage::~BroadcastMessage()
{
}

/**
 * 按照协议拼接数据
 */
u32 makeData(u8 pType, const u8 *opt, u16 optLength, u8 *&output)
{
	// 头部
	u32 headerLength = 7;
	u8 *header = (u8*)malloc(headerLength);
	header[0] = (u8) 0xAA;
	header[1] = (u8) 0xBB;
	header[2] = pType;
	header[3] = (u8) optLength;

	u8 *dataEncrypt = NULL;
	u32 encryptLength = 0;

	// 计算数据部的长度
	header[5] = (u8) (encryptLength >> 8 & 0xFF);
	header[6] = (u8) (encryptLength & 0xFF);

	// 计算校验和(数据长度 + 可选部 + 数据部)
	int checksum = (header[5] & 0xFF) + (header[6] & 0xFF);
	if (opt != NULL)
	{
		for (int i = 0; i < optLength; i++)
		{
			checksum += (opt[i] & 0xFF);
		}
	}

	header[4] = (u8) (checksum & 0xFF);
	// 数据拼接
	output = (u8*)malloc(headerLength + optLength + encryptLength);
	memcpy(output, header, headerLength);
	if (opt != NULL)
	{
		memcpy(output + headerLength, opt, optLength);
	}
	memcpy(output + headerLength + optLength, dataEncrypt, encryptLength);
	free(header);
	return headerLength + optLength + encryptLength;
}

int BroadcastMessage::getData(u16 serial, u8 *&output)
{
	// 可选部
	u8 optLength = 27;
	u8 *opt = (u8*)malloc(optLength);
	memcpy(opt, ConfigManager::instance().getAppType().c_str(), 5);
	memcpy(opt + 5, ConfigManager::instance().getAppId().c_str(), 12);
	memcpy(opt + 17, ConfigManager::instance().getUserId().c_str(), 10);
	int retLength = makeData((u8)6, opt, optLength, output);
	free(opt);
	return retLength;
}

bool BroadcastMessage::handle(const u8 *opt, u8 optLength, const u8 *data, u32 dataLength)
{
	if (NULL == opt || optLength < 1)
	{
		Log("广播消息返回异常，optLength：%d.\n", optLength);
		return false;
	}

	u16 code = (opt[0] << 8) + opt[1];
	if (code != 0)
	{
		Log("广播返回数据有误：Code：%d.\n", code);
		return false;
	}
	Log("广播包数据返回成功！\n");
	return true;
}
