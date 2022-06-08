//package com.robam.roki.ui.view.recipeclassify;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.legent.utils.LogUtils;
//import com.robam.common.pojos.Tag;
//
//import java.util.ArrayList;
//
///**
// * Created by zhoudingjun on 2016/12/8.
// */
//
//
//public class RecipeClassiflyGridViewAdapter extends BaseAdapter {
//    private Context cx;
//    private ArrayList<Tag> tags;
//
//    public RecipeClassiflyGridViewAdapter(Context context, ArrayList<Tag> tags) {
//        this.cx = context;
//        this.tags = tags;
//    }
//    @Override
//    public int getCount() {
//        return tags.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return tags.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder vh;
//        if (convertView == null) {
//            vh = new ViewHolder();
//            Tag tag = tags.get(position);
//            vh.view = new RecipeClassiflyItemView(cx,tag);
//            convertView = vh.view;
//            convertView.setTag(vh);
//        } else {
//            vh = (ViewHolder) convertView.getTag();
//        }
//        return convertView;
//    }
//
//    static class ViewHolder {
//        RecipeClassiflyItemView view;
//    }
//
//}
