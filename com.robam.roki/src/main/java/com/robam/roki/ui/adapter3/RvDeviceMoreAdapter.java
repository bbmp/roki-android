package com.robam.roki.ui.adapter3;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.ui.widget.layout.SettingBar;

import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class RvDeviceMoreAdapter extends BaseQuickAdapter<DeviceMoreBean , BaseViewHolder> {




    public RvDeviceMoreAdapter() {
        super(R.layout.item_more_page);
    }



    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceMoreBean item) {
        SettingBar sbMore = holder.getView(R.id.stb_more);
        sbMore.setLeftText(item.getName());
        sbMore.setTag(item.getType());
        if (getItemPosition(item) == 0){
            sbMore.setRightText(item.getDeviceName());
        }
    }


}
