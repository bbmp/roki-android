package com.robam.roki.ui.page;

import android.os.Bundle;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by Dell on 2018/1/17.
 */

public class DeviceOven075ProfessionSettingPage extends AbsDeviceOvenProfessionSetPage {
    @Override
    public void initData() {
        super.initData();
        List<String> list = Lists.newArrayList();
        list.add("快热");
        list.add("风焙烤");
        list.add("焙烤");
        list.add("底加热");
        list.add("风扇烤");
        list.add("烤烧");
        list.add("强烤烧");
        modeWheel.initData(list);
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        Map<Integer, Integer> map = Maps.newHashMap();
        map.put(0, R.mipmap.ic_oven028_recently_fasetheat_new);
        map.put(1, R.mipmap.ic_oven028_recently_fengbeikao_new);
        map.put(2, R.mipmap.ic_oven028_recently_beikao);
        map.put(3, R.mipmap.ic_oven028_recently_bottomheat);
        map.put(4, R.mipmap.ic_oven028_recently_fengshankao);
        map.put(5, R.mipmap.ic_oven028_recently_kaosao);
        map.put(6, R.mipmap.ic_oven028_recently_qingkaosao);
        modeWheel.setMapImg(map);
    }

    @Override
    protected void setDefaultValue(int index, Object item) {
        List<?> list1 = getList2(item);
        List<?> list2 = getList3(item);
        temWheel.setData(list1);
        timeWheel.setData(list2);
        int def1 = 0, def2 = 0;
        if (index == 0) {//快热
            def1 = 150;
            def2 = 49;
        } else if (index == 1) {//风焙烤
            def1 = 140;
            def2 = 59;
        } else if (index == 2) {//焙烤
            def1 = 100;
            def2 = 59;
        } else if (index == 3) {//底加热
            def1 = 110;
            def2 = 49;
        } else if (index == 4) {//风扇烤
            def1 = 160;
            def2 = 59;
        } else if (index == 5) {//烤烧
            def1 = 130;
            def2 = 49;
        } else if (index == 6) {//强烤烧
            def1 = 140;
            def2 = 39;
        }
        handler.sendEmptyMessage(0x03);
        temWheel.setDefault(def1);
        timeWheel.setDefault(def2);
    }

    /*设置各种模式温度范围*/
    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;

        if (s.equals("底加热")) {
            for (int i = 50; i <= 180; i++) {
                list.add(i);
            }
        } else if (s.equals("快热") || s.equals("烤烧")) {
            for (int i = 50; i <= 250; i++) {
                list.add(i);
            }
        } else if (s.equals("风焙烤") || s.equals("焙烤") || s.equals("风扇烤")) {
            for (int i = 60; i <= 250; i++) {
                list.add(i);
            }
        } else if (s.equals("强烤烧")) {
            for (int i = 40; i <= 250; i++) {
                list.add(i);
            }
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if (s.equals("快热") || s.equals("风焙烤") || s.equals("风扇烤")
                || s.equals("烤烧") || s.equals("强烤烧") || s.equals("焙烤") || s.equals("底加热")) {
            for (int i = 1; i <= 90; i++) {
                list.add(i);
            }
        }
        return list;
    }

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!oven.isConnected()) {
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return;
        }

        if (oven.status == OvenStatus.AlarmStatus) {
            ToastUtils.show(getString(R.string.mac_error), Toast.LENGTH_SHORT);
            return;
        }
        short model = getModel();
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
        oven.setOvenRunMode(model, time, temp, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, oven.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceOven026Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    //获取运行模式
    private short getModel() {
        short model = 0;
        if ("快热".equals(modeWheel.getSelectedText())) {
            model = 1;
        } else if ("风焙烤".equals(modeWheel.getSelectedText())) {
            model = 2;
        } else if ("焙烤".equals(modeWheel.getSelectedText())) {
            model = 3;
        } else if ("底加热".equals(modeWheel.getSelectedText())) {
            model = 4;
        } else if ("风扇烤".equals(modeWheel.getSelectedText())) {
            model = 6;
        } else if ("烤烧".equals(modeWheel.getSelectedText())) {
            model = 7;
        } else if ("强烤烧".equals(modeWheel.getSelectedText())) {
            model = 8;
        }
        return model;
    }


}
