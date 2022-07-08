package com.robam.roki.ui.activity3.device.hidkit.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PreferenceUtils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 藏宝盒其他功能模块
 */
public class RvHidKitFuncOtherItemAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {


    public RvHidKitFuncOtherItemAdapter() {
        super(R.layout.item_funtion_other);
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
                case FuncCodeKey.ONLYATHOME:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cbh_znjj));
                    break;
                case FuncCodeKey.STORYVIDEO:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cbh_yyyl));
                    break;
//                case FuncCodeKey.REMINDERS:
//                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cbh_cfzs));
//                    break;
                case FuncCodeKey.KNOWLEDGE:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cbh_cfzs));
                    break;
                case FuncCodeKey.LIFEASSISTANT:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cbh_shzs));
                    break;
                case FuncCodeKey.ENCYCLOPEDIAS:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_cbh_xlbk));
                    break;
                default:
                    holder.setText(R.id.tv_model_desc , "");
                    break;
            }
    }


    private Drawable getResId(int resId){
        return SkinCompatResources.getDrawable(getContext(), resId);
    }

}
