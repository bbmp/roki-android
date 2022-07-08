package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.VoidCallback;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;

import static com.legent.ContextIniter.cx;

/**
 * Created by rent on 2016/9/30.
 */

public class FullScreenDialog extends Dialog{
    int res;
    CallBack callBack;
    String text;
    View contentView;
    public ImageView oilclean_lock_bg;


    public interface CallBack {
        void OnPositive(View view);

        void OnNegative();
    }

    public FullScreenDialog(Context context, int res, CallBack callBack, String text) {
        super(context, R.style.dialog_bg_aim_aount);
        this.res = res;
        this.callBack = callBack;
        this.text = text;
        initView();
    }

    void initView() {
        contentView = LayoutInflater.from(cx)
                .inflate(res, null, false);
        oilclean_lock_bg = contentView.findViewById(R.id.oilclean_lock_bg);
        setContentView(contentView);
        if (callBack != null){
            callBack.OnPositive(oilclean_lock_bg);
        }
    }

    public static FullScreenDialog dlg;

    public static FullScreenDialog build(Context cx, int res, CallBack callBack, String text) {
        dlg = new FullScreenDialog(cx, res, callBack, text);
//        Window win = dlg.getWindow();
//        win.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager m = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.height = (int) (d.getHeight() * 0.8);
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////        lp.alpha = 0.8f;
//        win.setAttributes(lp);
//        dlg.show();
        return dlg;
    }

}
