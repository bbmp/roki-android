package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class Fan8700RemindSoupNoticeDialog extends AbsDialog {
    @InjectView(R.id.fan8700_remindsoup_title1)
    TextView fan8700_remindsoup_title1;
    @InjectView(R.id.fan8700_remindsoup_title2)
    TextView fan8700_remindsoup_title2;
    @InjectView(R.id.fan8700_remindsoupsetting_tv_confirm)
    Button fan8700_remindsoupsetting_tv_confirm;
    public static Fan8700RemindSoupNoticeDialog dlg;
    Context cx;
    private String title1;
    private String title2;
    private String btntitle;
    public Fan8700RemindSoupNoticeDialog(Context context, String title1,String title2,String btnTitle) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.title1=title1;
        this.title2=title2;
        this.btntitle=btnTitle;
        init();
    }

    private void init() {
        fan8700_remindsoup_title1.setText(title1);
        fan8700_remindsoup_title2.setText(title2);
        fan8700_remindsoupsetting_tv_confirm.setText(btntitle);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_fan8700_remindsoup_notice;
    }
    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.fan8700_remindsoupsetting_tv_confirm)//确认
    public void onClickConfirm() {
        if(listener!=null){
            listener.onConfirm();
        }
        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }

    static public Fan8700RemindSoupNoticeDialog show(Context context, String title1, String title2
    ,String btntitle) {
        dlg = new Fan8700RemindSoupNoticeDialog(context,title1,title2,btntitle);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

    public interface PickListener {
        void onCancel();
        void onConfirm();
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
