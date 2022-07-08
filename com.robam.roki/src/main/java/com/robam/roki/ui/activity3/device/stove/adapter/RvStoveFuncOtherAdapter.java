package com.robam.roki.ui.activity3.device.stove.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 其他功能模块
 */
public class RvStoveFuncOtherAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private Stove stove ;

    public RvStoveFuncOtherAdapter() {
        super(R.layout.item_funtion_other);
    }
    /**
     * 更新烟机状态
     * @param stove
     */
    public void notify(Stove stove){
        this.stove = stove ;
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        ImageView ivMode = holder.getView(R.id.iv_model_img);
        holder.setText(R.id.tv_model_name , item.functionName) ;
        if (getItemPosition(item) == getItemCount() - 1){
            holder.setVisible(R.id.tv_line , false);
        }
            String functionCode = item.functionCode;
            switch (functionCode){
                case FuncCodeKey.AUXISHUTDOWN:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_quick_off_fire));
                    break;
                case FuncCodeKey.TIMEOFF:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_timing_off_fire));
                    break;
                case FuncCodeKey.CURVE:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cure));
                    break;
                case FuncCodeKey.AUTOMOTICCOOKING:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_p_menu));
                    break;
                default:
                    holder.setText(R.id.tv_model_desc , "");
                    break;
            }
        if (stove != null){
        }
    }

    private Drawable getResId(int resId){
        return SkinCompatResources.getDrawable(getContext(), resId);
    }
    private int getColorSkin(int resId){
        return SkinCompatResources.getColor(getContext() ,resId) ;
    }
}
