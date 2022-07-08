package com.robam.roki.ui.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.PlatApp;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.utils.StatusBarCompat;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.common.util.RecipeUtils;
import com.robam.common.util.StatusBar2Utils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.RecipeCategoryAdapter;
import com.robam.roki.ui.adapter3.CustomLoadMoreView;
import com.robam.roki.ui.dialog.RecipeSearchDialog;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 灶具菜谱
 * 烤箱菜谱
 * 蒸箱菜谱
 * 微波炉菜谱
 */

public class RecipeCategoryListPage extends MyBasePage<MainActivity> {

    String type;
    ImageView mIvBack;
    TextView mTvPageName;

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mRv;
    int start;
    int num = 10;
    private long mUserId;
    private RecipeCategoryAdapter mRecipeCategoryAdapter;
    private final int UPDATE_DATA = 1;
    String recipeType;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();

    private FirebaseAnalytics firebaseAnalytics;

    String platformCode;
    String mGuid;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString(PageArgumentKey.RecipeId);
            platformCode = bundle.getString(PageArgumentKey.platformCode);
            mGuid = bundle.getString(PageArgumentKey.Guid);
            LogUtils.i("202010241103","list:::guid:::"+mGuid);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_recipe_category;
    }

    @Override
    protected void initView() {
//        setStateBarTransparent();
        mIvBack = findViewById(R.id.img_back);
        mTvPageName = findViewById(R.id.tv_page_name);
        mRv = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);

    }

    @Override
    protected void initData() {
        firebaseAnalytics = MobApp.getmFirebaseAnalytics();
        mUserId = Plat.accountService.getCurrentUserId();
        //灶具菜谱
        if (DeviceType.RRQZ.equals(type)) {
            title = cx.getString(R.string.home_stove_recipe_text);
            firebaseAnalytics.setCurrentScreen(getActivity(), "灶具菜谱页", null);
        } else if (DeviceType.RDKX.equals(type)) {
            title = cx.getString(R.string.home_oven_recipe_text);
            firebaseAnalytics.setCurrentScreen(getActivity(), "电烤箱菜谱页", null);
        } else if (DeviceType.RZQL.equals(type)) {
            title = cx.getString(R.string.home_steam_recipe_text);
            firebaseAnalytics.setCurrentScreen(getActivity(), "蒸汽炉菜谱页", null);
        } else if (DeviceType.RWBL.equals(type)) {
            title = cx.getString(R.string.home_microwave_recipe_text);
            firebaseAnalytics.setCurrentScreen(getActivity(), "微波炉菜谱页", null);
        } else if (DeviceType.RZNG.equals(type)) {
            title = cx.getString(R.string.home_pot_recipe_text);
            firebaseAnalytics.setCurrentScreen(getActivity(), "无人锅菜谱页面页", null);
        } else if (DeviceType.RZKY.equals(type)) {
            title = cx.getString(R.string.steamOvenOneRecipe);
            firebaseAnalytics.setCurrentScreen(getActivity(), "一体机自动烹饪菜谱页面页", null);
        }
        mTvPageName.setText(title);
        initBgTitle();
        initAdapter();//初始化适配器
        requestData();//初始化数据
        initListener();//监听事件和配置
    }


    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }
    private void initBgTitle() {
        if (type.equals(IDeviceType.RZNG)) {
            type = IDeviceType.RRQZ;
            recipeType = "pot";
        } else {
            recipeType = "all";
        }

    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        if (event.flag == HomeRecipeViewEvent.RecipeFavoriteChange) {
            if (mRecipeCategoryAdapter != null) {
                mRecipeCategoryAdapter.notifyDataSetChanged();
            }
        }
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
        mRecipeCategoryAdapter.addChildClickViewIds(R.id.iv_collection);
        mRecipeCategoryAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter baseQuickAdapter, @NonNull View view, int i) {
                if (view.getId() == R.id.iv_collection) {
                    boolean isLogin = Plat.accountService.isLogon();
                    if (isLogin) {
                        Recipe recipe = mRecipeCategoryAdapter.getItem(i);
                        if (recipe != null) {
                            CookbookManager cm = CookbookManager.getInstance();

                            if (recipe.collected) {
                                cm.deleteFavorityCookbooks(recipe.id, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        recipe.setIsCollected(false);
//                        recipeCategoryViewHolder.iv_collection.setVisibility(View.VISIBLE);
                                        mRecipeCategoryAdapter.notifyItemChanged(i);
                                        ToastUtils.showShort("已取消收藏");
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        ToastUtils.showShort(t.getMessage());
                                    }
                                });
                            } else {
                                cm.addFavorityCookbooks(recipe.id, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        recipe.setIsCollected(true);
//                        recipeCategoryViewHolder.iv_collection.setVisibility(View.GONE);
                                        mRecipeCategoryAdapter.notifyItemChanged(i);
                                        ToastUtils.showShort("收藏成功");
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        ToastUtils.showShort(t.getMessage());
                                    }
                                });

                            }
                        }
                    } else {
                        CmccLoginHelper.getInstance().toLogin();
                    }
                }
            }
        });
//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(cx, 1);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                int viewType = mRv.getAdapter().getItemViewType(position);
//                if (viewType == RecipeCategoryAdapter.OTHER_VIEW) {
//                    return gridLayoutManager.getSpanCount();
//                } else {
//                    return 1;
//                }
//            }
//        });

        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.setAdapter(mRecipeCategoryAdapter);
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
                            mRecipeCategoryAdapter.getLoadMoreModule().loadMoreEnd();
//                            mRv.setNoMore(true);
//                            ToastUtils.show(new String("无更多菜谱"), Toast.LENGTH_SHORT);
                            return;
                        }
                    }

                    if (start == 0) {
                        mRecipeList.clear();
                    }
                    mRecipeList.addAll(list);
//                    mRecipeCategoryAdapter.updateData(mRecipeList);
                    mRecipeCategoryAdapter.setList(mRecipeList);
                    mRecipeCategoryAdapter.getLoadMoreModule().loadMoreComplete();
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mHandler.sendEmptyMessage(UPDATE_DATA);
//                        }
//                    }, 1000);
//                    if(mRecipeList != null && mRecipeList.size() != 0 && mRecipeCategoryAdapter != null){
//                        mRecipeCategoryAdapter.updateData(mRecipeList);
//                        if (mRv != null) {
//                            //更新完成
//                            mRv.refreshComplete();
//                            //加载完成
//                            mRv.loadMoreComplete();
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

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().popBack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.reset(this);
    }


    @OnClick(R.id.iv_recipe_search)
    public void onMIvRecipeSearchClicked() {
//        RecipeSearchDialog.show(cx, new RecipeSearchDialog.OnSearchCallback() {
//            @Override
//            public void onSearchWord(String word) {
//
//            }
//        });
        UIService.getInstance().postPage(PageKey.RecipeSearch2);
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

}