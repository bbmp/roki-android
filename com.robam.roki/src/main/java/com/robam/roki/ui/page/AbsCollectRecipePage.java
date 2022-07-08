package com.robam.roki.ui.page;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.adapter3.RvRecipeThemeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.RecipeGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class AbsCollectRecipePage extends MyBasePage<MainActivity> {
    private RecyclerView recipeList;
    private LinearLayout emptyView;
    private RvRecipeThemeAdapter rvRecipeThemeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.page_layout_collect_recipe;
    }

    @Override
    protected void initView() {
        recipeList = (RecyclerView) findViewById(R.id.rv_recipe_list);
        emptyView = (LinearLayout) findViewById(R.id.emptyView);
        recipeList.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        rvRecipeThemeAdapter = new RvRecipeThemeAdapter();
        recipeList.setAdapter(rvRecipeThemeAdapter);
    }

    @Override
    protected void initData() {
        initRecipeData();
    }

    @Override
    protected void setStateBarFixer2() {

    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        initRecipeData();
    }

    /**
     * 获取菜谱数据
     */
    protected void initRecipeData() {
        ProgressDialogHelper.setRunning(cx, true);
        CookbookManager.getInstance().getFavorityCookbooks(
                new Callback<Reponses.CookbooksResponse>() {

                    @Override
                    public void onSuccess(Reponses.CookbooksResponse result) {
                        ProgressDialogHelper.setRunning(cx, false);
                        if (result == null || ((result.cookbooks == null || (result.cookbooks.size() == 0))
                                && (result.cookbooks3rd == null || result.cookbooks3rd.size() == 0))) {

                            emptyView.setVisibility(View.VISIBLE);
                            recipeList.setVisibility(View.GONE);
                            return;
                        }

                        emptyView.setVisibility(View.GONE);
                        recipeList.setVisibility(View.VISIBLE);
                        List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList = new ArrayList<>();
                        for (Recipe recipe : result.cookbooks) {
                            themeRecipeMultipleItemList.add(new ThemeRecipeMultipleItem(ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT, recipe));
                        }

                        rvRecipeThemeAdapter.setList(themeRecipeMultipleItemList);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        recipeList.setVisibility(View.GONE);
                        ToastUtils.showThrowable(t);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
