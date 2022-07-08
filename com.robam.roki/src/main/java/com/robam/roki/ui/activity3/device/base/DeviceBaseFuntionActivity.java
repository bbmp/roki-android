package com.robam.roki.ui.activity3.device.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.IDevice;
import com.legent.utils.EventUtils;
import com.robam.base.BaseActivity;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.ui.activity3.ToastAction;
import com.robam.roki.ui.extension.GlideApp;

import java.util.List;


/**
 * author :
 * time   : 2022/06/08
 * desc   : 设备功能基类
 */
public abstract class DeviceBaseFuntionActivity extends AppActivity
        {

    public static final String BUNDLE = "bundle" ;
    public static final String FUNCTION = "function" ;

    /**
     * 设备guid
     */
    protected String mGuid;
    /**
     * 设备
     */
    protected IDevice mDevice;
    /**
     * activity携带值
     */
    protected Bundle bundle;
    /**
     * 当前页面功能
     */
    protected DeviceConfigurationFunctions deviceConfigurationFunction ;

    /**
     * 基础数据加载完毕
     */
    protected boolean loadSuccess  = false ;

    @Override
    protected void initLayout() {
        super.initLayout();
    }

    @Override
    protected void initData() {
        EventUtils.regist(this);
         bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            mGuid =  bundle.getString(PageArgumentKey.Guid);
            mDevice = Plat.deviceService.lookupChild(mGuid);
            if (bundle.getSerializable(FUNCTION) != null) {
                deviceConfigurationFunction =  (DeviceConfigurationFunctions) bundle.getSerializable(FUNCTION);
            }
        }
        if (findViewById(R.id.iv_back)!=null) {
            setOnClickListener(R.id.iv_back);
        }
        //功能不为空
//        if (deviceConfigurationFunction != null){
            dealData();
            loadSuccess = true ;
//        }
    }

    /**
     * 显示数据
     */
    protected abstract void dealData();

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            onBackPressed();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}