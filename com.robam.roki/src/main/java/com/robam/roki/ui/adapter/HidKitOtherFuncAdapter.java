package com.robam.roki.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;

import java.util.List;


/**
 * Created by lixin on 2020/10/19.
 * 主功能区域适配器
 */

public class HidKitOtherFuncAdapter extends RecyclerView.Adapter<HidKitOtherFuncViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mData;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public HidKitOtherFuncAdapter(Context context,
                                  List<DeviceConfigurationFunctions> data,
                                  OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = data;
        this.mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public HidKitOtherFuncViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = mInflater.inflate(R.layout.item_otherfunc_hidkit_page, parent, false);

        HidKitOtherFuncViewHolder hidKitOtherFuncViewHolder = new HidKitOtherFuncViewHolder(view);
        hidKitOtherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnRecyclerViewItemClickListener) {
                    mOnRecyclerViewItemClickListener.onItemClick(v);
                }
            }
        });
        return hidKitOtherFuncViewHolder;
    }

    @Override
    public void onBindViewHolder(HidKitOtherFuncViewHolder holder, int position) {
        if (null != mData && 0 < mData.size()) {
            Glide.with(mContext).load(mData.get(position).backgroundImg).into(holder.mImageView);
            holder.mTvName.setText(mData.get(position).functionName);
            holder.mTvDesc.setText(mData.get(position).msg);
            holder.mItemView.setTag(mData.get(position).functionCode);
            holder.mItemView.setTag(R.id.tag_hid_kit_other_func_key, mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }
}

class HidKitOtherFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvName;
    TextView mTvDesc;
    LinearLayout mItemView;

    public HidKitOtherFuncViewHolder(View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        mTvName = itemView.findViewById(R.id.tv_name);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        mImageView = itemView.findViewById(R.id.iv_view);
        mItemView = itemView.findViewById(R.id.itemView);
    }


}
