//package com.robam.roki.ui.page;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.legent.Callback;
//import com.legent.plat.pojos.AppVersionInfo;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.utils.LogUtils;
//import com.legent.utils.TimeUtils;
//import com.legent.utils.api.PackageUtils;
//import com.legent.utils.api.ToastUtils;
//import com.robam.common.io.cloud.IRokiRestService;
//import com.robam.common.services.AppUpdateService;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//
//import java.util.Calendar;
//import java.util.concurrent.ScheduledFuture;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by sylar on 15/6/14.
// * 关于 关于Roki页
// */
//public class AboutPage extends BasePage {
//
//    ScheduledFuture<?> future;
//    @InjectView(R.id.txtVersion)
//    TextView txtVersion;
////    @InjectView(R.id.txtNewestVer)
////    TextView txtNewestVer;
//    @InjectView(R.id.txtWebSite)
//    TextView txtWebSite;
//    @InjectView(R.id.txtTerm)
//    TextView txtTerm;
//    @InjectView(R.id.ic_about_logo)
//    ImageView aboutLogo;
//    boolean isExit;
//    int logoClickCount;
//    AppVersionInfo verInfo;
//    @InjectView(R.id.img_back)
//    ImageView imgBack;
//    @InjectView(R.id.rl_user_agreement)
//    RelativeLayout userAgreement;
//    @InjectView(R.id.rl_privacy_policy)
//    RelativeLayout privacyPolicy;
//    @InjectView(R.id.tv_copyright)
//    TextView copyRight;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.page_about, container, false);
//        ButterKnife.inject(this, view);
//        txtVersion.setText("V " + PackageUtils.getVersionName(cx));
//        checkVer();
//        return view;
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        copyRight.setText("Copyright 2011-" + Calendar.getInstance().get(Calendar.YEAR) + " Roki. All Right Reserved");
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
////    @OnClick(R.id.txtNewestVer)
////    public void onClickNewestVer() {
////        if (verInfo == null) {
////            checkVer();
////        } else {
////            AppUpdateService.getInstance().start(cx);
////        }
////    }
//
//    @OnClick(R.id.txtWebSite)
//    public void onClickWebSite() {
////        Bundle bd = new Bundle();
////        bd.putString(PageArgumentKey.Url, txtWebSite.getText().toString());
////        UIService.getInstance().postPage(PageKey.WebClient, bd);
//    }
//
//    @OnClick(R.id.txtTerm)
//    public void onClickTerm() {
//       /* String url = String.format("%s/%s",
//                Plat.serverOpt.getRestfulBaseUrl(),
//                IRokiRestService.UserNotice);*/
//
//        String url = String.format("%s",
//                IRokiRestService.UserNotice);
//
//        Bundle bd = new Bundle();
//        bd.putString(PageArgumentKey.Url, url);
//        bd.putString(PageArgumentKey.WebTitle, cx.getString(R.string.my_about_roki_user_protocol));
//        UIService.getInstance().postPage(PageKey.WebClientNew, bd);
//    }
//
//    @OnClick(R.id.ic_about_logo)
//    public void onClickLogo() {
//        if (!isExit) {
//            isExit = true;
//            aboutLogo.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    isExit = false;
//                }
//            }, 500);
//        } else {
//            logoClickCount++;
//            if (logoClickCount >= 5) {
//                Context cx = getContext();
//                jmptoFirmwareUpgrade(cx);
//            }
//        }
//    }
//
//
//    @OnClick(R.id.rl_user_agreement)
//    public void onClickUserAgreement(){
//        String url = String.format("%s",
//                IRokiRestService.UserNotice);
//
//        Bundle bd = new Bundle();
//        bd.putString(PageArgumentKey.Url, url);
//        bd.putString(PageArgumentKey.WebTitle, cx.getString(R.string.my_about_user_protocol));
//        UIService.getInstance().postPage(PageKey.WebClientNew, bd);
//    }
//
//    @OnClick(R.id.rl_privacy_policy)
//    public void onClickPrivacyPolicy() {
//        String url = String.format("%s",
//                IRokiRestService.RegisterAgreement);
//        Bundle bdprivacy = new Bundle();
//        bdprivacy.putString(PageArgumentKey.Url, url);
//        bdprivacy.putString(PageArgumentKey.WebTitle, cx.getString(R.string.my_about_privacy_protocol));
//        UIService.getInstance().postPage(PageKey.WebClientNew, bdprivacy);
//    }
//
//    public void jmptoFirmwareUpgrade(Context cx) {
//        Uri uri = Uri.parse("http://manage.myroki.com/#/device-upgrade");
////        Uri uri = Uri.parse("http://172.16.14.32:8081");
//        Intent it = new Intent(Intent.ACTION_VIEW, uri);
//        cx.startActivity(it);
//    }
//
//
//    void checkVer() {
//        ProgressDialogHelper.setRunning(cx, true);
//        AppUpdateService.getInstance().checkVersion(new Callback<AppVersionInfo>() {
//            @Override
//            public void onSuccess(AppVersionInfo info) {
//                verInfo = info;
//                ProgressDialogHelper.setRunning(cx, false);
//
//                LogUtils.i("20170704", "info:" + info.name);
//                LogUtils.i("20170704", "txtVersion.getText():" + txtVersion.getText());
//
////                txtNewestVer.setText(info != null ? info.name : txtVersion.getText());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ProgressDialogHelper.setRunning(cx, false);
////                txtNewestVer.setText("点此检查");
//                ToastUtils.showThrowable(t);
//            }
//        });
//    }
//
//
//    @OnClick(R.id.img_back)
//    public void onViewClicked() {
//
//        UIService.getInstance().popBack();
//    }
//}
