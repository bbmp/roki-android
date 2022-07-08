package com.robam.roki.ui.page.device.pot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.R;
import com.robam.roki.model.bean.CookbookGroup;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.Rv610LocalRecipe2Adapter;
import com.robam.roki.ui.adapter3.RvPotMenuChoiceAdapter;
import com.robam.roki.ui.dialog.type.Dialog_Type_PotMenu;
import com.robam.roki.ui.page.device.steamovenone.Local610CookbookDetailPage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 无人锅
 */

public class PotMyMenuMorePage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_failed_tips)
    TextView tv_failed_tips;
    @InjectView(R.id.iv_recipe_search)
    ImageView iv_recipe_search;
    @InjectView(R.id.id_tab)
    PagerSlidingTabStrip idTab;
    @InjectView(R.id.id_view_pager)
    ViewPager mViewPager;
    @InjectView(R.id.tv_fav_count)
    TextView tv_fav_count;
    @InjectView(R.id.tv_fav_check)
    TextView tv_fav_check;
    @InjectView(R.id.ll_page)
    LinearLayout ll_page;

    String guid;
    String needDescalingParams;
    String dt;
    Pot pot;
    private List<DeviceConfigurationFunctions> localCookbookList;
    List<CookbookGroup> groups = new ArrayList<>();
    private List<PagerPotMenuItem> mTab = new ArrayList<>();
    Dialog_Type_PotMenu mDialog_Type_PotMenu;
    List<String> dataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_pot_my_menu_more, container, false);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        if (bd != null) {
            pot = (Pot) bd.getSerializable(PageArgumentKey.Bean);
        }
//        localCookbookList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
//        title = bd == null ? null : bd.getString(PageArgumentKey.title);
//        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
//        dt = bd == null ? null : bd.getString(PageArgumentKey.dt);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        try {

            tvTitle.setText("更多菜谱");

            mTab.add(new PagerPotMenuItem("爆炒", pot));
            mTab.add(new PagerPotMenuItem("煲汤", pot));
            mTab.add(new PagerPotMenuItem("炖煮", pot));
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            mViewPager.setAdapter(adapter);
            idTab.setViewPager(mViewPager);
            adapter.notifyDataSetChanged();


            dataList.add("红薯");
            dataList.add("红烧肉");
            dataList.add("红烧鱼");
            dataList.add("清蒸大闸蟹");
            dataList.add("糖醋排骨");



        } catch (Exception e) {
            e.printStackTrace();
        }

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

    @OnClick({R.id.iv_back, R.id.iv_recipe_search, R.id.tv_fav_check})
    public void OnClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.iv_recipe_search:
                ToastUtils.show("搜索", Toast.LENGTH_SHORT);
                break;
            case R.id.tv_fav_check:
                ToastUtils.show("查看", Toast.LENGTH_SHORT);
                if(mDialog_Type_PotMenu!=null){
                    mDialog_Type_PotMenu.show();
                }else {
                    mDialog_Type_PotMenu = new Dialog_Type_PotMenu(cx);
                    mDialog_Type_PotMenu.setmData(dataList);
                    mDialog_Type_PotMenu.show();
                }
                break;
        }
    }

}
