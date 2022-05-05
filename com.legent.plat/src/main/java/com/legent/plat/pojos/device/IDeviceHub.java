package com.legent.plat.pojos.device;

import com.legent.VoidCallback;

import java.util.List;

/**
 * Hub设备对象模型
 * Created by sylar on 15/7/22.
 */
public interface IDeviceHub{

    /**
     * 设备分组编码
     */
    long getGroupId();
    /**
     * 获取子设备   // by zhaiyuanyi 20151119
     */
    <T extends IDevice> T getChild();
    /**
     * 获取子设备,根据guid
     */
    <T extends IDevice> T getChild(String guid);

    /**
     * 获取子设备,根据索引
     */
    <T extends IDevice> T getChild(int index);

    /**
     * 获取子设备,根据设备类型编码
     */
    <T extends IDevice> T getChildByDeviceType(String deviceTypeId);

    /**
     * 获取子设备列表,根据设备类型编码
     */
    <T extends IDevice> List<T> getChildrenByDeviceType(String deviceTypeId);

    /**
     * 获取子设备列表
     */
    List<IDevice> getChildren();

    /**
     * 子设备列表变更
     */
    void onChildrenChanged(List<SubDeviceInfo> children);

    /**
     * 设置wifi模块路由信息
     */
    void setWifiParam(String wifiSsid, String wifiPwd, VoidCallback callback);

    /**
     * 设置设备的拥有者
     */
    void setOwnerId(long ownerId, VoidCallback callback);
}
