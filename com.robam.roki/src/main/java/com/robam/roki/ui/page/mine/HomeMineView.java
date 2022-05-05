package com.robam.roki.ui.page.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.ChatNewMsgEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.events.UserUpdatedEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CmccBackEvent;
import com.robam.common.events.CookMomentsRefreshEvent;
import com.robam.common.events.FavorityBookRefreshEvent;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.WxCode2Event;
import com.robam.common.util.StatusBar2Utils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.RWebActivity;
import com.robam.roki.ui.adapter3.RvDeviceAdapter;
import com.robam.roki.ui.adapter3.RvDeviceBluetoothAdapter;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.view.networkoptimization.BleConnectActivity;
import com.robam.roki.ui.widget.layout.SettingBar;
import com.robam.roki.ui.widget.layout.SettingBar2;
import com.robam.roki.utils.ApiSecurityExample;
import com.robam.roki.utils.GlideCircleTransform;
import com.robam.roki.utils.OnMultiClickListener;
import com.robam.roki.utils.StringUtil;

import java.util.List;

/**
 * 我的界面
 *
 * @author hxw
 */
public class HomeMineView extends MyBaseView {
    private static final String TAG = "HomeMineView";
    /**
     * 头像
     */
    private ImageView ivPhoto;
    /**
     * 用户名
     */
    private TextView tvUserName;
    /**
     * 手机号
     */
    private TextView tvUserPhone;
    /**
     * 编辑资料
     */
    private AppCompatButton btnEditInfo;
    /**
     * 我的收藏
     */
    private SettingBar2 stbCollection;
    /**
     * 我的作品
     */
    private SettingBar2 stbWork;
    /**
     * 烹饪曲线
     */
    private SettingBar2 stbCookLine;

    /**
     * 厨电管理
     */
    private SettingBar2 stbDevice;
    /**
     * 售后服务
     */
    private SettingBar2 stbSaleService;
    /**
     * 关于
     */
    private SettingBar2 stbAbout;
    /**
     * 设置
     */
    private SettingBar2 stbSetting;
    private CmccLoginHelper instance;
    private View rlLogin;

    public HomeMineView(Context context, FragmentActivity activity) {
        super(context, activity);
    }


    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        onRefresh();
    }


    @Subscribe
    public void onEvent(UserUpdatedEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(CookMomentsRefreshEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(FavorityBookRefreshEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(ChatNewMsgEvent event) {
        boolean hasNew = event.hasNew;
    }

    @Subscribe
    public void onEvent(CmccBackEvent event) {
        setLoginView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_home_mine;
    }

    @Override
    protected void initView() {
        rlLogin = findViewBy(R.id.rl_login);
        ivPhoto = (ImageView) findViewBy(R.id.iv_photo);
        tvUserName = (TextView) findViewBy(R.id.tv_user_name);
        tvUserPhone = (TextView) findViewBy(R.id.tv_user_phone);
        btnEditInfo = (AppCompatButton) findViewBy(R.id.btn_edit_info);
        stbCollection = (SettingBar2) findViewBy(R.id.stb_collection);
        stbWork = (SettingBar2) findViewBy(R.id.stb_work);
        stbCookLine = (SettingBar2) findViewBy(R.id.stb_cook_line);
        stbDevice = (SettingBar2) findViewBy(R.id.stb_device);
        stbSaleService = (SettingBar2) findViewBy(R.id.stb_sale_service);
        stbAbout = (SettingBar2) findViewBy(R.id.stb_about);
        stbSetting = (SettingBar2) findViewBy(R.id.stb_setting);
//        setOnClickListener(ivPhoto, tvUserName, tvUserPhone, btnEditInfo,
//                stbCollection, stbCollection, stbWork, stbDevice, stbSaleService, stbAbout, stbSetting);
        setOnClickListener(btnEditInfo,
                stbCollection, stbCollection, stbWork,stbCookLine, stbDevice, stbSaleService, stbAbout, stbSetting
                , ivPhoto, tvUserName, tvUserPhone);

        setLoginView();
    }

    private void setLoginView() {
        rlLogin.setOnClickListener(new OnMultiClickListener() {
            @Override
            protected void onMoreClick(View view) {
                boolean isLogin = Plat.accountService.isLogon();
                if (isLogin) {
                    UIService.getInstance().postPage(PageKey.MineEditInfoPage);
                } else {
//                    startLogin();
                }
            }

            @Override
            protected void onMoreErrorClick() {
//                ToastUtils.show("请勿重复点击");
            }
        });
    }


    @Override
    protected void initData() {
        instance = CmccLoginHelper.getInstance();
        instance.initSdk(activity);
        instance.getPhnoeInfo();
        onRefresh();
    }

    @Override
    public void onClick(View view) {
        boolean isLogin = Plat.accountService.isLogon();
//        if (view.equals(ivPhoto) || view.equals(tvUserName) || view.equals(tvUserPhone)) {
//            onLogin();
//        } else
        if (view.equals(btnEditInfo)) {
            if (isLogin) {
//                cx.startActivity(new Intent(cx , BleConnectActivity.class));
                UIService.getInstance().postPage(PageKey.MineEditInfoPage);
            } else {
                onLogin();
            }
        } else if (view.equals(ivPhoto) || view.equals(tvUserPhone) || view.equals(tvUserName)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.MineEditInfoPage);
            } else {
                startLogin();
            }
        } else if (view.equals(stbCollection)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.AbsThemeRecipeListGrid);
            } else {
                onLogin();
            }
        } else if (view.equals(stbWork)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.RecipeCookMoments);
            } else {
                onLogin();
            }
        } else if (view.equals(stbDevice)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.DeviceManager);
            } else {
                onLogin();
            }
        } else if (view.equals(stbSaleService)) {
            UIService.getInstance().postPage(PageKey.SaleService);
        } else if (view.equals(stbAbout)) {
            UIService.getInstance().postPage(PageKey.MineAboutPage);
        } else if (view.equals(stbSetting)) {
            UIService.getInstance().postPage(PageKey.MineSettingPage);
        }else if (view.equals(stbCookLine)) {
            if (isLogin) {
                UIService.getInstance().postPage(PageKey.CurveCookbooks);
            } else {
                onLogin();
            }
        }
        else {

        }
    }


    /**
     * 登录？用户信息
     */
    public void onLogin() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
//            UIService.getInstance().postPage(PageKey.MineEditInfoPage);
        } else {
            startLogin();
        }
    }

    /**
     * 指向登录界面
     */
    private void startLogin() {
        if (instance.isGetPhone) {
            instance.loginAuth();
        } else {
            instance.login();
        }
    }

    @Subscribe
    public void onEvent(WxCode2Event event) {
        if (!StringUtil.isEmpty(event.code) && "user_wx_login".equals(event.state)) {
            LoginHelper.loginWx(activity, event.code, true);
            instance.quitAuthActivity();
        }

    }

    /**
     * 获取用户信息3.7
     */
    public void getUser() {
        if (Plat.accountService.isLogon()) {
            ProgressDialogHelper.setRunning(cx, true);
            CloudHelper.getUser2(Plat.accountService.getCurrentUserId(), new Callback<User>() {

                @Override
                public void onSuccess(User user) {
                    ProgressDialogHelper.setRunning(cx, false);
                    Plat.accountService.onLogin(user);
                }

                @Override
                public void onFailure(Throwable t) {
                    ProgressDialogHelper.setRunning(cx, false);
                    ToastUtils.showThrowable(t);
                }
            });
        }
    }

    /**
     * 设置用户信息
     */
    @Override
    public void onRefresh() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            User user = Plat.accountService.getCurrentUser();
            tvUserName.setText(Strings.isNullOrEmpty(user.name) ? user.phone : user.name);
            tvUserPhone.setText(user.getPhone());
            if (!Strings.isNullOrEmpty(user.figureUrl)) {
                Glide.with(cx).load(user.figureUrl).placeholder(R.mipmap.ic_user_default_figure)
                        .transform(new GlideCircleTransform(cx, 1, cx.getResources().getColor(R.color.White)))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPhoto);
            }
            btnEditInfo.setVisibility(VISIBLE);
        } else {
            tvUserName.setText(getString(R.string.not_login));
            tvUserPhone.setText(getString(R.string.login_or_register));
            ivPhoto.setBackgroundResource(R.drawable.headportrait_wdl);
            Glide.with(cx).load(R.drawable.headportrait_wdl)
                    .transform(new GlideCircleTransform(cx, 1, cx.getResources().getColor(R.color.White)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPhoto);
            btnEditInfo.setVisibility(GONE);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}
