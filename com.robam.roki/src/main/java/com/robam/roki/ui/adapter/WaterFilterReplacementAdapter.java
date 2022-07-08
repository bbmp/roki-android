package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.model.bean.ReplaceFilterCoreParams;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;


/**
 * Created by 14807 on 2018/11/6.
 */

public class WaterFilterReplacementAdapter extends RecyclerView.Adapter<WaterFilterReplacementViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<ReplaceFilterCoreParams> mCoreParamses;


    public WaterFilterReplacementAdapter(Context context, List<ReplaceFilterCoreParams> coreParamses) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCoreParamses = coreParamses;
    }

    @Override
    public WaterFilterReplacementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_replacement_filter, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        WaterFilterReplacementViewHolder viewHolder = new WaterFilterReplacementViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WaterFilterReplacementViewHolder holder, int position) {
        if (mCoreParamses != null && mCoreParamses.size() > 0) {
            holder.tv_name.setText(mCoreParamses.get(position).getTitle());
            holder.tv_dec.setText(mCoreParamses.get(position).getValue());
        }
    }

    @Override
    public int getItemCount() {
        return mCoreParamses != null ? mCoreParamses.size() : 0;
    }
}

class WaterFilterReplacementViewHolder extends RecyclerView.ViewHolder {

    TextView tv_name;
    TextView tv_dec;

    public WaterFilterReplacementViewHolder(View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_dec = itemView.findViewById(R.id.tv_dec);
    }
}
