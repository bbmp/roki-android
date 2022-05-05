package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeTabItemView extends FrameLayout {

    @InjectView(R.id.imgTab)
    ImageView imgTab;

    @InjectView(R.id.txtTab)
    TextView txtTab;

    public HomeTabItemView(Context context) {
        super(context);
        init(context, null);
    }

    public HomeTabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HomeTabItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_tab_item,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);

            if (attrs != null) {
                TypedArray ta = cx.obtainStyledAttributes(attrs,
                        R.styleable.HomeTabItem);
                String title = ta.getString(R.styleable.HomeTabItem_title);
                int imgResid = ta.getResourceId(R.styleable.HomeTabItem_imgSource, 0);
                ta.recycle();
                txtTab.setText(title);
                imgTab.setImageResource(imgResid);

            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        txtTab.setSelected(selected);
        imgTab.setSelected(selected);
    }
}
