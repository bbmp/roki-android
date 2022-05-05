package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;



/**
 * Created by 14807 on 2018/1/24.
 * 主功能区域适配器
 */

public class PotOtherFuncAdapter extends RecyclerView.Adapter<PotOtherFuncViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    Pot mPot;
    List<DeviceConfigurationFunctions> mDatas;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;


    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mPot == null || !Objects.equal(mPot.getID(), event.pojo.getID()))
            return;
    }

    public PotOtherFuncAdapter(Context context, Pot pot, List<DeviceConfigurationFunctions> data,
                               OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mDatas = data;
        mPot = pot;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);
    }

    @Override
    public PotOtherFuncViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = mInflater.inflate(R.layout.item_pot_otherfunc_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        PotOtherFuncViewHolder otherFuncViewHolder = new PotOtherFuncViewHolder(view);
        otherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });
        return otherFuncViewHolder;
    }


    @Override
    public void onBindViewHolder(PotOtherFuncViewHolder holder, int position) {
        LogUtils.i("20181215", "position:" + position);
        if (mDatas != null && mDatas.size() > 0) {

            if ("automatiCooking".equals(mDatas.get(position).functionCode)) {
                Glide.with(mContext)
                        .load(mDatas.get(position).backgroundImg)
//                        .crossFade()
                        .into(holder.mImageView);
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
            } else if ("tobaccoPotLinkage".equals(mDatas.get(position).functionCode)) {
                Glide.with(mContext)
                        .load(mDatas.get(position).backgroundImg)
//                        .crossFade()
                        .into(holder.mImageView);
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
            } else if ("dryBurningWarning".equals(mDatas.get(position).functionCode)) {
                Glide.with(mContext)
                        .load(mDatas.get(position).backgroundImg)
//                        .crossFade()
                        .into(holder.mImageView);
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
            }
            holder.mItemView.setTag(R.id.tag_pot_other_func_key, mDatas.get(position).functionCode);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

class PotOtherFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    LinearLayout mItemView;
    LinearLayout mLlDefaultText;

    public PotOtherFuncViewHolder(View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        mTvName = itemView.findViewById(R.id.tv_name);
        mLlDefaultText = itemView.findViewById(R.id.ll_default_text);
        mImageView = itemView.findViewById(R.id.iv_view);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        mItemView = itemView.findViewById(R.id.itemView);
    }

}
