//package com.robam.roki.ui.view;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.google.common.collect.Lists;
//import com.google.common.eventbus.Subscribe;
//import com.legent.Callback;
//import com.legent.VoidCallback;
//import com.legent.plat.Plat;
//import com.legent.plat.events.RecipeOpenEvent;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.adapters.ExtPageAdapter;
//import com.legent.ui.ext.dialogs.DialogHelper;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.ExtViewPager;
//import com.legent.ui.ext.views.TitleBar;
//import com.legent.utils.EventUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.graphic.ImageUtils;
//import com.robam.common.events.FavorityBookRefreshEvent;
//import com.robam.common.events.OrderRefreshEvent;
//import com.robam.common.events.TodayBookRefreshEvent;
//import com.robam.common.io.cloud.Reponses;
//import com.robam.common.io.cloud.Reponses.CookbooksResponse;
//import com.robam.common.pojos.Materials;
//import com.robam.common.pojos.OrderInfo;
//import com.robam.common.services.CookbookManager;
//import com.robam.common.services.StoreService;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.ui.UIListeners;
//
//import java.util.Calendar;
//import java.util.List;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
//public class HomeTrolleyView extends FrameLayout implements UIListeners.IRefresh {
//
//    @InjectView(R.id.titleView)
//    TitleBar titleBar;
//    @InjectView(R.id.emptyView)
//    View emptyView;
//    @InjectView(R.id.img_event)
//    ImageView event;
//    @InjectView(R.id.mainView)
//    LinearLayout mainView;
//    @InjectView(R.id.tabView)
//    TrolleyTabView tabView;
//    @InjectView(R.id.pager)
//    ExtViewPager pager;
//
//
//    boolean isEnableOrder = false;
//
//    Context cx;
//    ExtPageAdapter adapter;
//    TrolleyRecipeView recipeView;
//    TrolleyMaterialView mainMaterialView;
//    TrolleyMaterialView slaveMaterialView;
//    ImageView iconThird, iconOrder, iconClear;
//
//
//    public HomeTrolleyView(Context context) {
//        super(context);
//        init(context, null);
//    }
//
//    public HomeTrolleyView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    public HomeTrolleyView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(context, attrs);
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        EventUtils.regist(this);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        EventUtils.unregist(this);
//    }
//
//    @Subscribe
//    public void onEvent(TodayBookRefreshEvent event) {
//        onRefreshTrolley();
//    }
//
//    @Subscribe
//    public void onEvent(FavorityBookRefreshEvent event) {
//        onRefreshTrolley();
//    }
//
//    @Subscribe
//    public void onEvent(OrderRefreshEvent event) {
//        iconOrder.postDelayed(new Runnable() {
//            @Override
//            public void onSuccess(Boolean b) {
//                refreshOrder(b);
//            public void run() {
//                refreshOrder(true);
//            }
//        }, 500);
//    }
//
//    @Subscribe
//    public void onEvent(RecipeOpenEvent event) {
//        getOrderEnable();
//        initStatusData();
//    }
//
//    void init(Context cx, AttributeSet attrs) {
//        this.cx = cx;
//        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_trolley,
//                this, true);
//        if (!view.isInEditMode()) {
//            ButterKnife.inject(this, view);
//            setTitleBar();
//
//            adapter = new ExtPageAdapter();
//            adapter.loadViews(createViews());
//            pager.setAdapter(adapter);
//
//            tabView.setOnTabSelectedCallback(tabCallback);
//            tabView.selectedTab(TrolleyTabView.TAB_RECIPE);
//            initStatusData();
//            onRefresh();
//        }
//    }
//
//    private void initStatusData() {
//        StoreService.getInstance().getEventStatus(new Callback<Reponses.EventStatusReponse>() {
//            @Override
//            public void onSuccess(Reponses.EventStatusReponse eventStatusReponse) {
//                ImageUtils.displayImage(eventStatusReponse.image, event, ImageUtils.defaultOptions);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
//
//    void setTitleBar() {
//        titleBar.setTitle("购物车");
//
//
//        iconThird = TitleBar.newTitleIconView(cx, R.mipmap.ic_home_trolley_thirdplat, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UIService.getInstance().postPage(PageKey.TrolleyThirdPlat);
//            }
//        });
//
//        titleBar.replaceLeft(iconThird);
//
//
//        iconOrder = TitleBar.newTitleIconView(cx, R.mipmap.ic_home_trolley_order, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UIService.getInstance().postPage(PageKey.OrderList);
//            }
//        });
//
//        titleBar.replaceRight(iconOrder);
//
//        iconClear = TitleBar.newTitleIconView(cx, R.mipmap.ic_home_trolley_clear, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogHelper.newDialog_OkCancel(getContext(), "确定清空购物车？", null, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (which == DialogInterface.BUTTON_POSITIVE) {
//                            onClear();
//                        }
//                    }
//                }).show();
//            }
//        });
//        titleBar.addRight(iconClear);
//
//    }
//
//
//    @Override
//    public void onRefresh() {
//
//        iconClear.setVisibility(GONE);
//        getOrderEnable();
//
//        onRefreshTrolley();
//    }
//
//    void onRefreshTrolley() {
//        boolean isAuth = Plat.accountService.isLogon();
//        if (!isAuth) {
//            switchView(true);
//            recipeView.loadData(null);
//            mainMaterialView.loadData(null);
//            slaveMaterialView.loadData(null);
//        } else {
//            refreshRecipes();
//        }
//    }
//
//
//    void getOrderEnable() {
////        StoreService.getInstance().orderIfOpen(new Callback<Boolean>() {
////            @Override
////            public void onSuccess(Boolean b) {
////                refreshOrder(b);
////            }
////
////            @Override
////            public void onFailure(Throwable throwable) {
////                refreshOrder(false);
////            }
////        });
//        refreshOrder(true);
//    }
//
//    void refreshOrder(boolean enalble) {
//        isEnableOrder = enalble;
//        iconOrder.setVisibility(GONE);
//        recipeView.setOrderEnable(isEnableOrder);
//        //iconThird.setVisibility(isEnableOrder ? VISIBLE : GONE);
//
//        if (isEnableOrder && Plat.accountService.isLogon()) {
//            StoreService.getInstance().queryOrder(Calendar.getInstance().getTimeInMillis() + 1000 * 60 * 60, 5, new Callback<List<OrderInfo>>() {
//                @Override
//                public void onSuccess(List<OrderInfo> orders) {
//                    final boolean notEmpty = orders != null && orders.size() > 0;
//                    iconOrder.setVisibility(notEmpty ? VISIBLE : GONE);
//                }
//
//
//                @Override
//                public void onFailure(Throwable t) {
//                   ToastUtils.showThrowable(t);
//                }
//            });
//        }
//    }
//
//    void refreshRecipes() {
//        ProgressDialogHelper.setRunning(cx, true);
//        CookbookManager.getInstance().getTodayCookbooks(new Callback<CookbooksResponse>() {
//            @Override
//            public void onSuccess(CookbooksResponse res) {
//                ProgressDialogHelper.setRunning(cx, false);
//
//                boolean isEmpty = (res == null || res.cookbooks == null || res.cookbooks.size() == 0);
//                switchView(isEmpty);
//
//                if (!isEmpty) {
//                    recipeView.loadData(res.cookbooks);
//                    refreshMaterrials();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ProgressDialogHelper.setRunning(cx, false);
//                ToastUtils.showThrowable(t);
//            }
//        });
//    }
//
//    void refreshMaterrials() {
//
//        CookbookManager.getInstance().exportMaterialsFromToday(new Callback<Materials>() {
//            @Override
//            public void onSuccess(Materials materials) {
//                if (materials != null) {
//                    mainMaterialView.loadData(materials.getMain());
//                    slaveMaterialView.loadData(materials.getAccessory());
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });
//    }
//
//    void onClear() {
//        ProgressDialogHelper.setRunning(cx, true);
//        CookbookManager.getInstance().deleteAllTodayCookbook(new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                ProgressDialogHelper.setRunning(cx, false);
//                onRefresh();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ProgressDialogHelper.setRunning(cx, false);
//                ToastUtils.showThrowable(t);
//            }
//        });
//    }
//
//    void switchView(boolean isEmpty) {
//        emptyView.setVisibility(isEmpty ? VISIBLE : GONE);
//        mainView.setVisibility(!isEmpty ? VISIBLE : GONE);
//
//        iconClear.setVisibility(!isEmpty ? VISIBLE : GONE);
//    }
//
//    List<View> createViews() {
//        List<View> views = Lists.newArrayList();
//        recipeView = new TrolleyRecipeView(cx);
//        mainMaterialView = new TrolleyMaterialView(cx);
//        slaveMaterialView = new TrolleyMaterialView(cx);
//        views.add(recipeView);
//        views.add(mainMaterialView);
//        views.add(slaveMaterialView);
//        return views;
//    }
//
//    TrolleyTabView.OnTabSelectedCallback tabCallback = new TrolleyTabView.OnTabSelectedCallback() {
//        @Override
//        public void onTabSelected(int tabIndex) {
//            pager.setCurrentItem(tabIndex, true);
//        }
//    };
//
//
//}
