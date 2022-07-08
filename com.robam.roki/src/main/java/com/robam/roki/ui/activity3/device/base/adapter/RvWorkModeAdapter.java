package com.robam.roki.ui.activity3.device.base.adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.StringUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.adapter.bean.WorkModeBean;

import org.jetbrains.annotations.NotNull;


/**
 * 工作中顶部数据显示
 */
public class RvWorkModeAdapter extends BaseQuickAdapter<WorkModeBean, BaseViewHolder> {

    public int selectPosition = 0;


    public RvWorkModeAdapter() {
        super(R.layout.item_work_mode);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, WorkModeBean item) {
        if (item != null) {
            holder.setText(R.id.tv_key, item.key);
            holder.setText(R.id.tv_value, item.value);
            holder.setText(R.id.tv_aux, item.aux);
            if (StringUtils.isBlank(item.unit)) {
                holder.setVisible(R.id.tv_temp_unit, false);
                holder.setVisible(R.id.tv_time_unit, false);
            } else if ("℃".equals(item.unit)) {
                holder.setVisible(R.id.tv_temp_unit, true);
                holder.setVisible(R.id.tv_time_unit, false);
            } else if ("min".equals(item.unit)) {
                holder.setVisible(R.id.tv_temp_unit, false);
                holder.setVisible(R.id.tv_time_unit, true);
            }else {
                holder.setVisible(R.id.tv_temp_unit, false);
                holder.setVisible(R.id.tv_time_unit, false);
            }
        }
    }

}
