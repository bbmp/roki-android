package com.robam.roki.ui.page;

import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.plat.Plat;
import com.legent.plat.events.ChatNewMsgEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.robam.common.ui.UiHelper;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.utils.ToolUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 * 售后管理 售后服务
 */
public class SaleServicePage extends MyBasePage<MainActivity> {


    @InjectView(R.id.tv_red_dot)
    TextView tvRedDot;
    @InjectView(R.id.ll_message_consulting)
    LinearLayout llMessageConsulting;
    @InjectView(R.id.ll_key_after_sales)
    LinearLayout llKeyAfterSales;
    @InjectView(R.id.tv_call_phone)
    TextView tvCallPhone;
    private final String KEY_AFTER_SALES_TEXT = "95105855";
    @InjectView(R.id.img_back)
    ImageView imgBack;

    @Override
    protected int getLayoutId() {
        return R.layout.page_sale_service;
    }

    @Override
    protected void initView() {
        EventUtils.regist(this);
        tvCallPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), "售后服务页", null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //售后拨打电话
    @OnClick(R.id.tv_call_phone)
    public void onClickCall() {
        ToolUtils.logEvent("售后服务", "一键售后", "roki_个人");
        Uri uri = Uri.parse(String.format("tel:%s", tvCallPhone.getText().toString()));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(it);
    }

    /**
     * 留言咨询
     */
    @OnClick(R.id.ll_message_consulting)
    public void onClickApply() {
        if (Plat.accountService.isLogon()) {
            ToolUtils.logEvent("售后服务", "留言咨询", "roki_个人");
            UIService.getInstance().postPage(PageKey.Chat);
        }else {
            startLogin();
        }
    }
    /**
     * 登录界面
     */
    private void startLogin() {
        if (CmccLoginHelper.getInstance().isGetPhone) {
            CmccLoginHelper.getInstance().loginAuth();
        } else {
            CmccLoginHelper.getInstance().login();
        }
    }

    private boolean isTelephonyEnabled(){
        TelephonyManager tm = (TelephonyManager)MobApp.getInstance().getSystemService(TELEPHONY_SERVICE);

        return tm != null && tm.getSimState()==TelephonyManager.SIM_STATE_READY;

    }
    //一键售后
    @OnClick(R.id.ll_key_after_sales)
    public void onClick() {
        if (isTelephonyEnabled()) {
            ToolUtils.logEvent("售后服务", "一键售后", "roki_个人");
            Uri uri = Uri.parse(String.format("tel:%s", KEY_AFTER_SALES_TEXT));
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(it);
        }else{
            ToastUtils.show("设备不支持通话功能");
        }
    }

    @Subscribe
    public void onEvent(ChatNewMsgEvent event) {
        boolean hasNew = event.hasNew;
        if (hasNew) {
            tvRedDot.setVisibility(View.VISIBLE);
        } else {
            tvRedDot.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }
}
