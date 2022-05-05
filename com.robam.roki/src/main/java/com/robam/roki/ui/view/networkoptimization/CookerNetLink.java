package com.robam.roki.ui.view.networkoptimization;



import android.widget.Toast;

import com.cook.config.ConfigCallback;
import com.cook.config.ConfigManager;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListenableFuture;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.io.device.ICookerLink;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.services.AbsTask;
import com.legent.services.TaskService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;

/**
 * Created by Dell on 2018/6/14.
 */

public class CookerNetLink implements ICookerLink {

    private ListenableFuture<DeviceInfo> future;
    @Override
    public void start(String wifiSsid, String wifiPwd, int timeout, Callback<DeviceInfo> callback) {
        Task task = new Task(wifiSsid, wifiPwd, timeout, callback);
        future = TaskService.getInstance().postAsyncTask(task);
    }

    @Override
    public void stop() {
        stopLink();
        if (future != null) {
            future.cancel(true);
        }

        future = null;
    }

    private void startLink(String ssid, String pwd) {
        Preconditions.checkState(Plat.accountService.isLogon(),
                "请先登录");
        long userId = Plat.accountService.getCurrentUser().id;
        String appId = Plat.appGuid.substring(5,Plat.appGuid.length());
        LogUtils.i("20180707","ssid:"+ssid+" pwd:"+pwd+" plat:"+Plat.appType+" appId:"+appId+
        " userId:"+String.valueOf(userId));
       // ToastUtils.show(ssid+":"+pwd+":"+appId,Toast.LENGTH_SHORT);
        ConfigManager.instance().startConfig(ssid, pwd, Plat.appType, appId, String.valueOf(userId), new ConfigCallback() {
            @Override
            public void onResult(int code) {

                if (code == 0) {
                   // ToastUtils.show("配网成功", Toast.LENGTH_LONG);
                } else if (code == 1) {
                   // ToastUtils.show("主动停止配网", Toast.LENGTH_LONG);
                } else {
                    ToastUtils.show("超时", Toast.LENGTH_LONG);
                }
            }
        });

    }

    private void stopLink() {
        ConfigManager.instance().stopConfig();
    }

    class Task extends AbsTask<DeviceInfo> {

        private String ssid, pwd;
        private int timeout;
        private Callback<DeviceInfo> callback;
        private boolean findFlag;

        public Task(String ssid, String pwd, int timeout,
                    Callback<DeviceInfo> callback) {
            this.ssid = ssid;
            this.timeout = timeout;
            this.pwd = pwd;
            this.callback = callback;
        }

        @Subscribe
        public void onEvent(DeviceFindEvent event) {

            // 非搜索期间，即使收到回应，也不处理
            if (!isRunning)
                return;

            // 如果已经发现一个，则忽略其它的回应
            if (findFlag)
                return;

            findFlag = true;
            DeviceInfo device = event.deviceInfo;
            device.ownerId = Plat.accountService.getCurrentUserId();
            notifySelfOnSuccess(device);
        }

        @Override
        public void onSuccess(DeviceInfo result) {
            super.onSuccess(result);
            Helper.onSuccess(callback, result);
        }

        @Override
        public void onFailure(Throwable e) {
            super.onFailure(e);
            if (Plat.DEBUG)
                LogUtils.i("20170324", "e:" + e.getMessage());
            Helper.onFailure(callback, e.getCause());
        }

        @Override
        public DeviceInfo doInBackground(Object... params) throws Exception {
            try {

                EventUtils.regist(this);
                 startLink(ssid,pwd);

                // 等待发现通知
                waitSelf(timeout);

                // 检查搜索结果
                Preconditions.checkState(result != null, "添加厨电失败，请确认\n" +
                        "1.WIFI密码正确\n" +
                        "2.厨电一键连功能激活");

            } finally {
                EventUtils.unregist(this);
                stopLink();
            }

            return result;
        }

    }
}
