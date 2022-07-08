//package com.robam.roki.ui.view;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.fragment.app.FragmentActivity;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.google.common.base.Strings;
//import com.google.common.collect.Lists;
//import com.google.common.eventbus.Subscribe;
//import com.jauker.widget.BadgeView;
//import com.legent.Callback;
//import com.legent.plat.Plat;
//import com.legent.plat.events.ChatNewMsgEvent;
//import com.legent.plat.events.UserLoginEvent;
//import com.legent.plat.events.UserLogoutEvent;
//import com.legent.plat.events.UserUpdatedEvent;
//import com.legent.plat.pojos.User;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.adapters.ExtBaseAdapter;
//import com.legent.utils.EventUtils;
//import com.legent.utils.LogUtils;
//import com.legent.utils.graphic.BitmapUtils;
//import com.legent.utils.graphic.ImageUtils;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.robam.common.events.CookMomentsRefreshEvent;
//import com.robam.common.events.FavorityBookRefreshEvent;
//import com.robam.common.events.HomeRecipeViewEvent;
//import com.robam.common.io.cloud.Reponses;
//import com.robam.common.io.cloud.Reponses.CookbooksResponse;
//import com.robam.common.pojos.CookAlbum;
//import com.robam.common.pojos.MallManagement;
//import com.robam.common.pojos.RecipeTheme;
//import com.robam.common.pojos.YouzanOrdersCount;
//import com.robam.common.services.CookbookManager;
//import com.robam.common.services.StoreService;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.ui.UIListeners;
//import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
//import com.robam.roki.utils.FastBlurUtils;
//import com.robam.roki.utils.GlideCircleTransform;
//import com.robam.roki.utils.LoginUtil;
//import com.yatoooon.screenadaptation.ScreenAdapterTools;
//
//import java.util.List;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//import butterknife.OnItemClick;
//
//import static com.legent.ContextIniter.cx;
//
///**
// * 我
// */
//public class HomePersonalView extends FrameLayout implements UIListeners.IRefresh, View.OnTouchListener {
//    private static final String TAG = "HomePersonalView";
//
//    final int ID_Favority = 0;
//    final int ID_CookMoments = 1;
//    final int ID_TrolleyBus = 2;
//    final int ID_DeviceManager = 3;
//    final int ID_SaleService = 4;
//    final int ID_Maintain = 5;
//    final int ID_About = 6;
//    final int ID_OrderDetail = 7;
//    final int ID_TEST = 8;
//    final int Food_shopping = 9;
//    final int Item_bg = 10;
//    final int item_setting=11;
////    final int ID_SPEECH_SWITCH = 11;
//
//    /*String[] ordersKey = {"ALL_WAIT_PAY","TRADE_NO_CREATE_PAY","WAIT_BUYER_PAY","WAIT_GROUP",
//            "WAIT_SELLER_SEND_GOODS", "WAIT_BUYER_CONFIRM_GOODS","TRADE_BUYER_SIGNED","TRADE_CLOSED"
//            ,"WAIT_BUYER_PAY","TRADE_NO_CREATE_PAY","ALL_CLOSED"};*/
//
//    String[] ordersKey = {"WAIT_SELLER_SEND_GOODS", "WAIT_BUYER_CONFIRM_GOODS"};
//
//    @InjectView(R.id.imgFigure)
//    ImageView imgUser;
//    @InjectView(R.id.txtFigure)
//    TextView txtUser;
//    @InjectView(R.id.listview)
//    ListView listview;
//
//    Adapter adapter;
//    @InjectView(R.id.ll_bg)
//    LinearLayout llBg;
//
//    FragmentActivity activity ;
//    private int count = 0;
//
//    public HomePersonalView(Context context) {
//        super(context);
//        init(context, null);
//    }
//    public HomePersonalView(Context context ,FragmentActivity activity) {
//        super(context);
//        this.activity = activity ;
//        init(context, null);
//    }
//    public HomePersonalView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    public HomePersonalView(Context context, AttributeSet attrs, int defStyle) {
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
//    public void onEvent(UserLogoutEvent event) {
//        onRefresh();
//        initOrdersData();
//    }
//
//    @Subscribe
//    public void onEvent(UserLoginEvent event) {
//        onRefresh();
//        initOrdersData();
//    }
//
//
//    @Subscribe
//    public void onEvent(UserUpdatedEvent event) {
//        onRefresh();
//    }
//
//    @Subscribe
//    public void onEvent(CookMomentsRefreshEvent event) {
//        onRefresh();
//    }
//
//    @Subscribe
//    public void onEvent(FavorityBookRefreshEvent event) {
//        onRefresh();
//    }
//
//    @Subscribe
//    public void onEvent(HomeRecipeViewEvent event) {
//        onRefresh();
//    }
//
//    @Subscribe
//    public void onEvent(ChatNewMsgEvent event) {
//        boolean hasNew = event.hasNew;
//        if (hasNew) {
//            listview.getChildAt(list.size() - 2).findViewById(R.id.tv_red_dot_server).setVisibility(VISIBLE);
//        } else {
//            listview.getChildAt(list.size() - 2).findViewById(R.id.tv_red_dot_server).setVisibility(GONE);
//        }
//    }
//
//    void init(Context cx, AttributeSet attrs) {
//        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_personal, this, true);
//        if (!view.isInEditMode()) {
//            ScreenAdapterTools.getInstance().loadView(view);
//            ButterKnife.inject(this, view);
//            initOrdersData();
//            adapter = new Adapter();
//            buildItems();
//            listview.setAdapter(adapter);
//            listview.setOnTouchListener(this);
//        }
//    }
//
//
//    /**
//     * 初始化订单数据
//     */
//    protected void initOrdersData() {
//
//        boolean logon = Plat.accountService.isLogon();
//        if (logon) {
//            long userId = Plat.accountService.getCurrentUserId();
//            CookbookManager.getInstance().getYouzanOrders(userId, ordersKey,
//                    new Callback<Reponses.YouzanOrdersReponses>() {
//                        @Override
//                        public void onSuccess(Reponses.YouzanOrdersReponses result) {
//
//                            List<YouzanOrdersCount> statusCount = result.statusCount;
//                            if (null == statusCount || statusCount.size() == 0) {
//                                return;
//                            }
//                            int count1 = 0;
//                            int count2 = 0;
//                            int countNum = 0;
//                            for (YouzanOrdersCount OrdersCount : statusCount) {
//                                if (("WAIT_SELLER_SEND_GOODS").equals(OrdersCount.status)) {
//                                    count1 = OrdersCount.count;
//                                } else if (("WAIT_BUYER_CONFIRM_GOODS").equals(OrdersCount.status)) {
//                                    count2 = OrdersCount.count;
//                                    LogUtils.i("20170721", "count2:" + count2);
//                                }
//                                countNum = count1 + count2;
//                            }
//
//                            count = countNum;
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//
//                        }
//                    });
//        } else {
//            count = 0;
//        }
//    }
//
//    @OnItemClick(R.id.listview)
//    public void onItemClicked(AdapterView<?> parent, View view, int position, long id) {
//        ListItem item = (ListItem) adapter.getItem(position);
//        final UIService ui = UIService.getInstance();
//        final Context cx = getContext();
//        switch (item.id) {
//
//            case ID_About://关于ROKI
//                UIService.getInstance().postPage(PageKey.About);
////                UIService.getInstance().postPage(PageKey.TellRoki);
////                UIService.getInstance().postPage(PageKey.RandomRecipe);
//                break;
//
//            case ID_SaleService://售后服务
////                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin))
////                    UIService.getInstance().postPage(PageKey.SaleService);
//                                if (LoginUtil.checkWhetherLogin2(cx, activity))
//                    UIService.getInstance().postPage(PageKey.SaleService);
//                break;
//            case item_setting:
//                LogUtils.i(TAG,"item_setting");
////                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin))
////                UIService.getInstance().postPage(PageKey.Setting);
//                if (LoginUtil.checkWhetherLogin2(cx, activity))
//                    UIService.getInstance().postPage(PageKey.Setting);
//                break;
//            case ID_DeviceManager://厨电管理
////                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin))
////                    ui.postPage(PageKey.DeviceManager);
//                if (LoginUtil.checkWhetherLogin2(cx, activity))
//                    UIService.getInstance().postPage(PageKey.DeviceManager);
//                break;
//            case Food_shopping://食材商城
//                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin)) {
//                    Bundle shoppingBundle = new Bundle();
//                    shoppingBundle.putString("pageUrl", item.pageUrl);
//                    ui.postPage(PageKey.Youzan, shoppingBundle);
//                }
//                break;
//            case ID_Favority://收藏
////                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin))
////                    ui.postPage(PageKey.AbsThemeRecipeListGrid);
//                if (LoginUtil.checkWhetherLogin2(cx, activity))
//                    UIService.getInstance().postPage(PageKey.AbsThemeRecipeListGrid);
//                break;
//            case ID_CookMoments://晒厨艺
////                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin))
////                    ui.postPage(PageKey.RecipeCookMoments);
//                if (LoginUtil.checkWhetherLogin2(cx, activity))
//                    UIService.getInstance().postPage(PageKey.RecipeCookMoments);
//                break;
//            case ID_TrolleyBus://购物车
//                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin)) {
//                    Bundle trolleyBusBundle = new Bundle();
//                    trolleyBusBundle.putString("pageUrl", item.pageUrl);
//                    ui.postPage(PageKey.YouzanTrolleyBus, trolleyBusBundle);
//                }
//
//                break;
//            case ID_OrderDetail://订单详情
//                if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin)) {
//                    Bundle orderBundle = new Bundle();
//                    orderBundle.putString("pageUrl", item.pageUrl);
//                    ui.postPage(PageKey.YouzanOrder, orderBundle);
//                }
//
//                break;
//            default:
//                break;
//            //语音控制
////            case ID_SPEECH_SWITCH:
////                ui.postPage(PageKey.SpeechSwitchPage);
////                break;
//        }
//    }
//
//    @OnClick(R.id.imgFigure)
//    public void onClickFigure() {
//        boolean isLogin = Plat.accountService.isLogon();
//        if (isLogin) {
//            UIService.getInstance().postPage(PageKey.UserInfo);
//        } else {
////            UIService.getInstance().postPage(PageKey.UserLogin);
//            startLogin();
//        }
//    }
//
//    /**
//     * 指向登录界面
//     */
//    private void  startLogin(){
//        CmccLoginHelper instance = CmccLoginHelper.getInstance();
//        instance.initSdk(activity );
//        instance.getPhnoeInfo();
//    }
//
//
//    @OnClick(R.id.txtFigure)
//    public void onClickTxtFigure() {
//        boolean isLogin = Plat.accountService.isLogon();
//        if (isLogin) {
//            UIService.getInstance().postPage(PageKey.UserInfo);
//        } else {
////            UIService.getInstance().postPage(PageKey.UserLogin);
//            startLogin();
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//
//        boolean isLogin = Plat.accountService.isLogon();
//        if (isLogin) {
//            User user = Plat.accountService.getCurrentUser();
//            txtUser.setText(Strings.isNullOrEmpty(user.name) ? user.phone : user.name);
//            if (!Strings.isNullOrEmpty(user.figureUrl)) {
//                imgUrlToBitmap(user.figureUrl);
////                Bitmap bitmap = ImageUtils.loadImageSync(user.figureUrl);
//                //登录后设置白色圆边
//                Glide.with(cx)
//                        .load(user.figureUrl)
////                        .asBitmap()
//                        .placeholder(R.mipmap.ic_user_default_figure)
////                        .transform(new GlideCircleTransform(cx, 1, cx.getResources().getColor(R.color.White)))
////                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .into(imgUser);
//            }
//        } else {
//            txtUser.setText(R.string.home_user_login_text);
//            imgUser.setImageResource(R.mipmap.ic_user_default_figure);
//            Bitmap bitmap = BitmapUtils.fromResource(cx, R.mipmap.img_bg);
//            Bitmap newBitmap = FastBlurUtils.doBlur(bitmap, 12, false);
//            Bitmap new2Bitmap = BitmapUtils.cropBitmap(newBitmap);
//            Drawable drawable = BitmapUtils.toDrawable(getContext(), new2Bitmap);
//            llBg.setBackground(drawable);
//        }
//
//        adapter.loadData(list);
//    }
//
//    //将链接转换为bitmap增减增加模糊并设为背景
//    private void imgUrlToBitmap(String figureUrl) {
//        ImageUtils.loadImage(figureUrl, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                if (loadedImage != null) {
//                    Bitmap newBitmap = FastBlurUtils.doBlur(loadedImage, 12, false);
//                    Bitmap new2Bitmap = BitmapUtils.cropBitmap(newBitmap);
//                    Drawable drawable = BitmapUtils.toDrawable(getContext(), new2Bitmap);
//                    llBg.setBackground(drawable);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//            }
//        });
//    }
//
//    final List<ListItem> list = Lists.newArrayList();
//
//    private void buildItems() {
//        CookbookManager.getInstance().getMallManagement(new Callback<Reponses.MallManagementResponse>() {
//            @Override
//            public void onSuccess(Reponses.MallManagementResponse mallManagementResponse) {
//                if (mallManagementResponse == null) return;
//                List<MallManagement> mallManagements = mallManagementResponse.mMallManagements;
//                if (mallManagements == null || mallManagements.size() == 0) return;
//
//                for (int i = 0; i < mallManagements.size(); i++) {
//                    if (cx.getString(R.string.my_order).equals(mallManagements.get(i).title)) {
//                        MallManagement mallManagement = mallManagements.get(i);
//                        if (mallManagement.isShow == 1) {
//                            list.add(new ListItem(ID_OrderDetail, mallManagement.title, mallManagement.portraitUrl, mallManagement.url));
//
//
//                        }
//                    } else if (cx.getString(R.string.my_trolley_bus).equals(mallManagements.get(i).title)) {
//                        MallManagement mallManagement = mallManagements.get(i);
//                        if (mallManagement.isShow == 1) {
//                            list.add(new ListItem(ID_TrolleyBus, mallManagement.title, mallManagement.portraitUrl, mallManagement.url));
//                        }
//                    }
//                    if (cx.getString(R.string.my_food_shopping).equals(mallManagements.get(i).title)) {
//                        MallManagement mallManagement = mallManagements.get(i);
//                        if (mallManagement.isShow == 1) {
//                            list.add(new ListItem(Food_shopping, mallManagement.title, mallManagement.portraitUrl, mallManagement.url));
//                        }
//                    }
//                }
//                list.add(new ListItem(ID_Favority, cx.getString(R.string.my_collect), R.mipmap.ic_my_item_favority));
//                list.add(new ListItem(ID_CookMoments, cx.getString(R.string.my_cook_moments), R.mipmap.ic_my_item_cook_moments));
//                list.add(new ListItem(ID_DeviceManager, cx.getString(R.string.my_device), R.mipmap.ic_my_item_device));
//                list.add(new ListItem(ID_SaleService, cx.getString(R.string.my_sale_service), R.mipmap.ic_my_item_sale_service));
////                list.add(new ListItem(ID_SPEECH_SWITCH, "语音控制", R.mipmap.speech));
//                list.add(new ListItem(ID_About, cx.getString(R.string.my_about), R.mipmap.ic_my_item_about));
//                list.add(new ListItem(item_setting,cx.getString(R.string.my_setting),R.mipmap.ic_my_item_setting));
//                adapter.loadData(list);
//                onRefresh();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                //如果获取动态接口加载失败，默认要展示的本地功能
//                list.add(new ListItem(ID_Favority, cx.getString(R.string.my_collect), R.mipmap.ic_my_item_favority));
//                list.add(new ListItem(ID_CookMoments, cx.getString(R.string.my_cook_moments), R.mipmap.ic_my_item_cook_moments));
//                list.add(new ListItem(ID_DeviceManager, cx.getString(R.string.my_device), R.mipmap.ic_my_item_device));
//                list.add(new ListItem(ID_SaleService, cx.getString(R.string.my_sale_service), R.mipmap.ic_my_item_sale_service));
////                list.add(new ListItem(ID_SPEECH_SWITCH, "语音控制", R.mipmap.ic_my_item_about));
//                list.add(new ListItem(ID_About, cx.getString(R.string.my_about), R.mipmap.ic_my_item_about));
//                list.add(new ListItem(item_setting, cx.getString(R.string.my_setting), R.mipmap.ic_my_item_setting));
//                adapter.loadData(list);
//                onRefresh();
//            }
//        });
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN://0
//                break;
//            case MotionEvent.ACTION_UP://1
//                initOrdersData();
//                break;
//            case MotionEvent.ACTION_MOVE://2
//        }
//        return false;
//    }
//
//    class ListItem {
//
//        int id;
//        int imgResid;
//        String title;
//        String imgUrl;
//        String pageUrl;
//
//
//        public ListItem(int id, String title, int img) {
//            this.id = id;
//            this.title = title;
//            this.imgResid = img;
//        }
//
//        public ListItem(int id, String title, String img, String url) {
//            this.id = id;
//            this.title = title;
//            this.imgUrl = img;
//            this.pageUrl = url;
//        }
//    }
//
//    class Adapter extends ExtBaseAdapter<ListItem> {
//
//        ListItem item;
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder vh;
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_personal_item, parent, false);
//                ScreenAdapterTools.getInstance().loadView(convertView);
//                vh = new ViewHolder(convertView);
//                convertView.setTag(vh);
//            } else {
//                vh = (ViewHolder) convertView.getTag();
//            }
//
//            item = (ListItem) getItem(position);
//            vh.showData(item);
//
//            return convertView;
//        }
//
//        class ViewHolder {
//            @InjectView(R.id.txtTitle)
//            TextView txtTitle;
//            @InjectView(R.id.txtTip)
//            TextView txtTip;
//            @InjectView(R.id.imgIcon)
//            ImageView imgIcon;
//            View view;
//            @InjectView(R.id.bv_view_count)
//            BadgeView bvViewCount;
//            @InjectView(R.id.bv_view_sp)
//            BadgeView bvViewSp;
//            @InjectView(R.id.rl_item_bg)
//            RelativeLayout rlItemBg;
//            @InjectView(R.id.view_line)
//            View viewLine;
//            @InjectView(R.id.ll_zh_line)
//            LinearLayout llZhLine;
//            @InjectView(R.id.tv_red_dot_server)
//            TextView tvRedDotServer;
//
//
//            ViewHolder(View view) {
//                this.view = view;
//                ButterKnife.inject(this, view);
//            }
//
//            void showData(final ListItem item) {
//                if (TextUtils.isEmpty(item.imgUrl)) {
//                    imgIcon.setImageResource(item.imgResid);
//                } else {
//                    Glide.with(cx).load(item.imgUrl).into(imgIcon);
//                }
//                txtTitle.setText(item.title);
//                if (cx.getString(R.string.my_order).equals(item.title)) {
//                    bvViewCount.setVisibility(VISIBLE);
//                    bvViewCount.setBadgeCount(count);
//                } else if (cx.getString(R.string.my_food_shopping).equals(item.title)) {
//                    bvViewSp.setVisibility(VISIBLE);
//                    bvViewSp.setTextSize(10);
//                    bvViewSp.setText("NEW");
//                } else {
//                    bvViewCount.setVisibility(GONE);
//                    bvViewSp.setVisibility(GONE);
//                }
//
//                if (item.id == Food_shopping || item.id == ID_DeviceManager) {//商城和厨电底部增加空白格
//                    viewLine.setVisibility(GONE);
//                    rlItemBg.setVisibility(VISIBLE);
//                } else {
//                    viewLine.setVisibility(VISIBLE);
//                    rlItemBg.setVisibility(GONE);
//                }
//
//                if (item.id == item_setting) {//设置底部画条全线
//                    viewLine.setVisibility(GONE);
//                    llZhLine.setVisibility(VISIBLE);
//                }
//
//                if (Plat.accountService.isLogon()) {
//                    view.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            updateTip(item.id);
//                        }
//                    }, 50);
//                } else {
//                    showTip(0);
//                }
//            }
//
//            void updateTip(int id) {
//                switch (id) {
//                    case ID_Favority:
//                        CookbookManager.getInstance().getFavorityCookbooks(new Callback<CookbooksResponse>() {
//                            @Override
//                            public void onSuccess(CookbooksResponse res) {
//                                int tip = 0;
//                                if (res != null) {
//                                    if (res.cookbooks != null) {
//                                        tip += res.cookbooks.size();
//                                    }
//                                    if (res.cookbooks3rd != null) {
//                                        tip += res.cookbooks3rd.size();
//                                    }
//                                }
//                                final int finalTip = tip;
//                                StoreService.getInstance().getMyFavoriteThemeRecipeList(new Callback<List<RecipeTheme>>() {
//                                    @Override
//                                    public void onSuccess(List<RecipeTheme> recipeThemes) {
//                                        if (recipeThemes == null || recipeThemes.size() == 0) {
//                                            showTip(finalTip);
//                                            return;
//                                        }
//                                        showTip(finalTip + recipeThemes.size());
//                                    }
//
//                                    @Override
//                                    public void onFailure(Throwable t) {
//                                        showTip(finalTip);
//                                        t.printStackTrace();
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onFailure(Throwable throwable) {
//                                StoreService.getInstance().getMyFavoriteThemeRecipeList(new Callback<List<RecipeTheme>>() {
//                                    @Override
//                                    public void onSuccess(List<RecipeTheme> recipeThemes) {
//                                        if (recipeThemes == null || recipeThemes.size() == 0) {
//                                            showTip(0);
//                                            return;
//                                        }
//                                        showTip(recipeThemes.size());
//                                        LogUtils.i("20170510", "size:" + recipeThemes.size());
//                                    }
//
//                                    @Override
//                                    public void onFailure(Throwable t) {
//                                        t.printStackTrace();
//                                        showTip(0);
//                                    }
//                                });
//                            }
//                        });
//                        break;
//                    case ID_CookMoments:
//                        CookbookManager.getInstance().getMyCookAlbums(new Callback<List<CookAlbum>>() {
//                            @Override
//                            public void onSuccess(List<CookAlbum> res) {
//                                int tip = 0;
//                                if (res != null) {
//                                    tip += res.size();
//                                }
//                                showTip(tip);
//                            }
//
//                            @Override
//                            public void onFailure(Throwable throwable) {
//                                showTip(0);
//                            }
//                        });
//                        break;
//                }
//            }
//
//            void showTip(int tip) {
//                txtTip.setText(String.format("(%s)", tip));
//                txtTip.setTextColor(getResources().getColor(R.color.gray));
//                txtTip.setVisibility(tip > 0 ? VISIBLE : GONE);
//            }
//        }
//
//    }
//
//}
