package com.robam.roki.ui.page;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.EventUtils;
import com.legent.utils.api.DisplayUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.EmojiEmptyView;
import com.robam.roki.ui.view.RecipeGridTitleView;
import com.robam.roki.ui.view.RecipeGridView;
import com.robam.roki.ui.view.ThemeListViewItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rent on 2016/8/26.
 * 我的收藏
 */

public class AbsThemeRecipeListGridPage extends MyBasePage<MainActivity> {
    //未收藏显示

    private ViewPager viewPager;//中间显示内容
    private TabLayout tabLayout;//顶部导航
    private TextView tvDelete;

    private List<String> titles = new ArrayList<>();//标题
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.abs_page_recipetheme_listgrid;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        tvDelete = findViewById(R.id.tv_delete);

        fragments.add(new AbsCollectRecipeThemePage());
        fragments.add(new AbsCollectRecipePage());
        titles.add("主题菜单");
        titles.add("菜谱");

        //添加tab标签
        for (int i=0;i<titles.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }
        //添加设置适配器
        viewPager.setAdapter(new CollectViewPagerAdapter(getParentFragmentManager()));
        //把TabLayout与ViewPager关联起来
        tabLayout.setupWithViewPager(viewPager);
        setOnClickListener(R.id.tv_delete, R.id.img_back);
    }

    @Override
    protected void initData() {
//        if (!rootView.isInEditMode()) {
//            emptyView.setText(getTextWhenEmpty());
//            init();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), "我的收藏页", null);
    }

    private void init() {
//        regsitRightView();//清空按钮
//        initThemeData();//获取收藏主题
    }

    @Subscribe
    public void onEvent(HomeRecipeViewEvent event) {
        init();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back)
            UIService.getInstance().popBack();
        else if (view.getId() == R.id.tv_delete) {
            onClear();
        }
    }

    protected String getTextWhenEmpty() {
        return "还没有收藏呢";
    }

    void onClear() {
        DialogHelper.newDialog_OkCancel(cx, "确认清空我的收藏？", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    ProgressDialogHelper.setRunning(cx, true);
                    CookbookManager.getInstance().delteAllFavorityCookbooks(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ProgressDialogHelper.setRunning(cx, false);
                            ToastUtils.showShort("清空成功");
                            init();
                            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.ClearMyFavorite));
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ProgressDialogHelper.setRunning(cx, false);
                            ToastUtils.showThrowable(t);
                        }
                    });
                }
            }
        }).show();

    }

    /**
     * 初始化右上角清空按钮
     */
//    void regsitRightView() {
//        TextView txtView = TitleBar.newTitleTextView(cx, "清空", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!emptyView.isShown())
//                    onClear();
//            }
//        });
//        txtView.setTextColor(Color.rgb(255, 180, 0));
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Subscribe
    public void onEvent(PageBackEvent event) {

    }

    class CollectViewPagerAdapter extends FragmentStatePagerAdapter {


        public CollectViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
