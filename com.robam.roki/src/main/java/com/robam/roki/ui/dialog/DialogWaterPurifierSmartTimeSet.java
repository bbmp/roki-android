package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.common.collect.Lists;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.view.DeviceNormalSettingTimeWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/23.
 */

public class DialogWaterPurifierSmartTimeSet extends AbsDialog {

    @InjectView(R.id.wv2)
    DeviceNormalSettingTimeWheel wv2;
    private List<Integer> list = Lists.newArrayList();
    static DialogWaterPurifierSmartTimeSet dlg;

    public DialogWaterPurifierSmartTimeSet(Context context) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_water321_smart_timeset;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        list=getListDate();
        wv2.setData(list);
    }

    public List<Integer> getListDate(){
        List<Integer> list = Lists.newArrayList();
        for (int i = 10; i <=60 ; i=i+10) {
            list.add(i);
        }
        return list;
    }

    public static Dialog show(Context cx) {
        dlg = new DialogWaterPurifierSmartTimeSet(cx);
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

    @OnClick(R.id.smart_time_cancel)
    public void onClickCancel(){
        if (dlg!=null&&dlg.isShowing()){
            dlg.dismiss();
        }
    }

    @OnClick(R.id.smart_time_confirm)
    public void onClickConfirm(){
        if (listener != null) {
            LogUtils.i("20170524","wv2:"+wv2.getSelectedTag());
            listener.onConfirm(Short.valueOf(String.valueOf(wv2.getSelectedTag())));
        }

        if(dlg!=null&&dlg.isShowing())
            dlg.dismiss();
    }

    //接口回调
    public interface PickListener {

        void onCancel();

        void onConfirm(short value);
    }

    private DialogWaterPurifierSmartTimeSet.PickListener listener;

    public void setPickListener(DialogWaterPurifierSmartTimeSet.PickListener listener) {
        this.listener = listener;
    }
}
