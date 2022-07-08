package com.robam.roki.ui.page.device.pot;

import androidx.fragment.app.Fragment;

import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.device.steamovenone.LocalCookContentFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/8/27.
 */

public class PagerPotMenuItem {

    private String mTitle;
    private Pot pot;
    public PagerPotMenuItem(String mTitle) {
        this.mTitle = mTitle;
    }
    public PagerPotMenuItem(String mTitle,Pot pot) {
        this.mTitle = mTitle;
        this.pot = pot;
    }
    public String getmTitle() {
        return mTitle;
    }

    public Fragment createFragment() {
        return PotMenuFragment.instance( mTitle);
    }
    public Fragment createFragment1() {
        return PotMenuFragment.instance( mTitle,pot);
    }
}
