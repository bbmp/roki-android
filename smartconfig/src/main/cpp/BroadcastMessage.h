#ifndef BROADCAST_MESSAGE_H
#define BROADCAST_MESSAGE_H

#include "Message.h"
/**
 * 广播包回复消息
**/
class BroadcastMessage : public Message
{
public:
	BroadcastMessage();
	virtual ~BroadcastMessage();
	virtual int getData(u16 serial, u8 *&output);
	virtual bool handle(const u8 *opt, u8 optLength, const u8 *data, u32 dataLength);
};

#endif // BROADCAST_MESSAGE_H
