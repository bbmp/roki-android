package com.robam.roki.ui.dialog;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/17.
 */
public class RecipeCompleteDialog extends BlackPromptDialog {
    @InjectView(R.id.dialog_recipe_exit)
    TextView dialog_recipe_exit;

    public RecipeCompleteDialog(Context context) {
        super(context, R.layout.dialog_recipe_complete);
        initView();
    }
    private void initView(){
        ButterKnife.inject(this,contentView);
    }
    public static RecipeCompleteDialog show(Context cx){
        RecipeCompleteDialog dlg = new RecipeCompleteDialog(cx);
        WindowManager wm= (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Window window=dlg.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width=displayMetrics.widthPixels;
        layoutParams.height=(int)(displayMetrics.heightPixels*0.3);
        window.setAttributes(layoutParams);
        return dlg;
    }
    @OnClick(R.id.dialog_recipe_exit)
    public void OnExitClick(){
        if(listener!=null){
            listener.onConfirm(0,null);
        }
    }
    @OnClick(R.id.dialog_recipe_photocooking)
    public void OnPhotoClick(){
        if(listener!=null){
            listener.onConfirm(1,null);
        }
    }
}
