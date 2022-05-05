#ifndef _MESSAGE_
#define _MESSAGE_

#include <string>
#include <list>
#include <vector>
#include "Common.h"

using std::string;
using std::list;
using std::vector;

#pragma pack(1)
typedef struct _Header
{
	u16 magic;			// 固定起始头为AABB
	u8 pktype:5;		// 数据包类型
	u8 ack:1;			// 数据包是否需要回复
	u8 reserved:2;		// 预留
	u8 optLength;		// 可选部长度
	u8 checksum;		// 数据校验和
	u16 length;			// 数据部长度
} Header;

typedef struct _Package
{
	Header header;		// 包头部
	u8 *opt;			// 可选部数据
	u8 *data;			// 数据部数据
} Package;

/**
 * 数据部 加密
 */
typedef struct _PackData
{
	u16 number;			// 包序号
	u8 data[0];			// 包数据
} PackData;

/**
 * 消息基类
 */
class Message
{
public:
	Message();
	virtual ~Message();
	virtual int getData(u16 serial, u8 *&output);
	virtual bool handle(const u8 *opt, u8 optLength, const u8 *data, u32 dataLength);
};

#endif //_MESSAGE_
