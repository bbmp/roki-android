package com.robam.roki.ui.activity3.device.hidkit.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.model.bean.HidKitHomeOtherParams;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 藏宝盒其他功能模块
 */
public class RvOtherFuncAdapter extends BaseQuickAdapter<HidKitHomeOtherParams.StepsBean, BaseViewHolder> {


    public RvOtherFuncAdapter() {
        super(R.layout.item_hidkit_other_func);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, HidKitHomeOtherParams.StepsBean item) {
        if (item != null){
            holder.setText(R.id.tv_tag , item.getTag().getValue());
            holder.setText(R.id.tv_tag_name , item.getName().getValue());
            holder.setText(R.id.tv_content , item.getDesc().getValue());
        }
    }

}
