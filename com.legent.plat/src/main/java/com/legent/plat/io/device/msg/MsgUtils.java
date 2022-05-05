package com.legent.plat.io.device.msg;

import com.legent.plat.io.device.mqtt.MqttProtocol;
import com.legent.utils.ByteUtils;
import com.legent.utils.security.Crc16Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MsgUtils {

    protected final static ByteOrder BYTE_ORDER = MqttProtocol.BYTE_ORDER;

    static public short getShort(byte b) {
        return ByteUtils.toShort(b);
    }

    static public short getShort(byte[] data, int startIndex) {
        short res = ByteUtils.toInt16(data, startIndex, BYTE_ORDER);
        return res;
    }

    static public int getInt(byte[] data, int startIndex) {
        int res = ByteUtils.toInt32(data, startIndex, BYTE_ORDER);
        return res;
    }

    static public int getIntInverse(byte[] data, int startIndex) {
        int res = ByteUtils.toInt32(data, startIndex, ByteOrder.BIG_ENDIAN);
        return res;
    }

    static public float getFloat(byte[] data, int startIndex) {
        float res = ByteUtils.toFloat(data, startIndex, BYTE_ORDER);
        return res;
    }

    static public long getLong(byte[] data, int startIndex) {
        long res = ByteUtils.toInt64(data, startIndex, BYTE_ORDER);
        return res;
    }

    static public double getDouble(byte[] data, int startIndex) {
        double res = ByteUtils.toDouble(data, startIndex, BYTE_ORDER);
        return res;
    }


    static public String getString(byte[] data, int startIndex, int length) {
        return ByteUtils.toString(data, startIndex, length);
    }

    static public byte toByte(short s) {
        return (byte) s;
    }

    static public byte toByte(int s) {
        return (byte) s;
    }

    static public short calcCrc(byte[] data) {

        short res = Crc16Utils.calcCrc16(data);
        return res;
    }

    static public short calcCrc2(byte[] data) {

        short res = Crc16Utils.calcCrc16(data);

        ByteBuffer buf = ByteBuffer.allocate(2).order(BYTE_ORDER);
        buf.putShort(res);

        byte[] bytes = new byte[2];
        bytes[0] = buf.get(1);
        bytes[1] = buf.get(0);

        res = ByteBuffer.wrap(bytes).order(BYTE_ORDER).getShort();

        return res;
    }
}
