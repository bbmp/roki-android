package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.plat.constant.IPlatRokiFamily;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/2/2.
 */

public class StoveSelectTipsDialog extends AbsDialog{
    public interface StoveSelectTipsDialogLister{
        void onConfirm(Integer flag);
    }

    private StoveSelectTipsDialogLister lister;

    public void setStoveSelectTipsDialogLister(StoveSelectTipsDialogLister lister){
        this.lister = lister;
    }

    public static StoveSelectTipsDialog dlg;

    Context cx;
    static Stove.StoveHead stoveHeadId;

    public StoveSelectTipsDialog(Context context, Stove stove, Integer integer) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        if (integer==0){
            stoveHeadId = stove.leftHead;
        }else{
            stoveHeadId = stove.rightHead;
        }

    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_stove_tips_show;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        EventUtils.regist(this);
    }

    @OnClick(R.id.confirm)
    public void onClickCannel() {
        /*if (lister != null) {
            lister.onConfirm(0);
        }*/
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventUtils.unregist(this);
    }

    static public StoveSelectTipsDialog show(Context cx, Stove stove, Integer integer) {
        dlg = new StoveSelectTipsDialog(cx,stove,integer);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = screenHeight/2;
        win.setAttributes(wl);
        TextView textView = dlg.findViewById(R.id.desc_txt);
        if (stoveHeadId.status == StoveStatus.Working) {
            textView.setText(R.string.recipe_stove_head_is_working);
        } else {
            textView.setText(R.string.recipe_can_not_open);
        }
        dlg.show();
        return dlg;
    }

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("20181223333","event dt:"+event.pojo.getDt());
        LogUtils.i("20181223333","event dp:"+event.pojo.getDp());
        if (IPlatRokiFamily.RQZ02.equals(event.pojo.getDp())){
            if(stoveHeadId==event.pojo.leftHead){
                if (stoveHeadId.getStatus()!=0&&stoveHeadId.level>=1){
                    if (lister!=null){
                        lister.onConfirm(0);
                        if (dlg != null && dlg.isShowing()) {
                            //  EventUtils.unregist(this);
                            dlg.dismiss();
                        }
                    }
                }
            }
            if (stoveHeadId== event.pojo.rightHead){
                if (stoveHeadId.getStatus()!=0&&stoveHeadId.level>=1){
                    if (lister!=null){
                        lister.onConfirm(0);
                        if (dlg != null && dlg.isShowing()) {
                            //  EventUtils.unregist(this);
                            dlg.dismiss();
                        }
                    }
                }
            }
        }else{
            if(stoveHeadId==event.pojo.leftHead){
                if (stoveHeadId.getStatus()!=0){
                    if (lister!=null){
                        lister.onConfirm(0);
                        if (dlg != null && dlg.isShowing()) {
                            //  EventUtils.unregist(this);
                            dlg.dismiss();
                        }
                    }
                }
            }
            if (stoveHeadId== event.pojo.rightHead){
                if (stoveHeadId.getStatus()!=0){
                    if (lister!=null){
                        lister.onConfirm(0);
                        if (dlg != null && dlg.isShowing()) {
                            //  EventUtils.unregist(this);
                            dlg.dismiss();
                        }
                    }
                }
            }
        }

    }
}






