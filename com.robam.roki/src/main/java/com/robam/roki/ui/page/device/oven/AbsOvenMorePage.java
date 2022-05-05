package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import android.view.View;

import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.AbsDeviceMorePage;


public class AbsOvenMorePage extends AbsDeviceMorePage {
    @Override
    public void subItemOnClick(View v, int position) {
        switch (position) {
            //留言咨询
            case 0:
                UIService.getInstance().postPage(PageKey.Chat);
                break;
            //一件售后
            case 1:
                callAfterSale();
                break;
            //产品信息
            case 2:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
                break;
        }
    }

    @Override
    public void subInitData() {
        DeviceMoreBean deviceMoreBean = new DeviceMoreBean();
        deviceMoreBean.setName("留言咨询");
        deviceMoreBean.setImageRes(R.mipmap.img_message_consulting);
        deviceMoreBean.setType(1);

        DeviceMoreBean deviceMoreBean1 = new DeviceMoreBean();
        deviceMoreBean1.setName("一键售后");
        deviceMoreBean1.setImageRes(R.mipmap.img_after_sales);
        deviceMoreBean1.setType(1);

        DeviceMoreBean deviceMoreBean2 = new DeviceMoreBean();
        deviceMoreBean2.setName("产品信息");
        deviceMoreBean2.setImageRes(R.mipmap.img_product_information);
        deviceMoreBean2.setType(1);

        mDatas.add(deviceMoreBean);
        mDatas.add(deviceMoreBean1);
        mDatas.add(deviceMoreBean2);

    }


}
