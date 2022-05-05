package com.robam.roki.ui.page;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback3;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;

import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.R;
import com.robam.roki.ui.view.IntelPotPadShowView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;




/**
 * Created by Dell on 2018/4/11.
 */

public class DevicePotPage extends BasePage {

    Pot[] pot;
    View contentView;
    LayoutInflater inflater;
    VoidCallback3<Integer> callback3;

    @InjectView(R.id.pot_view)
    IntelPotPadShowView pot_view;
    @InjectView(R.id.pot_intelli_device_status)
    TextView pot_intelli_device_status;
    @InjectView(R.id.pot_intelli_device_text1)
    TextView pot_intelli_device_text1;
    @InjectView(R.id.pot_intelli_device_text2)
    TextView pot_intelli_device_text2;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.pot_show,container,false);
        ButterKnife.inject(this,contentView);
        pot = Utils.getDefaultPot();
        if (pot[0]!=null&&pot[0].isConnected())
            onWaitStatus();

        else {
          //  onDisconnectStatus();
            pot_view.setModel(IntelPotPadShowView.IntelPotShow_Model.StandBy, null);
        }
        callback3 = new VoidCallback3<Integer>() {
            @Override
            public void onCompleted(final Integer integer) {
                UIService.getInstance().getTop().getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i("20180411","inter::"+integer);
                        onShowTemStatus(integer);
                    }
                });
            }
        };
        return contentView;
    }

    private void onWaitStatus() {
        pot_intelli_device_status.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.pot_return)
    public void back(){
        ToastUtils.show("返回", Toast.LENGTH_SHORT);
        UIService.getInstance().returnHome();
    }

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (event.pojo == null)
            return;
        if (Plat.DEBUG)
            LogUtils.i("20170406", "DeviceIntelliPotView 接受到温度:" + pot[0].tempUp + "  this:" + pot + "  " + pot[0].getID());
        if (pot[0].tempUp < 50) {
            onWaitStatus();
            pot_view.setModel(IntelPotPadShowView.IntelPotShow_Model.StandBy, null);
        } else {
            pot_view.setModel(IntelPotPadShowView.IntelPotShow_Model.ShowTemp,callback3, pot[0].tempUp + "");
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (pot[0] == null || !Objects.equal(pot[0].getID(), event.device.getID()))
            return;
        if (Plat.DEBUG)
            LogUtils.i("20170406", "DeviceIntelliPotView 连接情况:" + event.isConnected + "  " + pot[0].getID());
        if (!event.isConnected) {
            onDisconnectStatus();
            pot_view.setModel(IntelPotPadShowView.IntelPotShow_Model.StandBy, null);
        }
    }


    /**
     * 离线状态
     */
    private void onDisconnectStatus() {
    //    pot_intelli_device_curve.setVisibility(INVISIBLE);
        pot_intelli_device_status.setVisibility(View.VISIBLE);
        pot_intelli_device_status.setText(R.string.device_disconnect);
        pot_intelli_device_text1.setVisibility(View.VISIBLE);
        pot_intelli_device_text2.setVisibility(View.VISIBLE);
        pot_intelli_device_text1.setText(R.string.pot_disconnect_hint1);
        pot_intelli_device_text2.setText(R.string.pot_disconnect_hint2);
    }

    /**
     * 显示温度状态
     */
    private synchronized void onShowTemStatus(int level) {
        if (level < -100) {
            if (level == -101) {
                pot_intelli_device_status.setText("冷油温适合炒坚果。");
                pot_intelli_device_text1.setText("适合冷炒花生、腰果、肉松、杂酱等，也适合加入食用油热油。");
            } else if (level == -102) {
                pot_intelli_device_status.setText("低油温适合软炸。");
                pot_intelli_device_text1.setText("低油温具有保鲜嫩、去水份的作用，适合软炸虾仁、炸鱿鱼，炸藕盒，必要时可复炸。");
            } else if (level == -103) {
                pot_intelli_device_status.setText("中油温适合干炸、酥炸。");
                pot_intelli_device_text1.setText("最适合将葱姜蒜等辅料爆香，放食材后蛋白质可快速凝固，原料不易碎烂。适合滑炒肉丝、鸡蛋等。");
            } else if (level == -104) {
                pot_intelli_device_status.setText("高温度适合清炸、泼油。");
                pot_intelli_device_text1.setText("适合水煮类菜肴，如给水煮鱼片、油泼辣椒等菜肴的最后一道工序。");
            } else if (level == -105) {
                pot_intelli_device_status.setText("油温过热。");
                pot_intelli_device_text1.setText("建议调小火力,维持健康烹饪。");
            } else if (level == -106) {
                pot_intelli_device_status.setText("干烧预警");
                pot_intelli_device_text1.setText("锅中食材水份已煮干,请注意防止糊锅。");
            }
        } else {
            /*pot_intelli_device_curve.setVisibility(VISIBLE);
            pot_intelli_device_text1.setVisibility(VISIBLE);
            pot_intelli_device_text2.setVisibility(INVISIBLE);
            if (level == 1) {
                pot_intelli_device_curve.setImageResource(R.mipmap.ic_potcurve_yellow1);
            } else if (level == 2) {
                pot_intelli_device_curve.setImageResource(R.mipmap.ic_potcurve_yellow2);
            } else if (level == 3) {
                pot_intelli_device_curve.setImageResource(R.mipmap.ic_potcurve_red);

            }*/
        }
    }



}
