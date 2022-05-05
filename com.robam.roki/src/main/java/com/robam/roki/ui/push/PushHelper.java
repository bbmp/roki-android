package com.robam.roki.ui.push;

import static com.robam.roki.ui.page.SelectThemeDetailPage.TYPE_THEME_BANNER;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.legent.plat.events.DeviceTokenEvent;
import com.legent.utils.EventUtils;
import com.robam.common.events.Detailevnet;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.HomePage;
import com.robam.roki.ui.page.SelectThemeDetailPage;
import com.robam.roki.ui.view.umpush.CustomNotificationHandler;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.api.UPushRegisterCallback;
import com.umeng.message.entity.UMessage;

import org.android.agoo.huawei.HuaWeiRegister;

import org.android.agoo.xiaomi.MiPushRegistar;

/**
 * PushSDK集成帮助类
 */
public class PushHelper {

    private static final String TAG = "PushHelper";

    /**
     * 预初始化
     */
    public static void preInit(Context context) {
        //解决厂商通知点击时乱码等问题
        PushAgent.setup(context, PushConstants.APP_KEY,
                PushConstants.MESSAGE_SECRET);
        UMConfigure.preInit(context, PushConstants.APP_KEY,
                PushConstants.CHANNEL);
    }

    private static Context mContext;
    /**
     * 初始化
     */
    public static void init(final Context context) {
        // 基础组件包提供的初始化函数，应用配置信息：http://message.umeng.com/list/apps
        // 参数一：上下文context；
        // 参数二：应用申请的Appkey；
        // 参数三：发布渠道名称；
        // 参数四：设备类型，UMConfigure.DEVICE_TYPE_PHONE：手机；UMConfigure.DEVICE_TYPE_BOX：盒子；默认为手机
        // 参数五：Push推送业务的secret，填写Umeng Message Secret对应信息
        UMConfigure.init(context, PushConstants.APP_KEY,
                PushConstants.CHANNEL,
                UMConfigure.DEVICE_TYPE_PHONE,
                PushConstants.MESSAGE_SECRET);

        mContext=context;
        //获取推送实例
        PushAgent pushAgent = PushAgent.getInstance(context);

        //TODO:需修改为您app/src/main/AndroidManifest.xml中package值
        pushAgent.setResourcePackageName("com.robam.roki");

        //推送设置
        pushSetting(context);

        //注册推送服务，每次调用register方法都会回调该接口
        pushAgent.register(new UPushRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG, "deviceToken --> " + deviceToken);
                EventUtils.postEvent(new DeviceTokenEvent(deviceToken));
//                //获取deviceToken可通过接口：
//                PushAgent.getInstance(context).getRegistrationId();
//                //可设置别名，推送时使用别名推送
//                String alias = "123456";
//                String type = "aa";
//                PushAgent.getInstance(context).setAlias(alias, type, new UPushAliasCallback() {
//                    @Override
//                    public void onMessage(boolean success, String message) {
//                        Log.i(TAG, "setAlias " + success + " msg:" + message);
//                    }
//                });
            }

            @Override
            public void onFailure(String errCode, String errDesc) {
                Log.e(TAG, "register failure：--> " + "code:" + errCode + ",desc:" + errDesc);
            }
        });
        registerDeviceChannel(context);
    }

    /**
     * 注册设备推送通道（小米、华为等设备的推送）
     */
    private static void registerDeviceChannel(Context context) {
        //小米通道，填写您在小米后台APP对应的xiaomi id和key
        MiPushRegistar.register(context, PushConstants.MI_ID, PushConstants.MI_KEY);
        //华为，注意华为通道的初始化参数在minifest中配置
        HuaWeiRegister.register((Application) context.getApplicationContext());
//        //魅族，填写您在魅族后台APP对应的app id和key
//        MeizuRegister.register(context, PushConstants.MEI_ZU_ID, PushConstants.MEI_ZU_KEY);
//        //OPPO，填写您在OPPO后台APP对应的app key和secret
//        OppoRegister.register(context, PushConstants.OPPO_KEY, PushConstants.OPPO_SECRET);
//        //vivo，注意vivo通道的初始化参数在minifest中配置
//        VivoRegister.register(context);
    }


    private static PendingIntent toMainActivity(String id,String theme) {
        //点击广播监听
        Intent intentClick = null;
        intentClick = new Intent(mContext, MainActivity.class);
        intentClick.putExtra("id","6");
        intentClick.putExtra("theme","theme");
        intentClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(mContext, 0, intentClick, 0);
    }
    //推送设置
    private static void pushSetting(Context context) {
        PushAgent pushAgent = PushAgent.getInstance(context);

        //设置通知栏显示通知的最大个数（0～10），0：不限制个数
        pushAgent.setDisplayNotificationNumber(0);

//        推送消息处理
        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                super.dealWithCustomMessage(context, uMessage);
            }


            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
            }

            @SuppressLint("NewApi")
            @Override
            public Notification getNotification(Context context, UMessage msg) {
               if (msg.extra != null && msg.extra.get("type") .equals("theme")) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    @SuppressLint({"NewApi", "LocalSuppress"}) NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                    }
                    PendingIntent pendingIntent = toMainActivity(msg.extra.get("id"),msg.extra.get("theme"));
                    Notification.Builder builder = new Notification.Builder(context, "channel_id");
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))

                            .setContentTitle(msg.title)
                            .setContentText(msg.text)
                            .setAutoCancel(true);
                    return builder.build();
                } else {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    @SuppressLint({"NewApi", "LocalSuppress"}) NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                    }
                    Notification.Builder builder = new Notification.Builder(context, "channel_id");
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(msg.title)
                            .setContentText(msg.text)
                            .setAutoCancel(true);
                    return builder.build();
                }
            }
        };
        pushAgent.setMessageHandler(messageHandler);

        //推送消息点击处理
        UmengNotificationClickHandler notificationClickHandler = new CustomNotificationHandler() {
            @Override
            public void openActivity(Context context, UMessage msg) {
                Log.i(TAG, "click openActivity: " + msg.getRaw().toString());
                super.openActivity(context, msg);

            }

            @Override
            public void launchApp(Context context, UMessage msg) {
                if (msg.extra != null && msg.extra.get("type") .equals("theme")) {
                    if (isRokiInBackground(context)) {
                        startRokiAPP(context, APPPACKAGENAME, msg.extra.entrySet());
                    }
                    EventUtils.postEvent(new Detailevnet(Long.parseLong(msg.extra.get("id"))));
//                    SelectThemeDetailPage.show(Integer.parseInt("id"),TYPE_THEME_BANNER);
                }else{
                    super.launchApp(context, msg);
                }
//                Log.i(TAG, "click launchApp: " + msg.getRaw().toString());



            }

            @Override
            public void dismissNotification(Context context, UMessage msg) {
                Log.i(TAG, "click dismissNotification: " + msg.getRaw().toString());
                super.dismissNotification(context, msg);

            }
        };

        pushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
        pushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
        pushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
        pushAgent.setNotificationClickHandler(notificationClickHandler);

        //自定义接收并处理消息
//        pushAgent.setPushIntentServiceClass(MyCustomMessageService.class);

    }

}
