package com.robam.roki.ui.page;

import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.model.bean.TopicMultipleItem;
import com.robam.roki.ui.adapter.SelectedTopicsAdapter;
import com.robam.roki.ui.adapter3.RvRecipeThemeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class AbsCollectRecipeThemePage extends MyBasePage<MainActivity> {

    private RecyclerView recipeList;
    private LinearLayout emptyView;
    private SelectedTopicsAdapter selectedTopicsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.page_layout_collect_recipe;
    }

    @Override
    protected void initView() {
        recipeList = (RecyclerView) findViewById(R.id.rv_recipe_list);
        emptyView = (LinearLayout) findViewById(R.id.emptyView);
        recipeList.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        selectedTopicsAdapter = new SelectedTopicsAdapter();
        recipeList.setAdapter(selectedTopicsAdapter);
    }

    @Override
    protected void initData() {
        initThemeData();
    }

    @Override
    protected void setStateBarFixer2() {

    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        initThemeData();
    }
    /**
     * 获取收藏的主题
     */
    private void initThemeData() {
        ProgressDialogHelper.setRunning(cx, true);
        StoreService.getInstance().getMyFavoriteThemeRecipeList(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> recipeThemes) {
                ProgressDialogHelper.setRunning(cx, false);
                List<TopicMultipleItem> topicMultipleItemList = new ArrayList<TopicMultipleItem>() ;
                for (RecipeTheme recipeTheme : recipeThemes) {

                    topicMultipleItemList.add(new TopicMultipleItem(TopicMultipleItem.IMG_THEME_RECIPE_COLLECT, recipeTheme.imageUrl, recipeTheme.name));

                }
                if (topicMultipleItemList.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    recipeList.setVisibility(View.GONE);
                    return;
                }

                emptyView.setVisibility(View.GONE);
                recipeList.setVisibility(View.VISIBLE);
                selectedTopicsAdapter.setList(topicMultipleItemList);
            }

            @Override
            public void onFailure(Throwable t) {
                recipeList.setVisibility(View.GONE);
                t.printStackTrace();
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
