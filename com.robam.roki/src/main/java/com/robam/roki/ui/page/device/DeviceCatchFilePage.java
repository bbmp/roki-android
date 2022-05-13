package com.robam.roki.ui.page.device;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DataUtils;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

/**
 * Created by 14807 on 2018/8/15.
 */

public abstract class DeviceCatchFilePage extends BasePage {
    protected String mGuid;
    protected String mVersion = null;
    protected IDevice mDevice;
    protected String mDt;
    protected String mDc;
    protected long mUserId = Plat.accountService.getCurrentUserId();
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setDataToView((Reponses.DeviceResponse) msg.obj);
                    break;
                case 100:
                    updateTask();
                    break;
                default:
                    break;
            }
        }
    };
    private long userId;

    protected void updateTask(){

    }

    protected abstract void setDataToView(Reponses.DeviceResponse obj);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        LogUtils.i("202010241100","mGuid:::"+mGuid);
        mDevice = Plat.deviceService.lookupChild(mGuid);
    }

    /**
     * 缓存基类初始化数据
     */
    protected void initData() {
        userId = Plat.accountService.getCurrentUserId();
        mDt = mDevice.getDt();
        mDc = mDevice.getDc();
        mVersion = DataUtils.readJson(cx,"version"+mDt);
        LogUtils.i("20180815", " mDt:" + mDt + " mDc:" + mDc);
        LogUtils.i("20180815", "mUserId:" + mUserId);
        LogUtils.i("2020061003", "guid1:" + mDevice.getGuid().toString());
        LogUtils.i("2020061003", "guid2:" + mDevice.getGuid().getGuid());

        if (mVersion == null) {
            getDataMethod();
        } else {
            getVersionMethod();
        }
    }

    /**
     * 缓存基类获取版本号
     */
    protected void getVersionMethod() {
        CloudHelper.getCheck(mDt, mVersion, new Callback<Reponses.GetCheckResponse>() {
            @Override
            public void onSuccess(Reponses.GetCheckResponse getCheckResponse) {
                if (getCheckResponse == null) return;
                LogUtils.i("20180913"," getCheckResponse:" + getCheckResponse.isLast);
                if (!getCheckResponse.isLast) {//false是最新
                    getDataMethod();
                } else {//不是最新的不需要去服务器请求加载本地文件
                    readJson();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                readJson();
            }
        });
    }

    private void readJson(){
        String deviceData = DataUtils.readJson(cx, mDt);
        Reponses.DeviceResponse deviceResponse = null;
        try {
            deviceResponse = JsonUtils.json2Pojo(deviceData, Reponses.DeviceResponse.class);
            if (deviceResponse != null){
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = deviceResponse;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存基类获取数据
     */
    protected void getDataMethod() {
        CloudHelper.getDeviceByParams(mUserId, mDt, mDc, new Callback<Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;
                DataUtils.writeJson(cx,deviceResponse.version,"version"+mDt,false);
                DataUtils.writeJson(cx, deviceResponse.toString(), mDt, false);
                try {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = deviceResponse;
                    mHandler.sendMessage(msg);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}
