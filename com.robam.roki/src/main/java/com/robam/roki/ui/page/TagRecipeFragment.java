package com.robam.roki.ui.page;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.legent.Callback;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.TagValue;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.ui.adapter3.CustomLoadMoreView;
import com.robam.roki.ui.adapter3.RvRecipeThemeAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author r210190
 */
public class TagRecipeFragment extends MyBasePage<MainActivity> {
    private static final String TAG = "TagRecipeFragment";
    private RecyclerView mRecyclerView;
    private TagValue.TagsTagBean tagRecipe;
    /**
     * 菜谱标签
     */
    public static final String TAG_RECIPE = "tagsTagRecipe";
    private int pageNo = 0;
    private int pageSize = 13;
    private List<RecipeTheme> recipeThemeList = new ArrayList<>();
    private RvRecipeThemeAdapter rvRecipeThemeAdapter;



    public static TagRecipeFragment newInstance(TagValue.TagsTagBean tagsTagBean) {
        TagRecipeFragment mFragment = new TagRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG_RECIPE, tagsTagBean);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.rv_tag_recipe_list);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        rvRecipeThemeAdapter = new RvRecipeThemeAdapter();
        mRecyclerView.setAdapter(rvRecipeThemeAdapter);
        rvRecipeThemeAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        rvRecipeThemeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadRecipeData();
            }
        });
    }

    @Override
    protected void initData() {
        tagRecipe = getArguments().getParcelable(TAG_RECIPE);
        getByTagOtherThemes();
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        tagRecipe = getArguments().getParcelable(TAG_RECIPE);
//        mRecyclerView = view.findViewById(R.id.rv_tag_recipe_list);
//        initRecyclerView();
////        loadRecipeData();
//        getByTagOtherThemes();
//    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        rvRecipeThemeAdapter = new RvRecipeThemeAdapter();
        mRecyclerView.setAdapter(rvRecipeThemeAdapter);
        rvRecipeThemeAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
        rvRecipeThemeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadRecipeData();
            }
        });

    }

    /**
     * 加载菜谱数据
     */
    private void loadRecipeData() {
        Long cookBookTagId = tagRecipe.getCookbookTagId() == null ? null : (Long) (tagRecipe.getCookbookTagId());
        LogUtils.i(TAG, "pageNo:" + pageNo + " pageSize:" + pageSize + " cookBookTagId:" + cookBookTagId + " type:" + tagRecipe.getType());
        RokiRestHelper.getbyTagOtherCooks(cookBookTagId, false, pageNo, pageSize, tagRecipe.getType(), null,
                Reponses.PersonalizedRecipeResponse.class, new RetrofitCallback<Reponses.PersonalizedRecipeResponse>() {
                    @Override
                    public void onSuccess(Reponses.PersonalizedRecipeResponse personalizedRecipeResponse) {
                        if (null != personalizedRecipeResponse) {
                            List<Recipe> recipes = personalizedRecipeResponse.cookbooks;
                            if (recipes == null || recipes.size() == 0 ) {
                                LogUtils.i(TAG, " recipes isEmpty!");
                                rvRecipeThemeAdapter.getLoadMoreModule().loadMoreEnd();
                            }else {
                                List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList = new ArrayList<>();
                                for (Recipe recipe : recipes) {
                                    themeRecipeMultipleItemList.add(new ThemeRecipeMultipleItem(ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT, recipe));
                                }
                                settingData(themeRecipeMultipleItemList);
                            }
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        rvRecipeThemeAdapter.getLoadMoreModule().loadMoreFail();
                    }
        });
    }

    /**
     * 获取某个标签或推荐或周上新的主题
     */
    public void getByTagOtherThemes() {
        Long cookBookTagId = tagRecipe.getCookbookTagId() == null ? null : (Long) (tagRecipe.getCookbookTagId());
        RokiRestHelper.getByTagOtherThemes(cookBookTagId, false, 0, 30, tagRecipe.getType(),
                Reponses.RecipeThemeResponse.class, new RetrofitCallback<Reponses.RecipeThemeResponse>() {
                    @Override
                    public void onSuccess(Reponses.RecipeThemeResponse recipeThemeResponse) {
                        if (null != recipeThemeResponse && null != recipeThemeResponse.items) {
                            recipeThemeList.addAll(recipeThemeResponse.items) ;
                            loadRecipeData();
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        rvRecipeThemeAdapter.getLoadMoreModule().loadMoreFail();
                    }
        });

    }

    private void settingData(List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList){
        if (recipeThemeList == null || recipeThemeList.size() == 0) {
            rvRecipeThemeAdapter.addData(themeRecipeMultipleItemList);
        }else {
//            int i1 = (int)((10 +Math.random()*(11-10 +1)) ) ;
//            for (int i = 0 ; i < themeRecipeMultipleItemList.size() ; i++){
//                if (recipeThemeList.size() != 0){
//                    int i1 = (i + 1) * 3 - 1;
//                    int i1 =  new Random().nextInt(9) + 5;
                     int i1 = (int)((10 +Math.random()*(12-10 +1)) );
                    if (i1 < themeRecipeMultipleItemList.size()){
                        themeRecipeMultipleItemList.add(i1 ,new ThemeRecipeMultipleItem(ThemeRecipeMultipleItem.IMG_THEME_RECIPE_MSG_TEXT, recipeThemeList.get(0)));
                        recipeThemeList.remove(0);
                    }
//                    else {
//                        break;
//                    }
//                }
//                else {
//                    break;
//                }
//            }
            rvRecipeThemeAdapter.addData(themeRecipeMultipleItemList);
        }
        pageNo++ ;
        rvRecipeThemeAdapter.getLoadMoreModule().loadMoreComplete();
    }

    public void selectPage(){
        if (rvRecipeThemeAdapter != null){
            pageNo = 0 ;
            rvRecipeThemeAdapter.setList(new ArrayList<>());
            getByTagOtherThemes();
        }

    }
}
