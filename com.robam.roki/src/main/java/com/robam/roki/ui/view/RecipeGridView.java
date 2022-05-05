package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.events.ClickRecipeEvent;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.AbsRecipe;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;

import java.util.List;

public class RecipeGridView extends StaggeredGridView {
    public static final int Model_Search = 0;
    public static final int Model_Favority = 1;
    public static final int Model_History = 2;
    public static final int Model_classify = 3;

    int modelType;
    Adapter adapter;
    public static final int PULL_DOWN = 1;//下拉刷新
    public static final int PULL_UP = 2;// 上推加载
    public static final int FIRST_LOAD = 0;//第一次加载数据

    /**
     * 根据收藏次数排序呢
     */
    Ordering<AbsRecipe> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<AbsRecipe, Comparable>() {
        @Override
        public Comparable apply(AbsRecipe absRecipe) {
            return absRecipe.collectCount;//收藏次数
        }
    }).reverse();

    public RecipeGridView(Context cx) {
        super(cx);
        init(cx, null);
    }

    public RecipeGridView(Context cx, AttributeSet attrs) {
        super(cx, attrs);
        init(cx, attrs);
    }

    public RecipeGridView(Context cx, AttributeSet attrs, int defStyle) {
        super(cx, attrs, defStyle);
        init(cx, attrs);
    }

    protected void init(Context cx, AttributeSet attrs) {

    }


    public  List<AbsRecipe> loadData(int modelType, List<Recipe> books, List<Recipe3rd> books3rd, int model) {
        this.modelType = modelType;
        if (adapter == null) {
            adapter = new Adapter();
            this.setAdapter(adapter);
        }

        List<AbsRecipe> list= Lists.newArrayList();
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
        switch (model) {
            case PULL_UP:
                adapter.appendData(list);
                break;
          /*  case PULL_DOWN:
                adapter.appendData(list);*/
            default:
                adapter.loadData(list);
                break;
        }
        this.smoothScrollToPosition(0);

        return list;
    }

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
