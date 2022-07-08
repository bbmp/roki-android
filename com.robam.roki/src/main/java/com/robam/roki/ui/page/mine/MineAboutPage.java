package com.robam.roki.ui.page.mine;

import static android.content.Context.NOTIFICATION_SERVICE;

import static com.robam.roki.ui.page.SelectThemeDetailPage.TYPE_THEME_BANNER;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationCompat;

import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PackageUtils;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.common.services.AppUpdateService;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.SelectThemeDetailPage;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.widget.layout.SettingBar;

import java.util.Calendar;

import butterknife.OnClick;


/**
 * 关于Roki
 * @author hxw
 */
public class MineAboutPage extends MyBasePage<MainActivity> {

    /**
     * 版本号
     */
    private TextView tvVersion;
    /**
     * 公司官网
     */
    private RelativeLayout stbAboutWeb;
    /**
     * 用户协议
     */
    private RelativeLayout stbAboutAgreement;
    /**
     * 隐私协议
     */
    private RelativeLayout stbAboutPrivacy;

    /**
     * 隐私协议
     */
    private RelativeLayout stbUpload;
    /**
     * 底部信息
     */
    private TextView tvAboutBottom;
    private ImageView ivBack;

    AppVersionInfo verInfo ;

    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_about;
    }
    Notification notification;
    NotificationManager Notificamanager;
    private final static int NOTIFY_ID = 100;


    private PendingIntent toMainActivity(String id,String theme) {
        //点击广播监听
        Intent intentClick = null;

            intentClick = new Intent(getContext(), MainActivity.class);
            //如果跳转的activity 是一个普通的设置FLAG_ACTIVITY_NEW_TASK即可，
            //如果是一个singletask activity 则需要添加FLAG_ACTIVITY_CLEAR_TOP ，否则启动可能失败
            intentClick.putExtra("id",id);
            intentClick.putExtra("theme",theme);
            intentClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intentClick.addCategory("android.intent.category.LAUNCHER");
//            intentClick.setAction("android.intent.action.MAIN");

        return PendingIntent.getActivity(getActivity(), 0, intentClick, 0);
    }
    private void showNotification() {



        Notificamanager = (NotificationManager)getContext(). getSystemService(NOTIFICATION_SERVICE);
//        Intent hangIntent = new Intent(getContext(), MainActivity.class);
//        hangIntent.putExtra("id","6");
//        PendingIntent hangPendingIntent = PendingIntent.getActivity(getContext(), 1001, hangIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = "your_custom_id";//应用频道Id唯一值， 长度若太长可能会被截断，
        String CHANNEL_NAME = "your_custom_name";//最长40个字符，太长会被截断
        notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setContentTitle("这是一个猫333头")
                .setContentText("点我返回应用")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(toMainActivity("6","theam"))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.add))
                .setAutoCancel(true)
                .build();

        //Android 8.0 以上需包添加渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            Notificamanager.createNotificationChannel(notificationChannel);
        }

        Notificamanager.notify(NOTIFY_ID, notification);
    }
    @Override
    protected void initView() {
        ivBack = findViewById(R.id.img_back);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        stbAboutWeb = findViewById(R.id.stb_about_web);
        stbAboutAgreement = findViewById(R.id.stb_about_agreement);
        stbAboutPrivacy = findViewById(R.id.stb_about_privacy);
        stbUpload = findViewById(R.id.stb_upload);
        setOnClickListener(stbAboutAgreement, stbAboutPrivacy , stbUpload, ivBack);
        tvAboutBottom = findViewById(R.id.tv_about_bottom);
        tvAboutBottom.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                showNotification();
            }
        });
    }

    @Override
    protected void initData() {
        tvVersion.setText("V " + PackageUtils.getVersionName(cx));
        tvAboutBottom.setText("杭州老板电器股份有限公司\nCopyright 2011-" + Calendar.getInstance().get(Calendar.YEAR) + " Roki. All Right Reserved");
    }

    @Override
    public void onClick(View view) {
        if (view.equals(stbAboutAgreement)) {
            String url = String.format("%s",
                    IRokiRestService.UserNotice);

            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Url, url);
            bd.putString(PageArgumentKey.WebTitle, cx.getString(R.string.my_about_user_protocol));
            UIService.getInstance().postPage(PageKey.WebClientNew, bd);
        } else if (view.equals(stbAboutPrivacy)) {
            String url = String.format("%s",
                    IRokiRestService.RegisterAgreement);
            Bundle bdprivacy = new Bundle();
            bdprivacy.putString(PageArgumentKey.Url, url);
            bdprivacy.putString(PageArgumentKey.WebTitle, cx.getString(R.string.my_about_privacy_protocol));
            UIService.getInstance().postPage(PageKey.WebClientNew, bdprivacy);
        }else if (view.equals(stbUpload)){

//            SelectThemeDetailPage.show(Long.parseLong("6"), TYPE_THEME_BANNER);
//            if (verInfo == null){
                checkVer();
//            }else {
//                if (PackageUtils.getVersionCode(cx) < verInfo.code){
//                    jmptoFirmwareUpgrade(cx , verInfo.url);
//                }else {
//                    ToastUtils.show("当前已经是最新版本");
//                }
//            }
        } else if (view.equals(ivBack)) {
            UIService.getInstance().popBack();

        }
    }
    private void checkVer() {
        AppUpdateService.getInstance().start(cx ,"");
//        ProgressDialogHelper.setRunning(cx, true);
//        AppUpdateService.getInstance().checkVersion(new Callback<AppVersionInfo>() {
//            @Override
//            public void onSuccess(AppVersionInfo info) {
//                verInfo = info;
//                ProgressDialogHelper.setRunning(cx, false);
//                if (PackageUtils.getVersionCode(cx) < verInfo.code){
//                    jmptoFirmwareUpgrade(cx , verInfo.url);
//                }else {
//                    ToastUtils.show("当前已经是最新版本");
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ProgressDialogHelper.setRunning(cx, false);
////                txtNewestVer.setText("点此检查");
//                ToastUtils.show(t.toString());
//            }
//        });
    }
    public void jmptoFirmwareUpgrade(Context cx , String url) {
        Uri uri = Uri.parse(url);
//        Uri uri = Uri.parse("http://172.16.14.32:8081");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        cx.startActivity(it);
    }
    /**
     * 返回
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        UIService.getInstance().popBack();

    }


}
