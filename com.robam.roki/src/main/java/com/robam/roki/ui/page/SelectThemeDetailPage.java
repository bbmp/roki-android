package com.robam.roki.ui.page;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.events.ConnectionModeChangedEvent;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.ThemeRecipeDetail;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.NumberUtil;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.RvSelectThemeAdapter;
import com.robam.roki.ui.dialog.CookbookShareDialog;
import com.robam.roki.ui.dialog.RecipeThemeShareDialog;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 精选专题详情
 */
public class SelectThemeDetailPage extends MyBasePage<MainActivity> {
    private static final String TAG = "SelectThemeDetailPage";


    public static final int TYPE_THEME_RECIPE = 1;
    public static final int TYPE_THEME_DETAIL = 2;
    public static final int TYPE_THEME_BANNER = 3;


    @InjectView(R.id.xrv_select_theme_recipe)
    RecyclerView xRvSelectThemeRecipe;

    @InjectView(R.id.iv_select_theme_title_bg)
    ImageView ivSelectThemeTitleBg;

    @InjectView(R.id.tv_select_theme_back)
    ImageView tvSelectThemeBack;

    @InjectView(R.id.tv_select_theme_toolbar_title)
    TextView tvSelectThemeToolBarTitle;

//    @InjectView(R.id.tv_theme_desc)
//    TextView tvThemeDesc;
//
//    @InjectView(R.id.tv_read_theme_number)
//    TextView tvReadThemeNumber;
//
//    @InjectView(R.id.tv_collect_theme_number)
//    TextView tvCollectThemeNumber;
//
//    @InjectView(R.id.tv_theme_recipe_number)
//    TextView tvThemeRecipeNumber;

    @InjectView(R.id.imgFavority)
    ImageView imgFavority;

    @InjectView(R.id.imgShare)
    ImageView imgShare;

    @InjectView(R.id.srl_theme)
    SwipeRefreshLayout srl_theme;

    private Toolbar selectThemeToolbar;
    private AppBarLayout appBarLayout;
    private Bundle selectThemeBundle;
    private RecipeTheme theme;//主题对象
    private List<Recipe> recipeList = new ArrayList<>();

    private int pageNo = 0;
    private int pageSize = 10;
    private RvSelectThemeAdapter rvSelectThemeAdapter;
    float percent;

    /**
     * 底部告诉roki
     */
    private View footView;

    private TextView tvThemeTellRoki;
    /**
     * 获取数据为空时候的提示view
     */
    private View emptyView;
    private TextView tvDesc;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_theme_detail_page;
    }

    @Override
    protected void initView() {
        setStateBarTransparent();
        selectThemeToolbar = (Toolbar) findViewById(R.id.select_theme_toolbar);
        appBarLayout = findViewById(R.id.select_theme_appbar);

        xRvSelectThemeRecipe.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        rvSelectThemeAdapter = new RvSelectThemeAdapter();
        rvSelectThemeAdapter.addChildClickViewIds(R.id.iv_top_week_img, R.id.iv_love_recipe);
        xRvSelectThemeRecipe.setAdapter(rvSelectThemeAdapter);
        rvSelectThemeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Recipe item = rvSelectThemeAdapter.getItem(position);
                if (view.getId() == R.id.iv_top_week_img) {
                    RecipeDetailPage.show(item.id, item.sourceType);
                } else if (view.getId() == R.id.iv_love_recipe) {
                    collect(item, position);
                }
            }
        });

        footView = LinearLayout.inflate(cx, R.layout.foot_tell_roki, null);
        tvThemeTellRoki = (TextView) footView.findViewById(R.id.tv_theme_tell_roki);
        String tellRokiText = tvThemeTellRoki.getText().toString().trim();
        int start = 15;
        SpannableStringBuilder ssb = new SpannableStringBuilder();
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

        srl_theme.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recipeList.clear();
                getThemeRecipeDetail(theme.id);
            }
        });
    }

    @OnClick(R.id.imgShare)
    public void onClickShare() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (selfPermission == 0) {
                RecipeThemeShareDialog.show(cx, theme);
            } else {
                PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_RECIPE_DETAIL_SHARE);
            }
        } else {
            RecipeThemeShareDialog.show(cx, theme);
        }
    }

    @OnClick(R.id.imgFavority)
    public void onClickFavority() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            //判断是否被收藏
            if (!theme.isCollect) {
                ProgressDialogHelper.setRunning(cx, true);
                StoreService.getInstance().setThemeCollect(theme.id, new Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            theme.isCollect = true;
                            ToastUtils.showShort("收藏成功");
                            if (percent < 0.8) {
                                imgFavority.setImageResource(R.drawable.icon_collected);
                            } else {
                                imgFavority.setImageResource(com.robam.common.R.drawable.ic_baseline_favorite_24);
                            }
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
                StoreService.getInstance().setCancelThemeCollect(theme.id, new Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            theme.isCollect = false;
                            ToastUtils.showShort("已取消收藏");
                            if (percent < 0.8) {
                                imgFavority.setImageResource(R.drawable.icon_collect);
                            } else {
                                imgFavority.setImageResource(com.robam.common.R.drawable.ic_baseline_favorite_border_24);
                            }
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

    @Override
    protected void initData() {
        selectThemeBundle = getArguments();

        int type_theme = selectThemeBundle.getInt(PageArgumentKey.ThemeType, 1);
        if (type_theme == TYPE_THEME_RECIPE) {
            theme = (RecipeTheme) selectThemeBundle.getSerializable(PageArgumentKey.Theme);
//            setAvatorChange();
            LogUtils.i(TAG, "tHeme name:" + theme.name + "theme id:" + theme.id + " theme.subName: " + theme.subName + " theme.imageUrl:" + theme.imageUrl + "" + theme.description + " theme.viewCount:" + theme.viewCount + " theme collectCount:" + theme.collectCount);
            GlideApp.with(getActivity())
                    .load(theme.imageUrl)
                    .into(ivSelectThemeTitleBg);
            tvSelectThemeToolBarTitle.setText(theme.name);
//            tvThemeDesc.setText(theme.description);
//            if (percent < 0.8) {
//                imgFavority.setImageResource(theme.isCollect ? R.drawable.icon_collected : R.drawable.icon_collect);
//            } else {
//                imgFavority.setImageResource(theme.isCollect ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);
//            }
            imgFavority.setSelected(theme.isCollect);
            getThemeRecipeDetail(theme.id);
        } else if (type_theme == TYPE_THEME_DETAIL) {
            ThemeRecipeDetail themeRecipeDetail = (ThemeRecipeDetail) selectThemeBundle.getSerializable(PageArgumentKey.ThemeDetail);
//            setAvatorChange();
            GlideApp.with(getActivity())
                    .load(themeRecipeDetail.imageUrl)
                    .into(ivSelectThemeTitleBg);
            tvSelectThemeToolBarTitle.setText(themeRecipeDetail.name);
//            tvThemeDesc.setText(themeRecipeDetail.description);
            getThemeRecipeDetail(themeRecipeDetail.id);
        }else if (type_theme == TYPE_THEME_BANNER){
            long aLong = selectThemeBundle.getLong(PageArgumentKey.Id);
            getThemeRecipeDetail(aLong);
        }

    }


    public static void show(ThemeRecipeDetail themeRecipeDetail, int themeType) {
        Bundle bd = new Bundle();
        bd.putInt(PageArgumentKey.ThemeType, themeType);
        bd.putSerializable(PageArgumentKey.ThemeDetail, themeRecipeDetail);
        UIService.getInstance().postPage(PageKey.SelectThemeDetailPage, bd);
    }

    public static void show(RecipeTheme recipeTheme, int themeType) {
        Bundle bd = new Bundle();
        bd.putInt(PageArgumentKey.ThemeType, themeType);
        bd.putSerializable(PageArgumentKey.Theme, recipeTheme);
        UIService.getInstance().postPage(PageKey.SelectThemeDetailPage, bd);
    }
    public static void show(Long id, int themeType) {
        Bundle bd = new Bundle();
        bd.putInt(PageArgumentKey.ThemeType, themeType);
        bd.putLong(PageArgumentKey.Id, id);
        UIService.getInstance().postPage(PageKey.SelectThemeDetailPage, bd);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventUtils.postEvent(new PageBackEvent("SelectThemeDetailPage"));
        StatusBarUtils.setColor(cx, getResources().getColor(R.color.white));
        StatusBarUtils.setTextDark(cx, true);
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
                        ToastUtils.showShort("已取消收藏");
                        recipe.setIsCollected(false);
                        recipe.collectCount = recipe.collectCount - 1;
                        rvSelectThemeAdapter.setData(position, recipe);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.showShort(t.getMessage());

                    }
                });
            } else {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().addFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.showShort("收藏成功");
                        recipe.setIsCollected(true);
                        recipe.collectCount = recipe.collectCount + 1;
                        rvSelectThemeAdapter.setData(position, recipe);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.showShort(t.getMessage());
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

    /**
     * 获取专题下菜谱
     *
     * @param themeId
     */
    private void getThemeRecipeDetail(long themeId) {
        LogUtils.i(TAG, "getThemeRecipeDetail themeId:" + themeId);
        CookbookManager.getInstance().getThemeRecipeDetail(themeId, new Callback<Reponses.ThemeRecipeDetailResponse>() {
            @Override
            public void onSuccess(Reponses.ThemeRecipeDetailResponse themeRecipeDetailResponse) {
                if (themeRecipeDetailResponse != null && themeRecipeDetailResponse.themeRecipeDetail != null) {

                    if (theme==null){
                        theme=themeRecipeDetailResponse.themeRecipeDetail;
                    }
                    if (theme.isCollect){
                        theme = themeRecipeDetailResponse.themeRecipeDetail ;
                        theme.isCollect = true ;
                    }else {
                        theme = themeRecipeDetailResponse.themeRecipeDetail ;
                    }

                    LogUtils.i(TAG, "onSuccess theme viewCount:" + themeRecipeDetailResponse.themeRecipeDetail.viewCount + " theme collectCount:" + themeRecipeDetailResponse.themeRecipeDetail.collectCount);
//                    if (tvReadThemeNumber == null) {
//                        return;
//                    }
                    GlideApp.with(getActivity())
                            .load(themeRecipeDetailResponse.themeRecipeDetail.imageUrl)
                            .into(ivSelectThemeTitleBg);
                    tvSelectThemeToolBarTitle.setText(themeRecipeDetailResponse.themeRecipeDetail.name);
//                    tvThemeDesc.setText(themeRecipeDetailResponse.themeRecipeDetail.description);
//
//                    tvReadThemeNumber.setText("阅读 " + NumberUtil.converString(themeRecipeDetailResponse.themeRecipeDetail.viewCount));
//                    tvCollectThemeNumber.setText("收藏 " + NumberUtil.converString(themeRecipeDetailResponse.themeRecipeDetail.collectCount));
                   if (themeRecipeDetailResponse.themeRecipeDetail.recipeList!=null) {
                       recipeList.addAll(themeRecipeDetailResponse.themeRecipeDetail.recipeList);
//                       tvThemeRecipeNumber.setText(themeRecipeDetailResponse.themeRecipeDetail.recipeList.size() + "道菜谱");
                   }
                    rvSelectThemeAdapter.setThemeRecipeDetailResponse(themeRecipeDetailResponse);

                    setAvatorChange();
                    if (!Plat.accountService.isLogon()) {
                        setAdapterData();
                    } else {
                        //获取被收藏的菜谱
                        getCollectRecipe();
                        getThemeCollection();
                    }
                }
                srl_theme.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i(TAG, "onFail:" + t.toString());
                srl_theme.setRefreshing(false);
            }
        });
    }

    /**
     * 获取菜谱数据
     */
    protected void getCollectRecipe() {
        ProgressDialogHelper.setRunning(cx, true);
        CookbookManager.getInstance().getFavorityCookbooks(
                new Callback<Reponses.CookbooksResponse>() {
                    @Override
                    public void onSuccess(Reponses.CookbooksResponse result) {
                        ArrayList<AbsRecipe> absRecipes = new ArrayList<>();
                        if (result != null) {
                            if (result.cookbooks != null && result.cookbooks.size() != 0) {
                                absRecipes.addAll(result.cookbooks);
                            }
                            if (result.cookbooks3rd != null && result.cookbooks3rd.size() != 0) {
                                absRecipes.addAll(result.cookbooks3rd);
                            }
                        }
                        if (absRecipes.size() == 0) {
                            setAdapterData();
                        } else {
                            collectData(absRecipes);
                        }
                        ProgressDialogHelper.setRunning(cx, false);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.showShort(t.getMessage());
                    }
                });
    }

    private void collectData(ArrayList<AbsRecipe> absRecipes) {
        for (Recipe recipe :
                recipeList) {
            for (AbsRecipe absRecipe :
                    absRecipes) {
                if (recipe.id == absRecipe.id) {
                    recipe.collected = true;
                    break;
                }
            }
        }
        setAdapterData();
    }

    /**
     * 更新数据
     */
    private void setAdapterData() {
        rvSelectThemeAdapter.setList(recipeList);
        rvSelectThemeAdapter.setFooterView(footView);
    }


    @OnClick(R.id.tv_select_theme_back)
    public void selectThemeBack() {
        UIService.getInstance().popBack();
    }


    /**
     * 渐变toolbar背景
     */
    private void setAvatorChange() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset始终为0以下的负数
                percent = (Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange());
                LogUtils.i(TAG, "percent:" + percent);
                selectThemeToolbar.setBackgroundColor(changeAlpha(Color.WHITE, percent));
                if (percent > 0.8) {
                    tvSelectThemeToolBarTitle.setVisibility(View.VISIBLE);
                    StatusBarUtils.setTextDark(cx, true);
                    tvSelectThemeBack.setImageResource(R.mipmap.icon_back_black);
//                    imgFavority.setImageResource(R.drawable.ic_recipe_favority);
//                    imgFavority.setImageResource(theme.isCollect ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);

                    imgShare.setImageResource(R.mipmap.ic_recipe_detail_share);
                } else {
                    tvSelectThemeToolBarTitle.setVisibility(View.INVISIBLE);
                    StatusBarUtils.setTextDark(cx, false);
                    tvSelectThemeBack.setImageResource(R.mipmap.icon_back);
//                    imgFavority.setImageResource(R.drawable.ic_recipe_favority_shape);
//                    imgFavority.setImageResource(theme.isCollect ? R.drawable.icon_collected : R.drawable.animation_conmmion_dot);
                    imgShare.setImageResource(R.mipmap.icon_share);
                }
//                imgFavority.setSelected(theme.isCollect);

                if (percent < 0.8) {
                    imgFavority.setImageResource(theme.isCollect ? R.drawable.icon_collected : R.drawable.icon_collect);
                } else {
                    imgFavority.setImageResource(theme.isCollect ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);
                }

            }
        });
    }

    /**
     * @param color
     * @param fraction
     * @return
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }


    /**
     * 获取被收藏专题列表
     */
    private void getThemeCollection() {

        ProgressDialogHelper.setRunning(cx, true);
        StoreService.getInstance().getMyFavoriteThemeRecipeList(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> recipeThemes) {
                if (recipeThemes == null || recipeThemes.size() == 0) {
                    return;
                } else {
                    for (RecipeTheme recipeTheme :
                            recipeThemes) {
                        if (recipeTheme.id.equals(theme.id)) {
                            theme.isCollect = true ;
                            if (percent < 0.8) {
                                imgFavority.setImageResource(R.drawable.icon_collected);
                            } else {
                                imgFavority.setImageResource(com.robam.common.R.drawable.ic_baseline_favorite_24);
                            }
                            return;
                        }
                    }
                }
                theme.isCollect = false ;
                ProgressDialogHelper.setRunning(cx, false);
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    /**
     * 登录成功重新获取数据
     *
     * @param event
     */
    @Subscribe
    public void onEvent(UserLoginEvent event) {
        getThemeRecipeDetail(theme.id);
        getThemeCollection();
    }

    /**
     * 模拟从菜谱详情页返回
     *
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeDetailPage".equals(event.getPageName())) {
            recipeList.clear();
            getThemeRecipeDetail(theme.id);

        }
    }

}