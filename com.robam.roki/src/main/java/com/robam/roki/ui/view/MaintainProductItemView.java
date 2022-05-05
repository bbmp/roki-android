package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.common.pojos.CrmProduct;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MaintainProductItemView extends FrameLayout {

    boolean checked;
    CrmProduct product;

    @InjectView(R.id.imgChecked)
    ImageView imgChecked;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    public MaintainProductItemView(Context context) {
        super(context);
        init(context, null);
    }

    public MaintainProductItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaintainProductItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_maintain_product_item,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }

    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        imgChecked.setSelected(checked);
        txtTitle.setSelected(checked);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setProduct(CrmProduct product) {
        this.product = product;
        setProductInfo(product.id, product.type, product.category);
    }

    public void setProductInfo(String productId, String type, String category) {
        txtTitle.setText(type);
    }


    public CrmProduct getProduct() {
        return product;
    }
}
