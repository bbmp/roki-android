package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by 14807 on 2018/2/25.
 * 设备更多页面适配器
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mNameDatas;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private int [] mImgs;
    public MoreAdapter(Context context, List<String> nameDatas,int[] imgs,
                         OnRecyclerViewItemClickListener onRecyclerViewItemClickListener ) {
        mContext = context;
        mNameDatas = nameDatas;
        mImgs = imgs;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
    }



    @Override
    public MoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_more_page, parent, false);
        MoreViewHolder moreViewHolder = new MoreViewHolder(view);
        moreViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });
        return moreViewHolder;
    }

    @Override
    public void onBindViewHolder(MoreViewHolder holder, int position) {
        if (mNameDatas != null && mNameDatas.size() > 0){
            holder.mTvName.setText(mNameDatas.get(position));
            holder.mItemView.setTag(mNameDatas.get(position));
        }

        if (mImgs != null && mImgs.length > 0){
            holder.mImageView.setImageResource(mImgs[position]);
        }

    }

    @Override
    public int getItemCount() {
        return mNameDatas.size();
    }


}

class MoreViewHolder extends RecyclerView.ViewHolder {


    ImageView mImageView;
    TextView mTvName;
    FrameLayout mItemView;

    public MoreViewHolder(View itemView) {
        super(itemView);

        mTvName = itemView.findViewById(R.id.tv_model_name);
        mImageView = itemView.findViewById(R.id.iv_icon);
        mItemView = itemView.findViewById(R.id.itemView);
    }

}
