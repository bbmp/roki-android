//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import androidx.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.legent.ui.ext.BasePage;
//import com.robam.roki.R;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
///**
// * Created by Gu on 2016/1/7.
// */
//public class CountDownPage extends BasePage {
//    @InjectView(R.id.tv_steri_count_down)
//    TextView tvDown;
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            tvDown.setText(String.valueOf(msg.what));
//            if (msg.what == 1) {
//                if (getActivity() != null)
//                    getActivity().finish();
//            }
//        }
//    };
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_open_sterilizer, null);
//        ButterKnife.inject(this, view);
//        new Thread() {
//            @Override
//            public void run() {
//                for (int i = 3; i > 0; i--) {
//                    Message message = Message.obtain();
//                    message.what = i;
//                    handler.sendMessage(message);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//        return view;
//    }
//
//}
