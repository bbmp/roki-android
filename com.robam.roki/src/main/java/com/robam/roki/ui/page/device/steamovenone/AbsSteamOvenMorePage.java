package com.robam.roki.ui.page.device.steamovenone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.legent.ui.UIService;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.AbsDeviceMorePage;
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.RecipeListActivity;

/**
 * Created by Dell on 2018/7/6.
 */

public class AbsSteamOvenMorePage extends AbsDeviceMorePage {
    @Override
    public void subItemOnClick(View v, int position) {

        if (!mGuid.contains("920")) {
            position++;
        }
//            switch (position) {
//                case 1:
//                    UIService.getInstance().postPage(PageKey.Chat);
//                    break;
//                case 2:
//                    callAfterSale();
//                    break;
//                case 3:
//                    Bundle bd = new Bundle();
//                    bd.putString(PageArgumentKey.Guid, mGuid);
//                    UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
//                    break;
//            }
            switch (position) {
                case 1:
                    Intent intent = new Intent(getContext(), RecipeListActivity.class);
                    intent.putExtra(PageArgumentKey.Guid, mGuid);
                    startActivity(intent);
                    break;
                case 2:
                    UIService.getInstance().postPage(PageKey.Chat);
                    break;
                case 3:
                    callAfterSale();
                    break;
                case 4:
                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.Guid, mGuid);
                    UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
                    break;
            }

        }
    @Override
    public void subInitData() {
        if (mGuid.contains("920")) {
            DeviceMoreBean RecipeList = new DeviceMoreBean();
            RecipeList.setName("烹饪记录");
            RecipeList.setImageRes(R.mipmap.recipe_record);
            RecipeList.setType(1);
            mDatas.add(RecipeList);
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
    }
}
