package com.robam.roki.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.roki.R;
import com.robam.roki.ui.page.RecipeShowPage;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/17.
 */
public class CookCompletedDialog extends AbsDialog {

    public static CookCompletedDialog show(Context cx, long bookId) {
        CookCompletedDialog dlg = new CookCompletedDialog(cx, bookId);
        dlg.show();
        return dlg;
    }

    long bookId;

    public CookCompletedDialog(Context cx, long bookId) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
        this.bookId = bookId;
    }

    @OnClick(R.id.toShow)
    public void onClickShow() {
        this.dismiss();
        TaskService.getInstance().postUiTask(new Runnable() {
            @Override
            public void run() {
                onShowCooking();
            }
        }, 500);

    }

    @OnClick(R.id.toClose)
    public void onClickClose() {
        this.dismiss();
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_cook_completed;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }


    void onShowCooking() {
        Activity atv = UIService.getInstance().getTop().getActivity();
        RecipeShowPage.showCooking(atv, bookId);

    }
}
