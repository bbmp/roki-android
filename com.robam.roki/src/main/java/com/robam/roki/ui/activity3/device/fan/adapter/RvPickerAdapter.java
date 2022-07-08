package com.robam.roki.ui.activity3.device.fan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.IDevice;
import com.robam.roki.R;
import com.robam.roki.ui.mdialog.AppAdapter;

import org.jetbrains.annotations.NotNull;

import skin.support.content.res.SkinCompatResources;

/**
 * @author r210190
 * des 时间选择adapter
 */
public class RvPickerAdapter extends AppAdapter<Integer> {

    private int index ;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public RvPickerAdapter(Context context) {
        super(context);
    }
//    @Override
//    public int getItemCount() {
//
//        return Integer.MAX_VALUE;
//    }

    @Override
    public Integer getItem(int position) {
//        position = position % 5 ;
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
            super(R.layout.item_device_picker);
            mPickerView = findViewById(R.id.tv_time);
            mUnitTime = findViewById(R.id.tv_unit_time);
        }

        @Override
        public void onBindView(int position) {
            mPickerView.setText(getItem(position)+"");
            if (position == index){
                mUnitTime.setVisibility(View.VISIBLE);
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