package com.robam.roki.ui.activity3.device.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.eventbus.Subscribe;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.base.BaseActivity;
import com.robam.base.BaseDialog;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.ui.activity3.ToastAction;
import com.robam.roki.ui.activity3.device.fan.adapter.RvFuntionAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.page.login.action.TitleBarAction;
import com.robam.roki.ui.page.login.config.Constant;
import com.robam.roki.utils.DataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;


/**
 * author :
 * time   : 2022/06/07
 * desc   : 设备主页基类
 */
public abstract class DeviceBaseActivity extends AppActivity
        implements ToastAction, DeviceParamManage.OnParamListener {

    public static final String BUNDLE = "bundle" ;

    /**
     * 设备guid
     */
    protected String mGuid;
    /**
     * 设备
     */
    protected IDevice mDevice;
    /**
     * 设备类型
     */
    protected String mDt;
    /**
     *
     */
    protected String mDc;
    /**
     * 用户ID
     */
    protected long userId;

    /**
     * 基础数据加载完毕
     */
    protected boolean loadSuccess  = false ;
    protected Bundle bundle;

    @Subscribe
    public void onEvent(DeviceNameChangeEvent event) {
        if (mGuid.equals(event.device.getGuid().getGuid())) {
            String name = event.device.getName();
            setTitle(name);
        }
    }

    @Override
    protected void initLayout() {
        super.initLayout();
    }

    @Override
    protected void initData() {
        EventUtils.regist(this);
        bundle = getIntent().getBundleExtra(BUNDLE);
        mGuid = bundle == null ? null : bundle.getString(PageArgumentKey.Guid);
        mDevice = Plat.deviceService.lookupChild(mGuid);

        userId = Plat.accountService.getCurrentUserId();
        mDt = mDevice.getDt();
        mDc = mDevice.getDc();
        //请求设备参数
        DeviceParamManage deviceParamManage = new DeviceParamManage(this);
        deviceParamManage.getDataMethod(userId, mDt, mDc);

        setOnClickListener(R.id.iv_back, R.id.iv_device_switch, R.id.iv_device_more);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            onBackPressed();
        } else if (view.getId() == R.id.iv_device_switch) {
            onSwitch();
        } else if (view.getId() == R.id.iv_device_more) {
            toMore();
        }
    }

    /**
     * 关机按钮
     */
    protected void onSwitch() {
        if ( mDevice == null) return;
        if (!mDevice.isConnected()) {
            toast(R.string.device_connected);
            return;
        }
    }

    /**
     * 更多界面
     */
    protected void toMore() {

    }

    /**
     * 设置标题栏的标题
     */
    protected void setTitle(String title) {
        if (findViewById(R.id.tv_title) == null) {
            return;
        }
        if (findViewById(R.id.tv_title) instanceof TextView) {
            TextView tvTitle = (TextView)findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
    }

    /**
     * 隐藏关闭开关
     */
    protected void hideCloseSw(){
        if (findViewById(R.id.iv_device_switch) == null) {
            return;
        }
        findViewById(R.id.iv_device_switch).setVisibility(View.INVISIBLE);
    }
    /**
     * 设置设备背景
     * @param url
     */
    protected void setDeviceBgImg(String url){
        if (findViewById(R.id.iv_device_img) == null) {
            return;
        }
        if (findViewById(R.id.iv_device_img) instanceof AppCompatImageView) {
            AppCompatImageView ivBg = (AppCompatImageView)findViewById(R.id.iv_device_img);
            GlideApp.with(this)
                    .load(url)
                    .into(ivBg);
        }
    }


    protected void setScollBgImg(BaseQuickAdapter adapter ,String url){
        View view = getLayoutInflater().inflate(R.layout.item_device_bg_img, null);
        if (view.findViewById(R.id.iv_device_img) instanceof AppCompatImageView) {
            AppCompatImageView ivBg = (AppCompatImageView)view.findViewById(R.id.iv_device_img);
            GlideApp.with(this)
                    .load(url)
                    .into(ivBg);
            adapter.addHeaderView(view);
        }
    }
    /**
     * 设置设备状态
     */
    protected void setConnectedState(boolean state){
        if (findViewById(R.id.tv_device_connected) instanceof  TextView){
            ((TextView) findViewById(R.id.tv_device_connected)).setText(getString(R.string.device_new_connected));
        }
        findViewById(R.id.tv_device_connected).setVisibility(state ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}