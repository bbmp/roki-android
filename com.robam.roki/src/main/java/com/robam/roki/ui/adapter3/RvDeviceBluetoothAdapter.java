package com.robam.roki.ui.adapter3;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.IDevice;
import com.robam.common.ui.BleRssiDevice;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author r210190
 * des 设备列表adapter
 */
public class RvDeviceBluetoothAdapter extends BaseQuickAdapter<BleRssiDevice, BaseViewHolder> {

    public RvDeviceBluetoothAdapter() {
        super(R.layout.item_bluetooth_device);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BleRssiDevice item) {
        if (item != null) {
            holder.setImageResource(R.id.iv_device ,R.drawable.icon_906);
            holder.setText(R.id.tv_device_name ,item.getBleName());

        }
    }
}
