package com.robam.roki.ui.activity3.device.fan.adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.legent.ContextIniter.cx;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.model.bean.FanMainParams;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;


/**
 * @author r210190
 * des 烟机主功能模块adapter
 */
public class RvFuntionMainItemAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder>  {

    private AbsFan fan ;

    /**
     * 更新烟机状态
     * @param absFan
     */
    public void fanNotify(AbsFan absFan){
        fan = absFan ;
        notifyDataSetChanged();
    }

    public RvFuntionMainItemAdapter() {
        super(R.layout.item_funtion_main);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if(item != null){
            ImageView ivMode = holder.getView(R.id.iv_mode);
            FanMainParams fanMainParams = null;
            short level = 0;
            boolean agreement = false ;
            try {
                fanMainParams = JsonUtils.json2Pojo(item.functionParams, FanMainParams.class);
                String param = fanMainParams.getParam().getPower().getValue();
                level = Short.parseShort(param);
                 agreement = level == fan.level;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ("fry".equals(item.functionCode)){
                //爆炒
//                ivMode.setImageResource(agreement  ? R.mipmap.icon_hot_select : R.mipmap.icon_fan_baochao);
                ivMode.setImageDrawable(agreement  ? getResId(R.mipmap.icon_hot_select) : getResId(R.mipmap.icon_fan_baochao));
            }else if ("decoct".equals(item.functionCode)){
                //强档
                ivMode.setImageDrawable(agreement  ? getResId(R.mipmap.icon_strong_select) : getResId(R.mipmap.icon_fan_qiangdang));
            }else if ("stew".equals(item.functionCode)){
                //弱档
                ivMode.setImageDrawable(agreement ?getResId(R.mipmap.icon_weak_select)  : getResId(R.mipmap.icon_fan_ruodang));

            }
            holder.setText(R.id.tv_mode_name , item.functionName) ;
            if (getItemPosition(item) == getItemCount() - 1){
                holder.setVisible(R.id.tv_line , false);
            }

        }
    }

    private Drawable getResId(int resId){
        return SkinCompatResources.getInstance().getDrawable(getContext(), resId);
    }

}
