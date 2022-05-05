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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/11.
 */

public class DialogMicrowave526More extends AbsDialog {
    @InjectView(R.id.micro_product_desc)
    LinearLayout productInfo;
    @InjectView(R.id.micro_load)
    LinearLayout load;
    @InjectView(R.id.micro_server)
    LinearLayout server;
    @InjectView(R.id.micro_chat_online)
    LinearLayout chat;
    String guid;
    static DialogMicrowave526More dlg;
    Context cx;

    public DialogMicrowave526More(Context context,String guid) {
        super(context,R.style.Dialog_Microwave_professtion_bottom);
        this.guid=guid;
        this.cx=context;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave526_more;
    }
    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public static Dialog show(Context cx, String guid) {
        dlg = new DialogMicrowave526More(cx,guid);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = screenHeight/2;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }



    @OnClick(R.id.micro_product_desc)
    public void onClickProduct(){
       // ToastUtils.show("我是产品信息", Toast.LENGTH_SHORT);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid,guid);
        bd.putBoolean(PageArgumentKey.IfDeleteInDeviceDetail, false);
        UIService.getInstance().postPage(PageKey.DeviceDetail, bd);
        dlg.dismiss();
    }

    @OnClick(R.id.micro_load)
    public void onClickLoad(){
        ToastUtils.show("暂未上架", Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.micro_server)
    public void onClickServer(){
      //  ToastUtils.show("我是售后服务", Toast.LENGTH_SHORT);
        Uri uri = Uri.parse(String.format("tel:%s","95105855"));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        cx.startActivity(it);
        dlg.dismiss();
    }

    @OnClick(R.id.micro_chat_online)
    public void onClickChat(){
      //  ToastUtils.show("我是在线咨询", Toast.LENGTH_SHORT);
        UIService.getInstance().postPage(PageKey.MessageConsulting);
        dlg.dismiss();
    }

    @OnClick(R.id.micro_btn)
    public void onClickCannel(){
        if (dlg!=null&&dlg.isShowing()){
            dlg.dismiss();
        }
    }

}
