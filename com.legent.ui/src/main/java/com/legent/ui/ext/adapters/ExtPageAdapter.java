package com.legent.ui.ext.adapters;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


import java.util.List;

/**
 * Created by sylar on 15/6/4.
 */
public class ExtPageAdapter extends PagerAdapter {

   public interface IPrimaryItem{
        void IPos(int pos);
    }


    private IPrimaryItem iPrimaryItem;

    public void setIPrimaryItem(IPrimaryItem iPrimaryItem) {
        this.iPrimaryItem = iPrimaryItem;
    }

    protected List<View> list = Lists.newArrayList();
    private int mChildCount = 0;


    private static final String TAG = "ExtPageAdapter";
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);

        if (iPrimaryItem!=null)
        iPrimaryItem.IPos(position);
        Log.e(TAG,"setPrimaryItem"+"-----"+position);
    }

    public void loadViews(List<View> views) {

        list.clear();
        notifyDataSetChanged();

        if (views != null && views.size() > 0) {
            list.addAll(views);
            notifyDataSetChanged();
        }
    }



    public List<View> getViews() {
        return list;
    }

    public View getPage(int position) {
        Preconditions.checkState(position >= 0 && position < list.size(), "参数越界:ExtPageAdapter.getPage()");

//        if (list.get(position))
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }
}
