package com.robam.roki.model.device.rika;

import androidx.fragment.app.Fragment;

import com.robam.roki.ui.page.device.rika.AbsLocalRikaRecipePage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/8/27.
 */

public class PagerCookItem {

    private String mTitle;

    public PagerCookItem(String mTitle) {
        this.mTitle = mTitle;
    }

    public Fragment createFragment(ArrayList<CookBookTag> tags, String guid) {
        return AbsLocalRikaRecipePage.instance(tags,guid);
    }

    public String getmTitle() {
        return mTitle;
    }
}
