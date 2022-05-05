package com.robam.roki.ui.page.device.water;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.WaterPurifiyStatusChangedEvent;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/10/29.
 */
public class WaterPurifierFilterCoreStatePage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.iv_filter_change)
    ImageView mIvFilterChange;
    @InjectView(R.id.pager)
    ViewPager mPager;
    @InjectView(R.id.indicator)
    CirclePageIndicator mIndicator;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    AbsWaterPurifier mWaterPurifier;
    List<DeviceConfigurationFunctions> mList;
    private int mPercent_pp;
    private int mPercent_cto;
    private int mPercent_ro1;
    private int mPercent_ro2;
    private JSONArray mArray;
    private DeviceConfigurationFunctions mReplaceFilterCoreList;
    private Map<Short, String> mMap;
    private List<Short> mPurifierStatusList;
    private MyAdapter mAdapter;
    private List<Fragment> mFragments;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mWaterPurifier = (AbsWaterPurifier) bundle.getSerializable(PageArgumentKey.Bean);
            mList = (List<DeviceConfigurationFunctions>) bundle.getSerializable(PageArgumentKey.List);
        }
    }

    @Subscribe
    public void onEvent(WaterPurifiyStatusChangedEvent event) {//轮训查询数据
        if (mWaterPurifier == null || !Objects.equal(mWaterPurifier.getID(), event.pojo.getID()))
            return;
        mWaterPurifier = event.pojo;
        initFilterData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_filter_core_state, container, false);
        ButterKnife.inject(this, view);
        initListener();
        initFilterData();
        initAdapter();
        return view;
    }

    private void initListener() {

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                initAdapterData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initFilterData() {
        mMap = new HashMap<>();
        mPurifierStatusList = new ArrayList<>();
        short filter_state_pp = mWaterPurifier.filter_state_pp;
        short filter_state_cto = mWaterPurifier.filter_state_cto;
        short filter_state_ro1 = mWaterPurifier.filter_state_ro1;
        short filter_state_ro2 = mWaterPurifier.filter_state_ro2;

        LogUtils.i("cg", "filter_state_pp:" + filter_state_pp);

        if (filter_state_pp == 255) {
            filter_state_pp = 0;
        }
        if (filter_state_cto == 255) {
            filter_state_cto = 0;
        }
        if (filter_state_ro1 == 255) {
            filter_state_ro1 = 0;
        }
        if (filter_state_ro2 == 255) {
            filter_state_ro2 = 0;
        }
        mMap.put(filter_state_ro2, "ro2");
        mMap.put(filter_state_ro1, "ro1");
        mMap.put(filter_state_cto, "cto");
        mMap.put(filter_state_pp, "pp");
        mPurifierStatusList.add(filter_state_ro2);
        mPurifierStatusList.add(filter_state_ro1);
        mPurifierStatusList.add(filter_state_cto);
        mPurifierStatusList.add(filter_state_pp);
        mPercent_pp = getPercent(filter_state_pp);
        mPercent_cto = getPercent(filter_state_cto);
        mPercent_ro1 = getPercent(filter_state_ro1);
        mPercent_ro2 = getPercent(filter_state_ro2);
        LogUtils.i("20190305", "mPercent_ro2:" + mPercent_ro2);
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                String functionCode = mList.get(i).functionCode;
                if ("filterCoreState".equals(functionCode)) {
                    String title = mList.get(i).subView.title;
                    mTvTitle.setText(title);
                    List<DeviceConfigurationFunctions> subList = mList.get(i).subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                    if (subList != null && subList.size() > 0) {
                        for (int j = 0; j < subList.size(); j++) {
                            String subCode = subList.get(j).functionCode;
                            if ("filterCoreState".equals(subCode)) {
                                String filterCoreStateParams = subList.get(j).functionParams;
                                try {
                                    mArray = new JSONArray(filterCoreStateParams);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if ("ReplaceFilterCore".equals(subCode)) {
                                mReplaceFilterCoreList = subList.get(j);
                            }
                        }
                    }
                }
            }
        }
    }

    private void initAdapter() {
        initAdapterData(-1);
        selectedPage();
    }

    private void initAdapterData(int position) {
        if (position == -1) {
            mFragments = buildFragments();
            mAdapter = new MyAdapter(getChildFragmentManager(), mFragments);
            mPager.setAdapter(mAdapter);
            mIndicator.setViewPager(mPager);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 选择要进入的页面
     */
    private void selectedPage() {
        if (mPurifierStatusList != null && mPurifierStatusList.size() > 0) {
            Short min = Collections.min(mPurifierStatusList);
            String state = mMap.get(min);
            LogUtils.i("20181128", "min:" + min + "state:" + state);
            switch (state) {
                case "pp":
                    mPager.setCurrentItem(0);
                    break;
                case "cto":
                    mPager.setCurrentItem(1);
                    break;
                case "ro1":
                    mPager.setCurrentItem(2);
                    break;
                case "ro2":
                    mPager.setCurrentItem(3);
                    break;
                default:
                    break;
            }
        }
    }

    private List<Fragment> buildFragments() {
        List<Fragment> fragments = new ArrayList<>();
        FilterStateOnePage onePage = new FilterStateOnePage(mWaterPurifier, mArray);
        FilterStateTwoPage twoPage = new FilterStateTwoPage(mWaterPurifier, mArray);
        FilterStateThreePage threePage = new FilterStateThreePage(mWaterPurifier, mArray);
        FilterStateForePage forePage = new FilterStateForePage(mWaterPurifier, mArray);
        fragments.add(onePage);
        fragments.add(twoPage);
        fragments.add(threePage);
        fragments.add(forePage);
        return fragments;
    }

    private int getPercent(short num) {
        if (num <= 0) {
            return 0;
        } else if (num > 100) {
            return 0;
        }
        return num;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_filter_change)
    public void onMIvFilterChangeClicked() {

        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.List, mReplaceFilterCoreList);
        UIService.getInstance().postPage(PageKey.WaterPurifierFilterReplacement, bd);
    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        // start
        // 可以删除这段代码看看，数据源更新而viewpager不更新的情况
        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                // 这里利用判断执行若干次不缓存，刷新
                mChildCount--;
                // 返回这个是强制ViewPager不缓存，每次滑动都刷新视图
                return POSITION_NONE;
            }
            // 这个则是缓存不刷新视图
            return super.getItemPosition(object);
        }

        public void loadFragments(List<Fragment> fragments) {
            if (mFragments != null) {
                mFragments.clear();
            }
            notifyDataSetChanged();
            if (fragments != null && fragments.size() > 0) {
                mFragments.addAll(fragments);
                notifyDataSetChanged();
            }
        }
    }

}
