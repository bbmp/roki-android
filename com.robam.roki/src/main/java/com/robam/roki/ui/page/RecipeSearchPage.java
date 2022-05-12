package com.robam.roki.ui.page;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.SoftInputUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.TagValue;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.DialogAdapter;
import com.robam.roki.ui.adapter.RecipeAdapter;
import com.robam.roki.ui.adapter3.CustomLoadMoreView;
import com.robam.roki.ui.adapter3.RvRecipeSearchHeadAdapter;
import com.robam.roki.ui.adapter3.RvRecipeThemeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.EmojiEmptyView;
import com.robam.roki.ui.view.RecipeGridView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author wwq
 * des：搜索成功页
 */
public class RecipeSearchPage extends MyBasePage<MainActivity> {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.imgSearch)
    ImageView mImgSearch;
    @InjectView(R.id.layoutSearch)
    LinearLayout mLayoutSearch;
    private List<AbsRecipe> absRecipes;
    private int count;
    private static final String TAG = "RecipeSearchPage";
    private String cancelSearchText;

    /**
     * 搜索成功菜谱的adapter
     */
    private RvRecipeSearchHeadAdapter rvRecipeSearchHeadAdapter;
    /**
     * 搜索成功recycleView
     */
    private RecyclerView rvSearchRecipeHead;
    /**
     * 大家都在做文字显示
     */
    private TextView tvMostPeople;
    /**
     * 空数据提示
     */
    EmojiEmptyView emptyView;
    /**
     * 大家都在做菜谱列表
     */
    private RvRecipeThemeAdapter rvRecipeThemeAdapter;
    private String come_from;

    public static void show() {
        UIService.getInstance().postPage(PageKey.RecipeSearch);
    }

    @InjectView(R.id.edtSearch)
    EditText edtSearch;

    @InjectView(R.id.iv_cancel)
    ImageView ivCancel;
    /**
     * 数据列表
     */
    @InjectView(R.id.rv_search_recipe)
    RecyclerView rvSearchRecipe;
    List<Recipe> recipeList;
    List<Recipe3rd> recipe3rds;
    View view;
    private String searchWord = "";
    private TagValue.TagsTagBean tagRecipe;
    private RecipeAdapter mRecipeAdapter;

    private int pageNo = 0;
    private int pageSize = 13;


    @Override
    protected int getLayoutId() {
        return R.layout.page_recipe_search;
    }

    @Override
    protected void initView() {
        //大家都在做
//        StatusBarUtils.setTextDark(cx , true);
//        StatusBarUtils.setTransparent(cx);
        rvSearchRecipe.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));

        rvRecipeThemeAdapter = new RvRecipeThemeAdapter();
        rvRecipeThemeAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        rvRecipeThemeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadRecipeData();
            }
        });
        rvSearchRecipe.setAdapter(rvRecipeThemeAdapter);

        //搜索成功列表数据
        View head_view = LinearLayout.inflate(cx, R.layout.head_recipe_search, null);
        rvSearchRecipeHead = (RecyclerView)head_view.findViewById(R.id.rv_search_recipe_head);
        emptyView = (EmojiEmptyView)head_view.findViewById(R.id.emptyView);
        tvMostPeople = (TextView)head_view.findViewById(R.id.tv_most_people);
        rvSearchRecipeHead = (RecyclerView)head_view.findViewById(R.id.rv_search_recipe_head);
        rvSearchRecipeHead.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        rvRecipeSearchHeadAdapter = new RvRecipeSearchHeadAdapter();
        rvRecipeSearchHeadAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Recipe item = (Recipe)adapter.getItem(position);
                RecipeDetailPage.show(item.id, item.sourceType);

            }
        });
        rvSearchRecipeHead.setAdapter(rvRecipeSearchHeadAdapter);
        rvRecipeThemeAdapter.addHeaderView(head_view);
    }



    @Override
    protected  void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            searchWord = bundle.getString(PageArgumentKey.text);
            come_from = bundle.getString(PageArgumentKey.come_from);
            onSearchWord(searchWord);
        }
        edtSearch.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                        if (v.getText() != null) {
                                                            searchWord = v.getText().toString() ;
                                                            onSearchWord(searchWord);//文字自动搜索
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            }
        );
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                LogUtils.i("20190905", "s:" + s + " start:" + start + " count:" + count + " after:" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtils.i("20190905", "s:" + s + " start:" + start + " before:" + before + " count:" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    cancelSearchText = cx.getText(R.string.search).toString();
                } else {
                    cancelSearchText = cx.getText(R.string.cancel).toString();
                }
            }
        });

        toggleSoftInput();
        loadRecipeData();
    }

    /**
     * 搜索时的界面控制开关
     *
     * @param isEmpty 搜索的菜是否时空的
     */
    void switchView(boolean isEmpty) {
        try {
            emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            if (isEmpty){
                String emptyMessage = "ROKI没有找到‘" + searchWord + "’,换个关键词试试";

                SpannableString spannableString1 = new SpannableString(emptyMessage);
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                        0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB7432")),
                        8, emptyMessage.length() - 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                        emptyMessage.length() - 8, emptyMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                emptyView.setText(spannableString1);
            }

        } catch (Exception e) {
//            UIService.getInstance().returnHome();
            LogUtils.i(TAG, "Exception e " + e.toString());
        }
    }

    /**
     * 调用键盘
     */
    private void toggleSoftInput() {
        SoftInputUtils.show(cx, edtSearch);
    }

    /**
     * 搜索时触发
     * @param word 搜索时传入的内容
     */
    void onSearchWord(String word) {
        boolean isSearch = !Strings.isNullOrEmpty(word);
        if (isSearch) {
            edtSearch.setText(word);
            edtSearch.setSelection(word.length());
//            CookbookManager.getInstance().saveHistoryKeysForCookbook(word);
            ProgressDialogHelper.setRunning(cx, true);

            CookbookManager.getInstance().getCookbooksBy(word , "SpeechRecipePage".equals(come_from) , new Callback<Reponses.CookbooksResponse>() {
                @Override
                public void onSuccess(Reponses.CookbooksResponse result) {
                    try {
                        count = result.count();
                        ProgressDialogHelper.setRunning(cx, false);
                        recipeList = result.cookbooks;
                        recipe3rds = result.cookbooks3rd;
                        loadBooks(RecipeGridView.Model_Search);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    ProgressDialogHelper.setRunning(cx, false);
                    ToastUtils.showThrowable(t);
                }
            });
        }
    }

    protected void loadBooks(int modelType) {

            List<AbsRecipe> absAbsRecipes = new ArrayList<>();
            absAbsRecipes.addAll(recipeList);
            absAbsRecipes.addAll(recipe3rds);

        if (absAbsRecipes.size() == 0){
            switchView(true);
        }else {
            rvRecipeSearchHeadAdapter.setList(absAbsRecipes);
            rvSearchRecipe.scrollToPosition(0);
            rvSearchRecipeHead.scrollToPosition(0);
            switchView(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_cancel)
    public void onMTxtCancelClicked() {
        if (TextUtils.isEmpty(edtSearch.getText())) {
            return;
        }
        edtSearch.setText("");
    }

    /**
     * 主题列表
     */
    private List<RecipeTheme> recipeThemeList = new ArrayList<>();

    /**
     * 展示条目列表
     */
    private List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList = new ArrayList<>();

    /**
     * 获取大家都在做菜谱数据
     */
    private void loadRecipeData() {
        LogUtils.i(TAG, "pageNo:" + pageNo + " pageSize:" + pageSize);
        RokiRestHelper.getbyTagOtherCooks(null, true, pageNo, pageSize, -1, null,
                Reponses.PersonalizedRecipeResponse.class, new RetrofitCallback<Reponses.PersonalizedRecipeResponse>() {
                    @Override
                    public void onSuccess(Reponses.PersonalizedRecipeResponse personalizedRecipeResponse) {
                        if (null != personalizedRecipeResponse) {
                            List<Recipe> recipes = personalizedRecipeResponse.cookbooks;
                            if (recipes.isEmpty() || recipes.size() == 0 ) {
                                LogUtils.i(TAG, " recipes isEmpty!");
                                if(pageNo == 0){

                                }else {
                                    rvRecipeThemeAdapter.getLoadMoreModule().loadMoreEnd();
                                }
                            }else {
                                themeRecipeMultipleItemList.clear();
                                for (Recipe recipe : recipes) {
                                    themeRecipeMultipleItemList.add(new ThemeRecipeMultipleItem(ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT, recipe));
                                }
                                getByTagOtherThemes();
                            }
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        ToastUtils.show("请求数据失败", Toast.LENGTH_LONG);
                    }
        });
    }

    /**
     */
    private void getByTagOtherThemes() {
//        Long cookBookTagId = tagRecipe.getCookbookTagId() == null ? null : (Long) (tagRecipe.getCookbookTagId());
        RokiRestHelper.getByTagOtherThemes(null, false, pageNo, 1, -1,
                Reponses.RecipeThemeResponse.class, new RetrofitCallback<Reponses.RecipeThemeResponse>() {
                    @Override
                    public void onSuccess(Reponses.RecipeThemeResponse recipeThemeResponse) {
                        if (null != recipeThemeResponse && null != recipeThemeResponse.items) {
                            recipeThemeList.addAll(recipeThemeResponse.items) ;
                            pageNo ++ ;
                            if (recipeThemeList.isEmpty() || recipeThemeList.size() == 0) {
                                rvRecipeThemeAdapter.addData(themeRecipeMultipleItemList);
                            }else {
                                int i1 = (int)((10 +Math.random()*(12-10 +1)) );
                                if (i1 < themeRecipeMultipleItemList.size()){
                                    themeRecipeMultipleItemList.add(i1 ,new ThemeRecipeMultipleItem(ThemeRecipeMultipleItem.IMG_THEME_RECIPE_MSG_TEXT, recipeThemeList.get(0)));
                                    recipeThemeList.remove(0);
                                }
                                rvRecipeThemeAdapter.addData(themeRecipeMultipleItemList);
                                rvRecipeThemeAdapter.getLoadMoreModule().loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        rvRecipeThemeAdapter.getLoadMoreModule().loadMoreFail();
                    }
        });

    }

    @Override
    protected void setStateBarFixer() {
//        super.setStateBarFixer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventUtils.postEvent(new PageBackEvent("RecipeSearchPage"));
    }

    /**
     * 模拟从专题详情页返回
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("RecipeDetailPage".equals(event.getPageName())){
            super.setStateBarFixer2();
        }
    }
}
