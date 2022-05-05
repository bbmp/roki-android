package com.legent.ui.ext.dialogs;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.legent.ui.R;

public class ProgressNewDialogHelper {

    static ProgressNewDialog progress;

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ProgressNewDialog dialog = (ProgressNewDialog) msg.obj;
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public static void sendMsg(ProgressNewDialog dlg) {
        Message msg = Message.obtain();
        msg.what = 0;
        msg.obj = dlg;
        handler.sendMessageDelayed(msg, 5000);
    }

    public static void setRunning(Context cx, boolean isRunning) {
        if (isRunning) {
            show(cx);
        } else {
            hide();
        }
    }

    public static void setRunning(Context cx, boolean isRunning, boolean show) {
        if (show)
            setRunning(cx, isRunning);
    }

    public static ProgressNewDialog show(Context cx) {
        String select = String.format(cx.getString(R.string.loadingPleaseWait) );

        return show(cx, select);


    }

    public static ProgressNewDialog show(Context cx, String msg) {
        return show(cx, msg, false);
    }

    public static ProgressNewDialog show(Context cx, String msg, boolean cancelable) {
        return show(cx, msg, cancelable, false);
    }

    public static ProgressNewDialog show(Context cx, String msg,
                                      boolean cancelable, boolean canceledOnTouchOutside) {
        if (progress == null)
            progress = new ProgressNewDialog(cx);

        progress.setMsg(msg);
        progress.setCancelable(cancelable);
        progress.setCanceledOnTouchOutside(canceledOnTouchOutside);
        progress.show();
        sendMsg(progress);
        return progress;
    }

    public static void hide() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
}
