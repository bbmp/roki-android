package com.robam.roki.ui.activity3.device.fan;

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
public class FanMoreActivity extends DeviceMoreActivity {
    @Override
    protected List<String> initMoreData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("设备名称");
        data.add("留言咨询");
        data.add("一键售后");
        data.add("产品信息");
        return data;
    }



    @Override
    protected void otherOnClick(String more) {
        toast(more);
    }

}
