package com.robam.roki.ui.page;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.DialogOvenCommonMore;
import com.robam.roki.ui.dialog.OvenBroken026Dialog;
import com.robam.roki.ui.view.DeviceCtrlRecipeView;
import com.robam.roki.utils.StringConstantsUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rent on 2016/10/08.
 */
public class DeviceOven026Page extends BasePage {
    String guid;
    View contentView;
    AbsOven oven026;
    LayoutInflater inflater;
    @InjectView(R.id.oven026_title)
    TextView oven026_title;
    @InjectView(R.id.oven026_recipe_listview)
    DeviceCtrlRecipeView oven026_recipe_listview;
    @InjectView(R.id.oven026_normal_ctrl)
    RelativeLayout oven026_normal_ctrl;

    View view1;
    View view2;
    View view3;
    View view4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);

        oven026 = Plat.deviceService.lookupChild(guid);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_oven026_normal, container, false);
        ButterKnife.inject(this, contentView);
        init();
        return contentView;
    }

    void init() {
        /*if(guid.startsWith("RR016")){
            oven026_title.setText(cx.getString(R.string.device_oven016_name));//RR016
        }else if (guid.startsWith("RR026")){
            oven026_title.setText(cx.getString(R.string.device_oven026_name));//RR075
        }else if (){
            oven026_title.setText(cx.getString(R.string.device_oven075_name));
        }*/
        oven026_title.setText("电烤箱"+oven026.getDispalyType());
        oven026_recipe_listview.setHeadView(initRecipeListView());
        oven026_recipe_listview.setType(DeviceType.RDKX);
        oven026_recipe_listview.show();
        view1 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view1, null, false);
        view2 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view2, null, false);
        view3 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view3, null, false);
        view4 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view4, null, false);
        Disconnect_Model();




    }


    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        if (event.flag == HomeRecipeViewEvent.ClearMyFavorite || event.flag == HomeRecipeViewEvent.ThemeFavoriteChange
                || event.flag == HomeRecipeViewEvent.ClearMyFavorite || event.flag == HomeRecipeViewEvent.ThemeDetailBackMyCollect
                || event.flag == HomeRecipeViewEvent.RecipeFavoriteChange) {
            oven026_recipe_listview.onPullDownToRefresh(null);
        }
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (oven026 == null || !Objects.equal(oven026.getID(), event.pojo.getID()))
            return;
        if (oven026.status == OvenStatus.Wait || oven026.status == OvenStatus.Off) {
            WaitAndOff_Model();
        } else if (oven026.status == OvenStatus.On) {
            On_Model();
        } else if (oven026.status == OvenStatus.Order) {
            Order_Model();
        } else if (oven026.status == OvenStatus.Working || oven026.status == OvenStatus.Pause
                || oven026.status == OvenStatus.PreHeat) {
            Working_Model();
        } else if (oven026.status == OvenStatus.AlarmStatus) {
            switch (oven026.alarm){
                case 3:
                case 5:
                case 6:
                    Error_Model(cx.getString(R.string.device_oven_failure)+"0"+oven026.alarm);
                    break;
            }
        }
        if (oven026.status != OvenStatus.AlarmStatus) {
            if (ovenWarningDialog != null && ovenWarningDialog.isShowing()) {
                ovenWarningDialog.dismiss();
                ovenWarningDialog = null;
            }
        }
    }


    @OnClick(R.id.oven026_setting)
    public void onClickMore(){
        DialogOvenCommonMore.show(cx, guid);
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (oven026 == null || !Objects.equal(oven026.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            Disconnect_Model();
        }
    }

    public void WaitAndOff_Model() {
        oven026_normal_ctrl.removeAllViews();
        view1.setBackgroundColor(r.getColor(R.color.white));
        oven026_normal_ctrl.addView(view1);
        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
        textView.setText(cx.getString(R.string.device_in_remote_control));
        textView.setTextColor(r.getColor(R.color.Gray_57));
        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
        img.setVisibility(View.VISIBLE);
        img.setImageResource(R.mipmap.img_waterpurifiy_filterright_red);
    }

    public void On_Model() {
        oven026_normal_ctrl.removeAllViews();
        view1.setBackgroundColor(Color.parseColor("#FEFCCC"));
        oven026_normal_ctrl.addView(view1);
        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
        textView.setText(cx.getString(R.string.device_in_remote_control));
        textView.setTextColor(r.getColor(R.color.Gray_57));
        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
        img.setVisibility(View.VISIBLE);
        img.setImageResource(R.mipmap.img_waterpurifiy_filterright_red);
    }

    public void Error_Model(String string) {
        oven026_normal_ctrl.removeAllViews();
        view1.setBackgroundColor(r.getColor(R.color.c06));
        oven026_normal_ctrl.addView(view1);
        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
        textView.setText(string);
        textView.setTextColor(r.getColor(R.color.white));
        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
        img.setVisibility(View.VISIBLE);
        img.setImageResource(R.mipmap.ic_device_item_detail);
    }

    public void Disconnect_Model() {
        oven026_normal_ctrl.removeAllViews();
        view1.setBackgroundColor(r.getColor(R.color.white));
        oven026_normal_ctrl.addView(view1);
        TextView textView = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_tv);
        textView.setText(cx.getString(R.string.device_disconnect));
        textView.setTextColor(r.getColor(R.color.c06));
        ImageView img = view1.findViewById(R.id.oven026_normal_ctrl_waitoff_img);
        img.setVisibility(View.INVISIBLE);
    }

    public void Order_Model() {
        oven026_normal_ctrl.removeAllViews();
        view2.setBackgroundColor(Color.parseColor("#FEFCCC"));
        oven026_normal_ctrl.addView(view2);
        TextView textView = view2.findViewById(R.id.oven026_normal_ctrl_view2_time);
        if(oven026.orderTime_min<10)
            textView.setText(oven026.orderTime_hour + ":0" + oven026.orderTime_min);
        else
            textView.setText(oven026.orderTime_hour + ":" + oven026.orderTime_min);
    }

    public void Working_Model() {
        if (oven026.runP == OvenMode.EXP) {
            oven026_normal_ctrl.removeAllViews();
            view4.setBackgroundColor(Color.parseColor("#FEFCCC"));
            oven026_normal_ctrl.addView(view4);
            TextView textView = view4.findViewById(R.id.oven026_normal_ctrl_view4_tempup);
            textView.setText(oven026.temp + " ℃");
            textView = view4.findViewById(R.id.oven026_normal_ctrl_view4_tempdown);
            textView.setText(oven026.currentTempDownValue + " ℃");
            textView = view4.findViewById(R.id.oven026_normal_ctrl_view4_min);
            if (oven026.time % 60 != 0)
                textView.setText((oven026.time / 60 + 1) + StringConstantsUtil.STRING_MIN);
            else
                textView.setText((oven026.time / 60) + StringConstantsUtil.STRING_MIN);
            ImageView img = view4.findViewById(R.id.oven026_normal_ctrl_view4_model);
            if ("RR075".equals(oven026.getDt())){
                if (oven026.autoMode!=0){
                    initAutoModel(img);
                }else{
                    init075Model(img);
                }

            }else{
                if (oven026.autoMode != 0) {
                    initAutoModel(img);
                } else {
                    initExpModel(img);
                }
            }


        } else {
            oven026_normal_ctrl.removeAllViews();
            view3.setBackgroundColor(Color.parseColor("#FEFCCC"));
            oven026_normal_ctrl.addView(view3);
            TextView textView = view3.findViewById(R.id.oven026_normal_ctrl_view3_tempup);
            textView.setText(oven026.temp + " ℃");
            TextView textView1 = view3.findViewById(R.id.oven026_normal_ctrl_view3_min);
            if (oven026.time % 60 != 0)
                textView1.setText((oven026.time / 60 + 1) + StringConstantsUtil.STRING_MIN);
            else
                textView1.setText((oven026.time / 60) + StringConstantsUtil.STRING_MIN);
            ImageView img = view3.findViewById(R.id.oven026_normal_ctrl_view3_model);
            if ("RR075".equals(oven026.getDt())){
                if (oven026.autoMode!=0){
                    initAutoModel(img);
                }else{
                    init075Model(img);
                }

            }else{
                if (oven026.autoMode != 0) {
                    initAutoModel(img);
                } else {
                    initExpModel(img);
                }
            }
        }
    }

    @OnClick(R.id.oven026_normal_ctrl)
    void OnCtrlClick() {
        if (!oven026.isConnected()) {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
            return;
        }
        if (oven026.status == OvenStatus.Working || oven026.status == OvenStatus.Pause
                || oven026.status == OvenStatus.Order || oven026.status == OvenStatus.PreHeat) {
            Bundle bd = new Bundle();
            bd.putShort("from", (short) 1);
            bd.putString(PageArgumentKey.Guid, oven026.getID());
            UIService.getInstance().postPage(PageKey.DeviceOven026Working, bd);
        } else {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, oven026.getID());
            bd.putShort("from", (short) 0);
            UIService.getInstance().postPage(PageKey.DeviceOven026Main, bd);
        }
    }

    @OnClick(R.id.oven026_return)
    public void OnClickReturn() {
        UIService.getInstance().popBack();
    }

    View initRecipeListView() {
        View view = inflater.inflate(R.layout.page_device_oven026_listview_head, null, false);
        view.findViewById(R.id.oven026_recipe_listview_headview_myexclusive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(R.string.device_recipe_not_line, Toast.LENGTH_SHORT);
            }
        });
        view.findViewById(R.id.oven026_recipe_listview_headview_mycollection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().postPage(PageKey.AbsThemeRecipeListGrid);
            }
        });
        return view;
    }

    void init075Model(ImageView img){
        switch (oven026.runP){
            case 1://快热
                img.setImageResource(R.mipmap.img_075_shouye_kuaire);
                break;
            case 2://风焙烤
                img.setImageResource(R.mipmap.img_075_shouye_fengbeikao);
                break;
            case 3://焙烤
                img.setImageResource(R.mipmap.img_075_shouye_beikao);
                break;
            case 4://底加热
                img.setImageResource(R.mipmap.img_075_shouye_dijiare);
                break;
            case 6://风扇考
                img.setImageResource(R.mipmap.img_075_shouye_fengshankao);
                break;
            case 7://烤烧
                img.setImageResource(R.mipmap.img_075_shouye_kaosao);
                break;
            case 8://强烤烧
                img.setImageResource(R.mipmap.img_075_shouye_qiangsaokao);
                break;
            case 9://EXP
                img.setImageResource(R.mipmap.ic_026ovenwork_zhuanjia_black);
                break;
            default:
                break;
        }
    }

    void initAutoModel(ImageView imageView) {
        switch (oven026.autoMode) {
            case 1://烤牛排
                imageView.setImageResource(R.mipmap.ic_026ovenwork_beef_black);
                break;
            case 2://烤面包
                imageView.setImageResource(R.mipmap.ic_026ovenwork_bread_black);
                break;
            case 3://烤饼干
                imageView.setImageResource(R.mipmap.ic_026ovenwork_biscuits_black);
                break;
            case 4://烤鸡翅
                imageView.setImageResource(R.mipmap.ic_026ovenwork_chicken_black);
                break;
            case 5://烤蛋糕
                imageView.setImageResource(R.mipmap.ic_026ovenwork_cake_black);
                break;
            case 6://烤披萨
                imageView.setImageResource(R.mipmap.ic_026ovenwork_pizza_pizza_black);
                break;
            case 7://强虾
                imageView.setImageResource(R.mipmap.ic_026ovenwork_shrimp_black);
                break;
            case 8://烤鱼
                imageView.setImageResource(R.mipmap.ic_026ovenwork_fish_black);
                break;
            case 9://烤红薯
                imageView.setImageResource(R.mipmap.ic_026ovenwork_sweetpotato_black);
                break;
            case 10://烤玉米
                imageView.setImageResource(R.mipmap.ic_026ovenwork_corn_black);
                break;
            case 11://烤五花肉
                imageView.setImageResource(R.mipmap.ic_026ovenwork_wuhuarou_black);
                break;
            case 12://烤蔬菜
                imageView.setImageResource(R.mipmap.ic_026ovenwork_vegetables_black);
                break;
            default:
                break;
        }
    }


    void initExpModel(ImageView imageView) {
        switch (oven026.runP) {
            case 1://快热
                imageView.setImageResource(R.mipmap.ic_026ovenwork_kuaire_black);
                break;
            case 2://风焙烤
                imageView.setImageResource(R.mipmap.ic_026ovenwork_fengbeikao_black);
                break;
            case 3://焙烤
                imageView.setImageResource(R.mipmap.ic_026ovenwork_beikao_black);
                break;
            case 4://底加热
                imageView.setImageResource(R.mipmap.ic_026ovenwork_dijiare_black);
                break;
            case 6://风扇考
                imageView.setImageResource(R.mipmap.ic_026ovenwork_fengshankao_black);
                break;
            case 7://烤烧
                imageView.setImageResource(R.mipmap.ic_026ovenwork_shaokao_black);
                break;
            case 8://强烤烧
                imageView.setImageResource(R.mipmap.ic_026ovenwork_qiangkaoshao_black);
                break;
            case 9://EXP
                imageView.setImageResource(R.mipmap.ic_026ovenwork_zhuanjia_black);
                break;
            default:
                break;
        }
    }

    private OvenBroken026Dialog ovenWarningDialog = null;//报警

    /**
     * 报警
     **/
    @Subscribe
    public void onEvent(OvenAlarmEvent event) {
        if (!PageKey.DeviceOven026.equals(UIService.getInstance().getTop().getCurrentPageKey()))
            return;
        if (ovenWarningDialog != null && ovenWarningDialog.isShowing()) {
            ovenWarningDialog.dismiss();
            ovenWarningDialog = null;
        }
        switch (event.alarmId) {
            case AbsOven.Event_Oven_Heat_Fault:
                //showDialog("错误：加热故障", event.alarmId);
                break;
            case AbsOven.Event_Oven_Alarm_Senor_Fault:
                //showDialog("错误：传感器故障", event.alarmId);
                break;
            case AbsOven.Event_Oven_Communication_Fault:
               // showDialog("错误：通信故障", event.alarmId);
                Error_Model(cx.getString(R.string.device_oven_failure));
                break;
            default:
                break;
        }
    }

}
