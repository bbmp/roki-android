package com.robam.roki.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.ViewUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/20.
 */
public class OrderHintDialog extends AbsDialog {


    static public void show(Context cx, int rcCode) {

        String title = null;
        String desc = null;

        switch (rcCode) {
            case 5201:
                title="您已经享受过免费配送！";
                desc="未购买ROKI智能厨电用户只能免费配送一次.";
                break;
//            case 5202:
//                title="今日已送完，明日再来！";
//                desc="本活动：一人一次最多免费配送三道菜\n每日限免50人次";
//                break;
            case 5203:
                title="本周已配送！";
                desc="ROKI智能厨电用户每周一次免费配送";
                break;
            case 5204:
                title="选择大与1道菜";
                desc="本活动只支持配送1道菜";
                break;
//            default:
//                ToastUtils.showShort("未知的result code:" + rcCode);
//                break;
        }

        if (Strings.isNullOrEmpty(title)) return;

        OrderHintDialog dlg = new OrderHintDialog(cx);
        dlg.txtTitle.setText(title);
        dlg.txtDesc.setText(desc);
        dlg.show();
    }

    static public void show(Context cx, String title, String desc) {
        OrderHintDialog dlg = new OrderHintDialog(cx, title, desc);
        dlg.show();
    }


    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;


    public OrderHintDialog(Context cx, String title, String desc) {
        this(cx);
        txtTitle.setText(title);
        txtDesc.setText(desc);
    }

    public OrderHintDialog(Context cx) {
        super(cx, R.style.Theme_Dialog_FullScreen);
        ViewUtils.setFullScreen(cx, this);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_order_hint;
    }

    @OnClick(R.id.layout)
    public void onClick() {
        dismiss();
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 3000);
    }
}
