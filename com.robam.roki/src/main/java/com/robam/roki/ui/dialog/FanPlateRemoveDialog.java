package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.roki.R;

/**
 * Created by rent on 2016/5/5.
 */
public class FanPlateRemoveDialog extends AbsDialog{
    public FanPlateRemoveDialog(Context context){
        super(context);
    }
    public FanPlateRemoveDialog(Context context, int theme){
        super(context,theme);
    }
    @Override
    protected int getViewResId() {
        return R.layout.dialog_fanplate_remove;
    }

    @Override
    protected void loadView(Context cx) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.loadView(cx);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Window dialogWindow = getWindow();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (dm.widthPixels * 1.0); // 宽度设置为屏幕的0.95
        dialogWindow.setAttributes(p);
    }
}
