package com.robam.roki.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.api.PreferenceUtils;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.FanOtherFuncAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class Fan8700KitchenCleaningDialog extends AbsDialog {
    @InjectView(R.id.fan8700_remindsoupsetting_linear_divMain)
    LinearLayout fan8700_remindsoupsetting_linear_divMain;
    @InjectView(R.id.fan8700_remindsoupsetting_imgV_titleimg)//图标
            ImageView fan8700_remindsoupsetting_imgV_titleimg;
    @InjectView(R.id.fan8700_remindsoupsetting_tv_titletext)//文字
            TextView fan8700_remindsoupsetting_tv_titletext;
    @InjectView(R.id.fan8700_remindsoupsetting_tv_confirm)//确认
            Button fan8700_remindsoupsetting_tv_confirm;
    @InjectView(R.id.fan8700_kitchencleaning_imgV_small)
    ImageView fan8700_kitchencleaning_imgV_small;
    @InjectView(R.id.fan8700_kitchencleaning_imgV_big)
    ImageView fan8700_kitchencleaning_imgV_big;
    @InjectView(R.id.fan8700_kitchencleaning_imgV_open)
    ImageView fan8700_kitchencleaning_imgV_open;
    @InjectView(R.id.fan8700_kitchencleaning_tv_smallabove)
    TextView fan8700_kitchencleaning_tv_smallabove;
    @InjectView(R.id.fan8700_kitchencleaning_tv_smallbelow)
    TextView fan8700_kitchencleaning_tv_smallbelow;
    @InjectView(R.id.fan8700_kitchencleaning_tv_bigabove)
    TextView fan8700_kitchencleaning_tv_bigabove;
    @InjectView(R.id.fan8700_kitchencleaning_tv_bigbelow)
    TextView fan8700_kitchencleaning_tv_bigbelow;
    @InjectView(R.id.fan8700_kitchencleaning_tv_openabove)
    TextView fan8700_kitchencleaning_tv_openabove;
    @InjectView(R.id.fan8700_kitchencleaning_tv_openbelow)
    TextView fan8700_kitchencleaning_tv_openbelow;
    public static Fan8700KitchenCleaningDialog dlg;
    Context cx;
    Resources res;
    private short min = 1;
    private boolean lock;


    public Fan8700KitchenCleaningDialog(Context context) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.res = cx.getResources();
        init();
    }

    private void init() {
        if (PreferenceUtils.containKey(FanOtherFuncAdapter.CLEAN_REMIND)) {
            String local = PreferenceUtils.getString(FanOtherFuncAdapter.CLEAN_REMIND, "none");
            if (!"none".equals(local)) {
                String[] str = local.split(":");
                String min = str[1];
                int time = Integer.parseInt(min) / (60 * 1000);
                if (time == 1) {
                    OnSmallClick();
                } else if (time == 3) {
                    OnBigClick();
                } else if (time == 5) {
                    OnOpenClick();
                }
                fan8700_remindsoupsetting_tv_confirm.setText("知道了");
                fan8700_remindsoupsetting_imgV_titleimg.setVisibility(View.GONE);
                lock = true;
                LayoutInflater inflater = (LayoutInflater) cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = LayoutInflater.from(cx).inflate(R.layout.dialog_fan8700_kitchencleaning_notice,null, false);
                TextView tv = view.findViewById(R.id.fanclean_tv);
                tv.setText("烟机" + time + "分钟后自动关机");
                fan8700_remindsoupsetting_linear_divMain.removeAllViews();
                fan8700_remindsoupsetting_linear_divMain.addView(view);
            }
        }
    }


    @Override
    protected int getViewResId() {
        return R.layout.dialog_fan8700_kitchencleaning;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.fan8700_remindsoupsetting_tv_confirm)//确认
    public void onClickConfirm() {
        if (listener != null) {
            if (!lock)
                listener.onConfirm(min);
            min = 0;
        }
        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
    }

    @OnClick(R.id.fan8700_kitchencleaning_frame_small)
    public void OnSmallClick() {
        if (lock) return;
        min = 1;
        fan8700_kitchencleaning_imgV_small.setImageResource(R.mipmap.ic_fan8700_circle_yellow);
        fan8700_kitchencleaning_imgV_big.setImageResource(R.mipmap.ic_fan8700_circle_gray);
        fan8700_kitchencleaning_imgV_open.setImageResource(R.mipmap.ic_fan8700_circle_gray);
        fan8700_kitchencleaning_tv_smallabove.setTextColor(res.getColor(R.color.c02));
        fan8700_kitchencleaning_tv_smallbelow.setTextColor(res.getColor(R.color.c02));

        fan8700_kitchencleaning_tv_bigabove.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_bigbelow.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_openabove.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_openbelow.setTextColor(res.getColor(R.color.c03));
    }

    @OnClick(R.id.fan8700_kitchencleaning_frame_big)
    public void OnBigClick() {
        if (lock) return;
        min = 3;
        fan8700_kitchencleaning_imgV_small.setImageResource(R.mipmap.ic_fan8700_circle_gray);
        fan8700_kitchencleaning_imgV_big.setImageResource(R.mipmap.ic_fan8700_circle_yellow);
        fan8700_kitchencleaning_imgV_open.setImageResource(R.mipmap.ic_fan8700_circle_gray);

        fan8700_kitchencleaning_tv_smallabove.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_smallbelow.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_bigabove.setTextColor(res.getColor(R.color.c02));
        fan8700_kitchencleaning_tv_bigbelow.setTextColor(res.getColor(R.color.c02));
        fan8700_kitchencleaning_tv_openabove.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_openbelow.setTextColor(res.getColor(R.color.c03));

    }

    @OnClick(R.id.fan8700_kitchencleaning_frame_open)
    public void OnOpenClick() {
        if (lock) return;
        min = 5;
        fan8700_kitchencleaning_imgV_small.setImageResource(R.mipmap.ic_fan8700_circle_gray);
        fan8700_kitchencleaning_imgV_big.setImageResource(R.mipmap.ic_fan8700_circle_gray);
        fan8700_kitchencleaning_imgV_open.setImageResource(R.mipmap.ic_fan8700_circle_yellow);

        fan8700_kitchencleaning_tv_smallabove.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_smallbelow.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_bigabove.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_bigbelow.setTextColor(res.getColor(R.color.c03));
        fan8700_kitchencleaning_tv_openabove.setTextColor(res.getColor(R.color.c02));
        fan8700_kitchencleaning_tv_openbelow.setTextColor(res.getColor(R.color.c02));

    }

    static public Fan8700KitchenCleaningDialog show(Context context) {
        dlg = new Fan8700KitchenCleaningDialog(context);
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

        void onConfirm(short min);
    }

    private PickListener listener;

    public void setPickListener(PickListener listener) {
        Log.i("mic", "setPickListener");
        this.listener = listener;
    }

}
