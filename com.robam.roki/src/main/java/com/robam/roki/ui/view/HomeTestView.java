package com.robam.roki.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.legent.ui.ext.views.TagCloudView;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HomeTestView extends FrameLayout implements UIListeners.IRefresh {

    @InjectView(R.id.txtTest)
    TextView txtTest;

    @InjectView(R.id.tagView)
    TagCloudView tagView;

//    @InjectView(R.id.tagCloud)
//    TagCloud tagCloud;
//    @InjectView(R.id.tagLayout)
//    TagCloudLayout tagLayout;

    public HomeTestView(Context context) {
        super(context);
        init(context, null);
    }

    public HomeTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HomeTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_test,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }

    }

    @Override
    public void onRefresh() {

    }

    @OnClick(R.id.txtTest)
    public void onTest() {
        List<String> texts = Lists.newArrayList();
        List<TextView> views = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            String text = String.format("虎皮青椒 %s", i);
            texts.add(text);
            views.add(getTextView(text));
        }

        tagView.setCloudTags(views);
//        tagCloud.setItems(texts);

//        for (TextView view : views) {
//            tagLayout.addView(view);
//        }
    }

    TextView getTextView(final String text) {
        TextView txt = new TextView(getContext());
        txt.setTextColor(Color.WHITE);
        txt.setTextSize(15f);
        txt.setText(text);

        txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(String.format("%s clicked!", text));
            }
        });

        return txt;
    }
}
