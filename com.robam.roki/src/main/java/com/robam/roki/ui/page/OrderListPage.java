//package com.robam.roki.ui.page;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.google.common.base.Joiner;
//import com.google.common.eventbus.Subscribe;
//import com.legent.Callback;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.HeadPage;
//import com.legent.ui.ext.adapters.ExtBaseAdapter;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.TitleBar;
//import com.legent.utils.api.DisplayUtils;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.api.ViewUtils;
//import com.legent.utils.graphic.ImageUtils;
//import com.robam.common.events.OrderRefreshEvent;
//import com.robam.common.pojos.OrderInfo;
//import com.robam.common.pojos.OrderRecipe;
//import com.robam.common.services.StoreService;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by sylar on 15/6/14.
// */
//public class OrderListPage extends HeadPage {
//    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
//
//    Adapter adapter;
//
//    @InjectView(R.id.listview)
//    ListView listview;
//
//
//    @Override
//    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
//        View view = layoutInflater.inflate(R.layout.page_order_list, viewGroup, false);
//
//        ButterKnife.inject(this, view);
//        setTitlebar();
//        adapter = new Adapter();
//        listview.setAdapter(adapter);
//        onRefresh();
//
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    @Subscribe
//    public void onEvent(OrderRefreshEvent event) {
//        onRefresh();
//    }
//
//    void onRefresh() {
//        ProgressDialogHelper.setRunning(cx, true);
//        StoreService.getInstance().queryOrder(Calendar.getInstance().getTimeInMillis() + 1000 * 5, 100, new Callback<List<OrderInfo>>() {
//            @Override
//            public void onSuccess(List<OrderInfo> orders) {
//                ProgressDialogHelper.setRunning(cx, false);
//                adapter.loadData(orders);
//                listview.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ViewUtils.setListViewHeightBasedOnChildren(listview);
//                    }
//                }, 50);
//
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
//    void setTitlebar() {
//        ImageView img = TitleBar.newTitleIconView(cx, R.mipmap.ic_order_refresh, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onRefresh();
//            }
//        });
//
//        titleBar.replaceRight(img);
//    }
//
//    class Adapter extends ExtBaseAdapter<OrderInfo> {
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            OrderViewHolder vh;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(cx).inflate(R.layout.view_order_item, null);
//                vh = new OrderViewHolder(convertView);
//            } else {
//                vh = (OrderViewHolder) convertView.getTag();
//            }
//
//            OrderInfo order = getEntity(position);
//            vh.showData(order);
//
//            return convertView;
//        }
//
//        class OrderViewHolder {
//
//            OrderInfo order;
//
//            @InjectView(R.id.txtTime)
//            TextView txtTime;
//            @InjectView(R.id.txtStatus)
//            TextView txtStatus;
//            @InjectView(R.id.divRecipes)
//            LinearLayout divRecipes;
//
//
//            OrderViewHolder(View view) {
//                ButterKnife.inject(this, view);
//                view.setTag(this);
//            }
//
//            @OnClick(R.id.orderItemView)
//            public void onClick() {
//                Bundle bd = new Bundle();
//                bd.putParcelable(PageArgumentKey.Order, order);
//                UIService.getInstance().postPage(PageKey.OrderDetail, bd);
//            }
//
//            void showData(OrderInfo order) {
//                this.order = order;
//                Calendar c = Calendar.getInstance();
//                c.setTimeInMillis(order.submitTime);
//                txtTime.setText(sdf.format(c.getTime()));
//                txtStatus.setText(order.getStatusString());
//
//                boolean isDelivering = order.status == OrderInfo.OrderStatus_Delivering||order.status == OrderInfo.OrderStatus_nullity;
//
//                txtStatus.setBackgroundColor(isDelivering
//                        ? Color.parseColor("#f07878")
//                        : Color.TRANSPARENT);
//
//                txtStatus.setTextColor(isDelivering ? Color.WHITE :
//                        Color.parseColor("#a4a4a4"));
//
//                showRecipes(order.recipes);
//            }
//
//            void showRecipes(List<OrderRecipe> recipes) {
//                divRecipes.removeAllViews();
//                if (recipes == null || recipes.size() == 0) return;
//
//                View view;
//                RecipeViewHolder vh;
//                for (OrderRecipe recipe : recipes) {
//                    view = LayoutInflater.from(cx).inflate(R.layout.view_order_recipe_item, null);
//                    divRecipes.addView(view);
//                    divRecipes.addView(newDivider());
//
//                    vh = new RecipeViewHolder(view);
//                    vh.showData(recipe);
//                }
//
//            }
//
//            View newDivider() {
//                View view = new View(cx);
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(cx, 1f));
//                view.setLayoutParams(lp);
//                return view;
//            }
//
//        }
//
//        class RecipeViewHolder {
//
//            @InjectView(R.id.imgRecipe)
//            ImageView imgRecipe;
//            @InjectView(R.id.txtRecipe)
//            TextView txtRecipe;
//            @InjectView(R.id.txtDesc)
//            TextView txtDesc;
//            @InjectView(R.id.txtNumber)
//            TextView txtNumber;
//
//            RecipeViewHolder(View view) {
//                ButterKnife.inject(this, view);
//                view.setTag(this);
//            }
//
//            void showData(OrderRecipe recipe) {
//                imgRecipe.setImageDrawable(null);
//                ImageUtils.displayImage(recipe.imgUrl, imgRecipe);
//                txtRecipe.setText(recipe.name);
//                txtNumber.setText(String.format("X %s 份", recipe.number));
//                txtDesc.setText(Joiner.on(" ").skipNulls().join(recipe.materials).trim());
//            }
//        }
//    }
//
//
//}
