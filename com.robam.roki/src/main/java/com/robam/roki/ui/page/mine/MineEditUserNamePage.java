package com.robam.roki.ui.page.mine;

import android.view.View;
import android.widget.EditText;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.widget.view.ClearEditText;
import com.robam.roki.utils.StringUtil;


/**
 * desc : 设置用户昵称
 *
 * @author hxw
 */
public class MineEditUserNamePage extends MyBasePage<MainActivity> {

    /**
     * 用户信息
     */
    User user;
    /**
     * 用户昵称
     */
    private ClearEditText etUserName;

    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_edit_user_name;
    }

    @Override
    protected void initView() {
        setTitle(R.string.user_info);
        getTitleBar().setOnTitleBarListener(this);
        etUserName = (ClearEditText) findViewById(R.id.et_user_name);
    }

    @Override
    protected void initData() {
        user = Plat.accountService.getCurrentUser();
        etUserName.setText(null == user.name ? "" :user.name);
    }

    /**
     * 完成
     *
     * @param view 被点击的右项View
     */
    @Override
    public void onRightClick(View view) {
        if (StringUtil.isEmpty(etUserName.getText().toString())){
            ToastUtils.show(R.string.input_user_name);
            return;
        }
        setUserInfo(etUserName.getText().toString());
    }
    /**
     * 设置昵称
     * @param name
     */
    private void setUserInfo(String  name) {
        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.updateUser(user.id, name, user.phone, user.email, user.gender, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("设置成功");
                UIService.getInstance().popBack();
                ProgressDialogHelper.setRunning(cx, false);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.show(t.getMessage());
            }
        });
    }
    /**
     * 返回
     *
     * @param view 被点击的左项View
     */
    @Override
    public void onLeftClick(View view) {
        UIService.getInstance().popBack();
    }

}
