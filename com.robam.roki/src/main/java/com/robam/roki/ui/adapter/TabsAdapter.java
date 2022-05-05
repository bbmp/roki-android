package com.robam.roki.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.TagValue;
import com.robam.roki.ui.page.TagRecipeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wwq
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    private List<TagValue.TagsTagBean> tagsTagBeanList;


    public TabsAdapter(FragmentManager fm, List<TagValue.TagsTagBean> tagsTagBeanList) {
        super(fm);
        this.tagsTagBeanList = tagsTagBeanList;
    }

    @Override
    public int getCount() {
        return tagsTagBeanList.size() > 5 ? 5 : tagsTagBeanList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return TagRecipeFragment.newInstance(tagsTagBeanList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tagsTagBeanList.get(position).getName();
    }

}

