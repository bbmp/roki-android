package com.legent.plat.io.device.finder;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListenableFuture;
import com.legent.Callback;
import com.legent.Helper;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.io.device.IDeviceFinder;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.services.AbsTask;
import com.legent.services.TaskService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;

/**
 * 上海庆科wifi模块激活器
 *
 * @author sylar
 */
public class MxchipFinder implements IDeviceFinder {

    private IEasyLink easyLink;
    private ListenableFuture<DeviceInfo> future;

    @Override
    public void start(String wifiSsid, String wifiPwd, int timeout,
                      Callback<DeviceInfo> callback) {
        Task task = new Task(wifiSsid, wifiPwd, timeout, callback);
        future = TaskService.getInstance().postAsyncTask(task);
    }

    @Override
    public void stop() {
        stopEasyLink();
        if (future != null) {
            future.cancel(true);
        }

        future = null;
    }

    private void startEasyLink(String ssid, String pwd) {
        Preconditions.checkState(Plat.accountService.isLogon(),
                "请先登录");
        long userId = Plat.accountService.getCurrentUser().id;
        String userInfo = getUserInfo4Easylink(userId);

        if (easyLink == null) {
            easyLink = EasyLink31.getInstance();
        }
        easyLink.start(ssid, pwd, userInfo);
    }

    private void stopEasyLink() {
        if (easyLink != null) {
            easyLink.stop();
        }
    }

    private String getUserInfo4Easylink(long userId) {
        String strUserId = String.valueOf(userId);
        strUserId = Strings.padEnd(strUserId, 10, '\0');

        return String.format("%s%s", Plat.appGuid, strUserId);
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
                LogUtils.i("2020031307", "e:" + e.getMessage());
            Helper.onFailure(callback, e.getCause());
        }

        @Override
        public DeviceInfo doInBackground(Object... params) throws Exception {
            try {

                EventUtils.regist(this);
                startEasyLink(ssid, pwd);

                // 等待发现通知
                waitSelf(timeout);
                // 检查搜索结果
                Preconditions.checkState(result != null, "添加厨电失败，请确认\n" +
                        "1.WIFI密码正确\n" +
                        "2.厨电一键连功能激活");

            } finally {
                EventUtils.unregist(this);
                stopEasyLink();
            }

            return result;
        }

    }

}
