package com.robam.roki.ui.page;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.graphic.ImageUtils;
import com.legent.utils.security.MD5Utils;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.services.CookbookManager;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.extension.GlideApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by zhoudingjun on 2017/6/2.
 * 更多主题
 */

public class MoreThomePage extends BasePage {


    LinearLayout llMoreThemes;
    final String _signkey = "B9FAFDD1-BA4F-4AF5-A8D4-1440F7836001";
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.view_theme_recipe_refresh)
    PullToRefreshScrollView viewThemeRecipeRefresh;
    ArrayList<String> timeDataList = new ArrayList<>();

    private MultiTransformation multiTop = new MultiTransformation<>(new CenterCrop(), new RoundedCornersTransformation(50, 10, RoundedCornersTransformation.CornerType.ALL));

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_more_themes, null, false);
        initData();
        llMoreThemes = view.findViewById(R.id.ll_more_themes);
        ButterKnife.inject(this, view);
        viewThemeRecipeRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        viewThemeRecipeRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
            }
        });
        return view;
    }


    private void initData() {
        CookbookManager.getInstance().getThemeRecipes_new(new Callback<List<RecipeTheme>>() {
            @Override
            public void onSuccess(List<RecipeTheme> themes) {
                if (themes == null || themes.size() == 0) {
                    return;
                }
                for (int i  = 0; i < themes.size();i++){
                    LogUtils.i("20171221","more_type:"+themes.get(i).type);
                    if ("2".equals(themes.get(i).type)){
                    }else {
                        String insertTimeDate = themes.get(i).insertTimeDate;
                        LogUtils.i("20171221","insertTimeDate:"+insertTimeDate);
                        timeDataList.add(insertTimeDate);
                    }
                }

                llMoreThemes.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(cx);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                for (RecipeTheme theme : themes) {
                    View view = inflater.inflate(R.layout.view_home_theme_recipe, null, false);
                    LinearLayout ll_themes = view.findViewById(R.id.ll_themes);
                    ImageView iv_img = view.findViewById(R.id.iv_img);
                    TextView tv_subname = view.findViewById(R.id.tv_subname);
                    tv_subname.setText(theme.subName);
                    TextView tv_name = view.findViewById(R.id.tv_name);
                    tv_name.setText(theme.name);
                    TextView tv_number = view.findViewById(R.id.tv_number);
                    long time = Long.parseLong(theme.insertTimeDate);
                    String format = sdf.format(new Date(time));
                    tv_number.setText(format);
                    GlideApp.with(cx)
                            .load(theme.imageUrl)
                            .apply(RequestOptions.bitmapTransform(multiTop))
                            .into(iv_img);
                    ll_themes.setTag(theme);
                    ll_themes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecipeClick(v, timeDataList);
                        }
                    });
                    llMoreThemes.addView(view);
                }
                viewThemeRecipeRefresh.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }
    /**
     * 主题点击调用
     */
    private void RecipeClick(View view,ArrayList<String> list) {
        RecipeTheme theme = (RecipeTheme) view.getTag();
        LogUtils.i("20170824", "size3 :" + list.size());
        Bundle bundle = new Bundle();
        bundle.putSerializable(PageArgumentKey.Theme, theme);
        bundle.putStringArrayList(PageArgumentKey.timeDataList, timeDataList);
        UIService.getInstance().postPage(PageKey.SelectThemeDetailPage, bundle);
//        goToThemeDetail(theme,list);
    }
    /**
     * 主题详情页
     *
     * @param theme
     */
    private void goToThemeDetail(RecipeTheme theme,ArrayList<String> list) {
        LogUtils.i("20170927","type:"+theme.type + " activity:" + theme.activity);
        if ("1".equals(theme.type)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PageArgumentKey.Theme, theme);
            bundle.putStringArrayList(PageArgumentKey.timeDataList,list);
            UIService.getInstance().postPage(PageKey.RecipeThemeDetail, bundle);
        } else if ("100".equals(theme.type)){
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
//                 String url="isbuy=1&openId=B41E9E24-6FA2-4EB5-AE80-586616B8C1FB&key=B9FAFDD1-BA4F-4AF5-A8D4-1440F7836001";
                String urlStr = MD5Utils.Md5(url);
                String urlWeb = theme.activity + "isbuy=" + number + "&" + "openId=" + userid + "&" + "sign=" + urlStr;
                bd.putString(PageArgumentKey.WebTitle, "主题菜谱");
                bd.putString(PageArgumentKey.Url, urlWeb);
                UIService.getInstance().postPage(PageKey.WebClient, bd);
            } else {
                bd.putString(PageArgumentKey.WebTitle, "主题菜谱");
                bd.putString(PageArgumentKey.Url, theme.activity);
                UIService.getInstance().postPage(PageKey.WebClient, bd);
            }

        }else if ("2".equals(theme.type)){
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.WebTitle, theme.getName());
            bd.putString(PageArgumentKey.Url, theme.activity);
            UIService.getInstance().postPage(PageKey.WebClientNew, bd);
        }else{
            RecipeDetailPage.show(Long.valueOf(theme.getRelateCookbookId()), RecipeDetailPage.HomeRecipeView, RecipeRequestIdentification.RECIPE_HOME_RECOMMEND);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
