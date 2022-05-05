package com.robam.common.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.events.PageChangedEvent;
import com.legent.plat.Plat;
import com.legent.plat.pojos.AppVersionInfo;
import com.legent.services.AbsUpdateService;
import com.legent.services.DownloadService;
import com.legent.ui.UI;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.common.R;


@SuppressLint("InflateParams")
public class AppUpdateService extends AbsUpdateService {

    private static AppUpdateService instance = new AppUpdateService();
    private AlertDialog dlg;

    synchronized public static AppUpdateService getInstance() {
        return instance;
    }

    Context cx;
    Resources r;
    String apkName;

    private AppUpdateService() {
        apkName = String.format("%s.apk", Plat.app.getPackageName());
    }

    @Override
    public void checkVersion(final Context cx, final CheckVersionListener listener) {
        this.cx = cx;
        r = cx.getResources();

        checkVersion(new Callback<AppVersionInfo>() {
            @Override
            public void onSuccess(AppVersionInfo verInfo) {
                if (listener == null)
                    return;

                if (verInfo == null) {
                    listener.onWithoutNewest();
                } else {
                    int currentVercode = PackageUtils.getVersionCode(cx);
                    if (verInfo.code > currentVercode) {
                        if (Strings.isNullOrEmpty(verInfo.desc))
                            listener.onWithNewest(verInfo.url);
                        else
                            listener.onWithNewest(verInfo.url, verInfo.desc);
                    } else {
                        listener.onWithoutNewest();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (listener != null) {
                    listener.onCheckFailure(new Exception(t));
                }
            }
        });
    }

    public void checkVersion(Callback<AppVersionInfo> callback) {
        Plat.commonService.checkAppVersion(callback);
    }


    public Runnable updatePollingTask = new Runnable() {

        @Override
        public void run() {
            AppUpdateService.getInstance().start(cx);
        }
    };


    @Override
    protected void download(String downUrl) {
        ToastUtils.showLong(R.string.update_downloading);

        String title = r.getString(R.string.update_title);
        String description = r.getString(R.string.update_description);

        try {
            DownloadService.newAppDownloadTask(cx, getClass().getSimpleName(),
                    downUrl).download(apkName, title, description);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("20190815_download_problem",e.getMessage());
        }
    }

    @Override
    protected void onNewest(final String downUrl, Object... params) {
        String title = r.getString(R.string.update_title);
        String desc = null;
        if (params != null && params.length > 0) {
            if (params[0] != null)
                desc = params[0].toString();
        }

        LayoutInflater inflater = (LayoutInflater) cx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_upgrade_newest, null);
        final TextView txtDesc = view.findViewById(R.id.txtVerDesc);
        txtDesc.setText(desc);
//        txtDesc.setTextColor(r.getColor(R.color.White));

        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewUtils.setDialogShowField(dialog, true);

                if (which == DialogInterface.BUTTON_POSITIVE) {
                    download(downUrl);
                }
            }
        };

        Builder builder = new Builder(cx);
//        builder.setTitle(title);
        builder.setView(view);
//        builder.setNegativeButton("取消", clickListener);
//        builder.setPositiveButton("确定", clickListener);
        view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.setDialogShowField(dlg, true);
                if (dlg != null){
                    dlg.dismiss();
                }

            }
        });
        view.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.setDialogShowField(dlg, true);
                if (dlg != null){
                    dlg.dismiss();
                }
                download(downUrl);
            }
        });

        if (dlg == null || !dlg.isShowing()) {
            dlg = builder.create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                dlg.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
    //                    LogUtils.i("UpdateVersion","cancle dialog");
                        EventUtils.postEvent(new PageChangedEvent("dialogshow"));
                    }
                });
            }
            dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
//                    LogUtils.i("UpdateVersion","Dismiss dialog");
                    EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
                }
            });
            dlg.show();
        }
    }

    @Override
    protected void onWithout() {
        String msg = r.getString(R.string.update_newest);
        // 避免在定时获取更新状态时，没有更新时弹出提示，所以注释掉。
        ToastUtils.showShort(msg);
    }

    @Override
    protected void onFailure(Exception ex) {
        String msg = r.getString(R.string.update_check_failure);
        ToastUtils.showShort(msg);
    }

}
