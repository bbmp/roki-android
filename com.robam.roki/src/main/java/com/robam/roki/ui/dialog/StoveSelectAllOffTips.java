package com.robam.roki.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/1/24.
 */

public class StoveSelectAllOffTips extends AbsDialog {
    public interface AllOffTipsDialogLister{
        void onConfirm(Integer stoveHeadId);
    }

    private AllOffTipsDialogLister lister;

    public void setAllOffTipsDialogLister(AllOffTipsDialogLister lister){
        this.lister = lister;
    }


    public static StoveSelectAllOffTips dlg;

    Context cx;
    Stove stove;

    public StoveSelectAllOffTips(Context context, Stove stove) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.stove = stove;
    }

    @OnClick(R.id.confirm)
    public void onClickCannel() {
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventUtils.unregist(this);
    }

    static public StoveSelectAllOffTips show(Context cx, Stove stove) {
        dlg = new StoveSelectAllOffTips(cx,stove);
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

    @Override
    protected int getViewResId() {
        return R.layout.dialog_stove_tips_show;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        EventUtils.regist(this);
    }

    Integer tm;

    private void startCook(Stove.StoveHead stoveHead) {
        if (lister != null) {
            if (stoveHead == stove.rightHead) {
                tm = 1;  //1代表右炉头
            } else {
                tm = 0;//0代表左炉头
            }
            lister.onConfirm(tm);
            if (dlg != null && dlg.isShowing()) {
                //EventUtils.unregist(this);
                dlg.dismiss();
            }
        }
    }

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("2018122","event:"+event.pojo.getDp()+" "+event.pojo.rightHead.level);
        if (!TextUtils.equals(stove.getGuid().getGuid(), event.pojo.getGuid().getGuid())) {
            return;
        }
        if (DeviceType.RRQZ.equals(stove.getDp())){
            if (stove.leftHead != null && (stove.leftHead.status == StoveStatus.Working&&stove.leftHead.level>=1)) {
                startCook(stove.leftHead);
            } else if (stove.rightHead != null && (stove.rightHead.status == StoveStatus.Working&&stove.rightHead.level>=1)) {
                startCook(stove.rightHead);
            }
        }else if(IRokiFamily.RQZ02.equals(stove.getDp())){
            if (stove.leftHead != null && (stove.leftHead.status !=StoveStatus.Off&&stove.leftHead.level>=1)) {
                startCook(stove.leftHead);
            } else if (stove.rightHead != null && (stove.rightHead.status !=StoveStatus.Off&&stove.rightHead.level>=1)) {
                startCook(stove.rightHead);
            }
        }else{
            if (stove.leftHead != null && stove.leftHead.status == StoveStatus.StandyBy) {
                startCook(stove.leftHead);
            } else if (stove.rightHead != null && stove.rightHead.status == StoveStatus.StandyBy) {
                startCook(stove.rightHead);
            }
        }

    }
}
