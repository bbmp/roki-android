package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class WaterPurifierFilterOutDialog1 extends AbsDialog{
    @InjectView(R.id.water_alarm_filterout_btn)
    Button water_alarm_filterout_btn;

    @InjectView(R.id.water_lvxin_first)
    LinearLayout water_lvxin_first;//第一个滤芯的北景
    @InjectView(R.id.waterfiliter_pp_num)
    TextView waterfiliter_pp_num;//pp_数字
    @InjectView(R.id.waterfiliter_pp_eng)
    TextView waterfiliter_pp_eng;//pp_大写字母
    @InjectView(R.id.water_lvxin_and1)
    TextView water_lvxin_and1;//第一个+号
    @InjectView(R.id.water_lvxin_cto)
    TextView water_lvxin_cto;//CTO
    @InjectView(R.id.water_lvxin_precent_1)
    TextView water_lvxin_precent_1;//第一个百分数
    @InjectView(R.id.water_lvxin_precent01)
    TextView water_lvxin_precent01;//第一个百分号

    @InjectView(R.id.water_lvxin_second)
    LinearLayout water_lvxin_second;
    @InjectView(R.id.waterfiliter_cto_num)
    TextView waterfiliter_cto_num;//cto_数字
    @InjectView(R.id.waterfiliter_cto_eng)
    TextView waterfiliter_cto_eng;//cto_大写字母
    @InjectView(R.id.water_lvxin_and2)
    TextView water_lvxin_and2;
    @InjectView(R.id.water_lvxin_PP)
    TextView water_lvxin_PP;
    @InjectView(R.id.water_lvxin_precent_2)
    TextView water_lvxin_precent_2;
    @InjectView(R.id.water_lvxin_precent_second)
    TextView water_lvxin_precent_second;

    @InjectView(R.id.water_lvxin_third)
    LinearLayout water_lvxin_third;//ro1
    @InjectView(R.id.waterfiliter_ro1_num)
    TextView waterfiliter_ro1_num;//ro1_数字
    @InjectView(R.id.waterfiliter_ro1_eng)
    TextView waterfiliter_ro1_eng;//ro1_大写字母
    @InjectView(R.id.water_lvxin_precent_3)
    TextView water_lvxin_precent_3;
    @InjectView(R.id.water_lvxin_precent_third)
    TextView water_lvxin_precent_third;

    @InjectView(R.id.water_lvxin_four)
    LinearLayout water_lvxin_four;//ro2
    @InjectView(R.id.waterfiliter_ro2_num)
    TextView waterfiliter_ro2_num;//ro2_数字
    @InjectView(R.id.waterfiliter_ro2_eng)
    TextView waterfiliter_ro2_eng;//ro2_大写字母
    @InjectView(R.id.water_lvxin_precent_4)
    TextView water_lvxin_precent_4;
    @InjectView(R.id.water_lvxin_precent_four)
    TextView water_lvxin_precent_four;
    protected  static int res;
    protected Resources r;
    private int filter1_surplus;
    private int filter2_surplus;
    private int filter3_surplus;
    private int filter4_surplus;
    static WaterPurifierFilterOutDialog1 ww;
    public WaterPurifierFilterOutDialog1(Context context,int resId,int filter1,int
                                         filter2,int filter3,int filter4) {
        super(context);
        res=resId;
        this.filter1_surplus=filter1;
        this.filter2_surplus=filter2;
        this.filter3_surplus=filter3;
        this.filter4_surplus=filter4;
    }

    @Override
    protected int getViewResId() {
        return res;
    }

    public static void setRes(int resId){
        res=resId;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
        init();
    }

    public static Dialog show(Context cx, int id,int filter1,int filter2,int filter3,int filter4) {
        ww = new WaterPurifierFilterOutDialog1(cx,id,filter1,filter2,filter3,filter4);
        WindowManager wm = (WindowManager)cx.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        DisplayMetrics dm=new DisplayMetrics();
        display.getMetrics(dm);

        Window win = ww.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        //lp.gravity= Gravity.BOTTOM;
        lp.width = (int)(dm.widthPixels*0.86);
        lp.height=(int) (dm.heightPixels*0.8);
        win.setAttributes(lp);
        ww.show();
        return ww;
    }

    private void init(){
        water_lvxin_precent_1.setText(filter1_surplus+"");
        water_lvxin_precent_2.setText(filter2_surplus+"");
        water_lvxin_precent_3.setText(filter3_surplus+"");
        water_lvxin_precent_4.setText(filter4_surplus+"");
        //一号滤芯的状态
        water_lvxin_first.setBackgroundResource(filter1_surplus!=0?R.mipmap.img_waterpurifier_filterwhite:R.mipmap.img_waterpurifier_filterred);
        waterfiliter_pp_num.setTextColor(filter1_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        waterfiliter_pp_eng.setTextColor(filter1_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_cto.setTextColor(filter1_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent_1.setTextColor(filter1_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent01.setTextColor(filter1_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        //二号滤芯的状态
        water_lvxin_second.setBackgroundResource(filter2_surplus!=0?R.mipmap.img_waterpurifier_filterwhite :R.mipmap.img_waterpurifier_filterred);
        waterfiliter_cto_num.setTextColor(filter2_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        waterfiliter_cto_eng.setTextColor(filter2_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_and2.setTextColor(filter2_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_PP.setTextColor(filter2_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent_2.setTextColor(filter2_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent_second.setTextColor(filter2_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        //三号滤芯的状态
        water_lvxin_third.setBackgroundResource(filter3_surplus!=0?R.mipmap.img_waterpurifier_filterwhite:R.mipmap.img_waterpurifier_filterred);
        waterfiliter_ro1_num.setTextColor(filter3_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        waterfiliter_ro1_eng.setTextColor(filter3_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent_3.setTextColor(filter3_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent_third.setTextColor(filter3_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        //四号滤芯的状态
        water_lvxin_four.setBackgroundResource(filter4_surplus!=0?R.mipmap.img_waterpurifier_filterwhite :R.mipmap.img_waterpurifier_filterred);
        waterfiliter_ro2_num.setTextColor(filter4_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        waterfiliter_ro2_eng.setTextColor(filter4_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent_4.setTextColor(filter4_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));
        water_lvxin_precent_four.setTextColor(filter4_surplus!=0?r.getColor(R.color.white):r.getColor(R.color.red));

    }

}
