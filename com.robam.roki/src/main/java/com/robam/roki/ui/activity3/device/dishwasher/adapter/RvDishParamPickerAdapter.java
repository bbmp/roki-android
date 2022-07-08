package com.robam.roki.ui.activity3.device.dishwasher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.dishwasher.bean.ParamBean;
import com.robam.roki.ui.mdialog.AppAdapter;

import skin.support.content.res.SkinCompatResources;

/**
 * @author r210190
 * des 模式参数选择
 */
public class RvDishParamPickerAdapter extends AppAdapter<ParamBean.AssistFuncDTO> {

    private int index ;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public RvDishParamPickerAdapter(Context context  ) {
        super(context);
    }


    @Override
    public ParamBean.AssistFuncDTO getItem(int position) {
        return super.getItem(position);
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
            super(R.layout.item_device_param_picker);
            mPickerView = findViewById(R.id.tv_time);
            mUnitTime = findViewById(R.id.tv_unit_time);
        }

        @Override
        public void onBindView(int position) {
            ParamBean.AssistFuncDTO item = getItem(position);
            mPickerView.setText(item.title);
            if (position == index){

                mUnitTime.setVisibility(View.GONE);
                mPickerView.setTextColor(getColor(R.color.text_select_color));
                mUnitTime.setTextColor(getColor(R.color.text_select_color));
            }else {
                mUnitTime.setVisibility(View.GONE);
                mPickerView.setTextColor(getColorSkin(R.color.text_color_gray));
                mUnitTime.setTextColor(getColorSkin(R.color.text_color_gray));
            }
        }
    }


    private int getColorSkin(int resId){
        return SkinCompatResources.getColor(getContext() ,resId) ;
    }
}