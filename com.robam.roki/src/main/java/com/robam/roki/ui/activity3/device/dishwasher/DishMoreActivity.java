package com.robam.roki.ui.activity3.device.dishwasher;

import android.content.Intent;

import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.DeviceMoreActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/30
 *     desc   : 洗碗机更多界面
 *     version: 1.0
 * </pre>
 */
public class DishMoreActivity extends DeviceMoreActivity {
    @Override
    protected List<String> initMoreData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("设备名称");
        data.add("设置免打扰");
        data.add("耗材通知");
        data.add("留言咨询");
        data.add("一键售后");
        data.add("产品信息");
        return data;
    }

    @Override
    protected void otherOnClick(String more) {
        toast(more);
        Intent intent = new Intent();
        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
        switch (more){
            case "设置免打扰":
                intent.setClass(this , DishDisturbActivity.class);
                startActivity(intent);
                break;
            case "耗材通知":
                intent.setClass(this , DishConsNotifiActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
