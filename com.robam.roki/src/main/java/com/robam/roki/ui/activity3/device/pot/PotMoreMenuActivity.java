package com.robam.roki.ui.activity3.device.pot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.legent.plat.io.cloud.Reponses;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.dialog.type.Dialog_Type_PotMenu;
import com.robam.roki.ui.page.device.pot.PagerPotMenuItem;
import com.robam.roki.ui.page.device.pot.PotMyMenuMorePage;

import java.util.ArrayList;
import java.util.List;

import skin.support.content.res.SkinCompatResources;

public class PotMoreMenuActivity extends DeviceBaseFuntionActivity {

    ImageView iv_device_more;
    TextView tv_top_tips;
    ViewPager mViewPager;
    PagerSlidingTabStrip idTab;

    private List<PagerPotMenuItem> mTab = new ArrayList<>();
    Dialog_Type_PotMenu mDialog_Type_PotMenu;
    List<String> dataList = new ArrayList<>();
    Pot pot;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pot_more_menu;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_device_switch).setVisibility(View.INVISIBLE);
        iv_device_more = findViewById(R.id.iv_device_more);
        tv_top_tips = findViewById(R.id.tv_top_tips);
        iv_device_more.setImageDrawable(getResId(R.mipmap.icon_search_menu));
        mViewPager = findViewById(R.id.id_view_pager);
        idTab = findViewById(R.id.id_tab);

    }


    @Override
    protected void dealData() {

        setTitle("更多菜谱");
        pot = (Pot) bundle.getSerializable(PageArgumentKey.Bean);
        mTab.add(new PagerPotMenuItem("爆炒", pot));
        mTab.add(new PagerPotMenuItem("煲汤", pot));
        mTab.add(new PagerPotMenuItem("炖煮", pot));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        idTab.setViewPager(mViewPager);
        adapter.notifyDataSetChanged();


        dataList.add("红薯");
        dataList.add("红烧肉");
        dataList.add("红烧鱼");
        dataList.add("清蒸大闸蟹");
        dataList.add("糖醋排骨");

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TabItemName {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return mTab.get(position).createFragment1();
        }


        @Override
        public int getCount() {
            return mTab.size();
        }

        @Override
        public String getTabName(int position) {
            return mTab.get(position).getmTitle();
        }

    }

    private Drawable getResId(int resId) {
        return SkinCompatResources.getDrawable(getContext(), resId);
    }

    private int getColorSkin(int resId) {
        return SkinCompatResources.getColor(getContext(), resId);
    }
}