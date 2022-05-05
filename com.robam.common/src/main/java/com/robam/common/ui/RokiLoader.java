package com.robam.common.ui;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.legent.ui.UIService;
import com.legent.ui.ext.loaders.SimpleTitleBarLoader;
import com.legent.ui.ext.views.TitleBar;
import com.robam.common.R;

/**
 * Created by sylar on 15/7/26.
 */
public class RokiLoader extends SimpleTitleBarLoader{

    @Override
    protected int getBackIconResid() {
        return R.drawable.ic_baseline_arrow_back_black_24;
    }

    @Override
    protected int getMenuIconResid() {
        return super.getMenuIconResid();
    }

    @Override
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
        view.setLayoutParams(new AbsListView.LayoutParams(dip2px(cx, 95), dip2px(cx, 50)));
        view.setImageResource(iconResid);
        return view;
    }
}
