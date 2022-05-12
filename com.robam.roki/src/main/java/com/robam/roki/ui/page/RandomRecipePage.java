package com.robam.roki.ui.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.Recipe;
import com.robam.common.util.RecipeUtils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.CookbookRandomShareDialog;
import com.robam.roki.ui.dialog.CookbookShareDialog;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.utils.LoginUtil;
import com.robam.roki.utils.PermissionsUtils;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * des：小游戏
 */
public class RandomRecipePage extends MyBasePage<MainActivity> {
    private boolean isRandom = false;
    private static final String TAG = "RandomRecipePage";

    @InjectView(R.id.iv_random_recipe)
    ImageView randomRecipeImg;

    @InjectView(R.id.tv_random_recipe)
    TextView randomRecipeBtn;

    @InjectView(R.id.tv_last_week_top)
    TextView lastWeekTopBtn;

    @InjectView(R.id.ll_recipe_select)
    LinearLayout llRecipeSelect;

    @InjectView(R.id.tv_change_recipe)
    ImageView tvChageRecipe;

    @InjectView(R.id.tv_choice_recipe)
    ImageView tvChoice_recipe;

    @InjectView(R.id.tv_recipe_game_content)
    TextView tvRecipeGameContent;

    @InjectView(R.id.tv_today_which_recipe)
    TextView tvTodayWhichRecipe;

    @InjectView(R.id.iv_share_recipe)
    ImageView ivShareRecipe;

    @InjectView(R.id.iv_user_head)
    ImageView ivUserHead;

    @InjectView(R.id.tv_user_name)
    TextView tvUserName;

    @InjectView(R.id.tv_random)
    TextView tvRandom;

    @InjectView(R.id.user_game_item)
    ConstraintLayout cl_user_game;
    private Recipe ramdomRecipe;
    private Animation rotateAnimation;
    private LinearInterpolator lin;

    Handler handler=new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            checkRandomCookBook();
        }
    };
    private String recipeLargeUrl;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.page_random_recipe, container, false);
//        ButterKnife.inject(this, view);
//
//
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_random_recipe;
    }

    @Override
    protected void initView() {
        randomRecipeImg.setEnabled(false);
        tvTodayWhichRecipe.setVisibility(View.VISIBLE);
        cl_user_game.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) randomRecipeImg.getLayoutParams();
        params.height = DisplayUtils.getScreenWidthPixels(getContext()) - DisplayUtils.dip2px(getContext(), 100);//设置当前控件布局的高度
        randomRecipeImg.setLayoutParams(params);
        rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim_set);
        lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                randomRecipeImg.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        setStateBarFixer();
    }

    @Override
    protected void initData() {
        startRecipeGame();
        handler.postDelayed(runnable , 2000);
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) randomRecipeImg.getLayoutParams();
//        params.height = DisplayUtils.getScreenWidthPixels(getContext()) - DisplayUtils.dip2px(getContext(), 100);//设置当前控件布局的高度
//        randomRecipeImg.setLayoutParams(params);
//        rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
//        lin = new LinearInterpolator();
//        rotateAnimation.setInterpolator(lin);
//
//    }

    @OnClick(R.id.img_back)
    public void onBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.tv_random_recipe)
    public void onRandomClicked() {
//        if (LoginUtil.checkWhetherLogin(getContext(), PageKey.UserLogin)) {
//            startRecipeGame();
//            checkRandomCookBook();
//        }
//        boolean isLog = Plat.accountService.isLogon();
//        if (isLog) {
            if(randomRecipeBtn.getText().toString().equals("开始")){
                startRecipeGame();

            }else {
                checkRandomCookBook();
            }

//            checkRandomCookBook();
//        }else {
//            startLogin();
//        }
    }
    /**
     * 指向登录界面
     */
    private void startLogin() {
        if (CmccLoginHelper.getInstance().isGetPhone) {

            CmccLoginHelper.getInstance().loginAuth();
        } else {
            CmccLoginHelper.getInstance().login();
        }
    }
    private void checkRandomCookBook() {
        RokiRestHelper.getRamdomCookBook(1, Reponses.ThumbCookbookResponse.class, new RetrofitCallback<Reponses.ThumbCookbookResponse>() {
            @Override
            public void onSuccess(Reponses.ThumbCookbookResponse thumbCookbookResponse) {
                if (null != thumbCookbookResponse && null != thumbCookbookResponse.cookbooks) {
                    List<Recipe> recipes = thumbCookbookResponse.cookbooks;
                    ramdomRecipe = recipes.get(0);
                    recipeLargeUrl = ramdomRecipe.imgMedium;
                    LogUtils.i(TAG, "recipeLargeUrl:" + recipeLargeUrl);
                    if (llRecipeSelect == null){
                        return;
                    }
                    llRecipeSelect.setVisibility(View.VISIBLE);
                    randomRecipeBtn.setVisibility(View.GONE);
                    tvRecipeGameContent.setVisibility(View.GONE);

                    tvRandom.setText( ramdomRecipe.name );
                    tvTodayWhichRecipe.setVisibility(View.GONE);
                    cl_user_game.setVisibility(View.VISIBLE);

                    GlideApp.with(getContext())
                            .load(recipeLargeUrl)
                            .placeholder(R.mipmap.ic_user_default_figure)
                            .apply(RequestOptions.circleCropTransform())
                            .error(R.mipmap.ic_user_default_figure)
                            .into(randomRecipeImg);
                    randomRecipeImg.startAnimation(rotateAnimation);
                }
            }

            @Override
            public void onFaild(String err) {
                GlideApp.with(getContext())
                        .load(R.mipmap.ic_user_default_figure)
                        .placeholder(R.mipmap.ic_user_default_figure)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.mipmap.ic_user_default_figure)
                        .into(randomRecipeImg);
                ToastUtils.show("请求失败，请检查网络或者服务异常", Toast.LENGTH_SHORT);
                llRecipeSelect.setVisibility(View.VISIBLE);
                randomRecipeBtn.setVisibility(View.GONE);
                tvRecipeGameContent.setText("请求失败，请检查网络或者服务异常");
                tvTodayWhichRecipe.setVisibility(View.GONE);
                cl_user_game.setVisibility(View.VISIBLE);
            }
        });
    }

    private void startRecipeGame() {
        randomRecipeBtn.setText(R.string.home_stop);
        llRecipeSelect.setVisibility(View.INVISIBLE);
        randomRecipeBtn.setVisibility(View.INVISIBLE);
        cl_user_game.setVisibility(View.GONE);
        tvTodayWhichRecipe.setVisibility(View.VISIBLE);
        tvTodayWhichRecipe.setText(R.string.home_eat_what);
        tvRecipeGameContent.setText(R.string.home_click_to_get_recipe);
        GlideApp.with(getContext())
//                .asGif2()
                .asGif()
                .load(R.drawable.game)
                .into(randomRecipeImg);
    }


    @OnClick(R.id.tv_choice_recipe)
    public void onChoiceRecipe() {
        if (ramdomRecipe != null) {
            RecipeDetailPage.show(ramdomRecipe.id, ramdomRecipe.sourceType);
        } else {
            ToastUtils.show("请求失败，请检查网络或者服务异常", Toast.LENGTH_SHORT);
        }

    }
    @OnClick(R.id.iv_random_recipe)
    public void onImage() {
        if (ramdomRecipe != null) {
            RecipeDetailPage.show(ramdomRecipe.id, ramdomRecipe.sourceType);
        } else {
            ToastUtils.show("请求失败，请检查网络或者服务异常", Toast.LENGTH_SHORT);
        }

    }
    @OnClick(R.id.tv_change_recipe)
    public void onChageRecipe() {
        randomRecipeImg.setEnabled(false);
        startRecipeGame();
        handler.postDelayed(runnable , 2000);
    }

    @OnClick(R.id.tv_last_week_top)
    public void onLastWeekTopClicked() {
        UIService.getInstance().postPage(PageKey.TopWeekPage);
    }

    @OnClick(R.id.iv_share_recipe)
    public void onShareRecipe() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (selfPermission == 0) {
                CookbookRandomShareDialog.show(cx, ramdomRecipe);
            } else {
                PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_RECIPE_DETAIL_SHARE);
            }
        } else {
            CookbookRandomShareDialog.show(cx, ramdomRecipe);
        }
//        CookbookShareDialog.cookbookShareDialog.addOnFcListener(new CookbookShareDialog.OnFcListener() {
//            @Override
//            public void onFcClick() {
//                startFloatingService();
//            }
//        });
    }

    @SuppressLint("ShowToast")
    public void startFloatingService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            Toast.makeText(activity, "当前无权限，请授权", Toast.LENGTH_SHORT);
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            startActivityForResult(intent, 0);
            return;
        }
//        if (!isStart) {
//            activity.startForegroundService(new Intent(activity, FloatingService.class));
//        }
        EventUtils.postEvent(new FloatHelperEvent(5 ,recipeLargeUrl));
        UIService.getInstance().popBack();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        ButterKnife.reset(this);
    }
}
