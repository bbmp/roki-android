package com.robam.roki.ui.page.device.steamovenone.steamovenone620;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;
import com.robam.roki.model.bean.CookbookGroup;
import com.robam.roki.model.bean.PagerCookItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter3.Rv920LocalRecipeTitleAdapter;
import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.device.steamovenone.steamovenone920.fragment.CustomRecipeFragment;
import com.robam.roki.ui.widget.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * CQ908 915
 * 本地自动菜谱
 */
public class LocalCookbook620Page extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.id_tab)
    PagerSlidingTabStrip idTab;
    @InjectView(R.id.id_view_pager)
    ViewPager mViewPager;

    @InjectView(R.id.title_recycle_view)
    RecyclerView titleRecycleView;

    String guid;
    String needDescalingParams;
    String dt;
    private List<DeviceConfigurationFunctions> localCookbookList;
    List<CookbookGroup> groups = new ArrayList<>();
    private List<PagerCook620Item> mTab = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_local_cookbook_new, container, false);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        localCookbookList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        dt = bd == null ? null : bd.getString(PageArgumentKey.dt);
        ButterKnife.inject(this, view);
        initData();

        return view;
    }

    private void initData() {


            tvTitle.setText(title);
            for (int i = 0; i < localCookbookList.size(); i++) {
                if (!"ckno".equals(localCookbookList.get(i).functionCode)) {
                    CookbookGroup cookbookGroup = new CookbookGroup();
                    cookbookGroup.setId(localCookbookList.get(i).id);
                    cookbookGroup.setFunctionCode(localCookbookList.get(i).functionCode);
                    cookbookGroup.setFunctionName(localCookbookList.get(i).subView.title);
                    cookbookGroup.setDeviceCategory(localCookbookList.get(i).deviceCategory);
                    cookbookGroup.setDeviceType(localCookbookList.get(i).deviceType);

                    List<DeviceConfigurationFunctions> functions = localCookbookList.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;

                    List<CookBookTag> cookBookTags = new ArrayList<>();
                    for (int j = 0; j < functions.size(); j++) {
                        CookBookTag cookBookbean = new CookBookTag();
                        DeviceConfigurationFunctions deviceConfigurationFunctions = functions.get(j);
                        cookBookbean.setId(deviceConfigurationFunctions.id);
                        cookBookbean.setFunctionCode(deviceConfigurationFunctions.functionCode);
                        cookBookbean.setFunctionName(deviceConfigurationFunctions.functionName);
                        cookBookbean.setDeviceCategory(deviceConfigurationFunctions.deviceCategory);
                        cookBookbean.setDeviceType(deviceConfigurationFunctions.deviceType);
                        cookBookbean.setBackgroundImg(deviceConfigurationFunctions.backgroundImg);
                        cookBookbean.setBackgroundImghH(deviceConfigurationFunctions.backgroundImgH);
                        cookBookbean.setFunctionParams(deviceConfigurationFunctions.functionParams);
                        cookBookTags.add(cookBookbean);
                    }
                    cookbookGroup.setCookBookTagList(cookBookTags);
                    groups.add(cookbookGroup);

                }
            }

        ArrayList<String> titles=new ArrayList<>();
        for (int i=0;i<10;++i){
            titles.add(i+"");
        }
        titleRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        Rv920LocalRecipeTitleAdapter mRv920LocalRecipeTitleAdapter= new Rv920LocalRecipeTitleAdapter();
        titleRecycleView.setAdapter(mRv920LocalRecipeTitleAdapter);


        mRv920LocalRecipeTitleAdapter.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {

            }
        });

        mRv920LocalRecipeTitleAdapter.addData(titles);
            for (int i = 0; i < groups.size(); i++) {
                mTab.add(new PagerCook620Item(groups.get(i).getFunctionName()));
            }

            mTab.add(new PagerCook620Item("自定义"));
            groups.add(new CookbookGroup());
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            mViewPager.setAdapter(adapter);
            idTab.setMargins(true);
            idTab.setViewPager(mViewPager);
            adapter.notifyDataSetChanged();



    }

    public class ViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TabItemName {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            if (groups.size() > 0) {
                 if (groups.size()-1==position){
                     return CustomRecipeFragment.newInstance(guid);
                 }else {
                     if (mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid, needDescalingParams) == null) {
                         return mTab.get(position).createFragment(null, "", "", dt);
                     }
                     return mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid, needDescalingParams, dt);

                 }
            } else {
                return CustomRecipeFragment.newInstance(guid);
//                return mTab.get(position).createFragment(null, "","" , dt);
            }
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

    @OnClick({R.id.iv_back})
    public void OnClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }




}
