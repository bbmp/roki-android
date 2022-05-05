package com.robam.roki.model.bean;

import androidx.fragment.app.Fragment;

import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.device.steamovenone.CookBookBean;
import com.robam.roki.ui.page.device.steamovenone.LocalCookContentFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/8/27.
 */

public class PagerCookItem {

    private String mTitle;


    public PagerCookItem(String mTitle) {
        this.mTitle = mTitle;
    }

    public Fragment createFragment(ArrayList<CookBookTag> tags, String guid, String needDescalingParams) {
        return LocalCookContentFragment.instance(tags,guid,needDescalingParams);
    }
    public Fragment createFragment(ArrayList<CookBookTag> tags, String guid, String needDescalingParams ,String dt) {
        return LocalCookContentFragment.instance(tags,guid,needDescalingParams ,dt , mTitle);
    }
    public String getmTitle() {
        return mTitle;
    }
}
