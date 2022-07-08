package com.robam.roki.ui.activity3.device.stove.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.model.bean.StoveBackgroundFunParams;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.utils.DeviceJsonToBeanUtils;
import com.robam.roki.utils.StoveLevelUtils;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 灶具主功能模块adapter
 */
public class RvStoveFuncMainAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private Stove stove;

    /**
     * 背景功能区配置参数
     */
    BackgroundFunc mBackgroundFunc;

    public void setBackgroundFunc(BackgroundFunc mBackgroundFunc) {
        this.mBackgroundFunc = mBackgroundFunc;
    }

    /**
     * 更新灶具状态
     *
     * @param stove
     */
    public void notify(Stove stove) {
        this.stove = stove;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Math.min(super.getItemCount(), 4);
    }

    public RvStoveFuncMainAdapter() {
        super(R.layout.item_stove_funtion_main);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if (item != null) {
            //左灶
            if (item.functionCode.contains("left")) {
                holder.setText(R.id.tv_head_name, "左灶");
                if (stove == null || stove.leftHead.status <= 0 || !stove.isConnected()) {
                    holder.setText(R.id.tv_mode_name, "关");
                    holder.setTextColor(R.id.tv_mode_name, getColorSkin(R.color.text_color_device_category));
                } else if (stove.leftHead.level > 0) {
//                    StoveBackgroundFunParams mParamsLeft = DeviceJsonToBeanUtils.JsonToObject(mBackgroundFunc.deviceConfigurationFunctions.get(0).functionParams, StoveBackgroundFunParams.class);
//                    String stoveLeftTemp = StoveLevelUtils.getStovePotTemp(stove.leftTemp, mParamsLeft);
//                    String stoveLefttColor = StoveLevelUtils.getStovePotTextColor(stove.leftTemp, mParamsLeft);
//                    holder.setText(R.id.tv_mode_name, stoveLeftTemp);
//                    holder.setTextColor(R.id.tv_mode_name, Color.parseColor(stoveLefttColor));
                }
            }
            //右灶
            else if (item.functionCode.contains("right")) {
                holder.setText(R.id.tv_head_name, "右灶");
                if (stove == null || stove.rightHead.status <= 0 || !stove.isConnected()) {
                    holder.setText(R.id.tv_mode_name, "关");
                    holder.setTextColor(R.id.tv_mode_name, getColorSkin(R.color.text_color_device_category));
                } else if (stove.rightHead.level > 0) {
//                    StoveBackgroundFunParams mParamsRight = DeviceJsonToBeanUtils.JsonToObject(mBackgroundFunc.deviceConfigurationFunctions.get(1).functionParams, StoveBackgroundFunParams.class);
//                    String stoveRightTemp = StoveLevelUtils.getStovePotTemp(stove.rightTemp, mParamsRight);
//                    String stoveRightColor = StoveLevelUtils.getStovePotTextColor(stove.rightTemp, mParamsRight);
//                    holder.setText(R.id.tv_mode_name,"F"+stove.rightHead.level+ stoveRightTemp);
//                    holder.setTextColor(R.id.tv_mode_name, Color.parseColor(stoveRightColor));
                }
            }

            //最后一个item隐藏分割线
            if (getItemPosition(item) == getItemCount() - 1) {
                holder.setVisible(R.id.tv_line, false);
            }

        }
    }

    private int getColorSkin(int resId) {
        return SkinCompatResources.getColor(getContext(), resId);
    }
    private Drawable getResId(int resId){
        return SkinCompatResources.getDrawable(getContext(), resId);
    }
}
