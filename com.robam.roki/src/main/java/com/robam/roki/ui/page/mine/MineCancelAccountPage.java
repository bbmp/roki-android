package com.robam.roki.ui.page.mine;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.DialogUtil;


/**
 * desc : 注销账户界面(提示页)
 *
 * @author hxw
 */
public class MineCancelAccountPage extends MyBasePage<MainActivity> {



    @Override
    protected int getLayoutId() {
        return R.layout.page_mine_cancel_account;
    }

    @Override
    protected void initView() {
        setTitle(R.string.my_cancel_account);
        getTitleBar().setOnTitleBarListener(this);
        setOnClickListener(R.id.btn_logoff);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_logoff){
            accout_logout_setting();
        }
    }

    private void accout_logout_setting() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_26);
        dialog.setTitleText(R.string.user_cancel_account_dialog);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UIService.getInstance().postPage(PageKey.MineCancelAccountPage2 ,getBundle());
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
