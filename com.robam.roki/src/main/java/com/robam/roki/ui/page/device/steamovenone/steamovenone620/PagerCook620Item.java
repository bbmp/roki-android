package com.robam.roki.ui.page.device.steamovenone.steamovenone620;

import androidx.fragment.app.Fragment;

import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.device.steamovenone.LocalCookContentFragment;

import java.util.ArrayList;

public class PagerCook620Item {
    private String mTitle;


    public PagerCook620Item(String mTitle) {
        this.mTitle = mTitle;
    }

    public Fragment createFragment(ArrayList<CookBookTag> tags, String guid, String needDescalingParams) {
        return LocalCookContent620Fragment.instance(tags,guid,needDescalingParams);
    }
    public Fragment createFragment(ArrayList<CookBookTag> tags, String guid, String needDescalingParams ,String dt) {
        return LocalCookContent620Fragment.instance(tags,guid,needDescalingParams ,dt , mTitle);
    }



    public String getmTitle() {
        return mTitle;
    }
}
