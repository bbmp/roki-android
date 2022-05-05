package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.robam.roki.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

public class RecipeSearchOptionView extends FrameLayout {

    public interface OnWordSelectedCallback {
        void onWordSelected(String word);
    }

    public RecipeSearchOptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeSearchOptionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @InjectView(R.id.txtGroup)
    TextView txtGroup;

    @InjectViews({R.id.txtItem1, R.id.txtItem2, R.id.txtItem3})
    List<TextView> items;

    OnWordSelectedCallback callback;

    void init(Context cx, AttributeSet attrs) {
        String title = null;
        if (attrs == null) {

        } else {
            TypedArray ta = cx.obtainStyledAttributes(attrs,
                    R.styleable.RecipeSearchOption);
            title = ta.getString(R.styleable.RecipeSearchOption_title);
            ta.recycle();
        }

        View view = LayoutInflater.from(cx).inflate(
                R.layout.view_recipe_search_option, this, true);

        if (!isInEditMode()) {
            ButterKnife.inject(this, view);
            txtGroup.setText(title);
        }

    }

    public void loadData(List<String> words, OnWordSelectedCallback callabck) {
        this.callback = callabck;

        for (View view : items) {
            view.setVisibility(View.GONE);
        }

        if (words == null || words.size() == 0)
            return;

        TextView view;
        for (int i = 0; i < items.size() && i < words.size(); i++) {
            view = items.get(i);
            view.setVisibility(View.VISIBLE);
            view.setText(words.get(i));
        }

    }

    @OnClick({R.id.txtItem1, R.id.txtItem2, R.id.txtItem3})
    public void onClick(TextView view) {
        if (callback != null) {
            callback.onWordSelected(view.getText().toString());
        }
    }

}
