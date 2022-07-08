package com.robam.roki.ui.page;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.utils.StatusBarCompat;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.adapter3.CustomLoadMoreView;
import com.robam.roki.ui.adapter3.RvTopWeekAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author wwq
 * des : TOP榜单
 */
public class TopWeekPage extends MyBasePage<MainActivity> {

    private static final String TAG = "TopWeekPage";

    @InjectView(R.id.xrv_top_recipe)
    RecyclerView rvTopRecipe;


    @InjectView(R.id.tv_top_week_back)
    ImageView tvTopWeekBack;

    private int pageNo = 0;
    private int pageSize = 10;
    /**
     * 收藏的菜谱数据
     */
    ArrayList<AbsRecipe> absRecipes ;
    /**
     * 获取数据为空时候的提示view
     */
    private View emptyView;
    private TextView tvDesc;
    /**
     * adapter
     */
    private RvTopWeekAdapter rvTopWeekAdapter;

    /**
     * 选中的菜谱
     */
    private Recipe recipe ;

    private int position ;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_top_week_more;
    }

    @Override
    protected void initView() {
//        setStateBarTransparent();
        rvTopRecipe.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        emptyView = LinearLayout.inflate(cx, R.layout.view_emoji_empty, null);
        tvDesc = (TextView) emptyView.findViewById(R.id.txtDesc);

    }

    @Override
    protected void initData() {
//        topicWeekRecipeAdapter = new TopicWeekRecipeAdapter(getContext(), tagRecipeList);
        rvTopWeekAdapter = new RvTopWeekAdapter();
        rvTopWeekAdapter.addChildClickViewIds(R.id.iv_top_week_img, R.id.iv_love_recipe);
        rvTopRecipe.setAdapter(rvTopWeekAdapter);
        rvTopWeekAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                TopWeekPage.this.position = position ;
                 recipe = rvTopWeekAdapter.getItem(position);
                if (view.getId() == R.id.iv_top_week_img) {
                    RecipeDetailPage.show(recipe.id, recipe.sourceType);
                } else if (view.getId() == R.id.iv_love_recipe) {
                    collect(recipe, position);
                }
            }
        });
//        // 当数据不满一页时，是否继续自动加载（默认为true）
//        rvTopWeekAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
//        // 是否自定加载下一页（默认为true）
//        rvTopWeekAdapter.getLoadMoreModule().setAutoLoadMore(true);
//        // 预加载的位置（默认为1）
//        rvTopWeekAdapter.getLoadMoreModule().setPreLoadNumber(1);
        // 设置加载更多监听事件
        rvTopWeekAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        rvTopWeekAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getWeekRecipeTops();
            }
        });

        queryData();
    }

    private void queryData(){
        pageNo = 0 ;
        rvTopWeekAdapter.setNewInstance(new ArrayList<>());
        if (Plat.accountService.isLogon()){
            getCollectRecipe();
        }else {
            getWeekRecipeTops();
        }
    }
    /**
     * 收藏
     */
    private void collect(Recipe recipe, int position) {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            if (recipe.collected) {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().deleteFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show("已取消收藏");
                        recipe.setIsCollected(false);
                        recipe.collectCount = recipe.collectCount - 1;
                        rvTopWeekAdapter.setData(position, recipe);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show(t.getMessage());

                    }
                });
            } else {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().addFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show("收藏成功");
                        recipe.setIsCollected(true);
                        recipe.collectCount = recipe.collectCount + 1;
                        rvTopWeekAdapter.setData(position, recipe);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show(t.getMessage());
                    }
                });
            }

        } else {
            if (CmccLoginHelper.getInstance().isGetPhone) {
                CmccLoginHelper.getInstance().loginAuth();
            } else {
                CmccLoginHelper.getInstance().login();
            }
        }
    }

    private void setAdapterData(List<Recipe> recipes){
        rvTopWeekAdapter.addData(recipes);
        pageNo++;
        rvTopWeekAdapter.getLoadMoreModule().loadMoreComplete();
    }
    /**
     * 获取Top榜单
     */
    private void getWeekRecipeTops() {
        String weekTime = TimeUtils.getlastWeekTime();
        LogUtils.i(TAG, "weekTime:" + weekTime);
        RokiRestHelper.getWeekTops(weekTime, pageNo, pageSize, new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(final List<Recipe> recipes) {
                LogUtils.i(TAG, "getWeekTops onSuccess");
                if (recipes == null || recipes.size() == 0) {
                    rvTopWeekAdapter.getLoadMoreModule().loadMoreEnd();
                    return;
                }

                collectData(recipes, absRecipes);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i(TAG, "onFailure " + t.toString());
                rvTopWeekAdapter.getLoadMoreModule().loadMoreFail();
            }
        });

    }

    /**
     * 获取收藏菜谱数据
     */
    protected void getCollectRecipe() {

        CookbookManager.getInstance().getFavorityCookbooks(
                new Callback<Reponses.CookbooksResponse>() {
                    @Override
                    public void onSuccess(Reponses.CookbooksResponse result) {
                         absRecipes = new ArrayList<>();
                        if (result != null) {
                            if (result.cookbooks != null && result.cookbooks.size() != 0) {
                                absRecipes.addAll(result.cookbooks);
                            }
                            if (result.cookbooks3rd != null && result.cookbooks3rd.size() != 0) {
                                absRecipes.addAll(result.cookbooks3rd);
                            }
                        }
                        getWeekRecipeTops();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        rvTopWeekAdapter.getLoadMoreModule().loadMoreFail();
                        ToastUtils.show(t.getMessage());
                    }
                });
    }

    /**
     * 处理列表和收藏列表数据
     */
    private void collectData(List<Recipe> recipes, ArrayList<AbsRecipe> absRecipes) {
        if (absRecipes == null || absRecipes.size() == 0){
            setAdapterData(recipes);
        }else {
            for (Recipe recipe :
                    recipes) {
                for (AbsRecipe absRecipe :
                        absRecipes) {
                    if (recipe.id == absRecipe.id) {
                        recipe.collected = true;
                        break;
                    }
                }
            }
            setAdapterData(recipes);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    static class TopWeekRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRecipeIcon;
        TextView tvRecipeName;
        TextView recipeCollectCount;
        TextView tvRecipeReadNumber;
        TextView tvRecipeCollectNumber;
        ImageView ivRecipeLove;
        ImageView ivTopicRanking;

        TopWeekRecipeViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            ivRecipeIcon = itemView.findViewById(R.id.iv_top_week_img);
            tvRecipeReadNumber = itemView.findViewById(R.id.tv_recipe_read_number);
            tvRecipeCollectNumber = itemView.findViewById(R.id.tv_recipe_collect_number);
            ivRecipeLove = itemView.findViewById(R.id.iv_love_recipe);
            ivTopicRanking = itemView.findViewById(R.id.iv_topic_ranking);
        }
    }


    @OnClick(R.id.tv_top_week_back)
    public void topWeekBack() {
//        StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.white));
        UIService.getInstance().popBack();
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

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        pageNo = 0 ;
        rvTopWeekAdapter.setList(new ArrayList<>());
        getWeekRecipeTops();
    }

    /**
     * 模拟从菜谱详情页返回
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeDetailPage".equals(event.getPageName())){
            if (Plat.accountService.isLogon()){
//                queryData();
                getCollectRecipe2();
            }
        }
    }
    /**
     * 获取收藏菜谱数据
     */
    protected void getCollectRecipe2() {

        CookbookManager.getInstance().getFavorityCookbooks(
                new Callback<Reponses.CookbooksResponse>() {
                    @Override
                    public void onSuccess(Reponses.CookbooksResponse result) {
                        absRecipes = new ArrayList<>();
                        if (result != null) {
                            if (result.cookbooks != null && result.cookbooks.size() != 0) {
                                absRecipes.addAll(result.cookbooks);
                            }
                            if (result.cookbooks3rd != null && result.cookbooks3rd.size() != 0) {
                                absRecipes.addAll(result.cookbooks3rd);
                            }
                        }
                        for (AbsRecipe absRecipe: absRecipes
                             ) {
                            if (absRecipe.id == recipe.id){
                                recipe.collected = true ;
                                rvTopWeekAdapter.setData(position , recipe);
                                return;
                            }
                        }
                        recipe.setIsCollected(false);
                        rvTopWeekAdapter.setData(position , recipe);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show(t.getMessage());
                    }
                });
    }

}