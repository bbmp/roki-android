package com.robam.roki.ui.activity3.device.base;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.RecipeCategoryAdapter;
import com.robam.roki.ui.adapter3.CustomLoadMoreView;
import com.robam.roki.ui.page.RecipeDetailPage;

import java.util.ArrayList;
import java.util.List;

import skin.support.content.res.SkinCompatResources;

public class DevicePMenuActivity extends DeviceBaseFuntionActivity {

    ImageView iv_device_more;
    private RecyclerView rvPMunu;
    private long mUserId;
    protected String title;
    String type;

    int start;
    int num = 10;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecipeCategoryAdapter mRecipeCategoryAdapter;
    private final int UPDATE_DATA = 1;
    String recipeType;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();


    String platformCode;
    String mGuid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_pmenu;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_device_switch).setVisibility(View.INVISIBLE);
        iv_device_more = findViewById(R.id.iv_device_more);
        iv_device_more.setImageDrawable(getResId(R.mipmap.icon_search_menu));
        rvPMunu = findViewById(R.id.rv_p_munu);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
    }

    @Override
    protected void dealData() {
//        setTitle(mDevice.getDt());

        if (bundle != null) {
            type = bundle.getString(PageArgumentKey.RecipeId);
            platformCode = bundle.getString(PageArgumentKey.platformCode);
            mGuid = bundle.getString(PageArgumentKey.Guid);
        }
        mUserId = Plat.accountService.getCurrentUserId();
        //灶具菜谱
        if (DeviceType.RRQZ.equals(type)) {
            title = getActivity().getString(R.string.home_stove_recipe_text);
        } else if (DeviceType.RDKX.equals(type)) {
            title = getActivity().getString(R.string.home_oven_recipe_text);
        } else if (DeviceType.RZQL.equals(type)) {
            title = getActivity().getString(R.string.home_steam_recipe_text);
        } else if (DeviceType.RWBL.equals(type)) {
            title = getActivity().getString(R.string.home_microwave_recipe_text);
        } else if (DeviceType.RZNG.equals(type)) {
            title = getActivity().getString(R.string.home_pot_recipe_text);
        } else if (DeviceType.RZKY.equals(type)) {
            title = getActivity().getString(R.string.steamOvenOneRecipe);
        }
        setTitle(title);
        initAdapter();//初始化适配器
        requestData();//初始化数据
        initListener();//监听事件和配置
    }
    private void initAdapter() {
        mRecipeCategoryAdapter = new RecipeCategoryAdapter();
        mRecipeCategoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                Recipe recipe = mRecipeCategoryAdapter.getItem(i);
                RecipeDetailPage.show(recipe, recipe.id, RecipeDetailPage.DeviceRecipePage,
                        RecipeRequestIdentification.RECIPE_SORTED,platformCode,mGuid);
            }
        });


        rvPMunu.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPMunu.setAdapter(mRecipeCategoryAdapter);
    }

    private void requestData() {
        LogUtils.i("2020060901","mUserId:::"+mUserId);
        LogUtils.i("2020060901","type:::"+type);
        LogUtils.i("2020060901","recipeType:::"+recipeType);
        LogUtils.i("2020060901","start:::"+start);
        LogUtils.i("2020060901","num:::"+num);
        LogUtils.i("2020060901","platformCode:::"+platformCode);
        CookbookManager.getInstance().getGroundingRecipesByDc(mUserId, type, recipeType, start, num, platformCode, new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> list) {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    LogUtils.i("2020060802","list::"+list);

                    if (list == null || list.size() <= 0) {
                        list = Lists.newArrayList();
                        if (start > 0) {
//                            rvPMunu.setNoMore(true);
                            mRecipeCategoryAdapter.getLoadMoreModule().loadMoreEnd();
                        }
                    }
                    if (start == 0)
                        mRecipeList.clear();
                    mRecipeList.addAll(list);
                    mRecipeCategoryAdapter.setList(mRecipeList);
                    mRecipeCategoryAdapter.getLoadMoreModule().loadMoreComplete();

//                    if(mRecipeList != null && mRecipeList.size() != 0 && mRecipeCategoryAdapter != null){
//                        mRecipeCategoryAdapter.updateData(mRecipeList);
//                        if (rvPMunu != null) {
//                            //更新完成
//                            rvPMunu.refreshComplete();
//                            //加载完成
//                            rvPMunu.loadMoreComplete();
//                        }
//                    }
                } catch (Exception e) {
                    Log.e("RecipeCategory", "error:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                mRecipeCategoryAdapter.getLoadMoreModule().loadMoreFail();
            }
        });

    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                start = 0 ;
                //请求数据
                requestData();
            }
        });
        mRecipeCategoryAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        mRecipeCategoryAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LogUtils.i("20181114", " onLoadMore");
                start += 10 ;
//                num += 10;
                requestData();
            }
        });

    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        mUserId =  Plat.accountService.getCurrentUserId();
        requestData();
    }

    /**
     * 模拟从菜谱详情页返回
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeDetailPage".equals(event.getPageName())){
//            mRecipeList.clear();
            start = 0 ;
            requestData();
        }
    }


    private Drawable getResId(int resId) {
        return SkinCompatResources.getDrawable(getContext(), resId);
    }

    private int getColorSkin(int resId) {
        return SkinCompatResources.getColor(getContext(), resId);
    }
}