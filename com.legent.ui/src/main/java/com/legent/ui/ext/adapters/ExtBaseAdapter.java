package com.legent.ui.ext.adapters;

import android.widget.BaseAdapter;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by sylar on 15/6/4.
 */
public abstract class ExtBaseAdapter<T> extends BaseAdapter {

    protected List<T> list = Lists.newArrayList();

    public void loadData(List<T> list) {

        this.list.clear();
        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void insertData(T t) {
        this.list.add(0, t);
        notifyDataSetChanged();
    }

    public void appendData(T t) {
        this.list.add(t);
        notifyDataSetChanged();
    }


    public void appendData(List<T> list) {

        if (list != null && list.size() > 0) {
            this.list.addAll(list);
        }

        notifyDataSetChanged();
    }


    public List<T> getData() {
        return list;
    }

    public T getEntity(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
