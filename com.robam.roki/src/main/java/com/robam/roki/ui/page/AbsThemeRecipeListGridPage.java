package com.robam.roki.ui.page;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.EventUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.EmojiEmptyView;
import com.robam.roki.ui.view.RecipeGridTitleView;
import com.robam.roki.ui.view.RecipeGridView;
import com.robam.roki.ui.view.ThemeListViewItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rent on 2016/8/26.
 * 我的收藏
 */

public class AbsThemeRecipeListGridPage extends MyBasePage<MainActivity> {
    //未收藏显示
    @InjectView(R.id.emptyView)
    EmojiEmptyView emptyView;
    RecipeGridTitleView gridView;
    @InjectView(R.id.img_back)
    ImageView imgBack;

    /*@InjectView(R.id.listview)//主题listview
            ThemeListView listView;*/

//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.abs_page_recipetheme_listgrid, container,false);
//
//
//        EventUtils.regist(this);
//        ButterKnife.inject(this, view);
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.abs_page_recipetheme_listgrid;
    }

    @Override
    protected void initView() {
        EventUtils.regist(this);
        gridView = findViewById(R.id.gridView);

    }

    @Override
    protected void initData() {
        if (!rootView.isInEditMode()) {
            emptyView.setText(getTextWhenEmpty());
            init();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        regsitRightView();//清空按钮
        initThemeData();//获取收藏主题
    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        init();
    }

    /**
     * 获取菜谱数据
     */
    protected void initRecipeData() {
        ProgressDialogHelper.setRunning(cx, true);
        RokiRestHelper.getFavorityCookbooks(Reponses.CookbooksResponse.class,
                new RetrofitCallback<Reponses.CookbooksResponse>() {

                    @Override
                    public void onSuccess(Reponses.CookbooksResponse result) {
                        ProgressDialogHelper.setRunning(cx, false);
                        if (result == null || ((result.cookbooks == null || (result.cookbooks.size() == 0))
                                && (result.cookbooks3rd == null || result.cookbooks3rd.size() == 0))) {
                            if (theme_flag == null || theme_flag.size() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            gridView.loadData(RecipeGridView.Model_Favority, null, null);
                            return;
                        }
                        if (emptyView != null){
                            emptyView.setVisibility(View.GONE);
                        }
                        if (gridView !=null){
                            gridView.loadData(RecipeGridView.Model_Favority, result.cookbooks, result.cookbooks3rd);
                        }

                    }

                    @Override
                    public void onFaild(String err) {
                        ProgressDialogHelper.setRunning(cx, false);
                        gridView.setVisibility(View.GONE);
                        ToastUtils.show(err);
                    }
                });
    }

    private List<RecipeTheme> theme_flag;

    /**
     * 获取收藏的主题
     */
    private void initThemeData() {
//        ProgressDialogHelper.setRunning(cx, true);
        RokiRestHelper.getMyFavoriteThemeRecipeList_new(Reponses.RecipeThemeResponse3.class, new RetrofitCallback<Reponses.RecipeThemeResponse3>() {
            @Override
            public void onSuccess(Reponses.RecipeThemeResponse3 recipeThemeResponse3) {
                if (null != recipeThemeResponse3) {
                    List<RecipeTheme> recipeThemes = recipeThemeResponse3.recipeThemes;
                    theme_flag = recipeThemes;
                    if (recipeThemes == null || recipeThemes.size() == 0) {
                        //.setVisibility(View.GONE);
                        gridView.removeHeaderView(linearLayout);
                        initRecipeData();
                        return;
                    }
                    if (emptyView != null && gridView != null){
                        emptyView.setVisibility(View.GONE);
                        //listView.loadData(recipeThemes);
                        gridView.addHeaderView(loadHeadView(recipeThemes));
                        initRecipeData();
                    }
                }
            }

            @Override
            public void onFaild(String err) {
                theme_flag = null;
                gridView.removeHeaderView(linearLayout);
                initRecipeData();
            }
        });
    }

    LinearLayout linearLayout;

    /**
     * 设置头部主题列表
     */
    public LinearLayout loadHeadView(List<RecipeTheme> recipeThemes) {
        if (recipeThemes == null || recipeThemes.size() == 0) return null;
        gridView.removeHeaderView(linearLayout);
        linearLayout = (LinearLayout) LayoutInflater.from(cx).inflate(R.layout.view_recipe_myfavorite_title,null, false);
        TextView title = linearLayout.findViewById(R.id.myfavorite_title);
        title.setText("主题");
        for (RecipeTheme re : recipeThemes) {
            ThemeListViewItem themeListViewItem = new ThemeListViewItem(cx);
            themeListViewItem.loadData(re);
            //设置分割线
            View divider = new View(cx);
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(cx, 5)));
            linearLayout.addView(divider);
            linearLayout.addView(themeListViewItem);
        }
        return linearLayout;
    }

    protected String getTextWhenEmpty() {
        return "还没有收藏呢";
    }

    void onClear() {
        DialogHelper.newDialog_OkCancel(cx, "确认清空我的收藏？", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    ProgressDialogHelper.setRunning(cx, true);
                    CookbookManager.getInstance().delteAllFavorityCookbooks(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ProgressDialogHelper.setRunning(cx, false);
                            ToastUtils.showShort("清空成功");
                            init();
                            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.ClearMyFavorite));
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ProgressDialogHelper.setRunning(cx, false);
                            ToastUtils.showThrowable(t);
                        }
                    });
                }
            }
        }).show();

    }

    /**
     * 初始化右上角清空按钮
     */
    void regsitRightView() {
        TextView txtView = TitleBar.newTitleTextView(cx, "清空", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emptyView.isShown())
                    onClear();
            }
        });
        txtView.setTextColor(Color.rgb(255, 180, 0));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {

        UIService.getInstance().popBack();
    }

//    @Subscribe
//    public void onEvent(PageBackEvent event) {
//        if ("SelectThemeDetailPage".equals(event.getPageName())){
//            if (Plat.accountService.isLogon()){
//                initThemeData();
//            }
//        }
//        if ("RecipeDetailPage".equals(event.getPageName())){
//            if (Plat.accountService.isLogon()){
//                initRecipeData();
//            }
//        }
//    }
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("SelectThemeDetailPage".equals(event.getPageName())||"RecipeDetailPage".equals(event.getPageName())){
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = activity.getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                StatusBarUtils.setTextDark(getContext(), true);
            }
            if (Plat.accountService.isLogon()){
                initThemeData();
            }
        }
    }
}
