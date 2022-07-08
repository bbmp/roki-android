package com.robam.roki.ui.activity3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.robam.base.FragmentPagerAdapter;
import com.robam.roki.R;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.request.api.DeviceApi;
import com.robam.roki.ui.PageArgumentKey;

import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.utils.StringUtil;

/**
 * author : huxw
 * time   : 2018/10/18
 * desc   : 编辑
 */
public final class EditDeviceNameActivity extends AppActivity implements OnRequestListener {

    public static String EDIT_BD = "edit_bd" ;

    public static String DEVICE_NAME = "device_name";
    /**
     * 用户信息
     */
    User user;
    /**
     * 用户昵称
     */
    private ClearEditText etUserName;
    /**
     * 请求api
     */
    private DeviceApi deviceApi;
    private Bundle bundle;
    private String mGuid;
    private IDevice iDevice;
    private ImageView ivBack;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_name;
    }

    @Override
    protected void initView() {
//        setTitle(R.string.device_name);
//        getTitleBar().setOnTitleBarListener(this);
        ivBack = findViewById(R.id.img_back);
        etUserName = (ClearEditText) findViewById(R.id.et_user_name);
        findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.isEmpty(etUserName.getText().toString())){
                    ToastUtils.show(R.string.device_name_message);
                    return;
                }
                setUserInfo(etUserName.getText().toString());
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getBundleExtra(EDIT_BD);
        if (bundle != null){
            mGuid =  bundle.getString(PageArgumentKey.Guid);
            iDevice = Plat.deviceService.lookupChild(mGuid);
        }
        if(iDevice!=null){
            etUserName.setText(iDevice.getName() == null || iDevice.getName().equals(iDevice.getCategoryName()) ? iDevice.getDispalyType() : iDevice.getName());
        }
        user = Plat.accountService.getCurrentUser();
        deviceApi = new DeviceApi(this);
    }

    @Override
    public void onFailure(int requestId, int requestCode, @Nullable String msg, @Nullable Object data) {
        if (requestId == DeviceApi.setDeviceNameCode){
            ProgressDialogHelper.setRunning(this, false);
            ToastUtils.show("设置失败");
        }
    }

    @Override
    public void onSaveCache(int requestId, int requestCode, @Nullable Object paramObject) {

    }

    @Override
    public void onSuccess(int requestId, int requestCode, @Nullable Object paramObject) {
        if (requestId == DeviceApi.setDeviceNameCode){
            ToastUtils.show("设置成功");
            ProgressDialogHelper.setRunning(this, false);
            iDevice.setName(etUserName.getText().toString());
            EventUtils.postEvent(new DeviceNameChangeEvent(iDevice));
            setResult(Activity.RESULT_OK ,new Intent().putExtra(DEVICE_NAME , etUserName.getText().toString()));
            finish();
        }
    }

    @Override
    public void onLeftClick(View view) {
        super.onLeftClick(view);
//        finish();
    }

    @Override
    public void onRightClick(View view) {

    }

    /**
     * 设置昵称
     * @param name
     */
    private void setUserInfo(String  name) {
        ProgressDialogHelper.setRunning(this, true);
        deviceApi.setDeciceName(user.id , mGuid , name);

    }
}