package com.legent.plat.io.device;

import android.util.Log;

import com.legent.LogTags;
import com.legent.io.protocols.AbsProtocol;
import com.legent.plat.Plat;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.io.device.msg.MsgKeys;
import com.legent.plat.io.device.msg.MsgParams;
import com.legent.plat.io.device.msg.MsgUtils;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.SubDeviceInfo;
import com.legent.utils.LogUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

abstract public class AbsPlatProtocol extends AbsProtocol {

    public final static String TAG = LogTags.TAG_IO;
    static public final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    static public final int BufferSize = 1024 * 2;
    static protected final int GUID_SIZE = DeviceGuid.GUID_LENGTH;

    // -------------------------------------------------------------------------------
    // extends by interface
    // -------------------------------------------------------------------------------

    protected void onEncodeMsg(ByteBuffer buf, Msg msg) throws Exception {
        int key = msg.getID();
        String str;
        String wifiSsid, wifiPwd, ownerId;

        switch (key) {

            case MsgKeys.SetWifiParamsAndOwner_Req:

                boolean isSetOwner = msg.optBoolean(MsgParams.IsSetOwner);
                boolean isSetWifi = msg.optBoolean(MsgParams.IsSetWifi);
                ownerId = msg.getString(MsgParams.OwnerId);
                wifiSsid = msg.getString(MsgParams.WifiSsid);
                wifiPwd = msg.getString(MsgParams.WifiPwd);

                buf.put(isSetOwner ? (byte) 1 : (byte) 0);
                buf.put(ownerId.getBytes());

                buf.put(isSetWifi ? (byte) 1 : (byte) 0);
                buf.put(MsgUtils.toByte(wifiSsid.getBytes(StandardCharsets.UTF_8).length));
                buf.put(MsgUtils.toByte(wifiPwd.getBytes(StandardCharsets.UTF_8).length));
                buf.put(wifiSsid.getBytes());
                buf.put(wifiPwd.getBytes());

                break;

            case MsgKeys.RemoveChildDevice_Req:
                str = msg.getString(MsgParams.Guid);
                buf.put(str.getBytes());
                break;

            case MsgKeys.GetWifiSignal_Req:
                // do nothing
                break;

            case MsgKeys.GetDevices_Req:
                // do nothing
                break;

            case MsgKeys.MakePair_Req:
                // do nothing
                break;

            default:
                try {
                    IAppMsgMarshaller parser = getMarshaller();
                    if (parser != null) {
                        byte[] bytes = parser.marshal(msg);
                        buf.put(bytes);
                    }
                } catch (Exception e) {
                    LogUtils.i("20170926","e:::::"+e.getMessage());
                    Log.e(TAG, "IAppMsgMarshaller onEncodeMsg error! msg:%s" + msg.toString());
                }
                break;
        }
    }

    protected void onDecodeMsg(Msg msg, byte[] payload) throws Exception {
        int key = msg.getID();
        int offset = 0;
        String guid, bid, mac, str;
        long ownerId;
        short devNum, bidLen;
        short rc;
        DeviceInfo device;
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i <payload.length ; i++) {
            list.add(payload[i]);
        }
        LogUtils.i("20180608","AbsPlat::"+list.toString());
        switch (key) {

            // -------------------------------------------------------------------------------
            // 通知类
            // -------------------------------------------------------------------------------

            case MsgKeys.CloudPush_Noti:
                str = MsgUtils.getString(payload, offset, payload.length);
                msg.put(MsgParams.PushContent, str);
                break;

            case MsgKeys.DeviceConnected_Noti:
                devNum = MsgUtils.getShort(payload[offset++]);

                str = MsgUtils.getString(payload, offset, 10);
                ownerId = Long.parseLong(str);
                offset += 10;

                mac = MsgUtils.getString(payload, offset, 12);
                offset += 12;

                device = parserDeviceInfo(devNum, msg, payload, offset);
                device.ownerId = ownerId;
                device.mac = mac;

                msg.put(MsgParams.DeviceInfo, device);

                break;

            case MsgKeys.DeviceActivated_Noti:
                guid = MsgUtils.getString(payload, offset, 17);
                offset += 17;

                str = MsgUtils.getString(payload, offset, 10);
                ownerId = Long.parseLong(str);
                offset += 10;

                bidLen = MsgUtils.getShort(payload[offset++]);
                bid = MsgUtils.getString(payload, offset, bidLen);
                offset += bidLen;

                device = parserDeviceInfo(guid, bid, ownerId);
                msg.put(MsgParams.DeviceInfo, device);

                break;

            // -------------------------------------------------------------------------------
            // 应答类
            // -------------------------------------------------------------------------------

            case MsgKeys.SetWifiParamsAndOwner_Rep:
                rc = MsgUtils.getShort(payload[offset++]);
                msg.put(MsgParams.RC, rc);
                break;

            case MsgKeys.RemoveChildDevice_Rep:
                rc = MsgUtils.getShort(payload[offset++]);
                msg.put(MsgParams.RC, rc);
                break;

            case MsgKeys.GetDevices_Rep:
                devNum = MsgUtils.getShort(payload[offset++]);

                try {
                    str = MsgUtils.getString(payload, offset, 10);
                    ownerId = Long.parseLong(str);
                } catch (Exception e) {
                    ownerId = 0;
                }
                offset += 10;

                mac = MsgUtils.getString(payload, offset, 12);
                offset += 12;

                device = parserDeviceInfo(devNum, msg, payload, offset);
                device.ownerId = ownerId;
                device.mac = mac;
                msg.put(MsgParams.DeviceInfo, device);

                break;

            case MsgKeys.GetWifiSignal_Rep:
                msg.put(MsgParams.WifiSignal,
                        MsgUtils.getShort(payload[offset++]));
                msg.put(MsgParams.WifiModule,
                        MsgUtils.getShort(payload, offset));
                break;

            case MsgKeys.MakePair_Rep:
                rc = MsgUtils.getShort(payload[offset++]);
                msg.put(MsgParams.RC, rc);
                break;
            case MsgKeys.ExitPair_Rep:
                rc = MsgUtils.getShort(payload[offset++]);
                msg.put(MsgParams.RC, rc);
                break;

            default:
                try {
                    IAppMsgMarshaller parser = getMarshaller();
                    if (parser != null) {
                        LogUtils.i("20180609","msg"+msg.getID());
                        parser.unmarshal(msg, payload);
                    }
                } catch (Exception e) {
                    LogUtils.i("20170925","e:"+e.getMessage()+" "+msg.getTopic()+" "+msg.toString());
                    Log.e(TAG, "IAppMsgMarshaller onDecodeMsg error! msg:%s" + msg.getID()+" info:"+msg.getTopic());
                }
                break;
        }
    }

    // private
    // -------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------
    // extends by interface
    // -------------------------------------------------------------------------------

    private IAppMsgMarshaller getMarshaller() {
        return Plat.dcMqtt.getMsgMarshaller();
    }

    private DeviceInfo parserDeviceInfo(String guid, String bid, long ownerId) {
        return new DeviceInfo(guid, bid, ownerId);
    }

    private DeviceInfo parserDeviceInfo(int devNumber, Msg msg, byte[] payload,
                                        int offset) {

        DeviceInfo device = new DeviceInfo();

        String guid, bid;
        short bidLen, ver, mcuType;
        boolean isConnected;
        SubDeviceInfo subDevice;

        for (int i = 0; i < devNumber; i++) {
            guid = MsgUtils.getString(payload, offset, 17);
            offset += 17;

            bidLen = MsgUtils.getShort(payload[offset++]);
            bid = MsgUtils.getString(payload, offset, bidLen);
            offset += bidLen;

            ver = MsgUtils.getShort(payload[offset++]);
            mcuType = MsgUtils.getShort(payload[offset++]);
            isConnected = MsgUtils.getShort(payload[offset++]) == 1;

            if (i == 0) {
                device.guid = guid;
                device.bid = bid;
                device.ver = ver;
            } else {
                subDevice = new SubDeviceInfo(guid, bid);
                subDevice.guid = guid;
                subDevice.bid = bid;
                subDevice.ver = ver;
                subDevice.mcuType = mcuType;
                subDevice.isConnected = isConnected;
                device.addSubDevice(subDevice);
            }
        }
        return device;
    }

}
