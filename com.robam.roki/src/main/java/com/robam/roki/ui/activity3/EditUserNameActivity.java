package com.robam.roki.ui.activity3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.hjq.toast.ToastUtils;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.events.UserUpdatedEvent;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.robam.roki.R;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.request.api.DeviceApi;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.utils.StringUtil;

/**
 * author : huxw
 * time   : 2018/10/18
 * desc   : 编辑用户名称
 */
public final class EditUserNameActivity extends AppActivity {
    public static String DEVICE_NAME = "device_name";
    /**
     * 用户信息
     */
    User user;
    /**
     * 用户昵称
     */
    private ClearEditText etUserName;
    private ImageView ivBack;
    private Button btFinish;


    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_edit_user_name;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.img_back);
        btFinish = findViewById(R.id.btn_finish);
        etUserName = findViewById(R.id.et_user_name);
        setOnClickListener(R.id.img_back, R.id.btn_finish);
    }

    @Override
    protected void initData() {
        user = Plat.accountService.getCurrentUser();
        etUserName.setText(null == user.name ? "" :user.name);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(ivBack))
            finish();
        else if (view.equals(btFinish)) {
            if (StringUtil.isEmpty(etUserName.getText().toString())){
                ToastUtils.show(R.string.input_user_name);
                return;
            }
            setUserInfo(etUserName.getText().toString());
        }
    }

    @Override
    public void onLeftClick(View view) {
        super.onLeftClick(view);
//        finish();
    }

    @Override
    public void onRightClick(View view) {
        if (StringUtil.isEmpty(etUserName.getText().toString())){
            ToastUtils.show(R.string.device_name_message);
            return;
        }
        setUserInfo(etUserName.getText().toString());
    }

    /**
     * 设置昵称
     * @param name
     */
    private void setUserInfo(String  name) {
        ProgressDialogHelper.setRunning(this, true);
        Plat.accountService.updateUser(user.id, name, user.phone, user.email, user.gender, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("设置成功");
                ProgressDialogHelper.setRunning(EditUserNameActivity.this, false);
                user.name = etUserName.getText().toString();
                EventUtils.postEvent(new UserUpdatedEvent(user));
                setResult(Activity.RESULT_OK ,new Intent().putExtra(DEVICE_NAME , etUserName.getText().toString()));
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(EditUserNameActivity.this, false);
                ToastUtils.show(t.getMessage());
            }
        });

    }
}