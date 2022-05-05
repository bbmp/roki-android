package com.robam.roki.ui.page;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.WaterPurifierEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/1.
 */
public class DeviceWaterPurifiyFilterShowPage extends BasePage {

    @InjectView(R.id.waterpurifiy_lvxin_return)
    ImageView waterpurifiy_lvxin_return;//返回
    @InjectView(R.id.waterfiliter_install)
    TextView waterfiliter_install;//安装

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

    @InjectView(R.id.ll_point_container)
    LinearLayout ll_point_container;//横向滑动的点容器

    @InjectView(R.id.waterfilter_ro_notice)
    TextView waterfilter_ro_notice;//铝棒RO红色注意

    @InjectView(R.id.water_btn_buy)
    ImageView water_btn_buy;//官网购买

    @InjectView(R.id.btn_telephone)//电话购买
            Button btn_telephone;
    @InjectView(R.id.waterpurifier_lvxin_descs)
    ViewPager viewpager;//viewpager对象
    @InjectView(R.id.iv_point)
    ImageView ivPoint;
    @InjectView(R.id.waterfiliter_purebar)
    ViewGroup waterfiliter_purebar;

    private View contentView;
    private int id;//滤芯id
    private String name;//滤芯名称
    //滤芯剩余量
    private int filter1_surplus;
    private int filter2_surplus;
    private int filter3_surplus;
    private int filter4_surplus;

    private int filter_time_pp;
    private int filter_time_cto;
    private int filter_time_ro1;
    private int filter_time_ro2;

    private String url;//官网网址
    private String telephone;//官网电话
    View pointView;

    private Boolean isCon;

    private ArrayList<View> pageViews;
    private int mPointDis;
    String guid;
    private boolean isConFlag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        id = bd.getInt(PageArgumentKey.WaterFilterID);
        name = bd.getString(PageArgumentKey.WaterFilterName);
        guid = bd.getString(PageArgumentKey.Guid);
        isCon=bd.getBoolean(PageArgumentKey.isCon);

        if ("RJ312".equals(guid)){
            filter1_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus1);
            filter2_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus2);
            filter3_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus3);
            filter4_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus4);
            filter_time_pp = bd.getInt(PageArgumentKey.WaterFilterLeftTime_pp);
            filter_time_cto = bd.getInt(PageArgumentKey.WaterFilterLeftTime_cto);
            filter_time_ro1 = bd.getInt(PageArgumentKey.WaterFilterLeftTime_ro1);
            filter_time_ro2 = bd.getInt(PageArgumentKey.WaterFilterLeftTime_ro2);
        }else{
            filter1_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus1);
            filter2_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus2);
            filter3_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus3);
            filter4_surplus = bd.getInt(PageArgumentKey.WaterFilterSurplus4);
        }


        LogUtils.i("filter_sur", "filter_pp:" + filter1_surplus);
        LogUtils.i("filter_sur", "filter_cto:" + filter2_surplus);
        LogUtils.i("filter_sur", "filter_ro1:" + filter3_surplus);
        LogUtils.i("filter_sur", "filter_ro2:" + filter4_surplus);

        LogUtils.i("filter_sur", "filter_time_pp:" + filter_time_pp);
        LogUtils.i("filter_sur", "filter_time_cto:" + filter_time_cto);
        LogUtils.i("filter_sur", "filter_time_ro1:" + filter_time_ro1);
        LogUtils.i("filter_sur", "filter_time_ro2:" + filter_time_ro2);
        //初始化布局
        initView(inflater, container);
        ButterKnife.inject(this, contentView);
        pointView = new View(cx);

        init();
        return contentView;
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        contentView = inflater.inflate(R.layout.page_device_waterpurifiy_filter, container, false);
        pageViews = new ArrayList<View>();

        if(IRokiFamily.RJ312.equals(guid)){
            pageViews.add(inflater.inflate(R.layout.page_waterpurifier_view1, null));
            pageViews.add(inflater.inflate(R.layout.page_waterpurifier_view2, null));
            pageViews.add(inflater.inflate(R.layout.page_waterpurifier_view3, null));
            pageViews.add(inflater.inflate(R.layout.page_waterpurifier_view4, null));
        }else if (IRokiFamily.RJ320.equals(guid)){
            pageViews.add(inflater.inflate(R.layout.page_320waterpurifier_view1, null));
            pageViews.add(inflater.inflate(R.layout.page_320waterpurifier_view2, null));
            pageViews.add(inflater.inflate(R.layout.page_320waterpurifier_view3, null));
            pageViews.add(inflater.inflate(R.layout.page_320waterpurifier_view4, null));
        }else{
            pageViews.add(inflater.inflate(R.layout.page_321waterpurifier_view1, null));
            pageViews.add(inflater.inflate(R.layout.page_321waterpurifier_view2, null));
            pageViews.add(inflater.inflate(R.layout.page_321waterpurifier_view3, null));
            pageViews.add(inflater.inflate(R.layout.page_321waterpurifier_view4, null));
        }

    }


    private void setSelect(int id,boolean flag){
        water_lvxin_first.setBackgroundResource(R.mipmap.img_waterpurifier_filterblue);
        waterfiliter_pp_num.setTextColor(r.getColor(R.color.water_lv));
        waterfiliter_pp_eng.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_and1.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_cto.setTextColor(r.getColor(R.color.water_lv));
       // water_lvxin_precent_1.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_precent_1.setVisibility(View.INVISIBLE);
      //  water_lvxin_precent01.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_precent01.setVisibility(View.INVISIBLE);
        waterfilter_ro_notice.setVisibility(View.INVISIBLE);

        water_lvxin_second.setBackgroundResource(R.mipmap.img_waterpurifier_filterblue);
        waterfiliter_cto_num.setTextColor(r.getColor(R.color.water_lv));
        waterfiliter_cto_eng.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_and2.setTextColor(r.getColor(R.color.water_lv) );
        water_lvxin_PP.setTextColor(r.getColor(R.color.water_lv));
      //  water_lvxin_precent_2.setTextColor(r.getColor(R.color.water_lv) );
        water_lvxin_precent_2.setVisibility(View.INVISIBLE);
      //  water_lvxin_precent_second.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_precent_second.setVisibility(View.INVISIBLE);
        waterfilter_ro_notice.setVisibility(View.INVISIBLE);

        water_lvxin_third.setBackgroundResource( R.mipmap.img_waterpurifier_filterblue);
        waterfiliter_ro1_num.setTextColor(r.getColor(R.color.water_lv));
        waterfiliter_ro1_eng.setTextColor(r.getColor(R.color.water_lv));
      //  water_lvxin_precent_3.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_precent_3.setVisibility(View.INVISIBLE);
       // water_lvxin_precent_third.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_precent_third.setVisibility(View.INVISIBLE);
        waterfilter_ro_notice.setVisibility(View.VISIBLE);

        water_lvxin_four.setBackgroundResource(R.mipmap.img_waterpurifier_filterblue);
        waterfiliter_ro2_num.setTextColor(r.getColor(R.color.water_lv));
        waterfiliter_ro2_eng.setTextColor(r.getColor(R.color.water_lv));
      //  water_lvxin_precent_4.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_precent_4.setVisibility(View.INVISIBLE);
       // water_lvxin_precent_four.setTextColor(r.getColor(R.color.water_lv));
        water_lvxin_precent_four.setVisibility(View.INVISIBLE);
        waterfilter_ro_notice.setVisibility(View.VISIBLE);

        if (IRokiFamily.RJ312.equals(guid)){
            setWhichOneWasSelect(id,flag);
        }else{
            setWhichOne321WasSelect(id,flag);
        }


    }

    private void setWhichOneWasSelect(int id,boolean flag){
        switch (id){
            case 0:
                water_lvxin_first.setBackgroundResource(filter1_surplus > 10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
                waterfiliter_pp_num.setTextColor(filter1_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfiliter_pp_eng.setTextColor(filter1_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_and1.setTextColor(filter2_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_cto.setTextColor(filter1_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent_1.setVisibility(View.VISIBLE);
                water_lvxin_precent01.setVisibility(View.VISIBLE);
                water_lvxin_precent_1.setTextColor(filter1_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent01.setTextColor(filter1_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfilter_ro_notice.setVisibility(View.INVISIBLE);
                if (flag){
                    filterTimeLeft312(id);
                }
                break;
            case 1:
                water_lvxin_second.setBackgroundResource(filter2_surplus > 10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
                waterfiliter_cto_num.setTextColor(filter2_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfiliter_cto_eng.setTextColor(filter2_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_and2.setTextColor(filter2_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_PP.setTextColor(filter2_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent_2.setVisibility(View.VISIBLE);
                water_lvxin_precent_second.setVisibility(View.VISIBLE);
                water_lvxin_precent_2.setTextColor(filter2_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent_second.setTextColor(filter2_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfilter_ro_notice.setVisibility(View.INVISIBLE);
                if (flag){
                    filterTimeLeft312(id);
                }

                break;
            case 2:
                water_lvxin_third.setBackgroundResource(filter3_surplus > 10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
                waterfiliter_ro1_num.setTextColor(filter3_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfiliter_ro1_eng.setTextColor(filter3_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent_3.setVisibility(View.VISIBLE);
                water_lvxin_precent_third.setVisibility(View.VISIBLE);
                water_lvxin_precent_3.setTextColor(filter3_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent_third.setTextColor(filter3_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfilter_ro_notice.setVisibility(View.VISIBLE);
                if (flag){
                    filterTimeLeft312(id);
                }
                break;
            case 3:
                water_lvxin_four.setBackgroundResource(filter4_surplus > 10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
                waterfiliter_ro2_num.setTextColor(filter4_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfiliter_ro2_eng.setTextColor(filter4_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent_4.setVisibility(View.VISIBLE);
                water_lvxin_precent_four.setVisibility(View.VISIBLE);
                water_lvxin_precent_4.setTextColor(filter4_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                water_lvxin_precent_four.setTextColor(filter4_surplus > 10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                waterfilter_ro_notice.setVisibility(View.VISIBLE);
                if (flag){
                    filterTimeLeft312(id);
                }
                break;
            default:
                break;
        }
    }



    @Subscribe
    public void onEvent(WaterPurifierEvent event){
        LogUtils.i("20170803","event::::"+event.purifier.filter_state_pp+" "+event.purifier.filter_state_cto+" "+event.purifier.filter_state_ro1+" "+event.purifier.filter_state_ro2);

        if (!Objects.equal(guid, event.purifier.getDt())){
            return;
        }else {
            if (!event.purifier.isConnected()){
                filter1_surplus = 0;
                filter2_surplus =0;
                filter3_surplus = 0;
                filter4_surplus = 0;
                filter_time_pp = 0;
                filter_time_cto = 0;
                filter_time_ro1 = 0;
                filter_time_ro2 = 0;
                return;
            }
        }

        if ("RJ312".equals(guid)){
            filter1_surplus = event.purifier.filter_state_pp;
            filter2_surplus =event.purifier.filter_state_cto;
            filter3_surplus = event.purifier.filter_state_ro1;
            filter4_surplus = event.purifier.filter_state_ro2;
            filter_time_pp = setFilterLeft(event.purifier.filter_time_pp);
            filter_time_cto = setFilterLeft(event.purifier.filter_time_cto);
            filter_time_ro1 = setFilterLeft(event.purifier.filter_time_ro1);
            filter_time_ro2 = setFilterLeft(event.purifier.filter_time_ro2);
            water_lvxin_and2.setVisibility(View.VISIBLE);
            water_lvxin_PP.setVisibility(View.VISIBLE);
        }else if("RJ320".equals(guid)){
            filter1_surplus =event.purifier.filter_state_pp;
            filter2_surplus = event.purifier.filter_state_cto;
            filter3_surplus = event.purifier.filter_state_ro1;
            filter4_surplus = event.purifier.filter_state_ro2;
            water_lvxin_and2.setVisibility(View.INVISIBLE);
            water_lvxin_PP.setVisibility(View.INVISIBLE);
        }else {
            filter1_surplus =event.purifier.filter_state_pp;
            filter2_surplus = event.purifier.filter_state_cto;
            filter3_surplus = event.purifier.filter_state_ro1;
            filter4_surplus = event.purifier.filter_state_ro2;
            water_lvxin_and2.setVisibility(View.INVISIBLE);
            water_lvxin_PP.setVisibility(View.INVISIBLE);
        }
        whitchWaterPurifier();
    }

    /**
     * 根据 id name surplus初始化页面
     */
    private void init() {
        if ("RJ312".equals(guid)){
            if (isCon){
                water_lvxin_and2.setVisibility(View.VISIBLE);
                water_lvxin_PP.setVisibility(View.VISIBLE);
                filter1_surplus=11;
                filter2_surplus=11;
                filter3_surplus=11;
                filter4_surplus=11;
                unConnect();
                isConFlag = false;
                setSelect(id,isConFlag);
            }else {
                isConFlag = true;
                whitchWaterPurifier();
            }
        }else{
           // LogUtils.i("20170802","ison:"+isCon);
            if (isCon){
                filter1_surplus =11;
                filter2_surplus =11;
                filter3_surplus =11;
                filter4_surplus =11;
                if ("RJ320".equals(guid)){
                    waterfiliter_cto_eng.setText("CTO");
                }else{
                    waterfiliter_cto_eng.setText("UF");
                }
                water_lvxin_and2.setVisibility(View.GONE);
                water_lvxin_PP.setVisibility(View.GONE);
                unConnect();
                isConFlag = false;
                setSelect(id,isConFlag);
            }else {
                if ("RJ320".equals(guid)){
                    waterfiliter_cto_eng.setText("CTO");
                }else{
                    waterfiliter_cto_eng.setText("UF");
                }
                isConFlag = true;
                whitchWaterPurifier();
            }
        }

        //滑动的小圆点
        huaDong();
        touchEventListeren();//触摸滑动事件
    }

    //哪个净水器走相应的方法
    public void whitchWaterPurifier(){
        if("RJ312".equals(guid)){
            filterTimeLeft312(id);
           // filterStatusOne(); // 滤芯的状态
            setSelect(id,isConFlag);
            water_lvxin_and2.setVisibility(View.VISIBLE);
            water_lvxin_PP.setVisibility(View.VISIBLE);
        }else{
            water_lvxin_and2.setVisibility(View.GONE);
            water_lvxin_PP.setVisibility(View.GONE);
            filterTimeLeft321(id);
            setSelect(id,isConFlag);
           // filterStatusSecond();
        }
    }

    public void unConnect(){
        water_lvxin_precent_1.setText("-");
        water_lvxin_precent01.setVisibility(View.VISIBLE);
        water_lvxin_precent_2.setText("-");
        water_lvxin_precent_second.setVisibility(View.VISIBLE);
        water_lvxin_precent_3.setText("-");
        water_lvxin_precent_third.setVisibility(View.VISIBLE);
        water_lvxin_precent_4.setText("-");
        water_lvxin_precent_four.setVisibility(View.VISIBLE);
    }


    public void filterTimeLeft312(int pos){

        switch (pos){
            case 0:
                if(filter1_surplus==0){
                    water_lvxin_precent_1.setText( "失效");
                    water_lvxin_precent_1.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent01.setVisibility(View.GONE);
                }else{
                    water_lvxin_precent_1.setText( filter1_surplus+"");
                    water_lvxin_precent_1.invalidate();
                }
                break;
            case 1:
                if (filter2_surplus==0){
                    water_lvxin_precent_2.setText("失效");
                    water_lvxin_precent_2.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent_second.setVisibility(View.GONE);
                }else {
                    water_lvxin_precent_2.setText(filter2_surplus+"");
                    water_lvxin_precent_2.invalidate();
                }
                break;
            case 2:
                if(filter3_surplus==0){
                    water_lvxin_precent_3.setText("失效");
                    water_lvxin_precent_3.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent_third.setVisibility(View.GONE);
                }else{
                    water_lvxin_precent_3.setText(filter3_surplus+"");
                    water_lvxin_precent_3.invalidate();
                }
                break;
            case 3:
                if(filter4_surplus==0){
                    water_lvxin_precent_4.setText("失效");
                    water_lvxin_precent_4.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent_four.setVisibility(View.GONE);
                }else{
                    water_lvxin_precent_4.setText(filter4_surplus+"");
                    water_lvxin_precent_4.invalidate();
                }
                break;
            default:
                break;
        }
    }



    public void filterTimeLeft321(int pos){
        switch (pos){
            case 0:
                if(filter1_surplus==255){
                    water_lvxin_precent_1.setText( "失效");
                    water_lvxin_precent_1.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent01.setVisibility(View.GONE);
                }else{
                    water_lvxin_precent_1.setText( filter1_surplus+"");
                    water_lvxin_precent_1.invalidate();
                }
                break;
            case 1:
                if (filter2_surplus==255){
                    water_lvxin_precent_2.setText("失效");
                    water_lvxin_precent_2.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent_second.setVisibility(View.GONE);
                }else {
                    water_lvxin_precent_2.setText(filter2_surplus+"");
                    water_lvxin_precent_2.invalidate();
                }
                break;
            case 2:
                if(filter3_surplus==255){
                    water_lvxin_precent_3.setText("失效");
                    water_lvxin_precent_3.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent_third.setVisibility(View.GONE);
                }else{
                    water_lvxin_precent_3.setText(filter3_surplus+"");
                    water_lvxin_precent_3.invalidate();
                }
                break;
            case 3:
                if(filter4_surplus==255){
                    water_lvxin_precent_4.setText("失效");
                    water_lvxin_precent_4.setTextColor(Color.parseColor("#fe0e0e"));
                    water_lvxin_precent_four.setVisibility(View.GONE);
                }else{
                    water_lvxin_precent_4.setText(filter4_surplus+"");
                    water_lvxin_precent_4.invalidate();
                }
                break;
            default:
                break;
        }
    }


    private void setWhichOne321WasSelect(int id,boolean flag){
        switch (id){
            case 0:
                water_lvxin_precent_1.setVisibility(View.VISIBLE);
                water_lvxin_precent01.setVisibility(View.VISIBLE);
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
                waterfilter_ro_notice.setVisibility(View.INVISIBLE);
                if (flag){
                    filterTimeLeft321(id);
                }

                break;
            case 1:
                water_lvxin_precent_2.setVisibility(View.VISIBLE);
                water_lvxin_precent_second.setVisibility(View.VISIBLE);
                if (filter2_surplus==255){
                    water_lvxin_precent_2.setTextColor(r.getColor(R.color.red));
                    water_lvxin_second.setBackgroundResource(R.mipmap.img_waterpurifier_filterred);
                    waterfiliter_cto_num.setTextColor(r.getColor(R.color.red));
                    waterfiliter_cto_eng.setTextColor( r.getColor(R.color.red));
                    //   water_lvxin_and2.setTextColor(r.getColor(R.color.red));
                    //   water_lvxin_PP.setTextColor(r.getColor(R.color.red));
                    water_lvxin_precent_2.setTextColor(r.getColor(R.color.red));
                }else{
                    water_lvxin_second.setBackgroundResource(filter2_surplus  >  10 ? R.mipmap.img_waterpurifier_filterwhite : R.mipmap.img_waterpurifier_filterred);
                    waterfiliter_cto_num.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                    waterfiliter_cto_eng.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                    //  water_lvxin_and2.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                    //  water_lvxin_PP.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                    water_lvxin_precent_2.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                    water_lvxin_precent_second.setTextColor(filter2_surplus  >  10 ? r.getColor(R.color.white) : r.getColor(R.color.red));
                }
                waterfilter_ro_notice.setVisibility(View.INVISIBLE);
                if (flag){
                    filterTimeLeft321(id);
                }
                break;
            case 2:
                water_lvxin_precent_3.setVisibility(View.VISIBLE);
                water_lvxin_precent_third.setVisibility(View.VISIBLE);
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
                waterfilter_ro_notice.setVisibility(View.VISIBLE);
                if (flag){
                    filterTimeLeft321(id);
                }
                break;
            case 3:
                water_lvxin_precent_4.setVisibility(View.VISIBLE);
                water_lvxin_precent_four.setVisibility(View.VISIBLE);
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
                waterfilter_ro_notice.setVisibility(View.VISIBLE);
                if (flag){
                    filterTimeLeft321(id);
                }
                break;
            default:
                break;
        }
    }



/**
 * listView的适配器
 */
class DescAdapter extends PagerAdapter {

    @Override
    public void destroyItem(View v, int position, Object object) {
        ((ViewPager) v).removeView(pageViews.get(position));
    }

    @Override
    public int getCount() {

        return pageViews.size();
    }

    @Override
    public Object instantiateItem(View v, int position) {
        ((ViewPager) v).addView(pageViews.get(position));
        return pageViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}

    /**
     * 剩余天数
     *
     * @param time
     * @return
     */
    private int setFilterLeft(int time) {
        if (time < 360) {
            return (time + 22) / 24;
        }
        return (time) / 24;
    }

    /**
     * 返回按钮
     */

    @OnClick(R.id.waterpurifiy_lvxin_return)
    public void onClickReturn() {

        UIService.getInstance().popBack();
    }

    @OnClick(R.id.water_btn_buy)
    public void onClickOfficalBuy() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Url, "http://www.robam.com/");
        UIService.getInstance().postPage(PageKey.WebClient, bd);
    }

    /**
     * 电话购买
     */
    @OnClick(R.id.btn_telephone)
    public void onClickbuy() {
        Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:95105855"));
        startActivity(it);
    }

    /**
     * 安装按钮
     */
    @OnClick(R.id.waterfiliter_install)
    public void onClickInstall() {
      /*  WaterPurifierAlarmDialog.setRes(R.layout.dialog_waterpurifier_more);
        WaterPurifierAlarmDialog.show(cx,0);*/
        UIService.getInstance().postPage(PageKey.DeviceWaterPurifiyInstall, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = r.getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = r.getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 小圆点滑动
     */
    public void huaDong() {
        for (int i = 0; i < pageViews.size(); i++) {

            //初始化小圆点
            ImageView point = new ImageView(cx);
            point.setImageResource(R.drawable.shape_point_normal);

            //初始化布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                //params.leftMargin = 10;//从第二个点开始设置左边距10px
                params.leftMargin = dip2px(12);
            }

            point.setLayoutParams(params);//设置布局参数

            ll_point_container.addView(point);
        }
        viewpager.setAdapter(new DescAdapter());
        viewpager.setCurrentItem(id);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                LogUtils.i("20180322","po::"+position);

            }

            //监听滑动事件
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                LogUtils.i("20180322","当前位置:" + position + ";偏移百分比:" + positionOffset);
                //通过修改小红点的左边距来更新小红点的位置
                int leftMargin = (int) (mPointDis * positionOffset + position
                        * mPointDis + 0.5f);//要将当前的位置信息产生的距离加进来
                //获取小红点的布局参数
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;
                ivPoint.setLayoutParams(params);
                id = position;
                setSelect(position,isConFlag);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置观察者进行监听
        ivPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    //一旦视图树的layout方法调用完成, 就会回调此方法
                    @Override
                    public void onGlobalLayout() {
                        //布局位置已经确定,可以拿到位置信息了
                        mPointDis = ll_point_container.getChildAt(1).getLeft() - ll_point_container.getChildAt(0).getLeft();
                        System.out.println("mPointDis:" + mPointDis);
                        int leftMargin = (int) (id* mPointDis + 0.5f);//要将当前的位置信息产生的距离加进来
                        //获取小红点的布局参数
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPoint
                                .getLayoutParams();
                        params.leftMargin = leftMargin;
                        ivPoint.setLayoutParams(params);
                        //移除观察者
                        ivPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
    }

    private void touchEventListeren() {
        waterfiliter_purebar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() != 0 && waterfiliter_purebar.getId() == v.getId()) {
                    LogUtils.i("20170316",""+event.getAction());
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setTag(event.getX());
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        float start_x = (Float) v.getTag();
                        float offset_X = event.getX() - start_x;
                        LogUtils.i("20170316","Math.abs(offset_X)"+Math.abs(offset_X));
                        if (Math.abs(offset_X) < 30) {
                            return true;
                        } else if (offset_X > dip2px(35)) {//左滑
                            viewpager.setCurrentItem(
                                    viewpager.getCurrentItem() == 0 ? 0 : viewpager.getCurrentItem() - 1);
                            int index=viewpager.getCurrentItem() == 0 ? 0 : viewpager.getCurrentItem() - 1;
                            LogUtils.i("20170316","左滑"+index);
                            return true;
                        } else if (offset_X < -dip2px(35)) {//右滑
                            viewpager.setCurrentItem(
                                    viewpager.getCurrentItem() == viewpager.getAdapter().getCount() - 1 ? viewpager.getAdapter().getCount() - 1 : viewpager.getCurrentItem() + 1);
                            int index=viewpager.getCurrentItem() == viewpager.getAdapter().getCount() - 1 ? viewpager.getAdapter().getCount() - 1 : viewpager.getCurrentItem() + 1;
                            LogUtils.i("20170316","右滑"+index);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

}
