package com.robam.roki.ui.page;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.events.WxCodeEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.Advert;
import com.robam.common.services.StoreService;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.appStatus.AppStatus;
import com.robam.roki.ui.appStatus.AppStatusManager;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.WelcomeActivity;
import com.robam.roki.ui.form.WizardActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.widget.view.CountdownView2;
import com.robam.roki.utils.DialogUtil;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/4.
 */
public class WelcomePage extends MyBasePage<WelcomeActivity> {

    //刷新Ring
    final static int AUTO_REFRESH = 0;
    private static final String TAG = "WelcomePage";
    public int second = 2;
    @InjectView(R.id.img_advert)
    ImageView mImgAdvert;

    @InjectView(R.id.cv_ring)
    CountdownView2 cv_ring;

    private String mContent;
    //间隔->毫秒
    private int jiange = 130;
    //等待的次数
    private int total = (second * 1000) / jiange;
    private int now = 0;
    private int mType;
    private boolean mSige = false;

    private IRokiDialog privacyDialog;//隐私协议对话框
    private IRokiDialog exitDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.page_welcome;
    }

    @Override
    protected void initView() {
        setStateBarTransparent();
        privacyDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_23);
        exitDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_24);
        LogUtils.i(TAG, "onCreateView");
//        ButterKnife.inject(this, view);
        privacyPolicyDialog();
        initAdvertData();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initAdvertData() {
        cv_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNext();
            }
        });
        StoreService.getInstance().getAppAdvertImg(new Callback<Reponses.AppAdvertImgResponses>() {
            @Override
            public void onSuccess(Reponses.AppAdvertImgResponses appAdvertImgResponses) {
                List<Advert> images = appAdvertImgResponses.images;
                for (Advert advert : images) {
                    String imgUrl = advert.imgUrl;
                    mContent = advert.content;
                    mType = advert.type;
                    if (imgUrl != null){
                        if (getContext()!=null) {
                            GlideApp.with(getContext())
                                    .load(imgUrl)
                                    .into(mImgAdvert);
                        }
//                        ImageUtils.displayImage(imgUrl, mImgAdvert);
                    }
                }
                cv_ring.setVisibility(View.VISIBLE);
                cv_ring.start(new CountdownView2.StopLinstener() {
                    @Override
                    public void stop() {
                        startNext();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
//                initAdvertData();
                if (cv_ring != null){
                    cv_ring.setVisibility(View.VISIBLE);
                    cv_ring.start(new CountdownView2.StopLinstener() {
                        @Override
                        public void stop() {
                            startNext();
                        }
                    });
                }
            }
        });

    }



    private void startNext() {
        boolean isFirstUse = PreferenceUtils.getBool(PageArgumentKey.IsFirstUse, true);
        AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL);
        LogUtils.i(TAG, "isFirstUse:" + isFirstUse);
        if (isFirstUse) {
//            WizardActivity.start(activity);
        }else {

            MainActivity.start(activity);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.img_advert)
    public void onMImgAdvertClicked() {
        LogUtils.i(TAG, "mType:" + mType);
        if (PreferenceUtils.getBool(PageArgumentKey.IsFirstUse, true)) {
            return;
        }
        AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL);
        if (mType == 1) {
            if (!TextUtils.isEmpty(mContent)) {
//                mHandler.removeCallbacks(runnable);
                cv_ring.artStop();
                mSige = true;
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Url, mContent);
                bd.putString(PageArgumentKey.entranceCode, RecipeRequestIdentification.RECIPE_ADVERT);
                UIService.getInstance().postPage(PageKey.WebAdvert, bd);
            }
        } else if (mType == 2) {
//            mHandler.removeCallbacks(runnable);
            cv_ring.artStop();
            mSige = true;
            Bundle bd = new Bundle();
            bd.putLong(PageArgumentKey.BookId, Long.valueOf(mContent));
            bd.putString(PageArgumentKey.entranceCode, RecipeRequestIdentification.RECIPE_ADVERT);
            UIService.getInstance().postPage(PageKey.RecipeDetail, bd);

        }
    }

    private void privacyPolicyDialog() {
        boolean isFirstUse = PreferenceUtils.getBool(PageArgumentKey.IsFirstUse, true);
        LogUtils.i(TAG, "isFirstUse:" + isFirstUse);
        if (isFirstUse) {
            String privacyContent = getContext().getString(R.string.privacy_policy_content);
            privacyDialog.setCancelable(false);
            privacyDialog.setContentText(privacyContent);
            privacyDialog.show();
            privacyDialog.setOkBtn("同意并继续", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   PreferenceUtils.setBool(PageArgumentKey.IsFirstUse, false);
                    privacyDialog.dismiss();
                    AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL);
//                    WizardActivity.start(activity);
                    if (NetworkUtils.isConnect(cx)){
                        WizardActivity.start(activity);
                    }else {
                        MainActivity.start(activity);
                    }
//                    startLogin();
//                    startNext();
                }
            });
            privacyDialog.setCancelBtn(R.string.recipe_auto_cannel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    privacyDialog.dismiss();
                    showExitDialog();
                }
            });
        }
    }

    /**
     * @param
     */
//    @Subscribe
//    public void onEvent(WxCodeEvent event) {
//        LoginPageHelper.getInstance().onEvent(event);
//    }
    private void showExitDialog() {
        exitDialog.setCancelable(false);
        exitDialog.show();
        exitDialog.setOkBtn("同意并继续", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.setBool(PageArgumentKey.IsFirstUse, false);
                exitDialog.dismiss();
                AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL);
                if (NetworkUtils.isConnect(cx)){
                    WizardActivity.start(activity);
                }else {
                    MainActivity.start(activity);
                }
//                WizardActivity.start(activity);
//                startLogin();
//                startNext();
            }
        });
        exitDialog.setCancelBtn("退出应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                getActivity().finish();
            }
        });
    }
}


