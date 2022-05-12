package com.robam.roki.ui.view;

import static com.robam.roki.ui.activity3.ActivityWebViewPage.kitchenKnowledge_url;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeTheme;

import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.model.bean.ThemeRecipeMultipleItem;
import com.robam.roki.model.bean.TopicMultipleItem;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.RWebActivity;
import com.robam.roki.ui.adapter.RecipeTopicAdapter;
import com.robam.roki.ui.adapter.SelectedTopicsAdapter;
import com.robam.roki.ui.adapter3.CustomLoadMoreHomeView;
import com.robam.roki.ui.adapter3.MBannerAdapter;
import com.robam.roki.ui.adapter3.RvRecipeThemeAdapter;
import com.robam.roki.ui.bean3.BannerBean;
import com.robam.roki.ui.helper3.MyIndicator;
import com.robam.roki.ui.helper3.UploadFileHelper;

import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.SelectThemeDetailPage;
import com.robam.roki.ui.page.mine.MyBaseView;
import com.youth.banner.Banner;
import com.youth.banner.indicator.BaseIndicator;
import com.youth.banner.indicator.CircleIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wwq
 * des：美食频道页
 */
public class HomeRecipeView32 extends MyBaseView {

    private static final String TAG = "HomeRecipeView3";

    private EditText etReipeSearch;
    private ImageView ivRecipeVoice;
    private View rl_recipe_search;
    /**
     * 回到顶部
     */
    private View ivToTop;
    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout srlHome;
    /**
     *
     * 灶具
     */
    private View stove;
    private View combiSteamOven;
    private View oven;
    private View steamOven;
    private View more;
    /**
     * 更多
     */
    private View tvThemeMore;
    private View tvWeekTopMore;
    private View tvmore3 ;
    /**
     * 精选专题
     */
    RecyclerView rvSelectedTopics;
    /**
     * 美食top榜单
     */
    RecyclerView rvWeekTopics;
    /**
     * 美食主页recycleView
     */
    private RecyclerView rvHomeRecipe ;
    /**
     * 猜你喜欢adapter
     */
    private RvRecipeThemeAdapter rvRecipeThemeAdapter;
    /**
     * 精选专题Adapter
     */
    private SelectedTopicsAdapter mSelectedTopicsAdapter;
    /**
     * Top菜谱adapter
     */
    private RecipeTopicAdapter mRecipeTopicAdapter;
    /**
     * 菜谱和专题数据
     */
    private List<RecipeTheme> recipeThemeList = new ArrayList<>();
    /**
     * 菜谱数据 用于排除
     */
    private ArrayList<Recipe> recipeDatas = new ArrayList<>();
    /**
     * 页数
     */
    private int pageNo = 0;
    private Banner banner;
    private RecyclerView rvFootHome;

    public HomeRecipeView32(Context context) {
        super(context);
//        this.context = context;
//        initView();
    }

    public HomeRecipeView32(Context context, FragmentActivity activity) {
        super(context, activity);
//        this.context = context1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_home_recipe_new3_2;
    }

    @Override
    protected void initView() {
//        StatusBarUtils.setColor(cx, Color.WHITE);
        rvHomeRecipe = findViewBy(R.id.rv_home_recipe);
        srlHome = findViewBy(R.id.srl_home);
        etReipeSearch = findViewBy(R.id.et_recipe_search);
        rl_recipe_search = findViewBy(R.id.rl_recipe_search);
        ivRecipeVoice = findViewBy(R.id.iv_recipe_voice);
        ivToTop = findViewBy(R.id.iv_to_top);
        rvHomeRecipe.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        RvRecipeThemeAdapter rvRecipeThemeAdapter2 = new RvRecipeThemeAdapter();
        rvHomeRecipe.setAdapter(rvRecipeThemeAdapter2);
//        rvRecipeThemeAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreHomeView());
//        rvRecipeThemeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                loadRecipeData();
//            }
//        });
        View topView = activity.getLayoutInflater().inflate( R.layout.view_home_recipe_itme_top, null);
        rvRecipeThemeAdapter2.addHeaderView(topView);

        View footView = activity.getLayoutInflater().inflate( R.layout.view_home_recipe_itme_foot, null);
        rvRecipeThemeAdapter2.addFooterView(footView);

        banner = (Banner)topView.findViewById(R.id.br_home);


        stove = topView.findViewById(R.id.home_recipe_ll_stove);
        combiSteamOven = topView.findViewById(R.id.home_recipe_ll_combi_steam_oven);
        oven = topView.findViewById(R.id.home_recipe_ll_oven);
        steamOven = topView.findViewById(R.id.home_recipe_ll_steam_oven);
        more = topView.findViewById(R.id.home_recipe_more);
        tvmore3 = topView.findViewById(R.id.tv_more_3);
        tvThemeMore = topView.findViewById(R.id.tv_theme_more);
        tvWeekTopMore = topView.findViewById(R.id.tv_week_top_more);

        rvSelectedTopics = topView.findViewById(R.id.rv_selected_topics);
        rvSelectedTopics.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvWeekTopics = topView.findViewById(R.id.rv_week_topics);
        rvWeekTopics.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        setOnClickListener(rl_recipe_search,etReipeSearch , ivRecipeVoice ,stove , combiSteamOven , oven , steamOven , more ,tvThemeMore ,tvWeekTopMore ,ivToTop , tvmore3);

        rvFootHome = footView.findViewById(R.id.rv_home_foot);
        rvFootHome.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        rvRecipeThemeAdapter = new RvRecipeThemeAdapter();
        rvFootHome.setAdapter(rvRecipeThemeAdapter);
        rvRecipeThemeAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreHomeView());
        rvRecipeThemeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadRecipeData();
            }
        });


        rvHomeRecipe.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mmRvScrollY = 0; // 列表滑动距离
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                //判断是当前layoutManager是否为LinearLayoutManager
//                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                if (layoutManager instanceof StaggeredGridLayoutManager) {
//                    StaggeredGridLayoutManager linearManager = (StaggeredGridLayoutManager) layoutManager;
//                    //获取第一个可见view的位置
////                    int firstItemPosition = linearManager.();
//                    //获取最后一个可见view的位置
//                    int[] lastVisibleItemPositions = linearManager.findLastVisibleItemPositions(new int[linearManager.getSpanCount()]);
//                    if (lastVisibleItemPositions[lastVisibleItemPositions.length-1] >= 10){
//                        ivToTop.setVisibility(VISIBLE);
//                    }else {
//                        ivToTop.setVisibility(GONE);
//                    }
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mmRvScrollY += dy;
                LogUtils.i("20220323","mmRvScrollY:"+mmRvScrollY);
                if (mmRvScrollY >= 4500){
                    ivToTop.setVisibility(VISIBLE);
                }else {
                    ivToTop.setVisibility(GONE);
                }
            }
        });

        srlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0 ;
                rvRecipeThemeAdapter.setNewInstance(new ArrayList<ThemeRecipeMultipleItem>());
                getBannerRes();
                getByTagOtherThemes();
                getThemeHttpData();
            }
        });

    }

    @Override
    protected void initData() {
        getBannerRes();
        getByTagOtherThemes();
        getWeekRecipeTops();
        getThemeHttpData();
    }

    @Override
    public void onClick(View view) {
        if (view== etReipeSearch || view == rl_recipe_search){
            UIService.getInstance().postPage(PageKey.RecipeSearch2);
        }else if ( view == ivRecipeVoice){
            UIService.getInstance().postPage(PageKey.SpeechRecipePage);
        }else if (view == stove){
            recipeCategoryClick(DeviceType.RRQZ);
        }else if (view == combiSteamOven){
            recipeCategoryClick(DeviceType.RZKY);
        }else if (view == oven){
            recipeCategoryClick(DeviceType.RDKX);
        }else if (view == steamOven){
            recipeCategoryClick(DeviceType.RZQL);
        }else if (view == more){
            UIService.getInstance().postPage(PageKey.RecipeClassify);
        }else if (view == tvThemeMore){
            UIService.getInstance().postPage(PageKey.ThemeRecipeListPage);
        }else if (view == tvWeekTopMore){
            UIService.getInstance().postPage(PageKey.TopWeekPage);
        }else if (view == ivToTop){
            rvHomeRecipe.smoothScrollToPosition(0);
//            rvHomeRecipe.scrollToPosition(0);
            ivToTop.setVisibility(GONE);
        }else if (view == tvmore3){
            UIService.getInstance().postPage(PageKey.RecipeClassify);
        }
    }

    /**
     * 设备菜谱点击调用(可提供外部调用)
     */
    public static void recipeCategoryClick(String category) {
        boolean connect = NetworkUtils.isConnect(MobApp.getInstance());
        if (!connect){
            ToastUtils.showLong("当前网络不可用，请检查网络连接");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(PageArgumentKey.RecipeId, category);
        UIService.getInstance().postPage(PageKey.RecipeCategoryList, bundle);
    }

    /**
     * 获取banner资源图片
     */
    private void getBannerRes(){
        UploadFileHelper.getBanner(new Callback<BannerBean>() {
            @Override
            public void onSuccess(BannerBean bannerBean) {
                if (bannerBean != null && bannerBean.data != null && bannerBean.data.size() != 0){
                    MBannerAdapter mBannerAdapter = new MBannerAdapter(bannerBean.data);
                    mBannerAdapter.addOnItemClicklinstener(new MBannerAdapter.OnItemClicklinstener() {
                        @Override
                        public void onItemClick(BannerBean.DataDTO item) {
                            int linkAction = item.linkAction;
                            switch (linkAction){//1小游戏  2精选专题  3动态菜谱  4静态菜谱  5H5页面 6小程序  7 抽奖H5
                                case 1:
                                    UIService.getInstance().postPage(PageKey.RandomRecipe);
                                    break;
                                case 2:
                                    try {
                                        if (item.resource != null){
//                                        SelectThemeDetailPage.show(recipeThemes.get(position), SelectThemeDetailPage.TYPE_THEME_RECIPE);
                                            Bundle bd = new Bundle();
                                            bd.putLong(PageArgumentKey.Id, Long.parseLong(item.resource));
                                            bd.putInt(PageArgumentKey.ThemeType, 3);
                                            UIService.getInstance().postPage(PageKey.SelectThemeDetailPage , bd);
                                        }
                                    }catch (Exception e){
                                        e.getMessage();
                                        ToastUtils.show("专题ID错误");
                                    }
                                    break;
                                case 3:
                                case 4:
                                    try {
                                        if (item.resource != null){
                                            RecipeDetailPage.show( Long.parseLong(item.resource), 1);
                                        }
                                    }catch (Exception e){
                                        ToastUtils.show("菜谱ID错误");
                                    }
                                    break;
                                case 5:
                                    if (item.resource != null){
                                        if(item.resource.equals(kitchenKnowledge_url)){
                                            RWebActivity.start(activity , item.resource);
                                        }else {
                                            Bundle bd = new Bundle();
                                            bd.putString(PageArgumentKey.title, item.title);
                                            bd.putString(PageArgumentKey.Url, item.resource);
                                            bd.putString("Share_Img",item.imageUrl);
                                            bd.putString(PageArgumentKey.H5Key, "common_act");
                                            UIService.getInstance().postPage(PageKey.ActivityWebViewPage,bd);
                                        }

                                    }else {
//                                        ToastUtils.show("");
                                    }
                                    //跳转小程序
                                    case 6:
                                    if (item.resource != null){

                                    }else {
//                                        ToastUtils.show("");
                                    }
                                    break;
                                //awe抽奖活动
                                case 7:
                                    if (item.resource != null){
                                        Bundle bd = new Bundle();
                                        bd.putString(PageArgumentKey.title, item.title);
                                        bd.putString(PageArgumentKey.Url, item.resource);
                                        bd.putString("Share_Img",item.imageUrl);
                                        bd.putString(PageArgumentKey.H5Key, "special_act");
                                        UIService.getInstance().postPage(PageKey.ActivityWebViewPage,bd);
                                    }else {
//                                        ToastUtils.show("");
                                    }

                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    banner.setAdapter(mBannerAdapter)
                            .addBannerLifecycleObserver(activity)
                            .setIndicator(new MyIndicator(activity));

                }else {
                    banner.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    /**
     * 获取某个标签或推荐或周上新的主题
     */
    public void getByTagOtherThemes() {
        RokiRestHelper.getByTagOtherThemes(null, false, pageNo, 10, -1,
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
                        srlHome.setRefreshing(false);
                    }
        });
    }

    /**
     * 加载菜谱数据
     */
    private void loadRecipeData() {
        if (pageNo >= 10){
            rvRecipeThemeAdapter.getLoadMoreModule().loadMoreEnd();
            return;
        }
        RokiRestHelper.getbyTagOtherCooks(null , true, pageNo, 10, -1 , getIds(recipeDatas),
                Reponses.PersonalizedRecipeResponse.class, new RetrofitCallback<Reponses.PersonalizedRecipeResponse>() {
                    @Override
                    public void onSuccess(Reponses.PersonalizedRecipeResponse personalizedRecipeResponse) {
                        if (null != personalizedRecipeResponse) {
                            List<Recipe> recipes = personalizedRecipeResponse.cookbooks;
                            if (recipes == null || recipes.size() == 0 ) {
                                LogUtils.i(TAG, " recipes isEmpty!");
                                rvRecipeThemeAdapter.getLoadMoreModule().loadMoreEnd();
                            }else {
                                recipeDatas.addAll(recipes);
                                List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList = new ArrayList<>();
                                for (Recipe recipe : recipes) {
                                    themeRecipeMultipleItemList.add(new ThemeRecipeMultipleItem(ThemeRecipeMultipleItem.IMG_RECIPE_MSG_TEXT, recipe));
                                }
                                settingData(themeRecipeMultipleItemList);
                            }
                            srlHome.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        rvRecipeThemeAdapter.getLoadMoreModule().loadMoreFail();
                        srlHome.setRefreshing(false);
                    }
        });
    }

    /**
     * 设置菜谱数据
     * @param themeRecipeMultipleItemList
     */
    private void settingData(List<ThemeRecipeMultipleItem> themeRecipeMultipleItemList){
        if (recipeThemeList == null || recipeThemeList.size() == 0) {
            rvRecipeThemeAdapter.addData(themeRecipeMultipleItemList);
        }else {

            int i1 = (int)((7 +Math.random()*(3)) );
            if (i1 < themeRecipeMultipleItemList.size()){
                themeRecipeMultipleItemList.add(i1 ,new ThemeRecipeMultipleItem(ThemeRecipeMultipleItem.IMG_THEME_RECIPE_MSG_TEXT, recipeThemeList.get(0)));
                recipeThemeList.remove(0);
            }

            rvRecipeThemeAdapter.addData(themeRecipeMultipleItemList);
        }
        pageNo++ ;
        rvRecipeThemeAdapter.getLoadMoreModule().loadMoreComplete();
    }


    /**
     * 获取上周榜单菜谱数据
     */
    private void getWeekRecipeTops() {
        String weekTime = TimeUtils.getlastWeekTime();
        LogUtils.i(TAG, "weekTime:" + weekTime);
        RokiRestHelper.getWeekTops(weekTime, 0, 5, Reponses.WeekTopsResponse.class, new RetrofitCallback<Reponses.WeekTopsResponse>() {
            @Override
            public void onSuccess(Reponses.WeekTopsResponse weekTopsResponse) {
                if (null != weekTopsResponse && null != weekTopsResponse.payload) {
                    LogUtils.i(TAG, "getWeekTops onSuccess");
                    List<Recipe> recipes = weekTopsResponse.payload;

                    for (Recipe recipe : recipes) {
                        LogUtils.i(TAG, "imgLarge:" + recipe.imgLarge);
                        recipe.setItemType(Recipe.IMG);
                    }
                    recipes.add(new Recipe(Recipe.TEXT));
                    mRecipeTopicAdapter = new RecipeTopicAdapter(recipes);
                    rvWeekTopics.setAdapter(mRecipeTopicAdapter);
                    mRecipeTopicAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                            LogUtils.i(TAG, "recipes.size:" + recipes.size() + " position:" + position);
                            if (recipes.size() == position + 1) {
                                UIService.getInstance().postPage(PageKey.TopWeekPage);
                            } else {
                                RecipeDetailPage.show(recipes.get(position).id, recipes.get(position).sourceType);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFaild(String err) {
                LogUtils.i(TAG, "onFailure " + err);
            }
        });
    }

    /**
     * 精选专题列表
     */
    private void getThemeHttpData() {
        RokiRestHelper.getThemeRecipeList(Reponses.RecipeThemeResponse.class, new RetrofitCallback<Reponses.RecipeThemeResponse>() {
            @Override
            public void onSuccess(Reponses.RecipeThemeResponse recipeThemeResponse) {
                if (null != recipeThemeResponse) {
                    if (Plat.accountService.isLogon()) {
                        getThemeCollection(recipeThemeResponse.items);
                        return;
                    }
                    //设置精选专题
                    setTheme(recipeThemeResponse.items);
                }
            }

            @Override
            public void onFaild(String err) {

            }
        });
    }

    /**
     * 获取被收藏专题列表
     */
    private void getThemeCollection(List<RecipeTheme> themes) {

//        ProgressDialogHelper.setRunning(cx, true);
        StoreService.getInstance().getMyFavoriteThemeRecipeList(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> recipeThemes) {
                if (recipeThemes != null && recipeThemes.size() != 0) {
                    setTheme(themeCollection(themes, recipeThemes));
                } else {
                    setTheme(themes);
                }
            }

            @Override
            public void onFailure(Throwable t) {

//                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    /**
     * 专题列表数据和收藏列表数据合并处理
     */
    private List<RecipeTheme> themeCollection(List<RecipeTheme> themes, List<RecipeTheme> recipeThemes) {
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
        return themes;
    }

    /**
     * 设置精选专题
     * @param recipeThemes
     */
    private void  setTheme(List<RecipeTheme> recipeThemes){

//        List<String> imgUrlList = new ArrayList<>();
//        if (recipeThemes == null) {
//            rlSelectThemeList.setVisibility(GONE);
//            return;
//        }
        List<TopicMultipleItem> topicMultipleItemList = new ArrayList<TopicMultipleItem>() ;
        for (RecipeTheme recipeTheme : recipeThemes) {
            if (topicMultipleItemList.size() < 5) {
//                imgUrlList.add(recipeTheme.imageUrl);
                topicMultipleItemList.add(new TopicMultipleItem(TopicMultipleItem.IMG, TopicMultipleItem.IMG_SPAN_SIZE, recipeTheme.imageUrl));
            } else {
                break;
            }
        }

        topicMultipleItemList.add(new TopicMultipleItem(TopicMultipleItem.TEXT, TopicMultipleItem.IMG_SPAN_SIZE, "加载更多"));
        mSelectedTopicsAdapter = new SelectedTopicsAdapter(topicMultipleItemList);
        rvSelectedTopics.setAdapter(mSelectedTopicsAdapter);
        mSelectedTopicsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (position == topicMultipleItemList.size() - 1) {
                    UIService.getInstance().postPage(PageKey.ThemeRecipeListPage);
                } else {
                    SelectThemeDetailPage.show(recipeThemes.get(position), SelectThemeDetailPage.TYPE_THEME_RECIPE);
                }
            }
        });
    }

    private ArrayList<Long> getIds(ArrayList<Recipe> recipes){
        ArrayList<Long> ids = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ids.add(recipe.getID());
        }
        return ids ;
    }
    /**
     * 模拟从专题详情页返回
     *
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if (
                "SelectThemeDetailPage".equals(event.getPageName()) ||
                        "RecipeCategory".equals(event.getPageName())
                        || "RecipeSearchPage".equals(event.getPageName())
//                        || "RecipeSearch2Page".equals(event.getPageName())
                        || "RecipeDetailPage".equals(event.getPageName())
        ) {
            if (UIService.getInstance().isHome()) {
                if (Build.VERSION.SDK_INT >= 21) {
                    View decorView = activity.getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                    StatusBarUtils.setTextDark(getContext(), true);
                }
//                getThemeHttpData();
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            //true,则拦截所有点击事件，按钮的点击事件不会被执行
            return true;
        }
        return false;
    }

}
