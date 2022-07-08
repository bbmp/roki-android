package com.robam.roki.ui.activity3.device.dishwasher.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.dishwasher.bean.CourseBean;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 洗碗机提醒
 */
public class RvDishCourseAdapter extends BaseQuickAdapter<CourseBean.CorseDTO, BaseViewHolder> {


    public RvDishCourseAdapter() {
        super(R.layout.item_dish_course);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, CourseBean.CorseDTO item) {
        if (item != null){
            holder.setText(R.id.tv_desc , item.desc);
            ImageView imageView = (ImageView)holder.getView(R.id.iv_img);
            GlideApp.with(imageView)
                    .load(getImage(item.imageUrl))
                    .into(imageView);
        }
    }


    /**
     * 设置背景图片
     *
     * @param imgName
     */
    private int getImage(String imgName) {
        //设置背景图片
        int resId = getContext().getResources().getIdentifier(imgName, "mipmap", getContext().getPackageName());
        return  resId ;
    }
}
