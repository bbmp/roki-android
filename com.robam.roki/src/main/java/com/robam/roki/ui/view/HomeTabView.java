package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.legent.utils.EventUtils;
import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class HomeTabView extends FrameLayout {


    public interface OnTabSelectedCallback {
        void onTabSelected(int tabIndex);
    }

    @InjectViews({R.id.tab1, R.id.tab2, R.id.tab3,R.id.tab4})
    List<HomeTabItemView> tabs;

    OnTabSelectedCallback callback;

    public HomeTabView(Context context) {
        super(context);
        init(context, null);
    }

    public HomeTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }

    public void setOnTabSelectedCallback(OnTabSelectedCallback callback) {
        this.callback = callback;
    }

    public void selectTab(final int tabIndex) {
        if (tabIndex < 0 || tabIndex >= tabs.size())
            return;

        this.post(new Runnable() {
            @Override
            public void run() {
                tabs.get(tabIndex).performClick();
            }
        });

    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_tab,
                this, true);

        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
//            refreshOrder();
        }

    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3,R.id.tab4})
    public void onClickTab(HomeTabItemView view) {
        int index = tabs.indexOf(view);

        for (HomeTabItemView tab : tabs) {
            tab.setSelected(false);
        }
        view.setSelected(true);

        if (callback != null) {
            callback.onTabSelected(index);
        }

    }

//    @Subscribe
//    public void onEvent(OrderRefreshEvent event) {
//        imgFree.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                refreshOrder();
//            }
//        }, 500);
//    }


//    void refreshOrder() {
//
//        if (Plat.accountService.isLogon()) {
//            StoreService.getInstance().queryOrder(Calendar.getInstance().getTimeInMillis() + 1000 * 60 * 60, 5, new Callback<List<OrderInfo>>() {
//                @Override
//                public void onSuccess(List<OrderInfo> orders) {
//                    final boolean notEmpty = orders != null && orders.size() > 0;
//                    imgFree.setVisibility(notEmpty ? GONE : VISIBLE);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                }
//            });
//        } else {
//            imgFree.setVisibility(VISIBLE);
//        }
//    }
}
