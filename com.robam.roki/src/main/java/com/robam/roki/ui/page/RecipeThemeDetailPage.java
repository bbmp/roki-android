//package com.robam.roki.ui.page;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.resource.bitmap.CenterCrop;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.common.eventbus.Subscribe;
//import com.google.firebase.analytics.FirebaseAnalytics;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.legent.Callback;
//import com.legent.VoidCallback;
//import com.legent.plat.Plat;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.utils.EventUtils;
//import com.legent.utils.LogUtils;
//import com.legent.utils.StringUtils;
//import com.legent.utils.api.DisplayUtils;
//import com.legent.utils.api.ToastUtils;
//import com.robam.common.events.HomeRecipeViewEvent;
//import com.robam.common.pojos.Dc;
//import com.robam.common.pojos.DeviceType;
//import com.robam.common.pojos.Recipe;
//import com.robam.common.pojos.RecipeTheme;
//import com.robam.common.services.CookbookManager;
//import com.robam.common.services.StoreService;
//import com.robam.common.ui.UiHelper;
//import com.robam.common.util.RecipeRequestIdentification;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.ui.dialog.RecipeThemeShareDialog;
//import com.robam.roki.ui.extension.GlideApp;
//import com.robam.roki.ui.popup.CustomListPopupWindow;
//import com.robam.roki.ui.view.RoundTransformation;
//import com.robam.roki.utils.ListUtils;
//import com.robam.roki.utils.PermissionsUtils;
//import com.robam.roki.utils.ToolUtils;
//import com.zhy.view.flowlayout.FlowLayout;
//import com.zhy.view.flowlayout.TagAdapter;
//import com.zhy.view.flowlayout.TagFlowLayout;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import butterknife.ButterKnife;
//import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
//
///**
// * 主题详情页
// * Created by rent on 2016/8/7.
// */
//
//public class RecipeThemeDetailPage extends AbsListViewPage<Recipe> implements View.OnClickListener {
//
//    private RecipeListAdapter adapter = new RecipeListAdapter();//数据Adapter
//    private RecipeTheme theme;//主题对象
//    private final String uncollect = new String("已取消收藏");
//    private final String collect = new String("收藏主题");
//    StoreService ss = StoreService.getInstance();
//    Button themedetail_collect;//下方按钮
//    private String format;
//    private ArrayList<String> timeDataList = null;
//    private CustomListPopupWindow mPopupWindow;
//    private List<RecipeTheme> themeList = new ArrayList<>();
//    private View headview;
//    private View footview;
//    private int mCheckedUId = 3;
//    private ImageView ivThemeImg;
//    private static final String TAG = "RecipeThemeDetailPage";
//    private CustomListPopupWindow.ItemClickCallBack mCallBack;
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
//    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHH");
//    String title;
//    int index;
//
//    private TagFlowLayout mFlowLayout;
//    List<String> recipeName = new ArrayList<>();
//    private FirebaseAnalytics firebaseAnalytics;
//    RequestOptions options = new RequestOptions()
//            .centerCrop()
//            .placeholder(R.mipmap.img_default) //预加载图片
//            .error(R.mipmap.img_default) //加载失败图片
//            .priority(Priority.HIGH) //优先级
//            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
//            .transform(new RoundedCornersTransformation(50, 10)); //圆角
//
//    @Override
//    protected void initbefore() {
//        super.initbefore();
//        ivArwLeft.setVisibility(View.VISIBLE);
//        ivArwRight.setVisibility(View.VISIBLE);
//        Bundle bundle = getArguments();
//        theme = (RecipeTheme) bundle.getSerializable(PageArgumentKey.Theme);
//        LogUtils.i("20200409999", "theme::" + theme);
//        timeDataList = bundle.getStringArrayList(PageArgumentKey.timeDataList);
//        if ("2".equals(theme.type)) {
//        } else {
//            String type = theme.type;
//            if ("1".equals(type)) {
//                refreshThemeData(theme);
//            }
//        }
//        getThemes();//获取所有主题
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        firebaseAnalytics = MobApp.getmFirebaseAnalytics();
//        firebaseAnalytics.setCurrentScreen(getActivity(), "主题详情页", null);
//    }
//
//    /**
//     * 刷新主题数据
//     *
//     * @param theme
//     */
//    private void refreshThemeData(RecipeTheme theme) {
//        TextView textView;
//        if (theme == null)
//            UIService.getInstance().popBack();
//        //构建并加载主题头部介绍
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
//        headview = LayoutInflater.from(cx).inflate(R.layout.recipe_themedetail_head, null, false);
//        ivThemeImg = headview.findViewById(R.id.iv_theme_img);
//        textView = (TextView) headview.findViewById(R.id.themedetail_title);
//        //流式布局
////        mFlowLayout = (TagFlowLayout) headview.findViewById(R.id.id_flowlayout);
//
//        textView.setText(theme.name);
//        GlideApp.with(headview)
//                .load(theme.imageUrl)
//                .into(ivThemeImg);
//        long time = Long.parseLong(theme.insertTimeDate);
//        format = sdf.format(new Date(time));
//        textView = (TextView) headview.findViewById(R.id.themedetail_content);
//        textView.setText(theme.description);
//        listView.addHeaderView(headview, null, false);
//        //构建加载主题尾部收藏
//        footview = LayoutInflater.from(cx).inflate(R.layout.recipe_themedetail_foot, null, false);
//        themedetail_collect = (Button) footview.findViewById(R.id.themedetail_collect);
//        themedetail_collect.setOnClickListener(this);
//        listView.addFooterView(footview, null, false);
//    }
//
//
//    private void getThemes() {
//        CookbookManager.getInstance().getThemeRecipes_new(new Callback<List<RecipeTheme>>() {
//
//            @Override
//            public void onSuccess(List<RecipeTheme> recipeThemes) {
//                if (recipeThemes == null || recipeThemes.size() == 0) {
//                    LogUtils.out("recipeThemes is null");
//                    return;
//                }
//                List<RecipeTheme> recipeThemeList = filterThemeList(recipeThemes);
//                LogUtils.i(TAG, "recipeThemeList.size():::" + recipeThemeList.size());
//                index = getThemeIndex(recipeThemeList);
//                LogUtils.i(TAG, "index:::" + index);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
//
//    //过滤主题
//    private List<RecipeTheme> filterThemeList(List<RecipeTheme> recipeThemes) {
//        for (int i = 0; i < recipeThemes.size(); i++) {
//            if ("1".equals(recipeThemes.get(i).type)) {
//                themeList.add(recipeThemes.get(i));
//            }
//        }
//        return themeList;
//
//    }
//
//    //获取当前主题的下标
//    private int getThemeIndex(List<RecipeTheme> themes) {
//        for (int i = 0; i < themes.size(); i++) {
//            if ((themes.get(i).getID()).equals(theme.getID())) {
//                index = i;
//            }
//        }
//        return index;
//    }
//
//    @Override
//    protected void initAfter() {
//        super.initAfter();
//        //获取此主题是否以收藏过
//        ss.getThemeCollectStatus(theme.id, new Callback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//                if (aBoolean)
//                    themedetail_collect.setText(uncollect);
//                else
//                    themedetail_collect.setText(collect);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//        //导航栏左侧图标初始化
//        ImageView img = new ImageView(cx);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtils.dip2px(cx, 42f),
//                DisplayUtils.dip2px(cx, 42f));
//        layoutParams.setMargins(DisplayUtils.dip2px(cx, 20f), 0, 0, 0);
//        layoutParams.height = 60;
//        layoutParams.width = 60;
//        img.setLayoutParams(layoutParams);
//        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        img.setImageResource(R.mipmap.ic_recipe_share);
//        img.setClickable(true);
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
//                    if (selfPermission == 0) {
//                        RecipeThemeShareDialog.show(cx, theme);
//                    } else {
//                        PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_THEME_DETAIL_SHARE);
//                    }
//                } else {
//                    RecipeThemeShareDialog.show(cx, theme);
//                }
//            }
//        });
//        listview_titlebar_ll_right.addView(img);
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        initCallBackAndonClickListener();
//        home_recipe_ll_livelist.setMode(PullToRefreshBase.Mode.DISABLED);
//        getListData();
//        setTitle("第" + format + "期");
//
//        home_recipe_live_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void setTitle(String title) {
//        this.title = title;
//        String titleq = title.substring(0, 9);
//        String titleh = title.substring(11, 12);
//        String newTitle = titleq + titleh;
//        home_recipe_live_title.setGravity(Gravity.CENTER);
//        home_recipe_live_title.setText(newTitle);
//    }
//
//    @Override
//    protected String getTitle() {
//        return title;
//    }
//
//
//    /**
//     * 初始化popWind选择期数点击事件
//     */
//    private void initCallBackAndonClickListener() {
//        if (mCallBack == null) {
//            mCallBack = new CustomListPopupWindow.ItemClickCallBack() {
//                @Override
//                public void callBack(String time, Long themeId) {
//                    long mmTime = Long.parseLong(time);
//                    String format = sdf.format(new Date(mmTime));
//                    String textNum = "第" + format + "期";
//                    setTitle(textNum);
//                    refreshTheme(themeId);
//                    if (mPopupWindow != null) {
//                        mPopupWindow.dismiss();
//                    }
//                }
//            };
//        }
//
//
//        /**
//         * 上一期点击事件
//         */
//        ivArwLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (themeList.size() > 0) {
//
//                    if (index > 0) {
//                        index--;
//                        String time = themeList.get(index).insertTimeDate;
//                        long mmTime = Long.parseLong(time);
//                        String format = sdf.format(new Date(mmTime));
//                        String textNum = "第" + format + "期";
//                        setTitle(textNum);
//                        if (index == 0) {
//                            ToastUtils.showLong(R.string.up_recipe_time);
//                            ivArwLeft.setVisibility(View.GONE);
//                        } else {
//                            Long id = themeList.get(index).getID();
//                            refreshTheme(id);
//                        }
//
//
//                    } else {
//                        index = 0;
//
//                    }
//
//                }
//
//            }
//        });
//
//        /**
//         * 下一期点击事件
//         */
//        ivArwRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (themeList.size() > 0) {
//
//
//                    if (themeList.size() - 1 > index) {
//                        index++;
//
//                        String time = themeList.get(index).insertTimeDate;
//                        long mmTime = Long.parseLong(time);
//                        String format = sdf.format(new Date(mmTime));
//                        String textNum = "第" + format + "期";
//                        setTitle(textNum);
//                        Long id = themeList.get(index).getID();
//                        refreshTheme(id);
//                        if (themeList.size() - 1 == index) {
//                            ivArwRight.setVisibility(View.GONE);
//                            ToastUtils.showLong(R.string.next_recipe_time);
//                        }
//
//                    }
//
//                }
//
//
//            }
//        });
//
//    }
//
//    boolean post = false;
//
//    @Subscribe
//    public void onEvent(HomeRecipeViewEvent event) {
//        if (event.flag == HomeRecipeViewEvent.RecipeDetailPageBackToTheme) {
//            if (dataList != null)
//                dataList.clear();
//            getListData();
//            post = true;
//        }
//    }
//
//    @Override
//    protected void bindAdapter() {
//        home_recipe_ll_livelist.setAdapter(adapter);
//    }
//
//    /**
//     * 根据主题相关联菜谱ID获取菜谱列表
//     */
//    protected void getListData() {
//        if (theme.relateCookbookId != null && theme.relateCookbookId.length() != 0) {
//            String[] strings = theme.relateCookbookId.trim().split(",");
//            for (int i = 0; i < strings.length; i++) {
//                LogUtils.i(TAG, "iiii::" + strings[i]);
//            }
//            long userId = Plat.accountService.getCurrentUserId();
//            if (strings != null && strings.length > 0) {
//                ProgressDialogHelper.setRunning(cx, true);
//                StoreService.getInstance().getCookbookByIds(userId, strings, new Callback<List<Recipe>>() {
//                    @Override
//                    public void onSuccess(List<Recipe> recipes) {
//                        dataList.clear();
//                        dataList.addAll(recipes);
//                        recipeName.clear();
//                        for (int i = 0; i < dataList.size(); i++) {
//                            recipeName.add(dataList.get(i).name);
//                            LogUtils.i("20180228", "here is" + dataList.get(i).name);
//                        }
////                        mFlowLayout.setAdapter(new TagAdapter<String>(recipeName) {
////                            @Override
////                            public View getView(FlowLayout parent, int position, String s) {
////                                TextView tv = (TextView) LayoutInflater.from(cx).inflate(R.layout.tv,
////                                        mFlowLayout, false);
////                                tv.setText(s);
////                                return tv;
////                            }
////
////                        });
////                        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
////                            @Override
////                            public boolean onTagClick(View view, int position, FlowLayout parent) {
////                                if (dataList != null)
////                                    RecipeDetailPage.show(dataList.get(position).id, RecipeDetailPage.ThemeDetailPage, RecipeRequestIdentification.RECIPE_THEMED);
////                                return true;
////                            }
////                        });
//                        adapter.notifyDataSetChanged();
//                        ProgressDialogHelper.setRunning(cx, false);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        ProgressDialogHelper.setRunning(cx, false);
//                    }
//                });
//            }
//        }
//    }
//
//
//    /**
//     * 收藏/取消收藏点击
//     */
//    @Override
//    public void onClick(View v) {
//        if (v.getId() != R.id.themedetail_collect) {
//            return;
//        }
//        // 首先判断是否登录
//        if (!UiHelper.checkAuthWithDialog(cx, PageKey.UserLogin))
//            return;
//        if (collect.equals(themedetail_collect.getText().toString())) {
//
//            ToolUtils.logEvent("主题", "收藏主题:" + theme.getName(), "roki_美食");
//
//
//            ProgressDialogHelper.setRunning(cx, true);
//            ss.setThemeCollect(theme.id, new Callback<Boolean>() {
//                @Override
//                public void onSuccess(Boolean aBoolean) {
//                    if (aBoolean) {
//                        ToastUtils.show(new String("收藏成功"), Toast.LENGTH_SHORT);
//                        themedetail_collect.setText(uncollect);
//                        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.ThemeDetailBackMyCollect));
//                    }
//                    ProgressDialogHelper.setRunning(cx, false);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    ProgressDialogHelper.setRunning(cx, false);
//                }
//            });
//        } else {
//            ToolUtils.logEvent("主题", "取消收藏主题::" + theme.getName(), "roki_美食");
//
//            ProgressDialogHelper.setRunning(cx, true);
//            ss.setCancelThemeCollect(theme.id, new Callback<Boolean>() {
//                @Override
//                public void onSuccess(Boolean aBoolean) {
//                    if (aBoolean) {
//                        ToastUtils.show(new String("已取消收藏"), Toast.LENGTH_SHORT);
//                        themedetail_collect.setText(collect);
//                        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.ThemeDetailBackMyCollect));
//                    }
//                    ProgressDialogHelper.setRunning(cx, false);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    ProgressDialogHelper.setRunning(cx, false);
//                }
//            });
//        }
//    }
//
//    /**
//     * 导航栏右侧点击
//     */
//    @Override
//    void OnClickRight() {
//        super.OnClickRight();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_BACK == keyCode && post)
//            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.ThemeDetailPageBackToHomeRecipe));
//        return super.onKeyDown(keyCode, event);
//    }
//
//    /**
//     * 导航栏左侧点击
//     */
//    @Override
//    void OnClickLeft() {
//        super.OnClickLeft();
//        if (post)
//            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.ThemeDetailPageBackToHomeRecipe));
//        UIService.getInstance().popBack();
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//        timeDataList = null;
//    }
//
//    /**
//     * 点击期数刷新数据
//     */
//    private void refreshTheme(Long themeId) {
//
//        ivArwRight.setVisibility(View.VISIBLE);
//        ivArwLeft.setVisibility(View.VISIBLE);
//        for (RecipeTheme theme : themeList) {
//            if ("2".equals(theme.type)) {
//
//            } else {
//
//                if ("1".equals(theme.type)) {
//                    if (theme.getID().equals(themeId)) {
//
//                        this.theme = theme;
//                        if (dataList != null && dataList.size() != 0) {
//                            dataList.clear();
//                        }
//                        listView.removeHeaderView(headview);
//                        listView.removeFooterView(footview);
//                        refreshThemeData(theme);
//                        getListData();
//                    }
//                }
//
//            }
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (PermissionsUtils.CODE_THEME_DETAIL_SHARE == requestCode) {
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    RecipeThemeShareDialog.show(cx, theme);
//                }
//            }
//        }
//    }
//
//    List<Dc> listBefore=null;
//    class RecipeListAdapter extends ListAdapter {
//        ViewHolder viewHolder;
////        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final Recipe recipe = dataList.get(position);
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.view_home_recommandrecipe, null, false);
//                viewHolder = new ViewHolder(cx);
//                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_img);
//
//                viewHolder.tv_device_name_one =  convertView.findViewById(R.id.tv_device_name_one);
//                viewHolder.tv_device_name_two =  convertView.findViewById(R.id.tv_device_name_two);
//                viewHolder.img_device_one =  convertView.findViewById(R.id.img_device_one);
//                viewHolder.img_device_two =  convertView.findViewById(R.id.img_device_two);
//
//                viewHolder.iv_collection_select = (ImageView) convertView.findViewById(R.id.iv_collection_select);
//                viewHolder.iv_collection = (ImageView) convertView.findViewById(R.id.iv_collection);
//                viewHolder.nameTv = (TextView) convertView.findViewById(R.id.home_recipe_tv_recipename);
//                viewHolder.collectView = (TextView) convertView.findViewById(R.id.home_recipe_tv_collect);
//                convertView.setTag(R.id.tag_theme_collected_holder, viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag(R.id.tag_theme_collected_holder);
//            }
//            if (!StringUtils.isNullOrEmpty(recipe.imgLarge)) {
//                GlideApp.with(cx)
//                        .load(recipe.imgLarge)
//                        .apply(options)
//                        .into(viewHolder.imageView);
//            } else {
//                viewHolder.imageView.setImageDrawable(r.getDrawable(R.mipmap.img_default));
//            }
//            viewHolder.nameTv.setText(recipe.name);
//            List<Dc> js_dcs = recipe.getJs_dcs();
//
//            if (0 != js_dcs.size()) {
//                if (listBefore!=null) {
//                    listBefore.clear();
//                }
//                 listBefore = ListUtils.getListBefore(js_dcs);
//
//                for (int i = 0; i < listBefore.size(); i++) {
//                    if (listBefore.size()==1) {
//                        viewHolder.img_device_one.setVisibility(View.VISIBLE);
//                        viewHolder.tv_device_name_one.setVisibility(View.VISIBLE);
//                        viewHolder.img_device_two.setVisibility(View.GONE);
//                        viewHolder.tv_device_name_two.setVisibility(View.GONE);
//                        switch (listBefore.get(0).getName()) {
//                            case DeviceType.RDKX:
//                                viewHolder.tv_device_name_one.setText("烤");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.RZQL:
//                                viewHolder.tv_device_name_one.setText("蒸");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_zql_collection);
//                                break;
//                            case DeviceType.RWBL:
//                                viewHolder.tv_device_name_one.setText("微");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_wbl_collection);
//                                break;
//                            case DeviceType.RRQZ:
//                                viewHolder.tv_device_name_one.setText("灶");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_rzz_collection);
//                                break;
//                            case DeviceType.RZKY:
//                                viewHolder.tv_device_name_one.setText("一体");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.RIKA:
//                                viewHolder.tv_device_name_one.setText("RIKA");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.KZNZ:
//                                viewHolder.tv_device_name_one.setText("智能灶");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//
//
//                        }
//
//                    }else{
//                        viewHolder.img_device_one.setVisibility(View.VISIBLE);
//                        viewHolder.tv_device_name_one.setVisibility(View.VISIBLE);
//                        viewHolder.img_device_two.setVisibility(View.VISIBLE);
//                        viewHolder.tv_device_name_two.setVisibility(View.VISIBLE);
//                        switch (listBefore.get(0).getName()) {
//                            case DeviceType.RDKX:
//                                viewHolder.tv_device_name_one.setText("烤");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.RZQL:
//                                viewHolder.tv_device_name_one.setText("蒸");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_zql_collection);
//                                break;
//                            case DeviceType.RWBL:
//                                viewHolder.tv_device_name_one.setText("微");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_wbl_collection);
//                                break;
//                            case DeviceType.RRQZ:
//                                viewHolder.tv_device_name_one.setText("灶");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_rzz_collection);
//                                break;
//                            case DeviceType.RZKY:
//                                viewHolder.tv_device_name_one.setText("一体");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.RIKA:
//                                viewHolder.tv_device_name_one.setText("RIKA");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.KZNZ:
//                                viewHolder.tv_device_name_one.setText("智能灶");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//
//                        }
//                        switch (listBefore.get(1).getName()) {
//                            case DeviceType.RDKX:
//                                viewHolder.tv_device_name_two.setText("烤");
//                                viewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.RZQL:
//                                viewHolder.tv_device_name_two.setText("蒸");
//                                viewHolder.img_device_two.setImageResource(R.mipmap.img_zql_collection);
//                                break;
//                            case DeviceType.RWBL:
//                                viewHolder.tv_device_name_two.setText("微");
//                                viewHolder.img_device_two.setImageResource(R.mipmap.img_wbl_collection);
//                                break;
//                            case DeviceType.RRQZ:
//                                viewHolder.tv_device_name_two.setText("灶");
//                                viewHolder.img_device_two.setImageResource(R.mipmap.img_rzz_collection);
//                                break;
//                            case DeviceType.RZKY:
//                                viewHolder.tv_device_name_two.setText("一体");
//                                viewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.RIKA:
//                                viewHolder.tv_device_name_two.setText("RIKA");
//                                viewHolder.img_device_two.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//                            case DeviceType.KZNZ:
//                                viewHolder.tv_device_name_one.setText("智能灶");
//                                viewHolder.img_device_one.setImageResource(R.mipmap.img_dkx_collection);
//                                break;
//
//
//                        }
//
//                    }
//                }
//
//
//
//
//            }
//
//            viewHolder.collectView.setText(recipe.collectCount + "人收藏");
//            if (recipe.collected) {
//                viewHolder.iv_collection.setVisibility(View.GONE);
//                viewHolder.iv_collection_select.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.iv_collection.setVisibility(View.VISIBLE);
//                viewHolder.iv_collection_select.setVisibility(View.GONE);
//            }
//            viewHolder.iv_collection.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    boolean isLog = Plat.accountService.isLogon();
//                    if (isLog) {
//                        onItemClick(recipe, viewHolder);
//                    } else {
//                        UIService.getInstance().postPage(PageKey.UserLogin);
//                    }
//                }
//            });
//            viewHolder.iv_collection_select.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    boolean isLog = Plat.accountService.isLogon();
//                    if (isLog) {
//                        onItemClick(recipe, viewHolder);
//
//                    } else {
//                        UIService.getInstance().postPage(PageKey.UserLogin);
//                    }
//                }
//            });
//
//            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (recipe != null)
//                        RecipeDetailPage.show(recipe, recipe.id, RecipeDetailPage.ThemeDetailPage, RecipeRequestIdentification.RECIPE_THEMED);
//                }
//            });
//
//            return convertView;
//        }
//
//    }
//
//
//
//    private void onItemClick(final Recipe recipe, final ViewHolder viewHolder) {
//
//        CookbookManager cm = CookbookManager.getInstance();
//        if (recipe != null) {
//            if (recipe.collected) {
//                cm.deleteFavorityCookbooks(recipe.id, new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        recipe.setIsCollected(false);
//                        viewHolder.iv_collection.setVisibility(View.VISIBLE);
//                        viewHolder.iv_collection_select.setVisibility(View.GONE);
//                        viewHolder.collectView.setText(recipe.collectCount + "人收藏");
//                        recipe.collected = false;
//                        adapter.notifyDataSetChanged();
//                        ToastUtils.showShort("已取消收藏");
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        ToastUtils.showShort(t.getMessage());
//                    }
//                });
//            } else {
//                cm.addFavorityCookbooks(recipe.id, new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        recipe.setIsCollected(true);
//                        viewHolder.iv_collection.setVisibility(View.VISIBLE);
//                        viewHolder.iv_collection_select.setVisibility(View.GONE);
//                        viewHolder.collectView.setText(recipe.collectCount + "人收藏");
//                        recipe.collected = true;
//                        adapter.notifyDataSetChanged();
//                        ToastUtils.showShort("收藏成功");
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        ToastUtils.showShort(t.getMessage());
//                    }
//                });
//
//            }
//        }
//
//    }
//
//    private static class ViewHolder {
//        Context cx;
//
//        public ViewHolder(Context cx) {
//            this.cx = cx;
//        }
//
//        ImageView imageView,iv_collection, iv_collection_select;
//        TextView nameTv;
//        TextView collectView;
//        ImageView img_device_one;
//        ImageView img_device_two;
//        TextView tv_device_name_one;
//        TextView tv_device_name_two;
//    }
//}