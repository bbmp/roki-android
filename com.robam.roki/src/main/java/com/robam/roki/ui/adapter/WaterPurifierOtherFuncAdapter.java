package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;


import java.util.List;


/**
 * Created by 14807 on 2018/10/29.
 */

public class WaterPurifierOtherFuncAdapter extends RecyclerView.Adapter<WaterPurifierOtherFuncViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    List<DeviceConfigurationFunctions> mList;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public WaterPurifierOtherFuncAdapter(Context context, List<DeviceConfigurationFunctions> otherFuncList
            , OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mList = otherFuncList;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public WaterPurifierOtherFuncViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_otherfunc_page, parent, false);

        WaterPurifierOtherFuncViewHolder otherFuncViewHolder = new WaterPurifierOtherFuncViewHolder(view);
        otherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });
        return otherFuncViewHolder;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public void onBindViewHolder(WaterPurifierOtherFuncViewHolder holder, int position) {
        if (mList != null && mList.size() > 0) {
            Glide.with(mContext)
                    .load(mList.get(position).backgroundImg)
//                    .crossFade()
                    .into(holder.mImageView);
            holder.mTvName.setText(mList.get(position).functionName);
            holder.mTvDesc.setText(mList.get(position).msg);
        }
        holder.mItemView.setTag(mList.get(position).functionCode);
    }

}

class WaterPurifierOtherFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    LinearLayout mItemView;


    public WaterPurifierOtherFuncViewHolder(View itemView) {
        super(itemView);
        mTvName = itemView.findViewById(R.id.tv_name);
        mImageView = itemView.findViewById(R.id.iv_view);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        mItemView = itemView.findViewById(R.id.itemView);

    }
}

