//package com.legent.ui.ext.dialogs;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.google.common.collect.Lists;
//import com.legent.events.PageChangedEvent;
//import com.legent.ui.R;
//import com.legent.utils.EventUtils;
//
//import java.util.List;
//
//public class ListDialog {
//
//    @SuppressLint("InflateParams")
//    public static <T> void show(Context cx, String title, final List<T> list,
//                                final ListItemSelectedCallback callback) {
//        show(cx, title, list, Gravity.CENTER_VERTICAL, callback);
//    }
//
//    @SuppressLint("InflateParams")
//    public static <T> void show(Context cx, String title, final List<T> list, int gravity,
//                                final ListItemSelectedCallback callback) {
//
//        final ListView view = (ListView) LayoutInflater.from(cx).inflate(
//                R.layout.dialog_listview, null);
//        final Adapter<T> adapter = new Adapter<T>(cx);
//        view.setAdapter(adapter);
//        adapter.loadData(list, gravity);
//
//        AlertDialog.Builder builder = new Builder(cx);
//        builder.setTitle(title);
//        final AlertDialog dialog = builder.create();
//        dialog.setView(view);
//        view.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int position, long arg3) {
//                dialog.dismiss();
//                Object obj = adapter.getItem(position);
//
//                if (callback != null) {
//                    callback.onItemSelected(position, obj);
//                }
//            }
//        });
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                EventUtils.postEvent(new PageChangedEvent("dialogshow"));
//            }
//        });
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
//            }
//        });
//        dialog.show();
//    }
//
//    public interface ListItemSelectedCallback {
//        void onItemSelected(int position, Object obj);
//    }
//
//    static class Adapter<T> extends BaseAdapter {
//
//        Context cx;
//        int gravity;
//        List<T> list = Lists.newArrayList();
//
//        public Adapter(Context cx) {
//            this.cx = cx;
//        }
//
//        public void loadData(List<T> list) {
//            this.list.clear();
//            this.list.addAll(list);
//            notifyDataSetChanged();
//            loadData(list, Gravity.CENTER_VERTICAL);
//        }
//
//        public void loadData(List<T> list, int gravity) {
//            this.gravity = gravity;
//            this.list.clear();
//            this.list.addAll(list);
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @SuppressLint("InflateParams")
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
//            if (view == null) {
//                view = LayoutInflater.from(cx).inflate(
//                        android.R.layout.simple_list_item_1, null);
//            }
//
//            Object obj = getItem(position);
//            if (obj != null) {
//                TextView txt = (TextView) view;
//                txt.setText(obj.toString());
//                txt.setTextColor(cx.getResources().getColor(R.color.Black));
//                txt.setGravity(gravity);
//            }
//
//            return view;
//        }
//    }
//}
