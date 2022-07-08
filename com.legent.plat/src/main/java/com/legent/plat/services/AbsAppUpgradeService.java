package com.legent.plat.services;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.services.AbsService;
import com.legent.services.DownloadService;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.ToastUtils;

/**
 * Created by sylar on 15/8/17.
 */
abstract public class AbsAppUpgradeService extends AbsService {
    protected String apkName;

    public AbsAppUpgradeService() {
        apkName = Plat.app.getPackageName() + ".apk";
    }

    public void checkAndUpgrade() {
        checkVersion(new Callback<AppVersionInfo>() {
            @Override
            public void onSuccess(AppVersionInfo verInfo) {
                int currentVercode = PackageUtils.getVersionCode(cx);

                if (verInfo.code > currentVercode) {
                    onNewest(verInfo);
                } else {
                    onWithout();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                onCheckFailure(t);
            }
        });
    }

    public void checkVersion(Callback<AppVersionInfo> callback) {
        Plat.commonService.checkAppVersion(callback);
    }


    protected void onNewest(AppVersionInfo verInfo) {
        // 有新版本
        download(verInfo.url, "版本更新", null);
    }

    protected void download(String downUrl, String title, String description) {
        try {
            DownloadService.newAppDownloadTask(Plat.app, getClass().getSimpleName(),
                    downUrl).download(apkName, title, description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onWithout() {
        // 没有新版本
        ToastUtils.showShort("当前已是最新版");
    }

    protected void onCheckFailure(Throwable t) {
        // 检查版本出错
        ToastUtils.showShort("检查更新失败");
    }
}
