package com.robam.roki.ui.view.recipeclassify;


import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.dialog.RecipeSearchDialog;
import com.robam.roki.ui.page.AbsRecipeGridPage;
import com.robam.roki.ui.view.RecipeGridView;


import java.util.List;

import butterknife.OnClick;

public class RecipeClassifySearchPage extends AbsRecipeGridPage {
    private int model = RecipeGridView.FIRST_LOAD;
    @Override
    protected void initData() {
        super.initData();
        regsitRightView();
        ProgressDialogHelper.setRunning(cx, true);
        getData();
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
        mHomeRecipeLiveTitle.setText(tag.getName());
    }



    /**
     * 下拉监听
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecipeGridView> pullToRefreshBase) {
        super.onPullDownToRefresh(pullToRefreshBase);
        model = RecipeGridView.PULL_DOWN;
        getData();
    }

    /**
     * 上推监听
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecipeGridView> pullToRefreshBase) {
        super.onPullUpToRefresh(pullToRefreshBase);
        model = RecipeGridView.PULL_UP;
        getData();
    }

    int pageNo = 0;
    int pageSize = 20;

    public void getData(){
        RokiRestHelper.getCookbooksByTag(tag.getID(),pageNo,pageSize, new Callback<Reponses.CookbooksResponse>() {
            @Override
            public void onSuccess(Reponses.CookbooksResponse cookbooksResponse) {
                ProgressDialogHelper.setRunning(cx, false);
                if (cookbooksResponse!=null){
                    pageSize+=20;
                    loadBooks(RecipeGridView.Model_classify, cookbooksResponse.cookbooks, cookbooksResponse.cookbooks3rd);
                    gridView.onRefreshComplete();
                }
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
    protected void loadBooks(int modelType, List<Recipe> books, List<Recipe3rd> books3rd) {
        super.loadBooks(modelType, books, books3rd);
        switchView((books == null || books.size() == 0) && (books3rd == null || books3rd.size() == 0));
        gridView.getRefreshableView().loadData(modelType, books, books3rd, model);
    }

    /**
     * 导航栏左侧点击
     */
    @OnClick(R.id.home_recipe_live_imgv_return)
    void OnClickLeft() {
        UIService.getInstance().popBack();
    }

}
