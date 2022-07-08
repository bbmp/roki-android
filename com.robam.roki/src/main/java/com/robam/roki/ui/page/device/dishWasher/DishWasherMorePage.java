package com.robam.roki.ui.page.device.dishWasher;

import android.os.Bundle;
import android.view.View;

import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.AbsDeviceMorePage;

public class DishWasherMorePage extends AbsDeviceMorePage {
    @Override
    public void subItemOnClick(View v, int position) {
        switch (position) {
            //留言咨询
            case 1:
                UIService.getInstance().postPage(PageKey.Chat);
                break;
            //一键售后
            case 2:
                callAfterSale();
                break;
            case 3:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                bd.putString(PageArgumentKey.Url, mUrl);
                bd.putString(PageArgumentKey.title, title);
                UIService.getInstance().postPage(PageKey.QuickGuide, bd);
                break;
            //产品信息
            case 4:
                Bundle bd2 = new Bundle();
                bd2.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceInformation, bd2);
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
        deviceMoreBean2.setName(title);
        deviceMoreBean2.setImageRes(R.mipmap.m_kszl);
        deviceMoreBean2.setType(1);

        DeviceMoreBean deviceMoreBean3 = new DeviceMoreBean();
        deviceMoreBean3.setName("产品信息");
        deviceMoreBean3.setImageRes(R.mipmap.img_product_information);
        deviceMoreBean3.setType(1);

        mDatas.add(deviceMoreBean);
        mDatas.add(deviceMoreBean1);
        mDatas.add(deviceMoreBean2);
        mDatas.add(deviceMoreBean3);

    }
}
