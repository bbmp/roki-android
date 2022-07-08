package com.robam.roki.ui.activity3.device.steamoven.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.adapter.ImageSelectUtil;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 一体机主功能模块adapter
 */
public class RvSteamOvenFuncMainAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private AbsSteameOvenOneNew steamOven ;

    /**
     * 更新一体机状态
     * @param steamOven
     */
    public void notify(AbsSteameOvenOneNew steamOven){
        this.steamOven = steamOven ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Math.min(super.getItemCount(), 4);
    }

    public RvSteamOvenFuncMainAdapter() {
        super(R.layout.item_funtion_main);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if(item != null){
            ImageView ivMode = holder.getView(R.id.iv_mode);

//            if ("fry".equals(item.functionCode)){
//                //爆炒
//                ivMode.setImageResource(agreement  ? R.mipmap.icon_hot_select : R.mipmap.icon_hot);
//            }else if ("decoct".equals(item.functionCode)){
//                //强档
//                ivMode.setImageResource(agreement  ? R.mipmap.icon_strong_select : R.mipmap.icon_strong);
//            }else if ("stew".equals(item.functionCode)){
//                //弱档
//                ivMode.setImageResource(agreement ? R.mipmap.icon_weak_select : R.mipmap.icon_weak);
//            }
            GlideApp.with(getContext()).load( ImageSelectUtil.getResIdUnSelect(item.functionCode)).into((ImageView) holder.getView(R.id.iv_mode));


            holder.setText(R.id.tv_mode_name , item.functionName) ;
            //最后一个item隐藏分割线
            if (getItemPosition(item) == getItemCount() - 1){
                holder.setVisible(R.id.tv_line , false);
            }

        }
    }
}
