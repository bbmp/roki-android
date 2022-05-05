package com.robam.roki.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.model.NormalModeItemMsg;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/21.
 */
public class OvenNormalSettingDialog extends AbsDialog {
    @InjectView(R.id.txtOne)
    TextView txtOne;
    @InjectView(R.id.txtTwo)
    TextView txtTwo;
    @InjectView(R.id.txtThree)
    TextView txtThree;
    @InjectView(R.id.txtConfirmButton)
    TextView txtConfirmButton;


    NormalModeItemMsg deviceWorkMsg = null;

    public OvenNormalSettingDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_oven_self_cleaning;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public void setText(String one, String two, String three) {
        if (one != null) txtOne.setText(one);
        if (two != null) txtTwo.setText(two);
        if (three != null) txtThree.setText(three);
    }

    public void setMsg(NormalModeItemMsg msg) {
        deviceWorkMsg = msg;
    }

    static public void show(Context cx, NormalModeItemMsg msg) {
        OvenSelfCleaningDialog dlg = new OvenSelfCleaningDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
//            String one = "", two = null;
//            if (msg.getType().equals("自洁")) {
//                one = "请在凹状加热盘内滴些食醋\n工作45分钟后即可轻松除去水垢";
//                dlg.setIcon(R.mipmap.img_steam_ster_tag);
//            } else if (msg.getType().equals("杀菌")) {
//                dlg.setIcon(R.mipmap.img_steam_clean_tag);
//                one = "即将开启60分钟的\n高温蒸汽杀菌模式";
//            }
        dlg.setMsg(msg);
//            dlg.setText(one, two,three);
        dlg.show();
    }

    @OnClick(R.id.txtConfirmButton)
    public void onClickConfirm() {
        dismiss();
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            Bundle bundle = new Bundle();
            if (deviceWorkMsg != null) {
                bundle.putSerializable("msg", deviceWorkMsg);
            }
//            UIService.getInstance().postPage(PageKey.DeviceSteamOvenWorking, bundle);
        }
    }
}
