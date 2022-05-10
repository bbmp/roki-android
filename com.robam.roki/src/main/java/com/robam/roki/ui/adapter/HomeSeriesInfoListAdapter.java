package com.robam.roki.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.SeriesInfo;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.robam.roki.utils.UiUtils.getResources;

/**
 * Created by 14807 on 2018/8/22.
 */

public class HomeSeriesInfoListAdapter extends RecyclerView.Adapter<SeriesInfoListViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<SeriesInfo> mSeriesInfo = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return mOnRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public HomeSeriesInfoListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SeriesInfoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_series_list_page, parent, false);

        SeriesInfoListViewHolder seriesInfoListViewHolder = new SeriesInfoListViewHolder(view);
        seriesInfoListViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecyclerViewItemClickListener != null) {
                    mOnRecyclerViewItemClickListener.onItemClick(v);
                }

            }
        });
        return seriesInfoListViewHolder;
    }

    @Override
    public void onBindViewHolder(final SeriesInfoListViewHolder holder, int position) {

        if (mSeriesInfo != null && mSeriesInfo.size() > 0) {
            Glide.with(mContext).load(mSeriesInfo.get(position).seriesImage)
                    .into(holder.iv_img);
            //圆形
            Glide.with(mContext)
                    .asBitmap()
                    .load(mSeriesInfo.get(position).albumLogo)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.iv_album_logo) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.iv_album_logo.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            holder.tv_name.setText(mSeriesInfo.get(position).seriesName);
            holder.tv_album.setText(mSeriesInfo.get(position).album);
            holder.tv_episode.setText("更新至" + mSeriesInfo.get(position).episode + "集");
            holder.tv_play.setText(String.valueOf(mSeriesInfo.get(position).play));
        }
        holder.mItemView.setTag(mSeriesInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return mSeriesInfo.size() > 0 ? mSeriesInfo.size() : 0;
    }

        //专门更新数据用的方法

    public void updateData(ArrayList<SeriesInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        Iterator<SeriesInfo> iterator = mSeriesInfo.iterator();
        while (iterator.hasNext()){
            iterator.next();
            iterator.remove();
        }
        mSeriesInfo.addAll(list);
        notifyDataSetChanged();
    }

}

class SeriesInfoListViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout mItemView;
    ImageView iv_img;
    ImageView iv_album_logo;
    TextView tv_name;
    TextView tv_episode;
    TextView tv_play;
    TextView tv_album;

    public SeriesInfoListViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView.findViewById(R.id.itemView);
        iv_img = itemView.findViewById(R.id.iv_img);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_episode = itemView.findViewById(R.id.tv_episode);
        tv_play = itemView.findViewById(R.id.tv_play);
        tv_album = itemView.findViewById(R.id.tv_album);
        iv_album_logo = itemView.findViewById(R.id.iv_album_logo);

    }
}
