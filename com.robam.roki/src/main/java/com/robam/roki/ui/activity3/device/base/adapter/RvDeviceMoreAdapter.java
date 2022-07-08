package com.robam.roki.ui.activity3.device.base.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.IDevice;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.widget.layout.SettingBar;
import com.robam.roki.utils.StringUtil;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 设备更多adapter
 */
public class RvDeviceMoreAdapter extends BaseQuickAdapter<String , BaseViewHolder> {


    IDevice device ;

    public void setDevice(IDevice device) {
        this.device = device;
        notifyDataSetChanged();
    }

    public RvDeviceMoreAdapter() {
        super(R.layout.item_device_more);
    }



    @Override
    protected void convert(@NotNull BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_more , item);
        if ("设备名称".equals(item) && device != null){
            holder.setText(R.id.tv_more_desc , StringUtil.isBlank(device.getName()) ?
                    device.getDispalyType()
                    : device.getName());
        }
        if (getItemPosition(item) == getItemCount() - 1){
            holder.setVisible(R.id.tv_line , false);
        }
    }


}
