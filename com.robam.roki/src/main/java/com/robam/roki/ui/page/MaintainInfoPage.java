package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.legent.ui.ext.HeadPage;
import com.robam.common.pojos.MaintainInfo;
import com.robam.roki.R;
import com.robam.roki.model.CrmArea;
import com.robam.roki.model.CrmHelper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.view.MaintainProductItemView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sylar on 15/6/14.
 */
public class MaintainInfoPage extends HeadPage {

    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    MaintainInfo mi;
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
    @InjectView(R.id.edtStatus)
    EditText edtStatus;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mi = getArguments().getParcelable(PageArgumentKey.MaintainInfo);

        View view = inflater.inflate(R.layout.page_maintain_info, container, false);
        ButterKnife.inject(this, view);
        divProducts.removeAllViews();

        initData();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    void initData() {
        if (mi == null) return;

        edtName.setText(mi.name);
        edtPhone.setText(mi.phone);

        edtAddress.setText(mi.address);
        edtBookTime.setText(sdf.format(Calendar.getInstance().getTime()));
        edtStatus.setText(mi.status);

        CrmArea ca = CrmHelper.getCrmArea(mi.province,mi.city,mi.county);
        edtCity.setText(ca.toString());


        //---product
        MaintainProductItemView view = new MaintainProductItemView(cx);
        view.setProductInfo(mi.productId, mi.productType, mi.category);
        views.add(view);
        view.setChecked(true);
        divProducts.addView(view);
    }

}
