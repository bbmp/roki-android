package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.DialogOvenCommonMore;

import butterknife.OnClick;



/**
 * Created by yinwei on 2017/8/16.
 */

public class DeviceOven028Page extends AbsDevicePage{
    AbsOven oven;
    View view1;
    View view2;
    View view3;
    View view4;

    //初始化模式
    @Override
    public void initView() {
        oven= Plat.deviceService.lookupChild(guid);
        oven_title.setText("电烤箱028");
        oven_recipe_listview.setHeadView(initRecipeListView());
        oven_recipe_listview.setType(DeviceType.RDKX);
        oven_recipe_listview.show();
        view1 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven_normal_view1, null, false);
        view2 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven_normal_view2, null, false);
        view3 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven_normal_view3, null, false);
        view4 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven_normal_view4, null, false);
        oven_normal_ctrl.addView(view1);
    }

    @Override
    protected void remoteOperation() {
        common_model(cx.getString(R.string.device_oven_control),R.color.yellow_oven,View.VISIBLE);
    }

    @Override
    protected void errorModel() {
        common_model(cx.getString(R.string.device_oven_error),R.color.gray_oven,View.VISIBLE);
    }

    @Override
    protected void disconnectModel() {
        common_model(cx.getString(R.string.device_disconnect_tip),R.color.gray_oven,View.INVISIBLE);
    }

    @Override
    protected void workModel() {
        if (oven.runP == OvenMode.EXP) {
            professionModel();
        } else {
            autoModel();
        }
    }

    @Override
    protected void orderModel() {
        oven_normal_ctrl.removeAllViews();
        oven_normal_ctrl.addView(view2);
        TextView textView = view2.findViewById(R.id.oven_normal_ctrl_view2_time);
        if(oven.orderTime_min<10)
            textView.setText(oven.orderTime_hour + ":0" + oven.orderTime_min);
        else
            textView.setText(oven.orderTime_hour + ":" + oven.orderTime_min);
    }

    //烤箱状态轮训
    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        LogUtils.i("20171017","event::"+event.pojo.getID());
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        if (event.pojo.status == OvenStatus.Wait || event.pojo.status == OvenStatus.Off) {
            remoteOperation();
        } else if (event.pojo.status == OvenStatus.On) {
            remoteOperation();
        } else if (event.pojo.status == OvenStatus.Order) {
            orderModel();
        } else if (event.pojo.status == OvenStatus.Working || event.pojo.status == OvenStatus.Pause
                || event.pojo.status == OvenStatus.PreHeat) {
            workModel();
        } else if (event.pojo.status == OvenStatus.AlarmStatus) {
            switch (event.pojo.alarm){
                case 3:
                case 5:
                case 6:
                case 7:
                    errorModel();
                    break;
            }
        }
        if (oven.status != OvenStatus.AlarmStatus) {
           /* if (ovenWarningDialog != null && ovenWarningDialog.isShowing()) {
                ovenWarningDialog.dismiss();
                ovenWarningDialog = null;
            }*/
        }
    }





    //断网模式


    //预约模式


    //联网状态判断
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        LogUtils.i("20170830","event::"+event.isConnected);
        if (oven == null || !Objects.equal(oven.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            disconnectModel();
        }
    }


    @OnClick(R.id.oven_setting)
    public void OnclickSetting(){
       // ToastUtils.show("我是设置", Toast.LENGTH_SHORT);
        DialogOvenCommonMore.show(cx, guid);
    }

    @OnClick(R.id.oven_normal_ctrl)
    public void OnclickControl(){
        if (!oven.isConnected()) {
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return;
        }
       /* if (oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause
                || oven.status == OvenStatus.Order || oven.status == OvenStatus.PreHeat) {
            Bundle bd = new Bundle();
            bd.putShort("from", (short) 1);
            bd.putString(PageArgumentKey.Guid, oven.getID());
            UIService.getInstance().postPage(PageKey.DeviceOven028Working, bd);
        } else {*/
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, oven.getID());
            bd.putShort("from", (short) 0);
            UIService.getInstance().postPage(PageKey.DeviceOven028Main, bd);
       // }
    }

    private void common_model(String str,int color,int visiable){
        oven_normal_ctrl.removeAllViews();
        view1.setBackgroundColor(r.getColor(color));
        oven_normal_ctrl.addView(view1);
        TextView textView = view1.findViewById(R.id.oven_normal_ctrl_waitoff_tv);
        textView.setText(str);
        textView.setTextColor(r.getColor(R.color.black));
        ImageView img = view1.findViewById(R.id.oven_normal_ctrl_waitoff_img);
        img.setVisibility(visiable);
    }

    //自动模式
    private void autoModel(){
        oven_normal_ctrl.removeAllViews();
        //view3.setBackgroundColor(Color.parseColor(new String("#FEFCCC")));
        oven_normal_ctrl.addView(view3);
        TextView textView = view3.findViewById(R.id.oven_normal_ctrl_view3_tempup);
        textView.setText(oven.temp + " ℃");
        TextView textView1 = view3.findViewById(R.id.oven_normal_ctrl_view3_min);
        if (oven.time % 60 != 0)
            textView1.setText((oven.time / 60 + 1) + " 分");
        else
            textView1.setText((oven.time / 60) + " 分");
        TextView img = view3.findViewById(R.id.oven_normal_ctrl_view3_model);
        if (oven.autoMode != 0) {
            initAutoModel(img);
        } else {
            initExpModel(img);
        }
    }

    //专业模式
    private void professionModel(){
        oven_normal_ctrl.removeAllViews();
       // view4.setBackgroundColor(Color.parseColor(new String("#FEFCCC")));
        oven_normal_ctrl.addView(view4);
        TextView textView = view4.findViewById(R.id.oven_normal_ctrl_view4_tempup);
        textView.setText(oven.temp + " ℃");
        textView = view4.findViewById(R.id.oven_normal_ctrl_view4_tempdown);
        textView.setText(oven.currentTempDownValue + " ℃");
        textView = view4.findViewById(R.id.oven_normal_ctrl_view4_min);
        if (oven.time % 60 != 0)
            textView.setText((oven.time / 60 + 1) + " 分");
        else
            textView.setText((oven.time / 60) + " 分");
        TextView img = view4.findViewById(R.id.oven_normal_ctrl_view4_model);
        if (oven.autoMode != 0) {
             initAutoModel(img);
        } else {
            initExpModel(img);
        }
    }

    void initAutoModel(TextView txt) {
        switch (oven.autoMode) {
            case 1://烤牛排
                txt.setText("烤牛排");
                break;
            case 2://烤面包
                txt.setText("烤面包");
                break;
            case 3://烤饼干
                txt.setText("烤饼干");
                break;
            case 4://烤鸡翅
                txt.setText("烤鸡翅");
                break;
            case 5://烤蛋糕
                txt.setText("烤蛋糕");
                break;
            case 6://烤披萨
                txt.setText("烤披萨");
                break;
            case 7://强虾
                txt.setText("烤强虾");
                break;
            case 8://烤鱼
                txt.setText("烤烤鱼");
                break;
            case 9://烤红薯
                txt.setText("烤红薯");
                break;
            case 10://烤玉米
                txt.setText("烤玉米");
                break;
            case 11://烤五花肉
                txt.setText("烤五花肉");
                break;
            case 12://烤蔬菜
                txt.setText("烤蔬菜");
                break;
            default:
                break;
        }
    }


    void initExpModel(TextView txt) {
        switch (oven.runP) {
            case 1://快热
                txt.setText("快热");
                break;
            case 2://风焙烤
                txt.setText("风焙烤");
                break;
            case 3://焙烤
                txt.setText("焙烤");
                break;
            case 4://底加热
                txt.setText("底加热");
                break;
            case 5://解冻
                txt.setText("解冻");
                break;
            case 6://风扇考
                txt.setText("风扇烤");
                break;
            case 7://烤烧
                txt.setText("烤烧");
                break;
            case 8://强烤烧
                txt.setText("强烤烧");
                break;
            case 9://EXP
                txt.setText("EXP");
                break;
            case 10://快速预热
                txt.setText("快速预热");
                break;
            case 11://煎烤
                txt.setText("煎烤");
                break;
            case 12://果蔬烘干
                txt.setText("果蔬烘干");
                break;
            case 13://发酵
                txt.setText("发酵");
                break;
            case 14://杀菌
                txt.setText("杀菌");
                break;
            case 15://保温
                txt.setText("保温");
                break;
            default:
                break;
        }
    }
}
