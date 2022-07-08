package com.robam.roki.ui.view.recipeclassify;

import androidx.fragment.app.Fragment;

import com.robam.common.pojos.Tag;

import java.util.ArrayList;

/**
 * Created by moon.zhong on 2015/3/9.
 */
public class PagerItem {
    /*item的 title*/
    private String mTitle ;

    public PagerItem(String mTitle) {
        this.mTitle = mTitle;
    }

    public Fragment createFragment(ArrayList<Tag> tags){
        return RecipeClassifyContentFragment.instance(tags);
    }

    public String getTitle() {
        return mTitle;
    }
}
