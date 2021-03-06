package com.robam.roki.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceMoreBean;


import java.util.List;

/**
 * Created by Dell on 2018/6/1.
 */

public class AbsMoreAdapter extends RecyclerView.Adapter<AbsMoreViewHolder>{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceMoreBean> mDatas;
    private OnItemRecycleClick onItemRecycleClickLister;

    public interface OnItemRecycleClick{
        void onItemClick(View v,int position);
    }

    public void setOnItemRecycleClickLister(OnItemRecycleClick onItemRecycleClickLister){
        this.onItemRecycleClickLister = onItemRecycleClickLister;
    }

    public AbsMoreAdapter(Context context, List<DeviceMoreBean> mData) {
        mContext = context;
        mDatas = mData;
        mInflater = LayoutInflater.from(context);
    }





    @Override
    public AbsMoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_more_page, parent, false);
        AbsMoreViewHolder moreViewHolder = new AbsMoreViewHolder(view);

        return moreViewHolder;
    }

    @Override
    public void onBindViewHolder(AbsMoreViewHolder holder, final int position) {
        if (mDatas != null && mDatas.size() > 0){
            holder.mTvName.setText(mDatas.get(position).getName());
            holder.mItemView.setTag(mDatas.get(position));
            if (mDatas.get(position).getType()==1) {
                holder.mImageView.setImageResource(mDatas.get(position).getImageRes());
            }else{
                Glide.with(mContext).load(mDatas.get(position).getImageUrl()).into(holder.mImageView);
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRecycleClickLister!=null){
                    onItemRecycleClickLister.onItemClick(v,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


}

class AbsMoreViewHolder extends RecyclerView.ViewHolder {


    ImageView mImageView;
    TextView mTvName;
    FrameLayout mItemView;
    TextView text;
    public AbsMoreViewHolder(View itemView) {
        super(itemView);

        mTvName = itemView.findViewById(R.id.tv_model_name);
        mImageView = itemView.findViewById(R.id.iv_icon);
        mItemView = itemView.findViewById(R.id.itemView);
        text = itemView.findViewById(R.id.text);
    }

}
