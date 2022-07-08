package com.robam.roki.ui.adapter3;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author r210190
 * des 设备列表adapter
 */
public class RvDeviceBluetoothAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder> {

    public RvDeviceBluetoothAdapter() {
        super(R.layout.item_bluetooth_device);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BleDevice item) {
        if (item != null) {
//            ImageView iv_device = holder.getView(R.id.iv_device);
//            GlideApp.with(getContext())
//                    .load(item.iconUrl)
//                    .error(R.drawable.icon_906)
//                    .into(iv_device);
            String name = item.getName().substring(6,11);
            if(name.contains("DB620")){
                holder.setImageResource(R.id.iv_device ,R.mipmap.bm_db620);
            }else if(name.contains("CQ925")){
                holder.setImageResource(R.id.iv_device ,R.mipmap.bm_cq925);
            }else if(name.contains("RC906")){
                holder.setImageResource(R.id.iv_device ,R.mipmap.bm_rc906);
            }else if(name.contains("KM310")){
                holder.setImageResource(R.id.iv_device ,R.mipmap.bm_km310);
            }else if(name.contains("920")){
                holder.setImageResource(R.id.iv_device ,R.mipmap.device_920_image);
            }
//            holder.setImageResource(R.id.iv_device ,R.drawable.icon_906);
//            holder.setText(R.id.tv_device_name ,item.getName());
            holder.setText(R.id.tv_device_name ,name);

        }
    }
}
