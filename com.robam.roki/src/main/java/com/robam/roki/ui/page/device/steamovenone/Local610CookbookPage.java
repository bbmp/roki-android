package com.robam.roki.ui.page.device.steamovenone;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;
import com.robam.roki.model.bean.CookbookGroup;
import com.robam.roki.model.bean.PagerCookItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.Rv610LocalRecipeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.device.oven.CookBookTag;
import com.robam.roki.ui.page.login.MyBasePage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 610
 * 本地自动菜谱
 */
public class Local610CookbookPage extends MyBasePage<MainActivity> {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    String guid;
    String needDescalingParams;
    private List<DeviceConfigurationFunctions> localCookbookList;
    List<CookbookGroup> groups = new ArrayList<>();
    private RecyclerView rvCook;
    private Rv610LocalRecipeAdapter rv610LocalRecipeAdapter;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.page_local_cookbook_610, container, false);
//        Bundle bd = getArguments();
//        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
//        localCookbookList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
//        title = bd == null ? null : bd.getString(PageArgumentKey.title);
//        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
//        ButterKnife.inject(this, view);
//        initData();
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_local_cookbook_610;
    }

    @Override
    protected void initView() {
        rvCook = (RecyclerView) findViewById(R.id.rv_cook);
        rvCook.setLayoutManager(new GridLayoutManager(cx , 2 , RecyclerView.VERTICAL ,false));
        rv610LocalRecipeAdapter = new Rv610LocalRecipeAdapter();
        rvCook.setAdapter(rv610LocalRecipeAdapter);
        rv610LocalRecipeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull @NotNull View view, int i) {
                Bundle bundle = getArguments();
                bundle.putInt(Local610CookbookDetailPage.POSITION , i);
                UIService.getInstance().postPage(PageKey.Local610CookbookDetailPage, bundle);
            }
        });
    }
    @Override
    protected void initData() {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        localCookbookList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        try {
            if (localCookbookList != null && localCookbookList.size() != 0){
                rv610LocalRecipeAdapter.addData(localCookbookList);
            }
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
                        cookBookbean.setFunctionParams(deviceConfigurationFunctions.functionParams);
                        cookBookTags.add(cookBookbean);
                    }
                    cookbookGroup.setCookBookTagList(cookBookTags);
                    groups.add(cookbookGroup);

                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public class ViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.TabItemName {
//        public ViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//
//        @Override
//        public Fragment getItem(int position) {
//            if (groups.size() > 0) {
//                if (mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid,needDescalingParams) == null) {
//                    return mTab.get(position).createFragment(null, "","");
//                }
//                return mTab.get(position).createFragment((ArrayList<CookBookTag>) groups.get(position).getCookBookTagList(), guid,needDescalingParams);
//            } else {
//                return mTab.get(position).createFragment(null, "","");
//            }
//        }
//
//
//        @Override
//        public int getCount() {
//            return mTab.size();
//        }
//
//        @Override
//        public String getTabName(int position) {
//            return mTab.get(position).getmTitle();
//        }
//
//    }

    @OnClick({R.id.iv_back})
    public void OnClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }




}
