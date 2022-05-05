package com.legent.plat.pojos.device;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.IKey;
import com.legent.plat.Plat;

import java.util.HashMap;
import java.util.Map;


public class DeviceGuid implements IKey<String> {

    public final static int VENDOR_LENGTH = 0;
    public final static int DEVICE_TYPE_LENGTH = 5;
    public final static int DEVICE_NUMBER_LENGTH = 12;
    public final static int GUID_LENGTH = VENDOR_LENGTH + DEVICE_TYPE_LENGTH + DEVICE_NUMBER_LENGTH;

    static public final String ZeroGuid = Strings.repeat("0", GUID_LENGTH);

    //add by liyuebiao
    private static Map<String, DeviceGuid> hashDeviceGuidMap = new HashMap<String, DeviceGuid>();

    static public DeviceGuid newGuid(String guid) {
        if (hashDeviceGuidMap.containsKey(guid)) {
            return hashDeviceGuidMap.get(guid);
        } else {
            hashDeviceGuidMap.put(guid, new DeviceGuid(guid));
            return hashDeviceGuidMap.get(guid);
        }

    }

    static public DeviceGuid getAppGuid() {
        return newGuid(Plat.appGuid);
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    /**
     * 设备guid
     */
    protected String guid;

    /**
     * 厂商
     */
    protected String vendor;

    /**
     * 设备类型编码
     */
    protected String devTypeId;


    /**
     * 设备编码
     */
    protected String devNum;

    private static int count = 0;

    private DeviceGuid(String guid) {


        Preconditions.checkNotNull(guid, "invalid guid");
        Preconditions.checkState(guid.length() == GUID_LENGTH,
                "invalid guid length:" + guid);

        this.guid = guid;
        this.vendor = getVendor(guid);
        this.devTypeId = getDeviceTypeId(guid);
        this.devNum = getDeviceNumber(guid);
    }

    /**
     * 获取厂商编码
     *
     * @return 厂商编码
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * 获取设备类型
     *
     * @return 设备类型
     */
    public String getDeviceTypeId() {
        return devTypeId;
    }

    /**
     * 获取设备序号
     *
     * @return 设备序号
     */
    public String getDeviceNumber() {
        return devNum;
    }

    /**
     * 获取设备唯一编码
     *
     * @return 设备唯一编码
     */
    public String getGuid() {
        return guid;
    }

    @Override
    public String getID() {
        return getGuid();
    }

    @Override
    public String toString() {
        return getGuid();
    }

    // -------------------------------------------------------------------------------
    //static
    // -------------------------------------------------------------------------------


    static String getVendor(String guid) {
        int venderLen = VENDOR_LENGTH;
        if (guid.length() == GUID_LENGTH) {
            venderLen = 0;
        }
        return guid.substring(0, venderLen);
    }


    static String getDeviceTypeId(String guid) {
        int venderLen = VENDOR_LENGTH;
        if (guid.length() == GUID_LENGTH) {
            venderLen = 0;
        }
        return guid.substring(venderLen, venderLen + DEVICE_TYPE_LENGTH);
    }

    static String getDeviceNumber(String guid) {
        int venderLen = VENDOR_LENGTH;
        if (guid.length() == GUID_LENGTH) {
            venderLen = 0;
        }
        return guid.substring(venderLen + DEVICE_TYPE_LENGTH);

    }

}
