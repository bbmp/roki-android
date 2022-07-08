package com.robam.roki.ui.activity3.device.pot.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PreferenceUtils;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 其他功能模块
 */
public class RvPotFuntionOtherAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    Pot pot ;

    public RvPotFuntionOtherAdapter() {
        super(R.layout.item_funtion_other);
    }
    /**
     * 更新烟机状态
     * @param pot
     */
    public void potNotify(Pot pot){
        this.pot = pot ;
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
                case FuncCodeKey.CURVECOOKING:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cure));
                    break;
                case FuncCodeKey.AUTOCOOKING:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_p_menu));
                    break;
                case FuncCodeKey.FAVORITE:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_my_favourite));
                    break;
                default:
                    holder.setText(R.id.tv_model_desc , "");
                    break;
            }
        if (pot != null){

        }
    }
    private Drawable getResId(int resId){
        return SkinCompatResources.getDrawable(getContext(), resId);
    }
    private int getColorSkin(int resId){
        return SkinCompatResources.getColor(getContext() ,resId) ;
    }
}
