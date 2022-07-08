package com.robam.roki.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.robam.roki.R;

public class ItemMutiTitleView extends RelativeLayout {


    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.item_mutil_title_bar,this,true);
    }

    public ItemMutiTitleView(Context context) {
        super(context);initView();
    }

    public ItemMutiTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ItemMutiTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ItemMutiTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
}
