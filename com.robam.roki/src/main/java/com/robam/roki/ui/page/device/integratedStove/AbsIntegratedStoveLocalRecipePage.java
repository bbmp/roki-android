package com.robam.roki.ui.page.device.integratedStove;

import android.annotation.SuppressLint;
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
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.model.device.rika.CookBookTag;
import com.robam.roki.model.device.rika.CookbookGroup;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 集成灶本地自动菜谱
 * @author r210190
 */
public class AbsIntegratedStoveLocalRecipePage extends BasePage {


    @InjectView(R.id.home_recipe_live_title)
    TextView home_recipe_live_title;

    @InjectView(R.id.home_recipe_live_imgv_return)
    ImageView home_recipe_live_imgv_return;
    /*viewpager*/
    private ViewPager mViewPager;
    /*自定义的 tabLayout*/
    private PagerSlidingTabStrip mTabLayout;


    public String guid;
    private List<DeviceConfigurationFunctions> mDatas;
    private List<DeviceConfigurationFunctions> localCookbook;
    private AbsIntegratedStove integratedStove;
    private String mViewBackgroundImg;

    List<CookbookGroup> groups = new ArrayList<>();
    private List<PagerCookItem> mTab = new ArrayList<PagerCookItem>();
    private JSONObject titleJson;


    /**
     * @param event
     */
    @Subscribe
    public void onEvent(IntegStoveStatusChangedEvent event) {
        if (integratedStove == null || !Objects.equal(integratedStove.getID(), event.pojo.getID())) {
            return;
        }
        integratedStove = event.pojo ;
        if (integratedStove.getID().equals(event.pojo.getID())){
            if(integratedStove.workState != IntegStoveStatus.workState_free && integratedStove.workState != IntegStoveStatus.workState_complete){
                UIService.getInstance().popBack();
                UIService.getInstance().postPage(PageKey.IntegratedStoveWorkPage, getArguments());
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        integratedStove = bd == null ? null : (AbsIntegratedStove) bd.getSerializable(PageArgumentKey.INTEGRATED_STOVE);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
        View view = inflater.inflate(R.layout.abs_rika_local_recipe, container, false);
        ButterKnife.inject(this, view);
        mViewPager = view.findViewById(R.id.id_view_pager);
        mTabLayout = view.findViewById(R.id.id_tab);
        initData();
        return view;
    }

    private void initData() {
        try {

            home_recipe_live_title.setText(title);
            for (int i = 0; i < mDatas.size(); i++) {
                if ("localCookbook".equals(mDatas.get(i).functionCode)) {
                    localCookbook = mDatas.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                }
            }
            for (int i = 0; i < localCookbook.size(); i++) {

                if (localCookbook.get(i).functionCode.equals("cookBookTop")) {
                    String functionParams = localCookbook.get(i).functionParams;
                    titleJson = new JSONObject(functionParams);
                }

                if (!localCookbook.get(i).functionCode.equals("cookBookTop")) {

                    CookbookGroup cookbookGroup = new CookbookGroup();
                    cookbookGroup.setId(localCookbook.get(i).id);
                    cookbookGroup.setFunctionCode(localCookbook.get(i).functionCode);
                    cookbookGroup.setFunctionName(localCookbook.get(i).subView.title);
                    cookbookGroup.setDeviceCategory(localCookbook.get(i).deviceCategory);
                    cookbookGroup.setDeviceType(localCookbook.get(i).deviceType);

                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions = localCookbook.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                    CookBookTag cookBookTag;
                    List<CookBookTag> cookBookTags = new ArrayList<>();
                    for (int j = 0; j < deviceConfigurationFunctions.size(); j++) {
                        cookBookTag = new CookBookTag();
                        DeviceConfigurationFunctions functions = deviceConfigurationFunctions.get(j);
                        cookBookTag.setId(functions.id);
                        cookBookTag.setFunctionCode(functions.functionCode);
                        cookBookTag.setFunctionName(functions.functionName);
                        cookBookTag.setDeviceCategory(functions.deviceCategory);
                        cookBookTag.setDeviceType(functions.deviceType);
                        cookBookTag.setBackgroundImg(functions.backgroundImg);
                        cookBookTag.setFunctionParams(functions.functionParams);
                        cookBookTags.add(cookBookTag);
                    }

                    cookbookGroup.setCookBookTagList(cookBookTags);
                    groups.add(cookbookGroup);
                }
            }
            for (int i = 0; i < groups.size(); i++) {
                LogUtils.i("20191206789", groups.get(i).getFunctionName());
                mTab.add(new PagerCookItem(groups.get(i).getFunctionName()));
            }
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            mViewPager.setAdapter(adapter);
            /*需要先为 viewpager 设置 adapter*/
            mTabLayout.setViewPager(mViewPager);


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
                if (mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid) == null) {
                    return mTab.get(position).createFragment(null, "");
                }
                return mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid);
            } else {
                return mTab.get(position).createFragment(null, "");
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


    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.home_recipe_live_imgv_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_recipe_live_imgv_return:
                UIService.getInstance().popBack();
                break;
            default:
                break;
        }
    }

}