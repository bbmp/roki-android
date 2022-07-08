package com.robam.roki.ui.page;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.services.CookbookManager;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.dialog.RecipeSearchDialog;
import com.robam.roki.ui.view.RecipeGridView;

import java.util.List;

import butterknife.OnClick;

/**
 * 更多菜谱页
 * Created by sylar on 15/6/14.
 */
public class RecipeHistoryPage extends AbsRecipeGridPage {

    private int model = RecipeGridView.FIRST_LOAD;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void initData() {
        super.initData();
        regsitRightView();
        ProgressDialogHelper.setRunning(cx, true);
        getDataFromHttp();
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseAnalytics = MobApp.getmFirebaseAnalytics();
        firebaseAnalytics.setCurrentScreen(getActivity(), "更多菜谱页", null);
    }

    private void getDataFromHttp() {
        CookbookManager.getInstance().getGroundingRecipes_new(start * 10, limit, null, new Callback<Reponses.ThumbCookbookResponse>() {
            @Override
            public void onSuccess(Reponses.ThumbCookbookResponse response) {
                ProgressDialogHelper.setRunning(cx, false);
                //loadBooks(RecipeGridView.Model_History, response.cookbooks, response.cookbook_3rds);
                loadBooks(RecipeGridView.Model_History, response.cookbooks, response.cookbook_3rds);
                gridView.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
                ToastUtils.showThrowable(t);
                gridView.onRefreshComplete();
            }
        });
    }

    @Override
    protected void loadBooks(int modelType, List<Recipe> books) {
        super.loadBooks(modelType, books);
        List<Recipe3rd> books3rd = Lists.newArrayList();
        loadBooks(modelType, books, books3rd);
    }

    @Override
    protected void loadBooks(int modelType, List<Recipe> books, List<Recipe3rd> books3rd) {
        super.loadBooks(modelType, books, books3rd);
        switchView((start == 0) && (books == null || books.size() == 0) && (books3rd == null || books3rd.size() == 0));
        gridView.getRefreshableView().loadData(modelType, books, books3rd, model);
    }

    boolean post = false;

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        if (event.flag == HomeRecipeViewEvent.BackToMoreRecipe) {
            post = true;
            start = 0;
            model = RecipeGridView.PULL_DOWN;
            getDataFromHttp();
        }
    }


    /**
     * 下拉监听
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecipeGridView> pullToRefreshBase) {
        super.onPullDownToRefresh(pullToRefreshBase);
        model = RecipeGridView.PULL_DOWN;
        getDataFromHttp();
    }

    /**
     * 上推监听
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecipeGridView> pullToRefreshBase) {
        super.onPullUpToRefresh(pullToRefreshBase);
        model = RecipeGridView.PULL_UP;
        getDataFromHttp();
    }

    @Override
    protected String getTextWhenEmpty() {
        return "还没有更多菜谱";
    }

    @OnClick(R.id.home_recipe_live_imgv_return)
    public void onMHomeRecipeLiveImgvReturnClicked() {
        UIService.getInstance().popBack();

    }

    void regsitRightView() {

        //设置标题右边搜索
        //导航栏左侧图标初始化
        ImageView img = new ImageView(cx);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtils.dip2px(cx, 20f),
                DisplayUtils.dip2px(cx, 20f));
        img.setLayoutParams(layoutParams);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        img.setPadding(0, 2, 0, 2);
        img.setImageResource(R.mipmap.ic_recipe_search);
        img.setClickable(true);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.newRecipeSearchDialog(cx, new RecipeSearchDialog.OnSearchCallback() {
                    @Override
                    public void onSearchWord(String word) {
                    }
                });
            }
        });
        mListviewTitlebarLlRight.addView(img);
        mHomeRecipeLiveTitle.setText(R.string.home_more_recipe);

    }


}
