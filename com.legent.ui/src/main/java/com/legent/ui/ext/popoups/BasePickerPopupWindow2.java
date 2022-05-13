//package com.legent.ui.ext.popoups;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.legent.ui.R;
//
///**
// * Created by Rosicky on 15/12/12.
// */
//public class BasePickerPopupWindow2 extends AbsPopupWindow implements View.OnClickListener {
//
//    public interface PickListener {
//
//        void onCancel();
//
//        void onConfirm();
//    }
//
//    FrameLayout divMain;
//    ImageView warning;
//    TextView txtConfirm;
//    View customView;
//    protected PickListener listener;
//
//    public BasePickerPopupWindow2(Context cx, View customView) {
//        super(cx);
//
//        View view = LayoutInflater.from(cx).inflate(R.layout.abs_view_picker_popoup2, null);
//        divMain = view.findViewById(R.id.divMain);
//        warning = view.findViewById(R.id.icon);
//        txtConfirm = view.findViewById(R.id.txtConfirm);
//        txtConfirm.setOnClickListener(this);
//
//        this.setContentView(view);
//        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setFocusable(true);
//        this.setOutsideTouchable(false);
//        this.setAnimationStyle(R.style.bottom_window_style);
//
//        this.customView = customView;
//        divMain.addView(customView);
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view == txtConfirm) {
//            dismiss();
//            if (listener != null) {
//                listener.onConfirm();
//            }
//        }
//    }
//
//    public void setPickListener(PickListener listener) {
//        this.listener = listener;
//    }
//}
