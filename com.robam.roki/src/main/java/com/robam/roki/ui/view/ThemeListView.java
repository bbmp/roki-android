package com.robam.roki.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.robam.common.pojos.RecipeTheme;
import com.robam.roki.R;

import java.util.List;

import static com.legent.ContextIniter.cx;

/**
 * 主题列表
 * Created by rent on 2016/8/26.
 */

public class ThemeListView extends PullToRefreshListView {
    ListView listView;
    /**
     * 主题排序使用
     */
    Ordering<RecipeTheme> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<RecipeTheme, Comparable>() {
        @Override
        public Comparable apply(RecipeTheme theme) {
            return theme.sortNo;
        }
    }).reverse();

    public ThemeListView(Context context) {
        super(context);
       init();
    }

    public ThemeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
       init();
    }

    private void init() {
        //关闭上推和下拉
        this.setMode(Mode.DISABLED);
        listView = this.getRefreshableView();
        listView.setHeaderDividersEnabled(false);
        listView.setDividerHeight(15);
        //构建标题
        View view = LayoutInflater.from(cx).inflate(R.layout.view_recipe_myfavorite_title,
                null, false);
        TextView title = view.findViewById(R.id.myfavorite_title);
        title.setText("主题");
        listView.addHeaderView(view, null, false);
        //点击效果颜色透明
        listView.setSelection(cx.getResources().getColor(R.color.Transparent));
    }

    ThemeListViewAdapter adapter =null;

    public void loadData(List<RecipeTheme> recipeTheme) {
        if(adapter==null){
            adapter=new ThemeListViewAdapter();
            this.setAdapter(adapter);
        }
        List<RecipeTheme> list = Lists.newArrayList();
        list.addAll(recipeTheme);
        adapter.loadData(list);
        setListViewHeightBasedOnChildren(listView);
    }

    public LinearLayout loadData3(LinearLayout linearLayout,List<RecipeTheme> recipeThemes){
        if(linearLayout==null||recipeThemes==null||recipeThemes.size()==0)return null;
        for(RecipeTheme re:recipeThemes){
            ThemeListViewItem themeListViewItem=new ThemeListViewItem(cx);
            themeListViewItem.loadData(re);
            linearLayout.addView(themeListViewItem);
        }
        return linearLayout;
    }

    class ThemeListViewAdapter extends ExtBaseAdapter<RecipeTheme> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //LogUtils.out("adapter "+position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder(new ThemeListViewItem(cx));
                convertView = viewHolder.viewItem;
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.viewItem.loadData(list.get(position));
            return convertView;
        }
    }


    class ViewHolder {
        ThemeListViewItem viewItem;

        public ViewHolder(ThemeListViewItem viewItem) {
            this.viewItem = viewItem;
            viewItem.setTag(this);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(true)return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
