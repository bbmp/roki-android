package com.robam.roki.ui.activity3.device.fan.adapter;

import static com.legent.ContextIniter.cx;

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
 * des 其他功能模块
 */
public class RvFuntionOtherItemAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    AbsFan fan ;

    public RvFuntionOtherItemAdapter() {
        super(R.layout.item_funtion_other);
    }
    /**
     * 更新烟机状态
     * @param absFan
     */
    public void fanNotify(AbsFan absFan){
        fan = absFan ;
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
                case FuncCodeKey.AUTOPOWER:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_fan_zghx));
                    if (fan != null && fan.cruise == 1) {
                        holder.setImageResource(R.id.iv_model_img , R.drawable.icon_fan_zghx_on);
                        holder.setText(R.id.tv_model_desc , "已开启");
                        holder.setTextColor(R.id.tv_model_desc , getColorSkin(R.color.common_dialog_btn_normal_bg_blue));
                        break;
                    }else {
                        holder.setText(R.id.tv_model_desc , "");
                    }
                    break;
                case FuncCodeKey.OILNETDET:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_fan_ywjc));
                    break;
                case FuncCodeKey.HOLIDAY:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_fan_jrms));
                    break;
                case FuncCodeKey.KITCHEN:
                    //通风换气
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_fan_tfhq));
                    //通风换气
                    if (fan == null || fan.ventilationRemainingTime == 0) {
                        holder.setText(R.id.tv_model_desc , "");
                        return;
                    }
                    String curTime = TimeUtils.secToHourMinSec(fan.ventilationRemainingTime);
                    StringBuilder cleanTime;
                    cleanTime = new StringBuilder(curTime);
//                    cleanTime.append(getContext().getString(R.string.fan_complete_clean)).append(item.functionName);
                    holder.setText(R.id.tv_model_desc , cleanTime.toString());
                    holder.setTextColor(R.id.tv_model_desc , getColorSkin(R.color.common_dialog_btn_normal_bg_blue));
                    holder.setImageResource(R.id.iv_model_img , R.drawable.icon_fan_tfhq_on);
                    break;
                case FuncCodeKey.SETLINKAGETIME:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.drawable.icon_fan_ysgj));
                    //联动时间设置
                    if (fan == null || fan.smartParams == null) {
                        break;
                    }
                    short shutdownDelay = fan.smartParams.ShutdownDelay;
                    holder.setText(R.id.tv_model_desc , shutdownDelay + "分钟");
                    holder.setTextColor(R.id.tv_model_desc , getColorSkin(R.color.common_dialog_btn_normal_bg_blue));
                    holder.setImageResource(R.id.iv_model_img , R.drawable.icon_fan_link_time_on);
                    break;
                case FuncCodeKey.TIMEREMINDING:
                    //计时提醒
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_fan_jstx));
                    long timeReminding = PreferenceUtils.getLong("timeReminding", 0);
                    long l = System.currentTimeMillis();
                    if (l < timeReminding){
                        int l1 = (int)(timeReminding - l) / 1000 ;
                        String s = TimeUtils.secToHourMinSec(l1);
                        holder.setText(R.id.tv_model_desc , s);
                        holder.setTextColor(R.id.tv_model_desc , getColorSkin(R.color.common_dialog_btn_normal_bg_blue));
                        holder.setImageResource(R.id.iv_model_img , R.drawable.icon_fan_jstx_on);
                    }else {
                        holder.setText(R.id.tv_model_desc , "");
                    }
                    break;
                case FuncCodeKey.SMOKELINKAGE:
                    holder.setImageDrawable(R.id.iv_model_img , getResId(R.mipmap.icon_fan_linkage));
                    break;
                default:
                    holder.setText(R.id.tv_model_desc , "");
                    break;
            }
    }


    private Drawable getResId(int resId){
        return SkinCompatResources.getDrawable(getContext(), resId);
    }
    private int getColorSkin(int resId){
        return SkinCompatResources.getColor(getContext() ,resId) ;
    }
}
