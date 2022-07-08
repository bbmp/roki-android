package com.robam.roki.ui.activity3.device.pot.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.model.bean.FanMainParams;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 无人锅主功能模块adapter
 */
public class RvPotFuntionMainAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private Pot pot ;

    /**
     * 更新烟机状态
     * @param pot
     */
    public void potNotify(Pot pot){
        this.pot = pot ;
        notifyDataSetChanged();
    }

    public RvPotFuntionMainAdapter() {
        super(R.layout.item_funtion_main);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if(item != null){
            ImageView ivMode = holder.getView(R.id.iv_mode);
            TextView tvModeName  = holder.getView(R.id.tv_mode_name);
            //电机模式
            short potESPMode = 0;
            if(pot!=null){
                potESPMode = pot.potESPMode;
            }
            if ("quickSustainFried".equals(item.functionCode)){
                //持续快炒
                ivMode.setImageDrawable(potESPMode==1  ? getResId(R.mipmap.icon_continue_fry_blue) : getResId(R.mipmap.icon_continue_fry));
                tvModeName.setTextColor(potESPMode==1  ? getColorSkin(R.color.common_dialog_btn_normal_bg_blue) : getColorSkin(R.color.text_color_device_category));
            }else if ("tenSecondsFry".equals(item.functionCode)){
                //十秒翻炒
                ivMode.setImageDrawable(potESPMode==2  ? getResId(R.mipmap.icon_ten_second_fry_blue) : getResId(R.mipmap.icon_ten_second_fry));
                tvModeName.setTextColor(potESPMode==2  ? getColorSkin(R.color.common_dialog_btn_normal_bg_blue) : getColorSkin(R.color.text_color_device_category));

            }
            holder.setText(R.id.tv_mode_name , item.functionName) ;
            //最后一个item隐藏分割线
            if (getItemPosition(item) == getItemCount() - 1){
                holder.setVisible(R.id.tv_line , false);
            }

        }
    }
    private Drawable getResId(int resId){
        return SkinCompatResources.getInstance().getDrawable(getContext(), resId);
    }
    private int getColorSkin(int resId){
        return SkinCompatResources.getColor(getContext() ,resId) ;
    }
}
