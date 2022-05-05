package com.robam.roki.ui.page;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.DialogOvenCommonMore;
import com.robam.roki.ui.view.DeviceCtrlRecipeView;
import com.robam.roki.utils.StringConstantsUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rent on 2016/10/08.
 */
public class DeviceSteam226Page extends BasePage {
    String guid;
    View contentView;
    AbsSteamoven steam226;
    LayoutInflater inflater;
    @InjectView(R.id.oven026_title)//标题
            TextView oven026_title;
    @InjectView(R.id.oven026_recipe_listview)//菜谱列表
            DeviceCtrlRecipeView oven026_recipe_listview;
    @InjectView(R.id.oven026_normal_ctrl)//控制条
            RelativeLayout oven026_normal_ctrl;

    View view1;
    View view2;
    View view3;
    View view4;
    View view5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        LogUtils.i("20170307","guid:"+guid);
        steam226 = Plat.deviceService.lookupChild(guid);
        LogUtils.i("20170307","steam226:"+steam226.getGuid());
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_oven026_normal,
                container, false);
        ButterKnife.inject(this, contentView);
        init();
        return contentView;
    }

    void init() {
        oven026_title.setText("蒸汽炉"+steam226.getDispalyType());
        oven026_recipe_listview.setHeadView(initRecipeListView());
        oven026_recipe_listview.setType(DeviceType.RZQL);
        oven026_recipe_listview.show();
        view1 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view1, null, false);
        view2 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view2, null, false);
        view3 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view3, null, false);
        view4 = LayoutInflater.from(cx).inflate(R.layout.page_device_oven026_normal_view4, null, false);
        view5 = LayoutInflater.from(cx).inflate(R.layout.page_device_steam226_normal_view5, null, false);
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
    public void onEvent(SteamOvenStatusChangedEvent event) {
        LogUtils.i("20182018",event.pojo.getID());
        if (steam226 == null || !Objects.equal(steam226.getID(), event.pojo.getID()))
            return;
        if (steam226.status == SteamStatus.Wait || steam226.status == SteamStatus.Off) {
            WaitAndOff_Model();
        } else if (steam226.status == SteamStatus.On) {
            On_Model();
        } else if (steam226.status == SteamStatus.Order) {
            Order_Model();
        } else if (steam226.status == SteamStatus.Working || steam226.status == SteamStatus.Pause || steam226.status == SteamStatus.PreHeat) {
            Working_Model();
        } else if (steam226.status == SteamStatus.AlarmStatus) {
            switch (steam226.alarm) {
                case 1:
                    Water_Alarm();
                    break;
                case 3:
                case 5:
                case 6:
                    Error_Model(cx.getString(R.string.device_steam_failure));
                    break;
            }
        }
    }

    @Subscribe
    public void onEvent(SteamAlarmEvent event) {
        if (event.alarmId == 6) {
            Error_Model(cx.getString(R.string.device_steam_failure));
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        LogUtils.i("201703010", "eventId226:" + event.device.getID());
        if (steam226 == null || !Objects.equal(steam226.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            Disconnect_Model();
        }
    }

    @OnClick(R.id.oven026_setting)
    public void onClickMore(){
        DialogOvenCommonMore.show(cx, guid);
    }

    @OnClick(R.id.oven026_normal_ctrl)
    void OnCtrlClick() {
        LogUtils.i("201703010", "steam226.isConnected():" + steam226.isConnected());
        if (!steam226.isConnected()) {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
            return;
        }
        if (steam226.status == SteamStatus.Working || steam226.status == SteamStatus.Pause
                || steam226.status == SteamStatus.Order || steam226.status == SteamStatus.PreHeat) {
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, steam226.getID());
                bd.putShort("from", (short) 1);
                UIService.getInstance().postPage(PageKey.DeviceSteam226Working, bd);

        } else {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, steam226.getID());
            UIService.getInstance().postPage(PageKey.DeviceSteam226Main, bd);
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
                //ToastUtils.show(new String("导航菜谱尚未开通，敬请期待"),Toast.LENGTH_SHORT);
                UIService.getInstance().postPage(PageKey.AbsThemeRecipeListGrid);
            }
        });
        return view;
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
        img.setImageResource(R.mipmap.img_waterpurifiy_filterright_white);
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
        if (steam226.orderTime_min < 10)
            textView.setText(steam226.orderTime_hour + ":0" + steam226.orderTime_min);
        else
            textView.setText(steam226.orderTime_hour + ":" + steam226.orderTime_min);
    }

    public void Water_Alarm() {
        oven026_normal_ctrl.removeAllViews();
        view5.setBackgroundColor(r.getColor(R.color.c06));
        oven026_normal_ctrl.addView(view5);
    }

    public void Working_Model() {
        oven026_normal_ctrl.removeAllViews();
        view3.setBackgroundColor(Color.parseColor("#FEFCCC"));
        oven026_normal_ctrl.addView(view3);
        TextView textView = view3.findViewById(R.id.oven026_normal_ctrl_view3_tempup);
        textView.setText(steam226.temp + " ℃");
        TextView textView1 = view3.findViewById(R.id.oven026_normal_ctrl_view3_min);
        if (steam226.time % 60 != 0)
            textView1.setText((steam226.time / 60 + 1) + StringConstantsUtil.STRING_MIN);
        else
            textView1.setText((steam226.time / 60) + StringConstantsUtil.STRING_MIN);
        ImageView img = view3.findViewById(R.id.oven026_normal_ctrl_view3_model);
        if ("RS275".equals(steam226.getDt())){
            initAutoModel275(img);
        }else{
            initAutoModel(img);
        }

    }

    void initAutoModel275(ImageView imageView){
        switch (steam226.mode) {
            case 3://鱼
                imageView.setImageResource(R.mipmap.ic_275_finish);
                break;
            case 4://蛋
                imageView.setImageResource(R.mipmap.ic_275_shouye_egg);
                break;
            case 7://蔬菜
                imageView.setImageResource(R.mipmap.ic_275_shucai_shouye);
                break;
            case 11://馒头
                imageView.setImageResource(R.mipmap.ic_275_mianshi_shouye);
                break;
            case 13://强蒸
                imageView.setImageResource(R.mipmap.ic_275_shouye_suzheng);
                break;
            case 14://杀菌
                imageView.setImageResource(R.mipmap.ic_275_shouye_shajun);
                break;
            case 20://除垢
                imageView.setImageResource(R.mipmap.ic_275_shouye_clear);
                break;
            case 9://解冻
                imageView.setImageResource(R.mipmap.ic_275_shouye_jiedong);
                break;
            default:
                break;
        }
    }

    void initAutoModel(ImageView imageView) {
        switch (steam226.mode) {
            case 3://鱼
                imageView.setImageResource(R.mipmap.ic_steam226_fish_black);
                break;
            case 4://蛋
                imageView.setImageResource(R.mipmap.ic_steam226_egg_black);
                break;
            case 6://蹄筋
                imageView.setImageResource(R.mipmap.ic_steam226_tendon_black);
                break;
            case 7://蔬菜
                imageView.setImageResource(R.mipmap.ic_steam226_vegetable_black);
                break;
            case 10://五花肉
                imageView.setImageResource(R.mipmap.ic_steam226_meat_black);
                break;
            case 11://馒头
                imageView.setImageResource(R.mipmap.ic_steam226_pastry_black);
                break;
            case 12://米饭
                imageView.setImageResource(R.mipmap.ic_steam226_rice_black);
                break;
            case 13://强蒸
                imageView.setImageResource(R.mipmap.ic_steam226_strongsteam_black);
                break;
            case 14://杀菌
                imageView.setImageResource(R.mipmap.ic_steam226_sterilization_black);
                break;
            default:
                break;
        }
    }
}
