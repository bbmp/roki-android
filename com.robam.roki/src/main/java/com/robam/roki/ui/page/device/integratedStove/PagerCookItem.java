package com.robam.roki.ui.page.device.integratedStove;

import androidx.fragment.app.Fragment;

import com.robam.roki.model.device.rika.CookBookTag;
import com.robam.roki.ui.page.device.rika.AbsLocalRikaRecipePage;

import java.util.ArrayList;

/**
 *
 */
public class PagerCookItem {

    private String mTitle;

    public PagerCookItem(String mTitle) {
        this.mTitle = mTitle;
    }

    public Fragment createFragment(ArrayList<CookBookTag> tags, String guid) {
        return LocalRecipePage.instance(tags,guid);
    }

    public String getmTitle() {
        return mTitle;
    }
}
