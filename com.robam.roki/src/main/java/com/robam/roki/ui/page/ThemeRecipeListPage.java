package com.robam.roki.ui.page;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.RvThemeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * des ：精选专题列表
 *
 * @author wwq
 */
public class ThemeRecipeListPage extends MyBasePage<MainActivity> {


    private static final String TAG = "ThemeRecipeListPage";

    @InjectView(R.id.iv_theme_recipe_back)
    ImageView ivThemeRecipeBack;

    @InjectView(R.id.xrv_theme_recipe)
    RecyclerView xRvThemeRecipe;

    TextView tvThemeTellRoki;
    /**
     * 专题adapter
     */
    private RvThemeAdapter themeRecipeAdapter;
    private SpannableStringBuilder ssb;
    private ArrayList<String> timeDataList = new ArrayList<>();
    /**
     * 底部告诉roki
     */
    private View footView;
    /**
     * 获取数据为空时候的提示view
     */
    private View emptyView;
    private TextView tvDesc;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_theme_recipe_list_page;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setColor(cx , getResources().getColor(R.color.white));
        xRvThemeRecipe.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        themeRecipeAdapter = new RvThemeAdapter();
        themeRecipeAdapter.addChildClickViewIds(R.id.iv_love_theme, R.id.iv_theme_item_img);
        xRvThemeRecipe.setAdapter(themeRecipeAdapter);
        themeRecipeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.iv_love_theme) {
//                    ImageView imageView = (ImageView)adapter.getViewByPosition(position, R.id.iv_love_theme);
                    RecipeTheme item = (RecipeTheme) adapter.getItem(position);
//                    UiHelper.onFavority(item, imageView, null);
                    themeCollect(item, (ImageView) view);
                } else if (R.id.iv_theme_item_img == view.getId()) {
                    RecipeTheme item = (RecipeTheme) adapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PageArgumentKey.Theme, item);
//                    bundle.putStringArrayList(PageArgumentKey.timeDataList, timeDataList);
                    UIService.getInstance().postPage(PageKey.SelectThemeDetailPage, bundle);
                }
            }
        });
        footView = LinearLayout.inflate(cx, R.layout.foot_tell_roki, null);
        tvThemeTellRoki = (TextView) footView.findViewById(R.id.tv_theme_tell_roki);
        String tellRokiText = tvThemeTellRoki.getText().toString().trim();
        int start = 15;
        ssb = new SpannableStringBuilder();
        ssb.append(tellRokiText);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.roki_sub_color)), start, start + 6, 0);
        tvThemeTellRoki.setText(ssb);
        tvThemeTellRoki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i(TAG, "click tagRecipeTellRoki ");
                UIService.getInstance().postPage(PageKey.TellRoki);
            }
        });
        //空布局
        emptyView = LinearLayout.inflate(cx, R.layout.view_emoji_empty, null);
        tvDesc = (TextView) emptyView.findViewById(R.id.txtDesc);
    }

    @Override
    protected void initData() {
        getThemeRecipeList();
    }


    /**
     * 收藏专题
     */
    private void themeCollect(RecipeTheme recipeTheme, ImageView view) {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            //判断是否被收藏
            if (!recipeTheme.isCollect) {
                ProgressDialogHelper.setRunning(cx, true);
                StoreService.getInstance().setThemeCollect(recipeTheme.id, new Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            recipeTheme.isCollect = true;
                            ToastUtils.show("收藏成功");
                            view.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }
                        ProgressDialogHelper.setRunning(cx, false);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                    }
                });
            } else {
                ProgressDialogHelper.setRunning(cx, true);
                StoreService.getInstance().setCancelThemeCollect(recipeTheme.id, new Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            recipeTheme.isCollect = false;
                            ToastUtils.show("已取消收藏");
                            view.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                        ProgressDialogHelper.setRunning(cx, false);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
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


    @OnClick(R.id.iv_theme_recipe_back)
    public void onThemeBack() {
        UIService.getInstance().popBack();
    }


    /**
     * 获取专题列表
     */
    private void getThemeRecipeList() {
        ProgressDialogHelper.setRunning(cx, true);
        RokiRestHelper.getThemeRecipeList(Reponses.RecipeThemeResponse.class, new RetrofitCallback<Reponses.RecipeThemeResponse>() {
            @Override
            public void onSuccess(Reponses.RecipeThemeResponse recipeThemeResponse) {
                if (null != recipeThemeResponse) {
                    List<RecipeTheme> themes = recipeThemeResponse.items;
                    if (themes == null || themes.size() == 0) {
                        themeRecipeAdapter.setEmptyView(emptyView);
                        tvDesc.setText("没有获取到专题数据");
                        return;
                    }
                    //获取被收藏的专题
                    getThemeCollection(themes);
                    ProgressDialogHelper.setRunning(cx, false);
                }
            }

            @Override
            public void onFaild(String err) {
                themeRecipeAdapter.setEmptyView(emptyView);
                tvDesc.setText(err);
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    /**
     * 获取被收藏专题列表
     */
    private void getThemeCollection(List<RecipeTheme> themes) {
        if (!Plat.accountService.isLogon()) {
            themeRecipeAdapter.setList(themes);
            themeRecipeAdapter.setFooterView(footView);
            return;
        }
        ProgressDialogHelper.setRunning(cx, true);
        StoreService.getInstance().getMyFavoriteThemeRecipeList(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> recipeThemes) {
                if (recipeThemes != null && recipeThemes.size() != 0) {
                    themeCollection(themes, recipeThemes);
                } else {
                    themeRecipeAdapter.setList(themes);
                    themeRecipeAdapter.setFooterView(footView);
                }
                ProgressDialogHelper.setRunning(cx, false);
            }

            @Override
            public void onFailure(Throwable t) {
                themeRecipeAdapter.setEmptyView(emptyView);
                tvDesc.setText(t.getMessage());
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    /**
     * 专题列表数据和收藏列表数据合并处理
     */
    private void themeCollection(List<RecipeTheme> themes, List<RecipeTheme> recipeThemes) {
        for (RecipeTheme theme :
                themes) {
            for (RecipeTheme recipeTheme :
                    recipeThemes) {
                if (theme.id.equals(recipeTheme.id)) {
                    theme.isCollect = true;
                    break;
                }
            }
        }
        themeRecipeAdapter.setList(themes);
        themeRecipeAdapter.setFooterView(footView);
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        getThemeRecipeList();
    }

    /**
     * 从专题详情返回
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if("SelectThemeDetailPage".equals(event.getPageName())){
            getThemeRecipeList();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            StatusBarUtils.setTextDark(getContext() ,true);
        }
    }
}