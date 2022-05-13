//package com.legent.ui.ext.dialogs;
//
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.Gravity;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//
//import com.legent.events.PageChangedEvent;
//import com.legent.utils.EventUtils;
//
//public class RadioGroupDialog {
//
//    public static void show(Context cx, String title, String[] items,
//                            final ItemSeletedCallback callback) {
//
//        if (items == null || items.length == 0)
//            return;
//
//        RadioGroup group = new RadioGroup(cx);
//        group.setGravity(Gravity.LEFT);
//        group.setOrientation(RadioGroup.VERTICAL);
//        RadioButton btn;
//
//        for (String txt : items) {
//            btn = new RadioButton(cx);
//            btn.setPadding(10, 10, 10, 10);
//            btn.setTextSize(20);
//            btn.setText(txt);
//            group.addView(btn);
//        }
//
//        AlertDialog.Builder builder = new Builder(cx);
//        builder.setTitle(title);
//        final AlertDialog dlg = builder.create();
//
//        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                dlg.dismiss();
//
//                int index = group.indexOfChild(group.findViewById(checkedId));
//                if (callback != null) {
//                    callback.onItemSeleted(index);
//                }
//            }
//        });
//
//        dlg.setView(group);
//        // dlg.setView(group, 150, 50, 150, 50);
//        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                EventUtils.postEvent(new PageChangedEvent("dialogshow"));
//            }
//        });
//        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
//            }
//        });
//        dlg.show();
//    }
//
//    public interface ItemSeletedCallback {
//        void onItemSeleted(int index);
//    }
//
//}
