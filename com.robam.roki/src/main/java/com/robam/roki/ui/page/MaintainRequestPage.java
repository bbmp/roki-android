package com.robam.roki.ui.page;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.popoups.PopoupHelper;
import com.legent.utils.StringUtils;
import com.legent.utils.api.SoftInputUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.CrmCustomer;
import com.robam.common.pojos.CrmProduct;
import com.robam.roki.R;
import com.robam.roki.model.CrmArea;
import com.robam.roki.model.CrmHelper;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.MaintainSuccessDialog;
import com.robam.roki.ui.view.MaintainProductItemView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class MaintainRequestPage extends HeadPage {

    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    CrmCustomer customer;
    CrmProduct product;
    List<MaintainProductItemView> views = Lists.newArrayList();

    @InjectView(R.id.divProducts)
    LinearLayout divProducts;
    @InjectView(R.id.edtName)
    EditText edtName;
    @InjectView(R.id.edtPhone)
    EditText edtPhone;
    @InjectView(R.id.edtCity)
    EditText edtCity;
    @InjectView(R.id.edtAddress)
    EditText edtAddress;
    @InjectView(R.id.edtBookTime)
    EditText edtBookTime;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        customer = getArguments().getParcelable(PageArgumentKey.CrmCustomer);

        View view = inflater.inflate(R.layout.page_maintain_request, container, false);
        ButterKnife.inject(this, view);
        divProducts.removeAllViews();

        edtCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onPickArea(edtCity);
                } else {
                }
            }
        });

        edtBookTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onPickDate(edtBookTime);
                } else {
                }
            }
        });

        initData();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.txtOK)
    public void onClickOK() {
        try {
            onSubmit();
        } catch (Exception e) {
            ToastUtils.showException(e);
        }
    }

    void initData() {
        if (customer == null) return;

        edtName.setText(customer.name);
        edtPhone.setText(customer.phone);
        edtAddress.setText(customer.address);
        edtBookTime.setText(sdf.format(Calendar.getInstance().getTime()));

        CrmArea ca = CrmHelper.getCrmArea(customer.province, customer.city, customer.county);
        edtCity.setText(ca.toString());

        if (customer.products != null && customer.products.size() > 0) {
            MaintainProductItemView view;

            for (CrmProduct product : customer.products) {
                view = new MaintainProductItemView(cx);
                view.setProduct(product);
                views.add(view);
            }

            for (MaintainProductItemView v : views) {
                divProducts.addView(v);
                v.setOnClickListener(productItemClickListener);
            }
        }

    }

    void onPickArea(View parent) {

        SoftInputUtils.hide((Activity) cx);

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#e2e2e2"));
        PopupWindow pop = Helper.newCrmAreaPicker(cx, new Callback2<CrmArea>() {
            @Override
            public void onCompleted(CrmArea crmArea) {
                edtCity.setText(crmArea.toString());
                edtCity.setTag(crmArea);
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

    void onPickDate(View parent) {

        SoftInputUtils.hide((Activity) cx);

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#e2e2e2"));
        PopupWindow pop = PopoupHelper.newDatePicker(cx, new Callback2<Calendar>() {
            @Override
            public void onCompleted(Calendar c) {
                edtBookTime.setText(sdf.format(c.getTime()));
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

    View.OnClickListener productItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            product = null;
            MaintainProductItemView view = (MaintainProductItemView) v;
            boolean isChecked = view.isChecked();
            for (MaintainProductItemView mv : views) {
                mv.setChecked(false);
            }

            isChecked = !isChecked;
            view.setChecked(isChecked);

            if (isChecked) {
                product = view.getProduct();
            }
        }
    };

    void onSubmit() throws Exception {

        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String address = edtAddress.getText().toString();
        String strTime = edtBookTime.getText().toString();
        String province = customer.province;
        String city = customer.city;
        String county = customer.county;


        Preconditions.checkNotNull(product, "未选择要保养的烟机！");
        Preconditions.checkNotNull(name, "姓名不可为空！");
        Preconditions.checkNotNull(phone, "手机不可为空！");
        Preconditions.checkNotNull(address, "详细地址不可为空！");
        Preconditions.checkNotNull(strTime, "预约时间不可为空！");

        Preconditions.checkState(StringUtils.isMobile(phone), "无效手机号！");

        CrmArea ca = (CrmArea) edtCity.getTag();
        if (ca != null) {
            province = ca.province == null ? "" : String.valueOf(ca.province.id);
            city = ca.city == null ? "" : String.valueOf(ca.city.id);
            county = ca.county == null ? "" : String.valueOf(ca.county.id);
        }

        Date date = sdf.parse(strTime);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        long bookTime = c.getTimeInMillis();

        RokiRestHelper.submitMaintain(product, bookTime, customer.id, name, phone, province, city, county, address, new VoidCallback() {
            @Override
            public void onSuccess() {
                Dialog dlg = MaintainSuccessDialog.show(cx);
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        UIService.getInstance().popBack();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

}
