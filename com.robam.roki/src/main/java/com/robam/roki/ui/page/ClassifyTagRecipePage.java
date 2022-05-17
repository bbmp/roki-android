/*
 * Copyright (C) 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.robam.roki.ui.page;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.RecipeTagGroupItem;
import com.robam.roki.ui.adapter.RecipeAdapter;
import com.robam.roki.ui.adapter3.CustomLoadMoreView;
import com.robam.roki.ui.adapter3.RvClassTagRecipeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.CustomEmptyView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 全部菜谱
 */
public class ClassifyTagRecipePage extends MyBasePage<MainActivity> {
    public static int PAGEKEY = 99;
    private static final String TAG = "ClassifyTagRecipePage";
    private View mRootView;
    private RecyclerView mXRecyclerView;
    private Bundle tagRecipeBundle;
    private RecipeTagGroupItem.ItemInfo recipeInfo;
    private List<Recipe> tagRecipeList = new ArrayList<>();
    private int mid;
    public static final String TAG_RECIPE = "tagsTagRecipe";//菜谱标签
    private CustomEmptyView mCustomEmptyView;
    private TextView tvRecipeTag;
    private View loadMoreView;
    private RvClassTagRecipeAdapter mTagRecipeAdapter;
    private ImageView ivTagRecipeBack;
    private int pageNo = 0;
    private int pageSize = 10;
    private Long cookBookTagId;
    private AppBarLayout tagRecipeAppbar;
    private TextView tv_title;
    private static RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.img_default) //预加载图片
            .error(R.mipmap.img_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //缓存
            .transform(new RoundedCornersTransformation(50, 10, RoundedCornersTransformation.CornerType.TOP)); //圆角
    private View emptyView;
    private TextView tvDesc;
    private int barColor;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recipe_tag_page;
    }

    @Override
    protected void initView() {
        setStateBarTransparent();
        mXRecyclerView = findViewById(R.id.rv_tag_recipe_list);
        tvRecipeTag = findViewById(R.id.tv_recipe_tag_name);
        ivTagRecipeBack = findViewById(R.id.iv_tag_recipe_back);
        tagRecipeAppbar = findViewById(R.id.tag_recipe_appbar);
        tv_title = findViewById(R.id.tv_title);
        //空布局
        emptyView = LinearLayout.inflate(cx, R.layout.view_emoji_empty, null);
        tvDesc = (TextView) emptyView.findViewById(R.id.txtDesc);
    }

    @Override
    protected void initData() {
        Typeface typeface = Typeface.createFromAsset(cx.getAssets(), "HYRunYuan-GEW.ttf");
        tv_title.setTypeface(typeface);
        tagRecipeBundle = getArguments();
        recipeInfo = (RecipeTagGroupItem.ItemInfo) tagRecipeBundle.getSerializable(TAG_RECIPE);
        LogUtils.i(TAG, "onActivityCreated: tagRecipe type:" + recipeInfo.getType() + " recipe getId:" + recipeInfo.getId() + " recipe name:" + recipeInfo.getName()+"grpup:" +recipeInfo.getGroup());
        cookBookTagId = recipeInfo.getId() == null ? null : (Long) (recipeInfo.getId());
        tvRecipeTag.setText(recipeInfo.getName());
        tv_title.setText(recipeInfo.getName());
        loadRecipeData();
        initRecyclerView();
        setAvatorChange();
        ivTagRecipeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().popBack();
            }
        });
    }


    private void loadRecipeData() {
        if (pageNo == 0){
            ProgressDialogHelper.setRunning(cx, true);
        }

        RokiRestHelper.getCookbookByTag(cookBookTagId,  pageNo, pageSize, Reponses.PersonalizedRecipeResponse.class,
                new RetrofitCallback<Reponses.PersonalizedRecipeResponse>() {
                    @Override
                    public void onSuccess(Reponses.PersonalizedRecipeResponse personalizedRecipeResponse) {
                        ProgressDialogHelper.setRunning(cx, false);
                        if (null != personalizedRecipeResponse) {
                            List<Recipe> recipes = personalizedRecipeResponse.cookbooks;

                            if (recipes.isEmpty() || recipes.size() == 0) {
                                if (pageNo == 0){
                                    tvDesc.setText("暂时没有该类型数据");
                                    mTagRecipeAdapter.setEmptyView(emptyView);
                                    return;
                                }
                                mTagRecipeAdapter.getLoadMoreModule().loadMoreEnd();
                                return;
                            }
                            mTagRecipeAdapter.addData(recipes);
                            pageNo ++ ;
                            mTagRecipeAdapter.getLoadMoreModule().loadMoreComplete();
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        ProgressDialogHelper.setRunning(cx, false);
                        LogUtils.i(TAG, "getbyTagOtherCooks onFailure recipes:" + err);
                        if (pageNo == 0){
                            tvDesc.setText("服务器请求出错");
                            mTagRecipeAdapter.setEmptyView(emptyView);
                        }else {
                            mTagRecipeAdapter.getLoadMoreModule().loadMoreFail();
                        }
                    }
        });
    }


    private void initRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        mXRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        mTagRecipeAdapter = new RvClassTagRecipeAdapter();
//        if ("产品".equals(recipeInfo.getGroup())){
//            mTagRecipeAdapter.setShowDevice(false);
//        }
        mXRecyclerView.setAdapter(mTagRecipeAdapter);
        mTagRecipeAdapter.addData(tagRecipeList);
        mTagRecipeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Recipe item = mTagRecipeAdapter.getItem(position);
                com.robam.roki.ui.page.recipedetail.RecipeDetailPage.show(item.id, PAGEKEY);
            }
        });
        mTagRecipeAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        mTagRecipeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadRecipeData();
            }
        });

    }

    public static Fragment newInstance() {
        ClassifyTagRecipePage mFragment = new ClassifyTagRecipePage();
        return mFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            StatusBarUtils.setTextDark(getContext() ,true);
        }
//        StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.white));
    }

    /**
     * 渐变toolbar背景
     */
    private void setAvatorChange() {
        tagRecipeAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset始终为0以下的负数
                try {
                    float percent = (Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange());
                    LogUtils.i(TAG, "percent:" + percent);
                    tagRecipeAppbar.setBackgroundColor(changeAlpha(Color.WHITE, percent));

                    if (percent > 0.8) {
                        tvRecipeTag.setVisibility(View.VISIBLE);
//                    ivTagRecipeBack.setBackground(getContext().getDrawable(R.mipmap.icon_back_black));
//                    ivTagRecipeBack.setVisibility(View.VISIBLE);
                        barColor = getResources().getColor(R.color.white);
                        StatusBarUtils.setColor(getActivity(), barColor);
//                        ivTagRecipeBack.setImageResource(R.mipmap.icon_back_black);
                    } else {
                        tvRecipeTag.setVisibility(View.INVISIBLE);
//                    ivTagRecipeBack.setBackground(getContext().getDrawable(R.mipmap.icon_back_white));
//                    ivTagRecipeBack.setVisibility(View.VISIBLE);
//                        ivTagRecipeBack.setImageResource(R.mipmap.icon_back_white);
                        barColor = getResources().getColor(R.color.roki_main_color);
                        StatusBarUtils.setColor(getActivity(), barColor);

                    }
                }catch (Exception e){
                    e.getMessage();
                }

            }
        });
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

    /**
     * 模拟从菜谱详情页返回
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeDetailPage".equals(event.getPageName())){
//            setAvatorChange();
            StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.white));
        }
    }
}
