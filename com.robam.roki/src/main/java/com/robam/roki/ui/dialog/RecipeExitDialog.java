package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by WZTCM on 2015/12/26.
 */
public class RecipeExitDialog extends AbsDialog {

    @InjectView(R.id.txtTitle)
    TextView textTitle;
    @InjectView(R.id.showCook)
    TextView showCook;
    @InjectView(R.id.txtExit)
    TextView txtExit;

    Listener listener;

    public interface Listener {
        void onShowCook();

        void onExit();
    }

    public RecipeExitDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_recipe_exit;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    private void setListener(Listener listener) {
        this.listener = listener;
    }

    static public void show(Context cx, Listener listener) {
        RecipeExitDialog dlg = new RecipeExitDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.setListener(listener);
        dlg.show();
    }


    @OnClick(R.id.showCook)
    public void onClickShow() {
        listener.onShowCook();
    }

    @OnClick(R.id.txtExit)
    public void onClickExit() {
        dismiss();
        listener.onExit();
    }
}
