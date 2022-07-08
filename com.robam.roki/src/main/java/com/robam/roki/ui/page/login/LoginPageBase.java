package com.robam.roki.ui.page.login;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.CmccEvent;
import com.robam.common.events.WxCode2Event;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.UserActivity;
import com.robam.roki.ui.page.login.adapter.FragmentPagerAdapter;
import com.robam.roki.ui.page.login.helper.LoginHelper;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.widget.layout.NoScrollViewPager;
import com.robam.roki.ui.widget.view.ScaleImageView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;


/**
 * desc ： 登录
 *
 * @author hxw
 */
public class LoginPageBase extends MyBasePage<UserActivity> implements ViewPager.OnPageChangeListener {
    /**
     * 第三方登录lineaLayout
     */
    private LinearLayout ll_login_other;
    /**
     * 第三方登录 微信
     */
    private ImageView iv_login_wechat;
    /**
     * 登录pager
     */
    private NoScrollViewPager vp_login;
    /**
     * pagerAdapter
     */
    private FragmentPagerAdapter<MyBasePage> mPagerAdapter;
    /**
     * 同意认证checkBox
     */
    public CheckBox cb_accept_roki_privacy;
    /**
     * 认证文字
     */
    private TextView loginAuthentication;
    /**
     * 当前position
     */
    private int position;
    /**
     * page对象
     */
    @SuppressLint("StaticFieldLeak")
    public static LoginPageBase instance;
    /**
     * 微信登录
     */
    private String code;
    private boolean isCmccLogin;

    private ImageView ivBack;

    private TabLayout tabLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.page_login_base;
    }

    @Override
    protected void initView() {
        instance = this;
//        StatusBarUtils.setColor(cx ,Color.WHITE);
//        setRightTitle(R.string.login_password);
//        getTitleBar().setOnTitleBarListener(this);
        ivBack = findViewById(R.id.img_back);
        tabLayout = findViewById(R.id.tabLayout);
        iv_login_wechat = findViewById(R.id.iv_login_wechat);
        ll_login_other = (LinearLayout) findViewById(R.id.ll_login_other);
        cb_accept_roki_privacy = (CheckBox) findViewById(R.id.cb_accept_roki_privacy);
        loginAuthentication = findViewById(R.id.user_login_register);
        vp_login = (NoScrollViewPager) findViewById(R.id.vp_login);
        vp_login.addOnPageChangeListener(this);
        setOnClickListener(iv_login_wechat, ivBack);
        //初始化同意认证提示
        initLoginRegister();
        if (!MobApp.mWxApi.isWXAppInstalled()) {
            ll_login_other.setVisibility(View.GONE);
            iv_login_wechat.setVisibility(View.GONE);
        }

        TabLayout.Tab codeTab = tabLayout.newTab();
        View codeView = LayoutInflater.from(getContext()).inflate(R.layout.view_login_tab_item, null);
        TextView codeTv = codeView.findViewById(R.id.txtTab);
        codeTv.setText(R.string.login_code);
        codeTab.setCustomView(codeView);
        tabLayout.addTab(codeTab);

        TabLayout.Tab passTab = tabLayout.newTab();
        View passView = LayoutInflater.from(getContext()).inflate(R.layout.view_login_tab_item, null);
        TextView passTv = passView.findViewById(R.id.txtTab);
        passTv.setText(R.string.login_password);
        passTab.setCustomView(passView);
        tabLayout.addTab(passTab);

        //为了点击缩放
        iv_login_wechat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//放大图片
                    v.setScaleX(1.2f);
                    v.setScaleY(1.2f);
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL) {//恢复原状
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);

                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        int right_title_id = (getArguments() != null) ? getArguments().getInt("right_title_id", 0) : 0;
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(LoginCodePage.newInstance(), getResources().getString(R.string.login_code));
        mPagerAdapter.addFragment(LoginPasswordPage.newInstance(), getResources().getString(R.string.login_password));
        vp_login.setAdapter(mPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_login));
        vp_login.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        if (right_title_id == 0 || right_title_id == R.string.login_password) {
//            setRightTitle(R.string.login_password);
//            mPagerAdapter = new FragmentPagerAdapter<>(this);
//            mPagerAdapter.addFragment(LoginCodePage.newInstance(), getResources().getString(R.string.login_password));
//            mPagerAdapter.addFragment(LoginPasswordPage.newInstance(), getResources().getString(R.string.login_code));
//            vp_login.setAdapter(mPagerAdapter);
//        } else {
//            setRightTitle(R.string.login_code);
//            mPagerAdapter = new FragmentPagerAdapter<>(this);
//            mPagerAdapter.addFragment(LoginPasswordPage.newInstance(), getResources().getString(R.string.login_code));
//            mPagerAdapter.addFragment(LoginCodePage.newInstance(), getResources().getString(R.string.login_password));
//            vp_login.setAdapter(mPagerAdapter);
//        }
        if (getBundle()!=null){
            isCmccLogin = getBundle().getBoolean("isCmccLogin", false);
        }

    }


    @Override
    public void onClick(View view) {
        if (view == iv_login_wechat) {
            if (!isLoginAuthentication()) {
                ToastUtils.show(getString(R.string.accept_user_privacy_policy), Toast.LENGTH_SHORT);
                return;
            }
            LogUtils.i("20171023", "code!=null");
            sendWxMessage();
        } else if (view == ivBack) {
            doBack();
        }
    }

    /**
     * 微信认证
     */
    private void sendWxMessage() {

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wx_login";
        MobApp.mWxApi.sendReq(req);
    }

    @Subscribe
    public void onEvent(WxCode2Event event) {
        if ("wx_login".equals(event.state)){
            code = event.code;
            loginWx(code);
        }
    }
    @Subscribe
    public void onEvent(CmccEvent event) {
        UIService.getInstance().popBack();
    }
    /**
     * 第三方登录 微信
     *
     * @param code
     */
    private void loginWx(String code) {
        LoginHelper.loginWx(activity, code);
    }

    /**
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        doBack();
    }

    private void doBack() {
        if (isCmccLogin) {
            CmccLoginHelper.getInstance().loginAuth();

        } else {
            UIService.getInstance().popBack();
        }
    }

    /**
     * @param view 被点击的右项View
     */
    @Override
    public void onRightClick(View view) {
        if (position == 0) {
            vp_login.setCurrentItem(1);
        } else {
            vp_login.setCurrentItem(0);
        }
    }


    /**
     * 初始化认证提示
     */
    private void initLoginRegister() {
//        loginAuthentication.setText(Html.fromHtml(getResources().getString(R.string.login_authentication2)));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.login_authentication));
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = String.format("%s", IRokiRestService.UserNotice);
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Url, url);
                bd.putString(PageArgumentKey.WebTitle, "ROKI用户协议");
                UIService.getInstance().postPage(PageKey.WebClientNew, bd);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#61ACFF"));
                ds.setUnderlineText(false);
            }
        }, 6, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = String.format("%s", IRokiRestService.RegisterAgreement);
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Url, url);
                bd.putString(PageArgumentKey.WebTitle, "隐私协议");
                UIService.getInstance().postPage(PageKey.WebClientNew, bd);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#61ACFF"));
                ds.setUnderlineText(false);
            }
        }, 17, 23, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        loginAuthentication.setMovementMethod(LinkMovementMethod.getInstance());
        loginAuthentication.setText(spannableStringBuilder);
        avoidHintColor(loginAuthentication);
    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * viewPager监听 根据当前显示position显示右上角标题
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        this.position = position;
        setRightTitle(mPagerAdapter.getPageTitle(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 是否同意认证
     *
     * @return
     */
    public boolean isLoginAuthentication() {
        return cb_accept_roki_privacy.isChecked();
    }

    /**
     * 显示第几个page
     *
     * @param position
     */
    public void setLoginIndex(int position) {
        vp_login.setCurrentItem(position);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            doBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
