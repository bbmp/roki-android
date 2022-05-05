//package com.robam.roki.ui.page;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.ScaleAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.common.collect.Lists;
//import com.google.common.eventbus.Subscribe;
//import com.google.firebase.analytics.FirebaseAnalytics;
//import com.legent.Callback;
//import com.legent.VoidCallback;
//import com.legent.dao.DaoHelper;
//import com.legent.plat.events.RecipePraiseEvent;
//import com.legent.ui.UIService;
//import com.legent.utils.EventUtils;
//import com.legent.utils.LogUtils;
//import com.legent.utils.StringUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.graphic.ImageUtils;
//import com.robam.common.events.HomeRecipeViewEvent;
//import com.robam.common.pojos.RecipeShow;
//import com.robam.common.services.CookbookManager;
//import com.robam.common.ui.UiHelper;
//import com.robam.common.util.RecipeRequestIdentification;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageKey;
//
//import java.util.Calendar;
//import java.util.List;
//
//import static com.robam.roki.R.id.recipe_dynamic_imgv_praiseimg;
//import static com.robam.roki.R.id.recipe_dynamic_tv_desc;
//import static com.robam.roki.R.id.recipe_dynamic_tv_praisenum;
//import static com.robam.roki.R.id.recipe_dynamic_tv_recipename;
//
///**
// * Created by rent on 2016/8/7.
// * 关注动态
// */
//
//public class RecipeDynamicShowPage extends AbsListViewPage<RecipeShow> {
//    private RecipeListAdapter adapter = new RecipeListAdapter();
//    private FirebaseAnalytics firebaseAnalytics;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        EventUtils.regist(this);
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//
//    @Override
//    protected void initbefore() {
//        super.initbefore();
//        setTitle("动态");
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        getLiveListData();
//    }
//
//    @Override
//    protected void bindAdapter() {
//        home_recipe_ll_livelist.setAdapter(adapter);
//    }
//
//    @Override
//    protected void setTitle(String title) {
//        super.setTitle(title);
//    }
//
//    protected void getLiveListData() {
//        CookbookManager.getInstance().getRecipeDynamicShow(start, num, new Callback<List<RecipeShow>>() {
//            @Override
//            public void onSuccess(List<RecipeShow> recipeShows) {
//                try {
//                    if (recipeShows == null || recipeShows.size() <= 0)
//                        recipeShows = Lists.newArrayList();
//                    if (start == 0) {
//                        dataList.clear();
//                    }
//                    dataList.addAll(recipeShows);
//                    adapter.notifyDataSetChanged();
//                    home_recipe_ll_livelist.onRefreshComplete();
//                } catch (Exception e) {
//                    Log.e("RecipeDynamic", "error" + e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                home_recipe_ll_livelist.onRefreshComplete();
//            }
//        });
//    }
//
//    @Override
//    protected void onPullDown() {
//        super.onPullDown();
//        getLiveListData();
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        firebaseAnalytics = MobApp.getmFirebaseAnalytics();
//        firebaseAnalytics.setCurrentScreen(getActivity(), "关注动态页", null);
//    }
//
//    @Override
//    void OnClickLeft() {
//        super.OnClickLeft();
//        EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.DynamicShowToHomePage));
//        UIService.getInstance().popBack();
//    }
//
//    @Override
//    protected void onPullUp() {
//        super.onPullUp();
//        getLiveListData();
//    }
//
//    @Override
//    void OnClickRight() {
//        super.OnClickRight();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.DynamicShowToHomePage));
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Subscribe
//    public void onEvent(RecipePraiseEvent event) {
//        for (RecipeShow recipeShow : dataList) {
//            if (recipeShow.id == Long.parseLong(event.id)) { //从数据集中判断是否有对应的菜谱ID
//                if (event.isPraised) { //赞
//                    recipeShow.hasPraised = "true";
//                    recipeShow.praiseCount = String.valueOf(Integer.parseInt(recipeShow.praiseCount) + 1);
//                } else {
//                    recipeShow.hasPraised = "false";
//                    recipeShow.praiseCount = String.valueOf(Integer.parseInt(recipeShow.praiseCount) - 1);
//                }
//                DaoHelper.update(recipeShow);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    class RecipeListAdapter extends ListAdapter {
////        DisplayImageOptions.Builder options = new DisplayImageOptions.Builder();
//        RequestOptions options = new RequestOptions().centerCrop()
//                        .placeholder(R.mipmap.ic_recipe_headicon_roki)
//        .error(R.mipmap.ic_recipe_headicon_roki);
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final ViewHolder viewHolder;
//            final RecipeShow recipeShow = dataList.get(position);
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.view_home_dynamicshow, null, false);
//                viewHolder = new ViewHolder(cx, recipeShow);
//                viewHolder.recipe_dynamic_imgv_headic = convertView.findViewById(R.id.recipe_dynamic_imgv_headic);
//                viewHolder.recipe_dynamic_tv_name = convertView.findViewById(R.id.recipe_dynamic_tv_name);
//                viewHolder.recipe_dynamic_tv_time = convertView.findViewById(R.id.recipe_dynamic_tv_time);
//                viewHolder.recipe_dynamic_tv_desc = convertView.findViewById(recipe_dynamic_tv_desc);
//                viewHolder.recipe_dynamic_imgv_show = convertView.findViewById(R.id.recipe_dynamic_imgv_show);
//                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) viewHolder.recipe_dynamic_imgv_show.getLayoutParams();
//                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//                linearParams.width = wm.getDefaultDisplay().getWidth();
//                linearParams.height = (int) (wm.getDefaultDisplay().getWidth() * 0.78);
//                viewHolder.recipe_dynamic_tv_recipename = convertView.findViewById(recipe_dynamic_tv_recipename);
//                viewHolder.recipe_dynamic_tv_praisenum = convertView.findViewById(recipe_dynamic_tv_praisenum);
//                viewHolder.recipe_dynamic_imgv_praiseimg = convertView.findViewById(recipe_dynamic_imgv_praiseimg);
//                viewHolder.recipe_dynamic_ll_torecipe = convertView.findViewById(R.id.recipe_dynamic_ll_torecipe);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//            if (!StringUtils.isNullOrEmpty(recipeShow.imgUrl)) {//背景图
//                // ImageUtils.displayImage(recipeShow.imgUrl, viewHolder.recipe_dynamic_imgv_show, options.showImageOnLoading(R.mipmap.img_default).build());
//                Glide.with(cx).load(recipeShow.imgUrl).error(R.mipmap.img_default).into(viewHolder.recipe_dynamic_imgv_show);
//            } else {
//                viewHolder.recipe_dynamic_imgv_show.setImageDrawable(r.getDrawable(R.mipmap.img_default));
//            }
//            if (!StringUtils.isNullOrEmpty(recipeShow.ownerFigureUrl)) {//头像图
//                LogUtils.i("20170911", "figureurl:" + recipeShow.ownerFigureUrl);
//                ImageUtils.displayImage(cx, recipeShow.ownerFigureUrl, viewHolder.recipe_dynamic_imgv_headic, options);
//            } else {
//                viewHolder.recipe_dynamic_imgv_headic.setImageResource(R.mipmap.ic_recipe_headicon_roki);
//            }
//
//            if (TextUtils.isEmpty(recipeShow.ownerName)) {
//                viewHolder.recipe_dynamic_tv_name.setText("匿名");
//            } else {
//                viewHolder.recipe_dynamic_tv_name.setText(recipeShow.ownerName);
//            }
//            viewHolder.recipe_dynamic_tv_desc.setText(recipeShow.desc);
//            Calendar calendar = Calendar.getInstance();
//            try {
//                if (recipeShow.uploadTime.startsWith(calendar.get(Calendar.YEAR) + "")) {
//                    String[] str = recipeShow.uploadTime.split(calendar.get(Calendar.YEAR) + "-");
//                    viewHolder.recipe_dynamic_tv_time.setText(str[1].substring(0, str[1].length() - 5));
//                } else {
//                    viewHolder.recipe_dynamic_tv_time.setText(recipeShow.uploadTime.substring(0, recipeShow.uploadTime.length() - 5));
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            viewHolder.recipe_dynamic_imgv_praiseimg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String tag = (String) v.getTag();
//                    final ImageView img = (ImageView) v;
//                    // 首先判断是否登录
//                    if (!UiHelper.checkAuthWithDialog(cx, PageKey.UserLogin))
//                        return;
//                    //其次判断是否已点赞
//                    if ("no".equals(tag)) {
//                        LogUtils.out("no" + recipeShow.id);
//                        CookbookManager.getInstance().praiseCookAlbum(recipeShow.id, new VoidCallback() {
//                            @Override
//                            public void onSuccess() {
//                                img.setTag("yes");
//                                img.setImageResource(R.mipmap.ic_prasie_yes);
//                                ToastUtils.showShort(R.string.dynamic_thumb_up);
//                                //count = Integer.parseInt(recipeShow.praiseCount);
//                                LogUtils.out("no count前:" + Integer.parseInt(recipeShow.praiseCount));
//                                recipeShow.praiseCount = String.valueOf(Integer.parseInt(recipeShow.praiseCount) + 1);
//                                viewHolder.recipe_dynamic_tv_praisenum.setText(recipeShow.praiseCount + "人");
//                                //LogUtils.out("no count后:"+count);
//                                recipeShow.hasPraised = "true";
//                                //recipeShow.praiseCount = count + "";
//                                DaoHelper.update(recipeShow);
//                            }
//
//                            @Override
//                            public void onFailure(Throwable t) {
//                                LogUtils.out("no" + t.getMessage());
//                            }
//                        });
//                    } else {
//                        LogUtils.out("yes" + recipeShow.id);
//                        CookbookManager.getInstance().unpraiseCookAlbum(recipeShow.id, new VoidCallback() {
//                            @Override
//                            public void onSuccess() {
//                                img.setTag("no");
//                                img.setImageResource(R.mipmap.ic_prasie_no);
//                                ToastUtils.showShort(R.string.dynamic_cancel_thumb_up);
//                                recipeShow.praiseCount = String.valueOf(Integer.parseInt(recipeShow.praiseCount) - 1);
//                                //LogUtils.out("yes count前:"+count);
//                                viewHolder.recipe_dynamic_tv_praisenum.setText(recipeShow.praiseCount + "人");
//                                recipeShow.hasPraised = "false";
//                                //LogUtils.out("yes count后:"+count);
//                                // recipeShow.praiseCount = count + "";
//                                DaoHelper.update(recipeShow);
//                            }
//
//                            @Override
//                            public void onFailure(Throwable t) {
//                                LogUtils.out("yes" + t.getMessage());
//                            }
//                        });
//                    }
//
//                    //设置点赞动画效果
//                    ScaleAnimation animation = new ScaleAnimation(1f, 1.4f, 1f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    animation.setDuration(100);
//                    v.startAnimation(animation);
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            animation.cancel();
//                            img.clearAnimation();
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                }
//            });
//            if ("true".equals(recipeShow.hasPraised)) {//判断自己是否点过赞
//                viewHolder.recipe_dynamic_imgv_praiseimg.setTag("yes");
//                viewHolder.recipe_dynamic_imgv_praiseimg.setImageResource(R.mipmap.ic_prasie_yes);
//            } else {
//                viewHolder.recipe_dynamic_imgv_praiseimg.setTag("no");
//                viewHolder.recipe_dynamic_imgv_praiseimg.setImageResource(R.mipmap.ic_prasie_no);
//            }
//            viewHolder.recipe_dynamic_tv_praisenum.setText(recipeShow.praiseCount + "人");//点赞数量
//            if (!StringUtils.isNullOrEmpty(recipeShow.cookbookName))
//                viewHolder.recipe_dynamic_tv_recipename.setText(recipeShow.cookbookName);//菜谱名
//            viewHolder.recipe_dynamic_ll_torecipe.setOnClickListener(new View.OnClickListener() {//点击跳转菜谱详情页
//                @Override
//                public void onClick(View v) {
//                    if (recipeShow.cookbookId != null)
//                        RecipeDetailPage.show(Long.parseLong(recipeShow.cookbookId),
//                                RecipeDetailPage.DynamicRecipeShow, RecipeRequestIdentification.RECIPE_SHARE_CULINARY_SKILL);
//                }
//            });
//            return convertView;
//        }
//    }
//
//    private static class ViewHolder implements View.OnClickListener {
//        Context cx;
//        RecipeShow recipeShow;
//        int count = 0;
//
//        public ViewHolder(Context cx, RecipeShow rs) {
//            this.cx = cx;
//            recipeShow = rs;
//        }
//
//        ImageView recipe_dynamic_imgv_headic;
//        TextView recipe_dynamic_tv_name;
//        TextView recipe_dynamic_tv_time;
//        TextView recipe_dynamic_tv_desc;
//        ImageView recipe_dynamic_imgv_show;
//        TextView recipe_dynamic_tv_recipename;
//        TextView recipe_dynamic_tv_praisenum;
//        ImageView recipe_dynamic_imgv_praiseimg;
//        LinearLayout recipe_dynamic_ll_torecipe;
//
//        /**
//         * 点赞
//         */
//        @Override
//        public void onClick(final View v) {
//            String tag = (String) v.getTag();
//            final ImageView img = (ImageView) v;
//            // 首先判断是否登录
//            if (!UiHelper.checkAuthWithDialog(cx, PageKey.UserLogin))
//                return;
//            //其次判断是否已点赞
//            if ("no".equals(tag)) {
//                CookbookManager.getInstance().praiseCookAlbum(recipeShow.id, new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        img.setTag("yes");
//                        img.setImageResource(R.mipmap.ic_prasie_yes);
//                        count = Integer.parseInt(recipeShow.praiseCount);
//                        recipe_dynamic_tv_praisenum.setText(++count + "人");
//                        recipeShow.hasPraised = "true";
//                        recipeShow.praiseCount = count + "";
//                        DaoHelper.update(recipeShow);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                    }
//                });
//            } else {
//                CookbookManager.getInstance().unpraiseCookAlbum(recipeShow.id, new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        img.setTag("no");
//                        img.setImageResource(R.mipmap.ic_prasie_no);
//                        count = Integer.parseInt(recipeShow.praiseCount);
//                        recipe_dynamic_tv_praisenum.setText(--count + "人");
//                        recipeShow.hasPraised = "false";
//                        recipeShow.praiseCount = count + "";
//                        DaoHelper.update(recipeShow);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                    }
//                });
//            }
//
//            //设置点赞动画效果
//            ScaleAnimation animation = new ScaleAnimation(1f, 1.4f, 1f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            animation.setDuration(100);
//            v.startAnimation(animation);
//            animation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    animation.cancel();
//                    img.clearAnimation();
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//    }
//}