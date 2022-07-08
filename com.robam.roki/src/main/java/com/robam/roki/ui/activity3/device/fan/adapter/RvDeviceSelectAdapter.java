package com.robam.roki.ui.activity3.device.fan.adapter;

import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.IDevice;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author r210190
 * des 设备列表adapter
 */
public class RvDeviceSelectAdapter extends BaseQuickAdapter<IDevice, BaseViewHolder> {
    /**
     * 已经选择的guid
     */
    private  IDevice selectDevice ;

    /**
     * 更新选择的selectGuid
     * @param selectDevice
     */
    public void setSelectGuid(IDevice selectDevice) {
        this.selectDevice = selectDevice;
        notifyDataSetChanged();
    }

    /**
     * 获取选择的guid
     * @return
     */
    public IDevice getSelectGuid() {
        return selectDevice;
    }

    public RvDeviceSelectAdapter() {
        super(R.layout.item_device_select);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, IDevice item) {
        if (item != null) {
            holder.setText(R.id.tv_device_name ,item.getCategoryName() + item.getDispalyType());
            AppCompatCheckBox rbSelect = (AppCompatCheckBox)holder.getView(R.id.rb_select);

            if (selectDevice != null && selectDevice.equals(item)){
                rbSelect.setChecked(true);
            }else {
                rbSelect.setChecked(false);
            }
        }
    }

    OnSelectListener onSelectListener ;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener{
        void onSelect(int position , boolean bool , IDevice device );
    }
}
