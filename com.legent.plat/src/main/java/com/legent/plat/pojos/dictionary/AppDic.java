package com.legent.plat.pojos.dictionary;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.legent.Initialization;
import com.legent.io.channels.IChannel;
import com.legent.plat.io.device.IAppMsgMarshaller;
import com.legent.plat.io.device.IAppMsgSyncDecider;
import com.legent.plat.io.device.IAppNoticeReceiver;
import com.legent.plat.io.device.mqtt.MqttChannel;
import com.legent.plat.pojos.device.IDeviceFactory;
import com.legent.plat.pojos.dictionary.msg.AppMsgMarshaller;
import com.legent.plat.pojos.dictionary.msg.AppMsgSyncDecider;
import com.legent.plat.pojos.dictionary.msg.MsgDic;
import com.legent.plat.services.account.IAppOAuthService;
import com.legent.utils.ReflectUtils;
import com.legent.utils.api.ToastUtils;

public class AppDic extends PlatDic implements Initialization {

    @JsonProperty()
    public ServerOpt serverOpt;

    @JsonProperty()
    public CommOpt commOpt;

    @JsonProperty()
    public MsgDic msgDic;


    //==========================================================================

    @Override
    public void init(Context cx, Object... ps) {
        try {

            Preconditions.checkNotNull(commOpt, "CommOpt must be none-null");
            Preconditions.checkNotNull(msgDic, "MsgDic must be none-null");

            commOpt.init(cx);
            msgDic.init(cx);
        } catch (Exception e) {
            ToastUtils.showLong(e.getMessage());
            Preconditions.checkState(false, e);
        }
    }


    //==========================================================================

    public IChannel getAppChannel() {
        IChannel ch = ReflectUtils.getReflectObj(commOpt.implAppChannel);
        return ch != null ? ch : MqttChannel.getInstance();
    }

    public IDeviceFactory getDeviceFactory() {
        return ReflectUtils.getReflectObj(commOpt.implDeviceFactory);
    }

    public IAppMsgMarshaller getAppMsgMarshaller() {
        return new AppMsgMarshaller(msgDic);
    }

    public IAppMsgSyncDecider getAppMsgSyncDecider() {
        return new AppMsgSyncDecider(msgDic);
    }

    public IAppNoticeReceiver getAppNoticeReceiver() {
        return ReflectUtils.getReflectObj(commOpt.implAppNoticeReceiver);
    }

    public IAppOAuthService getAppOAuthService() {
        return ReflectUtils.getReflectObj(commOpt.implAppOAuthService);
    }

    //==========================================================================


    //==========================================================================

//    static public AppDic load(Context cx, int resid) {
//
//        loadPlatDic(cx);
//
//        AppDic dic = null;
//        String json = ResourcesUtils.raw2String(resid);
//
//        try {
//            dic = JsonUtils.json2Pojo(json, AppDic.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Preconditions.checkNotNull(dic, "加载 AppConfig 失败");
//
//        dic.init(cx);
//
//        //batchAdd
//        if (dic.deviceTypes != null) {
//            for (DeviceType dt : dic.deviceTypes) {
//                DeviceTypeManager.getInstance().add(dt);
//            }
//        }
//        if (dic.resultCodes != null) {
//            for (ResultCode rc : dic.resultCodes) {
//                ResultCodeManager.getInstance().add(rc);
//            }
//        }
//
//        return dic;
//    }


    //==========================================================================

}