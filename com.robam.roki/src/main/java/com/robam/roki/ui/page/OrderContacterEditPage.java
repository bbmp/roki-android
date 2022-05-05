package com.robam.roki.ui.page;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.common.base.Preconditions;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.popoups.PopoupHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.SoftInputUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.OrderContacter;
import com.robam.common.services.StoreService;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class OrderContacterEditPage extends BasePage {


    public interface OnConfirmCallback extends Parcelable {
        void onConfirm(OrderContacter orderContacter);
    }

    boolean isEditOrder;
    long orderId;
    OrderContacter contacter;
    OnConfirmCallback callback;

    @InjectView(R.id.layout)
    LinearLayout layout;
    @InjectView(R.id.edtName)
    EditText edtName;
    @InjectView(R.id.edtPhone)
    EditText edtPhone;
    @InjectView(R.id.edtCity)
    EditText edtCity;
    @InjectView(R.id.edtAddress)
    EditText edtAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        isEditOrder = getArguments().getBoolean(PageArgumentKey.IsEditOrderContacter, false);
        contacter = getArguments().getParcelable(PageArgumentKey.OrderContacter);
        callback = getArguments().getParcelable(PageArgumentKey.OrderContacterEditCallback);
        if (isEditOrder) {
            orderId = getArguments().getLong(PageArgumentKey.OrderId);
        }

        View view = inflater.inflate(R.layout.dialog_order_contacter_edit, container, false);
        ButterKnife.inject(this, view);

        initView();
        return view;
    }


    @OnClick(R.id.txtOK)
    public void onClickOK() {
        try {
            if (edtName.getText().toString().isEmpty()) {
                ToastUtils.show("收货人不能为空", 1500);
            } else if (edtAddress.getText().toString().isEmpty()) {
                ToastUtils.show("配送地址不能为空", 1500);
            } else {
                onSave();
            }
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    protected void initView() {

        edtCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onPickArea(edtCity);
                } else {
                }

                layout.setGravity(hasFocus ? Gravity.TOP : Gravity.CENTER);
            }
        });

        showData(contacter);

    }

    void showData(OrderContacter orderContacter) {
        if (orderContacter == null) return;

        this.contacter = orderContacter;
        edtName.setText(orderContacter.name);
        edtPhone.setText(orderContacter.phone);
        edtCity.setText(orderContacter.city);
        edtAddress.setText(orderContacter.address);
    }

    void onSave() {
        final String name = edtName.getText().toString();
        final String phone = edtPhone.getText().toString();
        final String city = edtCity.getText().toString();
        final String address = edtAddress.getText().toString();

        Preconditions.checkNotNull(name, "收货人不可为空");
        Preconditions.checkNotNull(phone, "手机号码不可为空");
        Preconditions.checkNotNull(name, "城市不可为空");
        Preconditions.checkNotNull(name, "详细地址不可为空");
        Preconditions.checkState(StringUtils.isMobile(phone), "无效的手机号码");

        if (isEditOrder) {
            StoreService.getInstance().updateOrderContacter(orderId, name, phone, city, address, new VoidCallback() {
                @Override
                public void onSuccess() {
                    onUpdateSuccess(name, phone, city, address);
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.showThrowable(t);
                }
            });
        } else {

            StoreService.getInstance().saveCustomerInfo(name, phone, city, address, new VoidCallback() {
                @Override
                public void onSuccess() {
                    onUpdateSuccess(name, phone, city, address);
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.showThrowable(t);
                }
            });
        }
    }

    void onPickArea(View parent) {

        SoftInputUtils.hide((Activity) cx);

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#e2e2e2"));
//        PopupWindow pop = PopoupHelper.newSimpleAreaPicker(cx, new Callback2<String>() {
//            @Override
//            public void onCompleted(String s) {
//                edtCity.setText(s);
//            }
//        });

        PopupWindow pop = Helper.newOrderAreaPicker(cx, new Callback2<String>() {
            @Override
            public void onCompleted(String s) {
                edtCity.setText(s);
            }
        });

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                edtAddress.requestFocus();
            }
        });

        PopoupHelper.show(parent, pop, Gravity.BOTTOM, cd);
    }

    void dismiss() {
        UIService.getInstance().popBack();
    }

    void onUpdateSuccess(String name, String phone, String city, String address) {
        contacter = new OrderContacter();
        contacter.name = name;
        contacter.phone = phone;
        contacter.city = city;
        contacter.address = address;

        dismiss();
        onSubmit(contacter);
    }

    void onSubmit(OrderContacter contacter) {
        if (callback != null) {
            callback.onConfirm(contacter);
        }
    }


}
