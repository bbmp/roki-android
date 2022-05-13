//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.common.base.Objects;
//import com.google.common.eventbus.Subscribe;
//import com.legent.plat.Plat;
//import com.legent.plat.events.DeviceConnectionChangedEvent;
//import com.legent.ui.UIService;
//import com.legent.utils.LogUtils;
//import com.legent.utils.api.ToastUtils;
//import com.robam.common.events.SteamOvenStatusChangedEvent;
//import com.robam.common.pojos.DeviceType;
//import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
//import com.robam.common.pojos.device.Steamoven.SteamStatus;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.ui.dialog.DialogOvenCommonMore;
//
//import butterknife.OnClick;
//
///**
// * Created by yinwei on 2017/8/30.
// */
//
//public class DeviceSteam228Page extends AbsDevicePage {
//
//    View view1_disconnect_status;
//    View view2_order_status;
//    View view3_work_status;
//    View view4;
//    View view5;
//    AbsSteamoven steam;
//
//    @Override
//    protected void initView() {
//        steam = Plat.deviceService.lookupChild(guid);
//        oven_title.setText("蒸汽炉S228");
//        oven_recipe_listview.setHeadView(initRecipeListView());
//        oven_recipe_listview.setType(DeviceType.RZQL);
//        oven_recipe_listview.show();
//        view1_disconnect_status= LayoutInflater.from(cx).inflate(R.layout.page_device_oven_normal_view1, null, false);
//        view2_order_status= LayoutInflater.from(cx).inflate(R.layout.page_device_oven_normal_view2, null, false);
//        view3_work_status = LayoutInflater.from(cx).inflate(R.layout.page_device_oven_normal_view3, null, false);
//        view5 = LayoutInflater.from(cx).inflate(R.layout.page_device_steam226_normal_view5, null, false);
//        oven_normal_ctrl.addView(view1_disconnect_status);
//    }
//
//    @Subscribe
//    public void onEvent(SteamOvenStatusChangedEvent event) {
//        LogUtils.i("20182018", "id::"+event.pojo.getID()+"status::"+event.pojo.status);
//        if (steam == null || !Objects.equal(steam.getID(), event.pojo.getID()))
//            return;
//        if (steam.status == SteamStatus.Wait || steam.status == SteamStatus.Off || steam.status == SteamStatus.On) {
//            remoteOperation();
//        } else if (steam.status == SteamStatus.Order) {
//            //  Order_Model();
//            orderModel();
//        } else if (steam.status == SteamStatus.Working || steam.status == SteamStatus.Pause || steam.status == SteamStatus.PreHeat) {
//            workModel();
//        } else if (steam.status == SteamStatus.AlarmStatus) {
//            switch (steam.alarm) {
//                case 1:
//                    common_model(cx.getString(R.string.device_lack), R.color.gray_oven, View.VISIBLE);
//                    break;
//                case 3:
//                case 5:
//                case 6:
//                case 7:
//                    errorModel();
//                    break;
//                case 8:
//                    common_model(cx.getString(R.string.device_door_error), R.color.gray_oven, View.VISIBLE);
//                    break;
//                default:
//                    errorModel();
//                    break;
//            }
//        }
//    }
//
//    @OnClick(R.id.oven_normal_ctrl)
//    public void OnclickControl() {
//        if (!steam.isConnected()) {
//            ToastUtils.show("设备已离线", Toast.LENGTH_SHORT);
//            return;
//        }
//
//        if (steam.status == SteamStatus.Working || steam.status == SteamStatus.Pause
//                || steam.status == SteamStatus.Order || steam.status == SteamStatus.PreHeat) {
//            Bundle bd = new Bundle();
//            bd.putString(PageArgumentKey.Guid, steam.getID());
//            bd.putShort("from", (short) 1);
//            UIService.getInstance().postPage(PageKey.DeviceSteam228Working, bd);
//        } else {
//            Bundle bd = new Bundle();
//            bd.putString(PageArgumentKey.Guid, steam.getID());
//            UIService.getInstance().postPage(PageKey.DeviceSteam228Main, bd);
//        }
//    }
//
//    @OnClick(R.id.oven_setting)
//    public void OnclickSetting() {
//       // ToastUtils.show("我是设置", Toast.LENGTH_SHORT);
//        DialogOvenCommonMore.show(cx, guid);
//    }
//
//
//    @Override
//    protected void remoteOperation() {
//        common_model(cx.getString(R.string.device_oven_control), R.color.yellow_oven, View.VISIBLE);
//    }
//
//    @Override
//    protected void errorModel() {
//        common_model(cx.getString(R.string.device_steam_error), R.color.gray_oven, View.VISIBLE);
//    }
//
//    @Override
//    protected void disconnectModel() {
//        common_model(cx.getString(R.string.device_disconnect_tip), R.color.gray_oven, View.INVISIBLE);
//    }
//
//    @Override
//    protected void workModel() {
//        oven_normal_ctrl.removeAllViews();
//        oven_normal_ctrl.addView(view3_work_status);
//        TextView textView = view3_work_status.findViewById(R.id.oven_normal_ctrl_view3_tempup);
//        textView.setText(steam.temp + " ℃");
//        TextView textView1 = view3_work_status.findViewById(R.id.oven_normal_ctrl_view3_min);
//        if (steam.time % 60 != 0)
//            textView1.setText((steam.time / 60 + 1) + " 分");
//        else
//            textView1.setText((steam.time / 60) + " 分");
//        TextView txt = view3_work_status.findViewById(R.id.oven_normal_ctrl_view3_model);
//        initAutoModel(txt);
//    }
//
//    private void initAutoModel(TextView txt) {
//        switch (steam.mode) {
//            case 0://无模式
//                txt.setText(getStringName(R.string.nomode));
//                break;
//            case 17://鲜嫩蒸
//                txt.setText(getStringName(R.string.freshsteam));
//                break;
//            case 16://营养蒸
//                txt.setText(getStringName(R.string.nutritive));
//                break;
//            case 13://强力蒸
//                txt.setText(getStringName(R.string.strongsteam));
//                break;
//            case 15://快蒸慢炖
//                txt.setText(getStringName(R.string.fast_steam_slow_steam));
//                break;
//            case 19://保温
//                txt.setText(getStringName(R.string.keeptempture));
//                break;
//            case 9://解冻
//                txt.setText(getStringName(R.string.unfreeze));
//                break;
//            case 18://发酵
//                txt.setText(getStringName(R.string.ferment));
//                break;
//            case 14://杀菌
//                txt.setText(getStringName(R.string.sterilization));
//                break;
//            case 20://除垢
//                txt.setText(getStringName(R.string.clean));
//                break;
//            case 21://干燥
//                txt.setText(getStringName(R.string.dry));
//                break;
//
//        }
//    }
//
//    private String getStringName(int id) {
//        return cx.getResources().getString(id);
//    }
//
//    @Override
//    protected void orderModel() {
//        oven_normal_ctrl.removeAllViews();
//        oven_normal_ctrl.addView(view2_order_status);
//        TextView textView = view2_order_status.findViewById(R.id.oven_normal_ctrl_view2_time);
//        if (steam.orderTime_min < 10){
//            if (steam.orderTime_hour<10){
//                textView.setText("0"+steam.orderTime_hour + ":0" + steam.orderTime_min);
//            }else{
//                textView.setText(steam.orderTime_hour + ":0" + steam.orderTime_min);
//            }
//        }else{
//            if (steam.orderTime_hour<10){
//                textView.setText("0"+steam.orderTime_hour + ":" + steam.orderTime_min);
//            }else{
//                textView.setText(steam.orderTime_hour + ":" + steam.orderTime_min);
//            }
//
//        }
//
//    }
//
//    private void common_model(String str, int color, int visiable) {
//        oven_normal_ctrl.removeAllViews();
//        view1_disconnect_status.setBackgroundColor(r.getColor(color));
//        oven_normal_ctrl.addView(view1_disconnect_status);
//        TextView textView = view1_disconnect_status.findViewById(R.id.oven_normal_ctrl_waitoff_tv);
//        textView.setText(str);
//        textView.setTextColor(r.getColor(R.color.black));
//        ImageView img = view1_disconnect_status.findViewById(R.id.oven_normal_ctrl_waitoff_img);
//        img.setVisibility(visiable);
//    }
//
//    @Subscribe
//    public void onEvent(DeviceConnectionChangedEvent event) {
//        if (steam == null || !Objects.equal(steam.getID(), event.device.getID()))
//            return;
//        if (!event.isConnected) {
//            disconnectModel();
//        }
//    }
//}
