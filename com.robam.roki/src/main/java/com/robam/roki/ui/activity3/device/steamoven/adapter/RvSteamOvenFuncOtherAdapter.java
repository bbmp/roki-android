package com.robam.roki.ui.activity3.device.steamoven.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.adapter.ImageSelectUtil;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 其他功能模块
 */
public class RvSteamOvenFuncOtherAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private AbsSteameOvenOneNew steamOven ;

    public RvSteamOvenFuncOtherAdapter() {
        super(R.layout.item_funtion_other);
    }
    /**
     * 更新烟机状态
     * @param steamOven
     */
    public void notify(AbsSteameOvenOneNew steamOven){
        this.steamOven = steamOven ;
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
        Log.e("模式",item.functionCode+"---");
        GlideApp.with(getContext()).load( ImageSelectUtil.getResIdSelect(item.functionCode)).into((ImageView) holder.getView(R.id.iv_model_img));

        if (steamOven != null){
            String functionCode = item.functionCode;
            switch (functionCode){
                case FuncCodeKey.KITCHEN:
//
//                    break;
//                case FuncCodeKey.SETLINKAGETIME:
//
//                    break;
//                case FuncCodeKey.TIMEREMINDING:
//
//                    break;
                default:
                    holder.setText(R.id.tv_model_desc , "");
                    break;
            }
        }
    }
}
