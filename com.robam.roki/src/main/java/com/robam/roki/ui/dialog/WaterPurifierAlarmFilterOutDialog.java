package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/05/31.
 */
public class WaterPurifierAlarmFilterOutDialog extends AbsDialog {
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
    @InjectView(R.id.water_txt)
    TextView water_txt;

    private int filter1_surplus;
    private int filter2_surplus;
    private int filter3_surplus;
    private int filter4_surplus;

    private int filter_time_pp;
    private int filter_time_cto;
    private int filter_time_ro1;
    private int filter_time_ro2;
    protected Resources r;
    static WaterPurifierAlarmFilterOutDialog dlg1;
    private String guid;
    public WaterPurifierAlarmFilterOutDialog(Context context,int filter1_surplus,int filter2_surplus,int filter3_surplus,
                                             int filter4_surplus ,int filter1_time,int filter2_time,int filter3_time,
                                             int filter4_time,String guid) {
        super(context, R.style.WatreDialog_FullScreen);
        this.guid = guid;
        if ("RJ312".equals(guid)){
            this.filter1_surplus=filter1_surplus;
            this.filter2_surplus=filter2_surplus;
            this.filter3_surplus=filter3_surplus;
            this.filter4_surplus=filter4_surplus;
            this.filter_time_pp=filter1_time;
            this.filter_time_cto=filter2_time;
            this.filter_time_ro1=filter3_time;
            this.filter_time_ro2=filter4_time;
        }else{
            this.filter1_surplus=filter1_surplus;
            this.filter2_surplus=filter2_surplus;
            this.filter3_surplus=filter3_surplus;
            this.filter4_surplus=filter4_surplus;
            LogUtils.i("20170708","pp:"+filter1_surplus+" cto:"+filter2_surplus+" ro1:"+filter3_surplus+"ro2:"+filter4_surplus);
        }
        r=context.getResources();
        init();

    }
    @Override
    protected int getViewResId() {

        return R.layout.dialog_water_alarm_filter_timeout;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    private void init(){
        if("RJ312".equals(guid)){
            filterTimeLeft312();
            filterStatus(); // 滤芯的状态
        }else{
            filterTimeLeft321();
            filterStatusSecond();

        }
    }

    private void filterTimeLeft312(){
        if (filter1_surplus == 0) {
            water_lvxin_precent_1.setText(filter_time_pp != 0 ? filter_time_pp + "天" : "失效");
            water_lvxin_precent01.setVisibility(View.GONE);
        } else {
            water_lvxin_precent_1.setText(filter1_surplus + "");
        }
        if (filter2_surplus == 0) {
            water_lvxin_precent_2.setText(filter_time_cto != 0 ? filter_time_cto + "天" : "失效");
            water_lvxin_precent_second.setVisibility(View.GONE);
        } else {
            water_lvxin_precent_2.setText(filter2_surplus + "");
        }
        if (filter3_surplus == 0) {
            water_lvxin_precent_3.setText(filter_time_ro1 != 0 ? filter_time_ro1 + "天" : "失效");
            water_lvxin_precent_third.setVisibility(View.GONE);
        } else {
            water_lvxin_precent_3.setText(filter3_surplus + "");
        }
        if (filter4_surplus == 0) {
            water_lvxin_precent_4.setText(filter_time_ro2 != 0 ? filter_time_ro2 + "天" : "失效");
            water_lvxin_precent_four.setVisibility(View.GONE);
        } else {
            water_lvxin_precent_4.setText(filter4_surplus + "");
        }
    }

    private void filterTimeLeft321(){
        if(filter1_surplus==255){
            water_lvxin_precent_1.setText( "失效");
            water_lvxin_precent_1.setTextColor(Color.parseColor("#fe0e0e"));
            water_lvxin_precent01.setVisibility(View.GONE);
        }else{
            water_lvxin_precent_1.setText( filter1_surplus+"");
            if (filter1_surplus<=10){
                water_txt.setText("滤芯还有"+filter1_surplus+"%的寿命，为了您的饮水健康，请尽快购买滤芯。");
            }

        }

        if (filter2_surplus==255){
            water_lvxin_precent_2.setText("失效");
            water_lvxin_precent_2.setTextColor(Color.parseColor("#fe0e0e"));
            water_lvxin_precent_second.setVisibility(View.GONE);
        }else {
            water_lvxin_precent_2.setText(filter2_surplus+"");
            if(filter2_surplus<=10){
                water_txt.setText("滤芯还有"+filter2_surplus+"%的寿命，为了您的饮水健康，请尽快购买滤芯。");
            }
        }

        if(filter3_surplus==255){
            water_lvxin_precent_3.setText("失效");
            water_lvxin_precent_3.setTextColor(Color.parseColor("#fe0e0e"));
            water_lvxin_precent_third.setVisibility(View.GONE);
        }else{
            water_lvxin_precent_3.setText(filter3_surplus+"");
            if (filter3_surplus<=10){
                water_txt.setText("滤芯还有"+filter3_surplus+"%的寿命，为了您的饮水健康，请尽快购买滤芯。");
            }
        }

        if(filter4_surplus==255){
            water_lvxin_precent_4.setText("失效");
            water_lvxin_precent_4.setTextColor(Color.parseColor("#fe0e0e"));
            water_lvxin_precent_four.setVisibility(View.GONE);
        }else{
            water_lvxin_precent_4.setText(filter4_surplus+"");
            if (filter4_surplus<=10){
                water_txt.setText("滤芯还有"+filter4_surplus+"%的寿命，为了您的饮水健康，请尽快购买滤芯。");
            }
        }
    }

    public static Dialog show(Context cx, int filter1_surplus, int filter2_surplus, int filter3_surplus, int filter4_surplus,
                              int filter1_time,int filter2_time,int filter3_time,int filter4_time,String guid) {
        dlg1 = new WaterPurifierAlarmFilterOutDialog(cx,filter1_surplus,filter2_surplus,filter3_surplus,filter4_surplus,
                filter1_time,filter2_time,filter3_time,filter4_time,guid);
        WindowManager wm = (WindowManager)cx.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        DisplayMetrics dm=new DisplayMetrics();
        display.getMetrics(dm);

        Window win = dlg1.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        //lp.gravity= Gravity.BOTTOM;
        lp.width = (int)(dm.widthPixels*0.86);
        lp.height=(int) (dm.heightPixels*0.8);
        win.setAttributes(lp);
        dlg1.show();
        return dlg1;
    }

    @OnClick(R.id.water_alarm_filterout_btn)
    public void onClickServer(){
        Bundle bundle = new Bundle();
        bundle.putInt(PageArgumentKey.WaterFilterSurplus1, filter1_surplus);
        bundle.putInt(PageArgumentKey.WaterFilterSurplus2, filter2_surplus);
        bundle.putInt(PageArgumentKey.WaterFilterSurplus3,filter3_surplus);
        bundle.putInt(PageArgumentKey.WaterFilterSurplus4, filter4_surplus);
        bundle.putInt(PageArgumentKey.WaterFilterLeftTime_pp,filter_time_pp);
        bundle.putInt(PageArgumentKey.WaterFilterLeftTime_cto,filter_time_cto);
        bundle.putInt(PageArgumentKey.WaterFilterLeftTime_ro1,filter_time_ro1);
        bundle.putInt(PageArgumentKey.WaterFilterLeftTime_ro2,filter_time_ro2);
        UIService.getInstance().postPage(PageKey.DeviceWaterPurifiyFilterShow,bundle);
        dlg1.dismiss();
    }

    private void filterStatus(){

        water_lvxin_first.setBackgroundResource(filter1_surplus != 0 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
        waterfiliter_pp_num.setTextColor(filter1_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        waterfiliter_pp_eng.setTextColor(filter1_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_and1.setTextColor(filter2_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_cto.setTextColor(filter1_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent_1.setTextColor(filter1_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent01.setTextColor(filter1_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));


        water_lvxin_second.setBackgroundResource(filter2_surplus != 0 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
        waterfiliter_cto_num.setTextColor(filter2_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        waterfiliter_cto_eng.setTextColor(filter2_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_and2.setTextColor(filter2_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_PP.setTextColor(filter2_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent_2.setTextColor(filter2_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent_second.setTextColor(filter2_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));


        water_lvxin_third.setBackgroundResource(filter3_surplus != 0 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
        waterfiliter_ro1_num.setTextColor(filter3_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        waterfiliter_ro1_eng.setTextColor(filter3_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent_3.setTextColor(filter3_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent_third.setTextColor(filter3_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));


        water_lvxin_four.setBackgroundResource(filter4_surplus != 0 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
        waterfiliter_ro2_num.setTextColor(filter4_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        waterfiliter_ro2_eng.setTextColor(filter4_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent_4.setTextColor(filter4_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        water_lvxin_precent_four.setTextColor(filter4_surplus != 0 ? r.getColor(R.color.white) : r.getColor(R.color.red));

    }

    private void filterStatusSecond(){
        if(filter1_surplus==255){
            water_lvxin_precent_1.setTextColor(r.getColor(R.color.red));
            water_lvxin_first.setBackgroundResource(R.mipmap.img_waterpurifier_filterred);
            waterfiliter_pp_num.setTextColor(r.getColor(R.color.red));
            waterfiliter_pp_eng.setTextColor(r.getColor(R.color.red));
            water_lvxin_and1.setTextColor(r.getColor(R.color.red));
            water_lvxin_cto.setTextColor(r.getColor(R.color.red));
            water_lvxin_precent_1.setTextColor(r.getColor(R.color.red));
        }else{
            water_lvxin_first.setBackgroundResource(filter1_surplus > 10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
            waterfiliter_pp_num.setTextColor(filter1_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            waterfiliter_pp_eng.setTextColor(filter1_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_and1.setTextColor(filter1_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_cto.setTextColor(filter1_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent_1.setTextColor(filter1_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent01.setTextColor(filter1_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        }


        if (filter2_surplus==255){
            water_lvxin_precent_2.setTextColor(r.getColor(R.color.red));
            water_lvxin_second.setBackgroundResource(R.mipmap.img_waterpurifier_filterred);
            waterfiliter_cto_num.setTextColor(r.getColor(R.color.red));
            waterfiliter_cto_eng.setTextColor( r.getColor(R.color.red));
            water_lvxin_and2.setTextColor(r.getColor(R.color.red));
            water_lvxin_PP.setTextColor(r.getColor(R.color.red));
            water_lvxin_precent_2.setTextColor(r.getColor(R.color.red));
        }else{
            water_lvxin_second.setBackgroundResource(filter2_surplus  >  10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
            waterfiliter_cto_num.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            waterfiliter_cto_eng.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_and2.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_PP.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent_2.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent_second.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        }


        if (filter3_surplus==255){
            water_lvxin_precent_3.setTextColor(r.getColor(R.color.red));
            water_lvxin_third.setBackgroundResource(R.mipmap.img_waterpurifier_filterred);
            waterfiliter_ro1_num.setTextColor(r.getColor(R.color.red));
            waterfiliter_ro1_eng.setTextColor(r.getColor(R.color.red));
            water_lvxin_precent_3.setTextColor(r.getColor(R.color.red));
        }else{
            water_lvxin_third.setBackgroundResource(filter3_surplus  >  10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
            waterfiliter_ro1_num.setTextColor(filter3_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            waterfiliter_ro1_eng.setTextColor(filter3_surplus  > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent_3.setTextColor(filter3_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent_third.setTextColor(filter3_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        }


        if (filter4_surplus==255){
            water_lvxin_precent_4.setTextColor(r.getColor(R.color.red));
            water_lvxin_four.setBackgroundResource(R.mipmap.img_waterpurifier_filterred);
            waterfiliter_ro2_num.setTextColor(r.getColor(R.color.red));
            waterfiliter_ro2_eng.setTextColor(r.getColor(R.color.red));
            water_lvxin_precent_4.setTextColor( r.getColor(R.color.red));
        }else{
            water_lvxin_four.setBackgroundResource(filter4_surplus  > 10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
            waterfiliter_ro2_num.setTextColor(filter4_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            waterfiliter_ro2_eng.setTextColor(filter4_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent_4.setTextColor(filter4_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
            water_lvxin_precent_four.setTextColor(filter4_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
        }

    }
}

