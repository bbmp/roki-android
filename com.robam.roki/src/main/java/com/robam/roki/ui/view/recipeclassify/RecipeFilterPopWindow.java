package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.Map;

/**
 * Created by zhoudingjun on 2017/1/3.
 */

public class RecipeFilterPopWindow extends PopupWindow {
    int width;
    int hight;
    public RecipeFilterPopWindow(Context cx, Map sourceData,Map electricKitchenData, Map flavorData) {
        super(cx);
        View view =new RecipeFilterView(cx,this,sourceData,electricKitchenData,flavorData);
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
       // int screenHeight = wm.getDefaultDisplay().getHeight();
        width=(screenWidth/4)*3;
        this.setWidth(width);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(view);
    }

}
