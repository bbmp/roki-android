package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
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
 * Created by Administrator on 2017/5/22.
 */

public class DialogWaterPurifier321More extends AbsDialog {
    @InjectView(R.id.micro_product_desc)
    LinearLayout productInfo;
    @InjectView(R.id.micro_server)
    LinearLayout server;
    @InjectView(R.id.water_smart)
    LinearLayout waterSmart;
   /* @InjectView(R.id.waterpurifiy_line3)
    View line3;*/
    static DialogWaterPurifier321More dlg;
    Context cx;
    String guid;
    public DialogWaterPurifier321More(Context context, String guid) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx=context;
        this.guid=guid;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_waterpurifier321_more;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public static Dialog show(Context cx, String guid) {
        dlg = new DialogWaterPurifier321More(cx,guid);
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


    //产品信息
    @OnClick(R.id.micro_product_desc)
    public void onClickProduct(){
        // ToastUtils.show("我是产品信息", Toast.LENGTH_SHORT);
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid,guid);
        bd.putBoolean(PageArgumentKey.IfDeleteInDeviceDetail, false);
        UIService.getInstance().postPage(PageKey.DeviceDetail, bd);
        dlg.dismiss();
    }
    //售后服务
    @OnClick(R.id.micro_server)
    public void onClickServer(){
        //  ToastUtils.show("我是售后服务", Toast.LENGTH_SHORT);
      /*  Uri uri = Uri.parse(String.format("tel:%s","95105855"));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        cx.startActivity(it);*/
        UIService.getInstance().postPage(PageKey.SaleService);
        dlg.dismiss();
    }

    @OnClick(R.id.water_smart)
    public void onClickSmart(){
      //  ToastUtils.show("我是智能设定", Toast.LENGTH_SHORT);
        UIService.getInstance().postPage(PageKey.SmartWaterPurifierParams);
        dlg.dismiss();
    }

    @OnClick(R.id.micro_btn)
    public void onClickCannel(){
        if (dlg!=null&&dlg.isShowing()){
            dlg.dismiss();
        }
    }
}
