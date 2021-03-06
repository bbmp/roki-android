package com.robam.roki;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
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

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.litepal.LitePal;
public class MobApp extends RobamApp {


    static public final String APP_TYPE = "RKDRD";
    private PushAgent mPushAgent;
    public static IWXAPI mWxApi;
    public static final String WX_APP_ID = "wx77973859a88a8921";


    public static  String id;


//    private void initsdk(){
//
//        mPushAgent = PushAgent.getInstance(this);
//        //?????????????????????????????????register???????????????????????????
////        mPushAgent.setDebugMode(false);
////        CustomNotificationHandler notificationHandler
////                = new CustomNotificationHandler();
////        mPushAgent.setNotificationClickHandler(notificationHandler);
////        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //??????
////        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//?????????
////        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//??????
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            initNotification();
////        }
//        mPushAgent.register(new UPushRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //?????????????????????deviceToken deviceToken??????????????????????????????
//                Log.i(TAG, "???????????? deviceToken:" + deviceToken);
//            }
//
//            @Override
//            public void onFailure(String errCode, String errDesc) {
//                Log.e(TAG, "???????????? " + "code:" + errCode + ", desc:" + errDesc);
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
        //???????????????
//        YouzanSDK.init(this, "a9263a4f294b175f63");//?????????
        registToWX();
//        MiPushRegistar.register(context, XIAOMI_ID, XIAOMI_KEY, false);
        //????????????
        initUMengSDK();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // ???????????????
//        ToastUtils.init(this, new ToastBlackStyle(this) {
//
//            @Override
//            public int getCornerRadius() {
//                return (int) getResources().getDimension(R.dimen.dialog_ui_round_size);
//            }
//        });
        CrashReport.initCrashReport(this, "d7782dd9d5", true);

        LitePal.initialize(this);
//        SQLiteDatabase db = LitePal.getDatabase();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            String processName = getProcessName(this);
//            String packageName = this.getPackageName();
//            if (!packageName.equals(processName)) {
//                WebView.setDataDirectorySuffix(processName);
//            }
//        }
        HuaWeiRegister.register(this);



    }
    // ??????dds????????????
    public DDSConfig createConfig() {
        DDSConfig config = new DDSConfig();
        // ???????????????
        config.addConfig(DDSConfig.K_PRODUCT_ID, "278582915"); // ??????ID -- ??????
        config.addConfig(DDSConfig.K_USER_ID, "lyb");  // ??????ID -- ??????
        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");   // ????????????????????? -- ??????
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "633eed0b2ac0b5d9862b1cb8d8f28c40");// Product Key -- ??????
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "1f10cffab30f470510d6336d9f47b4fb");// Product Secre -- ??????
        config.addConfig(DDSConfig.K_API_KEY, "6cb41d6dc2de6cb41d6dc2de6039f8cb");  // ????????????????????????????????????????????????????????? -- ??????

        return config;
    }

    // dds?????????????????????,??????init????????????
    public DDSInitListener mInitListener = new DDSInitListener() {
        @Override
        public void onInitComplete(boolean isFull) {

            LogUtils.i(TAG, "?????????????????????????????? isFull:" + isFull);
            try {

            }catch (Exception e){
                Log.d(TAG ,"?????????????????????");
            }

        }

        @Override
        public void onError(int what, final String msg) {
            Log.e(TAG, "Init onError: " + what + ", error: " + msg);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    };
    // dds?????????????????????,??????auth????????????
    public DDSAuthListener mAuthListener = new DDSAuthListener() {
        @Override
        public void onAuthSuccess() {
            LogUtils.i(TAG, "onAuthSuccess");
            // ?????????????????????????????????
//            getContext().sendBroadcast(new Intent("ddsdemo.intent.action.auth_success"));
        }

        @Override
        public void onAuthFailed(final String errId, final String error) {
            LogUtils.i(TAG, "onAuthFailed: " + errId + ", error:" + error);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(THIS,
//                            "????????????:" + errId + ":\n" + error + "\n?????????????????????", Toast.LENGTH_LONG).show();
                }
            });
            // ?????????????????????????????????
//            getContext().sendBroadcast(new Intent("ddsdemo.intent.action.auth_failed"));
        }
    };

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
        //??????????????????
        Intent intentClick = null;
        intentClick = new Intent(this, MainActivity.class);
        intentClick.putExtra("id",id);
        intentClick.putExtra("theme",theme);
        intentClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0, intentClick, 0);
    }

    //??????8.0??????????????????????????????
    private void initNotification() {
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @SuppressLint("NewApi")
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                if (msg.extra != null && msg.extra.get("type") .equals("theme")) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    @SuppressLint({"NewApi", "LocalSuppress"}) NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                    }
                    PendingIntent pendingIntent = toMainActivity(msg.extra.get("id"),msg.extra.get("type"));
                    Notification.Builder builder = new Notification.Builder(context, "channel_id");
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setContentIntent(pendingIntent)
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
        mPushAgent.setMessageHandler(messageHandler);
    }

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID??????????????????????????????????????????AppID??????????????????
        mWxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        // ??????app???????????????
        mWxApi.registerApp(WX_APP_ID);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
//        xcrash.XCrash.init(this);
    }

    @Override
    public NotifyService getNotifyService() {
        return MobNotifyService.getInstance();
    }

    @Override
    protected void initPlat() {
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
        //????????????
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override

    public Resources getResources() {
        Resources res = super.getResources();
        //????????????
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//????????????
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
}
