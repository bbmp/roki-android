package com.robam.roki.utils;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;

/**
 * Created by Administrator on 2017/8/29.
 */

public class LoginUtil {

    public static boolean checkWhetherLogin(Context cx, final String pageKey) {
        boolean auth = Plat.accountService.isLogon();
        if (auth) {
            return true;
        } else {

            final IRokiDialog rokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
            rokiDialog.setTitleText(R.string.user_Whether_not_login);
            rokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rokiDialog.dismiss();
                    UIService.getInstance().postPage(pageKey);
                }
            });
            rokiDialog.show();
            return false;
        }
    }

    /**
     * 判断是否登录 回调登录选择
     * @param cx
     * @return
     */
    public static boolean checkWhetherLogin2(Context cx, final FragmentActivity activity) {
        boolean auth = Plat.accountService.isLogon();
        if (auth) {
            return true;
        } else {

            final IRokiDialog rokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_26);
            rokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
            rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rokiDialog.dismiss();
                    startLogin(activity);
                }
            });
            rokiDialog.show();
            return false;
        }
    }

    /**
     * 指向登录界面
     */
    private static void  startLogin(FragmentActivity activity){
        CmccLoginHelper instance = CmccLoginHelper.getInstance();
        instance.initSdk(activity );
        instance.getPhnoeInfo();
    }
}
