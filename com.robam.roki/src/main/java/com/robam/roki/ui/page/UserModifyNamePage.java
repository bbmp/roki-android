package com.robam.roki.ui.page;

import android.os.Bundle;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.ui.PageArgumentKey;

/**
 * Created by sylar on 15/6/16.
 */
public class UserModifyNamePage extends AbsModifyTextPage {

    User user;

    @Override
    void initData(Bundle bd) {
        user = bd.getParcelable(PageArgumentKey.User);
        editText.setHint("昵称");
        editText.setText(user.name);
    }

    @Override
    void onConfirm() {

        final String name = editText.getText().toString();
        Preconditions.checkNotNull(name, "昵称不可为空");

        if (Objects.equal(user.name, name)) {
            UIService.getInstance().popBack();
            return;
        }

        ProgressDialogHelper.setRunning(cx, true);
        Plat.accountService.updateUser(user.id, name, user.phone, user.email, user.gender, new VoidCallback() {
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
