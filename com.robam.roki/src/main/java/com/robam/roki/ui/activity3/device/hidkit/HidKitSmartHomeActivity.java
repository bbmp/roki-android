package com.robam.roki.ui.activity3.device.hidkit;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.base.FragmentPagerAdapter;
import com.robam.roki.R;
import com.robam.roki.model.bean.FunctionSmartHomeParams;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.adapter.TabAdapter;
import com.robam.roki.ui.activity3.device.hidkit.fragment.HidKitSmartHomeFragment;
import com.robam.roki.ui.view.recipeclassify.PagerSmartItem;

import java.util.List;

/**
 * author : huxw
 * time   : 2022/07/06
 * desc   : 智能家居
 */
public final class HidKitSmartHomeActivity extends DeviceBaseFuntionActivity implements TabAdapter.OnTabListener, ViewPager.OnPageChangeListener {


    private RecyclerView rvHomeTab;
    private androidx.viewpager.widget.ViewPager viewPager;
    private TabAdapter mTabAdapter;
    /**
     * pageAdapter
     */
    private FragmentPagerAdapter<Fragment> fragmentFragmentPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hidkit_smart_home;
    }

    @Override
    protected void initView() {


        rvHomeTab = findViewById(R.id.rv_home_tab);
        viewPager = findViewById(R.id.view_pager);
        mTabAdapter = new TabAdapter(this);
        rvHomeTab.setAdapter(mTabAdapter);
        fragmentFragmentPagerAdapter = new FragmentPagerAdapter<>(this);

        viewPager.addOnPageChangeListener(this);
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        //设置title
        setTitle(deviceConfigurationFunction.functionName);
        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = deviceConfigurationFunction.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
        String functionParams = deviceConfigurationFunctions.get(0).functionParams;
        try {

            FunctionSmartHomeParams functionSmartHomeParams =
                    JsonUtils.json2Pojo(functionParams, FunctionSmartHomeParams.class);
            List<FunctionSmartHomeParams.DeviceInfoBean> deviceInfo = functionSmartHomeParams.getDeviceInfo();
            for (FunctionSmartHomeParams.DeviceInfoBean deviceInfoBean : deviceInfo) {
                mTabAdapter.addItem(deviceInfoBean.getName());
                fragmentFragmentPagerAdapter.addFragment(HidKitSmartHomeFragment.instance(deviceInfoBean));
            }
            mTabAdapter.setOnTabListener(this);
            viewPager.setAdapter(fragmentFragmentPagerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTabSelected(RecyclerView recyclerView, int position) {
        viewPager.setCurrentItem(position);
        return true;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mTabAdapter == null) {
            return;
        }
        mTabAdapter.setSelectedPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}