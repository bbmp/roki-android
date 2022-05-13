//package com.robam.roki.ui.page;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.common.base.Objects;
//import com.google.common.eventbus.Subscribe;
//import com.legent.plat.Plat;
//import com.legent.plat.events.DeviceConnectionChangedEvent;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.utils.api.ToastUtils;
//import com.robam.common.events.HomeRecipeViewEvent;
//import com.robam.common.events.MicroWaveAlarmEvent;
//import com.robam.common.events.MicroWaveStatusChangedEvent;
//import com.robam.common.pojos.DeviceType;
//import com.robam.common.pojos.device.IRokiFamily;
//import com.robam.common.pojos.device.microwave.MicroWaveM526;
//import com.robam.common.pojos.device.microwave.MicroWaveModel;
//import com.robam.common.pojos.device.microwave.MicroWaveStatus;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.ui.dialog.DialogMicrowave526More;
//import com.robam.roki.ui.view.DeviceCtrlRecipeView;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by Administrator on 2017/4/1.
// */
//
//public class DeviceMicrowave526Page extends BasePage {
//    String guid;
//    View contentView;
//    MicroWaveM526 microWave;
//    LayoutInflater inflater;
//    @InjectView(R.id.oven026_title)//标题
//            TextView oven026_title;
//    @InjectView(R.id.oven026_recipe_listview)//菜谱列表
//            DeviceCtrlRecipeView oven026_recipe_listview;
//    @InjectView(R.id.oven026_normal_ctrl)//控制条
//            RelativeLayout oven026_normal_ctrl;
//    @InjectView(R.id.oven026_setting)
//            TextView txtMore;
//    View view1;
//    View view2;
//    View view3;
//    View view4;
//    View view5;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle bd = getArguments();
//        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
//        microWave= Plat.deviceService.lookupChild(guid);
//        if (inflater == null) {
//            inflater = LayoutInflater.from(cx);
//        }
//        this.inflater = inflater;
//        contentView = inflater.inflate(R.layout.page_device_oven026_normal,
//                container, false);
//        ButterKnife.inject(this, contentView);
//        init();
//        return contentView;
//    }
//
//    void init() {
//        //标题栏
//        if (guid != null && guid.startsWith(IRokiFamily.RM526)) {//要需改
//            oven026_title.setText("微波炉526");
//        }
//        oven026_recipe_listview.setHeadView(initRecipeListView());
//        oven026_recipe_listview.setType(DeviceType.RWBL);
//        oven026_recipe_listview.show();
//        view1 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view1, null, false);
//        view2 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view2, null, false);
//        view3 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view3, null, false);
//        view4 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view4, null, false);
//        Disconnect_Model();
//    }
//
//
//    @Subscribe
//    public void onEvent(HomeRecipeViewEvent event) {
//        if (event.flag == HomeRecipeViewEvent.ClearMyFavorite || event.flag == HomeRecipeViewEvent.ThemeFavoriteChange
//                || event.flag == HomeRecipeViewEvent.ClearMyFavorite || event.flag == HomeRecipeViewEvent.ThemeDetailBackMyCollect
//                || event.flag == HomeRecipeViewEvent.RecipeFavoriteChange) {
//            oven026_recipe_listview.onPullDownToRefresh(null);
//        }
//    }
//
//    @Subscribe
//    public void onEvent(MicroWaveStatusChangedEvent event) {
//        if (microWave== null || !Objects.equal(microWave.getID(), event.pojo.getID()))
//            return;
//        if (microWave.state ==MicroWaveStatus.Wait) {
//            WaitAndOff_Model();//待机关机模式
//        } else if (microWave.state == MicroWaveStatus.Setting) {
//            On_Model();//设定模式
//        } else if (microWave.state == MicroWaveStatus.RunFinish) {
//            //Order_Model();//预设模式
//        } else if (microWave.state == MicroWaveStatus.Run ||microWave.state == MicroWaveStatus.Pause) {
//            Working_Model();//工作模式
//        } else if (microWave.state ==MicroWaveStatus.Alarm) {
//            //报警
//            switch (microWave.error) {
//                case 1:
//                    Water_Alarm();
//                    break;
//            }
//        }
//    }
//
//    @Subscribe
//    public void onEvent(MicroWaveAlarmEvent event) {
//        if (event.alarm== 1) {
//            Error_Model("微波炉故障");
//        }
//    }
//
//    @Subscribe
//    public void onEvent(DeviceConnectionChangedEvent event) {
//        if (microWave == null || !Objects.equal(microWave.getID(), event.device.getID()))
//            return;
//        if (!event.isConnected) {
//            Disconnect_Model();
//        }
//    }
//
//
//
//    //与设备断开连接点击事件
//    @OnClick(R.id.oven026_normal_ctrl)
//    void OnCtrlClick() {
//        if (!microWave.isConnected()) {
//            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
//            return;
//        }
//        if (microWave.state == MicroWaveStatus.Run || microWave.state ==  MicroWaveStatus.Pause
//                || microWave.state ==  MicroWaveStatus.RunFinish ) {//|| microWave.state ==  MicroWaveStatus.PreHeat
//            Bundle bd = new Bundle();
//            bd.putString(PageArgumentKey.Guid, microWave.getID());
//            bd.putShort("from", (short) 0);
//            bd.putString(PageArgumentKey.selectflag,"false");
//            UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bd);
//       } else {
//            Bundle bd = new Bundle();
//            bd.putString(PageArgumentKey.Guid,microWave.getID());
//            UIService.getInstance().postPage(PageKey.DeviceMicrowave526Main, bd);
//        }
//    }
//    //返回
//    @OnClick(R.id.oven026_return)
//    public void OnClickReturn() {
//        UIService.getInstance().popBack();
//    }
//    //更多
//    @OnClick(R.id.oven026_setting)
//    public void OnClickMore(){
//        DialogMicrowave526More.show(cx,guid);
//    }
//
//    View initRecipeListView() {
//        View view = inflater.inflate(R.layout.page_device_oven026_listview_head, null, false);
//        view.findViewById(R.id.oven026_recipe_listview_headview_myexclusive).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.show("我的专属菜谱暂未上线", Toast.LENGTH_SHORT);
//            }
//        });
//        view.findViewById(R.id.oven026_recipe_listview_headview_mycollection).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //ToastUtils.show(new String("导航菜谱尚未开通，敬请期待"),Toast.LENGTH_SHORT);
//                UIService.getInstance().postPage(PageKey.AbsThemeRecipeListGrid);
//            }
//        });
//        return view;
//    }
//
//    public void WaitAndOff_Model() {
//        oven026_normal_ctrl.removeAllViews();
//        view1.setBackgroundColor(r.getColor(R.color.white));
//        oven026_normal_ctrl.addView(view1);
//        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
//        textView.setText("进入远程操控");
//        textView.setTextColor(r.getColor(R.color.Gray_57));
//        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
//        img.setVisibility(View.VISIBLE);
//        img.setImageResource(R.mipmap.img_waterpurifiy_filterright_red);
//    }
//
//    public void On_Model() {
//        oven026_normal_ctrl.removeAllViews();
//        view1.setBackgroundColor(Color.parseColor("#FEFCCC"));
//        oven026_normal_ctrl.addView(view1);
//        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
//        textView.setText("进入远程操控");
//        textView.setTextColor(r.getColor(R.color.Gray_57));
//        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
//        img.setVisibility(View.VISIBLE);
//        img.setImageResource(R.mipmap.img_waterpurifiy_filterright_red);
//    }
//
//    public void Error_Model(String string) {
//        oven026_normal_ctrl.removeAllViews();
//        view1.setBackgroundColor(r.getColor(R.color.c06));
//        oven026_normal_ctrl.addView(view1);
//        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
//        textView.setText(string);
//        textView.setTextColor(r.getColor(R.color.white));
//        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
//        img.setVisibility(View.VISIBLE);
//        img.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
//    }
//
//    public void Disconnect_Model() {
//        oven026_normal_ctrl.removeAllViews();
//        view1.setBackgroundColor(r.getColor(R.color.white));
//        oven026_normal_ctrl.addView(view1);
//        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
//        textView.setText("与设备断开连接");
//        textView.setTextColor(r.getColor(R.color.c06));
//        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
//        img.setVisibility(View.INVISIBLE);
//    }
//
//  /*  //要修改
//    public void Order_Model() {
//        oven026_normal_ctrl.removeAllViews();
//        view2.setBackgroundColor(Color.parseColor(new String("#FEFCCC")));
//        oven026_normal_ctrl.addView(view2);
//        TextView textView = (TextView) view2.findViewById(R.id.oven026_normal_ctrl_view2_time);
//        if (steam226.orderTime_min < 10)
//            textView.setText(steam226.orderTime_hour + ":0" + steam226.orderTime_min);
//        else
//            textView.setText(steam226.orderTime_hour + ":" + steam226.orderTime_min);
//    }*/
//
//    public void Water_Alarm() {
//        oven026_normal_ctrl.removeAllViews();
//        view5.setBackgroundColor(r.getColor(R.color.c06));
//        oven026_normal_ctrl.addView(view5);
//    }
//
//    //要修改
//    public void Working_Model() {
//        oven026_normal_ctrl.removeAllViews();
//        view3.setBackgroundColor(Color.parseColor("#FEFCCC"));
//        oven026_normal_ctrl.addView(view3);
//        TextView fireTitle= view3.findViewById(R.id.txt_settitle);
//        TextView textView = view3.findViewById(R.id.oven026_normal_ctrl_view3_tempup);
//        TextView timeWorking= view3.findViewById(R.id.txt_changetitle);
//        fireTitle.setText("火力");
//        if (microWave.mode == MicroWaveModel.Barbecue) {
//            switch (microWave.power) {
//                case 7:
//                    textView.setText(6 + "");
//                    break;
//                case 8:
//                    textView.setText(4 + "");
//                    break;
//                case 9:
//                    textView.setText(2 + "");
//                    break;
//                default:
//                    break;
//            }
//        }else if(microWave.mode == MicroWaveModel.ComibineHeating){
//            switch (microWave.power){
//                case 10:
//                    textView.setText(6 + "");break;
//                case 11:
//                    textView.setText(4 + "");break;
//                case 12:
//                    textView.setText(2 + "");break;
//                default:break;
//            }
//        }else{
//            textView.setText(microWave.power+"");
//        }
//        timeWorking.setText("工作时间");
//        TextView textView1 = view3.findViewById(R.id.oven026_normal_ctrl_view3_min);
//        if (microWave.time!=0){
//            if (microWave.time/60<10){
//                if (microWave.time%60<10){
//                    textView1.setText("0"+(microWave.time / 60 ) + ":0"+(microWave.time%60));
//                }else{
//                    textView1.setText("0"+(microWave.time / 60 ) + ":"+(microWave.time%60));
//                }
//            }else{
//                if (microWave.time%60<10){
//                    textView1.setText((microWave.time / 60 ) + ":0"+(microWave.time%60));
//                }else{
//                    textView1.setText((microWave.time / 60 ) + ":"+(microWave.time%60));
//                }
//            }
//        }
//            ImageView img = view3.findViewById(R.id.oven026_normal_ctrl_view3_model);
//            initAutoModel(img);
//    }
//
//    void initAutoModel(ImageView imageView) {
//        switch (microWave.mode) {
//            case 1://黑椒牛排
//                imageView.setImageResource(R.mipmap.ic_micro526_beef_black);
//                break;
//            case 2://香烤全鸡
//                imageView.setImageResource(R.mipmap.ic_micro526_chicken_black);
//                break;
//            case 3://风味肉串
//                imageView.setImageResource(R.mipmap.ic_micro526_kaorou_black);
//                break;
//            case 4://米饭
//                imageView.setImageResource(R.mipmap.ic_micro526_porridge_black);
//                break;
//            case 5://粥
//                imageView.setImageResource(R.mipmap.ic_micro526_rice_black);
//                break;
//            case 6://汤
//                imageView.setImageResource(R.mipmap.ic_micro526_baotang_black);
//                break;
//            case 7://面包
//                imageView.setImageResource(R.mipmap.ic_micro526_bread_black);
//                break;
//            case 8://时蔬
//                imageView.setImageResource(R.mipmap.ic_micro526_vegetable_black);
//                break;
//            case 9://蒸鱼
//                imageView.setImageResource(R.mipmap.ic_micro526_fish_black);
//                break;
//            case 10://再加热
//                imageView.setImageResource(R.mipmap.ic_micro526_heatagain_black);
//                break;
//            case 50://微波火力
//                imageView.setImageResource(R.mipmap.ic_micro526_wave_black);
//                break;
//            case 51://薄快烧烤
//                imageView.setImageResource(R.mipmap.ic_micro526_saokao_black);
//                break;
//            case 52://组合烧烤
//                imageView.setImageResource(R.mipmap.ic_micro526_combine_black);
//                break;
//            case 255://解冻
//                imageView.setImageResource(R.mipmap.ic_micro526_jiedong_black);
//                break;
//            case 254://去味
//                imageView.setImageResource(R.mipmap.ic_micro526_quwei_black);
//                break;
//            default:
//                break;
//        }
//    }
//}
