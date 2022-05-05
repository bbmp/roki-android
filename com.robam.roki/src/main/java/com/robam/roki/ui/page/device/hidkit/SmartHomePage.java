package com.robam.roki.ui.page.device.hidkit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.SubView;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.roki.R;
import com.robam.roki.model.bean.FunctionSmartHomeParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.recipeclassify.PagerSmartItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/20.
 * @PS:藏宝盒智能家居
 */
public class SmartHomePage extends BasePage {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.iv_more)
    ImageView ivMore;
    @InjectView(R.id.iv_smart_bg)
    ImageView ivSmartBg;
    TextView tvRecipeName;
    @InjectView(R.id.id_tab)
    PagerSlidingTabStrip idTab;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;
    private List<PagerSmartItem> mTab = new ArrayList<PagerSmartItem>();
    String mGuid;
    protected List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    ArrayList<FunctionSmartHomeParams.DeviceInfoBean> devList = new ArrayList();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDeviceConfigurationFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_smart_home, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        LogUtils.i("20201021", "mDeviceConfigurationFunctions:" + mDeviceConfigurationFunctions);
        if (null != mDeviceConfigurationFunctions && 0 < mDeviceConfigurationFunctions.size()) {

            DeviceConfigurationFunctions functions = mDeviceConfigurationFunctions.get(0);
            SubView subView = functions.subView;
            String viewBackgroundImg = subView.viewBackgroundImg;
            String title = subView.title;
            tvName.setText(title);
            Glide.with(getContext()).load(viewBackgroundImg).into(ivSmartBg);
            List<DeviceConfigurationFunctions> configurationFunctions = subView.subViewModelMap
                    .subViewModelMapSubView.deviceConfigurationFunctions;
            LogUtils.i("20201021", "configurationFunctions:" + configurationFunctions);
            if (null != configurationFunctions && 0 < configurationFunctions.size()) {
                String jsonParams = configurationFunctions.get(0).functionParams;
                LogUtils.i("20201021", "jsonParams:" + jsonParams);
                //{"RYYJ":{"name":"油烟机","dt":["8236S"],"canSay":["老板老板,打开烟机"]"cookbooks":["烤牛排"，"烤披萨"]}}
                try {

                    FunctionSmartHomeParams functionSmartHomeParams =
                            JsonUtils.json2Pojo(jsonParams, FunctionSmartHomeParams.class);
                    List<FunctionSmartHomeParams.DeviceInfoBean> deviceInfo = functionSmartHomeParams.getDeviceInfo();

                    for (int i = 0; i < deviceInfo.size(); i++) {
                        devList.add(deviceInfo.get(i));
                        String name = deviceInfo.get(i).getName();
                        PagerSmartItem pagerSmartItem = new PagerSmartItem(name);
                        mTab.add(pagerSmartItem);
                        LogUtils.i("20201021", "name:" + name);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
                viewPager.setAdapter(viewPagerAdapter);
                idTab.setViewPager(viewPager);
                viewPagerAdapter.notifyDataSetChanged();
            }

        }
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TabItemName {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (devList.size() > 0) {
                if (null == mTab.get(position).createFragment(devList.get(position).getName(), devList)) {
                    return mTab.get(position).createFragment(null, null);
                }
                return mTab.get(position).createFragment(devList.get(position).getName(), devList);
            } else {
                return mTab.get(position).createFragment(null, null);
            }
        }

        @Override
        public int getCount() {
            return mTab.size();
        }

        @Override
        public String getTabName(int position) {
            return mTab.get(position).getTitle();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_more)
    public void onIvMoreClicked() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PageArgumentKey.Guid, mGuid);
        AbsHidKit mHidKit = Plat.deviceService.lookupChild(mGuid);
//        bundle.putSerializable(PageArgumentKey.title, mTitle);
        UIService.getInstance().postPage(PageKey.HidKitDeviceMore, bundle);

    }
}
