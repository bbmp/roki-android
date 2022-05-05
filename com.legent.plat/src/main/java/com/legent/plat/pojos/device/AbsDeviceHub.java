package com.legent.plat.pojos.device;

import android.content.Context;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/7/22.
 */
abstract public class AbsDeviceHub extends AbsDevice implements IDeviceHub {

    public long ownerId;
    public String mac;
    protected long groupId;
    protected Map<String, IDevice> map = Maps.newHashMap();


    public AbsDeviceHub(DeviceInfo devInfo) {
        super(devInfo);

        ownerId = devInfo.ownerId;
        groupId = devInfo.groupId;
        mac = devInfo.mac;

        if (devInfo.subDevices != null) {
            for (SubDeviceInfo subDevice : devInfo.subDevices) {
                try {
                    IDevice dev = newSubDevice(subDevice);
                    Preconditions.checkNotNull(dev, "newSubDevice() return null.\n subDeviceInfo:" + subDevice.guid);
                    dev.setParent(this);
                    map.put(subDevice.guid, dev);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void init(Context cx, Object... params) {
        super.init(cx, params);

        if (map != null) {
            for (IDevice dev : map.values()) {
                dev.init(cx, params);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (map != null) {
            for (IDevice dev : map.values()) {
                dev.dispose();
            }
        }
    }

    public boolean isOwner() {
        return ownerId == getCurrentUserId();
    }

    protected IDevice newSubDevice(SubDeviceInfo subDevice) {
        return Plat.deviceFactory.generate(subDevice);
    }

    // -------------------------------------------------------------------------------
    // IDeviceHub
    // -------------------------------------------------------------------------------


    @Override
    public long getGroupId() {
        return groupId;
    }


    @Override
    public <T extends IDevice> T getChild(String guid) {
        return (T) map.get(guid);
    }

    @Override
    public <T extends IDevice> T getChild(int index) {
        if (index > 0 && index < map.size()) {
            return (T) Lists.newArrayList(map.values()).get(index);
        }
        return null;
    }

    @Override
    public <T extends IDevice> T getChildByDeviceType(String deviceTypeId) {
        List<T> list = getChildrenByDeviceType(deviceTypeId);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <T extends IDevice> List<T> getChildrenByDeviceType(String deviceTypeId) {
        List<T> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            if (DeviceTypeManager.getInstance().isInDeviceType(dev.getGuid(), deviceTypeId)) {
                list.add((T) dev);
            }
        }
        return list;
    }

    // by zhaiyuanyi 20151119
    @Override
    public <T extends IDevice> T getChild() {
        return getChildStove();
    }

    public <T extends IDevice> T getChildStove() {
        List<T> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            if (IDeviceType.RRQZ.equals(dev.getDc())) {
                list.add((T) dev);
            }
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public <T extends IDevice> List<T> getChildGas(){
        return getChildGasSensor();
    }

    public <T extends IDevice> List<T> getChildGasSensor(){
        List<T> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            LogUtils.i("20180606","dev:::"+dev.getDc());
            if (IDeviceType.RQCG.equals(dev.getDc()))
                list.add((T) dev);
        }
        LogUtils.i("20180606","list-size:"+list.size());
        return list;
    }

    public <T extends IDevice> T getChildPot() {
        List<T> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            if (IDeviceType.RZNG.equals(dev.getDc()))
                list.add((T) dev);
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    // by zhoudj 20170106
    public List<IDevice> getChildList() {
        List<IDevice> list = Lists.newArrayList();
        for (IDevice dev : map.values()) {
            list.add(dev);
        }
        return list;
    }


    @Override
    public List<IDevice> getChildren() {
        return Lists.newArrayList(map.values());
    }

    @Override
    public void onChildrenChanged(List<SubDeviceInfo> children) {

        // 先暂置无效标记
        for (IDevice dev : map.values()) {
            dev.setValid(false);
        }

        // 处理变更
        for (SubDeviceInfo devInfo : children) {
            if (map.containsKey(devInfo.guid)) {
                // 如果变更后仍存在，置为有效
                map.get(devInfo.guid).setValid(true);
            } else {
                // 变更后新增的设备
                IDevice dev = newSubDevice(devInfo);
                dev.setParent(this);
                map.put(devInfo.guid, dev);
            }
        }

        // 无效设备更改连接状态
        for (IDevice dev : map.values()) {
            if (!dev.getValid()) {
                dev.setConnected(false);
            }
        }
    }


    @Override
    public void setWifiParam(String wifiSsid, String wifiPwd,
                             VoidCallback callback) {
        if (Plat.dcMqtt != null)
            dcMqtt.setWifiParam(id, wifiSsid, wifiPwd, callback);

        dcMqtt.setWifiParam(id, wifiSsid, wifiPwd, callback);
    }

    @Override
    public void setOwnerId(long ownerId, VoidCallback callback) {
        if (Plat.dcMqtt != null)
            dcMqtt.setOwnerId(id, ownerId, callback);

        dcMqtt.setOwnerId(id, ownerId, callback);
    }


    // -------------------------------------------------------------------------------
    // IDeviceHub
    // -------------------------------------------------------------------------------
}
