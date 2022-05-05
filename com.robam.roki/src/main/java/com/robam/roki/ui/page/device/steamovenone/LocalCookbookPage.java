package com.robam.roki.ui.page.device.steamovenone;

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
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.CookbookGroup;
import com.robam.roki.model.bean.PagerCookItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.utils.DialogUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * CQ908 915
 * 本地自动菜谱
 */
public class LocalCookbookPage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.id_tab)
    PagerSlidingTabStrip idTab;
    @InjectView(R.id.id_view_pager)
    ViewPager mViewPager;

    String guid;
    String needDescalingParams;
    String dt;
    private List<DeviceConfigurationFunctions> localCookbookList;
    List<CookbookGroup> groups = new ArrayList<>();
    private List<PagerCookItem> mTab = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_local_cookbook, container, false);
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
        try {

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


            for (int i = 0; i < groups.size(); i++) {
                mTab.add(new PagerCookItem(groups.get(i).getFunctionName()));
            }
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            mViewPager.setAdapter(adapter);
            idTab.setViewPager(mViewPager);
            adapter.notifyDataSetChanged();

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
            if (groups.size() > 0) {
                if (mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid,needDescalingParams) == null) {
                    return mTab.get(position).createFragment(null, "","" ,dt);
                }
                return mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid,needDescalingParams ,dt);
            } else {
                return mTab.get(position).createFragment(null, "","" , dt);
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
