package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rosicky on 15/12/12.
 */
public class SteamOvenCancelWorkDialog extends AbsDialog {

    @InjectView(R.id.txtOne)
    TextView txtOne;
    @InjectView(R.id.start)
    TextView start;

    private Listener listener;

    public interface Listener {
        void onClick();
    }

    public SteamOvenCancelWorkDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_cancel_work;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public void setText(String one, String two) {
        if (one != null) txtOne.setText(one);
    }

    static public void show(Context cx, String one, String two, Listener listener) {
        SteamOvenCancelWorkDialog dlg = new SteamOvenCancelWorkDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.setText(one, two);
        dlg.setListener(listener);
        dlg.show();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.start)
    public void onClickStart() {
        dismiss();
        UIService.getInstance().returnHome();
        listener.onClick();
    }
}
