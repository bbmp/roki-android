package com.legent.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;

import com.google.common.eventbus.Subscribe;
import com.legent.events.ActivityResultOnPageEvent;
import com.legent.events.ConnectionModeChangedEvent;
import com.legent.plat.PlatApp;
import com.legent.services.ConnectivtyService;
import com.legent.services.TaskService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.MemoryUtils;
import com.legent.utils.api.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsActivity extends AppCompatActivity implements IForm {

    public final static String WillShowPageKey = "WillShowPageKey";

    protected PlatApp app;
    protected String formKey;
    protected boolean isExit = false;

    abstract protected String createFormKey();

    @Override
    public String getFormKey() {
        return formKey;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        EventUtils.regist(this);
        Bundle bd = getIntent().getExtras();
        String pageKey = null;
        if (bd != null) {
            pageKey = bd.getString(WillShowPageKey);
        }
        formKey = createFormKey();
        LogUtils.i("20170718", "fromKey::" + formKey);
        app = (PlatApp) getApplication();
        app.addActivity(this);
        requestWindowFeature();
        setContentView();
        attachActivity(pageKey);
        initOnCreate();
        if (savedState != null) {
            restoreState(savedState);
        }

        getPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO});

    }

    @Override
    protected void onResume() {
        super.onResume();
        UIService.getInstance().setTopActivity(formKey);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventUtils.unregist(this);
            UIService.getInstance().detachActivity(formKey);
            View rootView = getWindow().getDecorView().findViewById(
                    android.R.id.content);
            MemoryUtils.disposeView(rootView);
            app.removeActivity(this);
        } catch (Exception e) {
            LogUtils.i("20170718", e.toString());
        }

    }


    /**
     * 检查是否拥有指定的所有权限
     */

    protected boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 批量申请权限
     *
     * @param permissionStr 要申请的权限
     * @return true: 允许  false: 拒绝
     */
    protected boolean getPermission(String[] permissionStr) {
        if (permissionStr == null || permissionStr.length == 0) {
            throw new NullPointerException("permissionStr is a not null values!");
        }

        boolean isAllGranted = checkPermissionAllGranted(permissionStr);

        if (isAllGranted) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();

            for (String permission : permissionStr) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(permission);
                }
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 12);
            } else {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 12:
                List<String> cancelPermissions = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        cancelPermissions.add(permissions[i]);
                    }
                }
                if (cancelPermissions.isEmpty()) {   //全部允许
                    getPermissionsResult(true, null);
                } else {                             //有不允许的
                    getPermissionsResult(false, cancelPermissions);
                }
        }

    }

    /**
     * 权限申请结果回调方法，如果想获取申请结果，请在子类中重写该方法即可
     *
     * @param isGranted         true: 全部允许  false: 不允许
     * @param cancelPermissions 如果全部允许则该参数为null, 如果没有全部允许则该参数会返回没有允许的权限
     */
    protected void getPermissionsResult(boolean isGranted, List<String> cancelPermissions) {

        if (!isGranted) {
            ToastUtils.showLong("您拒绝了一些应用需要的权限，可能导致部分功能不能正常使用哦!");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        EventUtils.postEvent(new ActivityResultOnPageEvent(requestCode, resultCode, data));
        LogUtils.i("20190107", "requestCode::" + requestCode + " resultCode  " + resultCode);
    }

    @Subscribe
    public void onEvent(ConnectionModeChangedEvent event) {

        int mode = event.connectionMode;
        switch (mode) {

            case ConnectivtyService.ConnectionMode_Broken:
                onConnectionBroken();
                break;
            case ConnectivtyService.ConnectionMode_Wifi:
                onConnectedByWifi();
                break;
            case ConnectivtyService.ConnectionMode_Mobil:
                onConnectedByMobil();
                break;

            default:
                break;
        }
    }


    protected void onConnectionBroken() {
    }

    protected void onConnectedByWifi() {
    }

    protected void onConnectedByMobil() {
    }

    // -------------------------------------------------------------------------------
    // onCreate
    // -------------------------------------------------------------------------------

    protected void requestWindowFeature() {

    }

    protected void setContentView() {
        // setContentView(R.layout.activity_layout);
        setContentView(R.layout.abs_activity);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        //非默认值
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    protected void attachActivity(String pageKey) {
        UIService.getInstance().attachActivity(formKey, this, pageKey);
    }

    protected void initOnCreate() {

    }

    protected void restoreState(Bundle savedState) {

    }

    // -------------------------------------------------------------------------------
    // onKeyDown
    // -------------------------------------------------------------------------------

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        IPage page = null;
        if (UIService.getInstance().getFormManager(formKey) == null) {

        } else {
            page = UIService.getInstance().getFormManager(formKey).getCurrentPage();
        }

        if (page != null && page.onKeyDown(keyCode, event)) {
            return true;
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_MENU:
                    onKeyDown_Menu();
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    onKeyDown_Back();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
    }


    protected void onKeyDown_Menu() {
        FormManager fm = UIService.getInstance().getFormManager(formKey);
        if (fm != null) {
            boolean isHome = fm.isHome();
            if (isHome) {
                fm.toggleMenu();
            }
        }

    }

    protected void onKeyDown_Back() {

        FormManager fm = UIService.getInstance().getTop();
        if (fm == null) {
            exit();
        } else {
            if (fm.isHome()) {
                exit();
            } else {
                UIService.getInstance().popBack();
            }
        }
    }

    protected void exit() {
        if (!isExit) {
            isExit = true;

            showTipWhenExit();

            TaskService.getInstance().postUiTask(new Runnable() {

                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            app.exit();
        }
    }

    protected void showTipWhenExit() {
        // ToastUtils.showShort(R.string.app_exit);
    }



}
