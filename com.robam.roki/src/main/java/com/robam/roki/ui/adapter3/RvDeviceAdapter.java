package com.robam.roki.ui.adapter3;

import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.IDevice;
import com.robam.common.pojos.RecipeTheme;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des 设备列表adapter
 */
public class RvDeviceAdapter extends BaseQuickAdapter<IDevice, BaseViewHolder> {

    public RvDeviceAdapter() {
        super(R.layout.item_detail3_device);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, IDevice item) {
        if (item != null) {
            holder.setText(R.id.tv_device_name ,item.getCategoryName() + item.getDispalyType())
                    .setText(R.id.tv_on_line, item.isConnected() ? "可使用" : "离线")
                    .setBackgroundResource(R.id.tv_on_line ,  item.isConnected() ? R.drawable.countdown_selector_green : R.drawable.countdown_selector_glay);
            switch (item.getDc()){
                case IDeviceType.RRQZ:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_cooker_online : R.drawable.ic_icon_cooker_outline);
                    break;
                case IDeviceType.RDCZ:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_cooker_online : R.drawable.ic_icon_cooker_outline);
                    break;
                case IDeviceType.RDKX:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_oven_online : R.drawable.ic_icon_oven_online_1_);
                    break;
                case IDeviceType.RZQL:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_steam_oven_online : R.drawable.ic_icon_steam_oven_outline);
                    break;
                case IDeviceType.RWBL:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_microwave_online : R.drawable.ic_icon_microwave_outline);
                    break;
                case IDeviceType.RZKY:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_combi_steam_oven_online : R.drawable.ic_icon_combi_steam_oven_outline);
                    break;
                case IDeviceType.RJSQ:
                    break;
                case IDeviceType.RPOT:
                    break;
                case IDeviceType.RIKA:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_rika_online : R.drawable.ic_icon_rika_online);
                    break;
                default:
                    holder.setImageResource(R.id.iv_device ,item.isConnected() ? R.drawable.ic_icon_cooker_online : R.drawable.ic_icon_cooker_outline);
                    break;

            }

        }
    }
}
