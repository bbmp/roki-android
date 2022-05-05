package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.robam.roki.R;
import com.robam.roki.model.NormalModeItemMsg;

import java.util.List;

/**
 * Created by linxiaobin on 2015/12/22.
 */
public class DeviceOvenNormalSettingWheelView extends FrameLayout {

    private List<Integer> list = Lists.newArrayList();
    private List<Integer> list1 = Lists.newArrayList();
    private DeviceNormalSettingTemWheel wv1;
    private DeviceNormalSettingTimeWheel wv2;
    private ImageView imgKind;
    private TextView txtKind;
    private String type;

    public DeviceOvenNormalSettingWheelView(Context cx, String index) {
        super(cx);
        init(cx, null, index);
//        wv2.setOnSelectListener(selectListener);
    }

    public DeviceOvenNormalSettingWheelView(Context context, AttributeSet attrs, String index) {
        super(context, attrs);
        init(context, attrs, index);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    private void init(Context cx, AttributeSet attrs, String s) {

        LayoutInflater.from(cx).inflate(R.layout.device_oven_normal_setting_two_wheel, this,
                true);

        wv1 = findViewById(R.id.wv1);
        wv2 = findViewById(R.id.wv2);
        imgKind = findViewById(R.id.imgKind);
        txtKind = findViewById(R.id.txtKind);
        type = s;
        list = (List<Integer>) getList2(type);
        list1 = (List<Integer>)getList3(type);
        wv1.setData(list);
        wv2.setData(list1);
        if (type.equals("鸡翅")) {
            wv2.setDefault(2);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_chicken_wing_unworking));
        } else if (type.equals("蛋糕")) {
            wv2.setDefault(2);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_cake_unworking));
        } else if (type.equals("面包")) {
            wv2.setDefault(3);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_bread_unworking));
        } else if (type.equals("五花肉")) {
            wv2.setDefault(0);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_streaky_pork_unworking));
        } else if (type.equals("牛排")) {
            wv2.setDefault(2);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_steak_unworking));
        } else if (type.equals("披萨")) {
            wv2.setDefault(4);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_pisa_unworking));
        } else if (type.equals("海鲜")) {
            wv2.setDefault(3);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_seafood_unworking));
        } else if (type.equals("饼干")) {
            wv2.setDefault(4);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_cookie_unworking));
        } else if (type.equals("蔬菜")) {
            wv2.setDefault(0);
            imgKind.setImageDrawable(getResources().getDrawable(R.mipmap.ic_device_oven_vegetable_unworking));
        }
//        wv1.setDefault(1);
        txtKind.setText("烤" + s);


    }

    public NormalModeItemMsg getSelected() {
        NormalModeItemMsg msg = new NormalModeItemMsg();
        msg.setTemperature(wv1.getSelectedText());
        msg.setTime(wv2.getSelectedText());
        msg.setType(type);
        return msg;
    }
    protected List<?> getList2(String s) {
        List<Integer> list = Lists.newArrayList();
        if (s.equals("鸡翅")) {
            list.add(180);
        } else if (s.equals("蛋糕")) {
            list.add(160);
        } else if (s.equals("面包"))
            list.add(165);
        else if (s.equals("五花肉"))
            list.add(215);
        else if (s.equals("牛排"))
            list.add(180);
        else if (s.equals("披萨"))
            list.add(200);
        else if (s.equals("蔬菜"))
            list.add(200);
        else if (s.equals("海鲜"))
            list.add(200);
        else if (s.equals("饼干"))
            list.add(170);
        return list;
    }

    protected List<?> getList3(String type) {
        List<Integer> list1 = Lists.newArrayList();
        if (type.equals("鸡翅")) {
            for (int i = 14; i <= 23; i++) {
                list1.add(i);
            }
        } else if (type.equals("蛋糕")) {
            for (int i = 23; i <= 28; i++) {
                list1.add(i);
            }
        } else if (type.equals("面包")) {
            for (int i = 15; i <= 22; i++) {
                list1.add(i);
            }
        } else if (type.equals("五花肉")) {
            for (int i = 45; i <= 50; i++) {
                list1.add(i);
            }
        } else if (type.equals("牛排")) {
            for (int i = 13; i <= 20; i++) {
                list1.add(i);
            }
        } else if (type.equals("披萨")) {
            for (int i = 16; i <= 25; i++) {
                list1.add(i);
            }
        } else if (type.equals("海鲜")) {
            for (int i = 20; i <= 25; i++) {
                list1.add(i);
            }
        } else if (type.equals("饼干")) {
            for (int i = 12; i <= 20; i++) {
                list1.add(i);
            }
        } else if (type.equals("蔬菜")) {
            for (int i = 15; i <= 30; i++) {
                list1.add(i);
            }
        }
        return list1;
    }
}

