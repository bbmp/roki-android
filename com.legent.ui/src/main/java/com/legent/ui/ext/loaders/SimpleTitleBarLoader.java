package com.legent.ui.ext.loaders;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.legent.ui.IPage;
import com.legent.ui.UIService;
import com.legent.ui.ext.views.TitleBar;

/**
 * Created by sylar on 15/7/26.
 */
public class SimpleTitleBarLoader extends AbsTitleBarLoader {


    @Override
    protected void onPageActivated(TitleBar bar, String pageKey) {
        IPage page = UIService.getInstance().getPage(pageKey);

        // 设置中间标题文本
        bar.setTitle(page.getPageTitle());
        //设置左侧菜单icon
        View iconView = getMenuIcon(bar.getContext());
        if (iconView != null) {
            bar.replaceLeft(iconView);
        }
    }

    protected View getMenuIcon(Context cx) {

        // 设置menu图标
        final boolean isMainForm = UIService.getInstance().isMainForm();
        final boolean isHome = UIService.getInstance().isHomePage(pageKey);
        int iconResid = (isMainForm && isHome) ? getMenuIconResid() : getBackIconResid();

        if (iconResid == 0) return null;

        ImageView view = TitleBar.newTitleIconView(cx, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMainForm) {
                    if (isHome)
                        UIService.getInstance().getMain().toggleMenu();
                    else
                        UIService.getInstance().popBack();
                } else {
                    if (isHome)
                        UIService.getInstance().getMain().getActivity().finish();
                    else
                        UIService.getInstance().popBack();
                }
            }
        });
        view.setImageResource(iconResid);
        view.setLayoutParams(new AbsListView.LayoutParams(dip2px(cx, 65), dip2px(cx, 50)));
        view.setImageResource(iconResid);
        return view;
    }

    /*转换为dp单位 */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    protected int getMenuIconResid() {
        return android.R.drawable.ic_menu_manage;
    }

    protected int getBackIconResid() {
        return android.R.drawable.ic_menu_revert;
    }

    @Override
    public void layout(FragmentActivity main) {
        super.layout(main);
    }
}
