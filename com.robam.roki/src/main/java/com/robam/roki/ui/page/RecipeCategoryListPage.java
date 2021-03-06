package com.robam.roki.ui.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
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
import com.robam.roki.model.bean.RecipeCategoryMultiItem;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.RecipeCategoryAdapter;
import com.robam.roki.ui.adapter3.CustomLoadMoreHomeView;
import com.robam.roki.ui.dialog.RecipeSearchDialog;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * ????????????
 * ????????????
 * ????????????
 * ???????????????
 */

public class RecipeCategoryListPage extends MyBasePage<MainActivity> {

    String type;
    TextView mIvBack;
    TextView mTvPageName;
    ImageView mIvRecipeSearch;
    private ImageView iv_top_week_title_bg;
    @InjectView(R.id.recyclerView)
    RecyclerView mRv;
    @InjectView(R.id.swiper_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    int start;
    int num = 10;
    private long mUserId;
    private RecipeCategoryAdapter mRecipeCategoryAdapter;
    private final int UPDATE_DATA = 1;
    String recipeType;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
//    @SuppressLint("HandlerLeak")
//    Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//
//                case 0:
////                    mRecipeCategoryAdapter.imgBg((String) msg.obj);
//                    GlideApp.with(cx)
//                            .load((String) msg.obj)
////                            .apply(RequestOptions.bitmapTransform(multiTop))
//                            .into(iv_top_week_title_bg);
//                    break;
//
//                case UPDATE_DATA:
//                    if(mRecipeList != null && mRecipeList.size() != 0 && mRecipeCategoryAdapter != null){
//                        mRecipeCategoryAdapter.updateData(mRecipeList);
//                        if (mRv != null) {
//                            //????????????
//                            mRv.refreshComplete();
//                            //????????????
//                            mRv.loadMoreComplete();
//                        }
//                    }
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    String platformCode;
    String mGuid;
    private Toolbar topWeekToolbar;
    private AppBarLayout appBarLayout;


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
        setStateBarTransparent();
        mIvBack = findViewById(R.id.iv_back);
        mTvPageName = findViewById(R.id.tv_page_name);
        mIvRecipeSearch = findViewById(R.id.iv_recipe_search);
        iv_top_week_title_bg = findViewById(R.id.iv_top_week_title_bg);

        appBarLayout = findViewById(R.id.top_week_appbar);
        topWeekToolbar = (Toolbar) findViewById(R.id.top_week_toolbar);

        setAvatorChange();
    }

    @Override
    protected void initData() {

        mUserId = Plat.accountService.getCurrentUserId();
        //????????????
        if (DeviceType.RRQZ.equals(type)) {
            title = cx.getString(R.string.home_stove_recipe_text);
        } else if (DeviceType.RDKX.equals(type)) {
            title = cx.getString(R.string.home_oven_recipe_text);
        } else if (DeviceType.RZQL.equals(type)) {
            title = cx.getString(R.string.home_steam_recipe_text);
        } else if (DeviceType.RWBL.equals(type)) {
            title = cx.getString(R.string.home_microwave_recipe_text);
        } else if (DeviceType.RZNG.equals(type)) {
            title = cx.getString(R.string.home_pot_recipe_text);
        } else if (DeviceType.RZKY.equals(type)) {
            title = cx.getString(R.string.steamOvenOneRecipe);
        }
        mTvPageName.setText(title);
        initBgTitle();
        initAdapter();//??????????????????
        requestData();//???????????????
//        initListener();//?????????????????????
    }

    /**
     * ??????toolbar??????
     */
    private void setAvatorChange() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset?????????0???????????????
                float percent = (Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange());
                topWeekToolbar.setBackgroundColor(changeAlpha(Color.WHITE, percent));
                if (percent > 0.8) {
                    mTvPageName.setVisibility(View.VISIBLE);
                    mIvRecipeSearch.setVisibility(View.VISIBLE);
                    mIvRecipeSearch.setImageResource(R.mipmap.ic_recipe_search);
                    mIvBack.setBackground(getContext().getDrawable(R.mipmap.icon_back_black));
//                    StatusBarUtils.setColor(getActivity(), Color.TRANSPARENT);
//                    StatusBarUtils.setColor(cx, Color.WHITE);
                    StatusBarUtils.setTextDark(getContext() ,true);
                } else {
                    mTvPageName.setVisibility(View.INVISIBLE);
                    mIvRecipeSearch.setVisibility(View.VISIBLE);
                    mIvRecipeSearch.setImageResource(R.mipmap.icon_search_w);
                    mIvBack.setBackground(getContext().getDrawable(R.mipmap.icon_back_white));
//                    StatusBarUtils.setColor(cx, Color.BLACK);
                    StatusBarUtils.setTextDark(getContext() ,false);
                    setStateBarTransparent();
                }
            }
        });
    }
    /**
     * ????????????????????????????????????
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
        //????????????????????????
        RokiRestHelper.getDeviceRecipeImg(type, Reponses.CategoryRecipeImgRespone.class, new RetrofitCallback<Reponses.CategoryRecipeImgRespone>() {
            @Override
            public void onSuccess(Reponses.CategoryRecipeImgRespone rc) {
                if (rc !=null){
                    String imgUrl = rc.imgUrl;
                    GlideApp.with(cx)
                            .load(imgUrl)
                            .into(iv_top_week_title_bg);
                }
            }

            @Override
            public void onFaild(String err) {

            }
        });
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
        mRecipeCategoryAdapter = new RecipeCategoryAdapter(platformCode, mGuid);
//        mRecipeCategoryAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view) {
//                Recipe recipe = (Recipe) view.getTag(R.id.tag_recipe_bg);
//                RecipeDetailPage.show(recipe, recipe.id, RecipeDetailPage.DeviceRecipePage,
//                        RecipeRequestIdentification.RECIPE_SORTED,platformCode,mGuid);
//            }
//        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mRv.setLayoutManager(linearLayoutManager);
        mRv.setAdapter(mRecipeCategoryAdapter);
        mRecipeCategoryAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreHomeView());
        mRecipeCategoryAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                start += 10 ;
                requestData();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                start = 0 ;
                mRecipeList.clear();
                //????????????
                requestData();
            }
        });
    }

    private void requestData() {
        LogUtils.i("2020060901","mUserId:::"+mUserId);
        LogUtils.i("2020060901","type:::"+type);
        LogUtils.i("2020060901","recipeType:::"+recipeType);
        LogUtils.i("2020060901","start:::"+start);
        LogUtils.i("2020060901","num:::"+num);
        LogUtils.i("2020060901","platformCode:::"+platformCode);
        RokiRestHelper.getGroundingRecipesByDc(mUserId, type, recipeType, start, num, platformCode,
                Reponses.ThumbCookbookResponse.class, new RetrofitCallback<Reponses.ThumbCookbookResponse>() {
                    @Override
                    public void onSuccess(Reponses.ThumbCookbookResponse thumbCookbookResponse) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (null != thumbCookbookResponse) {
                            List<Recipe> list = thumbCookbookResponse.cookbooks;
                            try {
                                LogUtils.i("2020060802","list::"+list);

                                if (list == null || list.size() <= 0) {
                                    list = Lists.newArrayList();
                                    if (start > 0) {
                                        ToastUtils.show(new String("???????????????"), Toast.LENGTH_SHORT);
                                    }
                                }

                                mRecipeList.addAll(list);
                                List<RecipeCategoryMultiItem> recipeCategoryMultiItemList = new ArrayList<>();
                                for (Recipe recipe : mRecipeList) {
                                    recipeCategoryMultiItemList.add(new RecipeCategoryMultiItem(RecipeCategoryMultiItem.OTHER_VIEW, recipe));
                                }
                                mRecipeCategoryAdapter.setList(recipeCategoryMultiItemList);

//                                if(mRecipeList != null && mRecipeList.size() != 0 && mRecipeCategoryAdapter != null){
//                                    mRecipeCategoryAdapter.updateData(mRecipeList);
//                                    if (mRv != null) {
//                                        //????????????
//                                        mRv.refreshComplete();
//                                        //????????????
//                                        mRv.loadMoreComplete();
//                                    }
//                                }
                            } catch (Exception e) {
                                Log.e("RecipeCategory", "error:" + e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

        });


    }

//    private void initListener() {
//        mRv.setLoadingMoreEnabled(true);
//        mRv.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                start = 0 ;
//                mRecipeList.clear();
//                //????????????
//                requestData();
//            }
//
//            @Override
//            public void onLoadMore() {
//                LogUtils.i("20181114", " onLoadMore");
//                start += 10 ;
////                num += 10;
//                requestData();
//            }
//        });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            StatusBarUtils.setTextDark(getContext() ,true);
        }
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
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
     * ??????????????????????????????
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeDetailPage".equals(event.getPageName())){
            mRecipeList.clear();
            requestData();
        }
    }

}