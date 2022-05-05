package com.robam.roki.ui.view.recipeclassify;

import androidx.fragment.app.Fragment;

import com.robam.roki.model.bean.FunctionSmartHomeParams;
import com.robam.roki.ui.view.HidKitDeviceContentFragment;

import java.util.ArrayList;

/**
 * Created by lixin on 2020/10/21.
 */
public class PagerSmartItem {
    /*item的 title*/
    private String mTitle ;

    public PagerSmartItem(String mTitle) {
        this.mTitle = mTitle;
    }

    public Fragment createFragment(String tags, ArrayList<FunctionSmartHomeParams.DeviceInfoBean> devInfos){

        return HidKitDeviceContentFragment.instance(tags,devInfos);
    }

    public String getTitle() {
        return mTitle;
    }
}
