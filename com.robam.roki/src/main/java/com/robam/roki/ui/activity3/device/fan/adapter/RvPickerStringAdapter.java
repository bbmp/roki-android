package com.robam.roki.ui.activity3.device.fan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.robam.roki.R;
import com.robam.roki.ui.mdialog.AppAdapter;

import skin.support.content.res.SkinCompatResources;

/**
 * @author r210190
 * des 时间选择adapter
 */
public class RvPickerStringAdapter extends AppAdapter<String> {

    private int index ;

    private String unit = "";

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public RvPickerStringAdapter(Context context , String unit) {
        super(context);
        this.unit = unit ;

    }
//    @Override
//    public int getItemCount() {
//        return Integer.MAX_VALUE;
//    }
//
//    @Override
//    public String getItem(int position) {
//        position = position % num ;
//        return super.getItem(position);
//    }
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
            mPickerView.setText(getItem(position)+"");
            if (position == index){
                mUnitTime.setVisibility(View.VISIBLE);
                mUnitTime.setText(unit);
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