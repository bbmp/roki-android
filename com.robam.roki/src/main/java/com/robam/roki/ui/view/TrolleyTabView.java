package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;
import butterknife.OnClick;

public class TrolleyTabView extends FrameLayout {

    public static final int TAB_RECIPE = 0;
    public static final int TAB_MATERIAL = 1;
    public static final int TAB_CONDIMENT = 2;

    public interface OnTabSelectedCallback {
        void onTabSelected(int tabIndex);
    }


    @InjectViews({R.id.txtTab1, R.id.txtTab2, R.id.txtTab3})
    List<View> tabs;

    OnTabSelectedCallback callback;

    public TrolleyTabView(Context context) {
        super(context);
        init(context, null);
    }

    public TrolleyTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TrolleyTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_trolley_tab,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void setOnTabSelectedCallback(OnTabSelectedCallback callback) {
        this.callback = callback;
    }

    @OnClick({R.id.txtTab1, R.id.txtTab2, R.id.txtTab3})
    public void onClickTab(View tab) {
        for (View view : tabs) {
            view.setSelected(false);
        }

        tab.setSelected(true);
        int index = tabs.indexOf(tab);
        if (callback != null) {
            callback.onTabSelected(index);
        }
    }

    public void selectedTab(int tabIndex) {
        if (tabIndex < 0 || tabIndex >= tabs.size())
            return;
        tabs.get(tabIndex).performClick();
    }

}
