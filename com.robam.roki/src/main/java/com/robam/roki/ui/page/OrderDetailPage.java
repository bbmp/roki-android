//package com.robam.roki.ui.page;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.os.Parcel;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.common.base.Joiner;
//import com.google.common.base.Strings;
//import com.legent.VoidCallback;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.HeadPage;
//import com.legent.ui.ext.adapters.ExtBaseAdapter;
//import com.legent.ui.ext.dialogs.DialogHelper;
//import com.legent.ui.ext.views.TitleBar;
//import com.legent.utils.api.ToastUtils;
//import com.legent.utils.graphic.ImageUtils;
//import com.robam.common.pojos.OrderContacter;
//import com.robam.common.pojos.OrderInfo;
//import com.robam.common.pojos.OrderRecipe;
//import com.robam.common.services.StoreService;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by sylar on 15/6/14.
// */
//public class OrderDetailPage extends HeadPage {
//
//    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
//
//    OrderInfo order;
//    RecipeAdapter adapter;
//    TextView txtCancelOrder;
//
//    @InjectView(R.id.txtStatus)
//    TextView txtStatus;
//    @InjectView(R.id.txtLogisticsNO)
//    TextView txtLogisticsNO;
//    @InjectView(R.id.logisticsView)
//    LinearLayout logisticsView;
//    @InjectView(R.id.txtCustomerName)
//    TextView txtCustomerName;
//    @InjectView(R.id.txtCustomerAddress)
//    TextView txtCustomerAddress;
//    @InjectView(R.id.txtCode)
//    TextView txtCode;
//    @InjectView(R.id.txtTime)
//    TextView txtTime;
//    @InjectView(R.id.recipeListview)
//    ListView recipeListview;
//    @InjectView(R.id.customerView)
//    RelativeLayout customerView;
//
//    @Override
//    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
//
//        View view = layoutInflater.inflate(R.layout.page_order_detail, viewGroup, false);
//        ButterKnife.inject(this, view);
//
//        setTitlebar();
//        adapter = new RecipeAdapter();
//        recipeListview.setAdapter(adapter);
//        order = getArguments().getParcelable(PageArgumentKey.Order);
//
//        onRefresh();
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//
//    @OnClick(R.id.customerView)
//    public void onClickContacter() {
//        Bundle bd = new Bundle();
//        bd.putBoolean(PageArgumentKey.IsEditOrderContacter, true);
//        bd.putLong(PageArgumentKey.OrderId, order.id);
//        bd.putParcelable(PageArgumentKey.OrderContacterEditCallback, new OrderContacterEditCallback());
//        bd.putParcelable(PageArgumentKey.OrderContacter, order.customer);
//        UIService.getInstance().postPage(PageKey.OrderContacterEdit, bd);
//    }
//
//
//    void setTitlebar() {
//        txtCancelOrder = TitleBar.newTitleTextView(cx, "取消订单", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogHelper.newDialog_OkCancel(cx, "确认取消订单？", null, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == DialogInterface.BUTTON_POSITIVE) {
//                            onCancelOrder();
//                        }
//                    }
//                }).show();
//            }
//        });
//
//        titleBar.replaceRight(txtCancelOrder);
//    }
//
//    void onRefresh() {
//        boolean isAccepted = order.status == OrderInfo.OrderStatus_Accepted;
//        txtCancelOrder.setVisibility(isAccepted ? View.VISIBLE : View.GONE);
//        customerView.setEnabled(isAccepted);
//
//        txtStatus.setText(order.getStatusString() + "...");
//        txtLogisticsNO.setText(String.format("%s  %s", order.logisticsCompany, order.logisticsNO));
//        logisticsView.setVisibility(Strings.isNullOrEmpty(order.logisticsNO) ? View.GONE : View.VISIBLE);
//        txtCode.setText(order.code);
//        onRefreshContacter();
//
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(order.submitTime);
//        txtTime.setText(sdf.format(c.getTime()));
//
//        adapter.loadData(order.recipes);
//    }
//
//    void onRefreshContacter() {
//        if (order.customer != null) {
//            txtCustomerName.setText(String.format("%s %s", order.customer.name, order.customer.phone));
//            txtCustomerAddress.setText(String.format("%s%s", order.customer.city, order.customer.address));
//        }
//    }
//
//    void onCancelOrder() {
//        StoreService.getInstance().cancelOrder(order.id, new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                ToastUtils.showShort("订单已取消");
//                UIService.getInstance().popBack();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ToastUtils.showThrowable(t);
//            }
//        });
//    }
//
//
//    public class OrderContacterEditCallback implements OrderContacterEditPage.OnConfirmCallback {
//
//
//        public OrderContacterEditCallback() {
//
//        }
//
//        @Override
//        public void onConfirm(OrderContacter contacter) {
//            order.customer = contacter;
//            onRefreshContacter();
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
//
//    class RecipeAdapter extends ExtBaseAdapter<OrderRecipe> {
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder vh = null;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(cx).inflate(R.layout.view_order_recipe_item, null);
//                vh = new ViewHolder(convertView);
//            } else {
//                vh = (ViewHolder) convertView.getTag();
//            }
//
//            OrderRecipe or = getEntity(position);
//            vh.showData(or);
//
//            return convertView;
//        }
//
//        class ViewHolder {
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
//            ViewHolder(View view) {
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
//}
