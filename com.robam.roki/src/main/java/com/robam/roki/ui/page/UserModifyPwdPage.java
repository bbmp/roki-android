package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class UserModifyPwdPage extends HeadPage {

    @InjectView(R.id.edtOldPwd)
    EditText edtOldPwd;
    @InjectView(R.id.edtNewPwd1)
    EditText edtNewPwd1;
    @InjectView(R.id.edtNewPwd2)
    EditText edtNewPwd2;

    User user;
    boolean hasPwd;

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        user = getArguments().getParcelable(PageArgumentKey.User);
        hasPwd = user.hasPassword();

        View view = layoutInflater.inflate(R.layout.page_user_info_modify_pwd, viewGroup, false);
        ButterKnife.inject(this, view);

        title = hasPwd ? "设置密码" : "修改密码";
        edtOldPwd.setVisibility(hasPwd ? View.VISIBLE : View.GONE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @OnClick(R.id.txtConfirm)
    public void onClick() {
        try {
            onConfirm();
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    void onConfirm() {
        String oldPwd = edtOldPwd.getText().toString();
        String newPwd = edtNewPwd1.getText().toString();
        String newPwd2 = edtNewPwd2.getText().toString();

        if (hasPwd) {
            Preconditions.checkState(!Strings.isNullOrEmpty(oldPwd), "原密码不可为空");
        } else {
            oldPwd = "";
        }

        Preconditions.checkState(!Strings.isNullOrEmpty(newPwd), "新密码不可为空");
        Preconditions.checkState(newPwd.length() >= 6, "密码至少6位");
        Preconditions.checkState(newPwd.equals(newPwd2), "两次密码不一致");

        oldPwd = User.encryptPassword(oldPwd);
        final String pwd = User.encryptPassword(newPwd);

        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.updatePassword(user.id, oldPwd, pwd, new VoidCallback() {
            @Override
            public void onSuccess() {
                ProgressDialogHelper.setRunning(cx, false);
                UIService.getInstance().popBack();
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

}
