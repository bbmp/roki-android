package com.robam.roki;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSAuthListener;
import com.aispeech.dui.dds.DDSConfig;
import com.aispeech.dui.dds.DDSInitListener;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.clj.fastble.BleManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastBlackStyle;
import com.legent.VoidCallback2;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceTokenEvent;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.RobamApp;
import com.robam.common.io.device.RokiDeviceFactory;
import com.robam.common.io.device.RokiMsgMarshaller;
import com.robam.common.io.device.RokiMsgSyncDecider;
import com.robam.common.io.device.RokiNoticeReceiver;
import com.robam.common.services.NotifyService;
import com.robam.roki.service.AppService;
import com.robam.roki.service.MobNotifyService;
import com.robam.roki.service.ShareSdkOAuthService;
import com.robam.roki.ui.activity3.device.base.other.ActivityManager;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.push.PushHelper;
import com.robam.roki.ui.view.umpush.CustomNotificationHandler;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.utils.UMUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.api.UPushRegisterCallback;
import com.umeng.message.entity.UMessage;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.litepal.LitePal;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinLayoutInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;
import skin.support.utils.SkinPreference;
import skin.support.utils.Slog;

public class MobApp extends RobamApp {


    static public final String APP_TYPE = "RKDRD";
    private PushAgent mPushAgent;
    public static IWXAPI mWxApi;
    public static final String WX_APP_ID = "wx77973859a88a8921";


    public static  String id;

    public static String cookId;

    public static String Game;


    public static String h5Url;
    public static String secondTitle;
    public static String img;
    public static String title;
    private static FirebaseAnalytics mFirebaseAnalytics;


//    private void initsdk(){
//
//        mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
////        mPushAgent.setDebugMode(false);
////        CustomNotificationHandler notificationHandler
////                = new CustomNotificationHandler();
////        mPushAgent.setNotificationClickHandler(notificationHandler);
////        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
////        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
////        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            initNotification();
////        }
//        mPushAgent.register(new UPushRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
//                Log.i(TAG, "注册成功 deviceToken:" + deviceToken);
//            }
//
//            @Override
//            public void onFailure(String errCode, String errDesc) {
//                Log.e(TAG, "注册失败 " + "code:" + errCode + ", desc:" + errDesc);
//            }
//        });
//    }
    private void initUMengSDK(){
        UMConfigure.setLogEnabled(true);
        PushHelper.preInit(this);
        boolean isMainProcess = UMUtils.isMainProgress(this);
        if (isMainProcess){
            new Thread(() -> PushHelper.init(this)).start();
        }else {
            PushHelper.init(this);
        }
    }

    @SuppressWarnings("static-access")
    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化有赞
//        YouzanSDK.init(this, "a9263a4f294b175f63");//正式版
        ScreenAdapterTools.init(this);
        registToWX();
//        MiPushRegistar.register(context, XIAOMI_ID, XIAOMI_KEY, false);
        //友盟推送
        initUMengSDK();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // 初始化吐司
        ToastUtils.init(this, new ToastBlackStyle(this) {

            @Override
            public int getCornerRadius() {
                return (int) getResources().getDimension(R.dimen.dialog_ui_round_size);
            }
        });
//        CrashReport.initCrashReport(this, "d7782dd9d5", true);

        LitePal.initialize(this);
        // Activity 栈管理初始化
        ActivityManager.getInstance().init(this);
//        SQLiteDatabase db = LitePal.getDatabase();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            String processName = getProcessName(this);
//            String packageName = this.getPackageName();
//            if (!packageName.equals(processName)) {
//                WebView.setDataDirectorySuffix(processName);
//            }
//        }
        HuaWeiRegister.register(this);

        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(3, 5000)
                .setOperateTimeout(5000);
//换肤
        Slog.DEBUG = BuildConfig.DEBUG;
        SkinCompatManager.withoutActivity(this)
                .addInflater((SkinLayoutInflater) new SkinAppCompatViewInflater())  // 基础控件换肤
                .addInflater(new SkinMaterialViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .loadSkin();


    }
    // 创建dds配置信息
    public DDSConfig createConfig() {
        DDSConfig config = new DDSConfig();
        // 基础配置项
        config.addConfig(DDSConfig.K_PRODUCT_ID, "278582915"); // 产品ID -- 必填
        config.addConfig(DDSConfig.K_USER_ID, "lyb");  // 用户ID -- 必填
        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");   // 产品的发布分支 -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "633eed0b2ac0b5d9862b1cb8d8f28c40");// Product Key -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "1f10cffab30f470510d6336d9f47b4fb");// Product Secre -- 必填
        config.addConfig(DDSConfig.K_API_KEY, "6cb41d6dc2de6cb41d6dc2de6039f8cb");  // 产品授权秘钥，服务端生成，用于产品授权 -- 必填

        return config;
    }

//    // dds初始状态监听器,监听init是否成功
//    public DDSInitListener mInitListener = new DDSInitListener() {
//        @Override
//        public void onInitComplete(boolean isFull) {
//
//            LogUtils.i(TAG, "思必驰语音初始化成功 isFull:" + isFull);
//            try {
//
//            }catch (Exception e){
//                Log.d(TAG ,"思必驰广播失败");
//            }
//
//        }
//
//        @Override
//        public void onError(int what, final String msg) {
//            Log.e(TAG, "Init onError: " + what + ", error: " + msg);
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                }
//            });
//        }
//    };
//    // dds认证状态监听器,监听auth是否成功
//    public DDSAuthListener mAuthListener = new DDSAuthListener() {
//        @Override
//        public void onAuthSuccess() {
//            LogUtils.i(TAG, "onAuthSuccess");
//            // 发送一个认证成功的广播
////            getContext().sendBroadcast(new Intent("ddsdemo.intent.action.auth_success"));
//        }
//
//        @Override
//        public void onAuthFailed(final String errId, final String error) {
//            LogUtils.i(TAG, "onAuthFailed: " + errId + ", error:" + error);
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
////                    Toast.makeText(THIS,
////                            "授权错误:" + errId + ":\n" + error + "\n请查看手册处理", Toast.LENGTH_LONG).show();
//                }
//            });
//            // 发送一个认证失败的广播
////            getContext().sendBroadcast(new Intent("ddsdemo.intent.action.auth_failed"));
//        }
//    };

//    private String getProcessName(Context context) {
//        if (context == null) return null;
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
//            if (processInfo.pid == android.os.Process.myPid()) {
//                return processInfo.processName;
//            }
//        }
//        return null;
//    }


    private PendingIntent toMainActivity(String id,String theme) {
        //点击广播监听
        Intent intentClick = null;
        intentClick = new Intent(this, MainActivity.class);
        intentClick.putExtra("id",id);
        intentClick.putExtra("theme",theme);
        intentClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0, intentClick, 0);
    }

    //适配8.0以上版本通知栏不显示
//    private void initNotification() {
//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            @SuppressLint("NewApi")
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                if (msg.extra != null && msg.extra.get("type") .equals("theme")) {
//                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    @SuppressLint({"NewApi", "LocalSuppress"}) NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
//                    if (manager != null) {
//                        manager.createNotificationChannel(channel);
//                    }
//                    PendingIntent pendingIntent = toMainActivity(msg.extra.get("id"),msg.extra.get("type"));
//                    Notification.Builder builder = new Notification.Builder(context, "channel_id");
//                    builder.setSmallIcon(R.mipmap.ic_launcher)
//                            .setWhen(System.currentTimeMillis())
//                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                            .setContentIntent(pendingIntent)
//                            .setContentTitle(msg.title)
//                            .setContentText(msg.text)
//                            .setAutoCancel(true);
//                    return builder.build();
//                } else {
//                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    @SuppressLint({"NewApi", "LocalSuppress"}) NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
//                    if (manager != null) {
//                        manager.createNotificationChannel(channel);
//                    }
//                    Notification.Builder builder = new Notification.Builder(context, "channel_id");
//                    builder.setSmallIcon(R.mipmap.ic_launcher)
//                            .setWhen(System.currentTimeMillis())
//                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                            .setContentTitle(msg.title)
//                            .setContentText(msg.text)
//                            .setAutoCancel(true);
//                    return builder.build();
//                }
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);
//    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        // 将该app注册到微信
        mWxApi.registerApp(WX_APP_ID);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public static FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        xcrash.XCrash.init(this);
    }

    @Override
    public NotifyService getNotifyService() {
        return MobNotifyService.getInstance();
    }

    @Override
    protected void initPlat() {
        if (mFirebaseAnalytics==null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }
        super.initPlat();
        Plat.appOAuthService = new ShareSdkOAuthService();
        Plat.init(this, APP_TYPE,
                new RokiDeviceFactory(),
                new RokiMsgMarshaller(),
                new RokiMsgSyncDecider(),
                new RokiNoticeReceiver(),
                new VoidCallback2() {
                    @Override
                    public void onCompleted() {
                        AppService.getInstance().init(MobApp.this);
                    }
                });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //非默认值
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override

    public Resources getResources() {
        Resources res = super.getResources();
        //非默认值
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
}
