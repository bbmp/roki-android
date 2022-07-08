package com.robam.roki.ui.activity3.device.dishwasher.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.adapter.ImageSelectUtil;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;


/**
 * 设备模式选择adapter
 * 专业模式中模式选择
 */
public class RvDishModeAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    public int selectPosition  = 0;
    public int oldSelectPosition  = 0;


    public RvDishModeAdapter() {
        super(R.layout.item_device_model);
    }


    private static final String TAG = "RvModeAdapter";
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        Log.e(TAG,""+item.functionCode);
        if (item != null){
            Drawable mDrawable=null;
            if (holder.getLayoutPosition() == selectPosition){
                mDrawable= DishImageHelper.getResIdSelect(item.functionCode);
//                GlideApp.with(getContext()).load(resId).into((ImageView) holder.getView(R.id.iv_model));
//                ImageUtils.displayImage(item.backgroundImgH, (ImageView) holder.getView(R.id.iv_model));
            }else {
                mDrawable=DishImageHelper.getResIdUnSelect(item.functionCode);
//                ImageUtils.displayImage(item.backgroundImg, (ImageView) holder.getView(R.id.iv_model));
            }

            GlideApp.with(getContext()).load(mDrawable).into((ImageView) holder.getView(R.id.iv_model));
            holder.setText(R.id.tv_model_name , item.functionName);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectPosition(int selectPosition) {
        oldSelectPosition = this.selectPosition ;
        this.selectPosition = selectPosition;
        notifyItemChanged(oldSelectPosition);
        notifyItemChanged(selectPosition);
    }
}
