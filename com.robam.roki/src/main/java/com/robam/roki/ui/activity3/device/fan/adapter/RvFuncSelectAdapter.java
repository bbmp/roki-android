package com.robam.roki.ui.activity3.device.fan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.ui.mdialog.AppAdapter;

import skin.support.content.res.SkinCompatResources;

/**
 * @author r210190
 * des 时间选择adapter
 */
public class RvFuncSelectAdapter extends AppAdapter<DeviceConfigurationFunctions> {

    private int index ;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public RvFuncSelectAdapter(Context context) {
        super(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView mPickerView;
        private final TextView mUnitTime;

        ViewHolder() {
            super(R.layout.item_device_picker);
            mPickerView = findViewById(R.id.tv_time);
            mUnitTime = findViewById(R.id.tv_unit_time);
        }

        @Override
        public void onBindView(int position) {
            DeviceConfigurationFunctions item = getItem(position);
            if (item != null){
                mPickerView.setText(item.functionName);
                if (position == index){
                    mUnitTime.setVisibility(View.INVISIBLE);
                    mPickerView.setTextColor(getColorSkin(R.color.text_select_color));
                    mUnitTime.setTextColor(getColorSkin(R.color.text_select_color));
                }else {
                    mUnitTime.setVisibility(View.INVISIBLE);
                    mPickerView.setTextColor(getColorSkin(R.color.text_color_gray));
                    mUnitTime.setTextColor(getColorSkin(R.color.text_color_gray));
                }
            }
        }
    }

    private int getColorSkin(int resId){
        return SkinCompatResources.getColor(getContext() ,resId) ;
    }
}