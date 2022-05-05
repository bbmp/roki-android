package com.robam.roki.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.ViewUtils;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.CrmCustomer;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/20.
 */
public class MaintainHomeDialog extends AbsDialog {

    static public void show(Context cx) {
        MaintainHomeDialog dlg = new MaintainHomeDialog(cx);
        dlg.show();
    }

    public MaintainHomeDialog(Context cx) {
        super(cx, R.style.maintain_home_dialog_style);
        ViewUtils.setFullScreen(cx, this);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_maintain_home;
    }

    @OnClick(R.id.layout)
    public void onClick() {
        dismiss();
    }


    @OnClick(R.id.txtOK)
    public void onClickOK() {
        onOK();
    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    void onOK() {
        dismiss();
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            String phone = Plat.accountService.getCurrentUser().phone;
            RokiRestHelper.getCrmCustomer(phone, new Callback<CrmCustomer>() {
                @Override
                public void onSuccess(CrmCustomer customer) {
                    if (customer != null && customer.products != null && customer.products.size() > 0) {
                        Bundle bd = new Bundle();
                        bd.putParcelable(PageArgumentKey.CrmCustomer, customer);
                        UIService.getInstance().postPage(PageKey.MaintainRequest, bd);
                    } else {
                        MaintainFailedDialog.show(cx);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.showThrowable(t);
                }
            });
        }
    }
}
