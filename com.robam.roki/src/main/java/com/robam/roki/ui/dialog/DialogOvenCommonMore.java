package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/8/16.
 */

public class DialogOvenCommonMore extends AbsDialog {

    @InjectView(R.id.oven_product_desc)
    LinearLayout ovenProductDesc;
    @InjectView(R.id.oven_sale)
    LinearLayout ovenSale;
    @InjectView(R.id.oven_commit)
    LinearLayout ovenCommit;
    @InjectView(R.id.micro_btn)
    Button microBtn;

    String guid;
    static DialogOvenCommonMore dlg;
    Context cx;
    public DialogOvenCommonMore(Context context, String guid) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.guid = guid;
        this.cx = context;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_oven_commen_more;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public static Dialog show(Context cx, String guid) {
        dlg = new DialogOvenCommonMore(cx, guid);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = screenHeight / 2;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }
    //产品信息
    @OnClick(R.id.oven_product_desc)
    public void OnClickProductDesc(){
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid,guid);
        bd.putBoolean(PageArgumentKey.IfDeleteInDeviceDetail, false);
        UIService.getInstance().postPage(PageKey.DeviceDetail, bd);
        dlg.dismiss();
    }
    //反馈建议
    @OnClick(R.id.oven_commit)
    public void OnClickCommit(){
        UIService.getInstance().postPage(PageKey.Chat);
        dlg.dismiss();
    }
    //售后服务
    @OnClick(R.id.oven_sale)
    public void OnClickSaleAfter(){
        Uri uri = Uri.parse(String.format("tel:%s","95105855"));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        cx.startActivity(it);
        dlg.dismiss();
    }

    @OnClick(R.id.micro_btn)
    public void OnClickCannel(){
        if (dlg!=null&&dlg.isShowing()){
            dlg.dismiss();
        }
    }

}
