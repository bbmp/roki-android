package com.robam.roki.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.MessageEventNumber;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.robam.common.events.WxCode2Event;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.MessageActivity;
import com.robam.roki.ui.activity3.main.HomeActivity;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.widget.layout.SettingBar2;
import com.robam.roki.utils.GlideCircleTransform;
import com.robam.roki.utils.OnMultiClickListener;
import com.robam.roki.utils.StringUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

/**
 *    author : huxw
 *    time   : 2018/10/18
 *    desc   : 我的 Fragment
 */
public final class MineFragment extends TitleBarFragment<HomeActivity> {
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
    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        rlLogin = findViewById(R.id.rl_login);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserPhone = (TextView) findViewById(R.id.tv_user_phone);
        btnEditInfo = (AppCompatButton) findViewById(R.id.btn_edit_info);
        stbCollection = (SettingBar2) findViewById(R.id.stb_collection);
        stbWork = (SettingBar2) findViewById(R.id.stb_work);
        stbCookLine = (SettingBar2) findViewById(R.id.stb_cook_line);
        stbDevice = (SettingBar2) findViewById(R.id.stb_device);
        stbSaleService = (SettingBar2) findViewById(R.id.stb_sale_service);
        stbAbout = (SettingBar2) findViewById(R.id.stb_about);
        stbSetting = (SettingBar2) findViewById(R.id.stb_setting);
//        setOnClickListener(ivPhoto, tvUserName, tvUserPhone, btnEditInfo,
//                stbCollection, stbCollection, stbWork, stbDevice, stbSaleService, stbAbout, stbSetting);
        setOnClickListener(btnEditInfo,
                stbCollection, stbCollection, stbWork,stbCookLine, stbDevice, stbSaleService, stbAbout, stbSetting
                , ivPhoto, tvUserName, tvUserPhone);

        setLoginView();


        findViewById(R.id.rl_mine_message).setOnClickListener(view -> {
            if (Plat.accountService.isLogon()) {
                getContext().startActivity(new Intent(getContext(), MessageActivity.class));
            }else{
                CmccLoginHelper.getInstance().toLogin();
            }
        });
    }

    @Override
    protected void initData() {
        instance = CmccLoginHelper.getInstance();
        instance.initSdk(getAttachActivity());
        instance.getPhnoeInfo();
        onRefresh();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Subscribe
    public void event(MessageEventNumber event){

        if (!Plat.accountService.isLogon()||event.getNumber()==0) {
            findViewById(R.id.textview_home_device_number).setVisibility(View.GONE);
        }else{
            if (event.getNumber()<=99) {
//                ((TextView) findViewById(R.id.textview_home_device_number)).setTextSize(getResources().getDimension(R.dimen.sp_12));
                ((TextView) findViewById(R.id.textview_home_device_number)).setText(event.getNumber() + "");
            }else {
//                ((TextView) findViewById(R.id.textview_home_device_number)).setTextSize(getResources().getDimension(R.dimen.sp_8));
                ((TextView) findViewById(R.id.textview_home_device_number)).setText("99+");
            }

            findViewById(R.id.textview_home_device_number).setVisibility(View.VISIBLE);
        }
        if (event.getNumber()==0){
            findViewById(R.id.textview_home_device_number).setVisibility(View.GONE);
        }
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
//            UIService.getInstance().postPage(PageKey.MineSettingPage);
            HomeActivity.start(getContext());
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
            LoginHelper.loginWx(getAttachActivity(), event.code, true);
            instance.quitAuthActivity();
        }

    }

    /**
     * 获取用户信息3.7
     */
    public void getUser() {
        if (Plat.accountService.isLogon()) {
            ProgressDialogHelper.setRunning(getAttachActivity(), true);
            CloudHelper.getUser2(Plat.accountService.getCurrentUserId(), new Callback<User>() {

                @Override
                public void onSuccess(User user) {
                    ProgressDialogHelper.setRunning(getAttachActivity(), false);
                    Plat.accountService.onLogin(user);
                }

                @Override
                public void onFailure(Throwable t) {
                    ProgressDialogHelper.setRunning(getAttachActivity(), false);
                    ToastUtils.show(t.getMessage());
                }
            });
        }
    }

    /**
     * 设置用户信息
     */
    public void onRefresh() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            User user = Plat.accountService.getCurrentUser();
            tvUserName.setText(Strings.isNullOrEmpty(user.name) ? user.phone : user.name);
            tvUserPhone.setText(user.getPhone());
            if (!Strings.isNullOrEmpty(user.figureUrl)) {
                Glide.with(getAttachActivity()).load(user.figureUrl).placeholder(R.mipmap.ic_user_default_figure)
                        .transform(new GlideCircleTransform(getAttachActivity(), 1, getAttachActivity().getResources().getColor(R.color.White)))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPhoto);
            }
            btnEditInfo.setVisibility(View.VISIBLE);
        } else {
            tvUserName.setText(getString(R.string.not_login));
            tvUserPhone.setText(getString(R.string.login_or_register));
            ivPhoto.setBackgroundResource(R.drawable.headportrait_wdl);
            Glide.with(getAttachActivity()).load(R.drawable.headportrait_wdl)
                    .transform(new GlideCircleTransform(getAttachActivity(), 1, getAttachActivity().getResources().getColor(R.color.White)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPhoto);
            btnEditInfo.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();
    }
}