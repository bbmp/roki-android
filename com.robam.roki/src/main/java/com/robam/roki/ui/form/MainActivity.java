package com.robam.roki.ui.form;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.VoidCallback2;
import com.legent.events.ActivityResultOnPageEvent;
import com.legent.events.AppVisibleEvent;
import com.legent.events.ChangeLoginErrorEvent;
import com.legent.plat.Plat;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.ui.AbsActivity;
import com.legent.ui.UIService;
//import com.legent.ui.ext.BaseActivity;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.AbsCookerAlarmEvent;
import com.robam.common.events.Detailevnet;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.events.DishWasherAlarmEvent;
import com.robam.common.events.IntegStoveAlarmEvent;
import com.robam.common.events.IntegStoveEvent;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.NewSteamOvenOneAlarmEvent;
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.PotAlarmEvent;
import com.robam.common.events.RikaAlarmEvent;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.WaterPurifiyAlarmEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.io.device.RokiDeviceFactory;
import com.robam.common.io.device.RokiMsgMarshaller;
import com.robam.common.io.device.RokiMsgSyncDecider;
import com.robam.common.io.device.RokiNoticeReceiver;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.integratedStove.IntegratedStoveConstant;
import com.robam.common.pojos.device.integratedStove.SteamOvenEventEnum;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.service.AppService;
import com.robam.roki.ui.FormKey;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.appStatus.AppStatus;
import com.robam.roki.ui.appStatus.AppStatusManager;
import com.robam.roki.ui.page.SelectThemeDetailPage;
import com.robam.roki.ui.view.umpush.UMPushMsg;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;


import static com.legent.plat.services.ResultCodeManager.EC_RC_Success;
import static com.robam.roki.MobApp.APP_TYPE;
import static com.robam.roki.ui.page.SelectThemeDetailPage.TYPE_THEME_BANNER;

public class MainActivity extends AbsActivity {
    public static Activity activity;
    private ActivityManager manager;
    private int i = 0;
    private final int ERRER_STATUS = 401;
    private final int NOT_LOGIN_STATUS = 7003;
    List<DeviceGroupList> groupList = new ArrayList<DeviceGroupList>();
    List<List<DeviceItemList>> deviceList = new ArrayList<List<DeviceItemList>>();

    static public void start(Activity atv) {
        atv.startActivity(new Intent(atv, MainActivity.class));
        atv.finish();
    }

    private static final String TAG = "MainActivity";
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG,"onNewIntent");
        if (intent.getStringExtra("id")!=null) {
            String id = intent.getStringExtra("id");
//         String theme=intent.getStringExtra("theme");
            SelectThemeDetailPage.show(Long.parseLong(id), TYPE_THEME_BANNER);
        }

    }

    @Override
    protected String createFormKey() {
        return FormKey.MainForm;
    }


    @Subscribe
    public void onEvent(StoveAlarmEvent event) {
        AlarmDataUtils.onStoveAlarmEvent(event.stove, event.alarm);
    }

    @Subscribe
    public void onEvent(PotAlarmEvent event) {
        AlarmDataUtils.onPotAlarmEvent(event.pot, event.alarmId);
    }

    /**
     * RIKA 报警事件
     *
     * @param event
     */
    @Subscribe
    public void onEvent(RikaAlarmEvent event) {
        AlarmDataUtils.rikaAlarmStatus(event.mRika, event.mAlarmCodeBean);

    }


    @Subscribe
    public void onEvent(Detailevnet event) {
        Log.e("----","----");
//        AlarmDataUtils.rikaAlarmStatus(event.mRika, event.mAlarmCodeBean);
        SelectThemeDetailPage.show(event.id, TYPE_THEME_BANNER);
    }

    /**
     * RIKA 集成灶报警
     *
     * @param event
     */
    @Subscribe
    public void onEvent(IntegStoveAlarmEvent event) {
        AlarmDataUtils.integratedAlarmStatus(event.integratedStove, event.faulCode, event.category);

    }

    /**
     * 集成灶事件
     *
     * @param event
     */
    @Subscribe
    public void onEvent(IntegStoveEvent event) {
        try {
            switch (event.category) {
                case IntegratedStoveConstant.FAN:
                    break;
                case IntegratedStoveConstant.STEAME_OVEN_ONE:
                    if (event.eventCode == 8 || event.eventCode == 9){
                        ToastUtils.show(SteamOvenEventEnum.match(event.eventCode).getValue() , 1000);
                    }
                    break;
                case IntegratedStoveConstant.STOVE:
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.getMessage();
        }


    }

    //电烤箱报警事件
    @Subscribe
    public void onEvent(OvenAlarmEvent event) {
        if (!isTopActivity()){
            return;
        }
        AbsOven oven = event.oven;
        AlarmDataUtils.ovenAlarmStatus(event.alarmId, oven);
    }





    //蒸汽炉报警接收事件
    @Subscribe
    public void onEvent(SteamAlarmEvent event) {
        if (!isTopActivity()){
            return;
        }
        AbsSteamoven steam = event.steam;
        AlarmDataUtils.SteamAlarmStatus(steam, event.alarmId);
    }

    //蒸烤一体机报警接收事件
    @Subscribe
    public void onEvent(NewSteamOvenOneAlarmEvent event) {
        if (!isTopActivity()){
            return;
        }
        AbsSteameOvenOne steameOvenOne = event.steameOvenOne;
        short alarms = event.alarmId;
        LogUtils.i("202012071057", "alarms::" + alarms);
        AlarmDataUtils.steamOvenOneAlarmStatus(steameOvenOne, alarms);
    }

    @Subscribe
    public void onEvent(SteriAlarmEvent event) {
        if (!isTopActivity()){
            return;
        }
        AbsSterilizer sterilizer = event.absSterilizer;
        LogUtils.i("20191204888", "event.alarm:::" + event.alarm);
        AlarmDataUtils.onSteriAlarmEvent(sterilizer, event.alarm);
    }

    //微波炉报警接收事件
    @Subscribe
    public void onEvent(MicroWaveAlarmEvent event) {
        AbsMicroWave absMicroWave = event.absMicroWave;
        AlarmDataUtils.onMicroWaveAlarmEvent(absMicroWave, event.alarm);
    }

    //净水器报警事件
    @Subscribe
    public void onEvent(WaterPurifiyAlarmEvent event) {
        if (!isTopActivity()){
            return;
        }
        AbsWaterPurifier purifier = event.purifier;
        short alarmId = (short) event.alarmId;
        AlarmDataUtils.onWaterPurifiyAlarmEvent(purifier, alarmId);

    }

    //电磁炉报警事件需要进行配置
    @Subscribe
    public void onEvent(AbsCookerAlarmEvent event) {
        if (!isTopActivity()){
            return;
        }
        AbsCooker cooker = event.absCooker;
        short alarmId = event.alramId;
        AlarmDataUtils.cookerAlarm(cooker, alarmId);
    }

    @Subscribe
    public void onEvent(ChangeLoginErrorEvent event) {
        if (event.status == ERRER_STATUS) {
            UIService.getInstance().postPage(PageKey.UserLogin);
        }
    }

    //洗碗机报警事件
    @Subscribe
    public void onEvent(DishWasherAlarmEvent event) {
        AbsDishWasher washer = event.washer;
        short alarmId = event.alarmId;
        LogUtils.i("2020061201", "alarmId2:" + alarmId);
        AlarmDataUtils.dishWasherAlarm(washer, alarmId);

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);


        activity = MainActivity.this;
        Plat.activtiyContext = activity;
        manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        AlarmDataUtils.init(this);
//        initWX();
        PreferenceUtils.setBool(
                PageArgumentKey.IsFirst, false);
        PreferenceUtils.setBool(PageArgumentKey.IsFirst, false);
        RokiRestHelper.getNetworkDeviceInfoRequest("roki", null, null, new Callback<List<DeviceGroupList>>() {
            @Override
            public void onSuccess(List<DeviceGroupList> deviceGroupLists) {
                groupList = deviceGroupLists;
                for (int i = 0; i < groupList.size(); i++) {
                    deviceList.add(groupList.get(i).getDeviceItemLists());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    //解决OPPO第一次初始化网络权限造成Mqtt失败的问题
    private void initPlat() {
        Plat.init(getApplication(), APP_TYPE,
                new RokiDeviceFactory(),
                new RokiMsgMarshaller(),
                new RokiMsgSyncDecider(),
                new RokiNoticeReceiver(),
                new VoidCallback2() {
                    @Override
                    public void onCompleted() {
                        AppService.getInstance().init(getApplication());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //            AlarmDataUtils.onResume();
//        EventUtils.postEvent(new AppVisibleEvent(true));
//        try {
//
//            if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE){
//                //跳到MainActivity,让MainActivity也finish掉
//                WelcomeActivity.start(this);
//                return;
//            }
//        }catch (Exception e){
//        }
        EventUtils.postEvent(new FloatHelperEvent(false,99));

    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("test1111" , "onPause");
        EventUtils.postEvent(new AppVisibleEvent(false));
        EventUtils.postEvent(new FloatHelperEvent(true,99));
    }

    @Override
    protected void onKeyDown_Back() {
        String pageKey = UIService.getInstance().getTop().getCurrentPageKey();
        if (PageKey.RecipeCooking.equals(pageKey)
                || PageKey.RecipeCook.equals(pageKey)
        ) {
            //防止烧菜中按返回键退出烧菜
        } else {
            super.onKeyDown_Back();
        }
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        UIService.getInstance().returnHome();
    }

    @Override
    protected void showTipWhenExit() {
        ToastUtils.showShort(R.string.exit_the_program);
    }


    @Override
    protected void onDestroy() {
        //初始化判断友盟是否推送条件
//        AlarmDataUtils.closeReceiver();
//        UMPushMsg.setMsgType(0);
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) return;

        for (Fragment fragment : fragments) {
            if (fragment != null) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EventUtils.postEvent(new ActivityResultOnPageEvent(requestCode, resultCode, data));
        LogUtils.i("20181128", "requestCode::" + requestCode + " resultCode::" + resultCode + " data::" + data);
        final User owner = Plat.accountService.getCurrentUser();
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    Bundle bd = data.getExtras();
                    String sn = bd.getString("result");
                    LogUtils.i("20190104", "sn:" + sn);
                    if (sn.contains("GUID")) {
                        addDCLDevice(sn, owner);
                    } else if (sn.contains("UUID-LOGIN")) {
                        loginPad(sn, owner);
                    } else if (sn.contains("com.robam.roki&product=")) {
                        String productSum = sn.split("&")[1];
                        String product = productSum.split("=")[1];
                        DeviceItemList list = new DeviceItemList();
                        String chnName = "";
                        for (int i = 0; i < deviceList.size(); i++) {
                            List<DeviceItemList> deviceItemLists = deviceList.get(i);
                            for (int j = 0; j < deviceItemLists.size(); j++) {
                                String name = deviceItemLists.get(j).getName();
//                                if ("R".equals(name.substring(0,1))){
//                                    name = name.substring(1);
//                                }
                                if (TextUtils.equals(product, name)) {
                                    list = deviceItemLists.get(j);
                                    chnName = groupList.get(i).name;
                                }
                            }
                        }
                        if (TextUtils.isEmpty(list.displayType)) {
                            ToastUtils.show("无效二维码", 2000);
                            return;
                        }
                        String dp = list.dp;
                        if ("YYJ02".equals(dp)) {//有屏
                            Bundle bundle = new Bundle();
                            bundle.putString("NetImgUrl", list.getNetImgUrl());
                            bundle.putString("displayType", list.displayType);
                            UIService.getInstance().postPage(PageKey.DeviceScan, bundle);
                            return;
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("NetImgUrl", list.getNetImgUrl());
                            bundle.putString("NetTips", list.getNetTips());
                            bundle.putString("displayType", list.displayType);
                            bundle.putString("strDeviceName", list.dt);
                            bundle.putString("chnName", chnName);
                            UIService.getInstance().postPage(PageKey.DeviceWifiConnect, bundle);
                        }
                    } else {
                        addDevice(sn, owner);
                    }
                }
                break;
            case 87:
                String downloaduri = PreferenceUtils.getString("downloaduri", "");
                Uri parse = Uri.parse(downloaduri);
                PackageUtils.installApk(activity, parse);
                break;

            default:
                break;
        }
    }

    private void addDevice(String sn, final User owner) {
        if (sn != null && sn.contains("http")) {
            ToastUtils.show("添加设备失败", Toast.LENGTH_SHORT);
            return;
        }
        Plat.deviceService.getDeviceBySn(sn, new Callback<DeviceInfo>() {
            @Override
            public void onSuccess(final DeviceInfo deviceInfo) {
                Plat.deviceService.bindDevice(owner.getID(), deviceInfo.getID(), deviceInfo.getName(), false, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort(R.string.add_device_failure);
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(deviceInfo));
                        UIService.getInstance().returnHome();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("添加失败", 2000);
            }
        });
    }

    private void loginPad(String sn, final User owner) {
        if (TextUtils.isEmpty(sn)) return;
        final String uuid = sn.substring(10);
        LogUtils.i("20190104", "uuid:" + uuid);
        final IRokiDialog loginDialog = RokiDialogFactory.createDialogByType(this, DialogUtil.DIALOG_TYPE_00);
        loginDialog.setTitleText(R.string.is_login_pad);
        loginDialog.show();
        loginDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
                RokiRestHelper.getScanQRLogin(owner.getID(), uuid, owner.phone,
                        new Callback<com.robam.common.io.cloud.Reponses.ScanQRLoginResponse>() {
                            @Override
                            public void onSuccess(com.robam.common.io.cloud.Reponses.ScanQRLoginResponse scanQRLoginResponse) {
                                int rc = scanQRLoginResponse.rc;
                                if (NOT_LOGIN_STATUS == rc) {
                                    UIService.getInstance().postPage(PageKey.UserLogin);
                                } else if (EC_RC_Success == rc) {
                                    ToastUtils.showShort(R.string.login_suc);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("20190104", "t:" + t);
                            }
                        });
            }
        });
        loginDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
            }
        });


    }

    private void addDCLDevice(String sn, final User owner) {
        if (sn != null && sn.contains("http")) {
            ToastUtils.show("添加设备失败", Toast.LENGTH_SHORT);
            return;
        }
        String guid = sn.substring(4);
        CloudHelper.getDeviceById(guid, new Callback<DeviceInfo>() {
            @Override
            public void onSuccess(final DeviceInfo deviceInfo) {
                Plat.deviceService.bindDevice(owner.getID(), deviceInfo.getID(), deviceInfo.getName(), false, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort(R.string.add_device_failure);
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(deviceInfo));
                        UIService.getInstance().returnHome();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void initWX() {

        User currentUser = Plat.accountService.getCurrentUser();
        if (currentUser != null && currentUser.password != null) {
            Helper.login(currentUser.getAccount(), currentUser.password);
        } else {
            boolean logu = PreferenceUtils.getBool("logout", false);
            if (logu) {
                String token = PreferenceUtils.getString("token", null);
                CloudHelper.otherLogin("wx", "RKDRD", null, token, new Callback<Reponses.OtherLoginResponse>() {
                    @Override
                    public void onSuccess(Reponses.OtherLoginResponse user3In) {
                        if (!user3In.user.binded) {
                            Bundle bundle = new Bundle();
                            bundle.putString("openId", user3In.user.thirdInfos.openId);
                            UIService.getInstance().postPage(PageKey.UserLoginThird, bundle);
                        } else {
                            Plat.accountService.onLogin(user3In.user);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if ("7001".equals(t.getMessage())) {
                        }
                    }
                });
            }
        }
    }

    private boolean isTopActivity()
    {
        boolean isTop = false;
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains("MainActivity"))
        {
            isTop = true;
        }
        return isTop;
    }

}

