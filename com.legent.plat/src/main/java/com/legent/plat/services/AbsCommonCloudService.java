package com.legent.plat.services;


import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.plat.pojos.ChatMsg;
import com.legent.services.AbsService;
import com.legent.utils.LogUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by sylar on 15/7/24.
 */
abstract public class AbsCommonCloudService extends AbsService {


    public String getAppGuid(String appType, String token,String phoneToken,String versionName) {
        return CloudHelper.getAppGuid(appType, token,phoneToken,versionName);
    }

    public void getAppGuid(String appType, String token,String phoneToken,String versionName,
                           Callback<String> callback) {
        CloudHelper.getAppGuid(appType, token,phoneToken,versionName, callback);
    }

    public void bindAppGuidAndUser(String appGuid, long userId,
                                   VoidCallback callback) {
        CloudHelper.bindAppGuidAndUser(appGuid, userId, callback);
    }

    public void unbindAppGuidAndUser(String appGuid, long userId,
                                     VoidCallback callback) {
        CloudHelper.unbindAppGuidAndUser(appGuid, userId, callback);
    }

    public void checkAppVersion(String appType, Callback<AppVersionInfo> callback) {
        CloudHelper.checkAppVersion(appType, callback);
    }

    public void reportLog(String appGuid, int logType, String log, VoidCallback callback) {
        CloudHelper.reportLog(appGuid, logType, log, callback);
    }

    public void getStartImages(String appType,
                               final Callback<List<String>> callback) {
        CloudHelper.getStartImages(appType, callback);
    }

    public void sendChatMsg(long userId, String msg,
                            final Callback<Reponses.ChatSendReponse> callback) {
        CloudHelper.sendChatMsg(userId, msg, callback);
    }

    public void getChatBefore(long userId, Date date, int count,
                              final Callback<List<ChatMsg>> callback) {
        CloudHelper.getChatBefore(userId, date, count, callback);
    }

    public void getChatAfter(long userId, Date date, int count,
                             final Callback<List<ChatMsg>> callback) {
        CloudHelper.getChatAfter(userId, date, count, callback);
    }

    public void isExistChatMsg(long userId, Date date,
                               final Callback<Boolean> callback) {
        CloudHelper.isExistChatMsg(userId, date, callback);
    }
}
