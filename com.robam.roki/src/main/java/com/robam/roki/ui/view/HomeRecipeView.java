package com.robam.roki.ui.view;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.legent.utils.security.MD5Utils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeConsultation;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.liveshow;
import com.robam.common.services.CookbookManager;
import com.robam.common.ui.UiHelper;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.dialog.RecipeSearchDialog;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.view.recipeclassify.GlideImageLoader;
import com.robam.roki.utils.ListUtils;
import com.robam.roki.utils.ToolUtils;
//import com.youth.banner.Banner;
//import com.youth.banner.listener.OnBannerClickListener;
//import com.youth.banner.transformer.AccordionTransformer;
//import com.youth.banner.transformer.BackgroundToForegroundTransformer;
//import com.youth.banner.transformer.CubeInTransformer;
//import com.youth.banner.transformer.CubeOutTransformer;
//import com.youth.banner.transformer.DefaultTransformer;
//import com.youth.banner.transformer.DepthPageTransformer;
//import com.youth.banner.transformer.FlipHorizontalTransformer;
//import com.youth.banner.transformer.FlipVerticalTransformer;
//import com.youth.banner.transformer.ForegroundToBackgroundTransformer;
//import com.youth.banner.transformer.RotateDownTransformer;
//import com.youth.banner.transformer.RotateUpTransformer;
//import com.youth.banner.transformer.ScaleInOutTransformer;
//import com.youth.banner.transformer.StackTransformer;
//import com.youth.banner.transformer.TabletTransformer;
//import com.youth.banner.transformer.ZoomInTransformer;
//import com.youth.banner.transformer.ZoomOutSlideTransformer;
//import com.youth.banner.transformer.ZoomOutTranformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 美食页
 */
public class HomeRecipeView extends FrameLayout implements UIListeners.IRefresh {
    @InjectView(R.id.home_recipe_ll_stove)//烟灶菜谱
    LinearLayout homeRecipeLlStove;
    @InjectView(R.id.home_recipe_ll_oven)//烤箱菜谱
    LinearLayout homeRecipeLlOven;
    @InjectView(R.id.imageView6)
    ImageView imageView6;
    @InjectView(R.id.home_recipe_ll_steam)//蒸想菜谱
    LinearLayout homeRecipeLlSteam;
    @InjectView(R.id.home_recipe_ll_micro)//微波炉菜谱
    LinearLayout homeRecipeLlMicro;
    @InjectView(R.id.home_recipe_imgv_dynamic)//动态封面
    ImageView homeRecipeImgvDynamic;
    @InjectView(R.id.home_recipe_frame_dynamicfollow)
    RelativeLayout homeRecipeFrameDynamicfollow;
    @InjectView(R.id.home_recipe_iv_chufang_zhishi)
    ImageView homeRecipeIvChufangZhishi;
    @InjectView(R.id.home_recipe_frame_chufang_zhishi)//厨房知识
    RelativeLayout homeRecipeFrameChufangZhishi;
    @InjectView(R.id.home_recipe_ll_themes)//列表主题
    LinearLayout homeRecipeLlThemes;
    @InjectView(R.id.theme_btn)//获取更多主题
    TextView themeBtn;
    @InjectView(R.id.home_recipe_ll_recommand)//推荐菜
    LinearLayout homeRecipeLlRecommand;
    @InjectView(R.id.view_home_recipe_refresh)
    SwipeRefreshLayout viewHomeRecipeRefresh;
    @InjectView(R.id.home_recipe_tv_morerecipe)
    TextView homeRecipeTvMorerecipe;
    @InjectView(R.id.home_recipe_info)
    LinearLayout home_recipe_info;
    @InjectView(R.id.home_recipe_chu)
    LinearLayout home_recipe_chu;

    private Context cx;
    private liveshow liveshowRc;
//    Banner banner;
    final String _signkey = "B9FAFDD1-BA4F-4AF5-A8D4-1440F7836001";

    List<Class<? extends ViewPager.PageTransformer>> transformers = new ArrayList<Class<? extends ViewPager.PageTransformer>>();
    List<RecipeTheme> moreList = new ArrayList<>();
    ArrayList<String> timeDataList = new ArrayList<>();
    ArrayList<Long> idDataList = new ArrayList<>();


    public void initData() {
//        transformers.add(DefaultTransformer.class);
//        transformers.add(AccordionTransformer.class);
//        transformers.add(BackgroundToForegroundTransformer.class);
//        transformers.add(ForegroundToBackgroundTransformer.class);
//        transformers.add(CubeInTransformer.class);
//        transformers.add(CubeOutTransformer.class);
//        transformers.add(DepthPageTransformer.class);
//        transformers.add(FlipHorizontalTransformer.class);
//        transformers.add(FlipVerticalTransformer.class);
//        transformers.add(RotateDownTransformer.class);
//        transformers.add(RotateUpTransformer.class);
//        transformers.add(ScaleInOutTransformer.class);
//        transformers.add(StackTransformer.class);
//        transformers.add(TabletTransformer.class);
//        transformers.add(ZoomInTransformer.class);
//        transformers.add(ZoomOutTranformer.class);
//        transformers.add(ZoomOutSlideTransformer.class);
    }

    public HomeRecipeView(Context context) {
        super(context);
        this.cx = context;
        EventUtils.regist(this);
        init(context, null);
    }

    private int sumY = 0;
    private float duration = 300.0f;//在0-150之间去改变头部的透明度
    private ArgbEvaluator evaluator = new ArgbEvaluator();


    void init(final Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_recipe_new, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
//        viewHomeRecipeRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//        viewHomeRecipeRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
//                homeRefresh();
//            }
//        });
        viewHomeRecipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeRefresh();
            }
        });
        homeRefresh();

    }




    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //homeRefresh();
    }


    public void homeRefresh() {
        getThemeHttpData();//构建主题
        getRecommandRecipeData();//获取推荐菜谱
    }

    //搜索
    @OnClick(R.id.home_recipe_ll_search)
    public void onSearchClick() {
        Helper.newRecipeSearchDialog(cx, new RecipeSearchDialog.OnSearchCallback() {
            @Override
            public void onSearchWord(String word) {

            }
        });
    }

    /**
     * 灶具菜谱
     * 烟机
     */

    @OnClick(R.id.home_recipe_ll_stove)
    public void onStoveClick() {
        recipeCategoryClick(DeviceType.RRQZ);
    }

    /**
     * 烤箱
     */
    @OnClick(R.id.home_recipe_ll_oven)
    public void onOvenClick() {
        recipeCategoryClick(DeviceType.RDKX);
    }

    /**
     * 蒸箱
     */
    @OnClick(R.id.home_recipe_ll_steam)
    public void onSteamClick() {
        recipeCategoryClick(DeviceType.RZQL);
    }

    /**
     * 微波炉
     */
    @OnClick(R.id.home_recipe_ll_micro)
    public void onMicroClick() {
        recipeCategoryClick(DeviceType.RZKY);
    }

    /**
     * 厨源知识
     */
    @OnClick(R.id.home_recipe_frame_chufang_zhishi)
    public void onLiveClick() {
        UIService.getInstance().postPage(PageKey.kitchenKnowledge);
    }

    /**
     * 关注动态
     */
    @OnClick(R.id.home_recipe_frame_dynamicfollow)
    public void onDynamicShowClick() {
        UIService.getInstance().postPage(PageKey.RecipeDynamicShow);
    }


    //菜谱分类
    //全部智能菜谱
    @OnClick(R.id.img_classify)
    public void onImg_classifyClick() {

        ToolUtils.logEvent("菜谱", "点击进入分类菜谱", "roki_美食");
        UIService.getInstance().postPage(PageKey.RecipeClassify);
    }

    /**
     * 更多主题点击--查看全部
     */
    @OnClick(R.id.theme_btn)
    public void onViewClicked() {
        UIService.getInstance().postPage(PageKey.MoreThomePage);
    }

    /**
     * 更多菜谱
     *
     * @param view
     */
    @OnClick({R.id.home_recipe_tv_morerecipe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_recipe_tv_morerecipe:
                UIService.getInstance().postPage(PageKey.RecipeHistory);
                break;
        }
    }


//    DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
    RequestOptions options = new RequestOptions().centerCrop()
        .placeholder(R.mipmap.img_default)
        .error(R.mipmap.img_default)
        .transform(new RoundedCorners(20));

    /**
     * 从远程获取主题列表数据
     */
    private void getThemeHttpData() {
        CookbookManager.getInstance().getThemeRecipes_new(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(final List<RecipeTheme> recipeThemes) {
                if (recipeThemes == null || recipeThemes.size() == 0) {
                    return;
                }

//                viewHomeRecipeRefresh.onRefreshComplete();
                moreList = recipeThemes;
                timeDataList.clear();
                List<String> imageUrls = new ArrayList<String>();

                final List<RecipeTheme> tempTheme = new ArrayList<RecipeTheme>();
                for (int i = 0; i < recipeThemes.size(); i++) {
                    if ("2".equals(recipeThemes.get(i).type)) {

                    } else {
                        if ("1".equals(recipeThemes.get(i).type)) {
                            String insertTimeDate = recipeThemes.get(i).insertTimeDate;
                            Long id = recipeThemes.get(i).getID();
                            timeDataList.add(insertTimeDate);
                            idDataList.add(id);
                        }

                    }
                }

                imageUrls.clear();
                for (int i = 0; i < recipeThemes.size(); i++) {
                    if (recipeThemes.get(i).topFlag == 1) {
                        imageUrls.add(recipeThemes.get(i).imageUrl);
                        tempTheme.add(recipeThemes.get(i));
                    }
                }


                //构建图片轮播主题展示
//                banner.setOnBannerClickListener(new OnBannerClickListener() {
//                    @Override
//                    public void OnBannerClick(int position) {
//                        goToThemeDetail(tempTheme.get(position - 1), timeDataList);
//                    }
//                });
//                banner.setImages(imageUrls)
//                        .setImageLoader(new GlideImageLoader())
//                        .start();
//                banner.setDelayTime(3500);
                //构建页面下方列表主题
                if (recipeThemes.size() > 3) {
                    homeRecipeLlThemes.removeAllViews();//清空数据
                    LayoutInflater inflater = LayoutInflater.from(cx);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    List<RecipeTheme> homeDyRecipeThemes = new ArrayList<RecipeTheme>();
                    for (RecipeTheme me : recipeThemes) {
                        if (me.topFlag == 0) {
                            homeDyRecipeThemes.add(me);
                        }
                    }
                    for (RecipeTheme theme : homeDyRecipeThemes.subList(0, 2)) {
                        if (theme != null) {//从0开始表示截取两条数据
                            View view = inflater.inflate(R.layout.view_home_theme_recipe, null, false);
                            LinearLayout ll_themes = (LinearLayout) view.findViewById(R.id.ll_themes);
                            ImageView img = (ImageView) view.findViewById(R.id.iv_img);
                            Glide.with(cx)
                                    .asBitmap()
                                    .load(theme.imageUrl)
//                                    .transform(new CenterCrop(cx), new RoundTransformation(cx, 10))
                                    .into(img);
                            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                            tvName.setText(theme.name);
                            TextView tvSubName = (TextView) view.findViewById(R.id.tv_subname);
                            tvSubName.setText(theme.subName);
                            TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
                            long time = Long.parseLong(theme.insertTimeDate);
                            String format = sdf.format(new Date(time));
                            tvNumber.setText(format);
                            //img.setTag(theme);
                            ll_themes.setTag(theme);
                            //点击进入主题详情
                            ll_themes.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RecipeClick(v);
                                }
                            });
                            homeRecipeLlThemes.addView(view);
                        }
                    }
//                    viewHomeRecipeRefresh.onRefreshComplete();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //viewHomeRecipeRefresh.onRefreshComplete();
            }
        });
    }
    List<Dc> listBefore=null;

    /**
     * 获取推荐菜谱
     */
    private void getRecommandRecipeData() {

        CookbookManager.getInstance().getRecommendCookbooks(new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                if (recipes == null || recipes.size() == 0)
                    return;
//                viewHomeRecipeRefresh.onRefreshComplete();
                LayoutInflater inflater = LayoutInflater.from(cx);
                homeRecipeLlRecommand.removeAllViews();
                int i = 0;
                for (final Recipe recipe : recipes) {
                    i++;
                    if (inflater == null)
                        return;
                    View view = inflater.inflate(R.layout.view_home_recommandrecipe, null, false);
                    //背景大图
                    ImageView imgv = (ImageView) view.findViewById(R.id.iv_img);
                    ImageView logo = (ImageView) view.findViewById(R.id.logo);

                    ImageView source_logo = (ImageView) view.findViewById(R.id.home_recipe_head_ic);
                    final ImageView collection = (ImageView) view.findViewById(R.id.iv_collection);
                    Glide.with(cx)
                            .asBitmap()
                            .load(recipe.imgLarge)
//                            .transform(new CenterCrop(cx), new RoundTransformation(cx, 10))
                            .into(imgv);
                    if (!TextUtils.isEmpty(recipe.stampLogo)) {
                        logo.setVisibility(View.VISIBLE);
                        Glide.with(cx).load(recipe.stampLogo).into(logo);
                    } else {
                        logo.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(recipe.providerImage)) {
                        Glide.with(cx).load(recipe.providerImage).into(source_logo);
                    }
                    View line = view.findViewById(R.id.line);
                    if (i == 1) {
                        line.setVisibility(View.GONE);
                    } else {
                        line.setVisibility(View.VISIBLE);
                    }
                    //名称
                    TextView textView = (TextView) view.findViewById(R.id.home_recipe_tv_recipename);
                    textView.setText(recipe.name);
                    //设备图片
                    ImageView img_device_one = (ImageView) view.findViewById(R.id.img_device_one);
                    ImageView img_device_two = (ImageView) view.findViewById(R.id.img_device_two);
                    //设备
                    TextView tv_device_name_one = (TextView) view.findViewById(R.id.tv_device_name_one);
                    TextView tv_device_name_two = (TextView) view.findViewById(R.id.tv_device_name_two);
                    List<Dc> js_dcs = recipe.getJs_dcs();

                    if (0 != js_dcs.size()) {
                        if (listBefore!=null) {
                            listBefore.clear();
                        }
                         listBefore = ListUtils.getListBefore(js_dcs);

                        for (int j = 0; j < listBefore.size(); j++) {
                            if (listBefore.size()==1) {
                                img_device_one.setVisibility(View.VISIBLE);
                                tv_device_name_one.setVisibility(View.VISIBLE);
                                img_device_two.setVisibility(View.GONE);
                                tv_device_name_two.setVisibility(View.GONE);
                                switch (listBefore.get(0).getName()) {
                                    case DeviceType.RDKX:
                                        tv_device_name_one.setText("烤");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RZQL:
                                        tv_device_name_one.setText("蒸");
                                        img_device_one.setImageResource(R.mipmap.img_zql_collection);
                                        break;
                                    case DeviceType.RWBL:
                                        tv_device_name_one.setText("微");
                                        img_device_one.setImageResource(R.mipmap.img_wbl_collection);
                                        break;
                                    case DeviceType.RRQZ:
                                        tv_device_name_one.setText("灶");
                                        img_device_one.setImageResource(R.mipmap.img_rzz_collection);
                                        break;
                                    case DeviceType.RZKY:
                                        tv_device_name_one.setText("一体");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RIKA:
                                        tv_device_name_one.setText("RIKA");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.KZNZ:
                                        tv_device_name_one.setText("智能灶");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;


                                }

                            }else{
                                img_device_one.setVisibility(View.VISIBLE);
                                tv_device_name_one.setVisibility(View.VISIBLE);
                                img_device_two.setVisibility(View.VISIBLE);
                                tv_device_name_two.setVisibility(View.VISIBLE);
                                switch (listBefore.get(0).getName()) {
                                    case DeviceType.RDKX:
                                        tv_device_name_one.setText("烤");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RZQL:
                                        tv_device_name_one.setText("蒸");
                                        img_device_one.setImageResource(R.mipmap.img_zql_collection);
                                        break;
                                    case DeviceType.RWBL:
                                        tv_device_name_one.setText("微");
                                        img_device_one.setImageResource(R.mipmap.img_wbl_collection);
                                        break;
                                    case DeviceType.RRQZ:
                                        tv_device_name_one.setText("灶");
                                        img_device_one.setImageResource(R.mipmap.img_rzz_collection);
                                        break;
                                    case DeviceType.RZKY:
                                        tv_device_name_one.setText("一体");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RIKA:
                                        tv_device_name_one.setText("RIKA");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.KZNZ:
                                        tv_device_name_one.setText("智能灶");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;

                                }
                                switch (listBefore.get(1).getName()) {
                                    case DeviceType.RDKX:
                                        tv_device_name_two.setText("烤");
                                        img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RZQL:
                                        tv_device_name_two.setText("蒸");
                                        img_device_two.setImageResource(R.mipmap.img_zql_collection);
                                        break;
                                    case DeviceType.RWBL:
                                        tv_device_name_two.setText("微");
                                        img_device_two.setImageResource(R.mipmap.img_wbl_collection);
                                        break;
                                    case DeviceType.RRQZ:
                                        tv_device_name_two.setText("灶");
                                        img_device_two.setImageResource(R.mipmap.img_rzz_collection);
                                        break;
                                    case DeviceType.RZKY:
                                        tv_device_name_two.setText("一体");
                                        img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.RIKA:
                                        tv_device_name_two.setText("RIKA");
                                        img_device_two.setImageResource(R.mipmap.img_dkx_collection);
                                        break;
                                    case DeviceType.KZNZ:
                                        tv_device_name_one.setText("智能灶");
                                        img_device_one.setImageResource(R.mipmap.img_dkx_collection);
                                        break;


                                }

                            }
                        }

                    }
                    //收藏数字
                    textView = (TextView) view.findViewById(R.id.home_recipe_tv_collect);
                    textView.setText(recipe.collectCount + "人收藏");
                    LogUtils.i("20181113", "collected:" + recipe.collected);
                    if (recipe.collected) {
                        collection.setImageResource(R.drawable.ic_baseline_favorite_24);
                    } else {
                        collection.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    }
                    final TextView finalTextView = textView;
                    collection.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean isLog = Plat.accountService.isLogon();
                            if (isLog) {
                                UiHelper.onFavority(recipe, (ImageView) view, finalTextView);
                            } else {
                                UIService.getInstance().postPage(PageKey.UserLogin);
                            }
                        }
                    });

                    //菜谱的点击事件
                    imgv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecipeDetailPage.show(recipe, recipe.getID(), RecipeDetailPage.HomeRecipeView, RecipeRequestIdentification.RECIPE_HOME_RECOMMEND);
                        }
                    });
                    homeRecipeLlRecommand.addView(view);
                }
//                viewHomeRecipeRefresh.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable t) {
//                viewHomeRecipeRefresh.onRefreshComplete();
            }
        });
    }

    private List<RecipeConsultation> recipeConsultations = new ArrayList<RecipeConsultation>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");

    /**
     * 获取咨询列表
     */
    private void getConsultationList() {
        int page = 0;
        int size = 20;
        CookbookManager.getInstance().getConsultationList(page, size, new Callback<List<RecipeConsultation>>() {
            @Override
            public void onSuccess(List<RecipeConsultation> list) {
                if (list == null || list.size() == 0)
                    return;
                recipeConsultations.clear();
                recipeConsultations.addAll(list);
                home_recipe_info.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(cx);
                for (int i = 0; i < 2; i++) {
                    if (inflater == null)
                        return;
                    final int temp = i;
                    View view = inflater.inflate(R.layout.view_home_info, null, false);
                    ImageView imgb = (ImageView) view.findViewById(R.id.home_info);
                    ImageUtils.displayImage(cx, recipeConsultations.get(i).imageUrl, imgb, options);
                    TextView txtTitle = (TextView) view.findViewById(R.id.home_info_title);
                    TextView txtTime = (TextView) view.findViewById(R.id.home_info_time);
                    txtTitle.setText(recipeConsultations.get(i).title);
                    long mmTime = Long.parseLong(recipeConsultations.get(i).insertTime);
                    String time = sdf.format(new Date(mmTime));
                    txtTime.setText(time);
                    TextView desc = (TextView) view.findViewById(R.id.desc_txt);
                    desc.setText(recipeConsultations.get(i).description);
                    view.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Bundle bd = new Bundle();
                            bd.putString(PageArgumentKey.WebTitle, recipeConsultations.get(temp).title);
                            bd.putString(PageArgumentKey.Url, recipeConsultations.get(temp).contentUrl);
                            UIService.getInstance().postPage(PageKey.WebClientNew, bd);
                        }
                    });
                    home_recipe_info.addView(view);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 更多主题推荐上的两个的点击事件
     */
    private void RecipeClick(View view) {
        RecipeTheme theme = (RecipeTheme) view.getTag();
        goToThemeDetail(theme, timeDataList);
    }


    private void goToThemeDetail(RecipeTheme theme, ArrayList<String> list) {
        LogUtils.i("20170927", "theme::" + theme.type + " " + theme.getID());
        if ("1".equals(theme.type)) {//主题菜谱
            Bundle bundle = new Bundle();
            bundle.putSerializable(PageArgumentKey.Theme, theme);
            bundle.putStringArrayList(PageArgumentKey.timeDataList, list);
            UIService.getInstance().postPage(PageKey.RecipeThemeDetail, bundle);
        } else if ("100".equals(theme.type)) {//易果
            String account = Plat.accountService.getLastAccount();
            long userid = Plat.accountService.getCurrentUserId();
            boolean isEmpty = Plat.deviceService.isEmpty();
            Bundle bd = new Bundle();
            String number;
            if (!isEmpty) {
                number = "1";
            } else {
                number = "0";
            }
            if (account != null) {
                String url = "isbuy=" + number + "&" + "openId=" + userid + "&" + "key=" + _signkey;
                String urlStr = MD5Utils.Md5(url);
                String urlWeb = theme.activity + "isbuy=" + number + "&" + "openId=" + userid + "&" + "sign=" + urlStr;
                LogUtils.i("20170220", "urlWeb::" + urlWeb);
                bd.putString(PageArgumentKey.WebTitle, new String("主题菜谱"));
                bd.putString(PageArgumentKey.Url, urlWeb);
                UIService.getInstance().postPage(PageKey.WebClient, bd);
            } else {
                bd.putString(PageArgumentKey.WebTitle, new String("主题菜谱"));
                bd.putString(PageArgumentKey.Url, theme.activity);
                UIService.getInstance().postPage(PageKey.WebClient, bd);
            }
        } else if ("2".equals(theme.type)) {//主题活动
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.WebTitle, theme.name);
            bd.putString(PageArgumentKey.Url, theme.activity);
            UIService.getInstance().postPage(PageKey.WebClientNew, bd);
        } else {
            //推荐菜谱
            RecipeDetailPage.show(Long.valueOf(theme.getRelateCookbookId()), RecipeDetailPage.HomeRecipeView, RecipeRequestIdentification.RECIPE_HOME_RECOMMEND);
        }
    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        switch (event.flag) {
            case HomeRecipeViewEvent.ThemeDetailPageBackToHomeRecipe:
                getRecommandRecipeData();
                break;
            case HomeRecipeViewEvent.DynamicShowToHomePage:
                getRecommandRecipeData();
                break;
            case HomeRecipeViewEvent.LiveListToHomePage:
                getRecommandRecipeData();
                break;
            case HomeRecipeViewEvent.DeviceRecipeBackHomePage:
                getRecommandRecipeData();
                break;
            case HomeRecipeViewEvent.RecipeDetailPageBackToHomePage:
                //getRecommandRecipeData();
                break;
            case HomeRecipeViewEvent.BackToMoreRecipe:
                getRecommandRecipeData();
                break;
            case HomeRecipeViewEvent.ClearMyFavorite:
                homeRefresh();
                break;
            case HomeRecipeViewEvent.RecipeFavoriteChange:
                getRecommandRecipeData();
                break;
            case HomeRecipeViewEvent.RecipeShowMomnet:
                //getDynamicCoverHttpData();
                break;
            case 0:
                homeRefresh();
            default:
                break;
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


    @Override
    public void onRefresh() {

    }


}
