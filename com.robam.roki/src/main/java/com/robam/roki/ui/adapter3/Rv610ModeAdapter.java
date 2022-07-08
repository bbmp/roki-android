package com.robam.roki.ui.adapter3;

import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;
import org.jetbrains.annotations.NotNull;


/**
 * @author r210190
 * des 610步骤adapter
 */
public class Rv610ModeAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private int oldSelectPosition ;
    private int selectPosition  = 0;


    public Rv610ModeAdapter() {
        super(R.layout.item_device_model);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if (item != null){
            if (holder.getLayoutPosition() == selectPosition){
                ImageUtils.displayImage(item.backgroundImgH, (ImageView) holder.getView(R.id.iv_model));
            }else {
                ImageUtils.displayImage(item.backgroundImg, (ImageView) holder.getView(R.id.iv_model));
            }

            holder.setText(R.id.tv_model_name , item.functionName);
            holder.itemView.setTag(item.functionCode);
        }
    }

    public void setSelectPosition(int selectPosition) {
        this.oldSelectPosition = this.selectPosition ;
        this.selectPosition = selectPosition;
        notifyItemChanged(oldSelectPosition);
        notifyItemChanged(selectPosition);
    }
}
