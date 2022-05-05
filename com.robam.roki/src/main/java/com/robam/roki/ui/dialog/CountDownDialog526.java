package com.robam.roki.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/5/3.
 */

public class CountDownDialog526 extends AbsDialog {
    private int count;
    private Timer timer;
    private CountDownDialog526.MyTimerTask timerTask;

    @InjectView(R.id.txt)
    TextView txt;

    public CountDownDialog526(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_count_micro526;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    static public  CountDownDialog526 show(Context cx, int time) {
        CountDownDialog526 dlg = new CountDownDialog526(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        dlg.setCanceledOnTouchOutside(false);
        dlg.setCancelable(false);
        dlg.start(time);
        dlg.show();
        return dlg;
    }

    private void start(int time) {
        count = time;
        timer = new Timer();
        timerTask = new MyTimerTask();
        handler.sendEmptyMessage(0);
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    txt.setText(String.valueOf(count));
                    break;
                case 1:
                    if (listener != null){
                        listener.onConfirm("finish");
                    }
                    dismiss();
                    break;
            }
        }
    };

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            count -= 1;
            if(count == 0){
                cancel();
                handler.sendEmptyMessage(1);
            }
            handler.sendEmptyMessage(0);
        }
    }

    public interface PickListener {

        void onCancel();

        void onConfirm(String finish);
    }

    private CountDownDialog526.PickListener listener;

    public void setPickListener(CountDownDialog526.PickListener listener) {
        this.listener = listener;
    }
}
