package com.robam.roki.ui.page.device.sterilizer;

import android.os.Bundle;
import android.view.View;

import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.AbsDeviceMorePage;

public class AbsSterilizerMorePage extends AbsDeviceMorePage {

    private String functionParams;
    private String functionName;

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
            case 3:
                Bundle bd2 = new Bundle();
                bd2.putString(PageArgumentKey.Url, functionParams);
                bd2.putString(PageArgumentKey.WebTitle, functionName);
                UIService.getInstance().postPage(PageKey.WebClient, bd2);
                break;
            default:
                break;
        }
    }

    @Override
    public void subInitData() {
        DeviceMoreBean deviceMoreBean3 = null;
        if (hideFunctions != null) {
            if (hideFunctions.size() != 0) {
                for (int i = 0; i < hideFunctions.size(); i++) {
                    functionParams = hideFunctions.get(i).functionParams;
                    functionName = hideFunctions.get(i).functionName;
                    String backgroundImg = hideFunctions.get(i).backgroundImg;
                    deviceMoreBean3 = new DeviceMoreBean();
                    deviceMoreBean3.setName(functionName);
                    deviceMoreBean3.setImageUrl(backgroundImg);
                    deviceMoreBean3.setType(2);

                }


            }
        }

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
        if (deviceMoreBean3 != null) {
            mDatas.add(deviceMoreBean3);
        }

    }
}
