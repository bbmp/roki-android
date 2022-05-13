//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.legent.ui.UIService;
//import com.legent.utils.api.DisplayUtils;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//
///**
// * Created by Administrator on 2016/9/8.
// */
//
//public class WebClientNavigationPage extends WebClientPage {
//    @Override
//    void addNavLeft(Bundle bd) {
//        super.addNavLeft(bd);
//        int flag = bd == null ? 0 : bd.getInt(PageArgumentKey.Flag);
//        switch (flag) {
//            case 1:
//                ImageView img = new ImageView(cx);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DisplayUtils.dip2px(cx, 20),
//                        DisplayUtils.dip2px(cx, 20));
//                layoutParams.setMargins(0,0,DisplayUtils.dip2px(cx,5),0);
//                img.setLayoutParams(layoutParams);
//                img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                img.setImageResource(R.mipmap.ic_recipe_live_rightnav);
//                img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        UIService.getInstance().postPage(PageKey.RecipeLiveList);
//                    }
//                });
//                titleBar.replaceRight(img);
//                break;
//            default:
//                break;
//        }
//    }
//}
