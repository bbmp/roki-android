package com.robam.roki.ui.activity3.device.dishwasher.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 其他功能模块
 */
public class RvDishWssherFuntionOtherAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    AbsDishWasher dishWasher ;

    public RvDishWssherFuntionOtherAdapter() {
        super(R.layout.item_dish_func_other);
    }
    /**
     * 更新烟机状态
     * @param dishWasher
     */
    public void potNotify(AbsDishWasher dishWasher){
        this.dishWasher = dishWasher ;
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        RelativeLayout rlBg = holder.getView(R.id.rl_bg);
        rlBg.setBackground(SkinCompatResources.getDrawable(getContext() ,R.drawable.shape_kitchen_bg_round));
        ImageView ivMode = holder.getView(R.id.iv_model_img);
        holder.setText(R.id.tv_model_name , item.functionName) ;
        ivMode.setImageDrawable(DishImageHelper.getResIdUnSelect(item.functionCode));
    }
}
