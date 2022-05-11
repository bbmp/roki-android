package com.legent.plat.services;


import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.DeviceGroupInfo;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.Payload;
import com.legent.services.AbsService;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/7/24.
 */
abstract public class AbsDeviceCloudService extends AbsService{

    // -------------------------------------------------------------------------------
    // restful service start
    // -------------------------------------------------------------------------------

    public void getDeviceGroups(long userId,
                                final Callback<List<DeviceGroupInfo>> callback) {
        CloudHelper.getDeviceGroups(userId, callback);
    }

    public void addDeviceGroup(long userId, String groupName,
                               Callback<Long> callback) {
        CloudHelper.addDeviceGroup(userId, groupName, callback);
    }

    public void deleteDeviceGroup(long userId, long groupId,
                                  VoidCallback callback) {
        CloudHelper.deleteDeviceGroup(userId, groupId, callback);
    }

    public void updateDeviceGroupName(long userId, long groupId,
                                      String groupName, VoidCallback callback) {
        CloudHelper.updateDeviceGroupName(userId, groupId, groupName, callback);
    }

    public void addDeviceToGroup(long userId, long groupId, String guid,
                                 VoidCallback callback) {
        CloudHelper.addDeviceToGroup(userId, groupId, guid, callback);
    }

    public void deleteDeviceFromGroup(long userId, long groupId, String guid,
                                      VoidCallback callback) {
        CloudHelper.deleteDeviceFromGroup(userId, groupId, guid, callback);
    }

    public void clearDeviceByGroup(long userId, long groupId,
                                   VoidCallback callback) {
        CloudHelper.clearDeviceByGroup(userId, groupId, callback);
    }

    public void getDevices(long userId, final Callback<List<DeviceInfo>> callback) {
        CloudHelper.getDevices(userId, callback);
    }

    public void getDeviceById(long userId, String guid,
                              final Callback<DeviceInfo> callback) {
        CloudHelper.getDeviceById(guid, callback);
    }

    public void getDeviceBySn(String sn, Callback<DeviceInfo> callback) {
        CloudHelper.getDeviceBySn(sn, callback);
    }

    public void updateDeviceName(long userId, String guid, String name,
                                 VoidCallback callback) {
        CloudHelper.updateDeviceName(userId, guid, name, callback);
    }

    public void bindDevice(long userId, final String guid, String name,
                           boolean isOwner, final VoidCallback callback) {
        CloudHelper.bindDevice(userId, guid, name, isOwner, callback);
    }

    public void unbindDevice(long userId, final String guid,
                             final VoidCallback callback) {
        CloudHelper.unbindDevice(userId, guid, callback);
    }

    public void getSnForDevice(long userId, String guid,
                               Callback<String> callback) {
        CloudHelper.getSnForDevice(userId, guid, callback);
    }

    public void getDeviceUsers(long userId, String guid,
                               Callback<List<User>> callback) {
        CloudHelper.getDeviceUsers(userId, guid, callback);
    }

    public void deleteDeviceUsers(long userId, String guid, List<Long> userIds,
                                  VoidCallback callback) {
        CloudHelper.deleteDeviceUsers(userId, guid, userIds, callback);
    }

    public void getDeviceByParams(long userId, String deviceType, String deviceCategory,
                                  Callback<Reponses.DeviceResponse> callback) {
        CloudHelper.getDeviceByParams(userId, deviceType, deviceCategory, callback);
    }


    public void getAllDeviceErrorInfo(Callback<Reponses.ErrorInfoResponse> callback) {
        CloudHelper.getAllDeviceErrorInfo(callback);
    }

    // -------------------------------------------------------------------------------
    // restful service end
    // -------------------------------------------------------------------------------


    public void getQueryDeviceReact(String deviceGuid, Callback<Reponses.QueryDeviceReact> callback) {
        CloudHelper.DeviceReactQuery(deviceGuid, callback);
    }

    public void setAllDeviceLinkage(String deviceGuid, Map<String, Payload> payloadMap, Callback<Reponses.SetDeviceLinkage> callback) {
        CloudHelper.setLinkage(deviceGuid, payloadMap, callback);
    }

    public void setAllDeviceLinkage(String deviceGuid,boolean enable,Callback<Reponses.SetDeviceLinkage>callback){
        CloudHelper.setAllDeviceLinkage(deviceGuid,enable,callback);
    }

    public void setOpenLinkage(String deviceGuid,boolean enable,int delaytime,Callback<Reponses.OpenDeviceResponse>callback){
        CloudHelper.setOpenLinkage(deviceGuid,enable,delaytime,callback);
    }

    public void getQueryDeviceReactSQ235(String deviceGuid, Callback<Reponses.QueryDeviceReact> callback) {
        CloudHelper.DeviceReactQuery(deviceGuid, callback);
    }

    public void setAllDeviceLinkageSQ235(String deviceGuid, Map<String,Payload> payloadMap, Callback<Reponses.SetDeviceLinkage> callback) {
        CloudHelper.setLinkage(deviceGuid, payloadMap, callback);
    }
}
