package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.utils.LogUtils;
import com.legent.utils.api.DisplayUtils;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.roki.R;

import java.util.List;


public class RecipeGridTitleView extends StaggeredGridView {
    public static final int Model_Search = 0;
    public static final int Model_Favority = 1;
    public static final int Model_History = 2;

    int modelType;
    Adapter adapter;

    Ordering<AbsRecipe> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<AbsRecipe, Comparable>() {
        @Override
        public Comparable apply(AbsRecipe absRecipe) {
            return absRecipe.collectCount;
        }
    }).reverse();

    public RecipeGridTitleView(Context cx) {
        super(cx);
        init(cx, null);
    }

    public RecipeGridTitleView(Context cx, AttributeSet attrs) {
        super(cx, attrs);
        init(cx, attrs);
    }

    public RecipeGridTitleView(Context cx, AttributeSet attrs, int defStyle) {
        super(cx, attrs, defStyle);
        init(cx, attrs);
    }

    protected void init(Context cx, AttributeSet attrs) {
    }


    public void loadData(int modelType, List<Recipe> books, List<Recipe3rd> books3rd) {
        this.modelType = modelType;

        List<AbsRecipe> list = Lists.newArrayList();
        if (books != null) {
            books = ordering.sortedCopy(books);
            for (Recipe r : books) {
                list.add(r);
            }
        }
        if (books3rd != null) {
            books3rd = ordering.sortedCopy(books3rd);
            for (Recipe3rd r : books3rd) {
                list.add(r);
            }
        }
        this.removeHeaderView(title);
        if (list != null && list.size() > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(getContext(), 30));
            //layoutParams.setMargins(DisplayUtils.dip2px(cx, 8), DisplayUtils.dip2px(cx, 5), 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            title = new TextView(getContext());
            title.setLayoutParams(layoutParams);
            title.setText("菜谱");
            title.setTextSize(16);
            title.setPadding(DisplayUtils.dip2px(getContext(), 8), DisplayUtils.dip2px(getContext(), 5), 0, 0);
            title.setTextColor(getContext().getResources().getColor(R.color.c06));
            this.addHeaderView(title);
        }
        if (adapter == null) {
            adapter = new Adapter();
            this.setAdapter(adapter);
        }
        adapter.loadData(list);
        this.smoothScrollToPosition(0);
    }
    TextView title;
    class Adapter extends ExtBaseAdapter<AbsRecipe> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder(new RecipeGridItemVIew(getContext(), modelType));
                convertView = vh.view;
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            AbsRecipe book = list.get(position);
            vh.showData(book, position == 0);
            return convertView;
        }

        class ViewHolder {
            RecipeGridItemVIew view;

            ViewHolder(RecipeGridItemVIew view) {
                this.view = view;
                view.setTag(this);
            }

            void showData(AbsRecipe book, boolean isSmallSize) {
                view.showData(book, isSmallSize);
            }
        }
    }

}
