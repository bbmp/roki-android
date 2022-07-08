//package com.robam.roki.ui.view;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Parcel;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.baoyz.swipemenulistview.SwipeMenu;
//import com.baoyz.swipemenulistview.SwipeMenuCreator;
//import com.baoyz.swipemenulistview.SwipeMenuItem;
//import com.baoyz.swipemenulistview.SwipeMenuListView;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//import com.legent.Callback;
//import com.legent.VoidCallback;
//import com.legent.plat.exceptions.RCException;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.adapters.ExtBaseAdapter;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.utils.api.DisplayUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.graphic.ImageUtils;
//import com.robam.common.pojos.OrderContacter;
//import com.robam.common.pojos.OrderInfo;
//import com.robam.common.pojos.Recipe;
//import com.robam.common.services.CookbookManager;
//import com.robam.common.services.StoreService;
//import com.robam.common.util.RecipeRequestIdentification;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.ui.dialog.OrderHintDialog;
//import com.robam.roki.ui.page.OrderContacterEditPage;
//import com.robam.roki.ui.page.RecipeDetailPage;
//
//import java.util.List;
//import java.util.Set;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
//public class TrolleyRecipeView extends FrameLayout {
//
//    @InjectView(R.id.listview)
//    SwipeMenuListView listview;
//    //    @InjectView(R.id.thirdPlatView)
////    TrolleyThirdPlatView thirdPlatView;
//    @InjectView(R.id.freeView)
//    TrolleyFreeView freeView;
//
//
//    Context cx;
//    Adapter adapter;
//    boolean isEnableOrder;
//
//    public TrolleyRecipeView(Context context) {
//        super(context);
//        init(context, null);
//    }
//
//    public TrolleyRecipeView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    public TrolleyRecipeView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(context, attrs);
//    }
//
//
//    void init(Context cx, AttributeSet attrs) {
//        this.cx = cx;
//
//        View view = LayoutInflater.from(cx).inflate(R.layout.view_trolley_recipe,
//                this, true);
//        if (!view.isInEditMode()) {
//            ButterKnife.inject(this, view);
//
//            adapter = new Adapter();
//            listview.setMenuCreator(new MenuCrater(cx));
//            listview.setOnMenuItemClickListener(menuListener);
//            listview.setOnItemClickListener(itemClickListener);
//            listview.setAdapter(adapter);
//
//            freeView.setOnDeliverCallback(deliverCallback);
//        }
//    }
//
//    void setOrderEnable(boolean enable) {
//        isEnableOrder = enable;
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();
//        }
//
////        thirdPlatView.setVisibility(isEnableOrder ? GONE : VISIBLE);
//        freeView.setVisibility(isEnableOrder ? VISIBLE : GONE);
//    }
//
//    void loadData(final List<Recipe> books) {
//        adapter.loadData(books);
//    }
//
//
//    void onDelete(int position) {
//        Recipe book = (Recipe) adapter.getItem(position);
//        CookbookManager.getInstance().deleteTodayCookbook(book.id, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                ToastUtils.showShort("已从购物车删除");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ToastUtils.showThrowable(t);
//            }
//        });
//    }
//
//    void onBatchDelete(List<Long> ids) {
//        if (ids == null || ids.size() == 0) return;
//        for (long id : ids) {
//            CookbookManager.getInstance().deleteTodayCookbook(id, null);
//        }
//    }
//
//    void onViewDetail(long orderId) {
//        StoreService.getInstance().getOrder(orderId, new Callback<OrderInfo>() {
//            @Override
//            public void onSuccess(OrderInfo orderInfo) {
//                Bundle bd = new Bundle();
//                bd.putParcelable(PageArgumentKey.Order, orderInfo);
//                UIService.getInstance().postPage(PageKey.OrderDetail, bd);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ToastUtils.showThrowable(t);
//            }
//        });
//    }
//
//    void onShowRecipeDetail(Recipe recipe) {
//        if (recipe.isNewest()) {
//            RecipeDetailPage.show(recipe.id,RecipeDetailPage.unKnown,RecipeRequestIdentification.RECIPE_TROLLEY);
//        } else {
//            ProgressDialogHelper.setRunning(cx, true);
//            CookbookManager.getInstance().getCookbookById(recipe.id, new Callback<Recipe>() {
//                @Override
//                public void onSuccess(Recipe recipe) {
//                    ProgressDialogHelper.setRunning(cx, false);
//                    RecipeDetailPage.show(recipe.id,RecipeDetailPage.unKnown,RecipeRequestIdentification.RECIPE_TROLLEY);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    ProgressDialogHelper.setRunning(cx, false);
//                    ToastUtils.showThrowable(t);
//                }
//            });
//        }
//    }
//
//    TrolleyFreeView.OnDeliverCallback deliverCallback = new TrolleyFreeView.OnDeliverCallback() {
//        @Override
//        public void onDeliver() {
//            final List<Long> ids = adapter.getSelectedRecipes();
//            if (ids == null || ids.size() == 0) {
//                ToastUtils.showShort("您好像还没选菜呢");
//                return;
//            }
//            if (ids == null || ids.size() > 1) {
//                ToastUtils.showShort("每次最多一道菜");
//                return;
//            }
//
//            StoreService.getInstance().getCustomerInfo(new Callback<OrderContacter>() {
//                @Override
//                public void onSuccess(OrderContacter contacter) {
//                        Bundle bd = new Bundle();
//                        bd.putBoolean(PageArgumentKey.IsEditOrderContacter, false);
//                        bd.putParcelable(PageArgumentKey.OrderContacterEditCallback, new OrderContacterEditCallback(ids));
//                        bd.putParcelable(PageArgumentKey.OrderContacter, contacter);
//
//                        UIService.getInstance().postPage(PageKey.OrderContacterEdit, bd);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    ToastUtils.showThrowable(t);
//                }
//            });
//        }
//    };
//
//    SwipeMenuListView.OnItemClickListener itemClickListener = new SwipeMenuListView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Recipe book = adapter.getEntity(position);
//            if (book == null) return;
//            onShowRecipeDetail(book);
//        }
//    };
//
//    SwipeMenuListView.OnMenuItemClickListener menuListener = new SwipeMenuListView.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int itemIndex) {
//            if (itemIndex == 0) {
//                onDelete(position);
//            }
//            // false : close the menu; true : not close the menu
//            return false;
//        }
//    };
//
//    public class OrderContacterEditCallback implements OrderContacterEditPage.OnConfirmCallback {
//
//        List<Long> ids;
//
//        public OrderContacterEditCallback(List<Long> ids) {
//            this.ids = ids;
//        }
//
//        @Override
//        public void onConfirm(OrderContacter contacter) {
//            StoreService.getInstance().submitOrder(ids, new Callback<Long>() {
//                @Override
//                public void onSuccess(Long orderId) {
//                    adapter.clearSelectes();
//                    onBatchDelete(ids);
//                    onViewDetail(orderId);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    if (t instanceof RCException) {
//                        RCException e = (RCException) t;
//                        OrderHintDialog.show(getContext(), e.errorCode);
//                    } else {
//                        ToastUtils.showThrowable(t);
//                    }
//                }
//            });
//        }
//
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//        }
//
//
//        protected OrderContacterEditCallback(Parcel in) {
//        }
//
//    }
//
//    class MenuCrater implements SwipeMenuCreator {
//        Context cx;
//
//        public MenuCrater(Context cx) {
//            this.cx = cx;
//        }
//
//        @Override
//        public void create(SwipeMenu menu) {
//            SwipeMenuItem delItem = new SwipeMenuItem(cx);
//            delItem.setBackground(new ColorDrawable(Color.rgb(0xEE, 0x00, 0x1A)));
//            delItem.setWidth(DisplayUtils.dip2px(cx, 135f));
//            delItem.setTitle("删除");
//            delItem.setTitleSize(18);
//            delItem.setTitleColor(Color.WHITE);
//            menu.addMenuItem(delItem);
//        }
//    }
//
//    class Adapter extends ExtBaseAdapter<Recipe> {
//
//        Set<Long> ids = Sets.newHashSet();
//
//        public List<Long> getSelectedRecipes() {
//            return Lists.newArrayList(ids);
//        }
//
//        public void clearSelectes() {
//            ids.clear();
//            notifyDataSetChanged();
//        }
//
//
//        @Override
//        public void loadData(List<Recipe> list) {
//            ids.clear();
//            super.loadData(list);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder vh = null;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(cx).inflate(R.layout.view_trolley_recipe_item, parent, false);
//                vh = new ViewHolder(convertView);
//                convertView.setTag(vh);
//            } else {
//                vh = (ViewHolder) convertView.getTag();
//            }
//
//            Recipe book = list.get(position);
//            vh.showData(book);
//            return convertView;
//        }
//
//
//        class ViewHolder {
//            @InjectView(R.id.imgSelected)
//            ImageView imgSelected;
//            @InjectView(R.id.imgRecipe)
//            ImageView imgRecipe;
//            @InjectView(R.id.txtRecipe)
//            TextView txtRecipe;
//            @InjectView(R.id.txtCount)
//            TextView txtCount;
//
//            Recipe book;
//
//            ViewHolder(View view) {
//                ButterKnife.inject(this, view);
//            }
//
//            @OnClick(R.id.imgSelected)
//            public void onClickSelect() {
//
//                if (ids.size() >= 3 && !imgSelected.isSelected()) {
//                    ToastUtils.showShort("每次最多3道菜");
//                    return;
//                }
//
//                if (ids.contains(book.id)) {
//                    ids.remove(book.id);
//                } else {
//                    ids.add(book.id);
//                }
//
//                imgSelected.setSelected(!imgSelected.isSelected());
//            }
//
//            public void showData(Recipe book) {
//                this.book = book;
//                ImageUtils.displayImage(book.imgMedium, imgRecipe);
//                txtRecipe.setText(book.name);
//                txtCount.setText(String.format("收藏（%s）", book.collectCount));
//
//                imgSelected.setSelected(ids.contains(book.id));
//                imgSelected.setVisibility(isEnableOrder ? VISIBLE : INVISIBLE);
//
//            }
//        }
//    }
//
//}
