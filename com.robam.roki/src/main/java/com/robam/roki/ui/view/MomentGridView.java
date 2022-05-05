package com.robam.roki.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.robam.common.pojos.CookAlbum;

import java.util.List;

public class MomentGridView extends StaggeredGridView {

    Adapter adapter;

    public MomentGridView(Context cx) {
        super(cx);
        init(cx, null);
    }

    public MomentGridView(Context cx, AttributeSet attrs) {
        super(cx, attrs);
        init(cx, attrs);
    }

    public MomentGridView(Context cx, AttributeSet attrs, int defStyle) {
        super(cx, attrs, defStyle);
        init(cx, attrs);
    }

    protected void init(Context cx, AttributeSet attrs) {
    }

    public void loadData(final List<CookAlbum> albums) {
        if (adapter == null) {
            adapter = new Adapter();
            this.setAdapter(adapter);
        }

        adapter.loadData(albums);
        this.smoothScrollToPosition(0);
    }

    class Adapter extends ExtBaseAdapter<CookAlbum> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder(new MomentGridItemView(getContext()));
                convertView = vh.view;
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            CookAlbum album = list.get(position);
            vh.showData(album, position == 0);
            return convertView;
        }

        class ViewHolder {
            MomentGridItemView view;

            ViewHolder(MomentGridItemView view) {
                this.view = view;
                view.setTag(this);
            }

            void showData(CookAlbum album, boolean isSmallSize) {
                view.showData(album, isSmallSize);
            }
        }
    }

}
