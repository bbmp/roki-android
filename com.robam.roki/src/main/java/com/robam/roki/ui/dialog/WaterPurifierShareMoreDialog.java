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
import com.legent.ui.ext.views.TitleBar;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2016/12/23.
 */

public class WaterPurifierShareMoreDialog extends AbsDialog {

   // protected  static int res;
    //产品信息
    @InjectView(R.id.waterpurifiy_trade_desc)
    LinearLayout waterpurifiy_trade_desc;
    //售后服务
    @InjectView(R.id.waterpurifiy_trade_server)
    LinearLayout Waterpurifiy_trade_server;
    //反馈建议
    @InjectView(R.id.waterpurifiy_trade_suggest)
    LinearLayout waterpurifiy_trade_suggest;
    @InjectView(R.id.waterpurifiy_cancel)
    Button waterpurifiy_more;
    String guid;
    protected TitleBar titleBar;
    static WaterPurifierShareMoreDialog dlg;

    public WaterPurifierShareMoreDialog(Context context, String guid) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.guid=guid;
    }
    @Override
    protected int getViewResId() {

        return R.layout.dialog_waterpurifier_more;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }
    //对话框窗体展示
    public static Dialog show(Context cx, String guid) {
        dlg = new WaterPurifierShareMoreDialog(cx, guid);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        dlg.show();
        return dlg;

    }

    //产品信息的点击事件
    @OnClick(R.id.waterpurifiy_trade_desc)
     public void onClickDesc(){

       // Toast.makeText(cx,"产品信息",Toast.LENGTH_SHORT).show();
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid,guid);
        bd.putBoolean(PageArgumentKey.IfDeleteInDeviceDetail, false);
        UIService.getInstance().postPage(PageKey.DeviceDetail, bd);
        dlg.dismiss();
    }
    //售后服务的点击事件
    @OnClick(R.id.waterpurifiy_trade_server)
    public void onClickServer(){
       /* UIService.getInstance().postPage(PageKey.SaleService,null);
        dlg.dismiss();*/
        Uri uri = Uri.parse(String.format("tel:%s","95105855"));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        cx.startActivity(it);
        dlg.dismiss();
    }
    //反馈建议的点击事件
    @OnClick(R.id.waterpurifiy_trade_suggest)
    public void onClickSuggest(){
        UIService.getInstance().postPage(PageKey.OnLineChat);
        dlg.dismiss();
    }
    //取消的点击事件
    @OnClick(R.id.waterpurifiy_cancel)
    public void onClickCancel(){

        dlg.dismiss();
    }



}
