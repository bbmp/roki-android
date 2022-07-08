package com.robam.roki.ui.page.device.fan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.AbsMoreAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.page.device.AbsDeviceMorePage;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/6/6.
 * 烟机更多页
 */

public class FanDeviceMorePage extends AbsDeviceMorePage {
    @Override
    public void subItemOnClick(View v, int position) {
        if ("8235S".equals(iDevice.getDt()) || "8236S".equals(iDevice.getDt())) {
            switch (position) {

                case 1:
                    UIService.getInstance().postPage(PageKey.Chat);
                    break;
                case 2:
                    callAfterSale();
                    break;
                case 3:
                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.Guid, mGuid);
                    UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
                    break;

                default:
                    break;
            }
        }else {
            switch (position) {

                case 1:
                    UIService.getInstance().postPage(PageKey.Chat);
                    break;
                case 2:
                    callAfterSale();
                    break;
                case 3:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PageArgumentKey.Url, mUrl);
                    bundle.putSerializable(PageArgumentKey.title, title);
                    bundle.putString(PageArgumentKey.Guid, mGuid);
                    UIService.getInstance().postPage(PageKey.DeviceFanOilDetail, bundle);
                    break;
                case 4:
                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.Guid, mGuid);
                    UIService.getInstance().postPage(PageKey.DeviceInformation, bd);
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public void subInitData() {
        if ("8235S".equals(iDevice.getDt()) || "8236S".equals(iDevice.getDt())) {
            DeviceMoreBean deviceMoreBean = new DeviceMoreBean();
            deviceMoreBean.setName(cx.getString(R.string.fan_dialog_setting_message_consulting));
            deviceMoreBean.setImageRes(R.mipmap.img_message_consulting);
            deviceMoreBean.setType(1);
            DeviceMoreBean deviceMoreBean1 = new DeviceMoreBean();
            deviceMoreBean1.setName(cx.getString(R.string.fan_dialog_setting_a_key_after_sale));
            deviceMoreBean1.setImageRes(R.mipmap.img_after_sales);
            deviceMoreBean1.setType(1);

            DeviceMoreBean deviceMoreBean3 = new DeviceMoreBean();
            deviceMoreBean3.setName(cx.getString(R.string.fan_dialog_setting_product_information));
            deviceMoreBean3.setImageRes(R.mipmap.img_product_information);
            deviceMoreBean3.setType(1);

            mDatas.add(deviceMoreBean);
            mDatas.add(deviceMoreBean1);
            mDatas.add(deviceMoreBean3);
        }else{
            DeviceMoreBean deviceMoreBean = new DeviceMoreBean();
            deviceMoreBean.setName(cx.getString(R.string.fan_dialog_setting_message_consulting));
            deviceMoreBean.setImageRes(R.mipmap.img_message_consulting);
            deviceMoreBean.setType(1);
            DeviceMoreBean deviceMoreBean1 = new DeviceMoreBean();
            deviceMoreBean1.setName(cx.getString(R.string.fan_dialog_setting_a_key_after_sale));
            deviceMoreBean1.setImageRes(R.mipmap.img_after_sales);
            deviceMoreBean1.setType(1);
            DeviceMoreBean deviceMoreBean2 = new DeviceMoreBean();
            deviceMoreBean2.setName(cx.getString(R.string.fan_oil_dismantling_title));
            deviceMoreBean2.setImageRes(R.mipmap.img_net_oil_removal);
            deviceMoreBean2.setType(1);

            DeviceMoreBean deviceMoreBean3 = new DeviceMoreBean();
            deviceMoreBean3.setName(cx.getString(R.string.fan_dialog_setting_product_information));
            deviceMoreBean3.setImageRes(R.mipmap.img_product_information);
            deviceMoreBean3.setType(1);

            mDatas.add(deviceMoreBean);
            mDatas.add(deviceMoreBean1);
            mDatas.add(deviceMoreBean2);
            mDatas.add(deviceMoreBean3);
        }


    }

}
