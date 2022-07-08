package com.robam.roki.ui.activity3.device.dishwasher.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 洗碗机主功能模块adapter
 */
public class RvDishWssherFuntionMainAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private AbsDishWasher dishWasher ;

    /**
     * 更新烟机状态
     * @param dishWasher
     */
    public void potNotify(AbsDishWasher dishWasher){
        dishWasher = dishWasher ;
        notifyDataSetChanged();
    }

    public RvDishWssherFuntionMainAdapter() {
        super(R.layout.item_funtion_main);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if(item != null){
            ImageView ivMode = holder.getView(R.id.iv_mode);
            ivMode.setImageDrawable(DishImageHelper.getResIdUnSelect(item.functionCode));
            holder.setText(R.id.tv_mode_name , item.functionName) ;
            //最后一个item隐藏分割线
            if (getItemPosition(item) == getItemCount() - 1){
                holder.setVisible(R.id.tv_line , false);
            }

        }
    }
}
